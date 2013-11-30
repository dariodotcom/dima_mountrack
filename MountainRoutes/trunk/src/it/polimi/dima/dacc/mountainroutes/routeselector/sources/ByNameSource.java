package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroute.commons.connector.StorageClient;
import it.polimi.dima.dacc.mountainroute.commons.connector.StorageClient.ResultCallback;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.ByNameQuery;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.Query;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.QueryResult;
import it.polimi.dima.dacc.mountainroute.commons.types.RouteDescriptionList;
import android.text.Editable;

public class ByNameSource implements RouteSource {

	private Editable searchTerm;

	public ByNameSource(Editable searchTerm) {
		this.searchTerm = searchTerm;
	}

	@Override
	public void loadRoutes(final ResultObserver observer) {
		String currentSearchTerm = searchTerm.toString();

		if (currentSearchTerm.length() == 0) {
			observer.onResultReceived(null);
			return;
		}

		observer.onLoadStart();
		
		Query q = new ByNameQuery(currentSearchTerm);
		new StorageClient(new ResultCallback() {

			@Override
			public void onResult(QueryResult result) {
				observer.onResultReceived(result.as(RouteDescriptionList.class));
			}
		}).execute(q);
	}

}

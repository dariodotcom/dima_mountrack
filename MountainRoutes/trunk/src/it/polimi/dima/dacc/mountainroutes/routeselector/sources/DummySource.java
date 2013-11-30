package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroute.commons.connector.StorageClient;
import it.polimi.dima.dacc.mountainroute.commons.connector.StorageClient.ResultCallback;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.AvailableRoutesQuery;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.Query;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.QueryResult;
import it.polimi.dima.dacc.mountainroute.commons.types.RouteDescriptionList;

public class DummySource implements RouteSource {

	@Override
	public void loadRoutes(final ResultObserver observer) {
		observer.onLoadStart();
		Query q = new AvailableRoutesQuery();
		new StorageClient(new ResultCallback() {

			@Override
			public void onResult(QueryResult result) {
				observer.onResultReceived(result.as(RouteDescriptionList.class));
			}
		}).execute(q);
	}
}

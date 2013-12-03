package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroutes.contentloader.ContentLoader;
import it.polimi.dima.dacc.mountainroutes.contentloader.ContentQuery;
import it.polimi.dima.dacc.mountainroutes.contentloader.ContentQuery.QueryType;
import android.content.Context;
import android.text.Editable;

public class ByNameSource implements RouteSource {

	private Editable searchTerm;
	private Context context;

	public ByNameSource(Editable searchTerm, Context context) {
		this.searchTerm = searchTerm;
		this.context = context;
	}

	@Override
	public void loadRoutes(final ResultObserver observer) {
		String currentSearchTerm = searchTerm.toString();

		if (currentSearchTerm.length() == 0) {
			observer.onResultReceived(null);
			return;
		}

		observer.onLoadStart();

		LoaderObserverAdapter adapter = new LoaderObserverAdapter(observer);
		ContentQuery q = new ContentQuery(QueryType.BYNAME);
		q.getParams().putString(ContentLoader.NAME_PARAM, currentSearchTerm);
		ContentLoader.getInstance().createConnector(context, adapter)
				.execute(q);
	}

}

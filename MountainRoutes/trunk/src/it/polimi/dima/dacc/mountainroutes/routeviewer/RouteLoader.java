package it.polimi.dima.dacc.mountainroutes.routeviewer;

import it.polimi.dima.dacc.mountainroutes.commons.connector.StorageClient;
import it.polimi.dima.dacc.mountainroutes.commons.connector.StorageClient.ResultCallback;
import it.polimi.dima.dacc.mountainroutes.commons.connector.query.Query;
import it.polimi.dima.dacc.mountainroutes.commons.connector.query.QueryResult;
import it.polimi.dima.dacc.mountainroutes.commons.connector.query.RouteQuery;
import it.polimi.dima.dacc.mountainroutes.commons.types.Route;
import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescription;

public class RouteLoader {

	private LoaderCallback callback;

	public RouteLoader(LoaderCallback callback) {
		this.callback = callback;
	}

	public void loadRoute(RouteDescription desc) {
		String id = desc.getId();

		if (id == null) {
			callback.onLoadError();
			return;
		}

		Query q = new RouteQuery(id);
		new StorageClient(new ResultCallback() {
			@Override
			public void onResult(QueryResult result) {
				Route r = result.as(Route.class);
				callback.onLoadResult(r);
			}
		}).execute(q);
	}

	public static interface LoaderCallback {
		public void onLoadError();

		public void onLoadResult(Route route);

		public void onLoadStart();
	}
}

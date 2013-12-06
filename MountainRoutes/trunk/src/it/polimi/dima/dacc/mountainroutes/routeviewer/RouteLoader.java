package it.polimi.dima.dacc.mountainroutes.routeviewer;

import android.content.Context;
import android.util.Log;
import it.polimi.dima.dacc.mountainroutes.remotecontent.ContentConnector;
import it.polimi.dima.dacc.mountainroutes.remotecontent.LoadError;
import it.polimi.dima.dacc.mountainroutes.remotecontent.ContentLoader;
import it.polimi.dima.dacc.mountainroutes.remotecontent.ContentQuery;
import it.polimi.dima.dacc.mountainroutes.remotecontent.LoaderObserver;
import it.polimi.dima.dacc.mountainroutes.remotecontent.LoaderResult;
import it.polimi.dima.dacc.mountainroutes.remotecontent.ContentQuery.QueryType;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;

public class RouteLoader {

	private Callback callback;
	private Context context;

	public RouteLoader(Context context, Callback callback) {
		this.callback = callback;
		this.context = context;
	}

	public void loadRoute(RouteID id) {
		ContentQuery query = new ContentQuery(QueryType.ID);
		query.getParams().putString(ContentLoader.ID_PARAM, id.getRouteID());
		ContentConnector connector = ContentLoader.getInstance()
				.createConnector(context);
		connector.executeQuery(query, new LoaderObserverAdapter(callback));
		Log.d("loader","request started");
	}

	private static class LoaderObserverAdapter implements LoaderObserver {

		private Callback callback;

		public LoaderObserverAdapter(Callback callback) {
			this.callback = callback;
		}

		@Override
		public void onLoadStart() {
			callback.onLoadStart();
		}

		@Override
		public void onLoadError(LoadError error) {
			callback.onLoadError(error);
		}

		@Override
		public void onLoadResult(LoaderResult result) {
			callback.onLoadResult(result.getResult(Route.class));
		}
	}

	public static interface Callback {
		public void onLoadError(LoadError error);

		public void onLoadResult(Route route);

		public void onLoadStart();
	}
}

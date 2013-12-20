package it.polimi.dima.dacc.mountainroutes.routeviewer;

import android.content.Context;
import android.util.Log;

import it.polimi.dima.dacc.mountainroutes.loader.GenericLoader;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.route.RoutePersistence;
import it.polimi.dima.dacc.mountainroutes.remote.RemoteContentConnector;
import it.polimi.dima.dacc.mountainroutes.remote.RemoteContentManager;
import it.polimi.dima.dacc.mountainroutes.remote.ContentQuery;
import it.polimi.dima.dacc.mountainroutes.remote.ContentQuery.QueryType;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;

public class RouteLoader extends GenericLoader<Route> {

	private final static String TAG = "route-loader";
	private RouteID id;

	public RouteLoader(Context context, RouteID id) {
		super(context);
		if (id == null) {
			throw new IllegalArgumentException("Route ID must not be null");
		}

		this.id = id;
	}

	@Override
	protected void onReleaseResources(LoadResult<Route> result) {
		// Route does not need to be released
	}

	@Override
	public LoadResult<Route> loadInBackground() {
		Context context = getContext();

		// Try to load route from the persistence
		RoutePersistence persistence = RoutePersistence.create(context);

		try {
			Route route = persistence.loadRoute(id);
			if (route != null) {
				return new LoadResult<Route>(route);
			}
		} catch (PersistenceException e) {
			// if an exception was caught, the persistence has some corrupted
			// data.
			Log.e(TAG, "Persistence exception - ", e);
		}

		// Route was not found in persistence, load from remote
		RemoteContentConnector connector = RemoteContentManager.getInstance()
				.createConnector(context);
		ContentQuery query = new ContentQuery(QueryType.ID);
		query.getParams().putString(RemoteContentManager.ID_PARAM,
				id.getRouteID());

		return connector.executeQuery(query, Route.class);
	}

	public static class Factory {
		private final Context context;
		private final RouteID id;

		public Factory(Context context, RouteID id) {
			if (id == null) {
				throw new IllegalArgumentException("Route ID must not be null");
			}

			this.context = context;
			this.id = id;
		}
		
		public RouteLoader createLoader(){
			return new RouteLoader(context, id);
		}
	}
}

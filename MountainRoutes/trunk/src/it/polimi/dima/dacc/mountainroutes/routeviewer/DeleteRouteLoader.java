package it.polimi.dima.dacc.mountainroutes.routeviewer;

import android.content.Context;
import android.util.Log;
import it.polimi.dima.dacc.mountainroutes.loader.GenericLoader;
import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.RoutePersistence;
import it.polimi.dima.dacc.mountainroutes.types.Route;

public class DeleteRouteLoader extends GenericLoader<Route> {

	private Route routeToDelete;

	public DeleteRouteLoader(Context context, Route route) {
		super(context);
		this.routeToDelete = route;
	}

	@Override
	protected void onReleaseResources(LoadResult<Route> result) {

	}

	@Override
	public LoadResult<Route> loadInBackground() {

		RoutePersistence persistence = RoutePersistence.create(getContext());
		try {
			persistence.removeRoute(routeToDelete.getId());
			return new LoadResult<Route>((Route) null);
		} catch (PersistenceException e) {
			Log.d("route-save-loader", "Received exception: ", e);
			return new LoadResult<Route>(LoadError.INTERNAL_ERROR);
		}
	}
}

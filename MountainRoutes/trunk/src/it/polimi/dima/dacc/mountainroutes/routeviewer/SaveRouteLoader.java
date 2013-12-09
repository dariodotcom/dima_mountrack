package it.polimi.dima.dacc.mountainroutes.routeviewer;

import android.content.Context;
import android.util.Log;
import it.polimi.dima.dacc.mountainroutes.loader.GenericLoader;
import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.RoutePersistence;
import it.polimi.dima.dacc.mountainroutes.types.Route;

public class SaveRouteLoader extends GenericLoader<Route> {

	private Route routeToSave;

	public SaveRouteLoader(Context context, Route route) {
		super(context);
		this.routeToSave = route;
	}

	@Override
	protected void onReleaseResources(LoadResult<Route> result) {

	}

	@Override
	public LoadResult<Route> loadInBackground() {

		RoutePersistence persistence = RoutePersistence.create(getContext());
		try {
			persistence.persistRoute(routeToSave);
			return new LoadResult<Route>((Route) null);
		} catch (PersistenceException e) {
			Log.d("route-save-loader", "Received exception: ", e);
			return new LoadResult<Route>(LoadError.INTERNAL_ERROR);
		}

	}

}

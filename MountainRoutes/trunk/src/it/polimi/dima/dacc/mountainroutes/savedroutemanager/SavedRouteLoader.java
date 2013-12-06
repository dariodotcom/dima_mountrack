package it.polimi.dima.dacc.mountainroutes.savedroutemanager;

import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.RoutePersistence;
import it.polimi.dima.dacc.mountainroutes.remotecontent.LoadError;
import it.polimi.dima.dacc.mountainroutes.remotecontent.LoadResult;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class SavedRouteLoader extends
		AsyncTaskLoader<LoadResult<RouteSummaryList>> {

	public SavedRouteLoader(Context context) {
		super(context);
	}
	
	@Override
	public LoadResult<RouteSummaryList> loadInBackground() {
		Context c = getContext();
		RoutePersistence persistence = RoutePersistence.create(c);

		try {
			RouteSummaryList result = persistence.getAvailableRoutes();
			return new LoadResult<RouteSummaryList>(result);
		} catch (PersistenceException e) {
			Log.e("saved-route-loader", "Error retrieving saved routes:", e);
			return new LoadResult<RouteSummaryList>(LoadError.INTERNAL_ERROR);
		}
	}
}

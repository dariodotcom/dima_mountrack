package it.polimi.dima.dacc.mountainroutes.savedroutemanager;

import android.content.Context;
import android.util.Log;
import it.polimi.dima.dacc.mountainroutes.loader.GenericLoader;
import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.route.RoutePersistence;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

/**
 * Loader that deletes a {@link Route} from the database and returns the list of available Routes.
 * @author Chiara
 *
 */
public class SavedRouteDeleteLoader extends GenericLoader<RouteSummaryList> {

	private final static String TAG = "SavedRouteDeleteLoader";
	private RouteID id;

	public SavedRouteDeleteLoader(Context context, RouteID id) {
		super(context);
		this.id = id;
	}

	@Override
	protected void onReleaseResources(LoadResult<RouteSummaryList> result) {

	}

	@Override
	public LoadResult<RouteSummaryList> loadInBackground() {
		RoutePersistence persistence = RoutePersistence.create(getContext());

		// Delete route
		try {
			persistence.removeRoute(id);
		} catch (PersistenceException e) {
			Log.e(TAG, "Exception deleting route " + id, e);
			return new LoadResult<RouteSummaryList>(LoadError.INTERNAL_ERROR);
		}

		// Return new routes
		try {
			RouteSummaryList result = persistence.getAvailableRoutes();
			return new LoadResult<RouteSummaryList>(result);
		} catch (PersistenceException e) {
			Log.e(TAG, "Exception loading new route list");
			return new LoadResult<RouteSummaryList>(LoadError.INTERNAL_ERROR);
		}		
	}

}

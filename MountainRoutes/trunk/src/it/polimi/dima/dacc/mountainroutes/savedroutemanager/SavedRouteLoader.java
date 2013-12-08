package it.polimi.dima.dacc.mountainroutes.savedroutemanager;

import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.RoutePersistence;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class SavedRouteLoader extends
		AsyncTaskLoader<LoadResult<RouteSummaryList>> {

	private final static String TAG = "saved-route-loader";

	private LoadResult<RouteSummaryList> result;

	public SavedRouteLoader(Context context) {
		super(context);
	}

	@Override
	public LoadResult<RouteSummaryList> loadInBackground() {
		Context c = getContext();
		RoutePersistence persistence = RoutePersistence.create(c);

		Log.d(TAG, "loadInBackground started");

		try {
			RouteSummaryList result = persistence.getAvailableRoutes();
			return new LoadResult<RouteSummaryList>(result);
		} catch (PersistenceException e) {
			Log.e("saved-route-loader", "Error retrieving saved routes:", e);
			return new LoadResult<RouteSummaryList>(LoadError.INTERNAL_ERROR);
		}
	}

	@Override
	public void deliverResult(LoadResult<RouteSummaryList> data) {
		if (isReset()) {
			if (this.result != null) {
				onReleaseResources(this.result);
			}
		}

		LoadResult<RouteSummaryList> oldResult = this.result;
		this.result = data;

		if (isStarted()) {
			super.deliverResult(result);
		}

		if (oldResult != null) {
			onReleaseResources(oldResult);
		}
	}

	private void onReleaseResources(LoadResult<RouteSummaryList> data) {
		
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();

		if (result != null) {
			deliverResult(result);
		}

		if (result == null || takeContentChanged()) {
			forceLoad();
		}
	}

	@Override
	protected void onReset() {
		// TODO Auto-generated method stub
		super.onReset();
	}

	@Override
	protected void onStopLoading() {
		// TODO Auto-generated method stub
		super.onStopLoading();
	}

	@Override
	public void onCanceled(LoadResult<RouteSummaryList> data) {
		// TODO Auto-generated method stub
		super.onCanceled(data);
	}
}

package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class NearMeSummaryListLoader extends SummaryListLoader {

	private final static String TAG = "near-me-source";

	private Context context;

	public NearMeSummaryListLoader(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public LoadResult<RouteSummaryList> loadInBackground() {

		// Create location updater
		LocationUpdater updater;
		try {
			updater = new LocationUpdater(context, this);
		} catch (IllegalStateException e) {
			return new LoadResult<RouteSummaryList>(LoadError.INTERNAL_ERROR);
		}

		new Thread(updater).start();
		
		Log.d(TAG, "Going to sleep!");
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				return new LoadResult<RouteSummaryList>(
						LoadError.INTERNAL_ERROR);
			}
		}

		Log.d(TAG, "I'm awaken");
		LatLng location = updater.getLocation();
		Log.d(TAG, "Location = " + location);
		return new LoadResult<RouteSummaryList>(LoadError.INTERNAL_ERROR);
	}
}

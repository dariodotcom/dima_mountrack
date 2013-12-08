package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.remote.RemoteContentConnector;
import it.polimi.dima.dacc.mountainroutes.remote.RemoteContentManager;
import it.polimi.dima.dacc.mountainroutes.remote.ContentQuery;
import it.polimi.dima.dacc.mountainroutes.remote.ContentQuery.QueryType;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
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

		// Get position
		String svcName = Context.LOCATION_SERVICE;
		String pvdName = LocationManager.GPS_PROVIDER;
		LocationManager locMan = (LocationManager) context
				.getSystemService(svcName);

		// Notify the user if the provider is disabled
		if (!locMan.isProviderEnabled(pvdName)) {
			Log.d(TAG, "GPS provider is disabled");
			return new LoadResult<RouteSummaryList>(LoadError.GPS_DISABLED);
		}

		LocationListenerImpl locListener = new LocationListenerImpl(this);
		Looper.prepare();

		locMan.requestSingleUpdate(pvdName, locListener, null);

		return new LoadResult<RouteSummaryList>(LoadError.INTERNAL_ERROR);
		
//		LatLng location = locListener.getLocation();
//		while (location == null) {
//			// We'll have to wait;
//			try {
//				Thread.sleep(250);
//				location = locListener.getLocation();
//			} catch (InterruptedException e) {
//				Log.d(TAG, "Error while waiting", e);
//				return new LoadResult<RouteSummaryList>(
//						LoadError.INTERNAL_ERROR);
//			}
//		}
//
//		ContentQuery query = new ContentQuery(QueryType.NEAR);
//		query.getParams().putParcelable(RemoteContentManager.POSITION_PARAM,
//				location);
//		RemoteContentConnector connector = RemoteContentManager.getInstance()
//				.createConnector(context);
//		return connector.executeQuery(query, RouteSummaryList.class);
	}

	private static class LocationListenerImpl implements LocationListener {

		private LatLng location;
		private NearMeSummaryListLoader loader;

		public LocationListenerImpl(NearMeSummaryListLoader loader) {
			this.loader = loader;
		}

		@Override
		public void onLocationChanged(Location l) {
			// Get LatLng
			double lat = l.getLatitude(), lng = l.getLongitude();
			location = new LatLng(lat, lng);
			Log.d(TAG, "location: " + location);
			loader.notify();
		}

		public LatLng getLocation() {
			return location;
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}
	}
}

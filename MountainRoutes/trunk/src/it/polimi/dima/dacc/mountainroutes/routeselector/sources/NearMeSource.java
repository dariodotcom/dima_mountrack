package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroutes.remotecontent.ContentConnector;
import it.polimi.dima.dacc.mountainroutes.remotecontent.LoadError;
import it.polimi.dima.dacc.mountainroutes.remotecontent.ContentLoader;
import it.polimi.dima.dacc.mountainroutes.remotecontent.ContentQuery;
import it.polimi.dima.dacc.mountainroutes.remotecontent.ContentQuery.QueryType;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class NearMeSource implements RouteSource {

	private final static String TAG = "near-me-source"; 
	
	private ContentConnector connector;
	private Context context;

	public NearMeSource(Context context) {
		this.context = context;
		this.connector = ContentLoader.getInstance().createConnector(context);
	}

	@Override
	public void loadRoutes(ResultObserver observer) {
		if (observer == null) {
			return;
		}

		// Get position
		String svcName = Context.LOCATION_SERVICE;
		String pvdName = LocationManager.GPS_PROVIDER;
		LocationManager locMan = (LocationManager) context
				.getSystemService(svcName);

		// Notify the user if the provider is disabled
		if (!locMan.isProviderEnabled(pvdName)) {
			Log.d(TAG, "GPS provider is disabled");
			observer.onError(LoadError.GPS_DISABLED);
		}

		observer.onLoadStart();
		
		// Register for updates
		LocationListenerImpl listener = new LocationListenerImpl(observer);
		locMan.requestSingleUpdate(pvdName, listener, null);
	}

	private class LocationListenerImpl implements LocationListener {

		private ResultObserver observer;

		public LocationListenerImpl(ResultObserver observer) {
			this.observer = observer;
		}

		@Override
		public void onLocationChanged(Location l) {
			// Get LatLng
			double lat = l.getLatitude(), lng = l.getLongitude();
			LatLng location = new LatLng(lat, lng);

			// Create query
			ContentQuery query = new ContentQuery(QueryType.NEAR);
			Bundle params = query.getParams();
			params.putParcelable(ContentLoader.POSITION_PARAM, location);

			// Start loader
			connector.executeQuery(query, new LoaderObserverAdapter(observer));
		}

		@Override
		public void onProviderDisabled(String arg0) {
		}

		@Override
		public void onProviderEnabled(String arg0) {
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		}
	}
}

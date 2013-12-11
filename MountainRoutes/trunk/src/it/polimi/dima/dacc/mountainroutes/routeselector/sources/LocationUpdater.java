package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class LocationUpdater implements Runnable {

	private final static String TAG = "location-updater";

	private LatLng location;
	private NearMeLoader loaderToUpdate;
	private LocationManager locMan;

	public LocationUpdater(Context context, NearMeLoader loader) {
		this.loaderToUpdate = loader;

		// Get location manager
		String svcName = Context.LOCATION_SERVICE;
		locMan = (LocationManager) context.getSystemService(svcName);

		String pvdName = LocationManager.GPS_PROVIDER;
		if (!locMan.isProviderEnabled(pvdName)) {
			throw new IllegalStateException("Provider disabled");
		}
	}

	@Override
	public void run() {
		Looper.prepare();

		// Register location updater
		String pvdName = LocationManager.GPS_PROVIDER;
		locMan.requestSingleUpdate(pvdName, locListenerImpl, Looper.myLooper());
		
		// Start looping
		Looper.loop();
	}

	public LatLng getLocation() {
		return location;
	}

	private LocationListener locListenerImpl = new LocationListener() {

		@Override
		public void onLocationChanged(Location l) {
			// Get location
			double lat = l.getLatitude(), lng = l.getLongitude();
			location = new LatLng(lat, lng);
			Log.d(TAG, "location: " + location);

			// Notify loader that current location is available
			synchronized (loaderToUpdate) {
				loaderToUpdate.notifyAll();
			}

			// Exit from looper
			Looper.myLooper().quit();
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
	};

}

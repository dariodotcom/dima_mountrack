package it.polimi.dima.dacc.mountainroutes.commons;

import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class LocationLoader {

	private final static String TAG = "LocationLoader";

	private Object lock;
	private LocationManager locMan;

	public LocationLoader(Context context) {
		// Get location manager
		lock = new Object();
		String svcName = Context.LOCATION_SERVICE;
		locMan = (LocationManager) context.getSystemService(svcName);
	}

	public LoadResult<LatLng> getLocation() {

		if (!locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return new LoadResult<LatLng>(LoadError.GPS_DISABLED);
		}

		LocationLoaderRunnable runnable = new LocationLoaderRunnable();
		new Thread(runnable).run();

		synchronized (lock) {
			if (runnable.getLocation() == null) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					Log.e(TAG, "Exception while waiting", e);
					return null;
				}
			}
		}

		LatLng location = runnable.getLocation();

		Log.d(TAG, "location: " + location);
		return new LoadResult<LatLng>(location);
	}

	private class LocationLoaderRunnable implements Runnable, LocationListener {

		private LatLng location;

		public LatLng getLocation() {
			return location;
		}

		@Override
		public void run() {
			Looper.prepare();

			// Register location updater
			String pvdName = LocationManager.GPS_PROVIDER;
			locMan.requestSingleUpdate(pvdName, this, Looper.myLooper());

			// Start looping
			Looper.loop();
		}

		@Override
		public void onLocationChanged(Location l) {
			// Get location
			double lat = l.getLatitude(), lng = l.getLongitude();
			location = new LatLng(lat, lng);

			synchronized (lock) {
				lock.notifyAll();
			}

			// Exit from looper
			locMan.removeUpdates(this);
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
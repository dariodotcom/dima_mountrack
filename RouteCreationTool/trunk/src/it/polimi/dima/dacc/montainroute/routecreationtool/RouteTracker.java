package it.polimi.dima.dacc.montainroute.routecreationtool;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class RouteTracker {

	private RouteTrackerUpdateListener listener;
	private boolean updateUI;
	private List<LatLng> pendingUpdates;

	private LocationManager locManager;
	private int minUpdateTimeMillis;
	private int minUpdateDistanceMeters;

	public RouteTracker(Context context, RouteTrackerUpdateListener listener) {
		this.listener = listener;
		this.updateUI = true;
		this.pendingUpdates = new ArrayList<LatLng>();

		// TODO load from res
		minUpdateTimeMillis = 10000;
		minUpdateDistanceMeters = 20;
		String svcName = Context.LOCATION_SERVICE;
		locManager = (LocationManager) context.getSystemService(svcName);
	}

	public void startTracking() {
		String provider = LocationManager.GPS_PROVIDER;
		locManager.requestLocationUpdates(provider, minUpdateTimeMillis,
				minUpdateDistanceMeters, locationListener);
	}

	public void stopTracking() {
		locManager.removeUpdates(locationListener);
	};

	public void pauseUpdates() {
		Log.d("ROUTE_TRACKER", "Updates paused.");
		updateUI = false;
	}

	public List<LatLng> resumeUpdates() {
		updateUI = true;
		List<LatLng> updates = pendingUpdates;
		pendingUpdates = new ArrayList<LatLng>();

		Log.d("ROUTE_TRACKER", "Pending updates delivered.");
		return updates;
	}

	private final LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onLocationChanged(Location loc) {
			LatLng point = new LatLng(loc.getLatitude(), loc.getLongitude());

			Log.d("ROUTE_TRACKER", "New location: " + point);

			if (updateUI) {
				Log.d("ROUTE_TRACKER", "UI Updated");
				listener.onPointTracked(point);
			} else {
				Log.d("ROUTE_TRACKER", "Update queued");
				queuePendingUpdate(point);
			}

		}
	};

	private void queuePendingUpdate(LatLng point) {
		pendingUpdates.add(point);
	}

}

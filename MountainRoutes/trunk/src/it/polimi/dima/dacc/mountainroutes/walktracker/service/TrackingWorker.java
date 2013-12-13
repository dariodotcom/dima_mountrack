package it.polimi.dima.dacc.mountainroutes.walktracker.service;

import com.google.android.gms.maps.model.LatLng;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Runnable that implements the tracking of user location.
 */
public class TrackingWorker implements Runnable, LocationListener {

	private final static String TAG = "tracking worker";
	private final static int MIN_DISTANCE_METERS = 50;
	private final static int MIN_TIME_MILLIS = 5000;

	private Context context;
	private LocationManager locMan;

	private Route trackingRoute;
	private Looper looper;
	private boolean isTracking;
	private float completionIndex;

	public TrackingWorker(Context context) {
		this.context = context;
	}

	public boolean isTracking() {
		return isTracking;
	}

	public synchronized void startTracking(Route route) {
		if (isTracking) {
			throw new IllegalStateException("Already tracking " + trackingRoute);
		}

		// Start worker
		new Thread(this).start();

		// Wait for the worker to be ready
		try {
			wait();
		} catch (InterruptedException e) {
			Log.e(TAG, "Interrupted exception - ", e);
		}

		// Register location listener
		String svcName = Context.LOCATION_SERVICE;
		String pvdName = LocationManager.GPS_PROVIDER;
		locMan = (LocationManager) context.getSystemService(svcName);
		locMan.requestLocationUpdates(pvdName, MIN_TIME_MILLIS,
				MIN_DISTANCE_METERS, this, looper);

		// Send start broadcast
		Intent i = BroadcastFactory.createTrackingStartBroadcast(route);
		sendBroadcast(i);

		Log.d(TAG, "Now tracking route " + route);
	}

	public void stopTracking() {
		locMan.removeUpdates(this); // Remove location listener
		looper.quit(); // Stop tracking worker
		isTracking = false;

		// Send stop broadcast
		Intent i = BroadcastFactory.createTrackingStopBroadcast(null);
		sendBroadcast(i);

		Log.d(TAG, "Tracking worker stopped");

		return;
	}

	@Override
	public void run() {
		Looper.prepare();
		this.looper = Looper.myLooper();
		synchronized (this) {
			notifyAll();
		}
		Looper.loop();
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "location: " + location.toString());

		if (!isTracking) {
			Log.d(TAG, "Worker not tracking");
			return;
		}

		LatLng position = latLngFrom(location);
		float newCompletionIndex;

		try {
			newCompletionIndex = computeCompletionIndex(position);
		} catch (DepartedFromRouteException e) {
			UpdateType update = UpdateType.FAR_FROM_ROUTE;
			Intent i = BroadcastFactory.createStatusBroadcast(update);
			sendBroadcast(i);
			return;
		}

		if (newCompletionIndex < completionIndex) {
			UpdateType update = UpdateType.GOING_BACKWARDS;
			Intent intent = BroadcastFactory.createStatusBroadcast(update);
			sendBroadcast(intent);
			return;
		}

		completionIndex = newCompletionIndex;
		Intent intent = BroadcastFactory
				.createTrackingUpdateBroadcast(completionIndex);
		sendBroadcast(intent);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(TAG, "status changed: " + provider + ", status = " + status);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(TAG, "provider enabled, " + provider);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(TAG, "provider disabled, " + provider);
	}

	// Helper methods called from location listener methods, thus running on the
	// tracking thread;
	private float computeCompletionIndex(LatLng point)
			throws DepartedFromRouteException {
		// TODO find out how to compute completion index
		return 0.0f;
	}

	private LatLng latLngFrom(Location l) {
		return new LatLng(l.getLatitude(), l.getLongitude());
	}

	private void sendBroadcast(Intent intent) {
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

}
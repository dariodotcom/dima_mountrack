package it.polimi.dima.dacc.mountainroutes.walktracker.service;

import java.util.Arrays;

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
 * Component that tracks the user position through data received from GPS and
 * sends updates to registered broadcast. A worker can be in three states:
 * <ul>
 * <li>READY: before <code>startTracking()</code>. The worker is actually not
 * running.</li>
 * <li>TRACKING: after <code>startTracking()</code> or <code>resume()</code>
 * called. The worker is running and registered as location updates listener.
 * Updates are sent to registered listeners.</li>
 * <li>PAUSED: after <code>pause()</code> is called. Worker is running but not
 * updating its report.</li>
 * <li>FINALIZED: after the worker has detected that the last point of the path
 * has been reached or <code>quit()</code> has been called. The worker is not
 * running, not registered as location listener and will not send any more
 * broadcasts.</li>
 * </ul>
 */
public class TrackingWorker implements Runnable, LocationListener {

	private final static String TAG = "tracking worker";
	private final static int MIN_DISTANCE_METERS = 50;
	private final static int MIN_TIME_MILLIS = 5000;

	private static enum State {
		READY, TRACKING, PAUSED, FINALIZED
	}

	private Context context;
	private LocationManager locMan;
	private State currentState;

	private Route trackingRoute;
	private ExcursionReport report;
	private Looper looper;

	/**
	 * Constructs a new {@link TrackingWorker} that operates in a given
	 * {@link Context}
	 * 
	 * @param context
	 *            - the context the {@link TrackingWorker} is being created in.
	 */
	public TrackingWorker(Context context) {
		this.context = context;
		this.currentState = State.READY;
	}

	/**
	 * Gives the tracking state
	 * 
	 * @return true iff a route is being tracked.
	 */
	public boolean isTracking() {
		return currentState == State.TRACKING || currentState == State.PAUSED;
	}

	/**
	 * Starts tracking a given {@link Route}. This implies:
	 * <ul>
	 * <li>Starting the tracking thread</li>
	 * <li>Registering a location updates listener</li>
	 * </ul>
	 * 
	 * @param route
	 *            - the route to track.
	 * @throws NullPointerException
	 *             if route is null.
	 * @throws IllegalStateException
	 *             if another route is already being tracked.
	 */
	public synchronized void startTracking(Route route) {
		if (route == null) {
			throw new NullPointerException("Route must not be null");
		}

		assertState(State.READY);

		this.trackingRoute = route;
		this.report = new ExcursionReport(route.getId());

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
		currentState = State.TRACKING;
		Intent i = BroadcastFactory.createTrackingStartBroadcast(route);
		sendBroadcast(i);

		Log.d(TAG, "Now tracking route " + route);
	}

	/**
	 * Pauses the worker.
	 */
	public synchronized void pause() {
		assertState(State.TRACKING);
		currentState = State.PAUSED;
		Intent i = BroadcastFactory
				.createStatusBroadcast(UpdateType.EXCURSION_PAUSED);
		sendBroadcast(i);
	}

	/**
	 * Resumes the worker
	 * */
	public synchronized void resume() {
		assertState(State.PAUSED);
		currentState = State.TRACKING;
		Intent i = BroadcastFactory
				.createStatusBroadcast(UpdateType.EXCURSION_RESUME);
		sendBroadcast(i);
	}

	/**
	 * Quits the worker. If the worker is in TRACKING state all operations will
	 * be stopped, including
	 * <ul>
	 * <li>Stopping the tracking thread</li>
	 * <li>Unregistering the location updates listener</li>
	 * </ul>
	 * 
	 * and a FORCE_QUIT update will be sent to all registered listener. </ul> If
	 * the worker is in READY or FINALIZED nothing will happen.
	 */
	public synchronized void quit() {
		if (currentState == State.READY || currentState == State.FINALIZED) {
			// The worker is not tracking so quit silently as it is not holding
			// any resource
			Log.d(TAG, "Quit silently");
			return;
		}

		locMan.removeUpdates(this); // Remove location listener
		looper.quit(); // Stop tracking worker and its thread
		currentState = State.FINALIZED;

		// Send stop broadcast
		Intent i = BroadcastFactory
				.createStatusBroadcast(UpdateType.FORCE_QUIT);
		sendBroadcast(i);

		Log.d(TAG, "Tracking worker was forced to stop");
	}

	// Tracking implementation
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

		if (!isTracking()) {
			Log.d(TAG, "Worker not tracking");
			return;
		}

		LatLng position = latLngFrom(location);

		if (currentState == State.TRACKING) {
			handleUpdateInTracking(position);
		} else {
			handleUpdateInPause(position);
		}
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
	private void handleUpdateInTracking(LatLng point) {
		float newCompletionIndex;

		try {
			newCompletionIndex = computeCompletionIndex(point);
		} catch (DepartedFromRouteException e) {
			UpdateType update = UpdateType.FAR_FROM_ROUTE;
			Intent i = BroadcastFactory.createStatusBroadcast(update);
			sendBroadcast(i);
			return;
		}

		float completionIndex = report.getCompletionIndex();

		if (newCompletionIndex < completionIndex) {
			UpdateType update = UpdateType.GOING_BACKWARDS;
			Intent intent = BroadcastFactory.createStatusBroadcast(update);
			sendBroadcast(intent);
			return;
		}

		report.setCompletionIndex(newCompletionIndex);
		Intent intent = BroadcastFactory
				.createTrackingUpdateBroadcast(newCompletionIndex);
		sendBroadcast(intent);
	}

	private void handleUpdateInPause(LatLng point) {

	}

	private float computeCompletionIndex(LatLng point)
			throws DepartedFromRouteException {
		// TODO find out how to compute completion index
		return (float) (point.latitude + point.longitude);
	}

	private LatLng latLngFrom(Location l) {
		return new LatLng(l.getLatitude(), l.getLongitude());
	}

	private void sendBroadcast(Intent intent) {
		Log.d(TAG, "sending broadcast: " + intent);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	private void assertState(State... state) {
		boolean found = false;
		;
		for (int i = 0; i < state.length; i++) {
			if (state[i] == currentState) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new IllegalStateException("Expected worker state of "
					+ Arrays.toString(state) + " but current state is "
					+ currentState);
		}
	}

}
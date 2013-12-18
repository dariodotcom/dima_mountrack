package it.polimi.dima.dacc.mountainroutes;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListenerManager;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService.TrackingControl;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.RouteWalkFragment;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.TimerView;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;

public class WalkingActivity extends Activity implements ServiceConnection {

	public static final String TRACKING_ROUTE = "TRACKING_ROUTE";
	private static final String TRACKING_INITIALIZED = "tracking_initialized";

	private RouteWalkFragment walkFragment;
	private TrackerListenerManager trackMan;
	private TimerView timerView;
	private TrackingControl control;

	private boolean trackingInitialized;
	private Route routeToTrack;

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_walking);

		walkFragment = (RouteWalkFragment) getFragmentManager()
				.findFragmentById(R.id.walking_map);
		timerView = (TimerView) findViewById(R.id.timer_view);

		trackMan = TrackerListenerManager.inject(this);

		trackingInitialized = savedState == null ? false : savedState
				.getBoolean(TRACKING_INITIALIZED);

		if (!trackingInitialized) {
			if (savedState != null) {
				routeToTrack = savedState.getParcelable(TRACKING_ROUTE);
			} else if (getIntent() != null) {
				routeToTrack = getIntent().getParcelableExtra(TRACKING_ROUTE);
			}

			if (routeToTrack == null) {
				throw new IllegalStateException(
						"Service not initialized but route to track not available");
			}
		}

		Intent i = new Intent(this, TrackingService.class);
		startService(i);
		bindService(i, this, BIND_AUTO_CREATE);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(TRACKING_INITIALIZED, trackingInitialized);
		if (routeToTrack != null) {
			outState.putParcelable(TRACKING_ROUTE, routeToTrack);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		trackMan.registerListener(timerView);
		trackMan.registerListener(walkFragment);
		trackMan.registerListener(logger);
	}

	@Override
	protected void onStop() {
		super.onStop();
		trackMan.unregisterListener(timerView);
		trackMan.unregisterListener(walkFragment);
		trackMan.unregisterListener(logger);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		TrackerListenerManager.clear(this);
		unbindService(this);
		Log.d("WalkingActivity", "destroyed!");
		trackMan = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.waking, menu);
		return true;
	}

	// Service connection
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		control = (TrackingControl) service;

		if (!trackingInitialized) {
			trackingInitialized = true;
			control.startTracking(routeToTrack);
			routeToTrack = null;
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		control = null;
	}

	private TrackerListener logger = new TrackerListener() {

		private final static String TAG = "ServiceListener";

		@Override
		public void onTrackingUpdate(TrackResult result) {
			Log.d(TAG, "tracking update: " + result);
		}

		@Override
		public void onStopTracking(ExcursionReport report) {
			Log.d(TAG, "stop tracking:" + report);
		}

		@Override
		public void onStatusUpdate(UpdateType update) {
			Log.d(TAG, "update: " + update);
		}

		@Override
		public void onStartTracking(Route route) {
			Log.d(TAG, "onStartTracking: " + route);
		}

		@Override
		public void onUnregister(LaggardBackup backup) {
			Log.d(TAG, "unregistered!");
		}

		@Override
		public void onRegister(LaggardBackup backup) {
			Log.d(TAG, "registered! am i late? " + backup.amILate());
		}
	};
}

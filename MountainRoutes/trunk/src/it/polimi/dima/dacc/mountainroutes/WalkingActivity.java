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
import it.polimi.dima.dacc.mountainroutes.walktracker.views.PauseResumeButton;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.RouteWalkFragment;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.TimerView;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.TrackingControlWrapper;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
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
	private TrackingControlWrapper controlWrapper;
	private PauseResumeButton pauseResumeButton;

	private boolean trackingInitialized = false;
	private boolean finalizeOnDestroy = false;
	private Route routeToTrack;

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_walking);

		// Load UI elements
		walkFragment = (RouteWalkFragment) getFragmentManager()
				.findFragmentById(R.id.walking_map);
		timerView = (TimerView) findViewById(R.id.timer_view);
		pauseResumeButton = (PauseResumeButton) findViewById(R.id.pause_resume_button);

		// Load components
		trackMan = TrackerListenerManager.instantiate(this);
		controlWrapper = new TrackingControlWrapper();
		pauseResumeButton.attachToControl(controlWrapper);

		// Load state
		if (savedState != null) {
			trackingInitialized = savedState.getBoolean(TRACKING_INITIALIZED);
		}

		if (!trackingInitialized) {
			if (savedState != null) {
				routeToTrack = savedState.getParcelable(TRACKING_ROUTE);
			} else if (getIntent() != null) {
				routeToTrack = getIntent().getParcelableExtra(TRACKING_ROUTE);
			}

			if (routeToTrack == null) {
				String msg = "Service not initialized but route to track not available";
				throw new IllegalStateException(msg);
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
	protected void onResume() {
		super.onResume();
		trackMan.registerListener(timerView);
		trackMan.registerListener(walkFragment);
		trackMan.registerListener(logger);
		trackMan.registerListener(pauseResumeButton);
	}

	@Override
	protected void onPause() {
		super.onPause();
		trackMan.unregisterListener(timerView);
		trackMan.unregisterListener(walkFragment);
		trackMan.unregisterListener(logger);
		trackMan.unregisterListener(pauseResumeButton);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.waking, menu);
		return true;
	}

	// Service connection
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		TrackingControl control = (TrackingControl) service;
		controlWrapper.setControl(control);

		if (!trackingInitialized) {
			trackingInitialized = true;
			control.startTracking(routeToTrack);
			routeToTrack = null;
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		controlWrapper.setControl(null);
	}

	@Override
	public void onBackPressed() {
		confirmAndQuit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (finalizeOnDestroy) {
			controlWrapper.stop(); // Stop service
			trackMan = null;
			TrackerListenerManager.unload(); // Unload manager
		}

		unbindService(this); // detach from service
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

	private void confirmAndQuit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("message")
				.setCancelable(false)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finalizeOnDestroy = true;
								WalkingActivity.super.onBackPressed();
							}
						}).setNegativeButton(android.R.string.cancel, null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}

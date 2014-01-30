package it.polimi.dima.dacc.mountainroutes.walktracker;

import java.util.ArrayList;
import java.util.List;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.commons.RouteProgressionMapFragment;
import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.report.ReportPersistence;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListenerManager;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService.TrackingControl;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.AltitudeView;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.ElapsedMeters;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.MissingTimeView;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.NotificationView;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.NotificationsEmitter;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.PauseResumeButton;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.RouteProgressionController;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.TimerView;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.TrackingControlWrapper;
import android.os.Bundle;
import android.os.IBinder;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WalkingActivity extends FragmentActivity implements ServiceConnection, TrackerListener {

	public static final String TRACKING_ROUTE = "TRACKING_ROUTE";
	private static final String TRACKING_INITIALIZED = "tracking_initialized";
	public static final String WALKING_REPORT = "walking_report";
	private static final String TAG = "WalkingActivity";

	private RouteProgressionMapFragment walkFragment;
	private List<TrackerListener> listeners;
	private TrackerListenerManager trackMan;
	private TrackingControlWrapper serviceControlWrapper;
	private NotificationsEmitter emitter;

	private String quitMessage;

	private boolean trackingInitialized = false;
	private Route routeToTrack;

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_walking);

		// Create components
		trackMan = TrackerListenerManager.getManager(this);
		emitter = new NotificationsEmitter(this);
		serviceControlWrapper = new TrackingControlWrapper();

		// Load UI elements
		listeners = new ArrayList<TrackerListener>();

		listeners.add((TimerView) findViewById(R.id.timer_view));
		listeners.add((MissingTimeView) findViewById(R.id.time_to_arrive_value));
		listeners.add((ElapsedMeters) findViewById(R.id.elapsed_meters_view_fragment));
		listeners.add((AltitudeView) findViewById(R.id.altitude_view));
		listeners.add((NotificationView) findViewById(R.id.notificationBar));

		walkFragment = (RouteProgressionMapFragment) getFragmentManager().findFragmentById(R.id.walking_map);
		listeners.add(new RouteProgressionController(walkFragment));

		PauseResumeButton pauseResumeButton = (PauseResumeButton) findViewById(R.id.pause_resume_button);
		listeners.add(pauseResumeButton);
		pauseResumeButton.attachToControl(serviceControlWrapper);

		listeners.add(this);

		// Buttons
		Button endWalk = (Button) findViewById(R.id.end_walk);
		Button panButton = (Button) findViewById(R.id.button_pan);
		Button zoomButton = (Button) findViewById(R.id.button_zoom);

		endWalk.setOnClickListener(quitButtonListener);
		panButton.setOnClickListener(panButtonListener);
		zoomButton.setOnClickListener(zoomButtonListener);

		// Load Strings
		quitMessage = getResources().getString(R.string.walking_quit_notify);

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

		emitter.dismiss();
		for (TrackerListener listener : listeners) {
			trackMan.registerListener(listener);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (trackMan.isAlive()) {
			emitter.turnOn();
			for (TrackerListener listener : listeners) {
				trackMan.unregisterListener(listener);
			}
		}
	}

	@Override
	protected void onDestroy() {
		// Detach this activity from service
		unbindService(this);
		super.onDestroy();
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
		serviceControlWrapper.setControl(control);

		if (!trackingInitialized) {
			trackingInitialized = true;
			control.startTracking(routeToTrack);
			routeToTrack = null;
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		serviceControlWrapper.setControl(null);
	}

	// Helpers
	private View.OnClickListener quitButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			assureUserWantsToQuit();
		}
	};

	private OnClickListener panButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			walkFragment.panToPath();
		}
	};

	private OnClickListener zoomButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			walkFragment.zoomToUser();
		}
	};

	@Override
	public void onBackPressed() {
		assureUserWantsToQuit();
	}

	private void assureUserWantsToQuit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(quitMessage).setCancelable(false).setPositiveButton(android.R.string.yes, quitter)
				.setNegativeButton(android.R.string.cancel, null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private DialogInterface.OnClickListener quitter = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			terminateWalk(null);
		}
	};

	@Override
	public void onStartTracking(Route route) {
	}

	@Override
	public void onStopTracking(ExcursionReport report) {
		terminateWalk(report);
	}

	private void terminateWalk(ExcursionReport report) {
		if (report != null) {
			Intent i = new Intent();
			i.putExtra(WALKING_REPORT, report);

			try {
				ReportPersistence.create(this).persistExcursionReport(report);
			} catch (PersistenceException e) {
				Log.e(TAG, "Error peristing report", e);
			}

			setResult(RESULT_OK, i);
		} else {
			setResult(RESULT_CANCELED);
		}

		TrackerListenerManager.unload();
		serviceControlWrapper.stop();
		finish();
	}

	@Override
	public void onStatusUpdate(UpdateType update) {
		if (update == UpdateType.FORCE_QUIT) {
			terminateWalk(null);
		}
	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
	}

	@Override
	public void onRegister(LaggardBackup backup) {
		switch (backup.getStatus()) {
		case ABRUPTED:
			terminateWalk(null);
			break;
		case FINISHED:
			terminateWalk(backup.getReport());
			break;
		default:
			break;
		}
	}

	@Override
	public void onUnregister(LaggardBackup backup) {
	}

	@Override
	public void onAltitudeGapUpdate(int altitude) {
	}
}

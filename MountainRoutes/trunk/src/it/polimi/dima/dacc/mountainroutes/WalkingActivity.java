package it.polimi.dima.dacc.mountainroutes;

import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListenerManager;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService.TrackingControl;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.RouteWalkFragment;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.TimerView;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;

public class WalkingActivity extends Activity implements ServiceConnection {

	public static final String TRACKING_ROUTE = "TRACKING_ROUTE";

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
		if (routeToTrack != null) {
			outState.putParcelable(TRACKING_ROUTE, routeToTrack);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (trackMan != null) {
			trackMan.registerListener(walkFragment);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (trackMan != null) {
			trackMan.unregisterListener(walkFragment);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waking, menu);
		return true;
	}

	// Service connection
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		control = (TrackingControl) service;
		LaggardBackup backup = control.getLaggardBackup();
		trackMan = TrackerListenerManager.create(this, backup);
		trackMan.registerListener(walkFragment);
		trackMan.unregisterListener(timerView);

		control.startTracking(routeToTrack);
		routeToTrack = null;

		if (!trackingInitialized) {
			trackingInitialized = true;
			control.startTracking(routeToTrack);
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		control = null;
		trackMan.unregisterListener(walkFragment);
		trackMan.unregisterListener(timerView);
		trackMan = null;
	}
}

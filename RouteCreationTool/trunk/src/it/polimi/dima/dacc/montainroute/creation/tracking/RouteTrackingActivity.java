package it.polimi.dima.dacc.montainroute.creation.tracking;


import com.google.android.gms.maps.model.LatLng;

import it.polimi.dima.dacc.montainroute.creation.R;
import it.polimi.dima.dacc.mountainroute.commons.presentation.StaticRouteFragment;
import it.polimi.dima.dacc.mountainroute.commons.types.PointList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RouteTrackingActivity extends Activity implements
		RouteTrackerUpdateListener {

	private static final String POINT_LIST = "POINT_LIST";
	public static final String RESULT_KEY = "RESULT_KEY";

	private PointList trackedPoints;
	private RouteTracker tracker;
	private StaticRouteFragment fragment;

	private final OnClickListener endActivity = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent result = new Intent().putExtra(RESULT_KEY, trackedPoints);
			setResult(RESULT_OK, result);
			tracker.stopTracking();
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_tracking);

		this.fragment = (StaticRouteFragment) getFragmentManager()
				.findFragmentById(R.id.static_route);

		if (savedInstanceState == null) {
			trackedPoints = new PointList();
			fragment.setPath(trackedPoints);
		} else {
			trackedPoints = (PointList) savedInstanceState
					.getParcelable(POINT_LIST);
			fragment.setPath(trackedPoints);
			fragment.notifyPathChanged();
		}

		Button stopTrackingButton = (Button) findViewById(R.id.button_stop_tracking);
		stopTrackingButton.setOnClickListener(endActivity);

		// Start tracking user position
		tracker = new RouteTracker(this, this);
		tracker.startTracking();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route_tracking, menu);
		return true;
	}

	@Override
	public void onPointTracked(LatLng point) {
		trackedPoints.add(point);
		fragment.notifyPathChanged();
	}

	@Override
	protected void onPause() {
		super.onPause();
		tracker.pauseUpdates();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!tracker.isUpdatingUI()) {
			PointList updates = tracker.resumeUpdates();
			this.trackedPoints.addAll(updates);
			this.fragment.notifyPathChanged();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(POINT_LIST, trackedPoints);
	}

}

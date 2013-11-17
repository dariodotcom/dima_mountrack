package it.polimi.dima.dacc.montainroute.creation.tracking;

import it.polimi.dima.dacc.montainroute.creation.R;
import it.polimi.dima.dacc.montainroute.creation.TrackedPoints;
import it.polimi.dima.dacc.mountainroute.commons.types.Point;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class RouteTrackingActivity extends Activity implements
		RouteTrackerUpdateListener {

	private static final String POINT_LIST = "POINT_LIST";
	public static final String RESULT_KEY = "RESULT_KEY";

	private TrackedPoints trackedPoints;
	private PointArrayAdapter pointAdapter;
	private RouteTracker tracker;

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

		if (savedInstanceState == null) {
			trackedPoints = new TrackedPoints();
		} else {
			trackedPoints = (TrackedPoints) savedInstanceState
					.getSerializable(POINT_LIST);
		}

		Button stopTrackingButton = (Button) findViewById(R.id.button_stop_tracking);
		stopTrackingButton.setOnClickListener(endActivity);

		// Append adapter to list view
		ListView listView = (ListView) findViewById(R.id.point_listview);
		int listViewElemId = android.R.layout.simple_list_item_1;
		pointAdapter = new PointArrayAdapter(this, listViewElemId,
				trackedPoints.getPoints());
		listView.setAdapter(pointAdapter);

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
	public void onPointTracked(Point point) {
		trackedPoints.add(point);
		pointAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		super.onPause();
		tracker.pauseUpdates();
	}

	@Override
	protected void onResume() {
		super.onResume();
		trackedPoints.addAll(tracker.resumeUpdates());
		pointAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(POINT_LIST, trackedPoints);
	}

}

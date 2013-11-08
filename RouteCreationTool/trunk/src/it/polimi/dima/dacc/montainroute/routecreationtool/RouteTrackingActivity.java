package it.polimi.dima.dacc.montainroute.routecreationtool;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RouteTrackingActivity extends Activity implements
		RouteTrackerUpdateListener {

	private static final String POINT_LIST = "POINT_LIST";
	public static final String RESULT_KEY = "RESULT_KEY";

	private ArrayList<LatLng> pointList;
	private ArrayAdapter<LatLng> pointAdapter;
	private RouteTracker tracker;

	private final OnClickListener endActivity = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent result = new Intent().putExtra(RESULT_KEY, pointList);
			setResult(RESULT_OK, result);
			tracker.stopTracking();
			finish();
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_tracking);

		if (savedInstanceState == null) {
			pointList = new ArrayList<LatLng>();
		} else {
			pointList = (ArrayList<LatLng>) savedInstanceState
					.getSerializable(POINT_LIST);
		}

		Button stopTrackingButton = (Button) findViewById(R.id.button_stop_tracking);
		stopTrackingButton.setOnClickListener(endActivity);

		// Append adapter to list view
		ListView listView = (ListView) findViewById(R.id.point_listview);
		int listViewElemId = android.R.layout.simple_list_item_1;
		pointAdapter = new LatLngArrayAdapter(this, listViewElemId, pointList);
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
	public void onPointTracked(LatLng point) {
		pointList.add(point);
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
		pointList.addAll(tracker.resumeUpdates());
		pointAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(POINT_LIST, pointList);
	}

}

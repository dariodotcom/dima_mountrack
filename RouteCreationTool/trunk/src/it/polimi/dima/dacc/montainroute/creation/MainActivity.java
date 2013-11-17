package it.polimi.dima.dacc.montainroute.creation;

import it.polimi.dima.dacc.montainroute.creation.saver.RouteSaverActivity;
import it.polimi.dima.dacc.montainroute.creation.tracking.RouteTrackingActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewSwitcher;

public class MainActivity extends Activity {

	private static final int TRACKING_ACTIVITY = 0x01;
	private static final int SAVING_ACTIVITY = 0x02;

	private static final String TRACKED_POINTS = "TRACKED_POINTS";

	private ViewSwitcher switcher;
	private TrackedPoints trackedPoints;

	private OnClickListener startListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this,
					RouteTrackingActivity.class);
			startActivityForResult(i, TRACKING_ACTIVITY);
		}
	};

	private OnClickListener saveListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this, RouteSaverActivity.class);
			i.putExtra(RouteSaverActivity.TRACKED_POINTS_KEY, trackedPoints);
			startActivityForResult(i, SAVING_ACTIVITY);
		}
	};

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_main);

		if (savedState != null) {
			this.trackedPoints = (TrackedPoints) savedState
					.getSerializable(TRACKED_POINTS);
		}

		switcher = (ViewSwitcher) findViewById(R.id.main_switcher);

		Button startTrackingButton = (Button) findViewById(R.id.button_start_tracking);
		startTrackingButton.setOnClickListener(startListener);

		Button restartTrackingButton = (Button) findViewById(R.id.restart_button);
		restartTrackingButton.setOnClickListener(startListener);

		Button saveButton = (Button) findViewById(R.id.save_button);
		saveButton.setOnClickListener(saveListener);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(TRACKED_POINTS, trackedPoints);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case TRACKING_ACTIVITY:
			if (resultCode != RESULT_OK) {
				break;
			}

			trackedPoints = (TrackedPoints) data
					.getSerializableExtra(RouteTrackingActivity.RESULT_KEY);

			switcher.showNext();
			break;
		case SAVING_ACTIVITY:
			if (resultCode != RESULT_OK) {
				break;
			}

			switcher.showPrevious();

			// Route has been saved
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package it.polimi.dima.dacc.montainroute.creation;

import it.polimi.dima.dacc.montainroute.creation.saver.RouteSaverActivity;
import it.polimi.dima.dacc.montainroute.creation.tracking.RouteTrackingActivity;
import it.polimi.dima.dacc.mountainroute.commons.types.PointList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private static final int TRACKING_ACTIVITY = 0x01;

	private OnClickListener startListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this,
					RouteTrackingActivity.class);
			startActivityForResult(i, TRACKING_ACTIVITY);
		}
	};

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_main);

		Button startTrackingButton = (Button) findViewById(R.id.button_start_tracking);
		startTrackingButton.setOnClickListener(startListener);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == TRACKING_ACTIVITY && resultCode == RESULT_OK) {
			PointList points = (PointList) data
					.getParcelableExtra(RouteTrackingActivity.RESULT_KEY);
			Log.d("main-activity", "Result: " + points);
			Intent intent = new Intent(this, RouteSaverActivity.class);
			intent.putExtra(RouteSaverActivity.TRACKED_POINTS_KEY, points);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

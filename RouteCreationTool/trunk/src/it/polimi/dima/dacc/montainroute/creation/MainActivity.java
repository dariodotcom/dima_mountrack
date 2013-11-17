package it.polimi.dima.dacc.montainroute.creation;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import it.polimi.dima.dacc.montainroute.creation.R;
import it.polimi.dima.dacc.montainroute.creation.saver.RouteSaverActivity;
import it.polimi.dima.dacc.montainroute.creation.tracking.RouteTrackingActivity;

public class MainActivity extends Activity {

	private static final int TRACKING_ACTIVITY = 0x01;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button startTrackingButton = (Button) findViewById(R.id.button_start_tracking);
		final Intent intent = new Intent(this, RouteTrackingActivity.class);
		startTrackingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(intent, TRACKING_ACTIVITY);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == TRACKING_ACTIVITY && resultCode == RESULT_OK) {
			TrackedPoints points = (TrackedPoints) data
					.getSerializableExtra(RouteTrackingActivity.RESULT_KEY);
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

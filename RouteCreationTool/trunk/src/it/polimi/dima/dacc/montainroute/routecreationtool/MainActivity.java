package it.polimi.dima.dacc.montainroute.routecreationtool;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import it.polimi.dima.dacc.montainroute.creation.R;

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

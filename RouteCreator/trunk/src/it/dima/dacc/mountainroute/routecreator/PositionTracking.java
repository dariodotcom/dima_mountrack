package it.dima.dacc.mountainroute.routecreator;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PositionTracking extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_position_tracking);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.position_tracking, menu);
		return true;
	}

}

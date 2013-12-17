package it.polimi.dima.dacc.mountainroutes;

import it.polimi.dima.dacc.mountainroutes.routeselector.RouteSelector;
import it.polimi.dima.dacc.mountainroutes.savedroutemanager.SavedRouteManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private OnClickListener startWalkingButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this, RouteSelector.class);
			startActivity(i);
		}
	};

	private OnClickListener showSavedRoutesButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this, SavedRouteManager.class);
			startActivity(i);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button startWalking = (Button) findViewById(R.id.main_start_walk_button);
		startWalking.setOnClickListener(startWalkingButtonListener);

		Button savedRoutes = (Button) findViewById(R.id.main_my_routes_button);
		savedRoutes.setOnClickListener(showSavedRoutesButtonListener);

		Intent i = new Intent(this, WalkingActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
			return true;
		default:
			return false;
		}
	}

}

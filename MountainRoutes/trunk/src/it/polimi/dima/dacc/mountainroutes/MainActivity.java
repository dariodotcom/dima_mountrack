package it.polimi.dima.dacc.mountainroutes;

import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.route.RoutePersistence;
import it.polimi.dima.dacc.mountainroutes.routeselector.RouteSelector;
import it.polimi.dima.dacc.mountainroutes.savedroutemanager.SavedRouteManager;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
import it.polimi.dima.dacc.mountainroutes.walktracker.WalkingActivity;
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

		RouteID id = new RouteID(
				"e6brx2:ahlzfmRpbWEtZGFjYy1tb3VudGFpbnJvdXRlcg0LEgVSb3V0ZRiRvwUM");
		Route dummy;
		try {
			dummy = RoutePersistence.create(this).loadRoute(id);
		} catch (PersistenceException e) {
			e.printStackTrace();
			return;
		}
		Intent i = new Intent(this, WalkingActivity.class);
		i.putExtra(WalkingActivity.TRACKING_ROUTE, dummy);
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

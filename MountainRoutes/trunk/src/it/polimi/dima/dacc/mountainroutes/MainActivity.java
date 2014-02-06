package it.polimi.dima.dacc.mountainroutes;

import it.polimi.dima.dacc.mountainroutes.reportmanager.ReportListActivity;
import it.polimi.dima.dacc.mountainroutes.reportviewer.ReportViewerActivity;
import it.polimi.dima.dacc.mountainroutes.routeselector.RouteSelector;
import it.polimi.dima.dacc.mountainroutes.savedroutemanager.SavedRouteManager;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.WalkingActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Main activity that displays the menu.
 * 
 * @author Chiara
 * 
 */
public class MainActivity extends Activity {

	private static final int SELECT_ROUTE = 0;
	private static final int TRACK_WALKING = 1;

	private OnClickListener startWalkingButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this, RouteSelector.class);
			startActivityForResult(i, SELECT_ROUTE);
		}
	};

	private OnClickListener showSavedRoutesButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this, SavedRouteManager.class);
			startActivityForResult(i, SELECT_ROUTE);
		}
	};

	private OnClickListener showMyExcursionsButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this, ReportListActivity.class);
			startActivity(i);
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SELECT_ROUTE: {
			if (resultCode == RESULT_CANCELED) {
				return;
			}

			Route selectedRoute = data.getParcelableExtra(RouteSelector.SELECTED_ROUTE);
			Intent i = new Intent(this, WalkingActivity.class);
			i.putExtra(WalkingActivity.TRACKING_ROUTE, selectedRoute);
			startActivityForResult(i, TRACK_WALKING);
			return;
		}
		case TRACK_WALKING: {
			if (resultCode == RESULT_CANCELED) {
				return;
			}

			Intent i = new Intent(this, ReportViewerActivity.class);
			ExcursionReport report = data.getParcelableExtra(WalkingActivity.WALKING_REPORT);
			i.putExtra(ReportViewerActivity.REPORT_TO_DISPLAY, report);
			startActivity(i);
		}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button startWalking = (Button) findViewById(R.id.main_start_walk_button);
		startWalking.setOnClickListener(startWalkingButtonListener);

		Button savedRoutes = (Button) findViewById(R.id.main_my_routes_button);
		savedRoutes.setOnClickListener(showSavedRoutesButtonListener);

		Button myExcursions = (Button) findViewById(R.id.main_my_excursions_button);
		myExcursions.setOnClickListener(showMyExcursionsButtonListener);

		// RouteID id = new
		// RouteID("e6brx2:ahlzfmRpbWEtZGFjYy1tb3VudGFpbnJvdXRlcg0LEgVSb3V0ZRiRvwUM");
		// Route dummy;
		// try {
		// dummy = RoutePersistence.create(this).loadRoute(id);
		// } catch (PersistenceException e) {
		// e.printStackTrace();
		// return;
		// }
		//
		// Intent i = new Intent(this, WalkingActivity.class);
		// i.putExtra(WalkingActivity.TRACKING_ROUTE, dummy);
		// startActivityForResult(i, TRACK_WALKING);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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

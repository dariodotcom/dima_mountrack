package it.polimi.dima.dacc.mountainroutes.routeviewer;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.commons.types.Route;
import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescription;
import it.polimi.dima.dacc.mountainroutes.routeviewer.RouteLoader.LoaderCallback;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class RouteViewer extends Activity implements LoaderCallback {

	private static final String ROUTE = "ROUTE";
	public static final String ROUTE_DESCRIPTION = "ROUTE_DESCRIPTION";

	private Route displayedRoute;
	private RouteDescription description;

	private DifficultyView difficultyView;
	private TextView lengthView;
	private TextView gapView;
	private TextView durationView;

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_route_viewer);

		difficultyView = (DifficultyView) findViewById(R.id.difficulty_value);
		lengthView = (TextView) findViewById(R.id.lenght_value);
		gapView = (TextView) findViewById(R.id.gap_value);
		durationView = (TextView) findViewById(R.id.estimated_time_value);

		if (savedState != null) {
			// App was resumed
			Route route = (Route) savedState.getParcelable(ROUTE);
			if (route != null) {
				displayRoute(route);
				return;
			}
		}

		// Load route from remote
		RouteDescription description;

		if (savedState != null) {
			description = savedState.getParcelable(ROUTE_DESCRIPTION);
		} else {
			description = getIntent().getParcelableExtra(ROUTE_DESCRIPTION);
		}

		// Load route from remote
		RouteLoader loader = new RouteLoader(this);
		loader.loadRoute(description);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putParcelable(ROUTE, displayedRoute);
		outState.putParcelable(ROUTE_DESCRIPTION, description);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route_viewer, menu);
		return true;
	}

	private void displayRoute(Route route) {
		this.displayedRoute = route;
		difficultyView.setDifficulty(route.getDifficulty());
		gapView.setText(route.getGap() + " m");
		lengthView.setText(route.getLength() + " km");
		durationView.setText(route.getDuration() + "ms");
	}

	@Override
	public void onLoadError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadResult(Route route) {
		displayRoute(route);

	}

	@Override
	public void onLoadStart() {

	}

}

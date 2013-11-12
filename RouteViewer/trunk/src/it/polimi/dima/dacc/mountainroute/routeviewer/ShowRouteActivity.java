package it.polimi.dima.dacc.mountainroute.routeviewer;

import it.polimi.dima.dacc.mountainroute.commons.presentation.RouteMapFragment;
import it.polimi.dima.dacc.mountainroute.commons.types.Route;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ShowRouteActivity extends Activity {

	public static String ROUTE_TO_SHOW_KEY = "ROUTE_TO_SHOW";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_route);
		Route routeToShow;

		try {
			routeToShow = (Route) getIntent().getExtras().getSerializable(
					ROUTE_TO_SHOW_KEY);
		} catch (ClassCastException e) {
			throw new RuntimeException("Intent extra \"" + ROUTE_TO_SHOW_KEY
					+ "\" should e a route");
		}

		RouteMapFragment routeMap = (RouteMapFragment) getFragmentManager()
				.findFragmentById(R.id.route_map);

		routeMap.initializeRoute(routeToShow, 0f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_route, menu);
		return true;
	}

}

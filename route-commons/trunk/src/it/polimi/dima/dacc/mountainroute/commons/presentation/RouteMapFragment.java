package it.polimi.dima.dacc.mountainroute.commons.presentation;

import it.polimi.dima.dacc.mountainroute.commons.R;
import it.polimi.dima.dacc.mountainroute.commons.types.Route;
import it.polimi.dima.dacc.mountainroute.commons.utils.Logger;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class RouteMapFragment extends MapFragment {

	private static final String INDEX_KEY = "INDEX_KEY";
	private static final String ROUTE_KEY = "ROUTE_KEY";

	private GoogleMap map;
	private Logger log;

	// State to preserve
	private Route route;
	private Float traversedIndex;

	private int traversedColor, pendingColor;
	private Polyline traversedLine, pendingLine;
	private RouteLineController routeLineController;

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);

		// Load colors
		traversedColor = getResources().getColor(R.color.traversedroute);
		pendingColor = getResources().getColor(R.color.pendingroute);

		log = new Logger("ROUTE_MAP_FRAGMENT");
		log.d("Fragment created");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedState) {

		View created = super.onCreateView(inflater, container, savedState);

		this.map = getMap();
		PolylineOptions travOpt = new PolylineOptions().color(traversedColor);
		PolylineOptions pendOpt = new PolylineOptions().color(pendingColor);
		this.traversedLine = map.addPolyline(travOpt);
		this.pendingLine = map.addPolyline(pendOpt);
		log.d("View created.");

		// Restore from saved state if available
		if (savedState != null) {
			Float index = savedState.getFloat(INDEX_KEY);
			Route route = (Route) savedState.getSerializable(ROUTE_KEY);

			log.d("State restored from saved bundle");
			this.initializeRoute(route, index);
		}

		return created;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Save state in bundle
		outState.putFloat(INDEX_KEY, traversedIndex);
		outState.putSerializable(ROUTE_KEY, route);
		log.d("Instance data saved.");
	}

	public void initializeRoute(Route route, float traversedIndex) {
		if (isInitialized()) {
			throw new IllegalStateException(
					"Please call route initializer only once");
		}

		this.route = route;
		this.traversedIndex = traversedIndex;
		this.routeLineController = new RouteLineController(traversedLine,
				pendingLine, route, traversedIndex);
		log.d("route initialized");
	}

	public void setIndex(float index) {
		routeLineController.setCompleteIndex(index);
	}

	private boolean isInitialized() {
		return routeLineController != null;
	}

}

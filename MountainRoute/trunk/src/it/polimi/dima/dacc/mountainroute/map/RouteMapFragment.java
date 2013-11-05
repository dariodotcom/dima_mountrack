package it.polimi.dima.dacc.mountainroute.map;

import it.polimi.dima.dacc.mountainroute.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class RouteMapFragment extends MapFragment {

	private static final String COMPLETE_INDEX_KEY = "COMPLETE_INDEX";
	private static final String ROUTE_KEY = "ROUTE";

	private GoogleMap map;

	private Route route;
	private float traversedIndex;

	private int traversedColor, pendingColor;
	private Polyline traversedLine, pendingLine;
	private boolean _routeInitialized = false;
	private RouteLineController routeLineController;

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);

		// Load colors
		traversedColor = getResources().getColor(R.color.traversedroute);
		pendingColor = getResources().getColor(R.color.pendingroute);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View created = super.onCreateView(inflater, container,
				savedInstanceState);

		this.map = getMap();
		this.traversedLine = map.addPolyline(new PolylineOptions()
				.color(traversedColor));
		this.pendingLine = map.addPolyline(new PolylineOptions()
				.color(pendingColor));

		return created;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

	}

	public void initializeRoute(Route route, float traversedIndex) {
		if (_routeInitialized) {
			throw new IllegalStateException(
					"Please call route initializer only once");
		}

		_routeInitialized = true;
		this.route = route;
		this.traversedIndex = traversedIndex;
		this.routeLineController = new RouteLineController(traversedLine,
				pendingLine, route, traversedIndex);
	}
}

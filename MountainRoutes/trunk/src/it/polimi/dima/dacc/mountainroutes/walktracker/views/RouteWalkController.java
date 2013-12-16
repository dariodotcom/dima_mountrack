package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.types.Route;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

public class RouteWalkController {

	private static final String COMPLETION_INDEX = "completion_index";
	private static final String ROUTE = "route";
	private float completionIndex;
	private Route routeToDisplay;
	private LinkedList<LatLng> pendingPath, walkedPath;

	public RouteWalkController(Route route) {
		initialize(route, 0);
	}

	public RouteWalkController(Bundle savedState) {
		if (savedState == null) {
			throw new NullPointerException("savedState must not be null");
		}

		float completionIndex = savedState.getFloat(COMPLETION_INDEX, -1);
		Route route = savedState.getParcelable(ROUTE);

		if (route == null || completionIndex == -1) {
			throw new IllegalStateException(
					"savedState does not contain controller data");
		}

		initialize(route, completionIndex);
	}

	private void initialize(Route route, float completionIndex) {
		this.routeToDisplay = route;
		this.completionIndex = completionIndex;

		List<LatLng> path = route.getPath().getList();
		int edge = intPart(completionIndex);
		int size = path.size();

		walkedPath = new LinkedList<LatLng>(path.subList(0, edge + 1));
		pendingPath = new LinkedList<LatLng>(path.subList(edge + 1, size));

		update();
	}

	public void setCompletionIndex(float completionIndex) {
		this.completionIndex = completionIndex;

		update();
	}

	public void saveState(Bundle out) {
		out.putFloat(COMPLETION_INDEX, completionIndex);
		out.putParcelable(ROUTE, routeToDisplay);
	}

	private int intPart(double d) {
		return (int) Math.floor(d);
	}

	private void update() {
		// TODO Auto-generated method stub

	}
}
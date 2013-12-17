package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.types.Route;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class RouteWalkController {

	private static final String TAG = "RouteWalkerController";

	private static final String COMPLETION_INDEX = "completion_index";
	private static final String ROUTE = "route";

	private float completionIndex;
	private Route routeToDisplay;

	private LinkedList<LatLng> pending, walked;
	private int currentEdge;
	private boolean hasInterpolationPoint;

	public static RouteWalkController loadFromSavedState(Bundle savedState) {
		if (savedState == null) {
			return null;
		}

		float completionIndex = savedState.getFloat(COMPLETION_INDEX, -1);
		Route route = savedState.getParcelable(ROUTE);

		if (route == null || completionIndex == -1) {
			return null;
		}

		RouteWalkController ctrl = new RouteWalkController(route);
		ctrl.setCompletionIndex(completionIndex);
		return ctrl;
	}

	// Initialization
	public RouteWalkController(Route route) {
		if (route == null) {
			throw new NullPointerException("route must not be null");
		}

		this.routeToDisplay = route;

		List<LatLng> path = route.getPath().getList();

		int edge = this.currentEdge = intPart(completionIndex);
		int size = path.size();

		walked = new LinkedList<LatLng>(path.subList(0, edge + 1));
		pending = new LinkedList<LatLng>(path.subList(edge + 1, size));
	}

	public void setCompletionIndex(float completionIndex) {
		int edge = intPart(completionIndex);

		if (edge < currentEdge) {
			Log.e(TAG, "Received inconsistend update");
			return;
		}

		this.completionIndex = completionIndex;

		if (hasInterpolationPoint) {
			walked.removeLast();
			pending.removeFirst();
			hasInterpolationPoint = false;
		}

		if (edge > currentEdge) {
			LatLng pivot = pending.removeFirst();
			walked.addFirst(pivot);
			currentEdge = edge;
		}

		float coeff = completionIndex - edge;
		if (coeff > 0) {
			LatLng start = walked.getLast(), end = walked.getFirst();
			LatLng interpolated = interpolate(start, end, coeff);
			walked.addLast(interpolated);
			pending.addFirst(interpolated);
			hasInterpolationPoint = true;
		}
	}

	public void saveState(Bundle out) {
		out.putFloat(COMPLETION_INDEX, completionIndex);
		out.putParcelable(ROUTE, routeToDisplay);
	}

	public List<LatLng> getWalkedPath() {
		return walked;
	}

	public LinkedList<LatLng> getPendingPath() {
		return pending;
	}

	private static int intPart(double d) {
		return (int) Math.floor(d);
	}

	private static LatLng interpolate(LatLng start, LatLng end, float coeff) {
		double lat = start.latitude + coeff * (end.latitude - start.latitude);
		double lng = start.longitude + coeff
				* (end.longitude - start.longitude);
		return new LatLng(lat, lng);
	}
}
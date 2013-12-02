package it.polimi.dima.dacc.mountainroutes.commons.presentation;

import it.polimi.dima.dacc.mountainroutes.commons.types.PointList;

import java.util.LinkedList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class RouteStepper {

	private LinkedList<LatLng> traversedRoute;
	private LinkedList<LatLng> pendingRoute;

	private float currentTraversedIndex;
	private int currentPivotIndex;
	private boolean hasInterpolationPoint;

	public RouteStepper(PointList input, float traversedIndex) {
		List<LatLng> route = input.getList();
		if (traversedIndex > route.size() - 1) {
			throw new RuntimeException(
					"Traversed index is greater that route size!");
		}

		this.traversedRoute = new LinkedList<LatLng>();
		this.pendingRoute = new LinkedList<LatLng>();
		this.currentTraversedIndex = traversedIndex;
		this.currentPivotIndex = parseInt(currentTraversedIndex);

		// Load Route
		int routeLength = route.size();
		traversedRoute.addAll(route.subList(0, currentPivotIndex + 1));
		pendingRoute.addAll(route.subList(currentPivotIndex + 1, routeLength));
		hasInterpolationPoint = false;

		// Add interpolation point
		float interpolationCoeff = currentTraversedIndex - currentPivotIndex;
		addInterpolation(interpolationCoeff);
	}

	public RouteStepper(PointList input) {
		this(input, 0);
	}

	public List<LatLng> getPending() {
		return pendingRoute;
	}

	public List<LatLng> getTraversed() {
		return traversedRoute;
	}

	public void step(float newIndex) {
		if (newIndex <= currentTraversedIndex) {
			throw new RuntimeException("Cannot step back in route");
		}

		clearInterpolation();

		int newPivotIndex = parseInt(currentTraversedIndex);

		if (newPivotIndex > currentPivotIndex) {
			// The pivot is present in both list. After the step the old pivot
			// should be only in the traversed list, the new one in both.
			pendingRoute.removeFirst();

			// Copy new pivot to traversed list
			LatLng pivot = pendingRoute.getFirst();
			pendingRoute.addLast(pivot);
			currentPivotIndex = newPivotIndex;
		}

		addInterpolation(newIndex - currentPivotIndex);

	}

	private int parseInt(double d) {
		return (int) Math.floor(d);
	}

	private void clearInterpolation() {
		if (hasInterpolationPoint) {
			traversedRoute.removeLast();
			pendingRoute.removeFirst();
			hasInterpolationPoint = false;
		}
	}

	private void addInterpolation(float coefficient) {
		if (hasInterpolationPoint) {
			return;
		}

		if (coefficient == 0) {
			// The interpolated point would coincide with current pivot, so do
			// nothing.
			hasInterpolationPoint = false;
			return;
		}

		LatLng interpolated = interpolate(traversedRoute.getLast(),
				pendingRoute.getFirst(), coefficient);
		traversedRoute.addLast(interpolated);
		pendingRoute.addFirst(interpolated);
		hasInterpolationPoint = true;
	}

	private LatLng interpolate(LatLng p1, LatLng p2, float coeff) {
		double lat = p1.latitude + coeff * (p2.latitude - p1.latitude);
		double lng = p1.longitude + coeff * (p2.longitude - p1.longitude);
		return new LatLng(lat, lng);
	}

}
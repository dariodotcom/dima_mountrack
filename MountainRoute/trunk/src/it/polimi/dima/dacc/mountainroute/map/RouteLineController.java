package it.polimi.dima.dacc.mountainroute.map;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

public class RouteLineController {
	private LinkedList<LatLng> traversedList, pendingList;
	private Polyline traversed, pending;
	private int currentEdge;
	private int routeSize;
	private boolean hasInterpolationPoint;

	public RouteLineController(Polyline traversed, Polyline pending,
			Route route, float completeIndex) {
		List<LatLng> points = route.getPoints();
		this.pending = pending;
		this.traversed = traversed;
		this.currentEdge = (int) Math.floor(completeIndex);
		this.routeSize = points.size();
		this.hasInterpolationPoint = false;

		if (currentEdge >= routeSize) {
			throw new IllegalArgumentException("index must be < " + routeSize);
		}

		traversedList = new LinkedList<LatLng>(points.subList(0,
				currentEdge + 1));
		pendingList = new LinkedList<LatLng>(points.subList(currentEdge + 1,
				routeSize));

		setCompleteIndex(completeIndex);
	}

	public RouteLineController(Polyline traversed, Polyline pending, Route route) {
		this(traversed, pending, route, 0);
	}

	public void setCompleteIndex(float index) {
		checkInputIndex(index);

		// Remove the interpolation point from lists
		if (hasInterpolationPoint) {
			pendingList.removeFirst();
			traversedList.removeLast();
			hasInterpolationPoint = false;
		}

		// Step to next point if necessary
		int newEdge = (int) Math.floor(index);
		if (newEdge > currentEdge) {
			LatLng edge = pendingList.removeFirst();
			traversedList.addLast(edge);
			currentEdge = newEdge;
		}

		// Add interpolation point if necessary
		float coeff = index - currentEdge;
		if (coeff > 0) {
			LatLng interpolated = linearInterpolate(traversedList.getLast(),
					pendingList.getFirst(), coeff);
			Log.d("ROUTE", "Interpolated point: " + interpolated);
			traversedList.addLast(interpolated);
			pendingList.addFirst(interpolated);
			hasInterpolationPoint = true;
		}

		updateLines();
	}

	private void updateLines() {
		traversed.setPoints(traversedList);
		pending.setPoints(pendingList);
	}

	private void checkInputIndex(float index) {
		if (index < currentEdge || index > routeSize - 1) {
			throw new IllegalArgumentException("Index must be between "
					+ currentEdge + " and " + (routeSize - 1));
		}
	}

	private LatLng linearInterpolate(LatLng p1, LatLng p2, float coeff) {
		Log.d("ROUTE", "coeff: " + coeff);
		double lat = p1.latitude + coeff * (p2.latitude - p1.latitude);
		double lng = p1.longitude + coeff * (p2.longitude - p1.longitude);
		return new LatLng(lat, lng);
	}
}
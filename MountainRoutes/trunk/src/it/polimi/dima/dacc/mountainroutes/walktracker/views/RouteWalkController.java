package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class RouteWalkController {

	private static final String TAG = "RouteWalkerController";


	private LinkedList<LatLng> pending, walked;
	private int currentEdge;
	private boolean hasInterpolationPoint;

	// Initialization
	public RouteWalkController(Route route) {
		if (route == null) {
			throw new NullPointerException("route must not be null");
		}

		List<LatLng> path = route.getPath().getList();

		int edge = this.currentEdge = 0;
		int size = path.size();

		walked = new LinkedList<LatLng>(path.subList(0, edge + 1));
		pending = new LinkedList<LatLng>(path.subList(edge, size));
	}

	public void update(TrackResult result){
		setCompletionIndex(result.getCompletionIndex());
	}
	
	
	private void setCompletionIndex(float completionIndex) {
		int edge = intPart(completionIndex);

		if (edge < currentEdge) {
			Log.e(TAG, "Received inconsistend update");
			return;
		}

		if (hasInterpolationPoint) {
			walked.removeLast();
			pending.removeFirst();
			hasInterpolationPoint = false;
		} else {
			pending.removeFirst();
		}

		if (edge > currentEdge) {
			int n = edge - currentEdge;
			while (n > 0) {
				LatLng pivot = pending.removeFirst();
				walked.addLast(pivot);
				n--;
			}
			
			currentEdge = edge;
		}

		float coeff = completionIndex - edge;
		if (coeff > 0) {
			LatLng start = walked.getLast(), end = pending.getFirst();
			LatLng interpolated = interpolate(start, end, coeff);
			walked.addLast(interpolated);
			pending.addFirst(interpolated);
			hasInterpolationPoint = true;
		} else {
			LatLng last = walked.getLast();
			pending.addFirst(last);
		}
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
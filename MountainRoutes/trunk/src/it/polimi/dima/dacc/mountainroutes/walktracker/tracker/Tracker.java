package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import com.google.android.gms.maps.model.LatLng;

import it.polimi.dima.dacc.mountainroutes.types.Route;

public abstract class Tracker {

	protected static final double MAX_DISTANCE_KM = 0.1;
	
	public abstract TrackResult track(LatLng newPoint) throws TrackerException;

	public abstract boolean isFinished();

	public static Tracker create(Route route) {
		return new TrackerImpl(route.getPath());
	}

	public static boolean canWalkOn(Route route, LatLng latLng) {
		return GeomUtils.haversineDistance(route.getPath().getList().get(0), latLng) < MAX_DISTANCE_KM;
	}
}
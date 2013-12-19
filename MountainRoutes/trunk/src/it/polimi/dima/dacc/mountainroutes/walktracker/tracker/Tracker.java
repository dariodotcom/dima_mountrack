package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import com.google.android.gms.maps.model.LatLng;

import it.polimi.dima.dacc.mountainroutes.types.Route;

public abstract class Tracker {

	public abstract TrackResult track(LatLng newPoint) throws TrackerException;

	public abstract boolean isFinished();

	public static Tracker create(Route route) {
		return new OldTracker(route.getPath());
	}

}
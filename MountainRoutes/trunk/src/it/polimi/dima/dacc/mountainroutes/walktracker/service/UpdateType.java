package it.polimi.dima.dacc.mountainroutes.walktracker.service;

import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackerException;

public enum UpdateType {

	GPS_ENABLED, GPS_DISABLED, GOING_BACKWARDS, FAR_FROM_ROUTE, EXCURSION_PAUSED, EXCURSION_RESUME, FORCE_QUIT, MOVING_WHILE_PAUSED;

	public static UpdateType from(TrackerException.Type exceptionType) {
		switch (exceptionType) {
		case FAR_FROM_ROUTE:
			return FAR_FROM_ROUTE;
		case GOING_BACKWARD:
			return GOING_BACKWARDS;
		default:
			return null;
		}
	}
}

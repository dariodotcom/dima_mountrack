package it.polimi.dima.dacc.mountainroutes.walktracker.service;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import android.content.Intent;

public class BroadcastFactory {

	public static final String ACTION_START = "START";
	public static final String ACTION_STOP = "STOP";
	public static final String ACTION_UPDATE = "UPDATE";
	public static final String ACTION_TRACKING = "TRACKING";

	public static final String EXTRA_ROUTE = "ROUTE";
	public static final String EXTRA_COMPLETION_INDEX = "COMPLETION_INDEX";
	public static final String EXTRA_REPORT = "REPORT";
	public static final String EXTRA_UPDATE = "UPDATE";

	public static Intent createTrackingUpdateBroadcast(float completeIndex) {
		return null;
	}

	public static Intent createTrackingStartBroadcast(Route route) {
		return null;
	}

	public static Intent createTrackingStopBroadcast(ExcursionReport report) {
		return null;
	}

	public static Intent createStatusBroadcast(UpdateType update) {
		return null;
	}
}

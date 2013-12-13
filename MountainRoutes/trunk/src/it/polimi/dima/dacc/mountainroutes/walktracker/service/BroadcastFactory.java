package it.polimi.dima.dacc.mountainroutes.walktracker.service;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import android.content.Intent;
import android.content.IntentFilter;

public class BroadcastFactory {

	public static final String PREFIX = "it.polimi.dima.dacc.mountainroutes.tracker.";

	public static final String ACTION_START = "START";
	public static final String ACTION_STOP = "STOP";
	public static final String ACTION_UPDATE = "UPDATE";
	public static final String ACTION_TRACKING = "TRACKING";

	public static final String EXTRA_ROUTE = "ROUTE";
	public static final String EXTRA_COMPLETION_INDEX = "COMPLETION_INDEX";
	public static final String EXTRA_REPORT = "REPORT";
	public static final String EXTRA_UPDATE = "UPDATE";

	private static IntentFilter intentFilter;

	static {
		intentFilter = new IntentFilter();
		String prefix = BroadcastFactory.PREFIX;

		intentFilter.addAction(prefix + BroadcastFactory.ACTION_START);
		intentFilter.addAction(prefix + BroadcastFactory.ACTION_STOP);
		intentFilter.addAction(prefix + BroadcastFactory.ACTION_TRACKING);
		intentFilter.addAction(prefix + BroadcastFactory.ACTION_UPDATE);
	}

	public static IntentFilter getCompleteIntentFilter() {
		return intentFilter;
	}

	public static Intent createTrackingUpdateBroadcast(float completeIndex) {
		Intent i = createIntent(ACTION_TRACKING);
		i.putExtra(EXTRA_COMPLETION_INDEX, completeIndex);
		return i;
	}

	public static Intent createTrackingStartBroadcast(Route route) {
		Intent i = createIntent(ACTION_START);
		i.putExtra(EXTRA_ROUTE, route);
		return i;
	}

	public static Intent createTrackingStopBroadcast(ExcursionReport report) {
		Intent i = createIntent(ACTION_STOP);
		i.putExtra(EXTRA_REPORT, report);
		return i;
	}

	public static Intent createStatusBroadcast(UpdateType update) {
		Intent i = createIntent(ACTION_UPDATE);
		i.putExtra(EXTRA_UPDATE, update.name());
		return i;
	}

	private static Intent createIntent(String actionName) {
		return new Intent(PREFIX + actionName);
	}
}
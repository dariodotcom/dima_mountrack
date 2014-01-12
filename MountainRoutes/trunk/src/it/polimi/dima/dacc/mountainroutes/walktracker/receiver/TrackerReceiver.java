package it.polimi.dima.dacc.mountainroutes.walktracker.receiver;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.BroadcastFactory;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/***
 * Adapter class to receive broadcasts from tracking service, extract updates
 * parameters and call matching hooks in a given {@link TrackerListenerBase}
 * listener.
 */
public class TrackerReceiver extends BroadcastReceiver {

	private TrackerListenerBase listener;

	public TrackerReceiver(TrackerListenerBase listener) {
		this.listener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String[] parts = intent.getAction().split("\\.");
		String action = parts[parts.length - 1];

		if (action.equals(BroadcastFactory.ACTION_START)) {
			Route route = (Route) intent.getParcelableExtra(BroadcastFactory.EXTRA_ROUTE);
			listener.onStartTracking(route);
			return;
		}

		if (action.equals(BroadcastFactory.ACTION_STOP)) {
			ExcursionReport report = (ExcursionReport) intent.getParcelableExtra(BroadcastFactory.EXTRA_REPORT);
			listener.onStopTracking(report);
		}

		if (action.equals(BroadcastFactory.ACTION_TRACKING)) {
			String extraName = BroadcastFactory.EXTRA_TRACK_RESULT;
			TrackResult result = intent.getParcelableExtra(extraName);
			listener.onTrackingUpdate(result);
		}

		if (action.equals(BroadcastFactory.ACTION_UPDATE)) {
			String typeName = intent.getStringExtra(BroadcastFactory.EXTRA_UPDATE);
			UpdateType updateType = UpdateType.valueOf(typeName);
			listener.onStatusUpdate(updateType);
		}

		if (action.equals(BroadcastFactory.ACTION_ALTITUDE_GAP)) {
			int altitude = intent.getExtras().getInt(BroadcastFactory.EXTRA_ALTITUDE_GAP);
			listener.onAltitudeGapUpdate(altitude);
		}
	}
}

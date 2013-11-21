package it.polimi.dima.dacc.montainroute.creation.tracking;

import it.polimi.dima.dacc.mountainroute.commons.types.PointList;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.Intent;

public class TrackingUpdateBroadcaster {

	private final static String PREFIX = "it.polimi.dima.dacc.mountainroute.";
	private final static String LOCATION_UPDATE_ACTION = PREFIX
			+ "LOCATION_UPDATE";
	private final static String LOCATION_UPDATE_LATLNG = "LATLNG";
	private final static String LOCATION_UPDATE_POINTLIST = "POINTLIST";
	private static final String TIME_UPDATE_ACTION = PREFIX + "TIME_UPDATE";
	private static final String TIME_UPDATE_MILLIS = "TIME_MILLIS";
	private static final String PROVIDER_ENABLED_UPDATE_ACTION = PREFIX
			+ "PROVIDER_DISABLED";
	private static final String PROVIDER_DISABLED_UPDATE_ACTION = PREFIX
			+ "PROVIDER_ENABLED";

	private Context context;
	private boolean updatesPaused;

	// Variables to queue pending updates
	private PointList pendingLocationUpdates;
	private Long lastTimeUpdate;
	private Intent providerStatusUpdate;

	public TrackingUpdateBroadcaster(Context context) {
		this.context = context;
		this.updatesPaused = false;
	}

	public void pauseUpdates() {
		this.updatesPaused = true;
	}

	public synchronized void resumeUpdates() {
		// Send time update
		if (lastTimeUpdate != null) {
			sendTimeUpdate(lastTimeUpdate);
			lastTimeUpdate = null;
		}

		// Send location updates
		if (!pendingLocationUpdates.isEmpty()) {
			sendPositionUpdate(pendingLocationUpdates);
			pendingLocationUpdates = new PointList();
		}

		// send provider updates
		if (providerStatusUpdate != null) {
			context.sendBroadcast(providerStatusUpdate);
			providerStatusUpdate = null;
		}

		updatesPaused = false;
	}

	public void sendPositionUpdate(LatLng position) {
		if (updatesPaused) {
			this.pendingLocationUpdates.add(position);
		} else {
			Intent intent = new Intent(LOCATION_UPDATE_ACTION);
			intent.putExtra(LOCATION_UPDATE_LATLNG, position);
			context.sendBroadcast(intent);
		}
	}

	public void sendPositionUpdate(PointList points) {
		if (updatesPaused) {
			this.pendingLocationUpdates.addAll(points);
		} else {
			Intent intent = new Intent(LOCATION_UPDATE_ACTION);
			intent.putExtra(LOCATION_UPDATE_POINTLIST, points);
			context.sendBroadcast(intent);
		}
	}

	public void sendTimeUpdate(long millis) {
		if (this.updatesPaused) {
			this.lastTimeUpdate = millis;
		} else {
			Intent intent = new Intent(TIME_UPDATE_ACTION);
			intent.putExtra(TIME_UPDATE_MILLIS, millis);
			context.sendBroadcast(intent);
		}
	}

	public void sendProviderDisabledUpdate() {
		Intent intent = new Intent(PROVIDER_DISABLED_UPDATE_ACTION);

		if (updatesPaused) {
			if (this.providerStatusUpdate != null
					&& this.providerStatusUpdate.getAction() == PROVIDER_ENABLED_UPDATE_ACTION) {
				this.providerStatusUpdate = null;
			} else {
				this.providerStatusUpdate = intent;
			}
		} else {
			context.sendBroadcast(intent);
		}
	}

	public void sendProviderEnabledUpdate() {
		Intent intent = new Intent(PROVIDER_ENABLED_UPDATE_ACTION);
		
		if (updatesPaused) {
			if (this.providerStatusUpdate != null
					&& this.providerStatusUpdate.getAction() == PROVIDER_DISABLED_UPDATE_ACTION) {
				this.providerStatusUpdate = null;
			} else {
				this.providerStatusUpdate = intent;
			}
		} else {
			context.sendBroadcast(intent);
		}
	}

}

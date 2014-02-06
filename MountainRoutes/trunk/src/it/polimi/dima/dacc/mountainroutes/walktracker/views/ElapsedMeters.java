package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup.TrackingStatus;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Displays the number of meters yet to be walked by users.
 */
public class ElapsedMeters extends TextView implements TrackerListener {

	private String routeLength;

	public ElapsedMeters(Context context) {
		super(context);
	}

	public ElapsedMeters(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ElapsedMeters(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onStartTracking(Route route) {
		routeLength = represent(route.getLengthInMeters());
		this.setText(String.format("%s / %s", "0", routeLength));
	}

	@Override
	public void onStopTracking(ExcursionReport report) {

	}

	@Override
	public void onStatusUpdate(UpdateType update) {

	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
		String elapsed = represent(result.getElapsedMeters());
		this.setText(String.format("%s/%s", elapsed, routeLength));
	}

	@Override
	public void onRegister(LaggardBackup backup) {
		TrackingStatus status = backup.getStatus();

		switch (status) {
		case PAUSED:
		case TRACKING:
			this.onStartTracking(backup.getRouteBeingTracked());
			TrackResult result = backup.getLastTrackResult();
			if (result != null) {
				this.onTrackingUpdate(result);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onUnregister(LaggardBackup backup) {

	}

	@Override
	public void onAltitudeGapUpdate(int altitude) {
		// TODO Auto-generated method stub

	}

	private String represent(int meters) {
		if (meters < 1000) {
			return meters + "m";
		}

		return (meters / 1000) + "km";
	}

}

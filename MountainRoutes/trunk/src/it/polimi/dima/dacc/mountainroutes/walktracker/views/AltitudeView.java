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
 * Automatically displays the altitude information contained in updates from the
 * tracker.
 */
public class AltitudeView extends TextView implements TrackerListener {

	private int totalGap;

	public AltitudeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AltitudeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AltitudeView(Context context) {
		super(context);
	}

	@Override
	public void onStartTracking(Route route) {
		totalGap = route.getGapInMeters();
		setText("-");
	}

	@Override
	public void onStopTracking(ExcursionReport report) {
	}

	@Override
	public void onStatusUpdate(UpdateType update) {
	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
	}

	@Override
	public void onRegister(LaggardBackup backup) {
		TrackingStatus status = backup.getStatus();

		switch (status) {
		case TRACKING:
		case PAUSED:
			totalGap = backup.getRouteBeingTracked().getGapInMeters();
			updateAltitude(backup.getAltitudeGap());
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
		updateAltitude(altitude);
	}

	private void updateAltitude(int gap) {
		if (Math.abs(totalGap) < 10) {
			return;
		}

		String message = String.format("%sm / %sm", gap, totalGap);
		setText(message);
	}
}
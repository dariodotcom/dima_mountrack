package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class AltitudeView extends TextView implements TrackerListener {

	private double totalGap;

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
		if (backup.amILate()) {
			totalGap = backup.getRouteBeingTracked().getGapInMeters();
			updateAltitude(backup.getAltitudeGap());
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
		String message = String.format("%s / %s", gap, totalGap);
		setText(message);
	}
}
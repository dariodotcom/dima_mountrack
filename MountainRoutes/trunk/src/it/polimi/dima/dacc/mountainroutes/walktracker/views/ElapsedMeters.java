package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.WalkingActivity;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class ElapsedMeters extends TextView implements TrackerListener {

	private int totalMeters;

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
		totalMeters = route.getLengthInMeters();
		this.setText(String.format("%s/%s", "0", totalMeters));
	}

	@Override
	public void onStopTracking(ExcursionReport report) {

	}

	@Override
	public void onStatusUpdate(UpdateType update) {

	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
		this.setText(String.format("%s/%s", result.getElapsedMeters(), totalMeters));
	}

	@Override
	public void onRegister(LaggardBackup backup) {

	}

	@Override
	public void onUnregister(LaggardBackup backup) {

	}

}

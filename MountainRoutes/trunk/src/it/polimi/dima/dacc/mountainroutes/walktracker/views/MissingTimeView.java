package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup.TrackingStatus;
import android.content.Context;
import android.util.AttributeSet;

public class MissingTimeView extends TimerView {

	private Integer durationMinutes;

	public MissingTimeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MissingTimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MissingTimeView(Context context) {
		super(context);
	}

	@Override
	public void onStartTracking(Route route) {
		durationMinutes = route.getDurationInMinutes();
		super.onStartTracking(route);
	}
	
	@Override
	public void onRegister(LaggardBackup backup) {
		TrackingStatus status = backup.getStatus();
		
		switch (status) {
		case PAUSED:
		case TRACKING:
			durationMinutes = backup.getRouteBeingTracked().getDurationInMinutes();
			break;
		default:
			break;
		}
		
		super.onRegister(backup);
	}

	@Override
	protected String parseMillis(long millis) {
		if (durationMinutes == null) {
			return "-";
		}

		int seconds = (int) millis / 1000;
		int minutes = seconds / 60;

		if (minutes > durationMinutes){
			return "0";
		}
		
		int missingMins = durationMinutes - minutes;
		
		String hours = pan(missingMins/60);
		String mins = pan(missingMins%60);
		return String.format("%s:%s", hours, mins);
	}

	private String pan(int n) {
		if (n < 10) {
			return "0" + n;
		}
		return String.valueOf(n);
	}
}

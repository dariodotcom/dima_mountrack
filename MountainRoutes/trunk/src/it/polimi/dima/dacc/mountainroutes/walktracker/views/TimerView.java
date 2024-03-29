package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.commons.Utils;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.Timer;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup.TrackingStatus;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Displays the timer of the walk.
 */
public class TimerView extends TextView implements TrackerListener, Timer.Listener {

	private int pausedColor, runningColor;

	// Default contructors
	public TimerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TimerView(Context context) {
		super(context);
		init();
	}

	// Tracker updates listener
	@Override
	public void onStartTracking(Route route) {
		setTextColor(runningColor);
	}

	@Override
	public void onStopTracking(ExcursionReport report) {

	}

	@Override
	public void onStatusUpdate(UpdateType update) {
		switch (update) {
		case EXCURSION_PAUSED:
			setTextColor(pausedColor);
			break;
		case EXCURSION_RESUME:
			setTextColor(runningColor);
			break;
		case FORCE_QUIT:
			break;
		default:
			break;
		}
	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
		// Do nothing!
	}

	@Override
	public void onRegister(LaggardBackup backup) {
		TrackingStatus status = backup.getStatus();

		switch (status) {
		case ABRUPTED:
		case FINISHED:
			return;
		case PAUSED:
			setTextColor(pausedColor);
			backup.registerTimerListener(this);
			break;
		case READY:
		case TRACKING:
			setTextColor(runningColor);
			backup.registerTimerListener(this);
			break;
		}
	}

	@Override
	public void onUnregister(LaggardBackup backup) {
		backup.unregisterTimerListener(this);
	}

	// Timer methods
	@Override
	public void onTime(long millis) {
		String text = parseMillis(millis);
		setText(text);
	}

	private void init() {
		Resources r = getContext().getResources();
		pausedColor = r.getColor(R.color.timer_paused_text);
		runningColor = r.getColor(R.color.timer_running_text);
		onTime(0L);
	}

	protected String parseMillis(long millis) {
		return Utils.formatMillis(millis);
	}

	@Override
	public void onAltitudeGapUpdate(int altitude) {
		// TODO Auto-generated method stub

	}
}
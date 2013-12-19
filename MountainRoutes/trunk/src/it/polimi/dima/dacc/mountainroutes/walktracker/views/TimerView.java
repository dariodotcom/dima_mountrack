package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import java.text.DateFormat;
import java.util.Date;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.Timer;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimerView extends TextView implements TrackerListener,
		Timer.Listener {

	private int pausedColor, runningColor;
	private DateFormat format;

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
		backup.registerTimerListener(this);
	}

	@Override
	public void onUnregister(LaggardBackup backup) {
		backup.unregisterTimerListener(this);
	}

	// Timer methods
	@Override
	public void onTime(long millis) {
		Date d = new Date(millis);
		String text = format.format(d);
		setText(text);
	}

	private void init() {
		Resources r = getContext().getResources();
		pausedColor = r.getColor(R.color.timer_paused_text);
		runningColor = r.getColor(R.color.timer_running_text);
		format = android.text.format.DateFormat.getTimeFormat(getContext());
		onTime(0L);
	}
}
package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.Timer;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimerView extends TextView implements TrackerListener {

	int pausedColor, runningColor;
	private Timer timer;
	private Timer.Listener timerListener = new Timer.Listener() {

		@Override
		public void onTime(long millis) {
			setTime(millis);
		}
	};

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
		timer.start();
		setTextColor(runningColor);
	}

	@Override
	public void onStopTracking(ExcursionReport report) {
		timer.stop();
		setTextColor(pausedColor);
	}

	@Override
	public void onStatusUpdate(UpdateType update) {
		switch (update) {
		case EXCURSION_PAUSED:
			setTextColor(pausedColor);
			timer.pause();
			break;
		case EXCURSION_RESUME:
			setTextColor(runningColor);
			timer.resume();
			break;
		case FORCE_QUIT:
			timer.stop();
			break;
		default:
			break;
		}
	}

	@Override
	public void onTrackingUpdate(float completionIndex) {
		// Do nothing!
	}

	@Override
	public void onRegister(LaggardBackup backup) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnregister() {
		// TODO Auto-generated method stub

	}

	private void init() {
		timer = new Timer(timerListener);
		Resources r = getContext().getResources();
		pausedColor = r.getColor(R.color.timer_paused_text);
		runningColor = r.getColor(R.color.timer_running_text);
		setTime(0);
	}

	// Helpers
	private void setTime(long millis) {
		String pattern = "%s:%s:%s";
		int seconds = (int) (millis / 1000);
		int minutes = (int) Math.floor(seconds / 60);
		int hours = (int) Math.floor(minutes / 60);

		if (seconds >= 60) {
			seconds = seconds % 60;
		}

		if (minutes >= 60) {
			minutes = minutes % 60;
		}

		setText(String.format(pattern, pan(hours), pan(minutes), pan(seconds)));
	}

	private String pan(int n) {
		if (n > 9) {
			return Integer.toString(n);
		}

		return "0" + n;
	}
}
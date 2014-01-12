package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.StringRepository;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PauseResumeButton extends Button implements TrackerListener {

	private final static String TAG = PauseResumeButton.class.getName();

	private static enum State {
		PAUSE, RESUME;

		public boolean is(State state) {
			return this == state;
		}
	}

	private State currentState;
	private TrackingControlWrapper attachedControl;
	private StringRepository stringRepo;

	// Default constructors
	public PauseResumeButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PauseResumeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PauseResumeButton(Context context) {
		super(context);
		init();
	}

	public void attachToControl(TrackingControlWrapper control) {
		this.attachedControl = control;
	}

	// Tracker Listener
	@Override
	public void onStartTracking(Route route) {
		setState(State.RESUME);
	}

	@Override
	public void onStopTracking(ExcursionReport report) {
		setEnabled(false);
	}

	@Override
	public void onStatusUpdate(UpdateType update) {
		switch (update) {
		case EXCURSION_PAUSED:
			setState(State.PAUSE);
			break;
		case EXCURSION_RESUME:
			setState(State.RESUME);
			break;
		case FORCE_QUIT:
			setEnabled(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
		// Don't care
	}

	@Override
	public void onRegister(LaggardBackup backup) {
		if (!backup.amILate()) {
			return;
		}

		switch (backup.getStatus()) {
		case PAUSED:
			setState(State.PAUSE);
			return;
		case FINISHED:
		case ABRUPTED:
			setEnabled(false);
			return;
		default:
			setState(State.RESUME);
		}

	}

	@Override
	public void onUnregister(LaggardBackup backup) {

	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		return;
	}

	private void init() {
		stringRepo = new StringRepository(getContext());
		stringRepo.loadString(R.string.walking_activity_pause);
		stringRepo.loadString(R.string.walking_activity_resume);

		super.setOnClickListener(clickDispatcher);
	}

	private void setState(State newState) {
		String message;

		if (newState.is(State.PAUSE)) {
			message = stringRepo.getString(R.string.walking_activity_resume);
		} else {
			message = stringRepo.getString(R.string.walking_activity_pause);
		}

		setText(message);
		currentState = newState;
		setEnabled(true);
	}

	private OnClickListener clickDispatcher = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (attachedControl == null) {
				Log.w(TAG, "attached control is null");
				return;
			}

			if (currentState.is(State.PAUSE)) {
				attachedControl.resume();
			}

			if (currentState.is(State.RESUME)) {
				attachedControl.pause();
			}
		}
	};

	@Override
	public void onAltitudeGapUpdate(int altitude) {
		// TODO Auto-generated method stub

	}
}

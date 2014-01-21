package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.StringRepository;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;

public class NotificationView extends TextView implements TrackerListener {

	private ArrayList<UpdateType> notificationList = new ArrayList<UpdateType>();
	private StringRepository repository;

	public NotificationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public NotificationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public NotificationView(Context context) {
		super(context);
		initialize();
	}

	private void initialize() {
		this.repository = new StringRepository(getContext());
		repository.loadString(R.string.notifFarFromRouteTitle);
		repository.loadString(R.string.notifGoingBackwardsTitle);
		repository.loadString(R.string.gps_disabled_message);
		repository.loadString(R.string.notifMovingWhilePausedTitle);
	}

	@Override
	public void onStartTracking(Route route) {

	}

	@Override
	public void onStopTracking(ExcursionReport report) {

	}

	@Override
	public void onStatusUpdate(UpdateType update) {
		switch (update) {
		case FAR_FROM_ROUTE:
		case GOING_BACKWARDS:
		case GPS_DISABLED:
		case MOVING_WHILE_PAUSED:
			showMessageFor(update);
			break;

		case GPS_ENABLED:
			removeNotification(UpdateType.GPS_DISABLED);
			break;

		default:
			break; // Yes, those warning are stupid.
		}
	}

	private void removeNotification(UpdateType... updates) {
		for (UpdateType u : updates) {
			notificationList.remove(u);
		}
		if (notificationList.isEmpty()) {
			setAlpha(0);
		} else {
			getTextFor(notificationList.get(notificationList.size() - 1));
		}
	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
		removeNotification(UpdateType.FAR_FROM_ROUTE, UpdateType.MOVING_WHILE_PAUSED, UpdateType.GOING_BACKWARDS);
	}

	@Override
	public void onAltitudeGapUpdate(int altitude) {

	}

	@Override
	public void onRegister(LaggardBackup backup) {
		if (!backup.amILate()) {
			return;
		}

		if (backup.isGoingBackwards()) {
			showMessageFor(UpdateType.GOING_BACKWARDS);

		}

		if (backup.isFarFromRoute()) {
			showMessageFor(UpdateType.FAR_FROM_ROUTE);
		}

		if (backup.isMovingWhilePaused()) {
			showMessageFor(UpdateType.MOVING_WHILE_PAUSED);
		}
	}

	@Override
	public void onUnregister(LaggardBackup backup) {

	}

	private void showMessageFor(UpdateType update) {
		notificationList.add(update);
		setText(getTextFor(update));
		setAlpha(1);

	}

	private String getTextFor(UpdateType update) {

		switch (update) {
		case FAR_FROM_ROUTE: {
			return repository.getString(R.string.notifFarFromRouteTitle);
		}
		case GOING_BACKWARDS: {
			return repository.getString(R.string.notifGoingBackwardsTitle);
		}
		case GPS_DISABLED: {
			return repository.getString(R.string.gps_disabled_message);
		}
		case MOVING_WHILE_PAUSED: {
			return repository.getString(R.string.notifMovingWhilePausedTitle);
		}
		default:
			return null;
		}

	}

}

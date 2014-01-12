package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListenerManager;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import android.content.Context;
import android.util.Log;

/**
 * Component that is able to send notifications to the user upon receiving
 * updates from the tracker; Notifications are sent only in a subset of updates,
 * which are
 * <ul>
 * <li>Walk has finished;</li>
 * <li>User is going backwards;</li>
 * <li>User is abandoning route;</li>
 * <li>Problems in tracking.</li>
 * </ul>
 */
public class NotificationsEmitter implements TrackerListener {

	private final static String TAG = "NotificationsEmitter";

	private static NotificationsEmitter instance;
	private static Object instanceLock;

	static {
		instanceLock = new Object();
	}

	public static NotificationsEmitter getEmitter(Context context) {
		synchronized (instanceLock) {
			if (instance == null) {
				Context appContext = context.getApplicationContext();
				instance = new NotificationsEmitter(appContext);
				TrackerListenerManager.getManager(context).registerListener(
						instance);
			}

			return instance;
		}
	}

	private boolean turnedOn;
	private Context context;

	public NotificationsEmitter(Context context) {
		this.context = context;
	}

	/**
	 * Turns on notifications. After this method has been called the component
	 * starts sending notifications.
	 */
	public void turnOn() {
		this.turnedOn = true;
	}

	/**
	 * Turns off notifications and dismiss all sent notifications.
	 */
	public void dismiss() {
		if (!turnedOn) {
			return;
		}

		this.turnedOn = false;
		context.getApplicationContext(); // TODO removethis
		// TODO dismiss sent notifications
	}

	@Override
	public void onStartTracking(Route route) {
		// Do nothing
	}

	@Override
	public void onStopTracking(ExcursionReport report) {
		sendNotification(Notification.ARRIVED);
	}

	@Override
	public void onStatusUpdate(UpdateType update) {
		switch (update) {
		case FAR_FROM_ROUTE:
			sendNotification(Notification.FAR_FROM_ROUTE);
			break;

		case GOING_BACKWARDS:
			sendNotification(Notification.GOING_BACKWARDS);
			break;

		case GPS_DISABLED:
			sendNotification(Notification.TRACKING_PROBLEM);
			break;

		case MOVING_WHILE_PAUSED:
			sendNotification(Notification.MOVING_WHILE_PAUSED);
			break;

		default:
			break;
		}
	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
		// Do nothing
	}

	@Override
	public void onRegister(LaggardBackup backup) {
		// Do noting, we don't care about previous state.
	}

	@Override
	public void onUnregister(LaggardBackup backup) {
		// Do nothing
	}
	
	@Override
	public void onAltitudeGapUpdate(int altitude) {
		
	}
	private static enum Notification {
		FAR_FROM_ROUTE, TRACKING_PROBLEM, GOING_BACKWARDS, QUIT, MOVING_WHILE_PAUSED, ARRIVED
	}

	private void sendNotification(Notification notification) {
		Log.d(TAG, "sent " + notification.name());
	}


}
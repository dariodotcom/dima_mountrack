package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.StringRepository;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.WalkingActivity;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListenerManager;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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
				TrackerListenerManager.getManager(context).registerListener(instance);
			}

			return instance;
		}
	}

	private boolean turnedOn;
	private Context context;
	private StringRepository notificationTexts;
	private NotificationManager notificationManager;

	private NotificationsEmitter(Context context) {
		this.context = context;
		this.notificationTexts = new StringRepository(context);

		String svcName = Context.NOTIFICATION_SERVICE;
		this.notificationManager = (NotificationManager) context.getSystemService(svcName);

		for (Notification n : Notification.values()) {
			notificationTexts.loadString(n.getTitleId());
			notificationTexts.loadString(n.getDescriptionId());
		}
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

		for (Notification n : Notification.values()) {
			notificationManager.cancel(n.ordinal());
		}
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
			// sendNotification(Notification.GPS_DISABLED);
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

		FAR_FROM_ROUTE(R.string.notifFarFromRouteTitle, R.string.notifFarFromRouteDesc), GOING_BACKWARDS(R.string.notifGoingBackwardsTitle, R.string.notifGoingBackwardsDesc), MOVING_WHILE_PAUSED(R.string.notifMovingWhilePausedTitle, R.string.notifMovingWhilePausedDesc), ARRIVED(R.string.notifArrivedTitle, R.string.notifArrivedDesc);

		private int titleId, descriptionId;

		Notification(int titleId, int descriptionId) {
			this.titleId = titleId;
			this.descriptionId = descriptionId;
		}

		public int getTitleId() {
			return titleId;
		}

		public int getDescriptionId() {
			return descriptionId;
		}
	}

	private void sendNotification(Notification notification) {
		Log.d(TAG, "sent " + notification.name());

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(android.R.drawable.ic_menu_save).setContentTitle(notificationTexts.getString(notification.getTitleId())).setContentText(notificationTexts.getString(notification.getDescriptionId())).setAutoCancel(true);

		Intent resultIntent = new Intent(context, WalkingActivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
		builder.setContentIntent(contentIntent);

		notificationManager.notify(notification.ordinal(), builder.build());
	}
}
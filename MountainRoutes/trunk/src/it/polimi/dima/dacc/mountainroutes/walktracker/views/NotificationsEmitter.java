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

	private boolean turnedOn;
	private Context context;
	private StringRepository notificationTexts;
	private NotificationManager notificationManager;
	private Integer currentNotification;

	public NotificationsEmitter(Context context) {
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
		TrackerListenerManager.getManager(context).registerListener(this);
	}

	/**
	 * Turns off notifications and dismiss all sent notifications.
	 */
	public void dismiss() {
		if (!turnedOn) {
			return;
		}

		this.turnedOn = false;
		TrackerListenerManager.getManager(context).unregisterListener(this);

		for (Notification n : Notification.values()) {
			removeNotification(n);
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
		if (!turnedOn) {
			return;
		}

		switch (update) {
		case FAR_FROM_ROUTE:
		case GOING_BACKWARDS:
		case GPS_DISABLED:
		case MOVING_WHILE_PAUSED:
		case FORCE_QUIT:
			Notification notification = Notification.forUpdate(update);
			sendNotification(notification);
			break;

		case GPS_ENABLED:
			removeNotification(Notification.GPS_DISABLED);
			break;

		default:
			break;
		}
	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
		removeNotification(Notification.FAR_FROM_ROUTE, Notification.GOING_BACKWARDS, Notification.MOVING_WHILE_PAUSED);
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

		FAR_FROM_ROUTE(R.string.notifFarFromRouteTitle, R.string.notifFarFromRouteDesc), GOING_BACKWARDS(
				R.string.notifGoingBackwardsTitle, R.string.notifGoingBackwardsDesc), MOVING_WHILE_PAUSED(
				R.string.notifMovingWhilePausedTitle, R.string.notifMovingWhilePausedDesc), FORCE_QUIT(
				R.string.notifForceQuit, R.string.notifForceQuitDesc), ARRIVED(R.string.notifArrivedTitle,
				R.string.notifArrivedDesc), GPS_DISABLED(R.string.notifGpsDisabled, R.string.notifGpsDisabledDesc);

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

		public static Notification forUpdate(UpdateType update) {
			switch (update) {
			case FAR_FROM_ROUTE:
				return Notification.FAR_FROM_ROUTE;
			case FORCE_QUIT:
				return FORCE_QUIT;
			case GOING_BACKWARDS:
				return GOING_BACKWARDS;
			case GPS_DISABLED:
				return GPS_DISABLED;
			case MOVING_WHILE_PAUSED:
				return MOVING_WHILE_PAUSED;
			default:
				return null;
			}
		}
	}

	private void removeNotification(Notification... notifications) {
		for (Notification n : notifications) {
			if (currentNotification != null && currentNotification == n.ordinal()) {
				notificationManager.cancel(n.ordinal());
				currentNotification = null;
			}
		}
	}

	private void sendNotification(Notification notification) {
		Log.d(TAG, "sent " + notification.name());

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setSmallIcon(android.R.drawable.ic_menu_save)
				.setContentTitle(notificationTexts.getString(notification.getTitleId()))
				.setContentText(notificationTexts.getString(notification.getDescriptionId())).setAutoCancel(true);

		Intent resultIntent = new Intent(context, WalkingActivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
		builder.setContentIntent(contentIntent);

		if (currentNotification != null) {
			notificationManager.cancel(currentNotification);
		}

		notificationManager.notify(notification.ordinal(), builder.build());
		currentNotification = notification.ordinal();
	}
}
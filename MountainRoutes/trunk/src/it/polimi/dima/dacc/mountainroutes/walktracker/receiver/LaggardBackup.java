package it.polimi.dima.dacc.mountainroutes.walktracker.receiver;

import java.util.ArrayList;
import java.util.List;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.Timer;
import it.polimi.dima.dacc.mountainroutes.walktracker.Timer.Listener;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;

/***
 * {@link TrackingService} observer that saves all received updates to allow
 * observers registered after the tracking started to get the up-to-date state
 */
public class LaggardBackup implements TrackerListenerBase {

	private Route routeBeingTracked;
	private ExcursionReport excursionReport;
	private TrackingStatus trackingStatus;
	private boolean isFarFromRoute;
	private boolean isMovingWhilePaused;
	private boolean isGoingBackwards;
	private boolean isGpsEnabled;
	private int altitudeGap;
	private TrackResult lastTrackResult;

	private CompoundListener timerListeners;
	private Timer timer;

	/* Package */public LaggardBackup() {
		isGpsEnabled = true;
		timerListeners = new CompoundListener();
		timer = new Timer(timerListeners);
		trackingStatus = TrackingStatus.READY;
	}

	public boolean amILate() {
		return trackingStatus != TrackingStatus.READY;
	}

	public TrackingStatus getStatus() {
		return trackingStatus;
	}

	public Route getRouteBeingTracked() {
		return routeBeingTracked;
	}

	public TrackResult getLastTrackResult() {
		return lastTrackResult;
	}

	public ExcursionReport getReport() {
		return excursionReport;
	}

	public int getAltitudeGap() {
		return altitudeGap;
	}

	public boolean isFarFromRoute() {
		return isFarFromRoute;
	}

	public boolean isMovingWhilePaused() {
		return isMovingWhilePaused;
	}

	public boolean isGoingBackwards() {
		return isGoingBackwards;
	}

	public boolean isGpsEnabled() {
		return isGpsEnabled;
	}

	public void registerTimerListener(Listener listener) {
		timerListeners.register(listener);
	}

	public void unregisterTimerListener(Listener listener) {
		timerListeners.unregister(listener);
	}

	@Override
	public void onStartTracking(Route route) {
		this.routeBeingTracked = route;
		this.trackingStatus = TrackingStatus.TRACKING;
		timer.start();
	}

	@Override
	public void onStopTracking(ExcursionReport report) {
		this.routeBeingTracked = null;
		this.excursionReport = report;
		this.trackingStatus = TrackingStatus.FINISHED;
		timer.stop();
	}

	@Override
	public void onStatusUpdate(UpdateType update) {
		switch (update) {
		case EXCURSION_PAUSED:
			this.trackingStatus = TrackingStatus.PAUSED;
			timer.pause();
			break;
		case EXCURSION_RESUME:
			this.trackingStatus = TrackingStatus.TRACKING;
			timer.resume();
			break;
		case FAR_FROM_ROUTE:
			isFarFromRoute = true;
			break;
		case GOING_BACKWARDS:
			isGoingBackwards = true;
			break;
		case GPS_DISABLED:
			isGpsEnabled = false;
			break;
		case GPS_ENABLED:
			isGpsEnabled = true;
		case MOVING_WHILE_PAUSED:
			isMovingWhilePaused = true;
			break;
		case FORCE_QUIT:
			this.trackingStatus = TrackingStatus.ABRUPTED;
			timer.stop();
			break;
		default:
			break;
		}
	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
		this.lastTrackResult = result;
		isGoingBackwards = false;
		isMovingWhilePaused = false;
		isFarFromRoute = false;
	}

	private class CompoundListener implements Timer.Listener {

		private List<Timer.Listener> registeredListeners;

		public CompoundListener() {
			registeredListeners = new ArrayList<Timer.Listener>();
		}

		public void register(Timer.Listener listener) {
			registeredListeners.add(listener);
		}

		public void unregister(Timer.Listener listener) {
			registeredListeners.remove(listener);
		}

		@Override
		public void onTime(long millis) {
			for (Timer.Listener l : registeredListeners) {
				l.onTime(millis);
			}
		}
	}

	@Override
	public void onAltitudeGapUpdate(int altitudeGap) {
		this.altitudeGap = altitudeGap;
	}

	public static enum TrackingStatus {
		READY, TRACKING, PAUSED, FINISHED, ABRUPTED
	}
}
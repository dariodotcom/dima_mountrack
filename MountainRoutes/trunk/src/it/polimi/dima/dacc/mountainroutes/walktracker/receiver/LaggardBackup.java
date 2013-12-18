package it.polimi.dima.dacc.mountainroutes.walktracker.receiver;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;

/***
 * {@link TrackingService} observer that saves all received updates to allow
 * observers registered after the tracking started to get the up-to-date state
 */
public class LaggardBackup implements TrackerListenerBase {

	private boolean isTrackingStarted;
	private Route routeBeingTracked;
	private boolean isTrackingPaused;
	private boolean isTrackingStopped;
	private boolean isFarFromRoute;
	private boolean isMovingWhilePaused;
	private boolean isGoingBackwards;
	private boolean isGpsEnabled;
	private long elapsedSeconds;
	private float completionIndex;

	/* Package */public LaggardBackup() {
		isGpsEnabled = true;
	}

	public boolean amILate() {
		return isTrackingStarted;
	}

	public Route getRouteBeingTracked() {
		return routeBeingTracked;
	}

	public boolean isTrackingPaused() {
		return isTrackingPaused;
	}

	public boolean isTrackingStopped() {
		return isTrackingStopped;
	}

	public long getElapsedSeconds() {
		return elapsedSeconds;
	}

	public boolean isTrackingStarted() {
		return isTrackingStarted;
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

	public float getCompletionIndex() {
		return completionIndex;
	}

	@Override
	public void onStartTracking(Route route) {
		this.isTrackingStarted = true;
		this.isTrackingPaused = false;
		this.isTrackingStopped = false;
		this.routeBeingTracked = route;
	}

	@Override
	public void onStopTracking(ExcursionReport report) {
		this.routeBeingTracked = null;
		this.isTrackingPaused = false;
		this.isTrackingStopped = true;
	}

	@Override
	public void onStatusUpdate(UpdateType update) {
		switch (update) {
		case EXCURSION_PAUSED:
			isTrackingPaused = true;
			break;
		case EXCURSION_RESUME:
			isTrackingPaused = false;
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
			isTrackingStopped = true;
			break;
		default:
			break;
		}
	}

	@Override
	public void onTrackingUpdate(float completionIndex) {
		this.completionIndex = completionIndex;
		isGoingBackwards = false;
		isMovingWhilePaused = false;
		isFarFromRoute = false;
	}
}

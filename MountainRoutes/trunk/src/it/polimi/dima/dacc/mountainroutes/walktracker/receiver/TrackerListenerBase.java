package it.polimi.dima.dacc.mountainroutes.walktracker.receiver;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;

/**
 * Base interface for {@link TrackingService} observers.
 */
/* package */interface TrackerListenerBase {

	public void onStartTracking(Route route);

	public void onStopTracking(ExcursionReport report);

	public void onStatusUpdate(UpdateType update);

	public void onTrackingUpdate(TrackResult result);
	
	public void onAltitudeGapUpdate(int altitude);

}

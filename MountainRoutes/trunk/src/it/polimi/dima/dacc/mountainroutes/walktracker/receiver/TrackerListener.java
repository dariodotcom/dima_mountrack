package it.polimi.dima.dacc.mountainroutes.walktracker.receiver;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;

public interface TrackerListener {

	public void onStartTracking(Route route);
	public void onStopTracking(ExcursionReport report);
	public void onStatusUpdate(UpdateType update);
	public void onTrackingUpdate(float completionIndex);

}

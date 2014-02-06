package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.commons.RouteProgressionMapFragment;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup.TrackingStatus;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;

/**
 * Controller that manages a {@link RouteProgressionMapFragment} depending on
 * informations received from tracker service.
 * 
 * @author Chiara
 * 
 */
public class RouteProgressionController implements TrackerListener {

	private RouteProgressionMapFragment mapFragment;

	public RouteProgressionController(RouteProgressionMapFragment mapFragment) {
		this.mapFragment = mapFragment;
	}

	@Override
	public void onStartTracking(Route route) {
		mapFragment.setPath(route.getPath());
	}

	@Override
	public void onStopTracking(ExcursionReport report) {

	}

	@Override
	public void onStatusUpdate(UpdateType update) {

	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
		float completionIndex = result.getCompletionIndex();
		mapFragment.setCompletionIndex(completionIndex);
	}

	@Override
	public void onRegister(LaggardBackup backup) {
		TrackingStatus status = backup.getStatus();

		switch (status) {
		case READY:
		case ABRUPTED:
		case FINISHED:
			return;
		default:
			break;
		}

		mapFragment.setPath(backup.getRouteBeingTracked().getPath());
		TrackResult lastResult = backup.getLastTrackResult();
		if (lastResult != null) {
			mapFragment.setCompletionIndex(lastResult.getCompletionIndex());
		}
	}

	@Override
	public void onUnregister(LaggardBackup backup) {

	}

	@Override
	public void onAltitudeGapUpdate(int altitude) {

	}
}
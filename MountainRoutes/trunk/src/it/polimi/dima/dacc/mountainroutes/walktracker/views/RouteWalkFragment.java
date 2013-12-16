package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.MapFragment;

public class RouteWalkFragment extends MapFragment implements TrackerListener {

	private static final String TAG = "RouteWalkFragment";

	private ControllerWrapper ctrlWrapper;

	// Default constructor
	public RouteWalkFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctrlWrapper = new ControllerWrapper();

		if (savedInstanceState != null) {
			RouteWalkController controller = new RouteWalkController(
					savedInstanceState);
			ctrlWrapper.setController(controller);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		ctrlWrapper.saveState(outState);
	}

	@Override
	public void onStartTracking(Route route) {
		RouteWalkController controller = new RouteWalkController(route);
		ctrlWrapper.setController(controller);
	}

	@Override
	public void onStopTracking(ExcursionReport report) {
		// Do nothing
	}

	@Override
	public void onStatusUpdate(UpdateType update) {
		// Do nothing
	}

	@Override
	public void onTrackingUpdate(float completionIndex) {
		ctrlWrapper.setCompletionIndex(completionIndex);
	}

	private static class ControllerWrapper {
		private RouteWalkController controller;

		public void setController(RouteWalkController controller) {
			this.controller = controller;
		}

		public void setCompletionIndex(float completionIndex) {
			if (controller != null) {
				controller.setCompletionIndex(completionIndex);
			} else {
				Log.w(TAG, "calling setCompletionIndex but controller is null");
			}
		}

		public void saveState(Bundle out) {
			if (controller != null) {
				controller.saveState(out);
			} else {
				Log.w(TAG, "calling setCompletionIndex but controller is null");
			}
		}
	}

}
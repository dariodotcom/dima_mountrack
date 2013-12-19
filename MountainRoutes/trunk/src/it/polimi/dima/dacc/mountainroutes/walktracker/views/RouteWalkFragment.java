package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import java.util.LinkedList;
import java.util.List;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class RouteWalkFragment extends MapFragment implements TrackerListener {

	private static final String TAG = "RouteWalkFragment";

	private ControllerWrapper ctrlWrapper;
	private Polyline walkedLine, pendingLine;
	private int pendingColor, walkedColor;
	private boolean linesInitialized = false;

	// Default constructor
	public RouteWalkFragment() {
		super();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ctrlWrapper = new ControllerWrapper();

		Resources r = getActivity().getResources();
		pendingColor = r.getColor(R.color.pending_line);
		walkedColor = r.getColor(R.color.walked_line);

		getMap().setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition position) {
				Log.d("map", position.toString());
			}
		});
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
	public void onTrackingUpdate(TrackResult result) {
		ctrlWrapper.update(result);
		centerMap(result.getPointOnPath());
		updateLines();
	}

	private void centerMap(LatLng pointOnPath) {
		CameraUpdate u = CameraUpdateFactory.newLatLngZoom(pointOnPath, 19);
		getMap().moveCamera(u);
	}

	@Override
	public void onRegister(LaggardBackup backup) {
		if (backup.amILate()) {
			Route r = backup.getRouteBeingTracked();
			RouteWalkController ctrl = new RouteWalkController(r);
			ctrlWrapper.setController(ctrl);
			TrackResult lastResult = backup.getLastTrackResult();
			if (lastResult != null) {
				ctrlWrapper.update(lastResult);
			}
		}
	}

	@Override
	public void onUnregister(LaggardBackup backup) {
		// TODO Auto-generated method stub
	}

	private void updateLines() {
		if (!linesInitialized) {
			GoogleMap map = getMap();
			walkedLine = map.addPolyline(new PolylineOptions()
					.color(walkedColor));
			pendingLine = map.addPolyline(new PolylineOptions()
					.color(pendingColor));
			linesInitialized = true;
		}

		List<LatLng> walkedPath = ctrlWrapper.getWalkedPath();
		List<LatLng> pendingPath = ctrlWrapper.getPendingPath();

		if (walkedPath != null && pendingPath != null) {
			walkedLine.setPoints(walkedPath);
			pendingLine.setPoints(pendingPath);
		}
	}

	private static class ControllerWrapper {
		private RouteWalkController controller;

		public void setController(RouteWalkController controller) {
			this.controller = controller;
		}

		public void update(TrackResult result) {
			if (controller != null) {
				controller.update(result);
			} else {
				Log.w(TAG, "calling setCompletionIndex but controller is null");
			}
		}

		public List<LatLng> getWalkedPath() {
			if (controller != null) {
				return controller.getWalkedPath();
			} else {
				Log.w(TAG, "calling setCompletionIndex but controller is null");
				return null;
			}
		}

		public LinkedList<LatLng> getPendingPath() {
			if (controller != null) {
				return controller.getPendingPath();
			} else {
				Log.w(TAG, "calling setCompletionIndex but controller is null");
				return null;
			}
		}
	}

}
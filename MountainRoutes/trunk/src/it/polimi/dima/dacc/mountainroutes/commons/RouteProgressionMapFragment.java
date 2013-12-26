package it.polimi.dima.dacc.mountainroutes.commons;

import java.util.LinkedList;
import java.util.List;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.types.PointList;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class RouteProgressionMapFragment extends MapFragment {

	private static final int RADIUS = 5;
	private static final String TAG = "RouteWalkFragment";

	private ProgressingPath progressingPath;
	private LatLngBounds pathBounds; // Cache for path LatLngBounds

	private boolean shapesInitialized, zoomed;
	private Polyline walkedLine, pendingLine;
	private Marker arrow;
	private int pendingColor, walkedColor, transparent;

	// Default constructor
	public RouteProgressionMapFragment() {
		super();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Resources r = getActivity().getResources();
		pendingColor = r.getColor(R.color.pending_line);
		walkedColor = r.getColor(R.color.walked_line);
		transparent = r.getColor(android.R.color.transparent);
		shapesInitialized = false;
		zoomed = false;

		UiSettings settings = getMap().getUiSettings();
		settings.setAllGesturesEnabled(false);
		settings.setZoomControlsEnabled(false);
	}

	public void setPath(PointList pointList) {
		progressingPath = new ProgressingPath(pointList);

		// Compute bounds
		Double minLat = Double.POSITIVE_INFINITY, minLng = Double.POSITIVE_INFINITY;
		Double maxLat = Double.NEGATIVE_INFINITY, maxLng = Double.NEGATIVE_INFINITY;

		for (LatLng point : pointList) {
			if (point.latitude < minLat) {
				minLat = point.latitude;
			}

			if (point.latitude > maxLat) {
				maxLat = point.latitude;
			}

			if (point.longitude < minLng) {
				minLng = point.longitude;
			}

			if (point.longitude > maxLng) {
				maxLng = point.longitude;
			}
		}

		pathBounds = new LatLngBounds(new LatLng(minLat, minLng), new LatLng(maxLat, maxLng));

		updateShapes();
	}

	public void setCompletionIndex(float completionIndex) {
		this.progressingPath.setCompletionIndex(completionIndex);
		updateShapes();
		if (zoomed) {
			zoomToUser();
		}
	}

	public void panToPath() {
		final GoogleMap map = getMap();

		if (map == null || pathBounds == null) {
			return;
		}

		final CameraUpdate update = CameraUpdateFactory.newLatLngBounds(pathBounds, 16);
		try {
			map.moveCamera(update);
			zoomed = false;
		} catch (IllegalStateException e) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					map.moveCamera(update);
					zoomed = false;
				}
			}, 100);
		}
	}

	public void zoomToUser() {
		final GoogleMap map = getMap();
		LatLng userPosition = progressingPath.getWalked().getLast();
		if (map == null || userPosition == null) {
			return;
		}

		final CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userPosition, 18);

		try {
			map.moveCamera(update);
			zoomed = true;
		} catch (IllegalStateException e) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					map.moveCamera(update);
					zoomed = true;
				}
			}, 100);
		}

	}

	private void updateShapes() {
		if (!shapesInitialized) {
			initializeShapes();
		}

		walkedLine.setPoints(progressingPath.getWalked());
		pendingLine.setPoints(progressingPath.getPending());

		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(progressingPath.getCurrentEdge(), 18);
		getMap().moveCamera(update);

		if (progressingPath.getCompletionIndex() > 0) {
			//arrow.setVisible(true);
			arrow.setPosition(progressingPath.getCurrentEdge());
			arrow.setRotation(progressingPath.getBearing());
		}
	}

	private void initializeShapes() {
		GoogleMap map = getMap();

		walkedLine = map.addPolyline(new PolylineOptions().color(walkedColor).width(15));
		pendingLine = map.addPolyline(new PolylineOptions().color(pendingColor).width(15));

		LatLng startPoint = progressingPath.getWalked().getFirst();
		LatLng endPoint = progressingPath.getPending().getLast();

		map.addCircle(new CircleOptions().fillColor(walkedColor).strokeColor(transparent).center(startPoint).radius(RADIUS));
		map.addCircle(new CircleOptions().fillColor(pendingColor).strokeColor(transparent).center(endPoint).radius(RADIUS));

		BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.arrow);
		arrow = map.addMarker(new MarkerOptions().flat(true).draggable(false).visible(false).anchor(0.5f, 0.5f).position(startPoint).icon(icon));

		shapesInitialized = true;
	}

	private static class ProgressingPath {
		private LinkedList<LatLng> pending, walked;
		private int maxCompletionIndex;
		private float currentCompletionIndex;
		private boolean hasInterpolationPoint;
		private float bearing;

		public ProgressingPath(PointList path) {
			List<LatLng> points = path.getList();
			maxCompletionIndex = points.size() - 1;
			currentCompletionIndex = 0;
			walked = new LinkedList<LatLng>();
			pending = new LinkedList<LatLng>();

			walked.add(points.get(0));
			pending.addAll(points);
		}

		public LinkedList<LatLng> getPending() {
			return pending;
		}

		public LinkedList<LatLng> getWalked() {
			return walked;
		}

		public LatLng getCurrentEdge() {
			return walked.getLast();
		}

		public float getCompletionIndex() {
			return currentCompletionIndex;
		}

		public void setCompletionIndex(float completionIndex) {
			if (completionIndex > maxCompletionIndex) {
				throw new IllegalArgumentException("completionIndex cannot be greater than path length");
			}

			if (completionIndex < currentCompletionIndex) {
				Log.e(TAG, "Received inconsistend update");
				return;
			}

			if (hasInterpolationPoint) {
				walked.removeLast();
				pending.removeFirst();
				hasInterpolationPoint = false;
			} else {
				pending.removeFirst();
			}

			int edge = intPart(completionIndex), currentEdge = intPart(currentCompletionIndex);
			if (edge > currentEdge) {
				int n = edge - currentEdge;
				while (n > 0) {
					LatLng pivot = pending.removeFirst();
					walked.addLast(pivot);
					n--;
				}

				currentEdge = edge;
			}

			bearing = computeBearing(walked.getLast(), pending.getFirst());

			float coeff = completionIndex - edge;
			if (coeff > 0) {
				LatLng start = walked.getLast(), end = pending.getFirst();
				LatLng interpolated = interpolate(start, end, coeff);
				walked.addLast(interpolated);
				pending.addFirst(interpolated);
				hasInterpolationPoint = true;
			} else {
				LatLng last = walked.getLast();
				pending.addFirst(last);
			}

			currentCompletionIndex = completionIndex;
		}

		public float getBearing() {
			return bearing;
		}

		private static int intPart(double d) {
			return (int) Math.floor(d);
		}

		private static LatLng interpolate(LatLng start, LatLng end, float coeff) {
			double lat = start.latitude + coeff * (end.latitude - start.latitude);
			double lng = start.longitude + coeff * (end.longitude - start.longitude);
			return new LatLng(lat, lng);
		}

		private static float computeBearing(LatLng start, LatLng end) {
			double slope = (end.latitude - start.latitude) / (end.longitude - start.longitude);
			double bearing;

			if (slope == Double.POSITIVE_INFINITY) {
				bearing = Math.PI / 2;
			} else if (slope == Double.NEGATIVE_INFINITY) {
				bearing = -Math.PI / 2;
			} else {
				bearing = Math.atan(slope);
			}

			return (float) Math.toDegrees(bearing);
		}
	}
}
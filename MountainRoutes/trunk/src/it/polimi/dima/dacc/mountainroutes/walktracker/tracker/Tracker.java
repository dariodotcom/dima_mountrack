package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import java.util.List;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import it.polimi.dima.dacc.mountainroutes.types.PointList;

public class Tracker {

	private final static String TAG = "tracker";
	private static final double MAX_DISTANCE_METERS = 100;
	private List<LatLng> path;
	private PathSegment currentSegment;
	private int edge;
	private float segPercent;

	public Tracker(PointList path) {
		this.path = path.getList();
		this.edge = 0;
		this.segPercent = 0;
		this.currentSegment = nextSegment();
	}

	public TrackResult track(LatLng newPoint) throws TrackerException {
		if (isFinished()) {
			throw new IllegalStateException("Tracking is finished");
		}

		TrackResult.Builder resultBuilder = new TrackResult.Builder()
				.realPosition(newPoint);

		// Check distance from segment
		double distance = currentSegment.distanceTo(newPoint);
		Log.d(TAG, "distance from segment: " + distance);
		if (distance > MAX_DISTANCE_METERS) {
			// The user is too distant
			throw new TrackerException(TrackerException.Type.FAR_FROM_ROUTE);
		}

		// Update point
		LatLng projection = currentSegment.project(newPoint);
		Log.d(TAG, "projection: " + projection);
		float percent = currentSegment.percentOf(projection);
		Log.d(TAG, "percent: " + percent);
		if (percent < segPercent) {
			// User is going backwards
			throw new TrackerException(TrackerException.Type.GOING_BACKWARD);
		}

		if (percent >= 1) {
			// Move to the next segment
			edge++;
			segPercent = 0;
			if (isFinished()) {
				return resultBuilder.completionIndex(edge)
						.pointOnPath(path.get(edge)).build();
			}

			Log.d(TAG, "Moving to next segment");
			currentSegment = nextSegment();
			return track(newPoint); // Re-track point in new segment
		}

		segPercent = percent; // Update state;
		return resultBuilder.completionIndex(edge + segPercent)
				.pointOnPath(projection).build();
	}

	public boolean isFinished() {
		return edge == this.path.size() - 1;
	}

	private PathSegment nextSegment() {
		return PathSegment.create(path.get(edge), path.get(edge + 1));
	}
}
package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import it.polimi.dima.dacc.mountainroutes.types.PointList;

import java.util.List;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Tracks the user position on a path given the position retrieved from GPS 
 */
public class TrackerImpl extends Tracker {

	private final static String TAG = "tracker";

	private List<LatLng> path;
	private PathSegment currentSegment;
	private int edge, elapsedMeters;
	private float segPercent;

	public TrackerImpl(PointList path) {
		this.path = path.getList();
		this.edge = 0;
		this.segPercent = 0;
		this.currentSegment = nextSegment();
	}

	public TrackResult track(LatLng newPoint) throws TrackerException {
		if (isFinished()) {
			throw new IllegalStateException("Tracking is finished");
		}

		TrackResult.Builder resultBuilder = new TrackResult.Builder().realPosition(newPoint);

		// Check distance from segment
		double distance = currentSegment.distanceTo(newPoint);
		Log.d(TAG, "distance from segment: " + distance);
		if (distance > MAX_DISTANCE_KM) {
			// The user is too distant
			throw new TrackerException(TrackerException.Type.FAR_FROM_ROUTE);
		}

		// Update point
		LatLng projection = currentSegment.project(newPoint);
		float percent = currentSegment.percentOf(projection);
		if (percent < segPercent) {
			// User is going backwards
			throw new TrackerException(TrackerException.Type.GOING_BACKWARD);
		}

		if (percent >= 1) {
			// Move to the next segment
			edge++;
			segPercent = 0;
			elapsedMeters += currentSegment.getLengthInMeters();
			if (isFinished()) {
				return resultBuilder.completionIndex(edge).pointOnPath(path.get(edge)).elapsedMeters(elapsedMeters)
						.build();
			}

			Log.d(TAG, "Moving to next segment");
			currentSegment = nextSegment();
			return track(newPoint); // Re-track point in new segment
		}

		segPercent = percent; // Update state;
		int segmentLength = currentSegment.getLengthInMeters();
		return resultBuilder.completionIndex(edge + segPercent).pointOnPath(projection)
				.elapsedMeters((int) (elapsedMeters + segmentLength * percent)).build();
	}

	public boolean isFinished() {
		return edge == this.path.size() - 1;
	}

	private PathSegment nextSegment() {
		return PathSegment.create(path.get(edge), path.get(edge + 1), 0);
	}

}

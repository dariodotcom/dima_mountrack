package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import it.polimi.dima.dacc.mountainroutes.types.PointList;

public class Tracker {

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

		// Update point
		LatLng projection = currentSegment.project(newPoint);
		float percent = currentSegment.percentOf(projection);
		if (percent < segPercent) {
			// User is going backwards
			throw new TrackerException(TrackerException.Type.GOING_BACKWARD);
		}

		if (currentSegment.distanceTo(newPoint) > MAX_DISTANCE_METERS) {
			// The user is too distant
			throw new TrackerException(TrackerException.Type.FAR_FROM_ROUTE);
		}

		if (percent > 1) {
			// Move to the next segment
			edge++;
			segPercent = 0;
			if (isFinished()) {
				return new TrackResult(edge, path.get(edge));
			}

			currentSegment = nextSegment();
			return track(newPoint); // Re-track point in new segment
		}

		segPercent = percent; // Update state;

		return new TrackResult(edge + segPercent, projection);
	}

	public boolean isFinished() {
		return edge == this.path.size() - 1;
	}

	private PathSegment nextSegment() {
		return PathSegment.create(path.get(edge), path.get(edge + 1));
	}
}
package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackerException.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class TrackerImpl extends Tracker {

	private static final double MAX_DISTANCE = 0;
	private float currentCompletionIndex;
	private List<PathSegment> segments;

	public TrackerImpl(Route route) {
		this.currentCompletionIndex = 0;
		this.segments = new ArrayList<PathSegment>();

		List<LatLng> points = route.getPath().getList();
		for (int i = 0; i < points.size() - 1; i++) {
			LatLng start = points.get(i);
			LatLng end = points.get(i + 1);
			segments.add(PathSegment.create(start, end, i));
		}
	}

	@Override
	public TrackResult track(LatLng newPoint) throws TrackerException {
		SegmentComparator comparator = new SegmentComparator(newPoint);
		Collections.sort(segments, comparator);
		PathSegment best = segments.get(0);

		if (best.distanceTo(newPoint) > MAX_DISTANCE) {
			throw new TrackerException(Type.FAR_FROM_ROUTE);
		}

		float newCompletionIndex = computeCompletionIndex(best, newPoint);

		if (newCompletionIndex < currentCompletionIndex) {
			throw new TrackerException(Type.GOING_BACKWARD);
		}

		currentCompletionIndex = newCompletionIndex;
		LatLng pointOnPath = best.project(newPoint);
		return new TrackResult.Builder()
				.completionIndex(currentCompletionIndex)
				.pointOnPath(pointOnPath).realPosition(newPoint).build();
	}

	@Override
	public boolean isFinished() {
		return currentCompletionIndex == segments.size();
	}

	private class SegmentComparator implements Comparator<PathSegment> {

		public LatLng reference;

		private SegmentComparator(LatLng reference) {
			this.reference = reference;
		}

		@Override
		public int compare(PathSegment arg0, PathSegment arg1) {
			return Double.compare(arg1.distanceTo(reference),
					arg0.distanceTo(reference));
		}
	}

	private float computeCompletionIndex(PathSegment segment, LatLng point) {
		LatLng projection = segment.project(point);
		double coeff = segment.percentOf(projection);
		int index = segment.getIndex();

		if (coeff >= 1) {
			// If we are ahead the end of the best segment, we are at the
			// beginning
			// of the next segment
			return index + 1;
		} else if (coeff < 0) {
			// If we are before the beginning of the best segment, we are at the
			// beginning of it
			return index;
		} else {
			return index + (float) coeff;
		}
	}
}
package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import it.polimi.dima.dacc.mountainroutes.types.Route;

import com.google.android.gms.maps.model.LatLng;

/**
 * Segment of the path of a {@link Route}
 */
public abstract class PathSegment {

	public static PathSegment create(LatLng start, LatLng end, int index) {
		if (start.longitude == end.longitude) {
			return new VerticalPathSegment(index, start, end);
		} else if (start.latitude == end.latitude) {
			return new HorizontalSegment(index, start, end);
		} else {
			return new GenericPathSegment(index, start, end);
		}
	}

	private int index;
	private Integer lengthInMeters;
	private LatLng start, end;

	protected PathSegment(int index, LatLng start, LatLng end) {
		this.start = start;
		this.end = end;
		this.index = index;
	}

	public final int getLengthInMeters() {
		if (lengthInMeters == null) {
			double meters = GeomUtils.haversineDistance(start, end) * 1000;
			lengthInMeters = (int) Math.round(meters);
		}

		return lengthInMeters;
	};

	public final double distanceTo(LatLng p) {
		return GeomUtils.haversineDistance(p, project(p));
	}

	public final int getIndex() {
		return index;
	};

	public final float percentOf(LatLng point) {
		return GeomUtils.percent(start, end, point);
	}

	public abstract LatLng project(LatLng point);

	// Implementation for vertical segments
	private static class VerticalPathSegment extends PathSegment {

		private double fixedLongitude;

		private VerticalPathSegment(int index, LatLng start, LatLng end) {
			super(index, start, end);
			this.fixedLongitude = start.longitude;
		}

		@Override
		public LatLng project(LatLng point) {
			return new LatLng(point.latitude, fixedLongitude);
		}
	}

	// Implementation for horizontal string
	private static class HorizontalSegment extends PathSegment {

		private double fixedLatitude;

		public HorizontalSegment(int index, LatLng start, LatLng end) {
			super(index, start, end);
			this.fixedLatitude = start.latitude;
		}

		@Override
		public LatLng project(LatLng point) {
			return new LatLng(fixedLatitude, point.longitude);
		}
	}

	// Implementation for other segments
	private static class GenericPathSegment extends PathSegment {
		private double slope, perpSlope, yntercept;

		private GenericPathSegment(int index, LatLng start, LatLng end) {
			super(index, start, end);

			this.slope = GeomUtils.slope(start, end);
			this.perpSlope = GeomUtils.perpendicularSlope(slope);
			this.yntercept = GeomUtils.yntercept(start, slope);
		}

		@Override
		public LatLng project(LatLng point) {
			double perpYntercept = GeomUtils.yntercept(point, perpSlope);
			double xp = -(yntercept - perpYntercept) / (slope - perpSlope);
			double yp = perpSlope * xp + perpYntercept;

			return GeomUtils.toLatLng(xp, yp);
		}
	}
}
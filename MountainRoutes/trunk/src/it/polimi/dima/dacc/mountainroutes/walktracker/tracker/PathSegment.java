package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import com.google.android.gms.maps.model.LatLng;

public abstract class PathSegment {

	private PathSegment() {
		// No other subclasses, thanks!
	}

	public static PathSegment create(LatLng start, LatLng end, int index) {
		if (start.longitude == end.longitude) {
			return new VerticalPathSegment(start, end, index);
		} else {
			return new GenericPathSegment(start, end, index);
		}
	}

	public abstract LatLng project(LatLng point);

	public abstract float percentOf(LatLng point);

	public abstract double distanceTo(LatLng p);

	public abstract int getIndex();

	public abstract int getLengthInMeters();

	// Implementation for non vertical segments
	private static class GenericPathSegment extends PathSegment {
		private Integer length;
		private LatLng start, end;
		private double slope, perpSlope, yntercept;
		private int index;

		private GenericPathSegment(LatLng start, LatLng end, int index) {
			this.start = start;
			this.end = end;
			this.index = index;

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

		@Override
		public float percentOf(LatLng point) {
			return GeomUtils.percent(start, end, point);
		}

		@Override
		public double distanceTo(LatLng p) {
			return 0.0;
		}

		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public int getLengthInMeters() {
			if (length == null) {
				length = (int) GeomUtils.haversineDistance(start, end);
			}

			return length;
		}
	}

	// Implementation for vertical segments
	private static class VerticalPathSegment extends PathSegment {

		private Integer length;
		private LatLng start, end;
		private int index;

		private VerticalPathSegment(LatLng start, LatLng end, int index) {
			this.start = start;
			this.end = end;
			this.index = index;
		}

		@Override
		public LatLng project(LatLng point) {
			return new LatLng(point.latitude, start.longitude);
		}

		@Override
		public float percentOf(LatLng point) {
			return GeomUtils.percent(start, end, point);
		}

		@Override
		public double distanceTo(LatLng p) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public int getLengthInMeters() {
			if (length == null) {
				length = (int) GeomUtils.haversineDistance(start, end);
			}

			return length;
		}
	}
}
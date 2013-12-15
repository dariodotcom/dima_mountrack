package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import com.google.android.gms.maps.model.LatLng;

public abstract class PathSegment {

	private PathSegment() {
		// No other subclasses, thanks!
	}

	public static PathSegment create(LatLng start, LatLng end) {
		if (start.longitude == end.longitude) {
			return new VerticalPathSegment(start, end);
		} else {
			return new GenericPathSegment(start, end);
		}
	}

	public abstract LatLng project(LatLng point);

	public abstract float percentOf(LatLng point);

	public abstract double distanceTo(LatLng p);

	// Implementation for non vertical segments
	private static class GenericPathSegment extends PathSegment {
		private LatLng start, end;
		private double slope, perpSlope, yntercept;

		private GenericPathSegment(LatLng start, LatLng end) {
			this.start = start;
			this.end = end;
			this.slope = GeomUtils.slope(start, end);
			this.perpSlope = GeomUtils.perpendicularSlope(slope);
			this.yntercept = GeomUtils.yntercept(start, slope);
		}

		public LatLng project(LatLng point) {
			double perpYntercept = GeomUtils.yntercept(point, perpSlope);
			double xp = -(yntercept - perpYntercept) / (slope - perpSlope);
			double yp = perpSlope * xp + perpYntercept;

			return GeomUtils.toLatLng(xp, yp);
		}

		public float percentOf(LatLng point) {
			return GeomUtils.percent(start, end, point);
		}

		public double distanceTo(LatLng p) {
			return 0.0;
		}
	}

	// Implementation for vertical segments
	private static class VerticalPathSegment extends PathSegment {

		private LatLng start, end;

		private VerticalPathSegment(LatLng start, LatLng end) {
			this.start = start;
			this.end = end;
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
	}
}

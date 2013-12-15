package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import com.google.android.gms.maps.model.LatLng;

public class GeomUtils {

	public static double slope(LatLng p1, LatLng p2) {
		return (p1.latitude - p2.latitude) / (p1.longitude - p2.longitude);
	}

	public static double perpendicularSlope(double slope) {
		return -1 / slope;
	}

	public static double yntercept(LatLng p, double slope) {
		return p.latitude - slope * p.longitude;
	}

	public static LatLng toLatLng(double x, double y) {
		return new LatLng(y, x);
	}

	public static float percent(LatLng start, LatLng end, LatLng point) {
		Float percentX = null, percentY = null;

		if (start.longitude != end.longitude) {
			percentX = (float) ((point.longitude - start.longitude) / (end.longitude - start.longitude));
		}

		if (start.latitude != end.latitude) {
			percentY = (float) ((point.latitude - start.latitude) / (end.latitude - start.latitude));
		}

		if (percentY != null) {
			return percentY;
		} else if (percentX != null) {
			return percentX;
		} else {
			throw new IllegalArgumentException("Start and end matches");
		}
	}
}

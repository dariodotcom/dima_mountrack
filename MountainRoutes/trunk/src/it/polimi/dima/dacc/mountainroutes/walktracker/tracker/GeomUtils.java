package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import it.polimi.dima.dacc.mountainroutes.types.PointList;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

/**
 * Helper class for geometric computations
 */
public class GeomUtils {

	private static final int EARTH_RADIUS = 6371;

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

	public static double haversineDistance(LatLng p1, LatLng p2) {
		double lat2 = p2.latitude, lat1 = p1.latitude, lng2 = p2.longitude, lng1 = p1.longitude, dLat = Math.toRadians(lat2 - lat1), dLon = Math.toRadians(lng2 - lng1);

		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS * c;
	}

	public static int lengthOf(PointList path, float completionIndex) {
		int result = 0, size = path.size() - 1, index = 1;
		List<LatLng> points = path.getList();

		if (points.size() < 2) {
			return result;
		}

		for (; index < completionIndex; index++) {
			result += haversineDistance(points.get(index - 1), points.get(index));
		}

		if (completionIndex > size) {
			float percent = completionIndex - size;
			result += percent * haversineDistance(points.get(index + 1), points.get(index));
		}

		return result;
	}
}

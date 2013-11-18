package it.polimi.dima.dacc.mountainroute.commons.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class PointList implements Serializable {

	private static final long serialVersionUID = -2137265292905126445L;
	private List<Point> points;

	public PointList(List<Point> points) {
		if (points == null) {
			this.points = new ArrayList<Point>();
		}
	}

	public PointList() {
		this(null);
	}

	public void addAll(List<Point> newPoints) {
		this.points.addAll(newPoints);
	}

	public void add(Point newPoint) {
		this.points.add(newPoint);
	}

	public List<Point> getList() {
		return this.points;
	}

	public List<LatLng> toLatLngList() {
		List<LatLng> result = new ArrayList<LatLng>();

		for (Point p : this.points) {
			result.add(p.toLatLng());
		}

		return result;
	}

}

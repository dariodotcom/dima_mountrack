package it.polimi.dima.dacc.montainroute.creation;

import it.polimi.dima.dacc.mountainroute.commons.types.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrackedPoints implements Serializable {

	private static final long serialVersionUID = 3038701511461218475L;
	private List<Point> points;

	public TrackedPoints() {
		points = new ArrayList<Point>();
	}

	public void add(Point point) {
		points.add(point);
	}

	public void addAll(List<Point> points) {
		points.addAll(points);
	}

	public List<Point> getPoints() {
		return points;
	}
}

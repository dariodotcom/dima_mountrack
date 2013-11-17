package it.polimi.dima.dacc.montainroute.creation.saver;

import it.polimi.dima.dacc.montainroute.creation.TrackedPoints;
import it.polimi.dima.dacc.mountainroute.commons.types.Point;
import it.polimi.dima.dacc.mountainroute.commons.types.Route;

import java.io.Serializable;
import java.util.List;

public class CreationState implements Serializable {

	private static final long serialVersionUID = -2322487256873277585L;
	private String name;
	private Integer duration;
	private List<Point> points;

	public CreationState(TrackedPoints points) {
		if (points == null) {
			throw new RuntimeException("Points must not be null");
		}
		this.points = points.getPoints();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Route createRoute() {
		if (name == null || duration == null) {
			throw new RuntimeException("Incomplete route");
		}

		return new Route(null, name, points, duration);
	}

}

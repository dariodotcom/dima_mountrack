package it.polimi.dima.dacc.montainroute.creation.saver;

import it.polimi.dima.dacc.mountainroute.commons.types.PointList;
import it.polimi.dima.dacc.mountainroute.commons.types.Route;

import java.io.Serializable;

public class CreationState implements Serializable {

	private static final long serialVersionUID = -2322487256873277585L;
	private String name;
	private Integer duration;
	private PointList points;

	public CreationState(PointList points) {
		if (points == null) {
			throw new RuntimeException("Points must not be null");
		}
		
		this.points = points;
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

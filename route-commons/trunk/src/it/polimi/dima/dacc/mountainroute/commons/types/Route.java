package it.polimi.dima.dacc.mountainroute.commons.types;

import java.io.Serializable;
import java.util.List;

public class Route implements Serializable {

	private static final long serialVersionUID = 7113317019108624384L;

	private String name;
	private String id;
	private List<Point> route;
	private int duration;

	public Route(String id, String name, List<Point> route, int duration) {
		this.id = id;
		this.name = name;
		this.route = route;
		this.duration = duration;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public List<Point> getRoute() {
		return route;
	}

	public int getDuration() {
		return duration;
	}
}
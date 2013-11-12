package it.polimi.dima.dacc.mountainroute.commons.types;

import java.util.ArrayList;
import java.util.List;

public class RouteDescriptionList {
	private List<RouteDescription> routeDescriptions;

	public RouteDescriptionList() {
		this.routeDescriptions = new ArrayList<RouteDescription>();
	}

	public void addRouteDescription(RouteDescription desc) {
		this.routeDescriptions.add(desc);
	}

	public List<RouteDescription> getRouteDescriptions() {
		return routeDescriptions;
	}

	@Override
	public String toString() {
		return routeDescriptions.toString();
	}
}

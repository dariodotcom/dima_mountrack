package it.polimi.dima.dacc.mountainroute.commons.presentation;

import it.polimi.dima.dacc.mountainroute.commons.types.Point;
import it.polimi.dima.dacc.mountainroute.commons.types.Route;
import it.polimi.dima.dacc.mountainroute.commons.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

public class RouteLineController {
	private Polyline traversed, pending;
	private Logger log;
	private RouteStepper stepper;

	public RouteLineController(Polyline traversed, Polyline pending,
			Route route, float completeIndex) {
		this.pending = pending;
		this.traversed = traversed;
		this.log = new Logger("ROUTE_LINE_CONTROLLER");

		// Load stepper
		List<LatLng> routeLatLng = convertPointList(route.getRoute());
		this.stepper = new RouteStepper(routeLatLng, completeIndex);
	}

	public RouteLineController(Polyline traversed, Polyline pending, Route route) {
		this(traversed, pending, route, 0);
	}

	public void setCompleteIndex(float index) {
		stepper.step(index);
		updateLines();
	}

	private void updateLines() {
		traversed.setPoints(stepper.getTraversed());
		pending.setPoints(stepper.getPending());
		log.d("Lines updated");
	}

	private List<LatLng> convertPointList(List<Point> input) {
		List<LatLng> output = new ArrayList<LatLng>();
		for (Point p : input) {
			output.add(p.toLatLng());
		}

		return output;
	}
}
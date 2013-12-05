package it.polimi.dima.dacc.mountainroute.backend.filters;

import java.util.List;

import it.polimi.dima.dacc.mountainroute.backend.Utils;
import it.polimi.dima.dacc.mountainroute.backend.model.GeoPoint;
import it.polimi.dima.dacc.mountainroute.backend.model.Route;

import org.slim3.datastore.InMemoryFilterCriterion;

public class StartsNearFilter implements InMemoryFilterCriterion {

    private static final int MAX_DISTANCE = 100;
    private GeoPoint reference;

    public StartsNearFilter(GeoPoint reference) {
        super();
        this.reference = reference;
    }

    public boolean accept(Object model) {
        if (!(model instanceof Route)) {
            return false;
        }

        Route route = (Route) model;
        List<GeoPoint> points = route.getPath();

        if (points == null || points.isEmpty()) {
            return false;
        }

        GeoPoint start = points.get(0);

        return Utils.haversineDistance(start, reference) < MAX_DISTANCE;
    }
}
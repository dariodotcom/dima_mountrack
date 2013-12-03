package it.polimi.dima.dacc.mountainroute.backend.filters;

import java.util.List;

import it.polimi.dima.dacc.mountainroute.backend.model.GeoPoint;
import it.polimi.dima.dacc.mountainroute.backend.model.Route;

import org.slim3.datastore.InMemoryFilterCriterion;

public class StartsNearFilter implements InMemoryFilterCriterion {

    private static final int EARTH_RADIUS = 6371; // km
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
        List<GeoPoint> points = route.getPoints();
        
        if(points == null || points.isEmpty()){
            return false;
        }
        
        GeoPoint start = points.get(0);
        
        return haversineDistance(start, reference) < MAX_DISTANCE;
    }

    private double haversineDistance(GeoPoint p1, GeoPoint p2) {
        double lat2 = p2.getLatitude(), lat1 = p1.getLatitude(), lng2 =
            p2.getLongitude(), lng1 = p1.getLongitude(), dLat =
            Math.toRadians(lat2 - lat1), dLon = Math.toRadians(lng2 - lng1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a =
            Math.sin(dLat / 2)
                * Math.sin(dLat / 2)
                + Math.sin(dLon / 2)
                * Math.sin(dLon / 2)
                * Math.cos(lat1)
                * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

}

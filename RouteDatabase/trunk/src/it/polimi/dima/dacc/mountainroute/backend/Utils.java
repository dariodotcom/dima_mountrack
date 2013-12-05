package it.polimi.dima.dacc.mountainroute.backend;

import java.util.ArrayList;
import java.util.List;

import it.polimi.dima.dacc.mountainroute.backend.model.GeoPoint;
import it.polimi.dima.dacc.mountainroute.backend.model.Route;
import it.polimi.dima.dacc.mountainroute.backend.rest.RouteReviewXml;

public class Utils {
    private static final int EARTH_RADIUS = 6371; // km

    public static double haversineDistance(GeoPoint p1, GeoPoint p2) {
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

    public static int lengthOf(List<GeoPoint> path) {
        int result = 0;
        
        if(path.size() < 2){
            return result;
        }
        
        for (int i = 1; i < path.size(); i++) {
            result += haversineDistance(path.get(i - 1), path.get(i));
        }
        return result;
    }
    
    public static List<RouteReviewXml> createReview(List<Route> input){
        List<RouteReviewXml> result = new ArrayList<RouteReviewXml>();
        
        for(Route r : input){
            result.add(new RouteReviewXml(r));
        }
        
        return result;
    }
}

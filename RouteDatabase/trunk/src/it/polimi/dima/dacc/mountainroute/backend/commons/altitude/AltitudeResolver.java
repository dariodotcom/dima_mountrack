package it.polimi.dima.dacc.mountainroute.backend.commons.altitude;

import java.util.List;

import it.polimi.dima.dacc.mountainroute.backend.model.GeoPoint;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class AltitudeResolver {

    private static final String ENDPOINT =
        "http://maps.googleapis.com/maps/api/elevation/json?locations=%s,%s&sensor=true";

    public static double altitudeOf(GeoPoint point) {
        double lat = point.getLatitude(), lng = point.getLongitude();
        String url = String.format(ENDPOINT, lat, lng);
        WebResource resolver = Client.create().resource(url);
        Response response = resolver.get(Response.class);

        return response.getResults()[0].getElevation();
    }

    public static int altitudeGap(List<GeoPoint> path) {
        int size = path.size();
        if (size < 2) {
            return 0;
        }

        double gap = altitudeOf(path.get(0)) - altitudeOf(path.get(size - 1));

        return (int) Math.floor(gap);
    }
}

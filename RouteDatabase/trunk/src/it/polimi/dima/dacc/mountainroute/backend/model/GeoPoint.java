package it.polimi.dima.dacc.mountainroute.backend.model;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GeoPoint implements Serializable {

    private static final long serialVersionUID = 8728790139458816689L;
    private static final Pattern pattern = Pattern
        .compile("([+-]?\\d+\\.?\\d+),([+-]?\\d+\\.?\\d+)");

    private double latitude;
    private double longitude;

    public GeoPoint() {
    }

    public GeoPoint(double latitude, double longitude, int altitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static GeoPoint from(String representation) {
        GeoPoint point = new GeoPoint();

        Matcher matcher = pattern.matcher(representation);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Wrong representation");
        }
        try {
            point.latitude = Double.parseDouble(matcher.group(1));
            point.longitude = Double.parseDouble(matcher.group(2));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        return point;
    }

    @XmlElement(name = "latitude")
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @XmlElement(name = "longitude")
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

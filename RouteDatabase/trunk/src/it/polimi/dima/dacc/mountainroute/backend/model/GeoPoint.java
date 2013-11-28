package it.polimi.dima.dacc.mountainroute.backend.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GeoPoint implements Serializable {

    private static final long serialVersionUID = 8728790139458816689L;

    private double latitude;
    private double longitude;

    public GeoPoint() {
    }

    public GeoPoint(double latitude, double longitude, int altitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
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

package it.polimi.dima.dacc.mountainroute.backend.commons.altitude;

import javax.xml.bind.annotation.XmlElement;

import it.polimi.dima.dacc.mountainroute.backend.model.GeoPoint;

public class Result {

    private double elevation, resolution;
    private GeoPoint location;

    @XmlElement(name = "elevation")
    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    @XmlElement(name = "resolution")
    public double getResolution() {
        return resolution;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    @XmlElement(name = "location")
    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}

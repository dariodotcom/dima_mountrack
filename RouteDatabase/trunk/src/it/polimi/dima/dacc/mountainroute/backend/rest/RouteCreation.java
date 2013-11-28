package it.polimi.dima.dacc.mountainroute.backend.rest;

import it.polimi.dima.dacc.mountainroute.backend.model.GeoPoint;
import it.polimi.dima.dacc.mountainroute.backend.model.Route;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RouteCreation {

    private String name;
    private List<GeoPoint> points;
    private int traversalTime;

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "points")
    public List<GeoPoint> getPoints() {
        return points;
    }

    public void setPoints(List<GeoPoint> points) {
        this.points = points;
    }

    @XmlElement(name = "traversalTime")
    public int getTraversalTime() {
        return traversalTime;
    }

    public void setTraversalTime(int traversalTime) {
        this.traversalTime = traversalTime;
    }

    public Route createRoute() {
        Route r = new Route();
        r.setName(name);
        r.setAvgTraversalTimeMins(traversalTime);
        r.setPoints(points);

        return r;
    }
}

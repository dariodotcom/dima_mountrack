package it.polimi.dima.dacc.mountainroute.backend.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.appengine.api.datastore.KeyFactory;

@XmlRootElement(name = "route")
public class RouteDescriptor {

    private String name;
    private String id;
    private List<GeoPoint> points;
    private int traversalTime;

    public RouteDescriptor() {
    }

    public RouteDescriptor(Route route) {
        this.name = route.getName();
        this.id = KeyFactory.keyToString(route.getKey());
        this.points = route.getPoints();
        this.traversalTime = route.getAvgTraversalTimeMins();
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}

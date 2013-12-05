package it.polimi.dima.dacc.mountainroute.backend.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.appengine.api.datastore.KeyFactory;

import it.polimi.dima.dacc.mountainroute.backend.model.Difficulty;
import it.polimi.dima.dacc.mountainroute.backend.model.GeoPoint;
import it.polimi.dima.dacc.mountainroute.backend.model.Route;

@XmlRootElement
public class RouteXml {

    private String id;
    private String name;
    private Difficulty difficulty;
    private int durationInMinutes;
    private int lengthInMeters;
    private int gapInMeters;
    private List<GeoPoint> path;

    public RouteXml(Route route) {
        id = KeyFactory.keyToString(route.getKey());
        name = route.getName();
        difficulty = route.getDifficulty();
        durationInMinutes = route.getDurationInMinutes();
        lengthInMeters = route.getLenghtInMeters();
        gapInMeters = route.getGapInMeters();
        path = route.getPath();
    }

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "difficulty")
    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @XmlElement(name = "durationInMinutes")
    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    @XmlElement(name = "lengthInMeters")
    public int getLengthInMeters() {
        return lengthInMeters;
    }

    public void setLengthInMeters(int lengthInMeters) {
        this.lengthInMeters = lengthInMeters;
    }

    @XmlElement(name = "gapInMeters")
    public int getGapInMeters() {
        return gapInMeters;
    }

    public void setGapInMeters(int gapInMeters) {
        this.gapInMeters = gapInMeters;
    }

    @XmlElement(name = "path")
    public List<GeoPoint> getPath() {
        return path;
    }

    public void setPath(List<GeoPoint> path) {
        this.path = path;
    }

}

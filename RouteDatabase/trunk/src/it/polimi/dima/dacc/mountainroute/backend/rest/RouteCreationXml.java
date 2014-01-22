package it.polimi.dima.dacc.mountainroute.backend.rest;

import it.polimi.dima.dacc.mountainroute.backend.Utils;
import it.polimi.dima.dacc.mountainroute.backend.commons.altitude.AltitudeResolver;
import it.polimi.dima.dacc.mountainroute.backend.model.Difficulty;
import it.polimi.dima.dacc.mountainroute.backend.model.GeoPoint;
import it.polimi.dima.dacc.mountainroute.backend.model.Route;

import java.util.List;
import java.util.MissingResourceException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RouteCreationXml {

    private String name;
    private Difficulty difficulty;
    private int durationInMinutes;
    private List<GeoPoint> path;

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

    @XmlElement(name = "path")
    public List<GeoPoint> getPath() {
        return path;
    }

    public void setPath(List<GeoPoint> path) {
        this.path = path;
    }

    public Route createRoute() throws MissingResourceException {
        if (name == null
            || path == null
            || path.size() < 2
            || durationInMinutes == 0
            || difficulty == null) {
            throw new IllegalStateException();
        }

        Route route = new Route();
        route.setName(name);
        route.setPath(path);
        route.setDifficulty(difficulty);
        route.setDurationInMinutes(durationInMinutes);
        route.setLenghtInMeters(Utils.lengthInMeters(path));
        route.setGapInMeters(AltitudeResolver.altitudeGap(path));

        return route;
    }

}

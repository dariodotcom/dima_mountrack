package it.polimi.dima.dacc.mountainroute.backend.rest;

import javax.xml.bind.annotation.XmlElement;

import com.google.appengine.api.datastore.KeyFactory;

import it.polimi.dima.dacc.mountainroute.backend.model.Difficulty;
import it.polimi.dima.dacc.mountainroute.backend.model.Route;

public class RouteReviewXml {

    private String id;
    private String name;
    private Difficulty difficulty;
    private int durationInMinutes;

    public RouteReviewXml() {

    }

    public RouteReviewXml(Route route) {
        this.id = KeyFactory.keyToString(route.getKey());
        this.name = route.getName();
        this.difficulty = route.getDifficulty();
        this.durationInMinutes = route.getDurationInMinutes();
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
}

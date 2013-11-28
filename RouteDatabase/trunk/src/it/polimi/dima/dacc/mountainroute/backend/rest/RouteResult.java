package it.polimi.dima.dacc.mountainroute.backend.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.appengine.api.datastore.KeyFactory;

import it.polimi.dima.dacc.mountainroute.backend.model.Route;

@XmlRootElement
public class RouteResult {

    private String name;
    private String id;

    public RouteResult(Route route) {
        name = route.getName();
        id = KeyFactory.keyToString(route.getKey());
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

}

package it.polimi.dima.dacc.mountainroute.backend.filters;

import it.polimi.dima.dacc.mountainroute.backend.model.Route;

import org.slim3.datastore.InMemoryFilterCriterion;

public class ContainsIgnoreCase implements InMemoryFilterCriterion {

    private String term;

    public ContainsIgnoreCase(String term) {
        this.term = term.toLowerCase();
    }

    public boolean accept(Object model) {
        if (!(model instanceof Route)) {
            return false;
        }
        
        Route route = (Route) model;
        
        return route.getName().toLowerCase().contains(term);
    }

}

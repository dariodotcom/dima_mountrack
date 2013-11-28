package it.polimi.dima.dacc.mountainroute.backend.rest;

import java.util.ArrayList;
import java.util.List;

import it.polimi.dima.dacc.mountainroute.backend.meta.RouteMeta;
import it.polimi.dima.dacc.mountainroute.backend.model.Route;
import it.polimi.dima.dacc.mountainroute.backend.model.RouteDescriptor;
import it.polimi.dima.dacc.mountainroute.backend.rest.ResponseFactory.ResponseError;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.EntityNotFoundRuntimeException;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Path("/")
public class RouteController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableRoutes() {
        List<RouteResult> result = new ArrayList<RouteResult>();

        RouteMeta route = RouteMeta.get();
        Iterable<Route> availableRoute = Datastore.query(route).asIterable();

        for (Route r : availableRoute) {
            result.add(new RouteResult(r));
        }

        return ResponseFactory.from(result);
    }

    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getRouteById(@PathParam(value = "id") String id) {
        Key key;

        try {
            key = KeyFactory.stringToKey(id);
            Route r = Datastore.get(Route.class, key);
            return ResponseFactory.from(new RouteDescriptor(r));
        } catch (IllegalArgumentException e) {
            return ResponseFactory.fromError(ResponseError.BAD_KEY);
        } catch (EntityNotFoundRuntimeException e) {
            return ResponseFactory.fromError(ResponseError.ROUTE_NOT_FOUND);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewRoute(RouteCreation request) {
        Route route = request.createRoute();
        Datastore.put(route);
        return ResponseFactory.from(new RouteDescriptor(route));
    }

}

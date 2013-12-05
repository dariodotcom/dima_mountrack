package it.polimi.dima.dacc.mountainroute.backend.rest;

import java.util.List;

import it.polimi.dima.dacc.mountainroute.backend.Utils;
import it.polimi.dima.dacc.mountainroute.backend.filters.ContainsIgnoreCase;
import it.polimi.dima.dacc.mountainroute.backend.filters.StartsNearFilter;
import it.polimi.dima.dacc.mountainroute.backend.meta.RouteMeta;
import it.polimi.dima.dacc.mountainroute.backend.model.GeoPoint;
import it.polimi.dima.dacc.mountainroute.backend.model.Route;
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
@Produces(MediaType.APPLICATION_JSON)
public class RouteController {

    @GET
    public Response getAvailableRoutes() {
        RouteMeta route = RouteMeta.get();
        List<Route> availableRoute = Datastore.query(route).asList();

        return ResponseFactory.from(Utils.createReview(availableRoute));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewRoute(RouteCreationXml request) {
        Route route;
        try {
            route = request.createRoute();
        } catch (IllegalStateException e) {
            return ResponseFactory.fromError(ResponseError.BAD_ROUTE);
        }

        Key key = Datastore.put(route);
        return ResponseFactory.from(KeyFactory.keyToString(key));
    }

    @GET
    @Path("/id/{id}")
    public Response getRouteById(@PathParam(value = "id") String id) {
        Key key;

        try {
            key = KeyFactory.stringToKey(id);
            Route r = Datastore.get(Route.class, key);
            return ResponseFactory.from(new RouteXml(r));
        } catch (IllegalArgumentException e) {
            return ResponseFactory.fromError(ResponseError.BAD_KEY);
        } catch (EntityNotFoundRuntimeException e) {
            return ResponseFactory.fromError(ResponseError.ROUTE_NOT_FOUND);
        }
    }

    @GET
    @Path("/name/{name}")
    public Response getRoutesByName(@PathParam(value = "name") String name) {
        RouteMeta meta = RouteMeta.get();
        List<Route> matching =
            Datastore
                .query(meta)
                .filterInMemory(new ContainsIgnoreCase(name))
                .asList();

        return ResponseFactory.from(Utils.createReview(matching));
    }

    @GET
    @Path("/near/{location}")
    public Response getRoutesNearMe(
            @PathParam(value = "location") String location) {
        GeoPoint point;

        try {
            point = GeoPoint.from(location);
        } catch (IllegalArgumentException e) {
            return ResponseFactory.fromError(ResponseError.BAD_LOCATION);
        }

        RouteMeta meta = RouteMeta.get();
        List<Route> matching =
            Datastore
                .query(meta)
                .filterInMemory(new StartsNearFilter(point))
                .asList();

        return ResponseFactory.from(Utils.createReview(matching));
    }
}

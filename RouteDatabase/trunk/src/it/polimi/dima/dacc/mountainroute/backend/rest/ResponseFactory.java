package it.polimi.dima.dacc.mountainroute.backend.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ResponseFactory {

    public static Response fromError(ResponseError error) {
        return Response.status(error.status).entity(error.friendlyText).build();
    }

    public static Response from(Object object) {
        return Response.ok(object).build();
    }

    public static enum ResponseError {
        BAD_KEY("Route ID is not valid.", Status.BAD_REQUEST), ROUTE_NOT_FOUND(
                "Route not found", Status.NOT_FOUND), BAD_LOCATION(
                "Bad location", Status.BAD_REQUEST);

        private final String friendlyText;
        private final Status status;

        ResponseError(String friendlyText, Status status) {
            this.friendlyText = friendlyText;
            this.status = status;
        }
    }

    public static Response empty() {
        return Response.ok().build();
    }

}

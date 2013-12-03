package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescriptionList;
import it.polimi.dima.dacc.mountainroutes.contentloader.ContentErrorType;

/**
 * Interface representing a source to retrieve routes
 */
public interface RouteSource {

	public void loadRoutes(ResultObserver observer);

	public static interface ResultObserver {
		public void onResultReceived(RouteDescriptionList result);

		public void onLoadStart();

		public void onError(ContentErrorType error);
	}
}

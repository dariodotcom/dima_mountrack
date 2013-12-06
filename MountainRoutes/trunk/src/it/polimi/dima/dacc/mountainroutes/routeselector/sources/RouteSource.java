package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroutes.remotecontent.LoadError;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

/**
 * Interface representing a source to retrieve routes
 */
public interface RouteSource {

	public void loadRoutes(ResultObserver observer);

	public static interface ResultObserver {
		public void onResultReceived(RouteSummaryList result);

		public void onLoadStart();

		public void onError(LoadError error);
	}
}

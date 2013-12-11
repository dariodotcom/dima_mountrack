package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

/**
 * Interface for factories that create instances of subclasses of
 * {@link RouteSummaryLoader}
 */
public interface RouteSummaryLoaderFactory {

	/**
	 * Creates a new instance of a subclass of {@link RouteSummaryLoader}
	 * 
	 * @return - the newly created instance
	 */
	public RouteSummaryLoader createLoader();
}
package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import android.content.Context;
import android.text.Editable;
import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.RoutePersistence;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

/**
 * Subclass of {@link RouteSummaryLoader} that implements the loading of
 * <code>Route Summary</code> saved by the user in the device's persistence and
 * that optionally match a given input
 */

public class SavedLoader extends RouteSummaryLoader {

	private final static String TAG = "saved-route-loader";

	private Editable searchTerm;
	private Context context;

	/**
	 * Constructs a new instance
	 * 
	 * @param searchTerm
	 *            - the {@link Editable} containing current earch term
	 * @param context
	 *            - the {@link Context} in which the loader is executing
	 */

	public SavedLoader(Context context, Editable searchTerm) {
		super(context);
		this.context = context;
		this.searchTerm = searchTerm;
	}

	@Override
	protected String getTag() {
		return TAG;
	}

	@Override
	public LoadResult<RouteSummaryList> loadInBackground() {
		try {
			String term = searchTerm.toString();
			RoutePersistence persistence = RoutePersistence.create(context);
			RouteSummaryList result = persistence.getAvailableRoutes(term);
			return new LoadResult<RouteSummaryList>(result);
		} catch (PersistenceException e) {
			return new LoadResult<RouteSummaryList>(LoadError.INTERNAL_ERROR);
		}
	}

	/**
	 * Implementation of {@link RouteSummaryLoaderFactory} that creates
	 * instances of {@link SavedLoader} subclass.
	 */
	public static class Factory implements RouteSummaryLoaderFactory {

		private Context context;
		private Editable searchTerm;

		public Factory(Context context, Editable searchTerm) {
			super();
			this.context = context;
			this.searchTerm = searchTerm;
		}

		@Override
		public RouteSummaryLoader createLoader() {
			return new SavedLoader(context, searchTerm);
		}

	}

}
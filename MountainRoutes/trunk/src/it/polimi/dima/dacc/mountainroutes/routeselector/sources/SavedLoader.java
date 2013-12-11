package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import android.content.Context;
import android.widget.EditText;
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

	private EditText searchTermField;
	private Context context;

	/**
	 * Constructs a new instance
	 * 
	 * @param searchTermField
	 *            - the {@link EditText} containing current earch term
	 * @param context
	 *            - the {@link Context} in which the loader is executing
	 */

	public SavedLoader(Context context, EditText searchTermField) {
		super(context);
		this.context = context;
		this.searchTermField = searchTermField;
	}

	@Override
	protected String getTag() {
		return TAG;
	}

	@Override
	public LoadResult<RouteSummaryList> loadInBackground() {
		try {
			String term = searchTermField.getText().toString();
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
		private EditText searchTermField;

		public Factory(Context context, EditText searchTermField) {
			super();
			this.context = context;
			this.searchTermField = searchTermField;
		}

		@Override
		public RouteSummaryLoader createLoader() {
			return new SavedLoader(context, searchTermField);
		}

	}

}
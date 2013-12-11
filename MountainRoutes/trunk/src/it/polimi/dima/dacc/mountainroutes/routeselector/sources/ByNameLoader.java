package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.remote.RemoteContentConnector;
import it.polimi.dima.dacc.mountainroutes.remote.RemoteContentManager;
import it.polimi.dima.dacc.mountainroutes.remote.ContentQuery;
import it.polimi.dima.dacc.mountainroutes.remote.ContentQuery.QueryType;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;

/**
 * Subclass of {@link RouteSummaryLoader} that implements the loading of
 * <code>Route Summary</code> whose name matches a given input.
 */
public class ByNameLoader extends RouteSummaryLoader {

	private final static String TAG = "byname-route-loader";

	private EditText searchTermField;
	private Context context;

	/**
	 * Constructs a new instance
	 * 
	 * @param searchTerm
	 *            - the {@link Editable} containing the search term.
	 * @param context
	 *            - the {@link Context} in which the loader is executing
	 */
	public ByNameLoader(EditText searchTermField, Context context) {
		super(context);
		this.searchTermField = searchTermField;
		this.context = context;
	}

	@Override
	protected String getTag() {
		return TAG;
	}

	@Override
	public LoadResult<RouteSummaryList> loadInBackground() {
		String currentSearchTerm = searchTermField.getText().toString();

		Log.d(TAG, "Current search term: " + currentSearchTerm);

		if (currentSearchTerm.length() == 0) {
			return new LoadResult<RouteSummaryList>(LoadError.EMPTY_PARAM);
		}

		ContentQuery q = new ContentQuery(QueryType.BYNAME);
		q.getParams().putString(RemoteContentManager.NAME_PARAM,
				currentSearchTerm);
		RemoteContentConnector connector = RemoteContentManager.getInstance()
				.createConnector(context);
		return connector.executeQuery(q, RouteSummaryList.class);
	}

	/**
	 * Implementation of {@link RouteSummaryLoaderFactory} that creates
	 * instances of {@link ByNameLoader} subclass.
	 */
	public static class Factory implements RouteSummaryLoaderFactory {

		private final EditText searchTermField;
		private final Context context;

		public Factory(EditText searchTermField, Context context) {
			this.searchTermField = searchTermField;
			this.context = context;
		}

		@Override
		public RouteSummaryLoader createLoader() {
			return new ByNameLoader(searchTermField, context);
		}
	}
}

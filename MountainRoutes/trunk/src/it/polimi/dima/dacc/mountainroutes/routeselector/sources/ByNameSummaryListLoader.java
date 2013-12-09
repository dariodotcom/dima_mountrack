package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.remote.RemoteContentConnector;
import it.polimi.dima.dacc.mountainroutes.remote.RemoteContentManager;
import it.polimi.dima.dacc.mountainroutes.remote.ContentQuery;
import it.polimi.dima.dacc.mountainroutes.remote.ContentQuery.QueryType;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;
import android.content.Context;
import android.text.Editable;

public class ByNameSummaryListLoader extends SummaryListLoader {

	private final static String TAG = "byname-route-loader";

	private Editable searchTerm;
	private Context context;

	public ByNameSummaryListLoader(Editable searchTerm, Context context) {
		super(context);
		this.searchTerm = searchTerm;
		this.context = context;
	}

	@Override
	protected String getTag() {
		return TAG;
	}

	@Override
	public LoadResult<RouteSummaryList> loadInBackground() {
		String currentSearchTerm = searchTerm.toString();

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
}

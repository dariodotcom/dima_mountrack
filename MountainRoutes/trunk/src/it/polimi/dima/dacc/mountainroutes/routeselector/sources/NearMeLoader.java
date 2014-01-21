package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroutes.commons.LocationLoader;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.remote.ContentQuery;
import it.polimi.dima.dacc.mountainroutes.remote.ContentQuery.QueryType;
import it.polimi.dima.dacc.mountainroutes.remote.RemoteContentConnector;
import it.polimi.dima.dacc.mountainroutes.remote.RemoteContentManager;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

/**
 * Subclass of {@link RouteSummaryLoader} that implements the loading of
 * <code>Route Summary</code> whose starting point are near the user's current
 * position
 */
public class NearMeLoader extends RouteSummaryLoader {

	private final static String TAG = "near-me-source";

	private Context context;

	/**
	 * Constructs a new instance
	 *
	 * @param context
	 *            - the {@link Context} in which the loader is executing
	 */
	public NearMeLoader(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected String getTag() {
		return TAG;
	}

	@Override
	public LoadResult<RouteSummaryList> loadInBackground() {

		LoadResult<LatLng> result = new LocationLoader(getContext()).getLocation();
		
		if(result.getType() == LoadResult.ERROR){
			return new LoadResult<RouteSummaryList>(result.getError());
		}
		
		LatLng location = result.getResult();
		ContentQuery query = new ContentQuery(QueryType.NEAR);
		query.getParams().putParcelable(RemoteContentManager.POSITION_PARAM,
				location);
		RemoteContentConnector connector = RemoteContentManager.getInstance()
				.createConnector(context);
		return connector.executeQuery(query, RouteSummaryList.class);
	}

	/**
	 * Implementation of {@link RouteSummaryLoaderFactory} that creates
	 * instances of {@link NearMeLoader} subclass.
	 */
	public static class Factory implements RouteSummaryLoaderFactory {

		private Context context;

		public Factory(Context context) {
			super();
			this.context = context;
		}

		@Override
		public RouteSummaryLoader createLoader() {
			return new NearMeLoader(context);
		}

	}
}

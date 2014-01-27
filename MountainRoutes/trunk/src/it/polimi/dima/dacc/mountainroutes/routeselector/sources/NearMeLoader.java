package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroutes.commons.Holder;
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
	private Holder<LatLng> locationHolder;

	/**
	 * Constructs a new instance
	 * 
	 * @param context
	 *            - the {@link Context} in which the loader is executing
	 * @param locationHolder
	 *            - a {@link Holder} containing current location
	 */
	public NearMeLoader(Context context, Holder<LatLng> locationHolder) {
		super(context);
		this.context = context;
		this.locationHolder = locationHolder;
	}

	@Override
	protected String getTag() {
		return TAG;
	}

	@Override
	public LoadResult<RouteSummaryList> loadInBackground() {
		LatLng location = locationHolder.getValue();
		ContentQuery query = new ContentQuery(QueryType.NEAR);
		query.getParams().putParcelable(RemoteContentManager.POSITION_PARAM, location);
		RemoteContentConnector connector = RemoteContentManager.getInstance().createConnector(context);
		return connector.executeQuery(query, RouteSummaryList.class);
	}

	/**
	 * Implementation of {@link RouteSummaryLoaderFactory} that creates
	 * instances of {@link NearMeLoader} subclass.
	 */
	public static class Factory implements RouteSummaryLoaderFactory {

		private Context context;
		private Holder<LatLng> locationHolder;

		public Factory(Context context, Holder<LatLng> locationHolder) {
			super();
			this.context = context;
			this.locationHolder = locationHolder;
		}

		@Override
		public RouteSummaryLoader createLoader() {
			return new NearMeLoader(context, locationHolder);
		}

	}
}

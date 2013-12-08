package it.polimi.dima.dacc.mountainroutes.routeselector.listfragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.OnRouteSelected.ItemClickAdapter;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.SummaryListLoader;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummary;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

public class RouteListFragment extends Fragment implements
		LoaderCallbacks<LoadResult<RouteSummaryList>> {

	private static final int LOADER_ID = 0;
	private final static int LIST_VIEW = 0;
	private final static int MESSAGE_VIEW = 1;
	private final static int LOADING_VIEW = 2;

	private View[] panels = new View[3];

	// UI elements
	private TextView messageContainer;
	private RouteListAdapter resultAdapter;
	private ListView listView;
	private SummaryListLoader loader;

	private String emptyMessage;
	private String gpsDisabledMessage;
	private String networkDisabledMessage;
	private String generalErrorMessage;
	private String internalErrorMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.route_list_fragment_2, null);

		panels[LOADING_VIEW] = inflated.findViewById(R.id.loading_overlay);
		panels[MESSAGE_VIEW] = inflated.findViewById(R.id.message_overlay);
		panels[LIST_VIEW] = inflated.findViewById(R.id.route_list);

		this.listView = (ListView) panels[LIST_VIEW];
		this.messageContainer = (TextView) inflated
				.findViewById(R.id.message_view);
		this.resultAdapter = new RouteListAdapter(this.getActivity());

		Context context = this.getActivity();

		// Load error messages
		emptyMessage = context.getString(R.string.no_result_message);
		generalErrorMessage = context.getString(R.string.general_error_message);
		internalErrorMessage = context
				.getString(R.string.internal_error_message);
		networkDisabledMessage = context
				.getString(R.string.network_unavailable_message);
		gpsDisabledMessage = context.getString(R.string.gps_disabled_message);

		listView.setAdapter(resultAdapter);

		return inflated;
	}

	public void setLoader(SummaryListLoader loader) {
		this.loader = loader;

	}

	public void update() {
		showPanel(LOADING_VIEW);
		Loader<LoadResult<RouteSummaryList>> loader = getLoaderManager()
				.getLoader(LOADER_ID);
		if (loader != null) {
			loader.forceLoad();
		} else {
			getLoaderManager().initLoader(LOADER_ID, null, this);
		}
	}

	public void setOnRouteSelectListener(OnRouteSelected listener) {
		OnItemClickListener l = new ItemClickAdapter(listener);
		this.listView.setOnItemClickListener(l);
	}

	// Loader callbacks
	@Override
	public Loader<LoadResult<RouteSummaryList>> onCreateLoader(int arg0,
			Bundle arg1) {
		return loader;
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<RouteSummaryList>> arg0) {

	}

	@Override
	public void onLoadFinished(Loader<LoadResult<RouteSummaryList>> loader,
			LoadResult<RouteSummaryList> result) {
		switch (result.getType()) {
		case LoadResult.ERROR:
			onError(result.getError());
			break;
		case LoadResult.RESULT:
			onResultReceived(result.getResult());
			break;
		}
	}

	public void onResultReceived(RouteSummaryList result) {
		List<RouteSummary> summaries = result.asList();
		if (summaries.isEmpty()) {
			messageContainer.setText(emptyMessage);
			showPanel(MESSAGE_VIEW);
			return;
		}

		RouteListAdapter adapter = new RouteListAdapter(getActivity());
		adapter.addAll(summaries);
		adapter.notifyDataSetChanged();
		this.resultAdapter = adapter;
		this.listView.setAdapter(adapter);
		showPanel(LIST_VIEW);
	}

	public void onError(LoadError error) {
		Log.d("list-fragment", "onError called");
		String message;
		switch (error) {
		case GPS_DISABLED:
			message = gpsDisabledMessage;
			break;
		case NETWORK_UNAVAILABLE:
			message = networkDisabledMessage;
			break;
		case RESPONSE_ERROR:
		case SERVER_ERROR:
			message = internalErrorMessage;
			break;
		default:
			message = generalErrorMessage;
			break;
		}

		messageContainer.setText(message);
		showPanel(MESSAGE_VIEW);
	}

	private void showPanel(int index) {
		Log.d("list-fragment", "show panel " + index);
		for (int i = 0; i < panels.length; i++) {
			View current = panels[i];

			if (index == i) {
				showView(current);
			} else {
				hideView(current);
			}
		}
	}

	private void showView(final View view) {
		view.animate().alpha(1).setDuration(250);
	}

	private void hideView(final View view) {
		view.animate().alpha(0).setDuration(250);
	}
}

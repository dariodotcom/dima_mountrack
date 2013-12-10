package it.polimi.dima.dacc.mountainroutes.routeselector.listfragment;

import java.util.List;

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
import it.polimi.dima.dacc.mountainroutes.StringRepository;
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
	private final static String ROUTE = "route";

	private View[] panels = new View[3];

	// UI elements
	private TextView messageContainer;
	private RouteListAdapter resultAdapter;
	private ListView listView;
	private RouteSummaryList currentResult;
	private SummaryListLoader loader;

	private StringRepository strings;

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
		listView.setAdapter(resultAdapter);

		// Load error messages
		strings = new StringRepository(this.getActivity());
		strings.loadString(R.string.no_result_message);
		strings.loadString(R.string.general_error_message);
		strings.loadString(R.string.internal_error_message);
		strings.loadString(R.string.network_unavailable_message);
		strings.loadString(R.string.gps_disabled_message);

		return inflated;
	}

	@Override
	public void onViewStateRestored(Bundle savedState) {
		super.onViewStateRestored(savedState);
		if (savedState != null) {
			RouteSummaryList savedResult = savedState.getParcelable(ROUTE);
			if (savedResult != null) {
				onResultReceived(savedResult);
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (currentResult != null) {
			outState.putParcelable(ROUTE, currentResult);
		}
	}

	public void setLoader(SummaryListLoader loader) {
		this.loader = loader;
	}

	public void update() {
		showPanel(LOADING_VIEW);
		Loader<LoadResult<RouteSummaryList>> loader;
		loader = getLoaderManager().getLoader(LOADER_ID);

		if (loader != null) {
			loader.forceLoad(); // Restarts the loader manually
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

	/* -- Private Methods -- */
	public void onResultReceived(RouteSummaryList result) {
		currentResult = result;

		Log.d("list-fragment", "onResult called: " + result);
		
		List<RouteSummary> summaries = result.asList();
		if (summaries.isEmpty()) {
			String message = strings.getString(R.string.no_result_message);
			messageContainer.setText(message);
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
		Log.d("list-fragment", "onError called: " + error);
		String message;
		switch (error) {
		case EMPTY_PARAM:
			message = "";
			break;
		case GPS_DISABLED:
			message = strings.getString(R.string.gps_disabled_message);
			break;
		case NETWORK_UNAVAILABLE:
			message = strings.getString(R.string.network_unavailable_message);
			break;
		case INTERNAL_ERROR:
		case RESPONSE_ERROR:
		case SERVER_ERROR:
			message = strings.getString(R.string.internal_error_message);
			break;
		default:
			message = strings.getString(R.string.general_error_message);
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

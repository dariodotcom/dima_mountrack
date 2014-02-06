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
import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.StringRepository;
import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.OnRouteSelected.ItemClickAdapter;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.RouteSummaryLoaderFactory;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummary;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

/**
 * Fragment that is used to automatically show a list of Routes loaded through a
 * content loader
 */
public class RouteListFragment extends Fragment implements LoaderCallbacks<LoadResult<RouteSummaryList>> {

	private static final int LOADER_ID = 0;
	private ListView listView;

	private RouteSummaryLoaderFactory loaderFactory;
	private StringRepository strings;
	private FragmentController controller;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.route_list_fragment_2, null);
		listView = (ListView) inflated.findViewById(R.id.route_list);

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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		controller = new FragmentController(getView(), getActivity());
		controller.loadState(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		controller.saveState(outState);
	}

	public void setLoaderFactory(RouteSummaryLoaderFactory factory) {
		this.loaderFactory = factory;
	}

	public void showMessage(String message) {
		controller.showMessage(message);
	}

	public void update() {
		// showPanel(LOADING_VIEW);

		controller.showLoading();
		Loader<LoadResult<RouteSummaryList>> loader;
		loader = getLoaderManager().getLoader(LOADER_ID);

		if (loader != null) {
			getLoaderManager().restartLoader(LOADER_ID, null, this);
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
	public Loader<LoadResult<RouteSummaryList>> onCreateLoader(int arg0, Bundle arg1) {
		return loaderFactory != null ? loaderFactory.createLoader() : null;
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<RouteSummaryList>> arg0) {

	}

	@Override
	public void onLoadFinished(Loader<LoadResult<RouteSummaryList>> loader, LoadResult<RouteSummaryList> result) {
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
		Log.d("list-fragment", "onResult called: " + result);

		List<RouteSummary> summaries = result.asList();
		if (summaries.isEmpty()) {
			String message = strings.getString(R.string.no_result_message);
			controller.showMessage(message);
			return;
		}

		controller.showMessage("");
		controller.showResult(result);
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

		controller.showMessage(message);
	}
}
package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.routeselector.RouteSelector;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.OnRouteSelected;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.SavedLoader;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummary;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Fragment that contains the page that allows the user to retrieve the routes
 * available the device's persistence.
 */
public class SavedPage extends Fragment {

	private RouteListFragment fragment;
	private EditText searchTermField;
	private SearchController searchController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View inflated = inflater.inflate(R.layout.page_saved, null);
		searchTermField = (EditText) inflated
				.findViewById(R.id.saved_route_search_term);
		return inflated;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragment = (RouteListFragment) getFragmentManager().findFragmentById(
				R.id.saved_list_fragment);

		fragment.setOnRouteSelectListener(new OnRouteSelected() {

			@Override
			public void onRouteSelected(RouteSummary summary) {
				RouteSelector selector = (RouteSelector) getActivity();
				selector.startViewer(summary.getId());
			}
		});

		searchController = new SearchController(searchTermField, fragment);
		fragment.setLoaderFactory(new SavedLoader.Factory(getActivity(),
				searchTermField));
		fragment.update();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		searchController.startListening();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		searchController.stopListening();
	}
}

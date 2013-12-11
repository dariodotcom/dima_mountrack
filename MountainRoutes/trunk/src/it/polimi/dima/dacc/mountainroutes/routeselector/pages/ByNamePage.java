package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.routeselector.RouteSelector;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.OnRouteSelected;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.ByNameLoader;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummary;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Fragments that contains the page that allows the user to search among routes
 * available in the currently selected data source.
 */
public class ByNamePage extends Fragment {

	private RouteListFragment fragment;
	private EditText searchTermField;
	private SearchController searchController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.page_by_name, null);
		searchTermField = (EditText) inflated.findViewById(R.id.search_term);
		return inflated;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragment = (RouteListFragment) getFragmentManager().findFragmentById(
				R.id.by_name_list_fragment);
		searchController = new SearchController(searchTermField, fragment);
	}

	@Override
	public void onStart() {
		super.onStart();
		fragment.setLoaderFactory(new ByNameLoader.Factory(searchTermField,
				getActivity()));
		fragment.setOnRouteSelectListener(new OnRouteSelected() {

			@Override
			public void onRouteSelected(RouteSummary summary) {
				RouteSelector selector = (RouteSelector) getActivity();
				selector.startViewer(summary.getId());
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		searchController.startListening();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		searchController.stopListening();
	}

}

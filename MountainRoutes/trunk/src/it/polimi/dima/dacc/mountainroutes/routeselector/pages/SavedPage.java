package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.SavedRouteListLoader;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.SummaryListLoader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SavedPage extends Fragment {

	private RouteListFragment fragment;
	private EditText searchTermField;

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
		SearchController sc = new SearchController(searchTermField, fragment);
		sc.addListener();

		SummaryListLoader loader = new SavedRouteListLoader(this.getActivity(),
				searchTermField.getText());
		fragment.setLoader(loader);
		fragment.update();
	}
}

package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.routeselector.RouteSelector;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.OnRouteSelected;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.ByNameSummaryListLoader;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.SummaryListLoader;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummary;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Fragments that contains the page that allows the user to search among routes
 * available in the currently selected datasource. 
 */
public class ByNamePage extends Fragment {

	private final static int UPDATE_DELAY_MILLIS = 250;

	private Handler resultUpdateScheduler = new Handler();
	private RouteListFragment fragment;
	private Editable searchTerm;
	private EditText searchTermField;

	private Runnable resultUpdater = new Runnable() {
		@Override
		public void run() {
			fragment.update();
		}
	};

	private OnKeyListener searchTermListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			resultUpdateScheduler.removeCallbacks(resultUpdater);
			resultUpdateScheduler.postDelayed(resultUpdater,
					UPDATE_DELAY_MILLIS);
			return false;
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.page_by_name, null);

		searchTermField = (EditText) inflated.findViewById(R.id.search_term);
		searchTerm = searchTermField.getText();

		return inflated;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		super.onStart();
		fragment = (RouteListFragment) getFragmentManager().findFragmentById(
				R.id.by_name_list_fragment);
		SummaryListLoader loader = new ByNameSummaryListLoader(searchTerm,
				this.getActivity());
		fragment.setLoader(loader);
		fragment.setOnRouteSelectListener(new OnRouteSelected() {

			@Override
			public void onRouteSelected(RouteSummary summary) {
				RouteSelector selector = (RouteSelector) getActivity();
				selector.startViewer(summary.getId());
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		searchTermField.setOnKeyListener(searchTermListener);
	}

}

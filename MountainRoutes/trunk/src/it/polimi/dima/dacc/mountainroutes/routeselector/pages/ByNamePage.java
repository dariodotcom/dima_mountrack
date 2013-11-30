package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.DummySource;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ByNamePage extends Fragment {

	RouteListFragment fragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.page_by_name, null);

		return inflated;
	}

	@Override
	public void onStart() {
		super.onStart();
		fragment = (RouteListFragment) getFragmentManager()
				.findFragmentById(R.id.by_name_list_fragment);
		fragment.setSource(new DummySource());
		fragment.update();
	}

}

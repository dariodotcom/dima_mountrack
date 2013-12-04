package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.NearMeSource;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.RouteSource;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NearMePage extends Fragment {

	private RouteListFragment fragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.page_near_me, null);

		return inflated;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragment = (RouteListFragment) getFragmentManager().findFragmentById(
				R.id.near_me_list_fragment);
		RouteSource source = new NearMeSource(getActivity());
		fragment.setSource(source);
		fragment.update();
	}

}

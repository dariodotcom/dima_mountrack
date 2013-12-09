package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.NearMeSummaryListLoader;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.SummaryListLoader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class NearMePage extends Fragment {

	private Button updateButton;
	private RouteListFragment fragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.page_near_me, null);
		updateButton = (Button) inflated.findViewById(R.id.update_button);
		return inflated;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragment = (RouteListFragment) getFragmentManager().findFragmentById(
				R.id.near_me_list_fragment);
		SummaryListLoader loader = new NearMeSummaryListLoader(getActivity());
		fragment.setLoader(loader);
		fragment.update();

		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fragment.update();
			}
		});
	}
}

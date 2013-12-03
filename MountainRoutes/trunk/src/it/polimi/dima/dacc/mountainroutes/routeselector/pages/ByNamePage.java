package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.ByNameSource;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.RouteSource;
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

public class ByNamePage extends Fragment {

	private final static int UPDATE_DELAY_MILLIS = 250;

	private Handler resultUpdateScheduler = new Handler();
	private RouteListFragment fragment;
	private Editable searchTerm;

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

		EditText searchTermField = (EditText) inflated
				.findViewById(R.id.search_term);
		searchTermField.setOnKeyListener(searchTermListener);
		searchTerm = searchTermField.getText();

		return inflated;
	}

	@Override
	public void onStart() {
		super.onStart();
		fragment = (RouteListFragment) getFragmentManager().findFragmentById(
				R.id.by_name_list_fragment);

		RouteSource source = new ByNameSource(searchTerm, this.getActivity());
		fragment.setSource(source);
	}

}

package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Controller that adds a listener to a {@link TextView} to automatically update
 * a {@link RouteListFragment}. The fragment is automatically updated after the
 * user stops typing, after a small delay.
 */
public class SearchController {

	private static final int UPDATE_DELAY_MILLIS = 250;
	private RouteListFragment fragment;
	private Handler resultUpdateScheduler = new Handler();
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

	public SearchController(EditText searchTermField, RouteListFragment fragment) {
		this.fragment = fragment;
		this.searchTermField = searchTermField;
	}

	public void addListener() {
		searchTermField.setOnKeyListener(searchTermListener);
	}

	public void removeListener() {
		// TODO find out how to remove listener
	}

}

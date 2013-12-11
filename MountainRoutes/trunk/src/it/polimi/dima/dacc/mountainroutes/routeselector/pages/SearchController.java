package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
	
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			resultUpdateScheduler.removeCallbacks(resultUpdater);
			resultUpdateScheduler.postDelayed(resultUpdater,
					UPDATE_DELAY_MILLIS);
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
		}
	};

	public SearchController(EditText searchTermField, RouteListFragment fragment) {
		this.fragment = fragment;
		this.searchTermField = searchTermField;
	}

	public void startListening() {
		searchTermField.addTextChangedListener(watcher);
	}

	public void stopListening() {
		searchTermField.removeTextChangedListener(watcher);
		resultUpdateScheduler.removeCallbacks(resultUpdater);
	}

}

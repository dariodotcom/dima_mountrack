package it.polimi.dima.dacc.mountainroutes.routeselector;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.routeselector.pages.SelectorPagerAdapter;
import it.polimi.dima.dacc.mountainroutes.routeviewer.RouteViewer;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

/**
 * Activity that shows the user a list of routes he can choose among. The view
 * is divided into three pages
 * <ul>
 * <li>BY NAME: search by name in currently selected datasource;</li>
 * <li>NEAR ME: displays routes from currently selected datasource that are near
 * the user;</li>
 * <li>SAVED: displays routes that have been saved to the device's persistence
 * by the user</li>
 * </ul>
 * */
public class RouteSelector extends FragmentActivity implements
		ActionBar.TabListener {

	public static final String SELECTED_ROUTE = "selected-route";
	public static final int VIEWER_REQUEST_CODE = 0;

	private SelectorPagerAdapter pagerAdapter;
	private ViewPager viewPager;

	private final static String TAG = "route-selector";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_selector);

		pagerAdapter = new SelectorPagerAdapter(getSupportFragmentManager(),
				this, true);

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(pagerAdapter);

		 // Bind the widget to the adapter
		 PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		 tabs.setViewPager(viewPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.complete_route_selector, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		switch (requestCode) {
		case VIEWER_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Route result = (Route) intent
						.getParcelableExtra(RouteViewer.ROUTE);
				Intent i = new Intent();
				i.putExtra(SELECTED_ROUTE, result);
				setResult(RESULT_OK, i);
				finish();
			}
			break;
		}
	}

	/**
	 * Method that is called by fragments to start {@link RouteViewer} class to
	 * display the route the user has selected.
	 * 
	 * @param id
	 *            - the {@link RouteID} of the selected route
	 */
	public void startViewer(RouteID id) {
		Intent i = new Intent(this, RouteViewer.class);
		i.putExtra(RouteViewer.ROUTE_ID, id);
		startActivityForResult(i, VIEWER_REQUEST_CODE);
	}
}

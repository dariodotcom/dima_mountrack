package it.polimi.dima.dacc.mountainroutes;

import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class TestActivity extends FragmentActivity {

	private RouteListFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		fragment = (RouteListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.test_list_fragment);
		fragment.update();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

}

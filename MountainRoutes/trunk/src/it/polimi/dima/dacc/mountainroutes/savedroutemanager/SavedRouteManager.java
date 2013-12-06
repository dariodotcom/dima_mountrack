package it.polimi.dima.dacc.mountainroutes.savedroutemanager;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.R.layout;
import it.polimi.dima.dacc.mountainroutes.R.menu;
import it.polimi.dima.dacc.mountainroutes.remotecontent.LoadResult;
import it.polimi.dima.dacc.mountainroutes.remotecontent.LoaderResult;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;
import android.app.LoaderManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Loader;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SavedRouteManager extends Activity implements
		LoaderManager.LoaderCallbacks<LoadResult<RouteSummaryList>> {

	private SavedRouteLoader loader;
	private ListView list;
	private ListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_route_manager);
		list = (ListView) findViewById(R.id.saved_route_list);
		adapter = new SavedRouteListAdapter(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved_route_manager, menu);
		return true;
	}

	@Override
	public Loader<LoadResult<RouteSummaryList>> onCreateLoader(int arg0,
			Bundle arg1) {
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<RouteSummaryList>> arg0,
			LoadResult<RouteSummaryList> result) {
		switch (result.getType()) {
		case LoadResult.ERROR:

			break;

		case LoadResult.RESULT:

			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<RouteSummaryList>> arg0) {
		// TODO Auto-generated method stub

	}

}

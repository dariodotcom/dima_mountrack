package it.polimi.dima.dacc.mountainroutes.savedroutemanager;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.savedroutemanager.SavedRouteListAdapter.onDeleteRouteListener;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;
import android.app.LoaderManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class SavedRouteManager extends Activity implements
		LoaderManager.LoaderCallbacks<LoadResult<RouteSummaryList>>,
		onDeleteRouteListener {

	private final static String TAG = "saved-route-manager";
	private final static int LOADER_ID = 1;

	private SavedRouteLoader loader;
	private ListView list;
	private SavedRouteListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_route_manager);
		list = (ListView) findViewById(R.id.saved_route_list);
		adapter = new SavedRouteListAdapter(this);
		list.setAdapter(adapter);

		adapter.setOnDeleteRouteListener(this);
		
		Log.d(TAG, "starting loader...");
		loader = new SavedRouteLoader(this);
		getLoaderManager().initLoader(LOADER_ID, null, this);
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
		Log.d(TAG, "loader initialized");
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<RouteSummaryList>> arg0,
			LoadResult<RouteSummaryList> result) {
		switch (result.getType()) {
		case LoadResult.ERROR:
			Log.d(TAG, "ERROR: " + result.getError());
			break;
		case LoadResult.RESULT:
			RouteSummaryList list = result.getResult();

			if (list.isEmpty()) {

			} else {
				this.adapter.clear();
				this.adapter.addAll(list.asList());
				this.adapter.notifyDataSetChanged();
			}

			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<RouteSummaryList>> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDelete(RouteID id) {
		Log.d(TAG, "Deleting route " + id);
	}

}

package it.polimi.dima.dacc.mountainroutes.savedroutemanager;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.routeselector.RouteSelector;
import it.polimi.dima.dacc.mountainroutes.routeviewer.RouteViewer;
import it.polimi.dima.dacc.mountainroutes.savedroutemanager.SavedRouteListAdapter.onDeleteRouteListener;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummary;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;
import android.app.LoaderManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * Activity that displays the list of available Routes and allows the user to
 * select or delete one.
 */
public class SavedRouteManager extends Activity implements LoaderManager.LoaderCallbacks<LoadResult<RouteSummaryList>>,
		onDeleteRouteListener, OnItemClickListener {

	private final static String TAG = "saved-route-manager";
	private final static int LIST_LOADER_ID = 0;
	private final static int DELETE_LOADER_ID = 1;
	private final static int VIEWER_REQUEST_CODE = 0;
	private final static String ROUTE_TO_DELETE = "route_to_delete";

	private ListView list;
	private SavedRouteListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_route_manager);

		list = (ListView) findViewById(R.id.saved_route_list);
		adapter = new SavedRouteListAdapter(this);
		TextView emptyView = (TextView) findViewById(R.id.empty_list_message_text);

		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		list.setEmptyView(emptyView);
		adapter.setOnDeleteRouteListener(this);

		getLoaderManager().initLoader(LIST_LOADER_ID, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved_route_manager, menu);
		return true;
	}

	@Override
	public Loader<LoadResult<RouteSummaryList>> onCreateLoader(int loaderId, Bundle args) {
		switch (loaderId) {
		case LIST_LOADER_ID:
			return new SavedRouteLoader(SavedRouteManager.this);
		case DELETE_LOADER_ID:
			RouteID id = args.getParcelable(ROUTE_TO_DELETE);
			return new SavedRouteDeleteLoader(this, id);
		default:
			Log.e(TAG, "No loader for id " + loaderId);
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<RouteSummaryList>> arg0, LoadResult<RouteSummaryList> result) {
		Log.d(TAG, "loader finished");
		switch (result.getType()) {
		case LoadResult.ERROR:
			Log.e(TAG, "ERROR: " + result.getError());
			break;

		case LoadResult.RESULT:
			RouteSummaryList list = result.getResult();
			this.adapter.clear();
			this.adapter.addAll(list.asList());
			this.adapter.notifyDataSetChanged();
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<RouteSummaryList>> arg0) {

	}

	// Handles the click on the delete button of a Route
	@Override
	public void onDelete(RouteID id) {
		Bundle b = new Bundle();
		b.putParcelable(ROUTE_TO_DELETE, id);
		getLoaderManager().restartLoader(DELETE_LOADER_ID, b, this).forceLoad();
	}

	// Handles the click on a Route from the list
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long resId) {
		Object o = parent.getItemAtPosition(position);
		RouteSummary summary = (RouteSummary) o;

		Intent i = new Intent(this, RouteViewer.class);
		i.putExtra(RouteViewer.ROUTE_ID, summary.getId());
		startActivityForResult(i, VIEWER_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case VIEWER_REQUEST_CODE:
			if (resultCode != RESULT_OK) {
				// Update list
				getLoaderManager().restartLoader(LIST_LOADER_ID, null, this);
				return;
			}

			Route result = data.getParcelableExtra(RouteViewer.ROUTE);
			Intent i = new Intent();
			i.putExtra(RouteSelector.SELECTED_ROUTE, result);
			setResult(RESULT_OK, i);
			finish();
		}
	}

}

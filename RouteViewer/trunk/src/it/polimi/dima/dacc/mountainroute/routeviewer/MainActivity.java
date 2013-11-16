package it.polimi.dima.dacc.mountainroute.routeviewer;

import it.polimi.dima.dacc.mountainroute.commons.connector.StorageClient;
import it.polimi.dima.dacc.mountainroute.commons.connector.StorageClient.ResultCallback;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.AvailableRoutesQuery;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.Query;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.Query.QueryType;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.QueryResult;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.RouteQuery;
import it.polimi.dima.dacc.mountainroute.commons.types.Route;
import it.polimi.dima.dacc.mountainroute.commons.types.RouteDescription;
import it.polimi.dima.dacc.mountainroute.commons.types.RouteDescriptionList;
import it.polimi.dima.dacc.mountainroute.routeviewer.R;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ViewSwitcher;

public class MainActivity extends Activity implements ResultCallback {

	private ViewSwitcher switcher;
	private ArrayAdapter<RouteDescription> routeAdapter;
	private List<RouteDescription> routeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Load view elements
		this.switcher = (ViewSwitcher) findViewById(R.id.switcher);
		ListView routeListView = (ListView) findViewById(R.id.route_list);

		// Load Adapter
		this.routeAdapter = new ArrayAdapter<RouteDescription>(this,
				android.R.layout.simple_list_item_1);

		routeListView.setAdapter(this.routeAdapter);
		routeListView.setOnItemClickListener(this.routeClickListener);

		// Start query to retrieve element
		Query q = new AvailableRoutesQuery();
		new StorageClient(this).execute(q);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResult(QueryResult result) {
		switch (result.getOriginalQuery().getType()) {
		case AVAILABLE:
			// Populate view
			this.routeList = result.as(RouteDescriptionList.class)
					.getRouteDescriptions();

			this.routeAdapter.addAll(this.routeList);
			this.routeAdapter.notifyDataSetChanged();

			// Switch to list view;
			this.switcher.showNext();

			break;
		case ID:
			Route route = result.as(Route.class);

			if (route == null) {
				throw new RuntimeException("Received null route.");
			}

			Intent intent = new Intent(this, ShowRouteActivity.class);
			intent.putExtra(ShowRouteActivity.ROUTE_TO_SHOW_KEY, route);

			Log.d("REQUEST: ", result.as(Route.class).toString());

			startActivity(intent);

			break;
		default:
			throw new RuntimeException("Not yet implemented");
		}
	}

	private OnItemClickListener routeClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int index,
				long rowId) {

			String id = routeList.get(index).getId();

			Query q = new RouteQuery(id);
			new StorageClient(MainActivity.this).execute(q);
		}
	};
}

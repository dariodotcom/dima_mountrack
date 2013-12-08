package it.polimi.dima.dacc.mountainroutes.routeviewer;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.RoutePersistence;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;

import android.os.Bundle;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RouteViewer extends Activity implements
		LoaderManager.LoaderCallbacks<LoadResult<Route>> {

	private static final String ROUTE = "ROUTE";
	public static final String ROUTE_ID = "ROUTE_ID";

	private Route displayedRoute;
	private RouteViewerFragment fragment;
	private View overlay;
	private Button saveButton;

	private DifficultyView difficultyView;
	private TextView lengthView;
	private TextView gapView;
	private TextView durationView;

	private RouteLoader loader;

	private class RouteSaver implements OnClickListener {

		private Route routeToSave;

		public RouteSaver(Route r) {
			this.routeToSave = r;
		}

		@Override
		public void onClick(View v) {
			RoutePersistence pers = RoutePersistence.create(RouteViewer.this);
			if (pers.hasRoute(routeToSave.getId())) {
				throw new IllegalStateException("Route " + routeToSave.getId()
						+ " already saved");
			}

			try {
				pers.persistRoute(routeToSave);

			} catch (PersistenceException e) {
				Log.d("route-viewer", "Exception while persisting class");
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_route_viewer);

		difficultyView = (DifficultyView) findViewById(R.id.difficulty_value);
		lengthView = (TextView) findViewById(R.id.lenght_value);
		gapView = (TextView) findViewById(R.id.gap_value);
		durationView = (TextView) findViewById(R.id.estimated_time_value);
		overlay = findViewById(R.id.overlay);
		fragment = (RouteViewerFragment) getFragmentManager().findFragmentById(
				R.id.viewer_map);

		saveButton = (Button) findViewById(R.id.route_viewer_save);

		// Load route from remote
		Log.d("viewer", "" + getIntent().getParcelableExtra(ROUTE_ID));
		RouteID id = (RouteID) getIntent().getParcelableExtra(ROUTE_ID);

		// Initialize loader
		loader = new RouteLoader(this, id);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putParcelable(ROUTE, displayedRoute);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route_viewer, menu);
		return true;
	}

	private void displayRoute(Route route) {
		this.displayedRoute = route;

		// Add save button listener
		RouteSaver saver = new RouteSaver(route);
		this.saveButton.setOnClickListener(saver);

		// Set infos
		difficultyView.setDifficulty(route.getDifficulty());
		gapView.setText(route.getGapInMeters() + " m");
		lengthView.setText(route.getLengthInMeters() + " m");
		durationView.setText(route.getDurationInMinutes() + "min");
		fragment.showRoute(route.getPath());

		// Hide overlay
		overlay.animate().alpha(0).setDuration(250);
	}

	@Override
	public Loader<LoadResult<Route>> onCreateLoader(int id, Bundle args) {
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<Route>> loader,
			LoadResult<Route> result) {
		switch (result.getType()) {
		case LoadResult.RESULT:
			this.displayRoute(result.getResult());
			break;
		case LoadResult.ERROR:

			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<Route>> arg0) {
		// TODO Auto-generated method stub

	}
}
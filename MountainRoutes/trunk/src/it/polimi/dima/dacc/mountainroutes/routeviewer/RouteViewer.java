package it.polimi.dima.dacc.mountainroutes.routeviewer;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.StringRepository;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;

import android.os.Bundle;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RouteViewer extends Activity implements
		LoaderManager.LoaderCallbacks<LoadResult<Route>> {

	private static final String TAG = "route-viewer";

	public static final int ROUTE_RESULT = 0;
	public static final String ROUTE = "ROUTE";
	public static final String ROUTE_ID = "ROUTE_ID";

	private static final int LOAD_ROUTE_LOADER_ID = 0;
	private static final int SAVE_ROUTE_LOADER_ID = 1;
	private static final int DELETE_ROUTE_LOADER_ID = 2;

	private final static int TOAST_DURATION = Toast.LENGTH_SHORT;

	private Route displayedRoute;
	private RouteViewerFragment fragment;
	private View overlay;

	private Button startButton;
	private Button saveButton;
	private Button deleteButton;

	private DifficultyView difficultyView;
	private TextView lengthView;
	private TextView gapView;
	private TextView durationView;

	private RouteLoader loader;

	private StringRepository stringRepo;

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_route_viewer);

		Log.d(TAG, "onCreate");

		// Load UI Components
		difficultyView = (DifficultyView) findViewById(R.id.difficulty_value);
		lengthView = (TextView) findViewById(R.id.lenght_value);
		gapView = (TextView) findViewById(R.id.gap_value);
		durationView = (TextView) findViewById(R.id.estimated_time_value);
		overlay = findViewById(R.id.overlay);
		fragment = (RouteViewerFragment) getFragmentManager().findFragmentById(
				R.id.viewer_map);

		startButton = (Button) findViewById(R.id.route_viewer_start);
		saveButton = (Button) findViewById(R.id.route_viewer_save);
		deleteButton = (Button) findViewById(R.id.route_viewer_delete);

		// Load strings
		stringRepo = new StringRepository(this);

		stringRepo.loadString(R.string.error_deleting_route);
		stringRepo.loadString(R.string.error_saving_route);
		stringRepo.loadString(R.string.route_deleted);
		stringRepo.loadString(R.string.route_saved);

		// Load route from remote
		if (savedState != null && savedState.getParcelable(ROUTE) != null) {
			Route r = (Route) savedState.getParcelable(ROUTE);
			displayRoute(r);
		} else {
			RouteID id = (RouteID) getIntent().getParcelableExtra(ROUTE_ID);
			loader = new RouteLoader(this, id);
			getLoaderManager().initLoader(LOAD_ROUTE_LOADER_ID, null, this);
		}
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Save displayed route to restore it later
		super.onSaveInstanceState(outState);
		outState.putParcelable(ROUTE, displayedRoute);
		Log.d(TAG, "onSaveInstanceState");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route_viewer, menu);
		return true;
	}

	/* -- START WALKING -- */
	private OnClickListener startButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent i = new Intent();
			i.putExtra(ROUTE, displayedRoute);
			setResult(RESULT_OK, i);
			finish();
		}
	};

	/* -- ROUTE DISPLAY -- */
	private void displayRoute(Route route) {
		this.displayedRoute = route;

		// Add save button listener
		this.saveButton.setOnClickListener(saveButtonClickListener);
		this.deleteButton.setOnClickListener(deleteButtonClickListener);
		this.startButton.setOnClickListener(startButtonClickListener);

		// Set infos
		setTitle(route.getName());
		difficultyView.setDifficulty(route.getDifficulty());
		gapView.setText(route.getGapInMeters() + " m");
		lengthView.setText(route.getLengthInMeters() + " m");
		durationView.setText(route.getDurationInMinutes() + "min");
		fragment.showRoute(route.getPath());

		// Show save/delete button
		if (route.getSource() == Route.Source.STORAGE) {
			deleteButton.setVisibility(View.VISIBLE);
		} else {
			saveButton.setVisibility(View.VISIBLE);
		}

		// Hide overlay
		overlay.animate().alpha(0).setDuration(250);
	}

	// Method to call when the route loader has finished loading
	private void onRouteLoaded(LoadResult<Route> loadResult) {
		switch (loadResult.getType()) {
		case LoadResult.RESULT:
			this.displayRoute(loadResult.getResult());
			break;
		case LoadResult.ERROR:

			break;
		}
	}

	/* -- ROUTE SAVE -- */
	private OnClickListener saveButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// Disable save button
			saveButton.setEnabled(false);

			// Start loader
			LoaderManager manager = RouteViewer.this.getLoaderManager();
			manager.initLoader(SAVE_ROUTE_LOADER_ID, null, RouteViewer.this);
		}
	};

	private void onRouteSaved(LoadResult<Route> loadResult) {
		switch (loadResult.getType()) {
		case LoadResult.ERROR:
			Toast.makeText(this,
					stringRepo.getString(R.string.error_saving_route),
					TOAST_DURATION).show();
			break;
		case LoadResult.RESULT:
			// Make tost
			Toast.makeText(this, stringRepo.getString(R.string.route_saved),
					TOAST_DURATION).show();

			// Hide save button
			saveButton.setVisibility(View.GONE);

			// Show delete button
			deleteButton.setVisibility(View.VISIBLE);
		}
	}

	/* -- ROUTE DELETE -- */
	private OnClickListener deleteButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// Disable save button
			deleteButton.setEnabled(false);

			// Start loader
			LoaderManager manager = RouteViewer.this.getLoaderManager();
			manager.initLoader(DELETE_ROUTE_LOADER_ID, null, RouteViewer.this);
		}
	};

	private void onRouteDeleted(LoadResult<Route> loadResult) {
		switch (loadResult.getType()) {
		case LoadResult.ERROR:
			Toast.makeText(this,
					stringRepo.getString(R.string.error_deleting_route),
					TOAST_DURATION).show();
			break;
		case LoadResult.RESULT:
			// Show Toast
			Toast.makeText(this, stringRepo.getString(R.string.route_deleted),
					TOAST_DURATION).show();
			// Hide save button
			saveButton.setVisibility(View.VISIBLE);

			// Show delete button
			deleteButton.setVisibility(View.GONE);
		}
	}

	// LoaderManager methods
	@Override
	public Loader<LoadResult<Route>> onCreateLoader(int id, Bundle args) {
		switch (id) {
		case LOAD_ROUTE_LOADER_ID:
			return loader;
		case SAVE_ROUTE_LOADER_ID:
			return new SaveRouteLoader(this, displayedRoute);
		case DELETE_ROUTE_LOADER_ID:
			return new DeleteRouteLoader(this, displayedRoute);
		default:
			throw new IllegalArgumentException("Illegal loader id");
		}
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<Route>> loader,
			LoadResult<Route> result) {
		switch (loader.getId()) {
		case LOAD_ROUTE_LOADER_ID:
			onRouteLoaded(result);
			break;
		case SAVE_ROUTE_LOADER_ID:
			onRouteSaved(result);
			break;
		case DELETE_ROUTE_LOADER_ID:
			onRouteDeleted(result);
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<Route>> arg0) {

	}
}
package it.polimi.dima.dacc.mountainroutes.routeviewer;

import com.google.android.gms.maps.model.LatLng;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.StringRepository;
import it.polimi.dima.dacc.mountainroutes.commons.Utils;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.Tracker;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity that displays information about a specific {@link Route}
 */
public class RouteViewer extends Activity implements LoaderManager.LoaderCallbacks<LoadResult<Route>>, LocationListener {

	private static final int LOCATION_UPDATE_DISTANCE = 50;
	private static final int LOCATION_UPDATE_TIME = 10000;
	private static final String TAG = "route-viewer";

	public static final int ROUTE_RESULT = 0;
	public static final String ROUTE = "ROUTE";
	public static final String ROUTE_ID = "ROUTE_ID";

	private static final int LOAD_ROUTE_LOADER_ID = 0;
	private static final int SAVE_ROUTE_LOADER_ID = 1;
	private static final int DELETE_ROUTE_LOADER_ID = 2;

	private final static int TOAST_DURATION = Toast.LENGTH_SHORT;

	// State
	private Route displayedRoute;

	// View elements
	private RouteViewerFragment fragment;
	private View overlay;
	private Button startButton;
	private Button saveButton;
	private Button deleteButton;
	private DifficultyView difficultyView;
	private TextView lengthView;
	private TextView gapView;
	private TextView durationView;
	private View farFromRouteMessage, gpsDisabledMessage;

	// Loader Factory
	private RouteLoader.Factory loaderFactory;

	// Strings
	private StringRepository stringRepo;

	private LocationManager locMan;

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
		fragment = (RouteViewerFragment) getFragmentManager().findFragmentById(R.id.viewer_map);
		startButton = (Button) findViewById(R.id.route_viewer_start);
		saveButton = (Button) findViewById(R.id.route_viewer_save);
		deleteButton = (Button) findViewById(R.id.route_viewer_delete);
		farFromRouteMessage = findViewById(R.id.messagebox_far_from_route);
		gpsDisabledMessage = findViewById(R.id.messagebox_gps_disabled);

		// Load strings
		stringRepo = new StringRepository(this);
		stringRepo.loadString(R.string.error_deleting_route);
		stringRepo.loadString(R.string.error_saving_route);
		stringRepo.loadString(R.string.route_deleted);
		stringRepo.loadString(R.string.route_saved);

		// Load route from remote
		if (savedState == null) {
			RouteID id = (RouteID) getIntent().getParcelableExtra(ROUTE_ID);
			loaderFactory = new RouteLoader.Factory(this, id);
			getLoaderManager().initLoader(LOAD_ROUTE_LOADER_ID, null, this);
		}

		String svcName = Context.LOCATION_SERVICE;
		locMan = (LocationManager) getSystemService(svcName);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (displayedRoute != null) {
			displayRoute(displayedRoute);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			Route route = (Route) savedInstanceState.getParcelable(ROUTE);
			if (route != null) {
				displayRoute(route);
			}
		}
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

	@Override
	protected void onPause() {
		super.onPause();
		locMan.removeUpdates(this);
	}

	/* -- START WALKING -- */
	private OnClickListener startButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent i = new Intent();
			i.putExtra(ROUTE, displayedRoute);
			setResult(RESULT_OK, i);
			finish();
		}
	};

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
			Toast.makeText(this, stringRepo.getString(R.string.error_saving_route), TOAST_DURATION).show();
			break;
		case LoadResult.RESULT:
			// Make tost
			Toast.makeText(this, stringRepo.getString(R.string.route_saved), TOAST_DURATION).show();

			// Hide save button
			saveButton.setVisibility(View.GONE);

			// Show delete button
			deleteButton.setVisibility(View.VISIBLE);
		}
	}

	/* -- ROUTE DISPLAY -- */
	private void displayRoute(final Route route) {
		this.displayedRoute = route;

		// Add save button listener
		this.saveButton.setOnClickListener(saveButtonClickListener);
		this.deleteButton.setOnClickListener(deleteButtonClickListener);
		this.startButton.setOnClickListener(startButtonClickListener);

		String pvdName = LocationManager.GPS_PROVIDER;
		if (!locMan.isProviderEnabled(pvdName)) {
			gpsDisabledMessage.setVisibility(View.VISIBLE);
		}

		locMan.requestLocationUpdates(pvdName, LOCATION_UPDATE_TIME, LOCATION_UPDATE_DISTANCE, this);

		// Set infos
		setTitle(route.getName());
		difficultyView.setDifficulty(route.getDifficulty());
		gapView.setText(Utils.formatGap(route.getGapInMeters()));
		lengthView.setText(route.getLengthInMeters() + " m");
		durationView.setText(route.getDurationInMinutes() + " min");
		fragment.showPath(route.getPath());

		// Show save/delete button
		if (route.getSource() == Route.Source.STORAGE) {
			deleteButton.setVisibility(View.VISIBLE);
		} else {
			saveButton.setVisibility(View.VISIBLE);
		}

		// Hide overlay
		overlay.animate().alpha(0).setDuration(250);
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
			Toast.makeText(this, stringRepo.getString(R.string.error_deleting_route), TOAST_DURATION).show();
			break;
		case LoadResult.RESULT:
			// Show Toast
			Toast.makeText(this, stringRepo.getString(R.string.route_deleted), TOAST_DURATION).show();
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
			return loaderFactory.createLoader();
		case SAVE_ROUTE_LOADER_ID:
			return new SaveRouteLoader(this, displayedRoute);
		case DELETE_ROUTE_LOADER_ID:
			return new DeleteRouteLoader(this, displayedRoute);
		default:
			throw new IllegalArgumentException("Illegal loader id");
		}
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<Route>> loader, LoadResult<Route> result) {
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

	@Override
	public void onLocationChanged(Location location) {
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		boolean canWalk = Tracker.canWalkOn(displayedRoute, latLng);
		startButton.setEnabled(canWalk);
		farFromRouteMessage.setVisibility(canWalk ? View.GONE : View.VISIBLE);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case LocationProvider.AVAILABLE:
			gpsDisabledMessage.setVisibility(View.GONE);
			break;
		default:
			gpsDisabledMessage.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		gpsDisabledMessage.setVisibility(View.GONE);
	}

	@Override
	public void onProviderDisabled(String provider) {
		gpsDisabledMessage.setVisibility(View.VISIBLE);
	}
}
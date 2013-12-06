package it.polimi.dima.dacc.mountainroutes.routeviewer;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.contentloader.ContentErrorType;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class RouteViewer extends Activity implements RouteLoader.Callback {

	private static final String ROUTE = "ROUTE";
	public static final String ROUTE_ID = "ROUTE_ID";

	private Route displayedRoute;
	private RouteViewerFragment fragment;

	private View overlay;
	private TextView overlayMessage;
	
	private DifficultyView difficultyView;
	private TextView lengthView;
	private TextView gapView;
	private TextView durationView;

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_route_viewer);

		difficultyView = (DifficultyView) findViewById(R.id.difficulty_value);
		lengthView = (TextView) findViewById(R.id.lenght_value);
		gapView = (TextView) findViewById(R.id.gap_value);
		durationView = (TextView) findViewById(R.id.estimated_time_value);
		overlay = findViewById(R.id.overlay);
		overlayMessage = (TextView) findViewById(R.id.overlay_message);
		fragment = (RouteViewerFragment) getFragmentManager().findFragmentById(
				R.id.viewer_map);
		
		// Load route from remote
		Log.d("viewer", "" + getIntent().getParcelableExtra(ROUTE_ID));
		RouteID id = (RouteID) getIntent().getParcelableExtra(ROUTE_ID);
		

		// Load route from remote
		RouteLoader loader = new RouteLoader(this, this);
		loader.loadRoute(id);
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
		difficultyView.setDifficulty(route.getDifficulty());
		gapView.setText(route.getGapInMeters() + " m");
		lengthView.setText(route.getLengthInMeters() + " m");
		durationView.setText(route.getDurationInMinutes() + "min");
		fragment.showRoute(route.getPath());
		overlay.animate().alpha(0).setDuration(250);
	}

	@Override
	public void onLoadError(ContentErrorType error) {
		Log.d("viewer","onloadstart");
	}

	@Override
	public void onLoadResult(Route route) {
		Log.d("viewer","onloadresult");
		displayRoute(route);
	}

	@Override
	public void onLoadStart() {
	}

}

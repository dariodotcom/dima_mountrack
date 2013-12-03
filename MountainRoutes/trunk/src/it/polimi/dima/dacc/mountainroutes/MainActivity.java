package it.polimi.dima.dacc.mountainroutes;

import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescription;
import it.polimi.dima.dacc.mountainroutes.contentloader.ContentErrorType;
import it.polimi.dima.dacc.mountainroutes.contentloader.ContentLoader;
import it.polimi.dima.dacc.mountainroutes.contentloader.ContentQuery;
import it.polimi.dima.dacc.mountainroutes.contentloader.ContentQuery.QueryType;
import it.polimi.dima.dacc.mountainroutes.contentloader.LoaderObserver;
import it.polimi.dima.dacc.mountainroutes.contentloader.LoaderResult;
import it.polimi.dima.dacc.mountainroutes.routeselector.CompleteRouteSelector;
import it.polimi.dima.dacc.mountainroutes.routeviewer.RouteViewer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private OnClickListener startWalkingButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this,
					CompleteRouteSelector.class);
			startActivity(i);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button b = (Button) findViewById(R.id.main_start_walk_button);
		b.setOnClickListener(startWalkingButtonListener);

		// RouteDescription r = new RouteDescription(
		// "ahlzfmRpbWEtZGFjYy1tb3VudGFpbnJvdXRlcg0LEgVSb3V0ZRjxogQM", null);
		// Intent i = new Intent(this, RouteViewer.class);
		// i.putExtra(RouteViewer.ROUTE_DESCRIPTION, r);
		// startActivity(i);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

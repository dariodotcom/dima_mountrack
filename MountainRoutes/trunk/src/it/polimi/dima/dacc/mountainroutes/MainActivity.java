package it.polimi.dima.dacc.mountainroutes;

import it.polimi.dima.dacc.mountainroutes.routeselector.CompleteRouteSelector;
import it.polimi.dima.dacc.mountainroutes.routeviewer.RouteViewer;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
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

		RouteID id = new RouteID(null, "ahlzfmRpbWEtZGFjYy1tb3VudGFpbnJvdXRlcg0LEgVSb3V0ZRiRvwUM");
		Intent i = new Intent(this, RouteViewer.class);
		i.putExtra(RouteViewer.ROUTE_ID, id);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

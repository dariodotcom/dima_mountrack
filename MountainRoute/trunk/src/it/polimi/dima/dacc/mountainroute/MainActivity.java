package it.polimi.dima.dacc.mountainroute;

import it.polimi.dima.dacc.mountainroute.map.Route;
import it.polimi.dima.dacc.mountainroute.map.RouteLineController;
import it.polimi.dima.dacc.mountainroute.map.RouteMapFragment;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final List<String> data = new ArrayList<String>();
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, data);

		ListView listView = (ListView) findViewById(R.id.PointList);
		listView.setAdapter(adapter);

		RouteMapFragment mapFragment = (RouteMapFragment) getFragmentManager()
				.findFragmentById(R.id.map);

		Route r = new Route();
		r.addPoint(new LatLng(0, 0));
		r.addPoint(new LatLng(10, 10));
		r.addPoint(new LatLng(20, 20));
		mapFragment.initializeRoute(r, 0.5f);
		
		mapFragment.getMap().setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
				data.add(point.toString());
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

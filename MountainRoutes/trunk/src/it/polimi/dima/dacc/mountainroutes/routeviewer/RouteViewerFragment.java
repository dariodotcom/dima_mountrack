package it.polimi.dima.dacc.mountainroutes.routeviewer;

import it.polimi.dima.dacc.mountainroutes.commons.types.PointList;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;

public class RouteViewerFragment extends MapFragment{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Disable map interaction;
		GoogleMap map = getMap();
		UiSettings settings = map.getUiSettings();
		settings.setAllGesturesEnabled(false);
		settings.setZoomControlsEnabled(false);
		
	}
	
	public void showRoutePoints(PointList point){
		
	}
	
}

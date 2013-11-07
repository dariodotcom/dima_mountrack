package it.polimi.dima.dacc.montainroute.routecreationtool;

import com.google.android.gms.maps.model.LatLng;

public interface RouteTrackerUpdateListener {

	public void onPointTracked(LatLng point);

}

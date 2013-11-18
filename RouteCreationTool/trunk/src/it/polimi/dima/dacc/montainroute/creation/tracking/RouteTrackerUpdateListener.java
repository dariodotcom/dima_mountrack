package it.polimi.dima.dacc.montainroute.creation.tracking;

import com.google.android.gms.maps.model.LatLng;

public interface RouteTrackerUpdateListener {

	public void onPointTracked(LatLng point);

}

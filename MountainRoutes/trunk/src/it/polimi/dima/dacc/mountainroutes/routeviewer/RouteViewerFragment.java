package it.polimi.dima.dacc.mountainroutes.routeviewer;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.types.PointList;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

public class RouteViewerFragment extends MapFragment {

	private int lineColor;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Load color
		lineColor = getActivity().getResources().getColor(R.color.accent_blue);

		// Disable map interaction;
		GoogleMap map = getMap();
		UiSettings settings = map.getUiSettings();
		settings.setAllGesturesEnabled(false);
		settings.setZoomControlsEnabled(false);
	}

	/**
	 * Displays a path on the map, centering and zooming to get the route fit
	 * the fragment view
	 * 
	 * @param points
	 *            - the list of points composing the route
	 */
	public void showRoute(PointList points) {
		// Add line
		GoogleMap map = getMap();
		PolylineOptions options = new PolylineOptions().addAll(points)
				.geodesic(true).color(lineColor);
		map.addPolyline(options);

		//
		
		// Center map
		LatLngBounds bounds = bound(points);
		Log.d("viewer", "bounds: " + bounds);
		CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 10);
		getMap().moveCamera(update);
	}

	private LatLngBounds bound(PointList path) {
		double maxLat = Double.NEGATIVE_INFINITY, minLat = Double.POSITIVE_INFINITY, maxLng = Double.NEGATIVE_INFINITY, minLng = Double.POSITIVE_INFINITY;

		for (LatLng point : path) {
			double lat = point.latitude, lng = point.longitude;
			if (lat > maxLat) {
				maxLat = lat;
			} else if (lat < minLat) {
				minLat = lat;
			}

			if (lng > maxLng) {
				maxLng = lng;
			} else if (lng < minLng) {
				minLng = lng;
			}
		}

		LatLng southWest = new LatLng(minLat, minLng);
		LatLng northEast = new LatLng(maxLat, maxLng);
		Log.d("viewer", "SW: " + southWest + ". NE: " + northEast);
		return new LatLngBounds(southWest, northEast);
	}
}

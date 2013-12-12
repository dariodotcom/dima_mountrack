package it.polimi.dima.dacc.mountainroutes.routeviewer;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.types.PointList;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

public class RouteViewerFragment extends MapFragment {

	private static final String TAG = RouteViewerFragment.class.getName();
	private int lineColor;
	private boolean canShowPath;
	private PointList pathToDisplay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load color
		lineColor = getActivity().getResources().getColor(R.color.accent_blue);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getView().getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						RouteViewerFragment.this.getView()
								.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						canShowPath = true;
						if (pathToDisplay != null) {
							mDisplayPath(pathToDisplay);
						}
					}
				});
	}

	@Override
	public void onStart() {
		super.onStart();

		GoogleMap map = getMap();

		if (map == null) {
			Log.e(TAG, "map is not available");
		} else {
			UiSettings settings = map.getUiSettings();
			settings.setAllGesturesEnabled(false);
			settings.setMyLocationButtonEnabled(false);
		}
	}

	@Override
	public void onPause() {
		canShowPath = false;
		super.onPause();
	}

	/**
	 * Displays a path on the map, centering and zooming to get the route fit
	 * the fragment view
	 * 
	 * @param points
	 *            - the list of points composing the route
	 */
	public void showPath(PointList points) {
		if (canShowPath) {
			mDisplayPath(points);
		} else {
			pathToDisplay = points;
		}
	}

	private void mDisplayPath(PointList points) {
		// Add line
		GoogleMap map = getMap();

		if (map != null) {
			PolylineOptions options = new PolylineOptions();
			options.addAll(points).geodesic(true).color(lineColor);
			map.addPolyline(options);

			// Center map
			LatLngBounds bnd = bound(points);
			CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bnd, 10);
			getMap().moveCamera(update);
		} else {
			Log.e(TAG, "map is not available");
		}
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
		return new LatLngBounds(southWest, northEast);
	}
}

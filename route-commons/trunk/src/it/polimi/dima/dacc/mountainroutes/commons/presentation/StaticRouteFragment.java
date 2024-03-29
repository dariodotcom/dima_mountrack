package it.polimi.dima.dacc.mountainroutes.commons.presentation;

import java.util.List;

import it.polimi.dima.dacc.mountainroutes.commons.R;
import it.polimi.dima.dacc.mountainroutes.commons.types.PointList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Adapter class to show a {@link PointList} on a map.
 */
public class StaticRouteFragment extends MapFragment {

	private static final String SAVED_PATH_KEY = "SAVED_PATH";

	// The path shown on the map
	private PointList path;

	// Elements shown on map
	private Polyline line;
	private Marker marker;

	// Public methods
	public void setPath(PointList path) {
		this.path = path;
	}

	public void notifyPathChanged() {
		List<LatLng> pointList = path.getList();
		if (pointList.isEmpty()) {
			return;
		}

		int last = pointList.size() - 1;
		LatLng lastPoint = pointList.get(last);
		this.line.setPoints(pointList);
		this.moveMarker(lastPoint);
		this.moveCamera(lastPoint);
	}

	// Activity methods to override
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedState) {
		View result = super.onCreateView(inflater, container, savedState);

		// Add visual elements to map
		int lineColor = getResources().getColor(R.color.traversedroute);
		PolylineOptions options = new PolylineOptions().color(lineColor);
		this.line = getMap().addPolyline(options);

		// If available, load previous state
		if (savedState != null) {
			this.path = (PointList) savedState.getParcelable(SAVED_PATH_KEY);
			notifyPathChanged();
		}

		return result;
	}

	public void centerMap() {
		if (this.path.isEmpty()) {
			return;
		}
		moveCamera(this.path.getLast());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(SAVED_PATH_KEY, path);
	}

	private void moveCamera(LatLng point) {
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point,
				18f);
		getMap().animateCamera(cameraUpdate);
	}

	private void moveMarker(LatLng position) {
		if (this.marker == null) {
			MarkerOptions options = new MarkerOptions().position(position);
			this.marker = getMap().addMarker(options);
		} else {
			this.marker.setPosition(position);
		}
	}
}
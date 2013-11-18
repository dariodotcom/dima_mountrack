package it.polimi.dima.dacc.mountainroute.commons.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.dima.dacc.mountainroute.commons.R;
import it.polimi.dima.dacc.mountainroute.commons.types.Point;
import it.polimi.dima.dacc.mountainroute.commons.types.PointList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class StaticRouteFragment extends MapFragment {

	private static final String SAVED_STATE_KEY = "SAVED_STATE";
	private Polyline line;
	private Marker marker;

	public void addPoint(Point p) {
		LatLng newPoint = p.toLatLng();

		// Add point to list
		List<LatLng> list = line.getPoints();
		list.add(newPoint);
		this.line.setPoints(list);

		// Move marker
		this.marker.setPosition(newPoint);
	}

	public void addAll(List<Point> pts) {
		List<LatLng> newPoints = new PointList(pts).toLatLngList();

		// Add points
		List<LatLng> list = line.getPoints();
		list.addAll(newPoints);
		this.line.setPoints(list);

		// Move marker to last point
		LatLng lastLatLng = newPoints.get(newPoints.size() - 1);
		this.marker.setPosition(lastLatLng);
	}

	public void clear() {
		this.line.setPoints(new ArrayList<LatLng>());
	}

	// Override methods
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedState) {
		View result = super.onCreateView(inflater, container, savedState);

		if (savedState != null) {
			State saved = (State) savedState.getSerializable(SAVED_STATE_KEY);
			this.line = saved.getLine();
			this.marker = saved.getMarker();
		} else {
			GoogleMap map = getMap();
			PolylineOptions lineOptions = new PolylineOptions();
			MarkerOptions markerOptions = new MarkerOptions();

			int color = getResources().getColor(R.color.traversedroute);
			lineOptions.color(color);
			markerOptions.draggable(false);

			this.line = map.addPolyline(lineOptions);
			this.marker = map.addMarker(markerOptions);
		}

		return result;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(SAVED_STATE_KEY, new State(line, marker));
		super.onSaveInstanceState(outState);
	}

	private static class State implements Serializable {

		private static final long serialVersionUID = 5763874970595962552L;
		private Polyline line;
		private Marker marker;

		public State(Polyline line, Marker marker) {
			this.line = line;
			this.marker = marker;
		}

		public Polyline getLine() {
			return line;
		}

		public Marker getMarker() {
			return marker;
		}
	}
}
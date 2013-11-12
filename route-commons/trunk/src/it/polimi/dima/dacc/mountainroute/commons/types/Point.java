package it.polimi.dima.dacc.mountainroute.commons.types;

import java.io.Serializable;

import com.google.android.gms.maps.model.LatLng;

public class Point implements Serializable {

	private static final long serialVersionUID = -8408073826722722052L;

	private double latitude;
	private double longitude;

	public Point(LatLng source) {
		this.latitude = source.latitude;
		this.longitude = source.longitude;
	}

	public Point(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public LatLng toLatLng() {
		return new LatLng(latitude, longitude);
	}
}

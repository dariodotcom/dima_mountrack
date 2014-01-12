package it.polimi.dima.dacc.mountainroutes.remote;

import com.google.android.gms.maps.model.LatLng;

public class AltitudeGap {
	private int gap;
	private LatLng point;

	public AltitudeGap(int gap, LatLng point) {
		this.gap = gap;
		this.point = point;
	}

	public int getValue() {
		return gap;
	}

	public LatLng getPoint() {
		return point;
	}
}

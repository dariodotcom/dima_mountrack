package it.polimi.dima.dacc.mountainroute.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Route implements Serializable {

	private static final long serialVersionUID = -6991907942876465754L;

	private List<LatLng> points;

	public List<LatLng> getPoints() {
		return points;
	}

	public void setPoints(List<LatLng> points) {
		this.points = points;
	}

	public void addPoint(LatLng point){
		if(points == null){
			points = new ArrayList<LatLng>();
		}
		
		points.add(point);
	}
}

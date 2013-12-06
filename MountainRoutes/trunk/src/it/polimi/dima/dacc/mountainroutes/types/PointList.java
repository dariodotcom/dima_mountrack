package it.polimi.dima.dacc.mountainroutes.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Parcelable list of LatLng
 * 
 */
public class PointList implements Parcelable, Iterable<LatLng> {

	private List<LatLng> points;

	// Public constructor
	public PointList(List<LatLng> newPoints) {
		this.points = new ArrayList<LatLng>(newPoints);
	}

	public PointList() {
		this.points = new ArrayList<LatLng>();
	}

	// Private contructor for parcelable
	public PointList(Parcel in) {
		this();
		in.readTypedList(this.points, LatLng.CREATOR);
	}

	// Public methods
	public void addAll(List<LatLng> newPoints) {
		this.points.addAll(newPoints);
	}
	
	public void addAll(PointList newPoints) {
		this.points.addAll(newPoints.points);
	}

	public void add(LatLng newPoint) {
		this.points.add(newPoint);
	}

	public List<LatLng> getList() {
		return this.points;
	}

	public int size(){
		return points.size();
	}
	
	public boolean isEmpty(){
		return points.isEmpty();
	}
	
	public LatLng getLast(){
		int last = points.size() - 1;
		return points.get(last);
	}
	
	// Parcelable methods
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(this.points);
	}

	public static final Creator<PointList> CREATOR = new Creator<PointList>() {
		public PointList createFromParcel(Parcel in) {
			return new PointList(in);
		}

		public PointList[] newArray(int size) {
			return new PointList[size];
		}
	};

	@Override
	public Iterator<LatLng> iterator() {
		return points.iterator();
	}
	
	@Override
	public String toString() {
		return points.toString();
	}
}

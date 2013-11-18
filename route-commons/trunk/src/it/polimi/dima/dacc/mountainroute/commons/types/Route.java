package it.polimi.dima.dacc.mountainroute.commons.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Route implements Parcelable {

	private String id;
	private String name;
	private PointList route;
	private int duration;

	// Public contructor
	public Route(String id, String name, PointList route, int duration) {
		this.id = id;
		this.name = name;
		this.route = route;
		this.duration = duration;
	}

	// Private constructor for parcelable
	private Route(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.route = (PointList) in.readParcelable(null);
		this.duration = in.readInt();
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public PointList getRoute() {
		return route;
	}

	public int getDuration() {
		return duration;
	}

	// Parcelable methods
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeParcelable(route, 0);
		dest.writeInt(duration);
	}

	public final Creator<Route> CREATOR = new Creator<Route>() {

		@Override
		public Route[] newArray(int size) {
			return new Route[size];
		}

		@Override
		public Route createFromParcel(Parcel source) {
			return new Route(source);
		}
	};
}
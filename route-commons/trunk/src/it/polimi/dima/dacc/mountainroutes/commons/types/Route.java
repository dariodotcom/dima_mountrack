package it.polimi.dima.dacc.mountainroutes.commons.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Route implements Parcelable {

	private String id;
	private String name;
	private PointList route;
	private int duration;
	private Difficulty difficulty;
	private int length;
	private int gap;

	// Default constructor
	public Route() {
	}

	// Private constructor for parcelable
	private Route(Parcel in) {
		this.difficulty = Difficulty.valueOf(in.readInt());
		this.duration = in.readInt();
		this.gap = in.readInt();
		this.id = in.readString();
		this.length = in.readInt();
		this.name = in.readString();
		this.route = (PointList) in.readParcelable(null);
	}

	// ID
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// Name
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	// Difficulty
	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	// Length
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	// Gap
	public int getGap() {
		return gap;
	}

	public void setGap(int gap) {
		this.gap = gap;
	}

	// Duration
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	// Point list
	public PointList getRoute() {
		return route;
	}

	public void setRoute(PointList route) {
		this.route = route;
	}

	// Parcelable methods
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Difficulty.indexOf(difficulty));
		dest.writeInt(duration);
		dest.writeInt(gap);
		dest.writeString(id);
		dest.writeInt(length);
		dest.writeString(name);
		dest.writeParcelable(route, 0);
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
package it.polimi.dima.dacc.mountainroutes.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Route implements Parcelable {

	// Identifiers
	private RouteID id;
	private Source source;

	// Features
	private String name;
	private Difficulty difficulty;
	private int durationInMinutes;
	private int lengthInMeters;
	private int gapInMeters;
	private PointList path;

	// Default constructor
	public Route(RouteID id, Source source) {
		this.id = id;
		this.source = source;
	}

	// Private constructor for parcelable
	private Route(Parcel in) {
		this.id = (RouteID) in.readParcelable(null);
		this.source = Source.valueOf(in.readString());

		this.name = in.readString();
		this.difficulty = Difficulty.valueOf(in.readInt());
		this.durationInMinutes = in.readInt();
		this.lengthInMeters = in.readInt();
		this.gapInMeters = in.readInt();
		this.path = (PointList) in.readParcelable(null);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.id, 0);
		dest.writeString(this.source.name());
		dest.writeString(name);
		dest.writeInt(Difficulty.indexOf(difficulty));
		dest.writeInt(durationInMinutes);
		dest.writeInt(lengthInMeters);
		dest.writeInt(gapInMeters);
		dest.writeParcelable(path, 0);
	}

	// ID
	public RouteID getId() {
		return this.id;
	}

	// Source
	public Source getSource() {
		return source;
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

	// Duration
	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}

	// Length
	public int getLengthInMeters() {
		return lengthInMeters;
	}

	public void setLengthInMeters(int lengthInMeters) {
		this.lengthInMeters = lengthInMeters;
	}

	// Gap
	public int getGapInMeters() {
		return gapInMeters;
	}

	public void setGapInMeters(int gapInMeters) {
		this.gapInMeters = gapInMeters;
	}

	// Path
	public PointList getPath() {
		return path;
	}

	public void setPath(PointList path) {
		this.path = path;
	}

	// Parcelable methods
	@Override
	public int describeContents() {
		return 0;
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

	public static enum Source {
		STORAGE, REMOTE
	}
}
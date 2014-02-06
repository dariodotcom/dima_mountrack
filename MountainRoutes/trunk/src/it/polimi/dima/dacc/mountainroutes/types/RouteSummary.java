package it.polimi.dima.dacc.mountainroutes.types;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable type that contains essentials informations regarding a route.
 */
public class RouteSummary implements Parcelable {

	private RouteID id;
	private String name;
	private Difficulty difficulty;
	private int durationInMinutes;

	public RouteSummary() {

	}

	private RouteSummary(Parcel in) {
		this.id = new RouteID(in.readString());
		this.name = in.readString();
		this.difficulty = Difficulty.valueOf(in.readString());
		this.durationInMinutes = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id.toString());
		dest.writeString(name);
		dest.writeString(difficulty.name());
		dest.writeInt(durationInMinutes);
	}

	public final static Creator<RouteSummary> CREATOR = new Creator<RouteSummary>() {

		@Override
		public RouteSummary[] newArray(int size) {
			return new RouteSummary[size];
		}

		@Override
		public RouteSummary createFromParcel(Parcel source) {
			return new RouteSummary(source);
		}
	};

	public RouteID getId() {
		return id;
	}

	public void setId(RouteID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}
	
	@Override
	public String toString() {
		String format = "routereview #%s %s";
		return String.format(format, id, name);
	}
}

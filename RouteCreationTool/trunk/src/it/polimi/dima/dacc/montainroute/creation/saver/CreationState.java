package it.polimi.dima.dacc.montainroute.creation.saver;

import it.polimi.dima.dacc.mountainroute.commons.types.PointList;
import it.polimi.dima.dacc.mountainroute.commons.types.Route;

import android.os.Parcel;
import android.os.Parcelable;

public class CreationState implements Parcelable {

	private String name;
	private Integer duration;
	private PointList points;

	
	public CreationState(PointList points) {
		if (points == null) {
			throw new RuntimeException("Points must not be null");
		}
		
		this.points = points;
	}

	private CreationState(Parcel source) {
		this.name = source.readString();
		this.duration = source.readInt();
		this.points = source.readParcelable(PointList.class.getClassLoader());
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Route createRoute() {
		if (name == null || duration == null) {
			throw new RuntimeException("Incomplete route");
		}

		return new Route(null, name, points, duration);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeInt(this.duration);
		dest.writeParcelable(this.points, 0);
	}

	public final static Creator<CreationState> CREATOR = new Creator<CreationState>() {
		
		@Override
		public CreationState[] newArray(int size) {
			return new CreationState[size];
		}
		
		@Override
		public CreationState createFromParcel(Parcel source) {
			return new CreationState(source);
		}
	};
}

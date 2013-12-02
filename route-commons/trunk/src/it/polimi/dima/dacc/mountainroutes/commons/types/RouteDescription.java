package it.polimi.dima.dacc.mountainroutes.commons.types;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteDescription implements Parcelable {

	private String name;
	private String id;

	public RouteDescription(String id, String name) {
		super();
		this.name = name;
		this.id = id;
	}

	private RouteDescription(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(name);
	}

	public final static Creator<RouteDescription> CREATOR = new Creator<RouteDescription>() {

		@Override
		public RouteDescription[] newArray(int size) {
			return new RouteDescription[size];
		}

		@Override
		public RouteDescription createFromParcel(Parcel source) {
			return new RouteDescription(source);
		}
	};

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		String format = "{\"id\":\"%s\",\"name\"=\"%s\"}";
		return String.format(format, this.id, this.name);
	}

}

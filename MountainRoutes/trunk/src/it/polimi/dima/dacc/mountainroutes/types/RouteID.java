package it.polimi.dima.dacc.mountainroutes.types;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteID implements Parcelable {

	private static final String SEPARATOR = ":";
	private String providerID;
	private String routeID;

	public RouteID(String providerID, String routeID) {
		this.providerID = providerID;
		this.routeID = routeID;
	}

	private RouteID(Parcel in) {
		providerID = in.readString();
		routeID = in.readString();
	}

	public RouteID(String representation) {
		String[] parts = representation.split(SEPARATOR);
		
		if (parts.length != 2) {
			throw new IllegalArgumentException("Invalid routeID representation");
		}
		
		this.providerID = parts[0];
		this.routeID = parts[1];
	}

	public String getProviderID() {
		return providerID;
	}

	public String getRouteID() {
		return routeID;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return providerID + SEPARATOR + routeID;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(providerID);
		dest.writeString(routeID);
	}

	public final static Creator<RouteID> CREATOR = new Creator<RouteID>() {

		@Override
		public RouteID[] newArray(int size) {
			return new RouteID[size];
		}

		@Override
		public RouteID createFromParcel(Parcel source) {
			return new RouteID(source);
		}
	};
}

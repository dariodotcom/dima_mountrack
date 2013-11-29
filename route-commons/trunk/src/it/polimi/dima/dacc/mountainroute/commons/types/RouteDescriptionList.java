package it.polimi.dima.dacc.mountainroute.commons.types;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/*Parcelable list of RouteDescription*/
public class RouteDescriptionList implements Parcelable {
	private List<RouteDescription> routeDescriptions;

	public RouteDescriptionList() {
		this.routeDescriptions = new ArrayList<RouteDescription>();
	}

	private RouteDescriptionList(Parcel in) {
		in.readTypedList(routeDescriptions, RouteDescription.CREATOR);
	}

	public void addRouteDescription(RouteDescription desc) {
		this.routeDescriptions.add(desc);
	}

	public List<RouteDescription> getRouteDescriptions() {
		return routeDescriptions;
	}

	public final static Creator<RouteDescriptionList> CREATOR = new Creator<RouteDescriptionList>() {

		@Override
		public RouteDescriptionList[] newArray(int size) {
			return new RouteDescriptionList[size];
		}

		@Override
		public RouteDescriptionList createFromParcel(Parcel source) {
			return new RouteDescriptionList(source);
		}
	};

	@Override
	public String toString() {
		return routeDescriptions.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(routeDescriptions);
	}
}

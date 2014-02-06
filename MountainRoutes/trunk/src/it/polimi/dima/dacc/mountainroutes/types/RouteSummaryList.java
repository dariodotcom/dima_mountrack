package it.polimi.dima.dacc.mountainroutes.types;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable list of RouteDescription
 */
public class RouteSummaryList implements Parcelable {
	private List<RouteSummary> summaries;

	public RouteSummaryList() {
		this.summaries = new ArrayList<RouteSummary>();
	}

	private RouteSummaryList(Parcel in) {
		in.readTypedList(summaries, RouteSummary.CREATOR);
	}

	public void addRouteSummary(RouteSummary desc) {
		this.summaries.add(desc);
	}

	public boolean isEmpty(){
		return summaries.isEmpty();
	}

	public List<RouteSummary> asList() {
		return summaries;
	}

	public final static Creator<RouteSummaryList> CREATOR = new Creator<RouteSummaryList>() {

		@Override
		public RouteSummaryList[] newArray(int size) {
			return new RouteSummaryList[size];
		}

		@Override
		public RouteSummaryList createFromParcel(Parcel source) {
			return new RouteSummaryList(source);
		}
	};

	@Override
	public String toString() {
		return summaries.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(summaries);
	}
}

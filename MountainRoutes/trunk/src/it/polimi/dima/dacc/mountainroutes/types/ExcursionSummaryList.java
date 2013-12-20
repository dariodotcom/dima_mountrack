package it.polimi.dima.dacc.mountainroutes.types;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ExcursionSummaryList implements Parcelable {

	private List<ExcursionSummary> summaries;
	
	public ExcursionSummaryList() {
		this.summaries = new ArrayList<ExcursionSummary>();
	}

	private ExcursionSummaryList(Parcel in) {
		in.readTypedList(summaries, ExcursionSummary.CREATOR);
	}
	
	public void addExcursionSummary(ExcursionSummary desc) {
		this.summaries.add(desc);
	}

	public boolean isEmpty(){
		return summaries.isEmpty();
	}

	public List<ExcursionSummary> asList() {
		return summaries;
	}
	
	public final static Creator<ExcursionSummaryList> CREATOR = new Creator<ExcursionSummaryList>() {

		@Override
		public ExcursionSummaryList[] newArray(int size) {
			return new ExcursionSummaryList[size];
		}

		@Override
		public ExcursionSummaryList createFromParcel(Parcel source) {
			return new ExcursionSummaryList(source);
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(summaries);
	}
	
	@Override
	public String toString() {
		return summaries.toString();
	}

}

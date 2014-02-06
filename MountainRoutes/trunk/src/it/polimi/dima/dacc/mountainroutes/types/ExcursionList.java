package it.polimi.dima.dacc.mountainroutes.types;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable list of {@link ExcursionReport}
 */
public class ExcursionList implements Parcelable {

	private List<ExcursionReport> reports;

	public ExcursionList() {
		this.reports = new ArrayList<ExcursionReport>();
	}

	private ExcursionList(Parcel in) {
		in.readTypedList(reports, ExcursionReport.CREATOR);
	}

	public void addExcursionReport(ExcursionReport report) {
		this.reports.add(report);
	}

	public boolean isEmpty() {
		return reports.isEmpty();
	}

	public List<ExcursionReport> asList() {
		return reports;
	}

	public final static Creator<ExcursionList> CREATOR = new Creator<ExcursionList>() {

		@Override
		public ExcursionList[] newArray(int size) {
			return new ExcursionList[size];
		}

		@Override
		public ExcursionList createFromParcel(Parcel source) {
			return new ExcursionList(source);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(reports);
	}

	@Override
	public String toString() {
		return reports.toString();
	}

}

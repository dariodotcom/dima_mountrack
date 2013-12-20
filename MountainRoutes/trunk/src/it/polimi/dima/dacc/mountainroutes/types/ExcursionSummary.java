package it.polimi.dima.dacc.mountainroutes.types;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class ExcursionSummary implements Parcelable {

	private RouteID routeId;
	private float completionIndex;
	private int elapsedSeconds;
	private Date date;
	private int gap;
	private int lengthInMeters;

	public ExcursionSummary() {

	}

	private ExcursionSummary(Parcel in) {
		this.routeId = (RouteID) in.readParcelable(RouteID.class
				.getClassLoader());
		this.completionIndex = in.readFloat();
		this.elapsedSeconds = in.readInt();
		this.date = new Date(in.readLong());
		this.gap = in.readInt();
		this.lengthInMeters = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.routeId, 0);
		dest.writeFloat(this.completionIndex);
		dest.writeInt(this.elapsedSeconds);
		dest.writeLong(this.date.getTime());
		dest.writeInt(this.gap);
		dest.writeInt(this.lengthInMeters);
	}
	
	public final static Creator<ExcursionSummary> CREATOR = new Creator<ExcursionSummary>() {
		@Override
		public ExcursionSummary[] newArray(int size) {
			return new ExcursionSummary[size];
		}

		@Override
		public ExcursionSummary createFromParcel(Parcel source) {
			return new ExcursionSummary(source);
		}
	};

	public RouteID getRouteId() {
		return routeId;
	}

	public void setRouteId(RouteID routeId) {
		this.routeId = routeId;
	}

	public float getCompletionIndex() {
		return completionIndex;
	}

	public void setCompletionIndex(float completionIndex) {
		this.completionIndex = completionIndex;
	}

	public int getElapsedSeconds() {
		return elapsedSeconds;
	}

	public void setElapsedSeconds(int elapsedSeconds) {
		this.elapsedSeconds = elapsedSeconds;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getGap() {
		return gap;
	}

	public void setGap(int gap) {
		this.gap = gap;
	}

	public int getLengthInMeters() {
		return lengthInMeters;
	}

	public void setLengthInMeters(int lengthInMeters) {
		this.lengthInMeters = lengthInMeters;
	}
	
	@Override
	public String toString() {
		String format = "excursionreview #%s %s";
		return String.format(format, routeId.toString(), date);
	}

}

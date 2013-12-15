package it.polimi.dima.dacc.mountainroutes.types;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class ExcursionReport implements Parcelable {

	private RouteID id;
	private float completionIndex;
	private int elapsedSeconds;
	private Date date;
	private int gap;
	private int lengthInMeters;

	public ExcursionReport(RouteID id) {
		this.id = id;
		this.date = new Date();
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

	public RouteID getId() {
		return id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/* -- Parcelable Methods -- */
	private ExcursionReport(Parcel in) {
		completionIndex = in.readFloat();
		date = new Date(in.readLong());
		elapsedSeconds = in.readInt();
		gap = in.readInt();
		id = in.readParcelable(RouteID.class.getClassLoader());
		lengthInMeters = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(completionIndex);
		dest.writeLong(date.getTime());
		dest.writeInt(elapsedSeconds);
		dest.writeInt(gap);
		dest.writeParcelable(id, 0);
		dest.writeInt(lengthInMeters);
	}

	public static final Creator<ExcursionReport> CREATOR = new Creator<ExcursionReport>() {

		@Override
		public ExcursionReport createFromParcel(Parcel source) {
			return new ExcursionReport(source);
		}

		@Override
		public ExcursionReport[] newArray(int size) {
			return new ExcursionReport[size];
		}

	};

}

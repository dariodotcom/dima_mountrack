package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;

public class TrackResult implements Parcelable {

	public float getCompletionIndex() {
		return completionIndex;
	}

	public void setCompletionIndex(float completionIndex) {
		this.completionIndex = completionIndex;
	}

	public LatLng getPointOnPath() {
		return pointOnPath;
	}

	public void setPointOnPath(LatLng pointOnPath) {
		this.pointOnPath = pointOnPath;
	}

	private float completionIndex;
	private LatLng pointOnPath;

	public TrackResult(float completionIndex, LatLng pointOnPath) {
		this.completionIndex = completionIndex;
		this.pointOnPath = pointOnPath;
	}

	private TrackResult(Parcel source) {
		completionIndex = source.readFloat();
		pointOnPath = source.readParcelable(LatLng.class.getClassLoader());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(completionIndex);
		dest.writeParcelable(pointOnPath, 0);
	}

	public static Creator<TrackResult> CREATOR = new Creator<TrackResult>() {

		@Override
		public TrackResult[] newArray(int size) {
			return new TrackResult[size];
		}

		@Override
		public TrackResult createFromParcel(Parcel source) {
			return new TrackResult(source);

		}
	};

}

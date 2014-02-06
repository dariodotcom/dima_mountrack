package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Result returned by Tracker.
 * @author Chiara
 *
 */
public class TrackResult implements Parcelable {

	private float completionIndex;
	private LatLng pointOnPath;
	private LatLng realPosition;
	private int elapsedMeters;

	public int getElapsedMeters() {
		return elapsedMeters;
	}

	public void setElapsedMeters(int elapsedMeters) {
		this.elapsedMeters = elapsedMeters;
	}

	private TrackResult() {

	}

	public float getCompletionIndex() {
		return completionIndex;
	}

	private void setCompletionIndex(float completionIndex) {
		this.completionIndex = completionIndex;
	}

	public LatLng getPointOnPath() {
		return pointOnPath;
	}

	private void setPointOnPath(LatLng pointOnPath) {
		this.pointOnPath = pointOnPath;
	}

	public LatLng getRealPosition() {
		return realPosition;
	}

	private void setRealPosition(LatLng realPosition) {
		this.realPosition = realPosition;
	}

	@Override
	public String toString() {
		String format = "  Percent: %s\n  Point: %s\n  Real point: %s\n  Elapsed meters: %s";
		return String.format(format, completionIndex, pointOnPath, realPosition, elapsedMeters);
	}

	// Parcelable
	private TrackResult(Parcel source) {
		completionIndex = source.readFloat();
		ClassLoader cl = LatLng.class.getClassLoader();
		pointOnPath = source.readParcelable(cl);
		realPosition = source.readParcelable(cl);
		elapsedMeters = source.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(completionIndex);
		dest.writeParcelable(pointOnPath, 0);
		dest.writeParcelable(realPosition, 0);
		dest.writeInt(elapsedMeters);
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

	public static class Builder {

		private TrackResult result;

		public Builder() {
			this.result = new TrackResult();
		}

		public Builder completionIndex(float completionIndex) {
			result.setCompletionIndex(completionIndex);
			return this;
		}

		public Builder pointOnPath(LatLng pointOnPath) {
			result.setPointOnPath(pointOnPath);
			return this;
		}

		public Builder realPosition(LatLng realPosition) {
			result.setRealPosition(realPosition);
			return this;
		}

		public Builder elapsedMeters(int elapsedMeters) {
			result.setElapsedMeters(elapsedMeters);
			return this;
		}

		public TrackResult build() {
			return result;
		}
	}
}

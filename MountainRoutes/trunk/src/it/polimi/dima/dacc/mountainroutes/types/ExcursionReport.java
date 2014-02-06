package it.polimi.dima.dacc.mountainroutes.types;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable class that contains the information of a route.
 * 
 * @author Chiara
 * 
 */
public class ExcursionReport implements Parcelable {

	private int id;
	private String routeName;
	private PointList path;
	private Date date;
	private float completionIndex;
	private int elapsedDurationSeconds;
	private int elapsedLenght;
	private int elapsedGap;
	private int routeDuration;
	private int routeGap;
	private int routeLenght;

	public ExcursionReport() {

	}

	public ExcursionReport(Route r) {
		this.date = new Date();
		this.routeName = r.getName();
		this.path = r.getPath();
		this.routeDuration = r.getDurationInMinutes();
		this.routeGap = r.getGapInMeters();
		this.routeLenght = r.getLengthInMeters();
	}

	// Setters and Getters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String name) {
		this.routeName = name;
	}

	public PointList getPath() {
		return path;
	}

	public void setPath(PointList path) {
		this.path = path;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public float getCompletionIndex() {
		return completionIndex;
	}

	public void setCompletionIndex(float completionIndex) {
		this.completionIndex = completionIndex;
	}

	public int getElapsedDuration() {
		return elapsedDurationSeconds;
	}

	public void setElapsedDuration(int elapsedSeconds) {
		this.elapsedDurationSeconds = elapsedSeconds;
	}

	public int getElapsedLength() {
		return elapsedLenght;
	}

	public void setElapsedLength(int elapsedMeters) {
		this.elapsedLenght = elapsedMeters;
	}

	public int getElapsedGap() {
		return elapsedGap;
	}

	public void setElapsedGap(int elapsedGap) {
		this.elapsedGap = elapsedGap;
	}

	public int getRouteDuration() {
		return routeDuration;
	}

	public void setRouteDuration(int routeDuration) {
		this.routeDuration = routeDuration;
	}

	public int getRouteGap() {
		return routeGap;
	}

	public void setRouteGap(int routeGap) {
		this.routeGap = routeGap;
	}

	public int getRouteLenght() {
		return routeLenght;
	}

	public void setRouteLength(int routeLenght) {
		this.routeLenght = routeLenght;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/* -- Parcelable Methods -- */
	private ExcursionReport(Parcel in) {
		id = in.readInt();
		routeName = in.readString();
		path = in.readParcelable(PointList.class.getClassLoader());
		date = new Date(in.readLong());
		completionIndex = in.readFloat();
		elapsedDurationSeconds = in.readInt();
		elapsedLenght = in.readInt();
		elapsedGap = in.readInt();
		routeDuration = in.readInt();
		routeGap = in.readInt();
		routeLenght = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(routeName);
		dest.writeParcelable(path, 0);
		dest.writeLong(date.getTime());
		dest.writeFloat(completionIndex);
		dest.writeInt(elapsedDurationSeconds);
		dest.writeInt(elapsedLenght);
		dest.writeInt(elapsedGap);
		dest.writeInt(routeDuration);
		dest.writeInt(routeGap);
		dest.writeInt(routeLenght);
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
package it.polimi.dima.dacc.mountainroutes.walktracker.tracker;

public class TrackerException extends Exception{

	private static final long serialVersionUID = 4112850390614019775L;

	public static enum Type {
		FAR_FROM_START, GOING_BACKWARD, FAR_FROM_ROUTE
	}

	private Type type;

	public TrackerException(Type type) {
		super(type.name());
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	


}

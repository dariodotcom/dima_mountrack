package it.polimi.dima.dacc.mountainroutes.walktracker.service;

public class ExcursionException extends Exception {

	private static final long serialVersionUID = -2257077933583510696L;

	private ExcursionException.Type type;
	
	public ExcursionException(ExcursionException.Type type){
		super();
		this.type = type;
	}
	
	public ExcursionException.Type getType(){
		return type;
	}
	
	public static enum Type {
		FAR_FROM_ROUTE, GOING_BACKWARDS
	}
}

package it.polimi.dima.dacc.mountainroutes.persistence;

public class PersistenceException extends Exception {

	private static final long serialVersionUID = -3388155218677244835L;

	public PersistenceException(Throwable e) {
		super(e);
	}

	public PersistenceException(String message) {
		super(message);
	}
}

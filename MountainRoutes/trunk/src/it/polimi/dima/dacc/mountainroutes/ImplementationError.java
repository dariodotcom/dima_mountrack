package it.polimi.dima.dacc.mountainroutes;

public class ImplementationError extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ImplementationError(String message) {
		super(message);
	}

	public ImplementationError(String message, Throwable cause) {
		super(message, cause);
	}

}

package it.polimi.dima.dacc.mountainroutes.remote;

/**
 * Exception that is thrown by track providers.
 */
public class ProviderException extends Exception {

	private static final long serialVersionUID = -5995231160281016565L;

	public ProviderException(Throwable cause){
		super(cause);
	}
	
	public ProviderException(String message){
		super(message);
	}
}

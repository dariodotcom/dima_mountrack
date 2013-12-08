package it.polimi.dima.dacc.mountainroutes.remote;

import it.polimi.dima.dacc.mountainroutes.remotecontent.ProviderException;

import org.apache.http.client.methods.HttpRequestBase;

public interface ContentProvider {

	public String getName();

	public String getID();

	public HttpRequestBase createRequestFor(ContentQuery query)
			throws ProviderException;

	public Object handleResult(String response, ContentQuery query)
			throws ProviderException;
}

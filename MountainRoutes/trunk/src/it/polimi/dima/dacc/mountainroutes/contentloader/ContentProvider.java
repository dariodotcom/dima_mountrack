package it.polimi.dima.dacc.mountainroutes.contentloader;

import org.apache.http.client.methods.HttpRequestBase;

public interface ContentProvider {

	public String getName();

	public String getID();

	public HttpRequestBase createRequestFor(ContentQuery query)
			throws ProviderException;

	public LoaderResult handleResult(String response, ContentQuery query)
			throws ProviderException;
}

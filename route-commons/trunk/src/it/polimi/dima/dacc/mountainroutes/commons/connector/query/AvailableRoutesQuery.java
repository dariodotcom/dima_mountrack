package it.polimi.dima.dacc.mountainroutes.commons.connector.query;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

public class AvailableRoutesQuery extends Query {

	@Override
	protected HttpRequestBase createRequest() {
		String url = ENDPOINT_URL;
		return new HttpGet(url);
	}

	@Override
	protected QueryType getQueryType() {
		return QueryType.AVAILABLE;
	}

}

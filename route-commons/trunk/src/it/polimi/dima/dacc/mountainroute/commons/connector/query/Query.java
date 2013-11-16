package it.polimi.dima.dacc.mountainroute.commons.connector.query;

import org.apache.http.client.methods.HttpRequestBase;

public abstract class Query {

	protected static final String ENDPOINT_URL = "http://dima-dacc-mountainroute.appspot.com/routes/";

	public enum QueryType {
		AVAILABLE, ID, LOCATION, CREATE
	}

	private HttpRequestBase request;
	private QueryType type;

	protected abstract HttpRequestBase createRequest();

	protected abstract QueryType getQueryType();

	public QueryType getType() {
		if (type == null) {
			type = getQueryType();
		}

		return type;
	}

	public HttpRequestBase getRequest() {
		if (request == null) {
			this.request = createRequest();
		}

		return request;
	}
}
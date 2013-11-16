package it.polimi.dima.dacc.mountainroute.commons.connector.query;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

public class RouteQuery extends Query {

	private String id;

	public RouteQuery(String id) {
		this.id = id;
	}

	@Override
	protected HttpRequestBase createRequest() {
		String url = ENDPOINT_URL + id;
		return new HttpGet(url);
	}

	@Override
	protected QueryType getQueryType() {
		return QueryType.ID;
	}

}

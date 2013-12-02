package it.polimi.dima.dacc.mountainroutes.commons.connector.query;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

public class ByNameQuery extends Query{

	private String name;
	
	public ByNameQuery(String name) {
		this.name = name;
	}

	@Override
	protected HttpRequestBase createRequest() {
		String url = ENDPOINT_URL + "name/" + name;
		return new HttpGet(url);
	}

	@Override
	protected QueryType getQueryType() {
		return QueryType.BYNAME;
	}

}

package it.polimi.dima.dacc.mountainroute.commons.connector.query;

import java.io.UnsupportedEncodingException;

import it.polimi.dima.dacc.mountainroute.commons.connector.JsonParser;
import it.polimi.dima.dacc.mountainroute.commons.types.Route;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

public class CreateRouteQuery extends Query {

	private Route route;

	public CreateRouteQuery(Route r) {
		this.route = r;
	}

	@Override
	protected HttpRequestBase createRequest() {
		String url = ENDPOINT_URL + "create";
		HttpPost request = new HttpPost(url);
		String json = JsonParser.ParseRoute.toJson(route);
		try {
			request.setEntity(new StringEntity(json));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		return request;
	}

	@Override
	protected QueryType getQueryType() {
		return QueryType.ID;
	}

}

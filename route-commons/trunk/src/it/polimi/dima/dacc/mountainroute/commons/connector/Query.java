package it.polimi.dima.dacc.mountainroute.commons.connector;

public class Query {

	public enum QueryType {
		AVAILABLE, ID, LOCATION, CREATE
	}

	private QueryType type;
	private String param;

	public Query(QueryType type, String param) {
		this.type = type;
		this.param = param;
	}

	public String getParam() {
		return param;
	}

	public QueryType getType() {
		return type;
	}
}
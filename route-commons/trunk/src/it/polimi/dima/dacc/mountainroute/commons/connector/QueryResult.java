package it.polimi.dima.dacc.mountainroute.commons.connector;

public class QueryResult {
	private Object result;
	private Query originalQuery;

	public QueryResult(Object result, Query originalQuery) {
		this.result = result;
		this.originalQuery = originalQuery;
	}

	public Query getOriginalQuery() {
		return originalQuery;
	}

	public <T> T as(Class<T> type) {
		return type.cast(result);
	}
}

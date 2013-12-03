package it.polimi.dima.dacc.mountainroutes.contentloader;

public class LoaderResult {

	private Object result;
	private ContentQuery query;
	
	public LoaderResult(Object result, ContentQuery query) {
		super();
		this.result = result;
		this.query = query;
	}

	public <E> E getResult(Class<E> type) {
		return type.cast(result);
	}

	public ContentQuery getQuery() {
		return query;
	}
}

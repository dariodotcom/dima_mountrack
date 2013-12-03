package it.polimi.dima.dacc.mountainroutes.contentloader;

import android.os.Bundle;

/**
 * Class to represent a query made to the content provider;
 * 
 * @author Chiara
 * 
 */
public class ContentQuery {

	private Bundle params;
	private QueryType type;

	public ContentQuery(QueryType type) {
		this.type = type;
		this.params = new Bundle();
	}

	public QueryType getType() {
		return type;
	}

	public Bundle getParams() {
		return params;
	}

	public static enum QueryType {
		BYNAME, NEAR, ID
	}
}

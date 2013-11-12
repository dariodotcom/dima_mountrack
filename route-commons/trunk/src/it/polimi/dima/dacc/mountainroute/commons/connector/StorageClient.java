package it.polimi.dima.dacc.mountainroute.commons.connector;

import it.polimi.dima.dacc.mountainroute.commons.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class StorageClient extends AsyncTask<Query, Void, QueryResult> {

	private final String sourceUrl = "http://dima-dacc-mountainroute.appspot.com/routes/";
	private ResultCallback callback;
	private Logger logger = new Logger("STORAGE_CLIENT");

	public StorageClient(ResultCallback callback) {
		this.callback = callback;
	}

	@Override
	protected QueryResult doInBackground(Query... params) {
		if (params.length != 1) {
			throw new RuntimeException("Please provide exactly ONE query");
		}

		Query q = params[0];

		// Execute just the first query.
		InputStream in = establishConnection(q);
		String json = getContent(in);
		return parseResponse(json, q);
	}

	@Override
	protected void onPostExecute(QueryResult result) {
		this.callback.onResult(result);
	}

	private InputStream establishConnection(Query query) {
		HttpClient httpClient = new DefaultHttpClient();
		StringBuilder urlBuilder = new StringBuilder(sourceUrl);
		HttpRequestBase request;

		switch (query.getType()) {
		case AVAILABLE:
			request = new HttpGet(urlBuilder.toString());
			break;
		case ID:
			urlBuilder.append(query.getParam());
			request = new HttpGet(urlBuilder.toString());
			break;
		default:
			throw new RuntimeException("Not implemented");
		}

		logger.d("Request url:" + urlBuilder.toString());

		try {
			HttpResponse response = httpClient.execute(request);
			return response.getEntity().getContent();
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String getContent(InputStream in) {
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader reader = new BufferedReader(isr);

		StringBuilder content = new StringBuilder();
		String line;

		try {
			line = reader.readLine();
			while (line != null) {
				content.append(line);
				content.append("\n");
				line = reader.readLine();
			}

			reader.close();
			String json = content.toString();
			logger.d("downloaded json: " + json);
			return json;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private QueryResult parseResponse(String json, Query query) {

		QueryResult result;

		switch (query.getType()) {
		case AVAILABLE:
			result = new QueryResult(JsonParser.parseRouteList(json), query);
			break;
		case ID:
			result = new QueryResult(JsonParser.parseRoute(json), query);
			break;
		default:
			throw new RuntimeException("Not yet implemented");
		}

		logger.d("Parsing complete");
		return result;
	}

	public interface ResultCallback {
		public void onResult(QueryResult result);
	}
}
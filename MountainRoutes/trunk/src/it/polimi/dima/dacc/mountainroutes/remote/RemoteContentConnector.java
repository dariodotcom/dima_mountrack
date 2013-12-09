package it.polimi.dima.dacc.mountainroutes.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class RemoteContentConnector {

	private final static String TAG = "remote-content-connector";
	private ContentProvider provider;
	private Context context;

	public RemoteContentConnector(Context context, ContentProvider provider) {
		if (provider == null) {
			throw new IllegalArgumentException("Provider must not be null");
		}

		if (context == null) {
			throw new IllegalArgumentException("Context must not be null");
		}
		this.provider = provider;
		this.context = context;
	}

	public <E> LoadResult<E> executeQuery(ContentQuery query,
			Class<E> resultType) {

		// Check that network is available
		if (!isNetworkAvailable()) {
			return new LoadResult<E>(LoadError.NETWORK_UNAVAILABLE);
		}

		// Create request
		HttpRequestBase request;

		try {
			request = provider.createRequestFor(query);
		} catch (ProviderException e) {
			Log.e(TAG, "Error creating request", e);
			return new LoadResult<E>(LoadError.INTERNAL_ERROR);
		}

		// Establish connection
		InputStream inputStream;

		try {
			inputStream = establishConnection(request);
		} catch (ProviderException e) {
			Log.e(TAG, "Error establishing request.", e);
			return new LoadResult<E>(LoadError.NETWORK_UNAVAILABLE);
		}

		// Read input stream
		String content;
		try {
			content = readContent(inputStream);
		} catch (IOException e) {
			Log.e(TAG, "Error reading response.", e);
			return new LoadResult<E>(LoadError.INTERNAL_ERROR);
		}

		// Parse response
		try {
			Object resultObj = provider.handleResult(content, query);
			E result = resultType.cast(resultObj);
			return new LoadResult<E>(result);
		} catch (ProviderException e) {
			Log.e(TAG, "Error parsing the response.", e);
			return new LoadResult<E>(LoadError.INTERNAL_ERROR);
		} catch (ClassCastException e) {
			Log.e(TAG, "Result type mismatch.", e);
			return new LoadResult<E>(LoadError.INTERNAL_ERROR);
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private InputStream establishConnection(HttpRequestBase request)
			throws ProviderException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;

		try {
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			throw new ProviderException(e);
		} catch (IOException e) {
			throw new ProviderException(e);
		}

		int status = response.getStatusLine().getStatusCode();
		if (status != HttpStatus.SC_OK) {
			throw new ProviderException("Received status " + status);
		}

		try {
			return response.getEntity().getContent();
		} catch (IllegalStateException e) {
			throw new ProviderException(e);
		} catch (IOException e) {
			throw new ProviderException(e);
		}
	}

	private String readContent(InputStream input) throws IOException {
		InputStreamReader isr = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(isr);

		StringBuilder content = new StringBuilder();
		String line;

		line = reader.readLine();
		while (line != null) {
			content.append(line);
			content.append("\n");
			line = reader.readLine();
		}

		reader.close();
		String json = content.toString();
		return json;
	}
}
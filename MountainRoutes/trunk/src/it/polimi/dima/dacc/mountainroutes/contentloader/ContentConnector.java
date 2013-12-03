package it.polimi.dima.dacc.mountainroutes.contentloader;

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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class ContentConnector extends
		AsyncTask<ContentQuery, Void, LoaderResult> {

	private final static String TAG = "content-connector";
	
	private ContentProvider provider;
	private LoaderObserver observer;
	private Context context;
	private ContentErrorType errorOccurred;

	ContentConnector(Context context, ContentProvider provider,
			LoaderObserver callback) {
		this.provider = provider;
		this.observer = callback;
		this.context = context;
	}

	@Override
	protected LoaderResult doInBackground(ContentQuery... params) {
		if (params == null || params[0] == null) {
			return null;
		}

		if (!isNetworkAvailable()) {
			errorOccurred = ContentErrorType.NETWORK_UNAVAILABLE;
			cancel(true);
			return null;
		}

		ContentQuery query = params[0];
		HttpRequestBase request;
		
		try {
			 request = provider.createRequestFor(query);
		} catch (ProviderException e) {
			errorOccurred = ContentErrorType.SERVER_ERROR;
			handleError(e);
			return null;
		}
		
		Log.d(TAG, "endpoint url: " + request.getURI());
		
		// Establish connection
		InputStream input;
		try {
			input = establishConnection(request);
		} catch (ProviderException e) {
			errorOccurred = ContentErrorType.SERVER_ERROR;
			handleError(e);
			return null;
		}

		// Get content
		String content;
		try {
			content = readContent(input);
		} catch (IOException e) {
			errorOccurred = ContentErrorType.SERVER_ERROR;
			handleError(e);
			return null;
		}

		Log.d(TAG, "Received data: " + content);
		
		// Handle result
		try {
			LoaderResult result = provider.handleResult(content, query);
			Log.d(TAG, "Parse complete");
			return result;
		} catch (ProviderException e) {
			errorOccurred = ContentErrorType.RESPONSE_ERROR;
			handleError(e);
			return null;
		}

	}

	@Override
	protected void onPostExecute(LoaderResult result) {
		observer.onLoadResult(result);
	}

	@Override
	protected void onCancelled(LoaderResult result) {
		observer.onLoadError(errorOccurred == null ? ContentErrorType.GENERIC_ERROR
				: errorOccurred);
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

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	private void handleError(Throwable t){
		cancel(true);
		Log.e(TAG, "Exception occurred: ", t);
	}
}

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

public class ContentConnector {

	private final static String TAG = "content-connector";

	private Context context;
	private ContentProvider provider;
	private Executor runningExecutor;

	public ContentConnector(Context context, ContentProvider provider) {
		this.context = context;
		this.provider = provider;
	}

	public void executeQuery(ContentQuery query, LoaderObserver observer) {
		if (runningExecutor != null) {
			runningExecutor.cancel(true);
		}

		runningExecutor = new Executor(observer);
		runningExecutor.execute(query);
	}

	private class Executor extends AsyncTask<ContentQuery, Void, LoaderResult> {

		private LoaderObserver observer;

		public Executor(LoaderObserver callback) {
			this.observer = callback;
		}

		@Override
		protected LoaderResult doInBackground(ContentQuery... params) {
			if (params == null || params[0] == null) {
				return null;
			}

			if (!isNetworkAvailable()) {
				handleError(ContentErrorType.NETWORK_UNAVAILABLE);
				return null;
			}

			ContentQuery query = params[0];
			HttpRequestBase request;

			try {
				request = provider.createRequestFor(query);
			} catch (ProviderException e) {
				handleError(ContentErrorType.SERVER_ERROR);
				return null;
			}

			Log.d(TAG, "endpoint url: " + request.getURI());

			if (isCancelled()) {
				return null;
			}

			// Establish connection
			InputStream input;
			try {
				input = establishConnection(request);
			} catch (ProviderException e) {
				handleError(ContentErrorType.SERVER_ERROR);
				return null;
			}

			// Get content
			String content;
			try {
				content = readContent(input);
			} catch (IOException e) {
				handleError(ContentErrorType.SERVER_ERROR);
				return null;
			}

			Log.d(TAG, "Received data: " + content);

			if (isCancelled()) {
				return null;
			}

			// Handle result
			try {
				LoaderResult result = provider.handleResult(content, query);
				Log.d(TAG, "Parse complete");
				return result;
			} catch (ProviderException e) {
				handleError(ContentErrorType.RESPONSE_ERROR);
				return null;
			}
		}

		private void handleError(ContentErrorType error) {
			observer.onLoadError(error);
		}

		@Override
		protected void onPostExecute(LoaderResult result) {
			observer.onLoadResult(result);
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
	}
}

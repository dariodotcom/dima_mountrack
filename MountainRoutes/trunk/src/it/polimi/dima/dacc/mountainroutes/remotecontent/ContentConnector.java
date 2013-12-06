package it.polimi.dima.dacc.mountainroutes.remotecontent;

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
		if (runningExecutor != null && !runningExecutor.isCompleted()) {
			runningExecutor.cancel(true);
		}

		runningExecutor = new Executor(observer);
		runningExecutor.execute(query);
	}

	private class Executor extends
			AsyncTask<ContentQuery, Void, ResultContainer> {

		private boolean completed = false;
		private LoaderObserver observer;

		public Executor(LoaderObserver callback) {
			this.observer = callback;
		}

		public boolean isCompleted(){
			return completed;
		}
		
		@Override
		protected ResultContainer doInBackground(ContentQuery... params) {
			if (!isNetworkAvailable()) {
				return handleError(LoadError.NETWORK_UNAVAILABLE);
			}

			ContentQuery query = params[0];
			HttpRequestBase request;

			try {
				request = provider.createRequestFor(query);
			} catch (ProviderException e) {
				return handleError(LoadError.SERVER_ERROR);
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
				return handleError(LoadError.SERVER_ERROR);
			}

			// Get content
			String content;
			try {
				content = readContent(input);
			} catch (IOException e) {
				return handleError(LoadError.SERVER_ERROR);
			}

			Log.d(TAG, "Received data: " + content);

			if (isCancelled()) {
				return null;
			}

			// Handle result
			try {
				LoaderResult loadResult = provider.handleResult(content, query);
				Log.d(TAG, "Parse complete");
				ResultContainer container = new ResultContainer(
						ResultContainer.SUCCESS);
				container.setResult(loadResult);
				completed = true;
				return container;
			} catch (ProviderException e) {
				return handleError(LoadError.RESPONSE_ERROR);
			}
		}

		private ResultContainer handleError(LoadError error) {
			ResultContainer container = new ResultContainer(ResultContainer.ERROR);
			container.setError(error);
			return container;
		}

		@Override
		protected void onPostExecute(ResultContainer result) {
			switch (result.getType()) {
			case ResultContainer.SUCCESS:
				observer.onLoadResult(result.getResult());
				break;
			case ResultContainer.ERROR:
				observer.onLoadError(result.getError());
				break;
			}
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

	private static class ResultContainer {

		public final static int SUCCESS = 0;
		public final static int ERROR = 1;

		private int type;
		private LoaderResult result;
		private LoadError error;

		public ResultContainer(int containerType) {
			this.type = containerType;
		}

		public void setError(LoadError error) {
			if (this.type == SUCCESS) {
				throw new IllegalStateException(
						"Positive result cannot contain error");
			}

			this.error = error;
		}

		public void setResult(LoaderResult result) {
			this.result = result;
		}

		public int getType() {
			return type;
		}

		public LoaderResult getResult() {
			return result;
		}

		public LoadError getError() {
			return error;
		}
	}
}

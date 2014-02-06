package it.polimi.dima.dacc.mountainroutes.remote;

import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Class that resolves the altitude of a geopoint performing a request to
 * Google Maps API
 */
public class AltitudeGapResolver extends AsyncTask<LatLng, Void, LoadResult<AltitudeGap>> {

	private static final String RESPONSE_ELEVATION = "elevation";
	private static final String RESPONSE_RESULT = "results";
	private static final String RESPONSE_OK = "OK";
	private static final String RESPONSE_STATUS = "status";
	private static final String TAG = "AltitudeLoader";
	private static final String ENDPOINT_FORMAT = "http://maps.googleapis.com/maps/api/elevation/json?locations=%s,%s&sensor=true";

	private Listener listener;
	private static Double initialAltitude;

	public static void resolve(LatLng point, Listener listener) {
		new AltitudeGapResolver(listener).execute(point);
	}

	private AltitudeGapResolver(Listener listener) {
		this.listener = listener;
	}

	@Override
	protected LoadResult<AltitudeGap> doInBackground(LatLng... params) {
		LatLng location = params[0];

		try {
			HttpEntity entity = performRequest(location);
			String content = readInput(entity);
			double altitude = getAltitude(content);

			int gapValue;

			if (initialAltitude == null) {
				initialAltitude = altitude;
				gapValue = 0;
			} else {
				gapValue = (int) Math.round(altitude - initialAltitude);
			}

			AltitudeGap gap = new AltitudeGap(gapValue, location);
			return new LoadResult<AltitudeGap>(gap);
		} catch (Throwable t) {
			Log.e(TAG, "Exception in resolving altitude: ", t);
			return new LoadResult<AltitudeGap>(LoadError.NETWORK_UNAVAILABLE);
		}
	}

	@Override
	protected void onPostExecute(LoadResult<AltitudeGap> result) {
		if (listener == null) {
			return;
		}

		if (result.getType() == LoadResult.RESULT) {
			listener.onAltitudeResolved(result.getResult());
		} else {
			listener.onAltitudeError(result.getError());
		}
	}

	public static interface Listener {
		public void onAltitudeResolved(AltitudeGap altitude);

		public void onAltitudeError(LoadError error);
	}

	private HttpEntity performRequest(LatLng point) throws ClientProtocolException, IOException {
		String endpoint = String.format(ENDPOINT_FORMAT, point.latitude, point.longitude);
		HttpGet get = new HttpGet(endpoint);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(get);
		return response.getEntity();
	}

	private String readInput(HttpEntity entity) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));

		String line = reader.readLine();
		while (line != null) {
			builder.append(line);
			line = reader.readLine();
		}

		return builder.toString();
	}

	private Double getAltitude(String content) throws JSONException {
		JSONObject root = new JSONObject(content);

		String status = root.getString(RESPONSE_STATUS);
		if (status == null || !status.equals(RESPONSE_OK)) {
			return null;
		}

		Double altitude = root.getJSONArray(RESPONSE_RESULT).getJSONObject(0).getDouble(RESPONSE_ELEVATION);
		return altitude;
	}
}
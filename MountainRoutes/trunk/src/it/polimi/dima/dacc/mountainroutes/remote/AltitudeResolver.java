package it.polimi.dima.dacc.mountainroutes.remote;

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

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class AltitudeResolver {

	private static final String RESPONSE_ELEVATION = "elevation";
	private static final String RESPONSE_RESULT = "results";
	private static final String RESPONSE_OK = "OK";
	private static final String RESPONSE_STATUS = "status";
	private static final String TAG = AltitudeResolver.class.getName();
	private static final String ENDPOINT_FORMAT = "http://maps.googleapis.com/maps/api/elevation/json?locations=%s,%s&sensor=true";

	public static Double resolve(LatLng location) {

		try {
			HttpEntity entity = performRequest(location);
			String content = readInput(entity);
			return getAltitude(content);
		} catch (Throwable t) {
			Log.e(TAG, "Exception in resolving altitude: ", t);
			return null;
		}
	}

	private static HttpEntity performRequest(LatLng point) throws ClientProtocolException, IOException {
		String endpoint = String.format(ENDPOINT_FORMAT, point.latitude, point.longitude);
		Log.d(TAG, "endpoint: " + endpoint);
		HttpGet get = new HttpGet(endpoint);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(get);
		return response.getEntity();
	}

	private static String readInput(HttpEntity entity) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));

		String line = reader.readLine();
		while (line != null) {
			builder.append(line);
			line = reader.readLine();
		}

		Log.d(TAG, "result: " + builder.toString());
		return builder.toString();
	}

	private static Double getAltitude(String content) throws JSONException {
		JSONObject root = new JSONObject(content);

		String status = root.getString(RESPONSE_STATUS);
		if (status == null || !status.equals(RESPONSE_OK)) {
			return null;
		}

		Double altitude = root.getJSONArray(RESPONSE_RESULT).getJSONObject(0).getDouble(RESPONSE_ELEVATION);
		Log.d(TAG, "altitude: " + altitude);
		return altitude;
	}

}
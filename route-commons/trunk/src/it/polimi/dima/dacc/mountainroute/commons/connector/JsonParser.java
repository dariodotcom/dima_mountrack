package it.polimi.dima.dacc.mountainroute.commons.connector;

import it.polimi.dima.dacc.mountainroute.commons.types.Route;
import it.polimi.dima.dacc.mountainroute.commons.types.RouteDescription;
import it.polimi.dima.dacc.mountainroute.commons.types.RouteDescriptionList;
import it.polimi.dima.dacc.mountainroute.commons.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class JsonParser {

	private static final String ROUTE_NAME = "name";
	private static final String ROUTE_DURATION = "traversalTime";
	private static final String ROUTE_POINT = "points";
	private static final String ROUTE_ID = "id";
	private static final String POINT_LATITUDE = "latitude";
	private static final String POINT_LONGITUDE = "longitude";

	private static Logger logger = new Logger("JSON_PARSER");

	private JsonParser() {

	}

	public static Route parseRoute(String json) {
		try {
			JSONObject obj = new JSONObject(json);

			String id = obj.getString(ROUTE_ID);
			String name = obj.getString(ROUTE_NAME);
			List<LatLng> points = parsePointList(obj.getJSONArray(ROUTE_POINT));
			int duration = obj.getInt(ROUTE_DURATION);
			Route r = new Route(id, name, points, duration);
			logger.d("Parsed route: " + r);
			return r;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

	}

	public static RouteDescriptionList parseRouteList(String json) {
		RouteDescriptionList result = new RouteDescriptionList();

		try {
			JSONArray array = new JSONArray(json);

			for (int i = 0; i < array.length(); i++) {

				JSONObject obj = array.getJSONObject(i);

				String id = obj.getString(ROUTE_ID);
				String name = obj.getString(ROUTE_NAME);
				result.addRouteDescription(new RouteDescription(id, name));
			}

			logger.d("Parsed list: " + result);
			return result;

		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<LatLng> parsePointList(JSONArray array) {
		List<LatLng> result = new ArrayList<LatLng>();

		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject obj = array.getJSONObject(i);
				double lat = obj.getDouble(POINT_LATITUDE);
				double lng = obj.getDouble(POINT_LONGITUDE);
				result.add(new LatLng(lat, lng));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		return result;
	}
}

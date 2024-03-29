package it.polimi.dima.dacc.mountainroutes.commons.connector;

import it.polimi.dima.dacc.mountainroutes.commons.types.PointList;
import it.polimi.dima.dacc.mountainroutes.commons.types.Route;
import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescription;
import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescriptionList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class JsonParser {

	public static final class ParseRoute {
		private static final String NAME = "name";
		private static final String DURATION = "traversalTime";
		private static final String POINTS = "points";
		private static final String ID = "id";

		private ParseRoute() {
		}

		public static Route fromJson(String json) {
			try {
				JSONObject obj = new JSONObject(json);

				String id = obj.getString(ID);
				String name = obj.getString(NAME);
				PointList points = pointListFromJson(obj.getJSONArray(POINTS));
				int duration = obj.getInt(DURATION);

				Route r = new Route();
				r.setId(id);
				r.setName(name);
				r.setRoute(points);
				r.setDuration(duration);
				return r;
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		public static String toJson(Route r) {
			try {
				JSONObject obj = new JSONObject();
				obj.put(NAME, r.getName());
				obj.put(POINTS, pointListToJson(r.getRoute()));
				obj.put(DURATION, r.getDuration());

				return obj.toString();
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		private static PointList pointListFromJson(JSONArray array) {
			PointList result = new PointList();
			try {
				for (int i = 0; i < array.length(); i++) {
					JSONObject pt = array.getJSONObject(i);
					result.add(JsonParser.ParsePoint.fromJson(pt));
				}

				return result;
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		private static JSONArray pointListToJson(PointList input) {
			JSONArray array = new JSONArray();
			for (LatLng p : input) {
				array.put(ParsePoint.toJson(p));
			}
			return array;
		}
	}

	public static final class ParseRouteList {
		private static final String NAME = "name";
		private static final String ID = "id";

		private ParseRouteList() {
		}

		public static RouteDescriptionList fromJson(String json) {
			RouteDescriptionList result = new RouteDescriptionList();

			try {
				JSONArray array = new JSONArray(json);

				for (int i = 0; i < array.length(); i++) {

					JSONObject obj = array.getJSONObject(i);

					String id = obj.getString(ID);
					String name = obj.getString(NAME);
					result.addRouteDescription(new RouteDescription(id, name));
				}

				return result;

			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Methods to parse points from and to json
	 */
	public static final class ParsePoint {
		private static final String LATITUDE = "latitude";
		private static final String LONGITUDE = "longitude";

		private ParsePoint() {

		}

		public static LatLng fromJson(JSONObject obj) {
			try {
				double lat = obj.getDouble(LATITUDE);
				double lng = obj.getDouble(LONGITUDE);
				return new LatLng(lat, lng);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		public static JSONObject toJson(LatLng p) {
			JSONObject obj = new JSONObject();
			try {
				obj.put(LATITUDE, p.latitude);
				obj.put(LONGITUDE, p.longitude);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			return obj;
		}
	}

}

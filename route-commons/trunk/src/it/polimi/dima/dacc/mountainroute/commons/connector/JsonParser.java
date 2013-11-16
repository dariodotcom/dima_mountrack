package it.polimi.dima.dacc.mountainroute.commons.connector;

import it.polimi.dima.dacc.mountainroute.commons.types.Point;
import it.polimi.dima.dacc.mountainroute.commons.types.Route;
import it.polimi.dima.dacc.mountainroute.commons.types.RouteDescription;
import it.polimi.dima.dacc.mountainroute.commons.types.RouteDescriptionList;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

	public static final class ParsePoint {
		private static final String LATITUDE = "latitude";
		private static final String LONGITUDE = "longitude";

		private ParsePoint() {

		}

		public static Point fromJson(JSONObject obj) {
			try {
				double lat = obj.getDouble(LATITUDE);
				double lng = obj.getDouble(LONGITUDE);
				return new Point(lat, lng);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		public static String toJson(Point p) {
			JSONObject obj = new JSONObject();
			try {
				obj.put(LATITUDE, p.getLatitude());
				obj.put(LONGITUDE, p.getLongitude());
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			return obj.toString();
		}
	}

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
				List<Point> points = pointListFromJson(obj.getJSONArray(POINTS));
				int duration = obj.getInt(DURATION);

				return new Route(id, name, points, duration);
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

		private static List<Point> pointListFromJson(JSONArray array) {
			List<Point> result = new ArrayList<Point>();
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

		private static JSONArray pointListToJson(List<Point> input) {
			JSONArray array = new JSONArray();
			for (Point p : input) {
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
}

package it.polimi.dima.dacc.mountainroutes.persistence.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import it.polimi.dima.dacc.mountainroutes.types.PointList;

/**
 * Json converter for point lists
 */
public class PointListConverter {

	public static String toString(PointList points) throws ConverterException {
		JSONArray array = new JSONArray();
		for (LatLng p : points) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("latitude", p.latitude);
				obj.put("longitude", p.longitude);
				array.put(obj);
			} catch (JSONException e) {
				throw new ConverterException();
			}
		}
		return array.toString();
	}

	public static PointList fromString(String string) throws ConverterException {
		PointList list = new PointList();
		try {
			JSONArray array = new JSONArray(string);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				double latitude = object.getDouble("latitude");
				double longitude = object.getDouble("longitude");
				LatLng point = new LatLng(latitude, longitude);
				list.add(point);
			}
		} catch (JSONException e) {
			Log.d("pointlist-converter", "exception: ", e);
			throw new ConverterException();
		}
		return list;
	}

	public static class ConverterException extends Exception {

		private static final long serialVersionUID = -8660084402633727273L;

	}
}

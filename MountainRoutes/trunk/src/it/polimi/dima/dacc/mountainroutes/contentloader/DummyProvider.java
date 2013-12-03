package it.polimi.dima.dacc.mountainroutes.contentloader;

import it.polimi.dima.dacc.mountainroutes.ImplementationError;
import it.polimi.dima.dacc.mountainroutes.commons.types.Difficulty;
import it.polimi.dima.dacc.mountainroutes.commons.types.PointList;
import it.polimi.dima.dacc.mountainroutes.commons.types.Route;
import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescription;
import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescriptionList;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class DummyProvider implements ContentProvider {

	private final static String NAME = "Test provider";
	private final static String ID = "e6brx2";
	private static final String ENDPOINT_URL = "http://dima-dacc-mountainroute.appspot.com/routes/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public HttpRequestBase createRequestFor(ContentQuery query) {

		switch (query.getType()) {
		case BYNAME:
			return createNameQuery(query);
		case NEAR:
			return createPositionQuery(query);
		case ID:
			return createIdQuery(query);
		default:
			throw new ImplementationError("Fallen outside switch on enum.");
		}
	}

	@Override
	public LoaderResult handleResult(String content, ContentQuery query)
			throws ProviderException {
		try {
			switch (query.getType()) {
			case BYNAME:
			case NEAR:
				// Parse a list of route description
				RouteDescriptionList list = JSONParser
						.parseRouteDescriptionList(content);
				return new LoaderResult(list, query);

			case ID:
				Route route = JSONParser.parseRoute(content);
				return new LoaderResult(route, query);
			default:
				throw new ImplementationError(
						"Fallen oustide of a switch on enum");
			}
		} catch (ParserException e) {
			throw new ProviderException(e);
		}
	}

	private HttpRequestBase createNameQuery(ContentQuery q) {
		String paramName = ContentLoader.NAME_PARAM;
		String name = q.getParams().getString(paramName);
		if (name == null) {
			throw new ImplementationError("Missing required String parameter "
					+ paramName);
		}

		String url = ENDPOINT_URL + "name/" + name;
		return new HttpGet(url);
	}

	private HttpRequestBase createPositionQuery(ContentQuery q) {
		String paramName = ContentLoader.POSITION_PARAM;
		LatLng position = (LatLng) q.getParams().getParcelable(paramName);
		if (position == null) {
			throw new ImplementationError("Missing required LatLng parameter "
					+ paramName);
		}

		String posRep = position.latitude + "," + position.longitude;
		String url = ENDPOINT_URL + "near/" + posRep;
		return new HttpGet(url);
	}

	private HttpRequestBase createIdQuery(ContentQuery q) {
		String paramName = ContentLoader.ID_PARAM;
		String id = q.getParams().getString(paramName);
		if (id == null) {
			throw new ImplementationError("Missing required String parameter "
					+ paramName);
		}

		String url = ENDPOINT_URL + "route/" + id;
		return new HttpGet(url);
	}

	private static class JSONParser {
		private static final String NAME = "name";
		private static final String ID = "id";
		private static final String LATITUDE = "latitude";
		private static final String LONGITUDE = "longitude";

		public static RouteDescription parseRouteDescription(JSONObject obj)
				throws ParserException {

			String id, name;

			try {
				id = obj.getString(ID);
				name = obj.getString(NAME);
			} catch (JSONException e) {
				throw new ParserException(e);
			}

			return new RouteDescription(id, name);
		}

		public static RouteDescriptionList parseRouteDescriptionList(
				String content) throws ParserException {

			RouteDescriptionList list = new RouteDescriptionList();

			try {
				JSONArray array = new JSONArray(content);

				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					RouteDescription desc = parseRouteDescription(obj);
					list.addRouteDescription(desc);
				}
			} catch (JSONException e) {
				throw new ParserException(e);
			}

			return list;
		}

		public static Route parseRoute(String content) throws ParserException {
			Route r = new Route();

			try {
				JSONObject obj = new JSONObject(content);
				r.setId(obj.getString(ID));
				r.setName(obj.getString(NAME));
				r.setGap(obj.getInt("gap"));
				r.setLength(obj.getInt("length"));
				r.setDuration(obj.getInt("duration"));
				r.setDifficulty(Difficulty.valueOf(obj.getString("difficulty")));
				r.setRoute(parsePointList(obj.getJSONArray("points")));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return r;

		}

		private static PointList parsePointList(JSONArray array)
				throws JSONException {
			PointList list = new PointList();

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				double lat = obj.getDouble(LATITUDE);
				double lng = obj.getDouble(LONGITUDE);
				list.add(new LatLng(lat, lng));
			}

			return list;
		}
	}

	private static class ParserException extends Exception {

		private static final long serialVersionUID = -937792115546429192L;

		public ParserException(Throwable cause) {
			super(cause);
		}
	}
}

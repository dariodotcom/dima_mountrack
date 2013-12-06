package it.polimi.dima.dacc.mountainroutes.remotecontent;

import it.polimi.dima.dacc.mountainroutes.ImplementationError;
import it.polimi.dima.dacc.mountainroutes.types.Difficulty;
import it.polimi.dima.dacc.mountainroutes.types.PointList;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummary;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class DummyProvider implements ContentProvider {

	private final static String PROVIDER_NAME = "Test provider";
	private final static String PROVIDER_ID = "e6brx2";
	private static final String ENDPOINT_URL = "http://dima-dacc-mountainroute.appspot.com/routes/";

	@Override
	public String getName() {
		return PROVIDER_NAME;
	}

	@Override
	public String getID() {
		return PROVIDER_ID;
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
				RouteSummaryList list = JSONParser
						.parseRouteSummaryList(content);
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

		String url = ENDPOINT_URL + "id/" + id;
		return new HttpGet(url);
	}

	private static class JSONParser {

		private static final String ID = "id";
		private static final String NAME = "name";
		private static final String DIFFICULTY = "difficulty";
		private static final String DURATION_IN_MINUTES = "durationInMinutes";
		private static final String LENGTH_IN_METERS = "lengthInMeters";
		private static final String GAP_IN_METERS = "gapInMeters";
		private static final String PATH = "path";

		private static final String LATITUDE = "latitude";
		private static final String LONGITUDE = "longitude";

		public static RouteSummary parseRouteSummary(JSONObject obj)
				throws ParserException {

			RouteSummary summary;

			try {
				summary = new RouteSummary();

				// Route ID
				String routeId = obj.getString(ID);
				RouteID id = new RouteID(PROVIDER_ID, routeId);
				summary.setId(id);

				summary.setName(obj.getString(NAME));
				summary.setDurationInMinutes(obj.getInt(DURATION_IN_MINUTES));
				Difficulty difficulty = Difficulty.valueOf(obj
						.getString(DIFFICULTY));
				summary.setDifficulty(difficulty);
			} catch (JSONException e) {
				throw new ParserException(e);
			}

			return summary;
		}

		public static RouteSummaryList parseRouteSummaryList(String content)
				throws ParserException {

			RouteSummaryList list = new RouteSummaryList();

			try {
				JSONArray array = new JSONArray(content);

				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					RouteSummary desc = parseRouteSummary(obj);
					list.addRouteSummary(desc);
				}
			} catch (JSONException e) {
				throw new ParserException(e);
			}

			return list;
		}

		public static Route parseRoute(String content) throws ParserException {
			try {
				JSONObject obj = new JSONObject(content);

				// Read route ID
				String routeID = obj.getString(ID);
				RouteID id = new RouteID(DummyProvider.PROVIDER_ID, routeID);

				Route r = new Route(id, Route.Source.REMOTE);

				r.setName(obj.getString(NAME));
				r.setDurationInMinutes(obj.getInt(DURATION_IN_MINUTES));
				r.setLengthInMeters(obj.getInt(LENGTH_IN_METERS));
				r.setGapInMeters(obj.getInt(GAP_IN_METERS));
				r.setDifficulty(Difficulty.valueOf(obj.getString(DIFFICULTY)));
				r.setPath(parsePointList(obj.getJSONArray(PATH)));

				return r;
			} catch (JSONException e) {
				throw new ParserException(e);
			}
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

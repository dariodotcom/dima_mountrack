package it.polimi.dima.dacc.mountainroutes.persistence;

import it.polimi.dima.dacc.mountainroutes.persistence.PointListConverter.ConverterException;
import it.polimi.dima.dacc.mountainroutes.types.Difficulty;
import it.polimi.dima.dacc.mountainroutes.types.PointList;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummary;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RoutePersistence {

	private static String[] allColumns = { DbHelper.COLUMN_KEY,
			DbHelper.COLUMN_NAME, DbHelper.COLUMN_DIFFICULTY,
			DbHelper.COLUMN_DURATION_IN_MINUTES,
			DbHelper.COLUMN_LENGTH_IN_METERS, DbHelper.COLUMN_GAP_IN_METERS,
			DbHelper.COLUMN_PATH };

	private DbHelper helper;
	private SQLiteDatabase database;
	private Context context;

	public static RoutePersistence create(Context context) {
		return new RoutePersistence(context);
	}

	private RoutePersistence(Context context) {
		this.helper = new DbHelper(context);
		this.database = helper.getReadableDatabase();
		this.context = context;
	}

	public void close() {
		this.helper.close();
	}

	public RouteSummaryList getAvailableRoutes() throws PersistenceException {
		Cursor c = database.query(DbHelper.DATABASE_TABLE, allColumns, null,
				null, null, null, null);
		RouteSummaryList summaryList = new RouteSummaryList();

		while (c.moveToNext()) {
			summaryList.addRouteSummary(routeSummaryFromCursor(c, context));
		}

		return summaryList;
	}

	public boolean hasRoute(RouteID id) {
		String[] columns = new String[] { "1" };
		String reference = id.toString();
		String where = DbHelper.COLUMN_ID + "=" + reference;
		Cursor c = database.query(DbHelper.DATABASE_TABLE, columns, where,
				null, null, null, null);
		boolean hasRoute = c.moveToFirst();
		c.close();
		return hasRoute;
	}

	public Route loadRoute(RouteID id) throws PersistenceException {
		String where = DbHelper.COLUMN_ID + "=" + id.toString();
		Cursor c = database.query(DbHelper.DATABASE_TABLE, allColumns, where,
				null, null, null, null);

		if (!c.moveToFirst()) {
			return null;
		}

		return routeFromCursor(c, context);
	}

	public void persistRoute(Route route) {
		if (hasRoute(route.getId())) {
			return;
		}

		ContentValues values = new ContentValues();
		values.put(DbHelper.COLUMN_ID, route.getId().toString());
		values.put(DbHelper.COLUMN_NAME, route.getName());
		values.put(DbHelper.COLUMN_DIFFICULTY, route.getDifficulty().name());
		values.put(DbHelper.COLUMN_DURATION_IN_MINUTES,
				route.getDurationInMinutes());
		values.put(DbHelper.COLUMN_LENGTH_IN_METERS, route.getLengthInMeters());
		values.put(DbHelper.COLUMN_GAP_IN_METERS, route.getGapInMeters());
		values.put(DbHelper.COLUMN_PATH, route.getPath().toString());

		database.insert(DbHelper.DATABASE_TABLE, null, values);
	}

	public void removeRoute(RouteID id) throws PersistenceException {
		if (!hasRoute(id)) {
			throw new PersistenceException("Route " + id + " not found");
		}

		String where = DbHelper.COLUMN_ID + "=" + id.toString();
		database.delete(DbHelper.DATABASE_NAME, where, null);
	}

	// Create Route reading it from cursor
	private static Route routeFromCursor(Cursor cursor, Context context)
			throws PersistenceException {

		// RouteID
		int idIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ID);
		RouteID id = new RouteID(cursor.getString(idIndex));

		// Name
		int nameIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_NAME);
		String name = cursor.getString(nameIndex);

		// Difficulty
		int difficultyIndex = cursor
				.getColumnIndexOrThrow(DbHelper.COLUMN_DIFFICULTY);
		Difficulty difficulty = Difficulty.valueOf(cursor
				.getString(difficultyIndex));

		// Duration in minutes
		int durationIndex = cursor
				.getColumnIndexOrThrow(DbHelper.COLUMN_DURATION_IN_MINUTES);
		int duration = cursor.getInt(durationIndex);

		// Length in meters
		int lengthIndex = cursor
				.getColumnIndexOrThrow(DbHelper.COLUMN_LENGTH_IN_METERS);
		int length = cursor.getInt(lengthIndex);

		// Gap in meters
		int gapIndex = cursor
				.getColumnIndexOrThrow(DbHelper.COLUMN_GAP_IN_METERS);
		int gap = cursor.getInt(gapIndex);

		// Path
		int pathIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_PATH);
		String pathString = cursor.getString(pathIndex);
		PointList list;
		try {
			list = PointListConverter.fromString(pathString);
		} catch (ConverterException e) {
			throw new PersistenceException(e);
		}

		Route route = new Route();
		route.setId(id);
		route.setName(name);
		route.setDifficulty(difficulty);
		route.setDurationInMinutes(duration);
		route.setLengthInMeters(length);
		route.setGapInMeters(gap);
		route.setPath(list);
		return route;
	}

	public static class PersistenceException extends Exception {

		private static final long serialVersionUID = -3388155218677244835L;

		public PersistenceException(Throwable e) {
			super(e);
		}

		public PersistenceException(String message) {
			super(message);
		}
	}

	private static RouteSummary routeSummaryFromCursor(Cursor cursor,
			Context context) throws PersistenceException {

		// RouteID
		int idIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ID);
		RouteID id = new RouteID(cursor.getString(idIndex));

		// Name
		int nameIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_NAME);
		String name = cursor.getString(nameIndex);

		// Difficulty
		int difficultyIndex = cursor
				.getColumnIndexOrThrow(DbHelper.COLUMN_DIFFICULTY);
		Difficulty difficulty = Difficulty.valueOf(cursor
				.getString(difficultyIndex));

		// Duration in minutes
		int durationIndex = cursor
				.getColumnIndexOrThrow(DbHelper.COLUMN_DURATION_IN_MINUTES);
		int duration = cursor.getInt(durationIndex);

		RouteSummary summary = new RouteSummary();
		summary.setId(id);
		summary.setName(name);
		summary.setDifficulty(difficulty);
		summary.setDurationInMinutes(duration);

		return summary;
	}

	// Class to handle the database
	private static class DbHelper extends SQLiteOpenHelper {

		public static final String DATABASE_NAME = "routedb";
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_TABLE = "routes";
		public static final String COLUMN_KEY = "_id";
		public static final String COLUMN_ID = "routeid";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_DIFFICULTY = "difficulty";
		public static final String COLUMN_DURATION_IN_MINUTES = "duration_in_minutes";
		public static final String COLUMN_LENGTH_IN_METERS = "length_in_meters";
		public static final String COLUMN_GAP_IN_METERS = "gap_in_meters";
		public static final String COLUMN_PATH = "path";

		private static final String DATABASE_CREATE_SQL = String.format(
				"CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,"
						+ " %s TEXT NOT NULL," + // Route ID
						" %s TEXT NOT NULL," + // Name
						" %s TEXT NOT NULL," + // Difficulty
						" %s INTEGER NOT NULL," + // Duration
						" %s INTEGER NOT NULL," + // Length
						" %s INTEGER NOT NULL," + // Gap
						" %s TEXT NOT NULL);", // Path
				DATABASE_TABLE, COLUMN_KEY, COLUMN_ID, COLUMN_NAME,
				COLUMN_DIFFICULTY, COLUMN_DURATION_IN_MINUTES,
				COLUMN_LENGTH_IN_METERS, COLUMN_GAP_IN_METERS, COLUMN_PATH);

		private static final String DATABASE_DROP_SQL = "DROP TABLE IF EXISTS "
				+ DATABASE_TABLE;

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			drop(db);
			onCreate(db);
		}

		public void drop(SQLiteDatabase db) {
			db.execSQL(DATABASE_DROP_SQL);
		}
	}
}
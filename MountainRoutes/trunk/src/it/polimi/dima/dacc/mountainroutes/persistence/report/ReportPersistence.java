package it.polimi.dima.dacc.mountainroutes.persistence.report;

import java.util.Date;

import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.route.PointListConverter;
import it.polimi.dima.dacc.mountainroutes.persistence.route.PointListConverter.ConverterException;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionList;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.PointList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReportPersistence {

	private static String[] allColumns = { DbHelper.COL_KEY, DbHelper.COL_ROUTE_NAME, DbHelper.COL_PATH, DbHelper.COL_DATE, DbHelper.COL_COMPLETION_INDEX, DbHelper.COL_ELAPSED_DURATION, DbHelper.COL_ELAPSED_GAP, DbHelper.COL_ELAPSED_LENGTH, DbHelper.COL_ROUTE_DURATION, DbHelper.COL_ROUTE_GAP, DbHelper.COL_ROUTE_LENGTH };
	private DbHelper helper;
	private SQLiteDatabase database;
	private Context context;

	public static ReportPersistence create(Context context) {
		return new ReportPersistence(context);
	}

	private ReportPersistence(Context context) {
		this.helper = new DbHelper(context);
		this.database = helper.getReadableDatabase();
		this.context = context;
	}

	public void close() {
		this.helper.close();
	}

	public ExcursionList getAvailableExcursions() throws PersistenceException {
		Cursor c = database.query(DbHelper.DATABASE_TABLE, allColumns, null, null, null, null, null);
		ExcursionList summaryList = new ExcursionList();

		while (c.moveToNext()) {
			ExcursionReport report = excursionReportFromCursor(c, context);
			summaryList.addExcursionReport(report);
		}

		return summaryList;
	}

	public void persistExcursionReport(ExcursionReport excursion) throws PersistenceException {

		String pointListRep;
		try {
			pointListRep = PointListConverter.toString(excursion.getPath());
		} catch (ConverterException e) {
			throw new PersistenceException(e);
		}

		ContentValues values = new ContentValues();
		values.put(DbHelper.COL_ROUTE_NAME, excursion.getRouteName());
		values.put(DbHelper.COL_PATH, pointListRep);
		values.put(DbHelper.COL_DATE, excursion.getDate().getTime());
		values.put(DbHelper.COL_COMPLETION_INDEX, excursion.getCompletionIndex());
		values.put(DbHelper.COL_ELAPSED_DURATION, excursion.getElapsedDuration());
		values.put(DbHelper.COL_ELAPSED_GAP, excursion.getElapsedGap());
		values.put(DbHelper.COL_ELAPSED_LENGTH, excursion.getElapsedLength());
		values.put(DbHelper.COL_ROUTE_DURATION, excursion.getRouteDuration());
		values.put(DbHelper.COL_ROUTE_GAP, excursion.getRouteGap());
		values.put(DbHelper.COL_ROUTE_LENGTH, excursion.getRouteLenght());

		if (database.insert(DbHelper.DATABASE_TABLE, null, values) == -1) {
			throw new PersistenceException("could not persist route");
		}
	}

	public void removeExcursion(int excursionId) throws PersistenceException {
		String where = DbHelper.COL_KEY + " = ?";
		String[] whereArgs = new String[] { String.valueOf(excursionId) };
		database.delete(DbHelper.DATABASE_TABLE, where, whereArgs);
	}

	private static ExcursionReport excursionReportFromCursor(Cursor cursor, Context context) throws PersistenceException {
		// Key
		int keyIndex = cursor.getColumnIndex(DbHelper.COL_KEY);
		int key = cursor.getInt(keyIndex);

		// Name
		int nameIndex = cursor.getColumnIndexOrThrow(DbHelper.COL_ROUTE_NAME);
		String name = cursor.getString(nameIndex);

		// Path
		int pathIndex = cursor.getColumnIndexOrThrow(DbHelper.COL_PATH);
		PointList path;
		try {
			path = PointListConverter.fromString(cursor.getString(pathIndex));
		} catch (ConverterException e) {
			throw new PersistenceException(e);
		}

		// Date
		int dateIndex = cursor.getColumnIndexOrThrow(DbHelper.COL_DATE);
		Date date = new Date(cursor.getLong(dateIndex));

		// Completion index
		int completionIndexIndex = cursor.getColumnIndexOrThrow(DbHelper.COL_COMPLETION_INDEX);
		float completionIndex = cursor.getFloat(completionIndexIndex);

		// Elapsed duration
		int elapsedDurationIndex = cursor.getColumnIndexOrThrow(DbHelper.COL_ELAPSED_DURATION);
		int elapsedDuration = cursor.getInt(elapsedDurationIndex);

		// ElapsedGap
		int elapsedGapIndex = cursor.getColumnIndexOrThrow(DbHelper.COL_ELAPSED_GAP);
		int elapsedGap = cursor.getInt(elapsedGapIndex);

		// ElapsedMeters
		int elapsedLengthIndex = cursor.getColumnIndexOrThrow(DbHelper.COL_ELAPSED_LENGTH);
		int elapsedLength = cursor.getInt(elapsedLengthIndex);

		// Route duration
		int routeDurationIndex = cursor.getColumnIndexOrThrow(DbHelper.COL_ROUTE_DURATION);
		int originalSeconds = cursor.getInt(routeDurationIndex);

		// Route gap
		int routeGapIndex = cursor.getColumnIndexOrThrow(DbHelper.COL_ROUTE_GAP);
		int routeGap = cursor.getInt(routeGapIndex);

		// Route meters
		int routeMetersIndex = cursor.getColumnIndexOrThrow(DbHelper.COL_ROUTE_LENGTH);
		int routeMeters = cursor.getInt(routeMetersIndex);

		ExcursionReport excursionReport = new ExcursionReport();
		excursionReport.setId(key);
		excursionReport.setRouteName(name);
		excursionReport.setPath(path);
		excursionReport.setDate(date);
		excursionReport.setCompletionIndex(completionIndex);
		excursionReport.setElapsedDuration(elapsedDuration);
		excursionReport.setElapsedGap(elapsedGap);
		excursionReport.setElapsedLength(elapsedLength);
		excursionReport.setRouteDuration(originalSeconds);
		excursionReport.setRouteGap(routeGap);
		excursionReport.setRouteLength(routeMeters);

		return excursionReport;
	}

	// Class to handle the database
	private static class DbHelper extends SQLiteOpenHelper {

		public static final String DATABASE_NAME = "reportdb";
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_TABLE = "reports";
		public static final String COL_KEY = "_id";
		public static final String COL_ROUTE_NAME = "route_name";
		public static final String COL_PATH = "path";
		public static final String COL_DATE = "date";
		public static final String COL_COMPLETION_INDEX = "completion_index";
		public static final String COL_ELAPSED_DURATION = "elapsed_duration";
		public static final String COL_ELAPSED_GAP = "elapsed_gap";
		public static final String COL_ELAPSED_LENGTH = "elapsed_length";
		public static final String COL_ROUTE_DURATION = "route_duration";
		public static final String COL_ROUTE_GAP = "route_gap";
		public static final String COL_ROUTE_LENGTH = "route_length";

		public static final String DATABASE_CREATE_SQL = String.format("CREATE TABLE %s " + "(%s INTEGER PRIMARY KEY AUTOINCREMENT," // ID
				+ " %s TEXT NOT NULL," // Route name
				+ " %s TEXT NOT NULL," // Path
				+ " %s INTEGER NOT NULL," // Date
				+ " %s REAL NOT NULL," // Completion Index
				+ " %s INTEGER NOT NULL," // Elapsed duration
				+ " %s INTEGER NOT NULL," // Elapsed gap
				+ " %s INTEGER NOT NULL," // Elapsed meters
				+ " %s INTEGER NOT NULL," // Route duration
				+ " %s INTEGER NOT NULL," // Route gap
				+ " %s INTEGER NOT NULL)", // Route meters

		DATABASE_TABLE, COL_KEY, COL_ROUTE_NAME, COL_PATH, COL_DATE, COL_COMPLETION_INDEX, COL_ELAPSED_DURATION, COL_ELAPSED_GAP, COL_ELAPSED_LENGTH, COL_ROUTE_DURATION, COL_ROUTE_GAP, COL_ROUTE_LENGTH);

		public static final String DATABASE_DROP_SQL = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

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

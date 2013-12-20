package it.polimi.dima.dacc.mountainroutes.persistence.report;

import java.util.Date;

import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionList;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;

import it.polimi.dima.dacc.mountainroutes.types.RouteID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReportPersistence {

	private static String[] allColumns = { DbHelper.COLUMN_ROUTE_ID, DbHelper.COLUMN_KEY,
			DbHelper.COLUMN_COMPLETION_INDEX, DbHelper.COLUMN_ELAPSED_SECONDS, DbHelper.COLUMN_DATE,
			DbHelper.COLUMN_GAP, DbHelper.COLUMN_LENGTH_IN_METERS };

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

		ContentValues values = new ContentValues();
		values.put(DbHelper.COLUMN_ROUTE_ID, excursion.getId().toString());
		values.put(DbHelper.COLUMN_COMPLETION_INDEX, excursion.getCompletionIndex());
		values.put(DbHelper.COLUMN_DATE, excursion.getDate().toString());
		values.put(DbHelper.COLUMN_ELAPSED_SECONDS, excursion.getElapsedSeconds());
		values.put(DbHelper.COLUMN_LENGTH_IN_METERS, excursion.getLengthInMeters());
		values.put(DbHelper.COLUMN_GAP, excursion.getGap());

		if (database.insert(DbHelper.DATABASE_TABLE, null, values) == -1) {
			throw new PersistenceException("could not persist route");
		}
	}

	public void removeExcursion(int excursionId) throws PersistenceException {

		String where = DbHelper.COLUMN_KEY + " = ?";
		String[] whereArgs = new String[] { String.valueOf(excursionId) };
		database.delete(DbHelper.DATABASE_TABLE, where, whereArgs);
	}

	private static ExcursionReport excursionReportFromCursor(Cursor cursor, Context context)
			throws PersistenceException {
		// Route ID
		int routeIdIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ROUTE_ID);
		RouteID routeId = new RouteID(cursor.getString(routeIdIndex));

		// Completion index
		int completionIndexIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_COMPLETION_INDEX);
		float completionIndex = cursor.getFloat(completionIndexIndex);

		// Elapsed seconds
		int elapsedIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ELAPSED_SECONDS);
		int elapsed = cursor.getInt(elapsedIndex);

		// Date
		int dateIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_DATE);
		Date date = new Date(cursor.getLong(dateIndex));

		// Gap
		int gapIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_GAP);
		int gap = cursor.getInt(gapIndex);

		// Length in meters
		int lengthIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LENGTH_IN_METERS);
		int length = cursor.getInt(lengthIndex);

		ExcursionReport excursionReport = new ExcursionReport(routeId, date);
		excursionReport.setCompletionIndex(completionIndex);
		excursionReport.setElapsedSeconds(elapsed);
		excursionReport.setGap(gap);
		excursionReport.setLengthInMeters(length);

		return excursionReport;
	}

	// Class to handle the database
	private static class DbHelper extends SQLiteOpenHelper {

		public static final String DATABASE_NAME = "reportdb";
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_TABLE = "reports";
		public static final String COLUMN_KEY = "_id";
		public static final String COLUMN_ROUTE_ID = "routeid";
		public static final String COLUMN_COMPLETION_INDEX = "completion_index";
		public static final String COLUMN_ELAPSED_SECONDS = "elapsed_seconds";
		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_GAP = "gap";
		public static final String COLUMN_LENGTH_IN_METERS = "length_in_meters";

		public static final String DATABASE_CREATE_SQL = String.format(
				"CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," + " %s TEXT NOT NULL," + // Report
																									// ID
						" %s REAL NOT NULL," + // Completion Index
						" %s INTEGER NOT NULL," + // Elapsed seconds
						" %s INTEGER NOT NULL," + // Date
						" %s INTEGER NOT NULL," + // Gap
						" %s INTEGER NOT NULL);", // Length in meters
				DATABASE_TABLE, COLUMN_KEY, COLUMN_ROUTE_ID, COLUMN_COMPLETION_INDEX, COLUMN_ELAPSED_SECONDS,
				COLUMN_DATE, COLUMN_GAP, COLUMN_LENGTH_IN_METERS);

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

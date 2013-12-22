package it.polimi.dima.dacc.mountainroutes.persistence.report;

import java.util.Date;

import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.route.PointListConverter;
import it.polimi.dima.dacc.mountainroutes.persistence.route.PointListConverter.ConverterException;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionList;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.PointList;

import it.polimi.dima.dacc.mountainroutes.types.RouteID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReportPersistence {

	private static String[] allColumns = { DbHelper.COLUMN_KEY, DbHelper.COLUMN_COMPLETION_INDEX,
			DbHelper.COLUMN_ELAPSED_SECONDS, DbHelper.COLUMN_DATE, DbHelper.COLUMN_GAP,
			DbHelper.COLUMN_LENGTH_IN_METERS, DbHelper.COLUMN_PATH, DbHelper.COLUMN_ORIGINAL_SECONDS,
			DbHelper.COLUMN_ORIGINAL_GAP, DbHelper.COLUMN_ORIGINAL_METERS };

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
		values.put(DbHelper.COLUMN_COMPLETION_INDEX, excursion.getCompletionIndex());
		values.put(DbHelper.COLUMN_DATE, excursion.getDate().toString());
		values.put(DbHelper.COLUMN_ELAPSED_SECONDS, excursion.getElapsedSeconds());
		values.put(DbHelper.COLUMN_LENGTH_IN_METERS, excursion.getLengthInMeters());
		values.put(DbHelper.COLUMN_GAP, excursion.getGap());
		values.put(DbHelper.COLUMN_PATH, excursion.getPath().toString());
		values.put(DbHelper.COLUMN_ORIGINAL_SECONDS, excursion.getOriginalSeconds());
		values.put(DbHelper.COLUMN_ORIGINAL_GAP, excursion.getOriginalGap());
		values.put(DbHelper.COLUMN_ORIGINAL_METERS, excursion.getOriginalMeters());

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
		// Key
		int keyIndex = cursor.getColumnIndex(DbHelper.COLUMN_KEY);
		int key = cursor.getInt(keyIndex);

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

		// Path
		int pathIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_PATH);
		try {
			PointList path = PointListConverter.fromString(cursor.getString(pathIndex));
		} catch (ConverterException e) {
			e.printStackTrace();
		}

		// Original seconds
		int originalSecondsIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ORIGINAL_SECONDS);
		int originalSeconds = cursor.getInt(originalSecondsIndex);

		// Original gap
		int originalGapIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ORIGINAL_GAP);
		int originalGap = cursor.getInt(originalGapIndex);

		// Original meters
		int originaMetersIndex = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ORIGINAL_METERS);
		int originalMeters = cursor.getInt(originaMetersIndex);

		ExcursionReport excursionReport = new ExcursionReport();
		excursionReport.setId(key);
		excursionReport.setCompletionIndex(completionIndex);
		excursionReport.setElapsedSeconds(elapsed);
		excursionReport.setGap(gap);
		excursionReport.setLengthInMeters(length);
		excursionReport.setPath(path);
		excursionReport.setOriginalSeconds(originalSeconds);
		excursionReport.setOriginalGap(originalGap);
		excursionReport.setOriginalMeters(originalMeters);

		return excursionReport;
	}

	// Class to handle the database
	private static class DbHelper extends SQLiteOpenHelper {

		public static final String DATABASE_NAME = "reportdb";
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_TABLE = "reports";
		public static final String COLUMN_KEY = "_id";
		public static final String COLUMN_COMPLETION_INDEX = "completion_index";
		public static final String COLUMN_ELAPSED_SECONDS = "elapsed_seconds";
		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_GAP = "gap";
		public static final String COLUMN_LENGTH_IN_METERS = "length_in_meters";
		public static final String COLUMN_PATH = "path";
		public static final String COLUMN_ORIGINAL_SECONDS = "original_seconds";
		public static final String COLUMN_ORIGINAL_GAP = "original_gap";
		public static final String COLUMN_ORIGINAL_METERS = "original_meters";

		public static final String DATABASE_CREATE_SQL = String.format(
				"CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," + " %s TEXT NOT NULL," + // Report
																									// ID
						" %s REAL NOT NULL," + // Completion Index
						" %s INTEGER NOT NULL," + // Elapsed seconds
						" %s INTEGER NOT NULL," + // Date
						" %s INTEGER NOT NULL," + // Gap
						" %s INTEGER NOT NULL," + // Length in meters
						" %s TEXT NOT NULL," + // Path
						" %s INTEGER NOT NULL" + // Original seconds
						" %s INTEGER NOT NULL" + // Original gap
						" %s INTEGER NOT NULL)", // Original meters
				DATABASE_TABLE, COLUMN_KEY, COLUMN_COMPLETION_INDEX, COLUMN_ELAPSED_SECONDS, COLUMN_DATE, COLUMN_GAP,
				COLUMN_LENGTH_IN_METERS, COLUMN_PATH, COLUMN_ORIGINAL_SECONDS, COLUMN_ORIGINAL_GAP,
				COLUMN_ORIGINAL_METERS);

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

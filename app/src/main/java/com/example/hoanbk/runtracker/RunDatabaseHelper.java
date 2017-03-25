package com.example.hoanbk.runtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Build;

import java.util.Date;

/**
 * Created by hoanbk on 3/25/2017.
 */

public class RunDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "runs.sqlite";
    private static final int DB_VERSION = 1;
    private static final String TABLE_RUN = "run";
    private static final String COLUMN_RUN_ID = "_id";
    private static final String COLUMN_RUN_START_DATE = "start_date";

    private static final String TABLE_LOCATION = "location";
    private static final String COLUMN_LOCATION_TIMESTAMP = "timestamp";
    private static final String COLUMN_LOCATION_LONGITUDE = "longitude";
    private static final String COLUMN_LOCATION_LATITUDE = "latitude";
    private static final String COLUMN_LOCATION_ALTITUDE = "altitude";
    private static final String COLUMN_LOCATION_PROVIDER = "provider";
    private static final String COLUMN_LOCATION_RUN_ID = "run_id";



    public RunDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the run table
        db.execSQL("create table " + TABLE_RUN
                + "(" + COLUMN_RUN_ID + " integer primary key autoincrement, "
                + COLUMN_RUN_START_DATE + " integer" + ")");
        // Create table location
        db.execSQL("create table " + TABLE_LOCATION
        + "(" + COLUMN_LOCATION_TIMESTAMP + " integer, "
        + COLUMN_LOCATION_LATITUDE + " real, "
        + COLUMN_LOCATION_LONGITUDE + " real, "
        + COLUMN_LOCATION_ALTITUDE + " real, "
        + COLUMN_LOCATION_PROVIDER + " varchar(100), "
        + COLUMN_LOCATION_RUN_ID + " integer references " + TABLE_RUN + "(" + COLUMN_RUN_ID + ")" +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertRun(Run run) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RUN_START_DATE, run.getStartDate().getTime());
        return getWritableDatabase().insert(TABLE_RUN, null, cv);
    }

    public long insertLocation(long runId, Location location) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOCATION_TIMESTAMP, location.getTime());
        cv.put(COLUMN_LOCATION_LONGITUDE, location.getLongitude());
        cv.put(COLUMN_LOCATION_LATITUDE, location.getLatitude());
        cv.put(COLUMN_LOCATION_ALTITUDE, location.getAltitude());
        cv.put(COLUMN_LOCATION_PROVIDER, location.getProvider());
        cv.put(COLUMN_LOCATION_RUN_ID, runId);

        return getWritableDatabase().insert(TABLE_LOCATION, null, cv);
    }

    public RunCursor queryRuns() {
        Cursor wrapper = getReadableDatabase().query(TABLE_RUN,
                null, null, null, null, null, COLUMN_RUN_START_DATE + " asc");
        return new RunCursor(wrapper);
    }

    public RunCursor queryrun(long id) {
        Cursor wrapper = getReadableDatabase().query(TABLE_RUN,
                null,
                COLUMN_RUN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                "1");
        return new RunCursor(wrapper);
    }

    public static class RunCursor extends CursorWrapper {

        public RunCursor(Cursor c) {
            super(c);
        }

        /**
         * Return a Run object configured for the current row, or null if the current row is invalid
         */
        public Run getRun() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }

            Run run = new Run();

            long runId = getLong(getColumnIndex(COLUMN_RUN_ID));
            run.setId(runId);
            long startDate = getLong(getColumnIndex(COLUMN_RUN_START_DATE));
            run.setStartDate(new Date(startDate));

            return run;
        }
    }
}

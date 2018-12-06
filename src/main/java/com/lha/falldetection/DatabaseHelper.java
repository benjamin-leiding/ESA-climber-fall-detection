package com.lha.falldetection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by lucahernandezacosta on 11.01.18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    static Date cDate = new Date();
    private static String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
    public static String DATABASE_NAME;
    public static final String TABLE_NAME = "fall_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "CALC_DISTANCE";
    public static final String COL_3 = "BAR_DISTANCE";
    public static final String COL_4 = "DURATION";
    public static final String COL_5 = "TIME";

    SQLiteDatabase db;

    String TAG = "DbHelper";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db  = this.getWritableDatabase();
    }

    public DatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, 1);
        DATABASE_NAME = dbName;
        db  = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " DOUBLE, " + COL_3 + " DOUBLE, " + COL_4 + " DOUBLE, " + COL_5 + " BIGINT); ");
        sqLiteDatabase.execSQL("create table " + "Altitude" + " (" + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "Timestamp" + " BIGINT, " + "Altitude_inMeter" + " DOUBLE); ");
        sqLiteDatabase.execSQL("create table " + "KalmanAltitude" + " (" + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "Timestamp" + " BIGINT, " + "Altitude_inMeter" + " DOUBLE); ");
        sqLiteDatabase.execSQL("create table " + "Acceleration" + " (" + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "Timestamp" + " BIGINT, " + "Acceleration_Means" + " DOUBLE); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(sqLiteDatabase);
    }

    public boolean insertData(Double calc_distance, Double bar_distance, Double duration, Long time) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, calc_distance);
        contentValues.put(COL_3, bar_distance);
        contentValues.put(COL_4, duration);
        contentValues.put(COL_5, time);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertData(String tableName, Long timestamp, double data) {

        ContentValues contentValues = new ContentValues();
        long result = 0;
        if (tableName.equals("Altitude")){
            contentValues.put("Timestamp", timestamp);
            contentValues.put("Altitude_inMeter", data);
            result = db.insert("Altitude", null, contentValues);

        } else if (tableName.equals("KalmanAltitude")) {
            contentValues.put("Timestamp", timestamp);
            contentValues.put("Altitude_inMeter", data);
            result = db.insert("KalmanAltitude", null, contentValues);

        } else if (tableName.equals("Acceleration")) {
            contentValues.put("Timestamp", timestamp);
            contentValues.put("Acceleration_Means", data);
            result = db.insert("Acceleration", null, contentValues);
        }

        //result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getTableAsString() {
        SQLiteDatabase database = db;
        String tableName = TABLE_NAME;
        Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }

    public float[] getFallDistanceFromDB() {
        SQLiteDatabase database = db;
        String tableName = TABLE_NAME;
        //Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);

        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        float calcDistance [] = new float[allRows.getCount()];
        int i = 0;
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    if(name.equals("CALC_DISTANCE")) {
                        calcDistance [i] = Float.parseFloat(allRows.getString(allRows.getColumnIndex(name)));//Double.parseDouble(allRows.getString(allRows.getColumnIndex(name)));
                        i++;
                        /**
                        tableString += String.format("%s: %s\n", name,
                                allRows.getString(allRows.getColumnIndex(name)));**/
                    }

                }
                /**tableString += "\n";**/

            } while (allRows.moveToNext());
        }

        return calcDistance;
    }



}

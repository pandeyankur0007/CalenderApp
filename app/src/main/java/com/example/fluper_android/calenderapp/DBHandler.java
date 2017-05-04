package com.example.fluper_android.calenderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fluper-android on 4/5/17.
 */

public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    public static DBHandler sInstance;
    // Database Name
    private static final String DATABASE_NAME = "demoDb";

    // Table name
    private static final String TABLE_NAME = "names";

    // Table Columns names
    private static final String KEY_EVENT_NAME = "eventName";
    private static final String KEY_EVENT_DAY = "eventDay";

    /**
     * @param context
     */
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static synchronized DBHandler getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_EVENT_NAME + " TEXT,"
                + KEY_EVENT_DAY + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public void addRecord(EventModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, model.getEvent());
        values.put(KEY_EVENT_DAY, model.getEventDay());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<EventModel> getAllRecord() {
        List<EventModel> nameVoList = new ArrayList<EventModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EventModel nameVO = new EventModel();
                nameVO.setEvent(cursor.getString(0));
                nameVO.setEventDay(cursor.getString(1));
                // Adding NameVO to list
                nameVoList.add(nameVO);
            } while (cursor.moveToNext());
        }
        // return NameVO list
        return nameVoList;
    }
}

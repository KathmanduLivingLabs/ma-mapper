package org.kll.app.meromapv1.Database;

/**
 * Created by Rahul Singh Maharjan on 11/27/16.
 * Project for Kathmandu Living Labs
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DetailDatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "poi.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_POI =  "poi";
    public static final String COLUMN_ID = "poiID";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DIS = "description";


    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_POI + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DIS + " TEXT, " +
                    ")";


    public DetailDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + TABLE_POI);
    }
}

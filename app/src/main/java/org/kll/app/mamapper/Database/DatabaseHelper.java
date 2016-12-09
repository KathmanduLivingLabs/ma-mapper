package org.kll.app.mamapper.Database;

/**
 * Created by Rahul Singh Maharjan on 11/27/16.
 * Project for Kathmandu Living Labs
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "OPENSTREETMAPS";

    // Table columns
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String DESC = "description";
    public static final String CONTACT = "contact";

    // Database Information
    static final String DB_NAME = "INFORMATION.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE =
            "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, "
            + DESC + " TEXT, " + CONTACT +" TEXT"+ " )";



    //+ CONTACT +" TEXT_TYPE

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}


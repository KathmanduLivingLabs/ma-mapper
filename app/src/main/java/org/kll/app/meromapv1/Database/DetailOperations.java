package org.kll.app.meromapv1.Database;

/**
 * Created by Rahul Singh Maharjan on 11/27/16.
 * Project for Kathmandu Living Labs
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.kll.app.meromapv1.Model.Information;


public class DetailOperations {

    public static final String LOGTAG = "POI_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;


    private static final String[] allColumn = {
            DetailDatabaseHandler.COLUMN_ID,
            DetailDatabaseHandler.COLUMN_NAME,
            DetailDatabaseHandler.COLUMN_DIS
    };

    public DetailOperations(Context context)
    {
        dbhandler = new DetailDatabaseHandler(context);
    }

    public void open(){
        Log.i(LOGTAG,"Database Opened");
        database = dbhandler.getWritableDatabase();


    }
    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();

    }

    public Information addInformation(Information information)
    {
        ContentValues values = new ContentValues();
        values.put(DetailDatabaseHandler.COLUMN_NAME, information.getInfoName());
        values.put(DetailDatabaseHandler.COLUMN_DIS, information.getInfoDescription());
        long insertid = database.insert(DetailDatabaseHandler.TABLE_POI, null, values);
        information.setInfoID(insertid);
        return information;
    }



    //getting single POI

    public Information getInformation(long id)
    {
        Cursor cursor = database.query(DetailDatabaseHandler.TABLE_POI,allColumn,DetailDatabaseHandler.COLUMN_ID + "=?",new String[]{String.valueOf(id)},null,null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Information i = new Information(Long.parseLong(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
        // return Information
        return i;
    }

    public List<Information> getAllInformation() {

        Cursor cursor = database.query(DetailDatabaseHandler.TABLE_POI,allColumn,null,null,null, null, null);

        List<Information> informations = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Information information = new Information();
                information.setInfoID(cursor.getLong(cursor.getColumnIndex(DetailDatabaseHandler.COLUMN_ID)));
                information.setInfoName(cursor.getString(cursor.getColumnIndex(DetailDatabaseHandler.COLUMN_NAME)));
                information.setInfoDescription(cursor.getString(cursor.getColumnIndex(DetailDatabaseHandler.COLUMN_DIS)));
                informations.add(information);
            }
        }
        // return All information
        return informations;
    }

    // Updating Information
    public int updateInformation(Information information) {

        ContentValues values = new ContentValues();
        values.put(DetailDatabaseHandler.COLUMN_NAME, information.getInfoName());
        values.put(DetailDatabaseHandler.COLUMN_DIS, information.getInfoDescription());
        // updating row
        return database.update(DetailDatabaseHandler.TABLE_POI, values,
                DetailDatabaseHandler.COLUMN_ID + "=?",new String[] { String.valueOf(information.getInfoID())});
    }

    // Deleting Information
    public void removeInformation(Information information) {

        database.delete(DetailDatabaseHandler.TABLE_POI, DetailDatabaseHandler.COLUMN_ID + "=" + information.getInfoID(), null);
    }

}

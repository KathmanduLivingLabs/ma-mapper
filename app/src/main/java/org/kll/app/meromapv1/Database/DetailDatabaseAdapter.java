package org.kll.app.meromapv1.Database;/*
package org.kll.app.meromapv1.Database;

*/
/**
 * Created by Rahul Singh Maharjan on 11/25/16.
 * Project for Kathmandu Living Labs
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.kll.app.meromapv1.Database.DatabaseHelper;

public class DetailDatabaseAdapter {

    static final String DATABASE_NAME = "detail.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    private final Context context;
    private DatabaseHelper dbHelper;
    //TODO:Create public field for each column in
    //SQL statement to create a new database.

    static final String DATABASE_CREATE = "create table" + "detail" +
            "( " + "ID" + " integer primary key autoincrement, " + "Name text, Detail text);";
    //Variable to hold the database instance
    public SQLiteDatabase db;

    public DetailDatabaseAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context, DATABASE_NAME,null, DATABASE_VERSION);

    }

    public DetailDatabaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        db.close();

    }

    public SQLiteDatabase getDatabaseInstance()
    {
        return db;

    }

    public void insertEntry(String Name, String Detail)
    {
        ContentValues newValues = new ContentValues();
        //Assign values for each row.
        newValues.put("Name", Name);
        newValues.put("Detail",Detail);

        //Insert the row into your table
        db.insert("DETAIL", null, newValues);

    }

    public int deleteEntry(String Name)
    {
        //String id - String.valueOf(ID);
        String where="NAME=?";
        int numberOFEntriesDeleted = db.delete("DETAIL", where, new String[]{Name});
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }
/*public String getStringEntry(String Name)
    {
        Cursor cursor = db.query("DETAIL", null, "NAME=?", new String[]{Name}, null, null, null);
        if(cursor.getCount() < 1) //Name Not Exit
        {
            cursor.close();
            return "NOT EXIT";

        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("Detail"))
    }
*//*


    public void updateEntry(String Name, String Detail)
    {
        //Define the updated row content
        ContentValues updatedValues = new ContentValues();
        //Assign value for each row
        updatedValues.put("NAME", Name);
        updatedValues.put("Detail", Detail);

        String where = "NAME = ?";
        db.update("DERAIL", updatedValues, where, new String[]{Name});
    }
    //Context of the application using the database


    //database open/upgarde helper


*/

}


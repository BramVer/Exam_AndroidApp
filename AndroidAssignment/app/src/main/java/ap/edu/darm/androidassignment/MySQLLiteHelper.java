package ap.edu.darm.androidassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by darm on 29/09/16.
 */

public class MySQLLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "lib.db";
    private static final String TBL_LIBS = "libraries";
    private static final int DB_VERSION = 5;

    public MySQLLiteHelper(Context c) { super(c, DB_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Android wants _id as the primary key
        String CREATE_LIB_TBL = "CREATE TABLE " + TBL_LIBS  + "(_id INTEGER PRIMARY KEY, name TEXT, lattitude DOUBLE, longitude DOUBLE)";
        db.execSQL(CREATE_LIB_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_LIBS);
        onCreate(db);
    }

    public void addLib(String name, Double lat, Double lon) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("name", name);
        v.put("lattitude", lat);
        v.put("longitude", lon);

        db.insert(TBL_LIBS, null, v);
    }

    public Double[] getLib(String name) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TBL_LIBS,                                   //table name
                new String[] { "_id", "name", "lattitude", "longitude" },       //columns to return
                "name=?",                                        //where part of query
                new String[] { String.valueOf(name) },            //fills in the '?' part of where
                null, null, null);                        //groupBy, having, orderBy

        c.moveToFirst();
        return new Double[] { c.getDouble(2), c.getDouble(3) };           //Return fname && lname, NOT _id (which is at 0)
    }

    public boolean checkLib(String name) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TBL_LIBS,                                   //table name
                new String[] { "_id", "name", "lattitude", "longitude" },       //columns to return
                "name=?",                                        //where part of query
                new String[] { String.valueOf(name) },            //fills in the '?' part of where
                null, null, null);                        //groupBy, having, orderBy

        return (c != null);
    }

    public ArrayList<Double[]> getAllLibs() throws Exception {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_LIBS, null);

        ArrayList<Double[]> results = new ArrayList<Double[]>();

        if(c.moveToFirst()) {
            do {
                results.add( new Double[] { c.getDouble(2) , c.getDouble(3) } );
            } while(c.moveToNext());
        }
        
        return results;
    }

    public boolean checkEmpty() throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT COUNT(*) FROM " + TBL_LIBS;

        Cursor c = db.rawQuery(q, null);

        c.moveToFirst();
        return (c.getInt(0) < 0);
    }
}


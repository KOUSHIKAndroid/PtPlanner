package com.ptplanner.K_DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by su on 7/11/17.
 */

public class Database extends SQLiteOpenHelper {
    //version number to upgrade database version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Ptp.db";

    LocalDataResponse Localdataresponse;

    //Table Query
    private static String TABLECalender="CREATE TABLE CALENDERDATA (Date TEXT PRIMARY KEY, data TEXT)";
    private static String TABLEDIARY="CREATE TABLE TABLEDIARY (Date TEXT PRIMARY KEY, data TEXT)";
    private static String TABLEDIATE="CREATE TABLE TABLEDIATE (Date TEXT PRIMARY KEY, data TEXT)";
    private static String TABLEProgramID="CREATE TABLE TABLEProgramID (ID TEXT PRIMARY KEY, data TEXT)";
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLECalender);
        sqLiteDatabase.execSQL(TABLEDIARY);
        sqLiteDatabase.execSQL(TABLEDIATE);
        sqLiteDatabase.execSQL(TABLEProgramID);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }




    /*
    @ Koushik Local Data Seta
     */

    public void SetCalenderPageData(String Date,String DATA, LocalDataResponse localdataresponse){
        this.Localdataresponse=localdataresponse;

        try {
            SQLiteDatabase dbb = getWritableDatabase();
            String countQuery = "SELECT  * FROM CALENDERDATA WHERE Date='" + Date + "'";
            Cursor cursor = dbb.rawQuery(countQuery, null);
            if (cursor.getCount() >= 1 && cursor.moveToFirst()) {

                ContentValues values = new ContentValues();
                values.put("data", DATA);
                dbb.update("CALENDERDATA", values, "Date='" + Date + "'", null);
                dbb.close();
                Localdataresponse.OnSuccess("UPDATE");

            } else {
                ContentValues values = new ContentValues();
                values.put("Date", Date);
                values.put("data", DATA + "");
                dbb.insert("CALENDERDATA", null, values);
                dbb.close(); // Closing data
                Localdataresponse.OnSuccess("INSERT");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Localdataresponse.OnNotfound(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Localdataresponse.OnNotfound(e.getMessage());
        }
    }


    public void SetDirayPageData(String Date,String DATA, LocalDataResponse localdataresponse){
        this.Localdataresponse=localdataresponse;

        try {
            SQLiteDatabase dbb = getWritableDatabase();
            String countQuery = "SELECT  * FROM TABLEDIARY WHERE Date='" + Date + "'";
            Cursor cursor = dbb.rawQuery(countQuery, null);
            if (cursor.getCount() >= 1 && cursor.moveToFirst()) {

                ContentValues values = new ContentValues();
                values.put("data", DATA);
                dbb.update("TABLEDIARY", values, "Date='" + Date + "'", null);
                dbb.close();
                Localdataresponse.OnSuccess("UPDATE");

            } else {
                ContentValues values = new ContentValues();
                values.put("Date", Date);
                values.put("data", DATA + "");
                dbb.insert("TABLEDIARY", null, values);
                dbb.close(); // Closing data
                Localdataresponse.OnSuccess("INSERT");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Localdataresponse.OnNotfound(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Localdataresponse.OnNotfound(e.getMessage());
        }
    }


    public void SetProgramData(String ID,String DATA, LocalDataResponse localdataresponse){
        this.Localdataresponse=localdataresponse;

        try {
            SQLiteDatabase dbb = getWritableDatabase();
            String countQuery = "SELECT  * FROM TABLEProgramID WHERE ID='" + ID + "'";
            Cursor cursor = dbb.rawQuery(countQuery, null);
            if (cursor.getCount() >= 1 && cursor.moveToFirst()) {

                ContentValues values = new ContentValues();
                values.put("data", DATA);
                dbb.update("TABLEProgramID", values, "ID='" + ID + "'", null);
                dbb.close();
                Localdataresponse.OnSuccess("UPDATE");

            } else {
                ContentValues values = new ContentValues();
                values.put("ID", ID);
                values.put("data", DATA + "");
                dbb.insert("TABLEProgramID", null, values);
                dbb.close(); // Closing data
                Localdataresponse.OnSuccess("INSERT");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Localdataresponse.OnNotfound(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Localdataresponse.OnNotfound(e.getMessage());
        }
    }
    public void GET_Caleder_Frag_Fetails(String Date, LocalDataResponse response)
    {
        this.Localdataresponse=response;
        String JSONOBJECT = "";
        SQLiteDatabase db = getWritableDatabase();
        String countQuery = "SELECT  * FROM CALENDERDATA WHERE Date='" + Date + "'";
        Cursor cursor2 = db.rawQuery(countQuery, null);
        if (cursor2.moveToFirst()) {
            do {
                try {
                    JSONOBJECT = cursor2.getString(cursor2.getColumnIndex("data"));
                    Localdataresponse.OnSuccess(JSONOBJECT);
                } catch (Exception e) {
                    Localdataresponse.OnNotfound(e.getMessage());
                    e.printStackTrace();
                }
            } while (cursor2.moveToNext());
        }else {
            Localdataresponse.OnNotfound("Nodata");
        }
        db.close();

    }


    public void GET_ProgramDetails(String ID, LocalDataResponse response)
    {
        this.Localdataresponse=response;
        String JSONOBJECT = "";
        SQLiteDatabase db = getWritableDatabase();
        String countQuery = "SELECT  * FROM TABLEProgramID WHERE ID='" + ID + "'";
        Cursor cursor2 = db.rawQuery(countQuery, null);
        if (cursor2.moveToFirst()) {
            do {
                try {
                    JSONOBJECT = cursor2.getString(cursor2.getColumnIndex("data"));
                    Localdataresponse.OnSuccess(JSONOBJECT);
                } catch (Exception e) {
                    Localdataresponse.OnNotfound(e.getMessage());
                    e.printStackTrace();
                }
            } while (cursor2.moveToNext());
        }else {
            Localdataresponse.OnNotfound("Nodata");
        }
        db.close();

    }
    public void GET_Diary_Frag_Fetails(String Date, LocalDataResponse response)
    {
        this.Localdataresponse=response;
        String JSONOBJECT = "";
        SQLiteDatabase db = getWritableDatabase();
        String countQuery = "SELECT  * FROM TABLEDIARY WHERE Date='" + Date + "'";
        Cursor cursor2 = db.rawQuery(countQuery, null);
        if (cursor2.moveToFirst()) {
            do {
                try {
                    JSONOBJECT = cursor2.getString(cursor2.getColumnIndex("data"));
                    Localdataresponse.OnSuccess(JSONOBJECT);
                } catch (Exception e) {
                    Localdataresponse.OnNotfound(e.getMessage());
                    e.printStackTrace();
                }
            } while (cursor2.moveToNext());
        }else {
            Localdataresponse.OnNotfound("NotFound");
        }
        db.close();

    }

    public void SetDiate_PageData(String date, String result, LocalDataResponse response) {
        this.Localdataresponse=response;

        try {
            SQLiteDatabase dbb = getWritableDatabase();
            String countQuery = "SELECT  * FROM TABLEDIATE WHERE Date='" + date + "'";
            Cursor cursor = dbb.rawQuery(countQuery, null);
            if (cursor.getCount() >= 1 && cursor.moveToFirst()) {

                ContentValues values = new ContentValues();
                values.put("data", result);
                dbb.update("TABLEDIATE", values, "Date='" + date + "'", null);
                dbb.close();
                Localdataresponse.OnSuccess("UPDATE");

            } else {
                ContentValues values = new ContentValues();
                values.put("Date", date);
                values.put("data", result + "");
                dbb.insert("TABLEDIATE", null, values);
                dbb.close(); // Closing data
                Localdataresponse.OnSuccess("INSERT");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Localdataresponse.OnNotfound(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Localdataresponse.OnNotfound(e.getMessage());
        }
    }

    public void GET_DietData(String dateChange, LocalDataResponse response) {

        this.Localdataresponse=response;
        String JSONOBJECT = "";
        SQLiteDatabase db = getWritableDatabase();
        String countQuery = "SELECT  * FROM TABLEDIATE WHERE Date='" + dateChange + "'";
        Cursor cursor2 = db.rawQuery(countQuery, null);
        if (cursor2.moveToFirst()) {
            do {
                try {
                    JSONOBJECT = cursor2.getString(cursor2.getColumnIndex("data"));
                    Localdataresponse.OnSuccess(JSONOBJECT);
                } catch (Exception e) {
                    Localdataresponse.OnNotfound(e.getMessage());
                    e.printStackTrace();
                }
            } while (cursor2.moveToNext());
        }else {
            Localdataresponse.OnNotfound("NotFound");
        }
        db.close();
    }
}

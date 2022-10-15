package com.example.lunchboxliste;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "tab_inventar";
    private static final String TABLE_NAME2 = "tab_gerichte";
    private static final String DATE_COL = "date";
    private static final String LUNCH_COL = "lunch";
    private static final String AMOUNT_COL = "name";
    private static final String FACH_COL = "fach";
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_COL + " TEXT, "
                + LUNCH_COL + " TEXT, "
                + AMOUNT_COL + " INTEGER, "
                + FACH_COL + " INTEGER)";
        db.execSQL(query);
        query = "CREATE TABLE " + TABLE_NAME2 + " ("
                + LUNCH_COL + " TEXT PRIMARY KEY)";
        db.execSQL(query);
    }

    public void add_lunchbox(String date, String lunch, String amount, String fach) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE_COL, date);
        values.put(LUNCH_COL, lunch);
        values.put(AMOUNT_COL, amount);
        values.put(FACH_COL, fach);
        db.insert(TABLE_NAME, null, values);
        values = new ContentValues();
        values.put(LUNCH_COL, lunch);
        db.insert(TABLE_NAME2, null, values);
        db.close();
    }

    public int delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_NAME, "ID ='"+id+"'",null);
        db.close();
        return i;
    }

    public void updateAmount(int id, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET "
                + AMOUNT_COL + " = " + amount + " WHERE ID = " + id + ";";
        db.execSQL(query);
    }

    public ArrayList<String> readLunchbox() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY date("+DATE_COL+")", null);
        ArrayList<String> LunchboxArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                LunchboxArrayList.add(cursor.getString(1)  + ": " + cursor.getString(2) + "\nMenge: " + cursor.getString(3) + "   Fach: " + cursor.getString(4) + "   ID: " + cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return LunchboxArrayList;
    }

    public ArrayList<String> readGerichte() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2, null);
        ArrayList<String> GerichteArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                GerichteArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return GerichteArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

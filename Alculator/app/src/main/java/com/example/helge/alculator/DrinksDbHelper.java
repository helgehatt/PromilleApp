package com.example.helge.alculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DrinksDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Drinks.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + DrinksContract.DrinkEntry.TABLE_NAME + " (" +
                DrinksContract.DrinkEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                DrinksContract.DrinkEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                DrinksContract.DrinkEntry.COLUMN_PERCENTAGE + TEXT_TYPE + COMMA_SEP +
                DrinksContract.DrinkEntry.COLUMN_VOLUME + TEXT_TYPE + COMMA_SEP +
                DrinksContract.DrinkEntry.COLUMN_CALORIES + TEXT_TYPE + COMMA_SEP +
                DrinksContract.DrinkEntry.COLUMN_IMAGE + TEXT_TYPE + COMMA_SEP +
                DrinksContract.DrinkEntry.COLUMN_LAST_USE + TEXT_TYPE + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DrinksContract.DrinkEntry.TABLE_NAME;

    public DrinksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}

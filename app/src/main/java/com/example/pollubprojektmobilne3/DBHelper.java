package com.example.pollubprojektmobilne3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public final static int DB_VERSION = 1;
    public final static String ID = "_id";
    public final static String DB_NAME = "smartphones.db";
    public final static String TABLE_NAME = "devices";
    public final static String BRAND = "brand";
    public final static String MODEL = "model";
    public final static String VERSION = "version";
    public final static String WWW = "www";
    public final static String DB_CREATE =
            "CREATE TABLE " + TABLE_NAME +
                    "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + BRAND + " TEXT NOT NULL, "
                    + MODEL + " TEXT NOT NULL, "
                    + VERSION + " TEXT, "
                    + WWW + " TEXT);";
    private static final String DB_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DB_DELETE);
        onCreate(db);
    }
}

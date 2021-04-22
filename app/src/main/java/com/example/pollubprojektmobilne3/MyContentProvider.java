package com.example.pollubprojektmobilne3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {

    private DBHelper dbHelper;

    //supplier ID
    private static final String IDENTIFIER = "com.example.pollubprojektmobilne3";

    //constant - so that you do not have to enter the text yourself
    //public static final Uri URI_CONTENT = Uri.parse("content://" + IDENTIFIER + "/" + DBHelper.TABLE_NAME);

    //constants to identify the type of URI recognized
    private static final int WHOLE_TABLE = 1;
    private static final int SELECTED_ROW = 2;

    //UriMatcher with empty URI tree root (NO_MATCH)
    private static final UriMatcher uriMatch = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //adding recognized URIs
        uriMatch.addURI(IDENTIFIER, DBHelper.TABLE_NAME, WHOLE_TABLE);
        uriMatch.addURI(IDENTIFIER, DBHelper.TABLE_NAME + "/#", SELECTED_ROW);
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    //TODO: Is method really @Nullable ?
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = uriMatch.match(uri);
        long addedID;

        //equivalent to C# using - automatic close() method call after end of scope
        //(?)Suppressed Exceptions - https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            if (uriType == WHOLE_TABLE) {
                addedID = db.insert(DBHelper.TABLE_NAME,
                        null, //nullColumnHack
                        values);
            } else {
                throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
        //notification of data change
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(DBHelper.TABLE_NAME + "/" + addedID);
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int uriType = uriMatch.match(uri);
        Cursor cursor;

        //try-with-resources - equivalent to C# using - automatic close() method call after end of scope
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            switch (uriType) {
                case WHOLE_TABLE:
                case SELECTED_ROW:
                    cursor = db.query(true, //distinct
                            DBHelper.TABLE_NAME,
                            projection, //columns
                            selection, //WHERE
                            selectionArgs, //whereArgs
                            null, //GROUP BY
                            null, //HAVING
                            sortOrder, //ORDER BY
                            null); //limit
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
        //The URI can be monitored for data changes - here it is logged.
        //Observer (which needs to be registered will be notified of the change of data)
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = uriMatch.match(uri);
        int deletedRows;

        //try-with-resources - equivalent to C# using - automatic close() method call after end of scope
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            switch (uriType) {
                case WHOLE_TABLE:
                case SELECTED_ROW:
                    deletedRows = db.delete(DBHelper.TABLE_NAME,
                            selection, //WHERE
                            selectionArgs); //whereArgs
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
        //notification of data change
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = uriMatch.match(uri);
        int updatedRows;

        //try-with-resources - equivalent to C# using - automatic close() method call after end of scope
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            switch (uriType) {
                case WHOLE_TABLE:
                case SELECTED_ROW:
                    updatedRows = db.update(DBHelper.TABLE_NAME,
                            values,
                            selection, //WHERE
                            selectionArgs); //whereArgs
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
        //notification of data change
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

}

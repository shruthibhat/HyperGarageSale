package com.example.shruthi.hypergaragesale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shruthi on 10/17/16.
 */

public class PostsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Posts.db";

    //COLUMN_NAME_IMAGE and COLUMN_NAME_DESCRIPTION was added so that it could be displayed in the PostDetails page and to display
    //image in the homescreen
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Posts.PostEntry.TABLE_NAME + " (" +
                    Posts.PostEntry._ID + " INTEGER PRIMARY KEY," +
                    Posts.PostEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    Posts.PostEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    Posts.PostEntry.COLUMN_NAME_PRICE + TEXT_TYPE +COMMA_SEP+
                    Posts.PostEntry.COLUMN_NAME_IMAGE+TEXT_TYPE+COMMA_SEP +
                    Posts.PostEntry.COLUMN_NAME_LATITUDE+TEXT_TYPE+COMMA_SEP +
                    Posts.PostEntry.COLUMN_NAME_LONGITUDE+TEXT_TYPE+COMMA_SEP +
                    Posts.PostEntry.COLUMN_NAME_LOCATION+TEXT_TYPE+
                    " )";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Posts.PostEntry.TABLE_NAME;

    public PostsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
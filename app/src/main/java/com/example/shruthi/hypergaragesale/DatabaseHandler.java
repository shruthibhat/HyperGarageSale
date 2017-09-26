package com.example.shruthi.hypergaragesale;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by shruthi on 10/17/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "itemsManager";

    // Contacts table name
    private static final String TABLE_ITEMS = "items";

    // Contacts Table Columns names
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_ITEM_DESCRIPTION = "item_description";
    private static final String KEY_ITEM_IMAGE="image";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    String CREATE_ITEMS_TABLE;
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_ITEM_NAME + " TEXT PRIMARY KEY," + KEY_PRICE + " DOUBLE,"
                + KEY_ITEM_DESCRIPTION + " TEXT" + KEY_ITEM_IMAGE + "TEXT" + ")";
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_ITEMS_TABLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addItems(Items item) {
        SQLiteDatabase db = this.getWritableDatabase();
//KEY_ITEM_DESCRIPTION and KEY_ITEM_IMAGE was added so that it could be displayed in the PostDetails page
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, item.get_item_name()); // Item Name
        values.put(KEY_PRICE, item.get_price()); // Item Price
        values.put(KEY_ITEM_DESCRIPTION,item.get_desc());
        values.put(KEY_ITEM_IMAGE,item.get_imagePath());

        // Inserting Row
        db.insert(TABLE_ITEMS, null, values);
        db.close(); // Closing database connection
    }


}

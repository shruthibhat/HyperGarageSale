package com.example.shruthi.hypergaragesale;

import android.provider.BaseColumns;

/**
 * Created by shruthi on 10/17/16.
 */

public class Posts {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public Posts() {}

    /* Inner class that defines the table contents */
    public static abstract class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "posts";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "decription";
        public static final String COLUMN_NAME_PRICE = "price";
        //added image to support the display of image in the home screen of the app
        public static final String COLUMN_NAME_IMAGE="image";
        public static final String COLUMN_NAME_LATITUDE="latitude";
        public static final String COLUMN_NAME_LONGITUDE="longitude";
        public static final String COLUMN_NAME_LOCATION="location";

    }
}
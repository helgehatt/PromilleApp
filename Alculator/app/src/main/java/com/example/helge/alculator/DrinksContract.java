package com.example.helge.alculator;

import android.provider.BaseColumns;

public final class DrinksContract {

    public DrinksContract() {}

    /* Inner class that defines the table contents */
    public static abstract class DrinkEntry implements BaseColumns {
        public static final String TABLE_NAME = "drinks";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PERCENTAGE = "percentage";
        public static final String COLUMN_VOLUME = "volume";
        public static final String COLUMN_CALORIES = "calories";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_LAST_USE = "lastUse";
    }
}

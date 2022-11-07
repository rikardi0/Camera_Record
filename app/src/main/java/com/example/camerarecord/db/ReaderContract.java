package com.example.camerarecord.db;

import android.provider.BaseColumns;

public class ReaderContract {
    private ReaderContract() {
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "IMAGES";
        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_STORAGE = "storage";

    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ReaderContract.FeedEntry.TABLE_NAME + " (" +
                    ReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    ReaderContract.FeedEntry.COLUMN_PATH + " BLOB," +
                    FeedEntry.COLUMN_STORAGE + " TEXT," +
                    ReaderContract.FeedEntry.COLUMN_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ReaderContract.FeedEntry.TABLE_NAME;

}

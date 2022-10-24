package com.example.camerarecord.db;

import android.provider.BaseColumns;

public class ReaderContract {
    private ReaderContract() {
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "IMAGES";
        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_DATE = "date";

    }
}

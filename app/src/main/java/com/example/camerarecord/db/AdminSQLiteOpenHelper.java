package com.example.camerarecord.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.camerarecord.model.ImageInfo;

import java.util.List;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CameraRecord.db";
    private static AdminSQLiteOpenHelper instance;


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ReaderContract.FeedEntry.TABLE_NAME + " (" +
                    ReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    ReaderContract.FeedEntry.COLUMN_PATH + " BLOB," +
                    ReaderContract.FeedEntry.COLUMN_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ReaderContract.FeedEntry.TABLE_NAME;

    public AdminSQLiteOpenHelper(@Nullable Context context) {
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

    public long insertData(byte[] image) {
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
        ContentValues values = new ContentValues();
        values.put(ReaderContract.FeedEntry.COLUMN_PATH, image);
        id = db.insert(ReaderContract.FeedEntry.TABLE_NAME, null, values);
              return id;
    }

    public void readData(List<ImageInfo> info) {
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ReaderContract.FeedEntry.COLUMN_PATH,
                ReaderContract.FeedEntry.COLUMN_DATE
        };

        Cursor cursor = db.query(
                ReaderContract.FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            long imageId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(ReaderContract.FeedEntry._ID));
            byte[] imagePath = cursor.getBlob(
                    cursor.getColumnIndexOrThrow(ReaderContract.FeedEntry.COLUMN_PATH));
            String imageDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(ReaderContract.FeedEntry.COLUMN_DATE));

                info.add(new ImageInfo(String.valueOf(imageId), imageDate, imagePath));

        }
        cursor.close();
    }

    public void deleteData( String imgId){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ReaderContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = { imgId };
        db.delete(ReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);

    }

}

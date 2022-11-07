package com.example.camerarecord.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.camerarecord.model.ImageInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.Nullable;

import static com.example.camerarecord.db.ReaderContract.SQL_CREATE_ENTRIES;
import static com.example.camerarecord.db.ReaderContract.SQL_DELETE_ENTRIES;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    Context context;
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "CameraRecord.db";
    private static AdminSQLiteOpenHelper instance;


    public AdminSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NotNull SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long insertData(byte[] image, String storage) {
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context);
        long id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReaderContract.FeedEntry.COLUMN_PATH, image);
        values.put(ReaderContract.FeedEntry.COLUMN_STORAGE, storage);
        id = db.insert(ReaderContract.FeedEntry.TABLE_NAME, null, values);
        return id;
    }

    public void readData(List<ImageInfo> info) {
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ReaderContract.FeedEntry.COLUMN_PATH,
                ReaderContract.FeedEntry.COLUMN_STORAGE,
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
            String storage = cursor.getString(
                    cursor.getColumnIndexOrThrow(ReaderContract.FeedEntry.COLUMN_STORAGE));

            info.add(new ImageInfo(String.valueOf(imageId), imageDate, imagePath, storage));
        }
        cursor.close();
    }

    public void deleteData(String imgId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ReaderContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {imgId};
        db.delete(ReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);

    }

}

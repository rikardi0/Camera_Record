package com.example.camerarecord;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.camerarecord.adapter.ImageAdapter;
import com.example.camerarecord.db.AdminSQLiteOpenHelper;
import com.example.camerarecord.model.ImageInfo;
import com.example.camerarecord.set_get.ImageState;
import com.example.camerarecord.utils.Spacer;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ImageInfo> infoList;
    ImageState imageState;
    AdminSQLiteOpenHelper Db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoList = new ArrayList<>();
        Db = new AdminSQLiteOpenHelper(this);
        Db.readData(infoList);
        ImageAdapter adapter = new ImageAdapter(infoList, this);

        recyclerView = findViewById(R.id.recycler_image_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new Spacer(0, 20));

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acciones, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.first_image);
        // ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        // bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        // byte[] img = byteArray.toByteArray();
        // Db.insertData(img);
        return true;

    }

    public void readDb() {


        // String[] projection = {
        //         BaseColumns._ID,
        //         ReaderContract.FeedEntry.COLUMN_PATH,
        //         ReaderContract.FeedEntry.COLUMN_DATE
        // };

        // Cursor cursor = db.query(
        //         ReaderContract.FeedEntry.TABLE_NAME,
        //         projection,
        //         null,
        //         null,
        //         null,
        //         null,
        //         null
        // );
        // while (cursor.moveToNext()) {
        //     long imageId = cursor.getLong(
        //             cursor.getColumnIndexOrThrow(ReaderContract.FeedEntry._ID));
        //     String imagePath = cursor.getString(
        //             cursor.getColumnIndexOrThrow(ReaderContract.FeedEntry.COLUMN_PATH));
        //     String imageDate = cursor.getString(
        //             cursor.getColumnIndexOrThrow(ReaderContract.FeedEntry.COLUMN_DATE));
        //     infoList.add(new ImageInfo(String.valueOf(imageId), imageDate, imagePath));

        // }
        // cursor.close();

    }

}

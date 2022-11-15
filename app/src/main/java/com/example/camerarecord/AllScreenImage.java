package com.example.camerarecord;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.camerarecord.db.AdminSQLiteOpenHelper;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;

import androidx.appcompat.app.AppCompatActivity;

public class AllScreenImage extends AppCompatActivity {
    TextView date, storage, size, resolution;
    ImageView imageView;
    FirebaseStorage storageDb;
    StorageReference storageRef;
    AdminSQLiteOpenHelper Db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_screen_image);
        date = findViewById(R.id.image_date);
        storage = findViewById(R.id.storage_path);
        size = findViewById(R.id.image_size);
        resolution = findViewById(R.id.image_resolution);
        imageView = findViewById(R.id.image_detail);

        Db = new AdminSQLiteOpenHelper(this);
        Intent intentImage = getIntent();
        Bundle bundle = intentImage.getExtras();

        byte[] imageBundle = bundle.getByteArray("image");
        String dateBundle = bundle.getString("fecha");
        String storageBundle = bundle.getString("storage");
        String sizeBundle = bundle.get("size").toString();
        String heightBundle = bundle.get("height").toString();
        String widthBundle = bundle.get("width").toString();

        ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBundle);
        Bitmap imageBit = BitmapFactory.decodeStream(imageStream);

        getSupportActionBar().hide();

        imageView.setImageBitmap(imageBit);
        date.setText(dateBundle);
        storage.setText(storageBundle);

        size.setText(sizeBundle.concat("kb"));
        resolution.setText(heightBundle.concat("x ").concat(widthBundle).concat(" pixel"));

        storageDb = FirebaseStorage.getInstance();
        storageRef = storageDb.getReference();
        Intent intentDetail = new Intent(this, DetailForImage.class);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentDetail.putExtra("image", imageBundle);
                startActivity(intentDetail);

            }
        });

    }
}


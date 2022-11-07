package com.example.camerarecord;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

import androidx.appcompat.app.AppCompatActivity;

public class AllScreenImage extends AppCompatActivity {
    TextView date, storage, size, resolution;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_screen_image);
        date = findViewById(R.id.image_date);
        storage = findViewById(R.id.storage_path);
        size = findViewById(R.id.image_size);
        resolution = findViewById(R.id.image_resolution);
        imageView = findViewById(R.id.image_detail);

        Intent intentImage = getIntent();
        Bundle bundle = intentImage.getExtras();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(bundle.getByteArray("image"));
        Bitmap imageBit = BitmapFactory.decodeStream(imageStream);

        getSupportActionBar().hide();

        imageView.setImageBitmap(imageBit);
        date.setText(bundle.getString("fecha"));
        storage.setText(bundle.getString("storage"));

        size.setText(bundle.get("size").toString() + "kB");
        resolution.setText(bundle.get("height").toString() + "x" + bundle.get("width") + " pixel");
    }
}


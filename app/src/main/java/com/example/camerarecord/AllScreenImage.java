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
    TextView fecha, size;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_screen_image);
        Intent intentImage = getIntent();
        Bundle bundle = intentImage.getExtras();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(bundle.getByteArray("image"));
        Bitmap imageBit = BitmapFactory.decodeStream(imageStream);
        fecha = findViewById(R.id.image_date);
        size = findViewById(R.id.image_size);
        imageView = findViewById(R.id.image_detail);
        imageView.setImageBitmap(imageBit);
        fecha.setText(bundle.getString("fecha"));
        size.setText(bundle.get("size").toString() + "kB");


    }


}


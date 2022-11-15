package com.example.camerarecord;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ortiz.touchview.TouchImageView;

import java.io.ByteArrayInputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DetailForImage extends AppCompatActivity {
    TouchImageView image;
    ConstraintLayout container;
    Boolean isEditable = false;
    Boolean isRotated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_for_image);
        image = findViewById(R.id.image_detail_all);
        container = findViewById(R.id.container_for_image);
        Intent intentImage = getIntent();
        Bundle bundle = intentImage.getExtras();
        byte[] imageBundle = bundle.getByteArray("image");
        ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBundle);
        Bitmap imageBit = BitmapFactory.decodeStream(imageStream);
        image.setImageBitmap(imageBit);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
        image.setOnClickListener(v -> {
            isEditable = !isEditable;
            getSupportActionBar().setDisplayHomeAsUpEnabled(isEditable);
            getSupportActionBar().setDisplayShowHomeEnabled(isEditable);

            invalidateMenu();
            if (isEditable) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
                image.resetZoom();
                image.animate();
            } else {
                container.setBackgroundColor(Color.BLACK);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
                image.setMaxZoom(10);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEditable) {
            getMenuInflater().inflate(R.menu.menu_detail_action, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        long id = item.getItemId();

        if (id == R.id.rotate) {
            if (!isRotated) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                isRotated = true;

            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                isRotated = false;
            }


        } else {
            super.onBackPressed();
        }
        return true;
    }
}

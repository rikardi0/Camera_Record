package com.example.camerarecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.camerarecord.adapter.ImageAdapter;
import com.example.camerarecord.db.AdminSQLiteOpenHelper;
import com.example.camerarecord.model.ImageInfo;
import com.example.camerarecord.model.SelectImage;
import com.example.camerarecord.utils.Spacer;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ImageInfo> infoList;
    AdminSQLiteOpenHelper Db;
    ImageAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoList = new ArrayList<>();
        Db = new AdminSQLiteOpenHelper(this);
        Db.readData(infoList);

        adapter = new ImageAdapter(infoList, this);

        recyclerView = findViewById(R.id.recycler_image_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new Spacer(0, 20));
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        if (SelectImage.isIsSelected()) {

            getMenuInflater().inflate(R.menu.menu_eliminate, menu);
        } else {

            getMenuInflater().inflate(R.menu.menu_acciones, menu);
        }

        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();
        if (id == R.id.add_button) {

            boolean pick = true;
            if (pick) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else PickImage();
            } else {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else PickImage();
            }
        } else if (id == R.id.delete_button) {
            for (ImageInfo strTemp : infoList) {
                if (strTemp.isSelected()) {
                    Db.deleteData(strTemp.getId());

                }
            }
            Toast.makeText(this, "Imagen(s) eliminada(s)", Toast.LENGTH_SHORT).show();
            SelectImage.setIsSelected(false);
            infoList.clear();
            Db.readData(infoList);
            adapter.notifyDataSetChanged();
            SelectImage.setFirstTerm(true);
            SelectImage.setCount(0);
            getSupportActionBar().setTitle("Camera Record");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFA13E37));
            invalidateMenu();

        }
        return true;
    }


    private void PickImage() {
        CropImage.activity().setAspectRatio(1, 1).start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    InputStream stream = getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);

                    byte[] img = byteArray.toByteArray();
                    Db.insertData(img);
                    infoList.clear();
                    Db.readData(infoList);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Imagen Agregada", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}

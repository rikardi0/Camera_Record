package com.example.camerarecord;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.example.camerarecord.adapter.ImageAdapter;
import com.example.camerarecord.db.AdminSQLiteOpenHelper;
import com.example.camerarecord.model.ImageInfo;
import com.example.camerarecord.model.SelectImage;
import com.example.camerarecord.ui.Spacer;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    ProgressDialog progressBar;
    AdminSQLiteOpenHelper Db;
    ImageAdapter adapter;
    FirebaseStorage storage;
    StorageReference storageRef;
    Uri resultUri;

    @Override
    public void onBackPressed() {
        if (adapter.getSelectedItemCount() > 0) {
            SelectImage.setIsSelected(false);
            invalidateMenu();
            adapter.deselectAll();
            getSupportActionBar().setTitle("Camera Record");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFA13E37));

        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("messi", "muu");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_image_list);

        infoList = new ArrayList<>();
//DB SQLite initialization
        Db = new AdminSQLiteOpenHelper(this);
        adapter = new ImageAdapter(infoList, this);
//Firebase instance
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        Db.readData(infoList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new Spacer(0, 20));
        recyclerView.setAdapter(adapter);
        adapterProperties();
    }

    private void adapterProperties() {
        adapter.setSingleClickMode(false);
        adapter.setMultiChoiceSelectionListener(new MultiChoiceAdapter.Listener() {
            @Override
            public void OnItemSelected(int selectedPosition, int itemSelectedCount, int allItemCount) {
                infoList.get(selectedPosition - 1).setSelected(true);
                changeMenu(true, "Eliminar " + itemSelectedCount + " imagen(s)", 0xFFE7E7E7);
            }

            @Override
            public void OnItemDeselected(int deselectedPosition, int itemSelectedCount, int allItemCount) {
                infoList.get(deselectedPosition - 1).setSelected(false);
                changeMenu(true, "Eliminar " + itemSelectedCount + " imagen(s)", 0xFF7E7776);
                if (itemSelectedCount == 0) {
                    changeMenu(false, "Camera Record              ", 0xFFA13E37);
                }
            }

            @Override
            public void OnSelectAll(int itemSelectedCount, int allItemCount) {
            }

            @Override
            public void OnDeselectAll(int itemSelectedCount, int allItemCount) {
            }
        });
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
            updateOnDelete();
        } else if (id == R.id.update) {
            progressBar = new ProgressDialog(this);
            progressBar.setCancelable(false);
            progressBar.setMessage("Sincronizando...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.show();

            StorageReference allImages = storageRef.child("images");
            allImages.listAll().addOnSuccessListener(listResult -> {
                List<String> filesName = new ArrayList<>();
                List<String> filesList = new ArrayList<>();
                for (StorageReference item1 : listResult.getItems()) {
                    filesName.add(item1.getName());
                    for (ImageInfo info : infoList) {
                        filesList.add(info.getStorage());
                        filesName.removeAll(filesList);
                    }
                }
                for (String downloadFile : filesName) {
                    StorageReference imagePath = storageRef.child("images/" + downloadFile);
                    imagePath.getBytes(716800).addOnSuccessListener(bytes -> {
                        Db.insertData(bytes, downloadFile);
                        updateAdapter(infoList);


                    });
                }

                deleteDialog();
            });
        }

        return true;
    }

    private void deleteDialog() {
        try {
            Thread.sleep(500);
            progressBar.dismiss();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void updateAdapter(List<ImageInfo> info) {
        infoList.clear();
        Db.readData(info);
        adapter.notifyDataSetChanged();
    }

    private void updateOnDelete() {
        Toast.makeText(this, "Imagen(s) eliminada(s)", Toast.LENGTH_SHORT).show();
        updateAdapter(infoList);
        adapter.deselectAll();
        SelectImage.setIsSelected(false);
        getSupportActionBar().setTitle("Camera Record");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFA13E37));
        invalidateMenu();
    }

    // Crop image method
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
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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
                resultUri = result.getUri();

                try {
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    InputStream stream = getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArray);
                    byte[] img = byteArray.toByteArray();
                    String storageFile = resultUri.getPath().substring(51);
                    Db.insertData(img, storageFile);
                    updateAdapter(infoList);
                    // Firebase Upload
                    StorageReference imageRef = storageRef.child("images/" + storageFile);
                    UploadTask uploadTask = imageRef.putBytes(img);

                    uploadTask.addOnFailureListener(e -> Log.d("messi", "error"));

                    Toast.makeText(this, "Imagen Agregada", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeMenu(boolean value, String title, int color) {
        SelectImage.setIsSelected(value);
        invalidateMenu();
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
    }
}

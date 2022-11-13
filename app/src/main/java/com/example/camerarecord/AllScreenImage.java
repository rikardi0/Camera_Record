package com.example.camerarecord;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        ByteArrayInputStream imageStream = new ByteArrayInputStream(bundle.getByteArray("image"));
        Bitmap imageBit = BitmapFactory.decodeStream(imageStream);

        getSupportActionBar().hide();

        imageView.setImageBitmap(imageBit);
        date.setText(bundle.getString("fecha"));
        storage.setText(bundle.getString("storage"));

        size.setText(bundle.get("size").toString().concat("kb"));
        resolution.setText(bundle.get("height").toString().concat("x ").concat(String.valueOf(bundle.get("width"))).concat(" pixel"));
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        storageDb = FirebaseStorage.getInstance();
        storageRef = storageDb.getReference();
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                alertDialog.setMessage("Desea eliminar la imagen de la nube?");
                alertDialog.setTitle("Atencion!");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                StorageReference eliminatePath = storageRef.child("images/" + bundle.getString("storage"));
                                eliminatePath.delete();
                                Db.deleteData(bundle.getString("id"));


                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                alertDialog.show();

                alertDialog.show();
                return false;
            }
        });

    }
}


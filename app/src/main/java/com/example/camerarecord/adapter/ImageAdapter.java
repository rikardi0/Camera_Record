package com.example.camerarecord.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.camerarecord.AllScreenImage;
import com.example.camerarecord.MainActivity;
import com.example.camerarecord.R;
import com.example.camerarecord.db.AdminSQLiteOpenHelper;
import com.example.camerarecord.model.ImageInfo;
import com.example.camerarecord.model.SelectImage;

import java.io.ByteArrayInputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    List<ImageInfo> list;
    private LayoutInflater inflater;
    AdminSQLiteOpenHelper Db;
    private Context context;


    public ImageAdapter(List<ImageInfo> itemList, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = itemList;

    }

    @NonNull

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.image_list_element, null);
        return new ImageAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        holder.binData(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView date, id, size;
        ConstraintLayout container;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image_list);
            date = itemView.findViewById(R.id.date_image);
            container = itemView.findViewById(R.id.container_image);

        }

        void binData(final ImageInfo item) {
            //Convert byte[] to bitmap
            ByteArrayInputStream imageStream = new ByteArrayInputStream(item.getImage());
            Db = new AdminSQLiteOpenHelper(context);
            Bitmap imageBit = BitmapFactory.decodeStream(imageStream);

            int heightImg = imageBit.getHeight();
            int widthImg = imageBit.getWidth();
            double imageBytes = item.getImage().length;
            double imageSize = Math.round(imageBytes / 1024 * 100.0) / 100.0;
            image.setImageBitmap(imageBit);
            String dateText = item.getDate().substring(0, 10);
            date.setText(dateText);
            container.setOnClickListener(v -> {
                Intent i = new Intent(context, AllScreenImage.class);
                i.putExtra("fecha", item.getDate());
                i.putExtra("image", item.getImage());
                i.putExtra("size", imageSize);
                i.putExtra("height", heightImg);
                i.putExtra("width", widthImg);
                shortClick(item, i);


            });
            container.setOnLongClickListener(v -> {
                longPress(item);
                //  Db.deleteData(item.getId());

                return true;
            });

        }

        private void shortClick(ImageInfo item, Intent i) {

            if (SelectImage.getCount() == 0) {
                context.startActivity(i);

            }
            if (!SelectImage.isFirstTerm() && !item.isSelected()) {
                container.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_container_ui_selected));
                item.setSelected(true);
                SelectImage.setCount(SelectImage.getCount() + 1);
                changeMenu(true, "Eliminar " + SelectImage.getCount() + " imagen(s)", 0xFF7E7776);
            } else if (item.isSelected() && SelectImage.getCount() != 0) {
                container.setBackgroundColor(Color.TRANSPARENT);
                item.setSelected(false);
                SelectImage.setCount(SelectImage.getCount() - 1);
                changeMenu(true, "Eliminar " + SelectImage.getCount() + " imagen(s)", 0xFF7E7776);
            }
            if (SelectImage.getCount() == 0) {
                changeMenu(false, "Camera Record", 0xFFA13E37);
                SelectImage.setFirstTerm(true);
            }

        }

        private void longPress(ImageInfo item) {
            if (SelectImage.isFirstTerm()) {
                SelectImage.setCount(SelectImage.getCount() + 1);
                item.setSelected(true);
                container.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_container_ui_selected));
                SelectImage.setFirstTerm(false);
                changeMenu(true, "Eliminar " + SelectImage.getCount() + " imagen(s)", 0xFF7E7776);
            }
        }


    }

    private void changeMenu(boolean value, String title, int color) {
        SelectImage.setIsSelected(value);
        ((MainActivity) context).invalidateMenu();
        ((MainActivity) context).getSupportActionBar().setTitle(title);
        ((MainActivity) context).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
    }

}

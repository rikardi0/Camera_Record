package com.example.camerarecord.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.camerarecord.AllScreenImage;
import com.example.camerarecord.R;
import com.example.camerarecord.model.ImageInfo;
import com.example.camerarecord.set_get.ImageState;

import java.io.ByteArrayInputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    List<ImageInfo> list;
    private LayoutInflater inflater;
    private Context context;
    boolean firstItem = true;
    Integer count = 0;


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

        // final ImageInfo model = list.get(position);
        // ImageState imageStateAdapter = new ImageState();

        //  holder.container.setOnLongClickListener(v -> {
        //      longPress(model, holder, imageStateAdapter);
        //      return true;
        //  });
        //  holder.container.setOnClickListener(v -> {


        //      shortPress(model, holder, imageStateAdapter);
        //  });
    }

    private void shortPress(ImageInfo model, ImageAdapter.ViewHolder holder, ImageState imageState) {

        if (count == 0) {
            firstItem = true;
        }
        if (firstItem) {

        } else if (!firstItem && !model.isSelected()) {
            count = count + 1;
            model.setSelected(true);
        } else {
            model.setSelected(false);
            count = count - 1;
        }
        if (model.isSelected()) {
            holder.container.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_container_ui_selected));
            imageState.setImageIsSelected(true);
        } else {
            holder.container.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void longPress(ImageInfo model, ImageAdapter.ViewHolder holder, ImageState imageState) {
        if (firstItem) {
            count = count + 1;
            model.setSelected(true);
            holder.container.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_container_ui_selected));
            imageState.setImageIsSelected(true);
            firstItem = false;
        }
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
            ByteArrayInputStream imageStream = new ByteArrayInputStream(item.getImage());
            Bitmap imageBit = BitmapFactory.decodeStream(imageStream);
            double imageBytes = item.getImage().length;
            double imageSize = Math.round(imageBytes / 1024 * 100.0) / 100.0;
            image.setImageBitmap(imageBit);
            date.setText(item.getDate());
            container.setOnClickListener(v -> {
                Intent i = new Intent(context, AllScreenImage.class);
                i.putExtra("fecha", item.getDate());
                i.putExtra("image", item.getImage());
                i.putExtra("size", imageSize);
                context.startActivity(i);
            });


        }


    }

}

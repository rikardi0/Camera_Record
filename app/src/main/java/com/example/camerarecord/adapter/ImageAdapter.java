package com.example.camerarecord.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.example.camerarecord.AllScreenImage;
import com.example.camerarecord.R;
import com.example.camerarecord.db.AdminSQLiteOpenHelper;
import com.example.camerarecord.model.ImageInfo;

import java.io.ByteArrayInputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends MultiChoiceAdapter<ImageAdapter.ViewHolder> {
    List<ImageInfo> list;
    private LayoutInflater inflater;
    AdminSQLiteOpenHelper Db;
    private Context context;


    public ImageAdapter(List<ImageInfo> itemList, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = itemList;

    }

    @Override
    public void setMultiChoiceSelectionListener(Listener listener) {
        super.setMultiChoiceSelectionListener(listener);
    }

    @Override
    protected View.OnClickListener defaultItemViewClickListener(ViewHolder holder, int position) {
        Intent i = new Intent(context, AllScreenImage.class);
        String date = list.get(position).getDate();
        byte[] image = list.get(position).getImage();

        ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
        Bitmap imageBit = BitmapFactory.decodeStream(imageStream);

        int heightImg = imageBit.getHeight();
        int widthImg = imageBit.getWidth();
        double imageBytes = image.length;
        double imageSize = Math.round(imageBytes / 1024 * 100.0) / 100.0;

        i.putExtra("fecha", date);
        i.putExtra("image", image);
        i.putExtra("size", imageSize);
        i.putExtra("height", heightImg);
        i.putExtra("width", widthImg);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(i);

            }
        });
        return super.defaultItemViewClickListener(holder, position);
    }


    @NonNull

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.image_list_element, null);
        return new ImageAdapter.ViewHolder(view);

    }

    @Override
    public void setActive(@NonNull View view, boolean state) {
        super.setActive(view, state);



    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.binData(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView date;
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
            image.setImageBitmap(imageBit);
            String dateText = item.getDate().substring(0, 10);
            date.setText(dateText);
        }


    }



}







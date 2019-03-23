package com.example.blooddonation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.imageViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;

    public imageAdapter(Context context,List<Upload> uploads)
    {
        mContext = context;
        mUploads = uploads;
    }
    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,viewGroup,false);
        return new imageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder imageViewHolder, int i) {
        Upload uploadCur = mUploads.get(i);
        imageViewHolder.imgDescription.setText(uploadCur.getImgName());
        Picasso.get().load(uploadCur.getImgUri()).placeholder(R.drawable.no).fit().
                centerCrop()
                .into(imageViewHolder.image_view);


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class imageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView imgDescription;
        public ImageView image_view;


        public imageViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imgDescription = itemView.findViewById(R.id.img_description);
            image_view = itemView.findViewById(R.id.image_view);
        }
    }
}


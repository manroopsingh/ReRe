package com.example.manroop.ReRe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.manroop.ReRe.R;

import java.util.List;

/**
 * Created by INSPIRON on 14-Jan-17.
 */
public class RestPhotosAdapter extends RecyclerView.Adapter<RestPhotosAdapter.ViewHolder> {


    List<String> photoUrl;
    Context context;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;

        public ViewHolder(final View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.restDetailsPhotos);
        }

        @Override
        public void onClick(View v) {

        }

    }


    public RestPhotosAdapter(Context context, List<String> photoUrl) {
        this.photoUrl = photoUrl;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_photo_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String photos = photoUrl.get(position);
        Glide.with(context).load(photos).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return photoUrl.size();
    }

}


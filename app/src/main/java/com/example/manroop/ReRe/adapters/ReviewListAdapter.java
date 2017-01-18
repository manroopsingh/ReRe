package com.example.manroop.ReRe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.manroop.ReRe.R;
import com.example.manroop.ReRe.pojos.yelpreviews.Review;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by INSPIRON on 13-Jan-17.
 */
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {


    List<Review> reviews;
    Context context;
    public ReviewListAdapter(Context context,List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView user,timeStamp, review;
        private RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.reviews_imageView);
            user = (TextView) itemView.findViewById(R.id.reviews_userName);
            timeStamp = (TextView) itemView.findViewById(R.id.reviews_timeStamp);
            review = (TextView) itemView.findViewById(R.id.reviews_review);
            ratingBar = (RatingBar) itemView.findViewById(R.id.reviews_ratingBar);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list, parent, false);
        return new ViewHolder(view);
    }

    Date date;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review r = reviews.get(position);
        holder.review.setText(r.getText());
        holder.ratingBar.setRating(r.getRating());
        Glide.with(context).load(r.getUser().getImageUrl()).into(holder.imageView);
        holder.user.setText(r.getUser().getName());

        DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        try {
            date = format.parse(r.getTimeCreated());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy");
        holder.timeStamp.setText(newFormat.format(date));

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


}

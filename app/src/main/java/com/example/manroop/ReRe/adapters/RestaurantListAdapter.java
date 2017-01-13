package com.example.manroop.ReRe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.manroop.ReRe.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * Created by INSPIRON on 08-Jan-17.
 */

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> {


    public static final String TAG = "Adapterrrrrrrrrrrr";
    List<com.example.manroop.ReRe.pojos.yelpBusinesses.Business> businesses;
    private Context context;
    private String distance;
    private Double avgSpeed = 40.0;
    private Double rawDistance;
    private Double avgTime;
    private int waitingTime;

    public RestaurantListAdapter(Context context, List<com.example.manroop.ReRe.pojos.yelpBusinesses.Business> businesses) {
        this.context = context;
        this.businesses = businesses;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, cuisine, distance,dollarRange,reviews, price;
        private RatingBar rating;
        private LinearLayout linearLayout;
        private ImageView imageView;
        private Button waitTime;

        public MyViewHolder(final View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.linear_res_list);
            name = (TextView) view.findViewById(R.id.name);
            rating = (RatingBar) view.findViewById(R.id.rating);
            cuisine = (TextView) view.findViewById(R.id.cuisine);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            distance = (TextView) view.findViewById(R.id.distance);
            dollarRange = (TextView) view.findViewById(R.id.dollar_range);
            reviews = (TextView) view.findViewById(R.id.reviews);
            waitTime=(Button) view.findViewById(R.id.btn_wait_time);
        }

        @Override
        public void onClick(View v) {
            reviews.setOnClickListener(this);
        }




    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View resView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list, parent, false);
        return new MyViewHolder(resView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final com.example.manroop.ReRe.pojos.yelpBusinesses.Business b = businesses.get(position);

        holder.name.setText(b.getName());
        holder.rating.setProgress(b.getRating().intValue());
        holder.cuisine.setText(b.getCategories().get(0).getAlias().toUpperCase().toString());
        Glide.with(context).load(b.getImageUrl()).into(holder.imageView);
        holder.dollarRange.setText(b.getPrice());
        calculateDistance(b);
        holder.distance.setText(distance);
        holder.reviews.setText("(" + b.getReviewCount() + ") reviews");
        setWaitTime(holder);


        holder.reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent in = new Intent(context, ReviewActivity.class);
//                in.putExtra("business", b);
//                context.startActivity(in);

//                DialogFragment review_dialog = new ReviewsFragment();
//                review_dialog.show(((Activity) context).getFragmentManager(),"reviews");
            }
        });


    }

    private void setWaitTime(MyViewHolder holder) {
        waitingTime = randInt();
        if(waitingTime<15)
        holder.waitTime.setBackgroundResource(R.drawable.wait_green);
        if(waitingTime>15 & waitingTime<30)
            holder.waitTime.setBackgroundResource(R.drawable.wait_yellow);
        if(waitingTime>30 & waitingTime<45)
            holder.waitTime.setBackgroundResource(R.drawable.wait_orange);
        if(waitingTime>45)
            holder.waitTime.setBackgroundResource(R.drawable.wait_red);

        holder.waitTime.setText(String.valueOf(waitingTime));
    }

    public static int randInt() {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((60 - 1) + 1) + 1;

        return randomNum;
    }

    private void calculateDistance(com.example.manroop.ReRe.pojos.yelpBusinesses.Business b) {
        rawDistance = b.getDistance();
        rawDistance = rawDistance * 1.6 / 1000;
        rawDistance = roundTwoDecimals(rawDistance);

        avgTime = rawDistance * 60 / avgSpeed;
        avgTime = roundZeroDecimals(avgTime);
        distance = String.valueOf(rawDistance) + " mi, " + String.valueOf(avgTime.intValue()) + " mins";
    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public double roundTwoDecimals(double d) {


        String format;
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public double roundZeroDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#");
        return Double.valueOf(twoDForm.format(d));
    }
}

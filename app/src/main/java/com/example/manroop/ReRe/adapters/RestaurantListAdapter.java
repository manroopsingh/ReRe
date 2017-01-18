package com.example.manroop.ReRe.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.manroop.ReRe.activities.MakeReservationActivity;
import com.example.manroop.ReRe.activities.RestaurantDetailActivity;
import com.example.manroop.ReRe.activities.ReviewActivity;
import com.example.manroop.ReRe.services.MyIntentService;
import com.example.manroop.ReRe.services.RestDetailIntentService;
import com.example.manroop.ReRe.services.ReviewsIntentService;

import java.text.DecimalFormat;
import java.util.Date;
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
    int hr1, hr2, hr3, min1, min2, min3, min=0;

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
        private Button resvTime1, resvTime2, resvTime3;

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
            waitTime=(Button) view.findViewById(R.id.btnTimer);
            resvTime1 = (Button) view.findViewById(R.id.resvTime1);
            resvTime2 = (Button) view.findViewById(R.id.resvTime2);
            resvTime3 = (Button) view.findViewById(R.id.resvTime3);

        }

        @Override
        public void onClick(View v) {
            linearLayout.setOnClickListener(this);
            reviews.setOnClickListener(this);
            resvTime1.setOnClickListener(this);
            resvTime2.setOnClickListener(this);
            resvTime3.setOnClickListener(this);
            waitTime.setOnClickListener(this);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View resView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list, parent, false);
        return new MyViewHolder(resView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final com.example.manroop.ReRe.pojos.yelpBusinesses.Business b = businesses.get(position);

        holder.name.setText(b.getName());
        holder.rating.setRating(b.getRating().floatValue());
        holder.cuisine.setText(b.getCategories().get(0).getAlias().toUpperCase().toString());
        Glide.with(context).load(b.getImageUrl()).into(holder.imageView);
        holder.dollarRange.setText(b.getPrice());
        calculateDistance(b);
        holder.distance.setText(distance);
        holder.reviews.setText("(" + b.getReviewCount() + ") reviews");
        setWaitTime(holder);
        holder.resvTime1.setText(resvTime(0));
        holder.resvTime2.setText(resvTime(1));
        holder.resvTime3.setText(resvTime(2));


        holder.reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intGetReview = new Intent(context, ReviewsIntentService.class);
                intGetReview.putExtra("id", b.getId());
                context.startService(intGetReview);

                Intent in = new Intent(context, ReviewActivity.class);
                in.putExtra("id", b.getId());
                context.startActivity(in);

            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, RestDetailIntentService.class);
                intent.putExtra("id", b.getId());
                context.startService(intent);

                Intent intent2 = new Intent(context, MyIntentService.class);
                intent2.putExtra("id", b.getId());
                context.startService(intent2);

                Intent in = new Intent(context, RestaurantDetailActivity.class);
                context.startActivity(in);

            }
        });

        holder.resvTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MakeReservationActivity.class);
                intent.putExtra("TAG", "restList");
                intent.putExtra("TAG_RES_LIST", "fixedtime");
                intent.putExtra("businessV3", b);
                intent.putExtra("hour", hr1);
                intent.putExtra("minute", min1);
                context.startActivity(intent);
            }
        });

        holder.resvTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MakeReservationActivity.class);
                intent.putExtra("TAG", "restList");
                intent.putExtra("TAG_RES_LIST", "fixedtime");
                intent.putExtra("businessV3", b);
                intent.putExtra("hour", hr2);
                intent.putExtra("minute", min2);
                context.startActivity(intent);
            }
        });


        holder.resvTime3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MakeReservationActivity.class);
                intent.putExtra("TAG", "restList");
                intent.putExtra("TAG_RES_LIST", "fixedtime");
                intent.putExtra("businessV3", b);
                intent.putExtra("hour", hr3);
                intent.putExtra("minute", min3);
                context.startActivity(intent);
            }
        });


        holder.waitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MakeReservationActivity.class);
                intent.putExtra("TAG", "restList");
                intent.putExtra("TAG_RES_LIST", "waittime");
                intent.putExtra("businessV3", b);
                intent.putExtra("timer", holder.waitTime.getText().toString());
                context.startActivity(intent);
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

    public String resvTime(int i){

        String resvtime[]={"","",""};

        int time;
        Date now = new Date();
        now.getTime();
        int hrNow = now.getHours();
        int minNow = now.getMinutes();

        min = minNow / 15;


        if ((hrNow > 21) || (hrNow < 12))
            hrNow = 13;

        switch (min) {
            case 0:
                hr1 = hrNow;
                hr2 = hrNow;
                hr3 = hrNow + 1;
                min1 = 30;
                min2 = 45;
                min3 = 00;
            case 1:
                hr1 = hrNow;
                hr2 = hrNow + 1;
                hr3 = hrNow + 1;
                min1 = 45;
                min2 = 00;
                min3 = 15;
            case 2:
                hr1 = hrNow + 1;
                hr2 = hrNow + 1;
                hr3 = hrNow + 1;
                min1 = 00;
                min2 = 15;
                min3 = 30;
            case 3:
                hr1 = hrNow + 1;
                hr2 = hrNow + 1;
                hr3 = hrNow + 1;
                min1 = 15;
                min2 = 30;
                min3 = 45;
        }


        resvtime[0] = hr1 + ":" + min1;
        resvtime[1] = hr2 + ":" + min2;
        resvtime[2] = hr3 + ":" + min3;

        return resvtime[i];
    }
}

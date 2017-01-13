package com.example.manroop.ReRe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.manroop.ReRe.R;
import com.example.manroop.ReRe.services.ReviewsIntentService;
import com.yelp.clientlib.entities.Business;

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = "ReviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();
        Business business = (Business)intent.getSerializableExtra("business");
        //Log.d(TAG, "onCreate: "+ business.id());

        Intent intent1 = new Intent(this, ReviewsIntentService.class);
        intent1.putExtra("id", business.id());
        startService(intent1);



    }
}

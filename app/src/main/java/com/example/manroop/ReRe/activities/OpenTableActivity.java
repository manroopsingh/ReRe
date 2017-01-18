package com.example.manroop.ReRe.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.example.manroop.ReRe.R;
import com.example.manroop.ReRe.RestCalls;
import com.example.manroop.ReRe.pojos.openTableRestaurants.OpenTableRestaurants;
import com.example.manroop.ReRe.pojos.openTableRestaurants.Restaurant;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OpenTableActivity extends AppCompatActivity {

    private static final String TAG = "OpenTableActivity: ";
    private Subscription mySubscription;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_open_table);

        textView = (TextView) findViewById(R.id.opentableTextview);

        searchOpenTable();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mySubscription != null && !mySubscription.isUnsubscribed()) {
            mySubscription.unsubscribe();
        }
    }

    public void searchOpenTable() {

        final RestCalls restCalls = new RestCalls();
        Observable<OpenTableRestaurants> myObservable = Observable.fromCallable(new Callable<OpenTableRestaurants>() {

            @Override
            public OpenTableRestaurants call() {
                OpenTableRestaurants openTableRestaurants = restCalls.getOpenTableResults();
                return openTableRestaurants;
            }
        });

        mySubscription = myObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OpenTableRestaurants>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(OpenTableRestaurants restaurants) {
                        List<Restaurant> restaurantList = restaurants.getRestaurants();
                        StringBuilder sb = new StringBuilder();
                        int i =0;
                        for (Restaurant r : restaurantList) {
                            Log.d(TAG, "onNext: " + r.getName());
                            sb.append(restaurantList.get(i).getName());
                            sb.append("\n");
                            i++;
                        }
                        textView.setText(sb.toString());
                    }
                });
    }

}

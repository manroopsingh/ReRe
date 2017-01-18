package com.example.manroop.ReRe.activities;

import android.Manifest;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.manroop.ReRe.R;
import com.example.manroop.ReRe.adapters.RestPhotosAdapter;
import com.example.manroop.ReRe.databeans.ResData;
import com.example.manroop.ReRe.fragments.FragmentOffer2Details;
import com.example.manroop.ReRe.fragments.FragmentOfferDetails;
import com.example.manroop.ReRe.pojos.yelpBusinessDetail.BusinessDetail;
import com.example.manroop.ReRe.pojos.yelpBusinessDetail.Category;
import com.example.manroop.ReRe.pojos.yelpBusinessDetail.Coordinates;
import com.example.manroop.ReRe.pojos.yelpBusinessDetail.Hour;
import com.example.manroop.ReRe.pojos.yelpBusinessDetail.Location;
import com.example.manroop.ReRe.pojos.yelpBusinessDetail.Open;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Deal;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "RestDetailActivity";
    Button btn_makeReservation, btn_orderFood;
    String res_name;
    GoogleMap resmap;
    TextView offer1, offer2, price, cuisine, address;
    private GoogleApiClient mGoogleApiClient;
    ResData resData;
    //LatLng resLoc = new LatLng(37, -121);

    private LatLng reslatLng;
    private ImageView image1, imageViewEat24, imageViewYelp;
    private String yelpUrl, eat24Url;
    private BusinessDetail businessDetail;
    private Business business;


    private TextView reviews, distance, status, hours, description;
    private RatingBar ratingBar;

    private List<String> photoUrl = new ArrayList<>();
    private List<String> photoUrl2 = new ArrayList<>();
    private RecyclerView recyclerViewPhotos;
    private RecyclerView.LayoutManager layoutManager;
    private RestPhotosAdapter restPhotosAdapter;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout linearLayoutLinks, linearLayoutDesc, linearLayoutDirections, linearLayoutCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Firebase.setAndroidContext(this);
//
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .enableAutoManage(this, this)
//                    .build();
//        }
//
//
//
//        Intent in = getIntent();
//        res_name = in.getStringExtra("res_name");

        setTitle(res_name);

//        FetchResDetails(res_name);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.resmap);
        mapFragment.getMapAsync((this));
        mapFragment.getMapAsync(this);




        //map elements
        image1 = (ImageView) findViewById(R.id.img_res);
        price = (TextView) findViewById(R.id.restDetailsprice);
        cuisine = (TextView) findViewById(R.id.restDetailscuisine);
        address = (TextView) findViewById(R.id.restDetailsaddress);
        btn_makeReservation = (Button) findViewById(R.id.btn_makeReservation);
        btn_orderFood = (Button) findViewById(R.id.btn_orderFood);

        ratingBar = (RatingBar) findViewById(R.id.restDetailsrating);
        reviews = (TextView) findViewById(R.id.restDetailsreviews);
        distance = (TextView) findViewById(R.id.restDetailsdistance);
        status = (TextView) findViewById(R.id.restDetailsstatus);
        hours = (TextView) findViewById(R.id.restDetailsHours);
        description = (TextView) findViewById(R.id.restDetailsDescription);
        imageViewEat24 = (ImageView) findViewById(R.id.restDetailsEat24);
        imageViewYelp = (ImageView) findViewById(R.id.restDetailsYelp);
        recyclerViewPhotos = (RecyclerView) findViewById(R.id.recyclerRestPhotos);


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPhotos.setLayoutManager(layoutManager);
        recyclerViewPhotos.setItemAnimator(new DefaultItemAnimator());
        restPhotosAdapter = new RestPhotosAdapter(this, photoUrl2);
        recyclerViewPhotos.setAdapter(restPhotosAdapter);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        linearLayoutLinks = (LinearLayout) findViewById(R.id.linearLinks);
        linearLayoutDesc = (LinearLayout) findViewById(R.id.linearDescription);
        linearLayoutDirections = (LinearLayout) findViewById(R.id.restDetailsDirections);
        linearLayoutCall = (LinearLayout) findViewById(R.id.restDetailsCall);


        linearLayoutDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                android.location.Location myLocation = getCurrentLocation();
                String myLocationString = String.valueOf(myLocation.getLatitude()) + "," + String.valueOf(myLocation.getLongitude());
                String restLocationString = String.valueOf(reslatLng.latitude) + "," + String.valueOf(reslatLng.longitude);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + myLocationString + "&daddr=" + restLocationString));
                startActivity(intent);

            }
        });

        linearLayoutCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = business.displayPhone();

                String uri = "tel:" + tel.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                } else
                    startActivity(intent);
            }
        });

//        image1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.res_fiorillo);
//
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
//
//                File f = new File(Environment.getExternalStorageDirectory()
//                        + File.separator + "test.jpg");
//                try {
//                    f.createNewFile();
//                    FileOutputStream fo = new FileOutputStream(f);
//                    fo.write(bytes.toByteArray());
//                    fo.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//                Uri path = Uri.fromFile(f);
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(path, "image/*");
//                startActivity(intent);
//            }
//        });

        offer1 = (TextView) findViewById(R.id.text_offer1);
//        offer1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showOffer1Dialog();
//            }
//        });
        offer2 = (TextView) findViewById(R.id.text_offer2);
//        offer2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showOffer2Dialog();
//            }
//        });
//
//
//        phone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String tel = phone.getText().toString();
//
//                String uri = "tel:" + tel.trim();
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse(uri));
//                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                startActivity(intent);
//            }
//        });
//
        btn_makeReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), MakeReservationActivity.class);
                in.putExtra("TAG", "restDetail");
                in.putExtra("businessDetail", businessDetail);
                in.putExtra("business", business);
                startActivity(in);

            }
        });

        btn_orderFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), MenuActivity.class);
                in.putExtra("res_name", business.name());
                startActivity(in);

            }
        });


        imageViewEat24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(eat24Url));
                startActivity(i);
            }
        });

        imageViewYelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(yelpUrl));
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver(brBusinessDetails, new IntentFilter("getBizDetail"));
        LocalBroadcastManager.getInstance(this).registerReceiver(brBusinessDetails2, new IntentFilter("getBizDetail2"));

        super.onStart();
    }

    @Override
    protected void onStop() {


        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(brBusinessDetails);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(brBusinessDetails2);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        if (id == R.id.add_res) {
            Intent in = new Intent(getApplicationContext(), AddRes.class);
            startActivity(in);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_reserveTable) {
            Intent in = new Intent(getApplicationContext(), RestaurantListActivity.class);
            startActivity(in);
            // Handle the camera action
        } else if (id == R.id.nav_myAccount) {
            Intent in = new Intent(getApplicationContext(), UserAccountActivity.class);
            startActivity(in);
        } else if (id == R.id.nav_myReservations) {
            Intent in = new Intent(getApplicationContext(), MyReservationsActivity.class);
            startActivity(in);

        } else if (id == R.id.nav_writeReview) {
            Intent in = new Intent(getApplicationContext(), MyReservationsActivity.class);
            startActivity(in);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public void FetchResDetails(final String res_name) {
//        Firebase myFirebaseRef = new Firebase("https://resplendent-heat-2353.firebaseio.com/Restaurants");
//
//
//        myFirebaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    resData = postSnapshot.getValue(ResData.class);
//                    //Toast.makeText(getApplicationContext(), res_name, Toast.LENGTH_SHORT).show();
//                    if (!resData.getName().isEmpty()) {
//
//                        if (resData.getName().equals(res_name)) {
//                            System.out.println(postSnapshot.getKey());
//
//                            price.setText(resData.getDollar_range());
//                            cuisine.setText(resData.getCuisine());
//
//                            StringBuilder sb = new StringBuilder();
//                            sb.append(resData.getStreet());
//                            sb.append(", ");
//                            sb.append(resData.getCity());
//                            sb.append(", ");
//                            sb.append(resData.getState());
//                            sb.append(", ");
//                            sb.append(resData.getZipcode());
//                            String add = sb.toString();
//
//                            address.setText(add);
//                            resLoc = new LatLng(Double.parseDouble(resData.getLatitude()), Double.parseDouble(resData.getLongitude()));
//                        } else {
//
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });
//
//    }


    private void showOffer1Dialog() {
        FragmentManager fm = this.getFragmentManager();

        DialogFragment newFragment = new FragmentOfferDetails();
        newFragment.show(fm, "offer1");
    }

    private void showOffer2Dialog() {
        FragmentManager fm = this.getFragmentManager();

        DialogFragment newFragment = new FragmentOffer2Details();
        newFragment.show(fm, "offer2");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        resmap = googleMap;

        resmap.getUiSettings().setScrollGesturesEnabled(false);
        resmap.getUiSettings().setZoomControlsEnabled(false);
        resmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                Intent in = new Intent(getApplicationContext(), MapsActivity.class);
                in.putExtra("latitude", reslatLng.latitude);
                in.putExtra("longitude", reslatLng.longitude);
                in.putExtra("res_name", business.name());
                startActivity(in);

            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public BroadcastReceiver brBusinessDetails = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            businessDetail = (BusinessDetail) intent.getSerializableExtra("bizDetail");

            price.setText(businessDetail.getPrice());
            collapsingToolbarLayout.setTitle(businessDetail.getName());

            int myWidth = 512;
            int myHeight = 384;

            Glide.with(getApplicationContext())
                    .load(businessDetail.getImageUrl())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            // Do something with bitmap here.
                            Drawable drawable = new BitmapDrawable(bitmap);
                            collapsingToolbarLayout.setBackground(drawable);
                        }
                    });

            reviews.setText(businessDetail.getReviewCount().toString() + " reviews");
            ratingBar.setRating(businessDetail.getRating().floatValue());

            Location location = businessDetail.getLocation();
            StringBuilder sb = new StringBuilder()
                    .append(location.getAddress1())
                    .append(", ")
                    .append(location.getCity())
                    .append(", ")
                    .append(location.getState() + " " + location.getZipCode());
            address.setText(sb.toString());


            Coordinates coordinates = businessDetail.getCoordinates();
            reslatLng = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
            resmap.moveCamera(CameraUpdateFactory.newLatLngZoom(reslatLng, 14));
            resmap.addMarker(new MarkerOptions().position(reslatLng).title(businessDetail.getName()));


            List<Category> categoryList = businessDetail.getCategories();
            StringBuilder sbCategory = new StringBuilder();
            for (Category c : categoryList) {
                sbCategory
                        .append(c.getTitle())
                        .append(" ");
            }
            cuisine.setText(sbCategory.toString());

            List<Hour> hourList = businessDetail.getHours();


            Boolean openNow = true;
            List<Open> openList = new ArrayList<>();
            StringBuilder sbHours = new StringBuilder();
            for (Hour h : hourList) {
                if (h.getHoursType().equals("REGULAR"))
                    openList = h.getOpen();
                openNow = h.getIsOpenNow();

            }

            if (openNow) {
                status.setText("Open");
                status.setTextColor(getResources().getColor(R.color.colorGreen));
            } else {
                status.setText("Closed");
                status.setTextColor(getResources().getColor(R.color.colorRed));
            }


            for (Open o : openList) {
                String day = getDay(o.getDay());
                sbHours.append(o.getStart() + "-" + o.getEnd() + " " + day)
                        .append("\n");
            }

            hours.setText(sbHours.toString());


            photoUrl = businessDetail.getPhotos();
            photoUrl2.addAll(photoUrl);

            for (String s : photoUrl2) {
                Log.d(TAG, "onReceive: " + s);
            }

            restPhotosAdapter.notifyDataSetChanged();
            Log.d(TAG, "onReceive: Photos");

        }
    };


    List<Deal> deals = new ArrayList<>();
    public BroadcastReceiver brBusinessDetails2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            business = (Business) intent.getSerializableExtra("bizDetail2");
            //phone.setText(business.displayPhone());


//            Log.d(TAG, "onReceive: " + business.deals().size());

            if (business.deals() != null) {
                deals = business.deals();
                if (deals.size() > 0) {
                    offer1.setText(deals.get(0).title());
                }
            }


            if (business.eat24Url() != null) {
                eat24Url = business.eat24Url();
            } else {
                linearLayoutLinks.removeView(imageViewEat24);
                imageViewEat24.setVisibility(View.INVISIBLE);
            }

            yelpUrl = business.mobileUrl();

            if (business.snippetText() != null)
                description.setText(business.snippetText());
            else
                linearLayoutDesc.removeAllViews();


        }
    };

    private String getDay(Integer day) {
        String dayString = "";
        switch (day) {
            case 0:
                return "Mon";
            case 1:
                return "Tue";
            case 2:
                return "Wed";
            case 3:
                return "Thu";
            case 4:
                return "Fri";
            case 5:
                return "Sat";
            case 6:
                return "Sun";

        }
        return dayString;
    }

    private android.location.Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return new android.location.Location("");
        }
        android.location.Location location = locationManager.getLastKnownLocation(bestProvider);

        return location;
    }
}

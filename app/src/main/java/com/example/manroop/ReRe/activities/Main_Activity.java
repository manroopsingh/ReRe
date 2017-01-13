package com.example.manroop.ReRe.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manroop.ReRe.R;
import com.example.manroop.ReRe.adapters.ResListAdapter;
import com.example.manroop.ReRe.adapters.RestaurantListAdapter;
import com.example.manroop.ReRe.pojos.yelpBusinesses.Business;
import com.example.manroop.ReRe.pojos.yelpBusinesses.YelpBusiness;
import com.example.manroop.ReRe.services.AccessTokenIntentService;
import com.example.manroop.ReRe.services.GeocodingIntentService;
import com.example.manroop.ReRe.services.RestaurantIntentService;
import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static com.example.manroop.ReRe.R.id.map;

public class Main_Activity extends AppCompatActivity
        implements LocationListener, NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    private static final String TAG = "MainActivityTag";

    public static String phone = "6508669800";

    private GoogleApiClient mGoogleApiClient;
    public static String Latitude = "37", Longitude = "-121";
    public static Location myLoc = new Location(LocationManager.NETWORK_PROVIDER);
    public static GoogleMap mMap;
    public static String rName, rWaittime;
    public String permissions[];

    private Location mLastLocation, location;
    private LocationManager locationManager;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RestaurantListAdapter restaurantListAdapter;
    private List<com.example.manroop.ReRe.pojos.yelpBusinesses.Business> yelp_business = new ArrayList<>();
    private List<com.example.manroop.ReRe.pojos.yelpBusinesses.Business> yelp_business1 = new ArrayList<>();
    private YelpBusiness yelpBusiness;
    private String address;

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    ResListAdapter adapter;
    public String res_key;
    EditText search;
    ListView res_list;
    TextView res_name;
    Firebase myFirebaseRef, refRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //testing token initialisation

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, this)
                    .build();
        }


        verifyPermissions();

        registerBroadcastReceiver();

        setupRecyclerView();


        //map elements from view
        res_name = (TextView) findViewById(R.id.text_res_name);
        res_list = (ListView) findViewById(R.id.res_list);
        search = (EditText) findViewById((R.id.searchBar));


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        location = locationManager.getLastKnownLocation(bestProvider);
        Latitude = (String.valueOf(location.getLatitude()));
        Longitude = (String.valueOf(location.getLongitude()));
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

//        Firebase.setAndroidContext(this);
//        myFirebaseRef = new Firebase("https://resplendent-heat-2353.firebaseio.com");
//        refRestaurants = myFirebaseRef.child("Restaurants");
//
//
//        res_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                rName = ((TextView) view.findViewById(R.id.text_res_name)).getText().toString();
//                rWaittime = ((Button) view.findViewById(R.id.btn_wait_time)).getText().toString();
//
//                //pass values through intent
//                Intent in = new Intent(getApplicationContext(), ActResDetails.class);
//                in.putExtra("res_name", rName);
//                in.putExtra("waittime", rWaittime);
//                startActivity(in);
//
//
//            }
//        });

//        adapter = new ResListAdapter(refRestaurants, this, R.layout.res_list);
//        res_list.setAdapter(adapter);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Main_Activity.this.adapter.getFilter().filter(cs);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toast.makeText(getApplicationContext(), "Finding you current location...", Toast.LENGTH_SHORT).show();


    }

    private void registerBroadcastReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("yelp");
        broadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.yelp_list);
        restaurantListAdapter = new RestaurantListAdapter(this, yelp_business1);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(restaurantListAdapter);
    }

    private void verifyPermissions() {

        permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this, String.valueOf(permissions)) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, permissions,
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }

    public void startyelp() {

        Intent in = new Intent(this, RestaurantIntentService.class);
        in.putExtra("latitude", Latitude);
        in.putExtra("longitude", Longitude);
        startService(in);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        getToken();
        LocalBroadcastManager.getInstance(this).registerReceiver(brAddressGpsList, new IntentFilter("addressList"));
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        unregisterReceiver(broadcastReceiver);

        super.onStop();

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


    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (location != null) {

        }

        Latitude = (String.valueOf(location.getLatitude()));
        Longitude = (String.valueOf(location.getLongitude()));
        Log.d(TAG, "onConnected: " + Latitude + " " + Longitude);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);


        Log.d(TAG, "onMapReady: " + Latitude + " " + Longitude);
        LatLng sydney = new LatLng(Double.parseDouble(Latitude), Double.parseDouble(Longitude));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(sydney, 15));


        if (location != null) {
            onLocationChanged(location);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        Latitude = String.valueOf(latitude);
        Longitude = String.valueOf(longitude);
        myLoc.setLongitude(longitude);
        myLoc.setLatitude(latitude);
        //Log.d(TAG, "onLocationChanged: " + Latitude + " " + Longitude);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

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
            // Handle the camera action
        } else if (id == R.id.nav_writeReview) {
            Intent in = new Intent(getApplicationContext(), ActMyReservations.class);
            startActivity(in);

        } else if (id == R.id.nav_myReservations) {
            Intent in = new Intent(getApplicationContext(), ActMyReservations.class);
            startActivity(in);

        } else if (id == R.id.nav_myAccount) {
            Intent in = new Intent(getApplicationContext(), ActAccount.class);
            startActivity(in);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void getToken() {

        Intent intent = new Intent(this, AccessTokenIntentService.class);
        intent.putExtra("type", "restaurants");
        startService(intent);

    }
    //Members for below method
    List<String> addressName;

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            addressName = new ArrayList<>();
            Bundle bundle = intent.getExtras();
            yelpBusiness = (YelpBusiness) bundle.getSerializable("data");
            yelp_business = yelpBusiness.getBusinesses();
            yelp_business1.addAll(yelp_business);
            restaurantListAdapter.notifyDataSetChanged();


            for (Business b : yelp_business) {
                addressName.add(b.getLocation().getDisplayAddress().toString());
            }
            getLatLongFromAddress(addressName);

        }


    }

    private void getLatLongFromAddress(List<String> address) {

        Intent intent = new Intent(this, GeocodingIntentService.class);
        intent.putStringArrayListExtra("address", (ArrayList<String>) address);
        startService(intent);
    }


    private BroadcastReceiver brAddressGpsList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "onReceive: ");
            LatLng resLatLng;
            List<Location> resLocationList = intent.getParcelableArrayListExtra("addresses");
            for(Location l : resLocationList){
                resLatLng = new LatLng(l.getLatitude(),l.getLongitude());
                mMap.addMarker(new MarkerOptions().position(resLatLng));
            }

        }
    };
}
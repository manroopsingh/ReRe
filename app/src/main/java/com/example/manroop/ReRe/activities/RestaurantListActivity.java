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
import android.support.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manroop.ReRe.R;
import com.example.manroop.ReRe.adapters.ResListAdapter;
import com.example.manroop.ReRe.adapters.RestaurantListAdapter;
import com.example.manroop.ReRe.pojos.User;
import com.example.manroop.ReRe.pojos.yelpBusinesses.Business;
import com.example.manroop.ReRe.pojos.yelpBusinesses.Coordinates;
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

public class RestaurantListActivity extends AppCompatActivity
        implements
        LocationListener,
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private static final int MY_PERMISSIONS_REQUEST_CODE = 10;
    private static final String TAG = "MainActivityTag";


    //User
    public static User user;

    public static String phone = "6508669800";

    private GoogleApiClient mGoogleApiClient;
    public static String Latitude = "37", Longitude = "-121";
    public static Location myLoc = new Location(LocationManager.NETWORK_PROVIDER);
    public static GoogleMap mMap;
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
    EditText searchBox;
    ListView res_list;
    TextView res_name;
    Firebase myFirebaseRef, refRestaurants;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        user = new User("Manroop Singh", "singh@manroop.com", "6508669800", "1050 Benton Street", "", "Santa Clara", "CA", "USA");

        res_name = (TextView) findViewById(R.id.text_res_name);
        res_list = (ListView) findViewById(R.id.res_list);
        searchBox = (EditText) findViewById((R.id.searchBar));
        verifyPermissions();

        setupGoogleMapAndApi();
        setupRecyclerView();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toast.makeText(getApplicationContext(), "Searching restaurants near you...", Toast.LENGTH_SHORT).show();


    }

    private void getCurrentLocation() {
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
        onLocationChanged(location);
    }

    private void setupGoogleMapAndApi() {
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
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_CONTACTS};

        if (ContextCompat.checkSelfPermission(this, String.valueOf(permissions)) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, permissions,
                    MY_PERMISSIONS_REQUEST_CODE);
        } else {
            getCurrentLocation();


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getCurrentLocation();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    getCurrentLocation();

                    Toast.makeText(this, "App can't work without location permission", Toast.LENGTH_SHORT).show();
                    finish();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

//             other 'case' lines to check for other
//             permissions this app might request
        }

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        LocalBroadcastManager.getInstance(this).registerReceiver(brAddressGpsList, new IntentFilter("addressList"));
        LocalBroadcastManager.getInstance(this).registerReceiver(brRestaurantList, new IntentFilter("restaurantList"));
        getToken();
        super.onStart();
    }

    @Override
    protected void onResume() {
//        getToken();
        super.onResume();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(brRestaurantList);
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


        //Log.d(TAG, "onMapReady: " + Latitude + " " + Longitude);
        LatLng sydney = new LatLng(Double.parseDouble(Latitude), Double.parseDouble(Longitude));
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

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));


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
            Intent in = new Intent(getApplicationContext(), MyReservationsActivity.class);
            startActivity(in);

        } else if (id == R.id.nav_myReservations) {
            Intent in = new Intent(getApplicationContext(), MyReservationsActivity.class);
            startActivity(in);

        } else if (id == R.id.nav_myAccount) {
            Intent in = new Intent(getApplicationContext(), UserAccountActivity.class);
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

    public void searchRestaurants(View view) {

        String search = searchBox.getText().toString();
        Intent intent = new Intent(this, RestaurantIntentService.class);
        intent.putExtra("search", search);
        intent.putExtra("latitude", Latitude);
        intent.putExtra("longitude", Longitude);
        startService(intent);
    }

    public BroadcastReceiver brRestaurantList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            yelp_business1.clear();
            yelp_business.clear();
            addressName = new ArrayList<>();
            Bundle bundle = intent.getExtras();
            yelpBusiness = (YelpBusiness) bundle.getSerializable("data");
            yelp_business = yelpBusiness.getBusinesses();

            yelp_business1.addAll(yelp_business);
            restaurantListAdapter.notifyDataSetChanged();

            List<Coordinates> coordinates = new ArrayList<>();
            LatLng resLatLng;


            for (Business b : yelp_business) {
                addressName.add(b.getLocation().getDisplayAddress().toString());
                Coordinates resLocation = b.getCoordinates();
                resLatLng = new LatLng(resLocation.getLatitude(), resLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(resLatLng).title(b.getName()));
            }

//            for(Coordinates c: coordinates)
//            {
//                resLatLng = new LatLng(c.getLatitude(), c.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(resLatLng).title());
//               // Log.d(TAG, "onReceive: Location" + c.getLongitude() + " " + c.getLongitude());
//
//            }
            //getLatLongFromAddress(addressName);
        }
    };

    private void getLatLongFromAddress(List<String> address) {

        Intent intent = new Intent(this, GeocodingIntentService.class);
        intent.putStringArrayListExtra("address", (ArrayList<String>) address);
        startService(intent);
    }


    private BroadcastReceiver brAddressGpsList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            LatLng resLatLng;
            List<Location> resLocationList = intent.getParcelableArrayListExtra("addresses");
            for (Location l : resLocationList) {
                resLatLng = new LatLng(l.getLatitude(), l.getLongitude());
                mMap.addMarker(new MarkerOptions().position(resLatLng));
            }

        }
    };

}
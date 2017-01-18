package com.example.manroop.ReRe.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.manroop.ReRe.R;
import com.example.manroop.ReRe.fragments.FragmentPartySize;
import com.example.manroop.ReRe.fragments.FragmentTablePicker;
import com.example.manroop.ReRe.fragments.ReservationDatePickerFragment;
import com.example.manroop.ReRe.fragments.ReservationTimePickerFragment;
import com.example.manroop.ReRe.pojos.reservation.Reservation1;
import com.example.manroop.ReRe.pojos.yelpBusinessDetail.BusinessDetail;
import com.example.manroop.ReRe.receivers.DelayedNotif;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yelp.clientlib.entities.Business;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.manroop.ReRe.activities.RestaurantListActivity.user;


public class MakeReservationActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        FragmentTablePicker.TablePicker,
        FragmentPartySize.PartySize {

    private static final String TAG = "MakeResvActivityTag";
    private TextView rdate, rtime, wait_time, rname;
    Firebase myFirebaseRef;
    private String name, email, phone, pushID, rName, genTime;
    int party, year, month, day, hour, minute, code;
    static final int REQUEST_SEND_SMS = 1;
    private CountDownTimer countDownTimer;
    LinearLayout layout_timer;
    Button share;
    ShareDialog shareDialog;
    Switch facebook;
    int timer = 0;
    Calendar cl;

    private String dbTime,dbDate,dbgenTime;
    private int dbPartySize, dbTableNo;


    private EditText enterCode;
    private Business business;
    private com.example.manroop.ReRe.pojos.yelpBusinesses.Business businessV3;
    private BusinessDetail businessDetail;
    private TextView reviews, distance, category, price;
    private RatingBar ratingBar;
    private LinearLayout linearDate, linearTime, linearTable, linearParty, linearReserve;

    private String bizName, bizPrice, bizCategory, bizID, bizReviewCount, bizDistance,time, date;
    private String btnStringDate, btnStringTime,btnStringTable, btnStringParty;
    private TextView tableNo,partySize;
    private Double bizRating;
    private Button sendCode;
    private Button saveButton;


    private TextView btnDate,btnTime,btnTable,btnParty;


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.SEND_SMS
    };

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_SEND_SMS
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, String.valueOf(code), null, null);
                } else {
                    System.out.println("Texts can't be sent.");
                }
                return;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Reservation");
        FacebookSdk.sdkInitialize(getApplicationContext());

        //get intent
        Intent intent = getIntent();

        //map elements
        enterCode = (EditText) findViewById(R.id.enterCode);
        facebook = (Switch) findViewById((R.id.switch_facebook));
        rdate = (TextView) findViewById(R.id.date);
        rtime = (TextView) findViewById(R.id.time);
        wait_time = (TextView) findViewById(R.id.text_timer);
        rname = (TextView) findViewById(R.id.reservationName);
        sendCode = (Button) findViewById(R.id.sendCode);
//        saveButton = (Button) findViewById(R.id.saveButton);

        btnDate = (TextView) findViewById(R.id.reservationBtnDate);
        btnTime = (TextView) findViewById(R.id.reservationBtnTime);
        btnTable = (TextView) findViewById(R.id.reservationBtnTable);
        btnParty = (TextView) findViewById(R.id.reservationBtnParty);
        tableNo = (TextView) findViewById(R.id.reservationTable);
        partySize = (TextView) findViewById(R.id.reservationParty);

        ratingBar = (RatingBar) findViewById(R.id.reservationRating);
        reviews = (TextView) findViewById(R.id.reservationReviews);
        distance = (TextView) findViewById(R.id.reservationDistance);
        category = (TextView) findViewById(R.id.reservationCategory);
        price = (TextView) findViewById(R.id.reservationPrice);
        linearDate = (LinearLayout) findViewById(R.id.linearDate);
        linearTime = (LinearLayout) findViewById(R.id.linearTime);
        linearParty = (LinearLayout) findViewById(R.id.linearParty);
        linearTable = (LinearLayout) findViewById(R.id.linearTable);
        linearReserve = (LinearLayout) findViewById(R.id.linearReserve);


        cl = Calendar.getInstance();
        cl.setTime(new Date());
        day = cl.get(Calendar.DAY_OF_MONTH);
        month = cl.get(Calendar.MONTH);
        year = cl.get(Calendar.YEAR);
        cl.set(Calendar.MINUTE, cl.get(Calendar.MINUTE) + timer);


        String tag = intent.getStringExtra("TAG");
        switch (tag) {
            case "restDetail":
                business = (Business) intent.getSerializableExtra("business");
                businessDetail = (BusinessDetail) intent.getSerializableExtra("businessDetail");

                btnStringDate = "Pick a date";
                btnStringTime = "Select time";

                bizID = businessDetail.getId();
                bizName = businessDetail.getName();
                bizPrice = businessDetail.getPrice();
                bizReviewCount = String.valueOf(businessDetail.getReviewCount()) + " reviews";
                bizID = businessDetail.getId();
                bizCategory = businessDetail.getCategories().get(0).getTitle();
                bizRating = businessDetail.getRating();
                bizDistance = "7.5 mi";
                time = "";
                date = "";
                break;
            case "restList":
                String tagResList = intent.getStringExtra("TAG_RES_LIST");
                businessV3 = (com.example.manroop.ReRe.pojos.yelpBusinesses.Business) intent.getSerializableExtra("businessV3");
                date = getDisplayDate(year, month, day);
                dbDate = String.valueOf(month+1) + "/"+ String.valueOf(day)+ "/" + String.valueOf(year);

                if (tagResList.equals("waittime")) {
                    timer = Integer.parseInt(intent.getStringExtra("timer"));
                    cl.set(Calendar.MINUTE, cl.get(Calendar.MINUTE) + timer);
                    hour = cl.get(Calendar.HOUR_OF_DAY);
                    minute = cl.get(Calendar.MINUTE);
                    time = getDisplayTime(hour, minute);
                    dbTime = String.valueOf(hour) + String.valueOf(minute);
                }
                if (tagResList.equals("fixedtime")) {
                    hour = intent.getIntExtra("hour",0);
                    minute = intent.getIntExtra("minute",0);
                    time = getDisplayTime(hour,minute);
                    dbTime = String.valueOf(hour) + String.valueOf(minute);
                }

                btnStringDate = "Change date";
                btnStringTime = "Change time";

                bizID = businessV3.getId();
                bizName = businessV3.getName();
                bizPrice = businessV3.getPrice();
                bizReviewCount = String.valueOf(businessV3.getReviewCount()) + " reviews";
                bizID = businessV3.getId();
                bizCategory = businessV3.getCategories().get(0).getTitle();
                bizRating = businessV3.getRating();

                break;



        }
        btnStringParty = "Select party size";
        btnStringTable = "Select table";

        rdate.setText(date);
        rtime.setText(time);
        rname.setText(bizName);
        ratingBar.setRating(bizRating.floatValue());
        reviews.setText(bizReviewCount);
        category.setText(bizCategory);
        distance.setText(bizDistance);
        category.setText(bizCategory);
        price.setText(bizPrice);
        btnDate.setText(btnStringDate);
        btnTime.setText(btnStringTime);
        btnTable.setText(btnStringTable);
        btnParty.setText(btnStringParty);
        partySize.setText("");
        tableNo.setText("No table selected");


        shareDialog = new ShareDialog(this);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new

                FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                });



        linearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        linearTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        linearParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = MakeReservationActivity.this.getFragmentManager();
                DialogFragment newFragment = new FragmentPartySize();
                newFragment.show(fm, "partySizePicker");
            }
        });

        linearTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = MakeReservationActivity.this.getFragmentManager();
                DialogFragment newFragment = new FragmentTablePicker();
                newFragment.show(fm, "tablePicker");
            }
        });


        layout_timer = (LinearLayout) findViewById(R.id.layout_timer);
        layout_timer.setVisibility(View.GONE);

        if (timer != 0) {
            layout_timer.setVisibility(View.VISIBLE);
            long startTime = timer * 60 * 1000;
            countDownTimer = new CountDownTimer(startTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    wait_time.setText("" + String.format("%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }

                @Override
                public void onFinish() {

                    wait_time.setText("Book now!!");
                }
            }.start();
        }



        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EditText nameField = (EditText) findViewById(R.id.enterName);
//                EditText emailField = (EditText) findViewById(R.id.enterEmail);
//                EditText phoneField = (EditText) findViewById(R.id.enterPhone);
//                EditText partyField = (EditText) findViewById(R.id.enterParty);
//
//                name = nameField.getText().toString();
//                email = emailField.getText().toString();
//                phone = phoneField.getText().toString();
//                party = Integer.parseInt(partyField.getText().toString());
//
//                if (name != null && email != null && phone != null && party != 0) {
//                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                        Toast.makeText(getApplicationContext(), "Please enter a valid email.", Toast.LENGTH_SHORT).show();
//                    } else if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
//                        Toast.makeText(getApplicationContext(), "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
//                    } else if (party > 10) {
//                        Toast.makeText(getApplicationContext(), "We can not take reservations for over 10 people. Please call the restaurant.", Toast.LENGTH_LONG).show();
//                    } else {
//                        verifyStoragePermissions(MakeReservationActivity.this);
//                        SmsManager smsManager = SmsManager.getDefault();
//                        code = (int) (Math.random() * 100001) + 1;
//                        System.out.println("This is the verification code: " + code);
//                        smsManager.sendTextMessage(phone, null, String.valueOf(code), null, null);
//                        Toast.makeText(getApplicationContext(), "Verification code has been sent.", Toast.LENGTH_SHORT).show();
//                        nameField.setKeyListener(null);
//                        emailField.setKeyListener(null);
//                        phoneField.setKeyListener(null);
//                        partyField.setKeyListener(null);
//                        saveButton.setEnabled(true);
//                        enterCode.setVisibility(View.VISIBLE);
//                        enterCode.setEnabled(true);
//                    }
//                }

                SmsManager smsManager = SmsManager.getDefault();
                code = (int) (Math.random() * 100001) + 1;
                System.out.println("This is the verification code: " + code);
                smsManager.sendTextMessage(user.getPhone(), null, String.valueOf(code), null, null);
                enterCode.setVisibility(View.VISIBLE);
                enterCode.setEnabled(true);
            }
        });


        linearReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushToFirebase();

                Snackbar.make(v, "Reservation request sent!", Snackbar.LENGTH_SHORT).show();


                Intent myIntent = new Intent(MakeReservationActivity.this, DelayedNotif.class);
                myIntent.putExtra("restName", bizName);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MakeReservationActivity.this, 0, myIntent, 0);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month );
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour - 1);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND,0);
                Log.d(TAG, "onClick: " + calendar.toString());
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);



            }
        });
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int id = 12345;
//
//                Notification.Builder notification = new Notification.Builder(MakeReservationActivity.this)
//                        .setContentTitle("ReRe")
//                        .setContentText("Reservation at " + bizName + " confirmed")
//                        .setSmallIcon(R.drawable.logo);
//
//                Intent notificationIntent = new Intent(getApplicationContext(), MyReservationsActivity.class);
//
//                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//
//                notification.setContentIntent(contentIntent);
//                notification.setAutoCancel(true);
//                notification.setLights(Color.BLUE, 500, 500);
//                long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
//                notification.setVibrate(pattern);
//
//                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                notification.setSound(alarmSound);
//                NotificationManager notificationManager = (NotificationManager) getSystemService(
//                        Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(id, notification.build());
//



//                Intent myIntent = new Intent(MakeReservationActivity.this, DelayedNotif.class);
//                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(MakeReservationActivity.this, 0, myIntent, 0);
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.YEAR, year);
//                calendar.set(Calendar.MONTH, month - 1);
//                calendar.set(Calendar.DAY_OF_MONTH, day);
//                calendar.set(Calendar.HOUR_OF_DAY, hour - 2);
//                calendar.set(Calendar.MINUTE, minute);
//
//                System.out.println(calendar.getTime());
//                System.out.println("sound the alarm");
//
//                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//                System.out.println("alarm sounded");
//
//                if (facebook.isChecked()) {
//                    if (ShareDialog.canShow(ShareLinkContent.class)) {
//
//                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                                .setContentTitle("Yay I got my table at " + bizName)
//                                .setContentDescription("Yolo people get your table reserved from this amazing new app")
//
//                                .setContentUrl(Uri.parse("http://www.yelp.com/san-jose"))
//                                .build();
//
//                        shareDialog.show(linkContent);
//                    }
//                }
//                finish();
//
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    public void showTimePickerDialog(View v) {
        FragmentManager fm = this.getFragmentManager();
        DialogFragment newFragment = new ReservationTimePickerFragment();
        newFragment.show(fm, "timePicker");
    }

    public void showDatePickerDialog(View v) {
        FragmentManager fm = this.getFragmentManager();
        DialogFragment newFragment = new ReservationDatePickerFragment();
        newFragment.show(fm, "datePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minutes) {
        this.hour = hour;
        this.minute = minutes;

        rtime.setText(getDisplayTime(hour, minutes));
        btnTime.setText("Change time");

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        String strDate = getDisplayDate(year, month, day);

        rdate.setText(strDate);
        btnDate.setText("Change date");

    }

    private String getDisplayDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy");
        return format.format(calendar.getTime());
    }

    public String getDisplayTime(int hour, int minutes) {

        int hours = hour;
        int minutesToDisplay = minutes;

        int hoursToDisplay = hours;

        if (hours > 12) {
            hoursToDisplay = hoursToDisplay - 12;
        }


        String minToDisplay = null;
        if (minutesToDisplay == 0) minToDisplay = "00";
        else if (minutesToDisplay < 10) minToDisplay = "0" + minutesToDisplay;
        else minToDisplay = "" + minutesToDisplay;

        String displayValue = hoursToDisplay + ":" + minToDisplay;

        if (hours < 12)
            displayValue = displayValue + " AM";
        else
            displayValue = displayValue + " PM";
        return displayValue;
    }

    @Override
    public void getTableNumber(int tableNumber) {
        dbTableNo = tableNumber;
        tableNo.setText(String.valueOf(tableNumber));
        tableNo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void getPartySize(int partySizeNumber) {

        partySize.setText(String.valueOf(partySizeNumber));
        dbPartySize = partySizeNumber;

    }

    public BroadcastReceiver brSMSListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();
                        enterCode.setText(message);
                    }
                }
            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);
            }
        }
    };

    @Override
    protected void onStart() {
        registerReceiver(brSMSListener, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        super.onStart();
    }


    public void pushToFirebase(){

        String time = new SimpleDateFormat("dd MMM, yyyy hh:mm a").format(new Date());
        dbgenTime = String.valueOf(time);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reservations = firebaseDatabase.getReference("reservation");

        Reservation1 reservation1 = new Reservation1(user.getEmail()
                , bizName, bizID, dbDate,dbTime, dbPartySize
                ,dbTableNo, "Yelp",
                "Pending", dbgenTime);

        String key = reservations.push().getKey();
        reservations.child(key).setValue(reservation1);
    }


    public void searchOpenTable(View view) {

        Intent intent = new Intent(this, OpenTableActivity.class);
        startActivity(intent);

    }

    public void inviteFriends(View view) {
        Intent intent = new Intent(this, InviteFriendsActivity.class);
        startActivity(intent);
    }
}

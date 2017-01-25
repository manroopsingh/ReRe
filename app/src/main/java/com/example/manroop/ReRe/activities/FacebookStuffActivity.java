package com.example.manroop.ReRe.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.manroop.ReRe.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FacebookStuffActivity extends AppCompatActivity {


    private static final String TAG = "fbStuffActivity";
    public static String ACCESS_TOKEN = "EAACEdEose0cBAEF5kZBKDSAvNsGgdfLXMk4XG9d3XHf5BTdFZBrHiIELhs42ZBtS6KpLwgerm2g0jYtc612dJWayx4XC9ZBHN0IyBn01zD9FRwQpfQjcue51xZAZCu7hAPp1iq0RNXSIxbWuGdjNLCy5JC1uwt9ZAP4GCkMhSVASG5qtYi6MZB4rxiO6outDwGYZD";

    ShareDialog shareDialog;
    EditText etContentTitle;
    EditText etContentURL;
    EditText etContentDesc;
    EditText etStatus;
    TextView tvResponse;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_stuff);

        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setIsDebugEnabled(true);

        Profile profile = Profile.getCurrentProfile();



        String name= profile.getName().toString();
        setTitle(name);
        AccessToken token = AccessToken.getCurrentAccessToken();

        Log.d(TAG, "onCreate: " + token.getToken());
        if (token != null) {
            if (token.isExpired()) {
                AccessToken.refreshCurrentAccessTokenAsync();
            }

        }

        Log.d(TAG, "onCreate: get" + getAccessToken());

        //mapping elements
        etContentTitle = (EditText) findViewById(R.id.etContentTitle);
        etContentDesc = (EditText) findViewById(R.id.etContentDescription);
        etContentURL = (EditText) findViewById(R.id.etContentUrl);
        etStatus = (EditText) findViewById(R.id.etStatus);
        tvResponse = (TextView) findViewById(R.id.tvResponse);
        imageView = (ImageView) findViewById(R.id.imageView);

        Glide.with(this).load(profile.getProfilePictureUri(50,50)).into(imageView);

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

    }

    public void fbSharelink(View view) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(etContentTitle.getText().toString())
                    .setContentDescription(etContentDesc.getText().toString())
                    .setContentUrl(Uri.parse("http://www." + etContentURL.getText().toString()))
                    .build();

            shareDialog.show(linkContent);
        }

    }

    public void fbSharePhoto(View view) {

        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);

    }

    public void fbShareStatus(final View view) {

        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("graph.facebook.com")
                .addPathSegment("me")
                .addPathSegment("feed")
                .addQueryParameter("access_token", ACCESS_TOKEN)

                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("message", etStatus.getText().toString())
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                Snackbar.make(view, "Status Posted!", Snackbar.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //TODO: update your UI
                            try {
                                tvResponse.setText(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                if (ShareDialog.canShow(ShareLinkContent.class)) {

                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(bitmap)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();

                    shareDialog.show(content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void fbGetFriends(View view) {

        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("graph.facebook.com")
                .addPathSegment("me")
                .addPathSegment("friends")
                .addQueryParameter("access_token", ACCESS_TOKEN)

                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //TODO: update your UI
                            try {
                                tvResponse.setText(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                }

            }
        });

    }
    String resp = "";

    private String getAccessToken(){
        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("graph.facebook.com")
                .addPathSegment("oauth")
                .addPathSegment("access_token")
                .addQueryParameter("grant_type", "fb_exchange_token&amp")
                .addQueryParameter("client_id", this.getString(R.string.facebook_app_id)+ "&amp")
                .addQueryParameter("client_secret","f42725cdaa986db4624fb04c9515db5f" + "&amp")
                .addQueryParameter("fb_exchange_token","long-lived-token")
                .build();


        Request request = new Request.Builder()
                .url(httpUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                resp = response.body().string();
            }
        });


        return resp;
    }

    public void logOut(View view) {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, FBLoginActivity.class);
        startActivity(intent);

    }
}

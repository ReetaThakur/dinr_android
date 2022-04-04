package com.godynamo.dinr.controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.widget.VideoView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.api.EndPointUrl;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.ui.ErrorFinishDialog;

import org.json.JSONObject;

import java.util.TimeZone;

public class ActivitySplash extends BaseDinrActivity {

    private VideoView backgroundVideo;

    private Context context;

    boolean restaurantLoaded = false;
    boolean userLoaded = false;

    final static int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 11;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimeZone.setDefault(TimeZone.getTimeZone("America/Montreal"));

        context = this;

        if (!cd.isConnectingToInternet()) {
            new ErrorFinishDialog(context, "Unable to Connect", "Please try again when you have access to the internet!");
        }

        backgroundVideo = (VideoView) findViewById(R.id.video_background);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.sing_in_background;

        try {
            backgroundVideo.setVideoURI(Uri.parse(path));

            backgroundVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    mp.setVolume(0f, 0f);
                }
            });

            backgroundVideo.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("Splash ", "onCreate: perm");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        } else {
            Log.e("Splash ", "onCreate: perm ok");

            prepareApp();
        }
    }

    private void prepareApp() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                wrapper.getCities();
            }
        }.execute();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    prepareApp();
                } else {
                    finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (backgroundVideo.isPlaying()) {
            backgroundVideo.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!backgroundVideo.isPlaying()) {
            backgroundVideo.resume();
        }
    }

    @Override
    public void onSuccess(JSONObject obj, String event) {

        Log.e("Splash ", "onCreate: perm success");
        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_GET_CLOCEST_CITY)) {
            wrapper.Restaurants();
        }

        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_GET_CITIES)) {
            wrapper.getClosestCity();
        }

        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_GET_ALL_RESTAURANTS)) {
            restaurantLoaded = true;
            try {
                wrapper.getPaymentInfo(context.getSharedPreferences(DinrSession.PREFS_NAME, 0).getInt(DinrSession.USER_ID_PREF_NAME, 0));
            } catch (Exception e) {
                e.printStackTrace();
            }

            loadApplication();
        }
    }

    @Override
    public void onFailure(JSONObject errorResponse, String event) {

        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_LOAD_USER)) {
            context.getSharedPreferences(DinrSession.PREFS_NAME, 0).edit().clear().commit();
            loadApplication();
        }

    }

    private void loadApplication() {

        if (restaurantLoaded) {
            Intent intent = new Intent(ActivitySplash.this, ActivityMain.class);
            startActivity(intent);
            finish();
        }

    }


}


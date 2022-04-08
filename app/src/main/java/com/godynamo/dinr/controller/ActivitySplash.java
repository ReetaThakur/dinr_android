package com.godynamo.dinr.controller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.api.EndPointUrl;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.ui.ErrorFinishDialog;

import org.json.JSONObject;

import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.jvm.internal.Intrinsics;

public class ActivitySplash extends BaseDinrActivity {

    private VideoView backgroundVideo;

    private Context context;

    boolean restaurantLoaded = false;
    boolean userLoaded = false;

    final static int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 11;

    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 3000;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimeZone.setDefault(TimeZone.getTimeZone("America/Montreal"));

        context = this;

        backgroundVideo = (VideoView) findViewById(R.id.video_background);
        progressBar = findViewById(R.id.progress_bar);

        String path = "android.resource://" + getPackageName() + "/" + R.raw.sing_in_background;

        try {
            backgroundVideo.setVideoURI(Uri.parse(path));

            backgroundVideo.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                mp.setVolume(0f, 0f);
            });

            backgroundVideo.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!cd.isConnectingToInternet()) {
            new ErrorFinishDialog(context, "Unable to Connect", "Please try again when you have access to the internet!");
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            } else {
                prepareApp();
            }

        }

    }

    private void prepareApp() {

//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//
//                try {
//                    Log.e("Sam", "doInBackground: "+params);
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//                Log.e("Sam", "onPostExecute: "+result);
//                wrapper.getCities();
//            }
//        }.execute();


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                Thread.sleep(1000);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.VISIBLE);
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(() -> {
                wrapper.getCities();
            });
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (backgroundVideo.isPlaying()) {
            backgroundVideo.pause();
        }
    }


    @Override
    public void onSuccess(JSONObject obj, String event) {


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
        progressBar.setVisibility(View.VISIBLE);
        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_LOAD_USER)) {
            context.getSharedPreferences(DinrSession.PREFS_NAME, 0).edit().clear().commit();
            loadApplication();
        }

    }

    private void loadApplication() {

        if (restaurantLoaded) {
            Intent intent = new Intent(ActivitySplash.this, ActivityMainNew.class);
            startActivity(intent);
            finish();
        }

    }

    AlertDialog alert;

    private void showDialog() {


        runOnUiThread(() -> {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Before using the App you have to accept the Location Permission!")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        dialog.dismiss();
                        finish();
                    });
            alert = builder.create();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!backgroundVideo.isPlaying()) {
            backgroundVideo.resume();
        }


    }


    private boolean isPermissionGiven() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                prepareApp();
            } else {
                finish();
            }
        }
    }


}


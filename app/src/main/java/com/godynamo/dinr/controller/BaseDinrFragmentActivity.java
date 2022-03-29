package com.godynamo.dinr.controller;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.appboy.Appboy;
import com.godynamo.dinr.api.APICaller;
import com.godynamo.dinr.api.APIWrapper;
import com.godynamo.dinr.tools.ConnectionDetector;

import org.json.JSONObject;

/**
 * Created by dankovassev on 14-11-27.
 */
public class BaseDinrFragmentActivity extends FragmentActivity implements APICaller{

    protected Context _context;
    protected APIWrapper wrapper;
    protected ConnectionDetector cd;

    protected boolean mRefreshData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cd = new ConnectionDetector(getApplicationContext());


        wrapper = new APIWrapper(this, getApplicationContext(), this);

        _context = this;

    }

    @Override
    public void onSuccess(JSONObject obj, String event) {

    }

    @Override
    public void onFailure(JSONObject errorResponse, String event) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if(Appboy.getInstance(this).openSession(this)){
            mRefreshData = true;
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        Appboy.getInstance(this).closeSession(this);
    }
}

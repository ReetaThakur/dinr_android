package com.godynamo.dinr.controller;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appboy.Appboy;
//import com.appboy.ui.inappmessage.AppboyInAppMessageManager;
import com.braze.ui.inappmessage.BrazeInAppMessageManager;
import com.godynamo.dinr.R;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.model.City;
import com.godynamo.dinr.ui.DialogListAdapter;
import com.godynamo.dinr.ui.TabsPagerAdapter;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.listeners.OnLoginListener;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dankovassev on 15-01-26.
 */
public class ActivityMain extends BaseDinrFragmentActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private TextView signOutButton;

    private ListView lvForDialog;

    private Dialog dialogMarketList;

    public boolean location = false;

    private SimpleFacebook mSimpleFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Permission[] permissions = new Permission[] {
                Permission.EMAIL
        };


        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(this.getResources().getString(R.string.app_id))
                .setNamespace(DinrSession.PREFS_NAME)
                .setPermissions(permissions)
                .build();

        SimpleFacebook.setConfiguration(configuration);

        if(DinrSession.getInstance().getUser(this) != null) {
            Appboy.getInstance(this).changeUser("" + DinrSession.getInstance().getUser(this).getId());
            Appboy.getInstance(this).getCurrentUser().setEmail(DinrSession.getInstance().getUser(this).getEmail());
            Appboy.getInstance(this).getCurrentUser().setFirstName(DinrSession.getInstance().getUser(this).getFirstName());
            Appboy.getInstance(this).getCurrentUser().setFirstName(DinrSession.getInstance().getUser(this).getLastName());
            Appboy.getInstance(this).getCurrentUser().setFirstName(DinrSession.getInstance().getUser(this).getPhoneNumber());
        }

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);

        actionBar = getActionBar();

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        // Adding Tabs
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.icon_restaurant_yellow).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.icon_favorites_white).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.icon_map_white).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.icon_account_white).setTabListener(this));


        signOutButton = (TextView) actionBar.getCustomView().findViewById(R.id.sign_out_button);
        signOutButton.setVisibility(View.INVISIBLE);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


        if(DinrSession.getInstance().getSelectedCity() == null){

            if(DinrSession.getInstance().getCities(this) == null){

                Intent intent = new Intent(this, ActivitySplash.class);
                startActivity(intent);
                finish();

                return;
            }

            showListDialog(DinrSession.getInstance().getCities(this).getCities());
        }


        LocationManager lm = null;
        boolean gps_enabled = false,network_enabled = false;
        if(lm==null)
            lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){}
        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){

        }

        if(!gps_enabled && !network_enabled){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Please enable your location services!");
            dialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);

                    location = true;

                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();




        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                actionBar.setSelectedNavigationItem(position);
                RelativeLayout container = (RelativeLayout) actionBar.getCustomView();
                TextView title = (TextView) container.getChildAt(0);

                if (position == 0) { // Restaurants
                    actionBar.getTabAt(0).setIcon(R.drawable.icon_restaurant_yellow);
                    actionBar.getTabAt(1).setIcon(R.drawable.icon_favorites_white);
                    actionBar.getTabAt(2).setIcon(R.drawable.icon_map_white);
                    actionBar.getTabAt(3).setIcon(R.drawable.icon_account_white);

                    title.setText(_context.getString(R.string.restaurants));

                    signOutButton.setVisibility(View.INVISIBLE);

                }  else if (position == 1) { // Notify me
                    actionBar.getTabAt(0).setIcon(R.drawable.icon_restaurant_white);
                    actionBar.getTabAt(1).setIcon(R.drawable.icon_favorites_yellow);
                    actionBar.getTabAt(2).setIcon(R.drawable.icon_map_white);
                    actionBar.getTabAt(3).setIcon(R.drawable.icon_account_white);

                    title.setText("Notifications");

                    signOutButton.setVisibility(View.INVISIBLE);

                }else if (position == 2) { // map
                    actionBar.getTabAt(0).setIcon(R.drawable.icon_restaurant_white);
                    actionBar.getTabAt(1).setIcon(R.drawable.icon_favorites_white);
                    actionBar.getTabAt(2).setIcon(R.drawable.icon_map_yellow);
                    actionBar.getTabAt(3).setIcon(R.drawable.icon_account_white);

                    title.setText(_context.getString(R.string.map));

                    signOutButton.setVisibility(View.INVISIBLE);

                }else if (position == 3) { // Account

                    actionBar.getTabAt(0).setIcon(R.drawable.icon_restaurant_white);
                    actionBar.getTabAt(1).setIcon(R.drawable.icon_favorites_white);
                    actionBar.getTabAt(2).setIcon(R.drawable.icon_map_white);
                    actionBar.getTabAt(3).setIcon(R.drawable.icon_account_yellow);

                    title.setText(_context.getString(R.string.me));

                    if (_context.getSharedPreferences(DinrSession.PREFS_NAME, 0).contains(DinrSession.USER_ID_PREF_NAME)) {
                        signOutButton.setVisibility(View.VISIBLE);
                    } else {
                        signOutButton.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void updateMap(){
        if(mAdapter != null) {
            mAdapter.updateMap();
        }
    }

    public void refreshData(){
        if(mAdapter != null){
            mAdapter.updateRestaurants();
        }
    }

    public void fetchRestaurants(){
        if(mAdapter != null){
            mAdapter.fetchRestaurants();
        }
    }

    public void goToAccount(){
        actionBar.setSelectedNavigationItem(3);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onSuccess(JSONObject obj, String event) {

    }

    @Override
    public void onFailure(JSONObject errorResponse, String event) {

    }

    public void LoginFacebook(){
        OnLoginListener onLoginListener = new OnLoginListener() {
            @Override
            public void onLogin() {
                // change the state of the button or do whatever you want
                mAdapter.facebookLogin(mSimpleFacebook.getSession().getAccessToken());
            }

            @Override
            public void onNotAcceptingPermissions(Permission.Type type) {
                // user didn't accept READ or WRITE permission

            }

            @Override
            public void onThinking() {

            }

            @Override
            public void onException(Throwable throwable) {

            }

            @Override
            public void onFail(String s) {

            }
        };

        mSimpleFacebook.login(onLoginListener);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);

        BrazeInAppMessageManager.getInstance().registerInAppMessageManager(this);
        if (mRefreshData) {
           // Appboy.getInstance(this).requestInAppMessageRefresh();
            mRefreshData = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        BrazeInAppMessageManager.getInstance().unregisterInAppMessageManager(this);

    }

    private void logOut(){
        DinrSession.getInstance().logOut(this);



        Intent intent = new Intent(this, ActivitySplash.class);
        startActivity(intent);
        this.finish();

    }


    private void showListDialog(final ArrayList<City> cities){

        View viewList = this.getLayoutInflater().inflate(R.layout.dialog_list, null);
        dialogMarketList = new Dialog(this);
        dialogMarketList.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMarketList.setContentView(viewList);
        lvForDialog = (ListView) viewList.findViewById(R.id.listView);
        DialogListAdapter adapter = new DialogListAdapter(this, cities);
        lvForDialog.setAdapter(adapter);

        dialogMarketList.show();

        lvForDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DinrSession.getInstance().setSelectedCity(cities.get(i));

                updateMap();
                fetchRestaurants();
                refreshData();

                dialogMarketList.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 3 is equal to the Account Fragment
        // this hole system would need to be rebuild.
        if(viewPager.getCurrentItem() == 3) {
            mAdapter.OnBackFragmentAcount();
            return;
        }

        super.onBackPressed();
    }
}


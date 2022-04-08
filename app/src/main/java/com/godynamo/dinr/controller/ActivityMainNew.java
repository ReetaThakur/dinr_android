package com.godynamo.dinr.controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.appboy.Appboy;
import com.braze.ui.inappmessage.BrazeInAppMessageManager;
import com.godynamo.dinr.R;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.model.City;
import com.godynamo.dinr.ui.DialogListAdapter;
import com.godynamo.dinr.ui.TabsPagerAdapterNew;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class ActivityMainNew extends BaseDinrFragmentActivity {

    TextView tvHeading;
    private TextView signOutButton;
    private ListView lvForDialog;

    private Dialog dialogMarketList;

    public boolean location = false;

    TabsPagerAdapterNew mAdapter;

    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        mAdapter = new TabsPagerAdapterNew(this);

        initSession();
        initViews();
        setViewPager();
        showLocationDialog();
        handleCLickListener();
    }

    private void initSession() {
        if (DinrSession.getInstance().getUser(this) != null) {
            Appboy.getInstance(this).changeUser("" + DinrSession.getInstance().getUser(this).getId());
            Appboy.getInstance(this).getCurrentUser().setEmail(DinrSession.getInstance().getUser(this).getEmail());
            Appboy.getInstance(this).getCurrentUser().setFirstName(DinrSession.getInstance().getUser(this).getFirstName());
            Appboy.getInstance(this).getCurrentUser().setFirstName(DinrSession.getInstance().getUser(this).getLastName());
            Appboy.getInstance(this).getCurrentUser().setFirstName(DinrSession.getInstance().getUser(this).getPhoneNumber());
        }

        if (DinrSession.getInstance().getSelectedCity() == null) {

            if (DinrSession.getInstance().getCities(this) == null) {

                Intent intent = new Intent(this, ActivitySplash.class);
                startActivity(intent);
                finish();

                return;
            }

            showListDialog(DinrSession.getInstance().getCities(this).getCities());
        }

    }

    private void initViews() {
        signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setVisibility(View.INVISIBLE);
    }

    private void handleCLickListener() {
        signOutButton.setOnClickListener(v -> logOut());
    }

    private void setViewPager() {
        viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(mAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tvHeading = findViewById(R.id.custom_action_bar_title);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0: {
                    //tab.setText("Test 1");
                    // tab.setIcon(R.drawable.icon_restaurant_white);
                    break;
                }
                case 1: {
                    //tab.setText("Test 2");
                    //tab.setIcon(R.drawable.icon_favorites_white);
                    break;
                }
                case 2: {
                    //tab.setText("Test 3");
                    //tab.setIcon(R.drawable.icon_map_white);
                    break;
                }
            }
        }
        );
        tabLayoutMediator.attach();

        View view = LayoutInflater.from(ActivityMainNew.this).inflate(R.layout.customtab, null);
        ((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.icon_restaurant_yellow);

        View view2 = LayoutInflater.from(ActivityMainNew.this).inflate(R.layout.customtab, null);
        ((ImageView) view2.findViewById(R.id.icon)).setImageResource(R.drawable.icon_favorites_white);


        View view3 = LayoutInflater.from(ActivityMainNew.this).inflate(R.layout.customtab, null);
        ((ImageView) view3.findViewById(R.id.icon)).setImageResource(R.drawable.icon_map_white);

        View view4 = LayoutInflater.from(ActivityMainNew.this).inflate(R.layout.customtab, null);
        ((ImageView) view3.findViewById(R.id.icon)).setImageResource(R.drawable.icon_account_white);


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {


                    ((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.icon_restaurant_yellow);
                    ((ImageView) view2.findViewById(R.id.icon)).setImageResource(R.drawable.icon_favorites_white);
                    ((ImageView) view3.findViewById(R.id.icon)).setImageResource(R.drawable.icon_map_white);
                    ((ImageView) view4.findViewById(R.id.icon)).setImageResource(R.drawable.icon_account_white);

                    tabLayout.getTabAt(0).setCustomView(view);
                    tabLayout.getTabAt(1).setCustomView(view2);
                    tabLayout.getTabAt(2).setCustomView(view3);
                    tabLayout.getTabAt(3).setCustomView(view4);

                    tvHeading.setText(getResources().getString(R.string.restaurants));

                    signOutButton.setVisibility(View.INVISIBLE);

                } else if (position == 1) {

                    ((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.icon_restaurant_white);
                    ((ImageView) view2.findViewById(R.id.icon)).setImageResource(R.drawable.icon_favorites_yellow);
                    ((ImageView) view3.findViewById(R.id.icon)).setImageResource(R.drawable.icon_map_white);
                    ((ImageView) view4.findViewById(R.id.icon)).setImageResource(R.drawable.icon_account_white);

                    tabLayout.getTabAt(0).setCustomView(view);
                    tabLayout.getTabAt(1).setCustomView(view2);
                    tabLayout.getTabAt(2).setCustomView(view3);
                    tabLayout.getTabAt(3).setCustomView(view4);

                    tvHeading.setText(getResources().getString(R.string.notifications));

                    signOutButton.setVisibility(View.INVISIBLE);

                } else if (position == 2) {

                    ((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.icon_restaurant_white);
                    ((ImageView) view2.findViewById(R.id.icon)).setImageResource(R.drawable.icon_favorites_white);
                    ((ImageView) view3.findViewById(R.id.icon)).setImageResource(R.drawable.icon_map_yellow);
                    ((ImageView) view4.findViewById(R.id.icon)).setImageResource(R.drawable.icon_account_white);

                    tabLayout.getTabAt(0).setCustomView(view);
                    tabLayout.getTabAt(1).setCustomView(view2);
                    tabLayout.getTabAt(2).setCustomView(view3);
                    tabLayout.getTabAt(3).setCustomView(view4);

                    tvHeading.setText(getResources().getString(R.string.map));

                    signOutButton.setVisibility(View.INVISIBLE);
                } else if (position == 3) {

                    ((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.icon_restaurant_white);
                    ((ImageView) view2.findViewById(R.id.icon)).setImageResource(R.drawable.icon_favorites_white);
                    ((ImageView) view3.findViewById(R.id.icon)).setImageResource(R.drawable.icon_map_white);
                    ((ImageView) view4.findViewById(R.id.icon)).setImageResource(R.drawable.icon_account_yellow);

                    tabLayout.getTabAt(0).setCustomView(view);
                    tabLayout.getTabAt(1).setCustomView(view2);
                    tabLayout.getTabAt(2).setCustomView(view3);
                    tabLayout.getTabAt(3).setCustomView(view4);

                    tvHeading.setText(getResources().getString(R.string.me));

                    if (_context.getSharedPreferences(DinrSession.PREFS_NAME, 0).contains(DinrSession.USER_ID_PREF_NAME)) {
                        signOutButton.setVisibility(View.VISIBLE);
                    } else {
                        signOutButton.setVisibility(View.INVISIBLE);
                    }

                }
            }

        });
    }

    private void showLocationDialog() {
        LocationManager lm = null;
        boolean gps_enabled = false, network_enabled = false;
        if (lm == null)
            lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Please enable your location services!");
            dialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
    }

    public void updateMap() {
        if (mAdapter != null) {
            mAdapter.updateMap();
        }
    }

    public void refreshData() {
        if (mAdapter != null) {
            mAdapter.updateRestaurants();
        }
    }

    public void fetchRestaurants() {
        if (mAdapter != null) {
            mAdapter.fetchRestaurants();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // mSimpleFacebook = SimpleFacebook.getInstance(this);

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

    private void logOut() {
        DinrSession.getInstance().logOut(this);
        Intent intent = new Intent(this, ActivitySplash.class);
        startActivity(intent);
        this.finish();

    }

    private void showListDialog(final ArrayList<City> cities) {

        View viewList = this.getLayoutInflater().inflate(R.layout.dialog_list, null);
        dialogMarketList = new Dialog(this);
        dialogMarketList.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMarketList.setContentView(viewList);
        lvForDialog = (ListView) viewList.findViewById(R.id.listView);
        DialogListAdapter adapter = new DialogListAdapter(this, cities);
        lvForDialog.setAdapter(adapter);

        dialogMarketList.show();

        lvForDialog.setOnItemClickListener((adapterView, view, i, l) -> {
            DinrSession.getInstance().setSelectedCity(cities.get(i));

            updateMap();
            fetchRestaurants();
            refreshData();

            dialogMarketList.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        // 3 is equal to the Account Fragment
        // this hole system would need to be rebuild.
        if (viewPager2.getCurrentItem() == 3) {
            mAdapter.OnBackFragmentAcount();
            return;
        }

        super.onBackPressed();
    }

    public void LoginFacebook() {
      /*  OnLoginListener onLoginListener = new OnLoginListener() {
            @Override
            public void onLogin() {
                // change the state of the button or do whatever you want
               // mAdapter.facebookLogin(mSimpleFacebook.getSession().getAccessToken());
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

        mSimpleFacebook.login(onLoginListener);*/

    }


    public void goToAccount() {
        // actionBar.setSelectedNavigationItem(3);
    }


}
package com.godynamo.dinr.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.godynamo.dinr.controller.FragmentAcount;
import com.godynamo.dinr.controller.FragmentMap;
import com.godynamo.dinr.controller.FragmentNotifyRestaurant;
import com.godynamo.dinr.controller.FragmentRestaurant;

/**
 * Created by dankovassev on 15-01-26.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter{

    FragmentMap mapFragment;
    FragmentAcount accountFragment;
    FragmentRestaurant restaurantFragment;
    FragmentNotifyRestaurant notifyRestaurantFragment;

    public TabsPagerAdapter(FragmentManager fm){
        super(fm);
        notifyRestaurantFragment = new FragmentNotifyRestaurant();
        restaurantFragment = new FragmentRestaurant();
        accountFragment = new FragmentAcount();

    }

    @Override
    public Fragment getItem(int index){
        switch (index){
            case 0:
                restaurantFragment = new FragmentRestaurant();
                return restaurantFragment;
            case 1:
                notifyRestaurantFragment = new FragmentNotifyRestaurant();
                return notifyRestaurantFragment;
            case 2:
                mapFragment = new FragmentMap();
                return mapFragment;
            case 3:
                accountFragment = new FragmentAcount();
                return accountFragment;
        }

        return null;
    }


    @Override
    public int getCount(){
        return 4;
    }

    public void updateMap(){
        if(mapFragment != null) {
            mapFragment.updateMarkers();
        }
    }

    public void updateRestaurants(){
        if(restaurantFragment != null) {
            restaurantFragment.refreshData();
        }
    }


    public void fetchRestaurants(){
        if(restaurantFragment != null){
            restaurantFragment.fetchRestaurants();
        }
    }

    public void facebookLogin(String token){
        if(accountFragment != null){
            accountFragment.facebookLoggedIn(token);
        }
    }

    public void OnBackFragmentAcount(){
        if(accountFragment != null){
            accountFragment.OnBackPressed();
        }
    }

}

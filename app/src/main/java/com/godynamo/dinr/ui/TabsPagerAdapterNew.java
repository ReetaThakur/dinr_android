package com.godynamo.dinr.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.godynamo.dinr.controller.FragmentAcount;
import com.godynamo.dinr.controller.FragmentMap;
import com.godynamo.dinr.controller.FragmentNotifyRestaurant;
import com.godynamo.dinr.controller.FragmentRestaurant;


public class TabsPagerAdapterNew extends FragmentStateAdapter {

    FragmentMap mapFragment;
    FragmentAcount accountFragment;
    FragmentRestaurant restaurantFragment;
    FragmentNotifyRestaurant notifyRestaurantFragment;

    public TabsPagerAdapterNew(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        notifyRestaurantFragment = new FragmentNotifyRestaurant();
        restaurantFragment = new FragmentRestaurant();
        accountFragment = new FragmentAcount();
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
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
    public int getItemCount() {
        return 4;
    }

    public void updateMap() {
        if (mapFragment != null) {
            mapFragment.updateMarkers();
        }
    }


    public void updateRestaurants() {
        if (restaurantFragment != null) {
            restaurantFragment.refreshData();
        }
    }

    public void fetchRestaurants() {
        if (restaurantFragment != null) {
            restaurantFragment.fetchRestaurants();
        }
    }

    public void facebookLogin(String token) {
        if (accountFragment != null) {
            accountFragment.facebookLoggedIn(token);
        }
    }

    public void OnBackFragmentAcount() {
        if (accountFragment != null) {
            accountFragment.OnBackPressed();
        }
    }

}

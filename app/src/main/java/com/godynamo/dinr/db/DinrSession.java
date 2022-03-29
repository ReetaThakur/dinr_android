package com.godynamo.dinr.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.godynamo.dinr.model.Cities;
import com.godynamo.dinr.model.City;
import com.godynamo.dinr.model.NotifyRestaurants;
import com.godynamo.dinr.model.PaymentInfo;
import com.godynamo.dinr.model.Restaurant;
import com.godynamo.dinr.model.Restaurants;
import com.godynamo.dinr.model.User;
import com.google.gson.Gson;

import java.util.List;
public class DinrSession {

    static public final String PREFS_NAME = "com.dinr.application";

    static public final String USER_ID_PREF_NAME = "user_id";

    static public final String USER_PREF_NAME = "user_pref";
    static public final String CITY_PREF_NAME = "city_pref";


    private Restaurants restaurants;

    private NotifyRestaurants notifyRestaurants;

    private PaymentInfo paymentInfo;

    private City selectedCity;

    public City getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(City selectedCity) {
        this.selectedCity = selectedCity;
    }

    public Cities getCities(Context context) {

        Gson gson = new Gson();

        if(context.getSharedPreferences(PREFS_NAME, 0).contains(CITY_PREF_NAME)) {
            return gson.fromJson(context.getSharedPreferences(PREFS_NAME, 0).getString(CITY_PREF_NAME, null), Cities.class);
        }else{
            return null;
        }

    }

    public void setCities(Context context, Cities cities) {

        Gson gson = new Gson();
        String citiesJsonString = gson.toJson(cities);

        context.getSharedPreferences(PREFS_NAME, 0).edit().putString(CITY_PREF_NAME, citiesJsonString).commit();

    }

    private DinrSession() {

    }

    public void logOut(Context context){
        restaurants = null;
        paymentInfo = null;

        SharedPreferences preferences = context.getSharedPreferences(DinrSession.PREFS_NAME, 0);
        preferences.edit().clear().commit();

    }

    public User getUser(Context context){

        Gson gson = new Gson();

        if(context.getSharedPreferences(PREFS_NAME, 0).contains(USER_PREF_NAME)) {
            return gson.fromJson(context.getSharedPreferences(PREFS_NAME, 0).getString(USER_PREF_NAME, null), User.class);
        }else{
            return null;
        }
    }

    public void setUser(Context context, User user){

        Gson gson = new Gson();
        String userJSONString = gson.toJson(user);

        context.getSharedPreferences(PREFS_NAME, 0).edit().putString(USER_PREF_NAME, userJSONString).commit();

    }

    public List<Restaurant> getRestaurants() {

        if(restaurants != null) {
            return restaurants.getRestaurants();
        }else{
            return null;
        }
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public void setRestaurants(Restaurants restaurants) {
        this.restaurants = restaurants;
    }

    public void setNotifyRestaurants(NotifyRestaurants notifyRestaurants) {
        this.notifyRestaurants = notifyRestaurants;
    }

    public NotifyRestaurants getNotifyRestaurants() {
        return notifyRestaurants;
    }


    public static DinrSession getInstance() {
        return FakeDBHolder.instance;
    }

    static public DinrSession create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of
        // the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, DinrSession.class);
    }

    private static class FakeDBHolder {
        private static final DinrSession instance = new DinrSession();
    }




}

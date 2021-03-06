package com.godynamo.dinr.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.model.Cities;
import com.godynamo.dinr.model.CloseCity;
import com.godynamo.dinr.model.NotifyRestaurants;
import com.godynamo.dinr.model.PaymentInfo;
import com.godynamo.dinr.model.Restaurants;
import com.godynamo.dinr.model.User;
import com.godynamo.dinr.tools.gsonUTCdateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

/*import org.apache.http.Header;
import org.apache.http.entity.StringEntity;*/
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;


//import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by dankovassev on 14-11-27.
 */
public class APIWrapper {

    private APICaller mainClass;
    private Context mContext;

    public APIWrapper(APICaller mClass, Context context, Activity activity) {
        mContext = context;
        mainClass = mClass;
    }

    public void cancelReservation(int restaurantID, int reservationID) throws JSONException, UnsupportedEncodingException {

        String path = String.format(EndPointUrl.DINR_RESTAURANT_CANCEL_RESERVATION, restaurantID, reservationID) + "?user_token=" + DinrSession.getInstance().getUser(mContext).getToken();

        DinrClient.put(path, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("cancelReservation onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response);
                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_RESTAURANT_CANCEL_RESERVATION);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                JSONObject jsonObj = new JSONObject();
                Log.e("cancelReservation onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_RESTAURANT_CANCEL_RESERVATION);
            }


        });
    }


    public void sendPaymentInfo(String userID, HttpEntity entity) throws JSONException, UnsupportedEncodingException {

        String path = String.format(EndPointUrl.DINR_CREATE_UPDATE_USER_PAYMENT, userID) + "?user_token=" + DinrSession.getInstance().getUser(mContext).getToken();

        DinrClient.post(path, entity, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("sendPaymentInfo onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response);

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_CREATE_UPDATE_USER_PAYMENT);

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("sendPaymentInfo onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_CREATE_UPDATE_USER_PAYMENT);

            }

        });
    }

    public void getLastReservation(int userID) throws JSONException, UnsupportedEncodingException {

        String path = String.format(EndPointUrl.DINR_USER_RETRIEVE_RESERVATION_TONIGHT, userID) + "?user_token=" + DinrSession.getInstance().getUser(mContext).getToken();

        DinrClient.get(path, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("getLastReservation onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response);

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_RETRIEVE_RESERVATION_TONIGHT);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("getLastReservation onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_RETRIEVE_RESERVATION_TONIGHT);

            }


        });
    }

    public void getPaymentInfo(int userID) throws JSONException, UnsupportedEncodingException {

        String path = String.format(EndPointUrl.DINR_RETRIEVE_USER_PAYMENT, userID) + "?user_token=" + DinrSession.getInstance().getUser(mContext).getToken();

        DinrClient.get(path, null, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    JSONObject user = (JSONObject) response.get("paymentInfo");
                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new gsonUTCdateAdapter()).create();
                    DinrSession.getInstance().setPaymentInfo(gson.fromJson(user.toString(), PaymentInfo.class));
                    Log.e("getPaymentInfo onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response + "\ngson" + gson);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_RETRIEVE_USER_PAYMENT);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("getPaymentInfo onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_RETRIEVE_USER_PAYMENT);

            }

        });
    }

    public void reserveRestaurant(String restaurantID, HttpEntity entity) throws JSONException, UnsupportedEncodingException {

        String path = String.format(EndPointUrl.DINR_RETRIVE_RESTAURANT_RESERVATIONS, "" + restaurantID) + "?user_token" + DinrSession.getInstance().getUser(mContext).getToken();


        DinrClient.post(path, entity, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("reserveRestaurant onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response);

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_RESTAURANT_RESERVATION);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("reserveRestaurant onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_RESTAURANT_RESERVATION);
            }
        });
    }

    public void signUpWithFacebook(HttpEntity entity) throws JSONException, UnsupportedEncodingException {

        String path = EndPointUrl.DINR_AUTHENTICATE_USER_FACEBOOK;
        DinrClient.post(path, entity, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONObject user = (JSONObject) response.get("user");

                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new gsonUTCdateAdapter()).create();
                    Log.e("signUpWithFacebook onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response + "\ngson" + gson);

                    DinrSession.getInstance().setUser(mContext, gson.fromJson(user.toString(), User.class));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_AUTHENTICATE_USER_FACEBOOK);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                // If no corresponding registration exists, the request will return 422 Unprocessable Entity, and an (incomplete) JSON representation of the registration.
                if (statusCode == 422) {
                    Log.e("signUpWithFacebook onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                    mainClass.onSuccess(errorResponse, EndPointUrl.DINR_EVENT_AUTHENTICATE_USER_FACEBOOK);

                } else {
                    Log.e("signUpWithFacebook onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                    // If the access-token doesn't work, the request will return 401 Unauthorized, with an empty JSON object in the response body.
                    mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_AUTHENTICATE_USER_FACEBOOK);
                }
            }
        });
    }

    //Resend Confirmation
    public void signInWithEmail(HttpEntity entity) throws JSONException, UnsupportedEncodingException {

        String path = EndPointUrl.DINR_AUTHENTICATE_USER;

        DinrClient.post(path, entity, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONObject user = (JSONObject) response.get("user");
                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new gsonUTCdateAdapter()).create();
                    DinrSession.getInstance().setUser(mContext, gson.fromJson(user.toString(), User.class));
                    Log.e("signInWithEmail onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response + "\ngson" + gson);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_AUTHENTICATE_USER_EMAIL);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("signInWithEmail onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_AUTHENTICATE_USER_EMAIL);
            }

        });
    }

    //Resend Confirmation
    public void loadUser(int id, String token) throws JSONException, UnsupportedEncodingException {
        String path = String.format(EndPointUrl.DINR_USER, "" + id);
        DinrClient.get(path, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONObject user = (JSONObject) response.get("user");
                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new gsonUTCdateAdapter()).create();
                    DinrSession.getInstance().setUser(mContext, gson.fromJson(user.toString(), User.class));
                    Log.e("loadUser onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response + "\ngson" + gson);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_LOAD_USER);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("loadUser onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_AUTHENTICATE_USER_FACEBOOK);
            }

        });
    }

    public void signUp(HttpEntity entity) throws JSONException, UnsupportedEncodingException {

        String path = EndPointUrl.DINR_CREATE_USER;
        DinrClient.post(path, entity, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONObject user = (JSONObject) response.get("user");
                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new gsonUTCdateAdapter()).create();
                    Log.e("signUp onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response + "\ngson" + gson);

                    DinrSession.getInstance().setUser(mContext, gson.fromJson(user.toString(), User.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_CREATE_USER);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("signUp onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_CREATE_USER);

            }

        });
    }

    public void resetPassword(HttpEntity entity) throws JSONException, UnsupportedEncodingException {

        String path = EndPointUrl.DINR_RESET_PASSWORD;

        DinrClient.post(path, entity, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("resetPassword onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response);

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_PASSWORD_RESET);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("resetPassword onFailure 1 ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_PASSWORD_RESET);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("resetPassword onFailure 2 ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + responseString);

                if (statusCode == 200) {
                    mainClass.onSuccess(null, EndPointUrl.DINR_EVENT_PASSWORD_RESET);
                } else {
                    mainClass.onFailure(null, EndPointUrl.DINR_EVENT_PASSWORD_RESET);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("resetPassword onFailure 3 ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

            }

        });
    }

    public void signOut() throws JSONException {

        String path = EndPointUrl.DINR_USER_SIGN_OUT + "?user_token=";

        DinrClient.delete(path, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("signOut onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response);

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_USER_SIGN_OUT);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("signOut onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_AUTHENTICATE_USER_FACEBOOK);

            }


        });
    }

    public void Restaurants() {
        String path = "";

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }

            if (location != null) {

                path = EndPointUrl.DINR_LIST_ALL_RESTAURANTS_CLOSEST + "?lat=" + location.getLatitude() + "&lng=" + location.getLongitude();
                Log.e("IN A ", "?lat=" + location.getLatitude() + "&lng=" + location.getLongitude());
            } else {
                path = EndPointUrl.DINR_LIST_ALL_RESTAURANTS_CLOSEST + "?lat=45.5288714" + "&lng=-73.6137561";
                Log.e("IN A ", "?lat=45.5288714" + "&lng=-73.6137561");
            }

            if (DinrSession.getInstance().getSelectedCity() != null) {
                path += "&city_id=" + DinrSession.getInstance().getSelectedCity().getId();
                Log.e("IN B ", "city_id" + DinrSession.getInstance().getSelectedCity().getId());
            }

            String finalPath = path;
            DinrClient.get(path, null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new gsonUTCdateAdapter()).create();
                    DinrSession.getInstance().setRestaurants(gson.fromJson(response.toString(), Restaurants.class));

                    Log.e("Restaurants onSuccess ", "path" + finalPath + "\nheaders " + Arrays.toString(headers) + " \nresponse " + response + " \ngson " + gson);
                    mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_GET_ALL_RESTAURANTS);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.e("Restaurants onFailure ", "path" + finalPath + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);
                    mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_GET_ALL_RESTAURANTS);
                }

            });
        }
    }

    public void getNotifyRestaurants(User user) {

        if (user == null) {

            mainClass.onFailure(null, EndPointUrl.DINR_EVENT_GET_NOTIFY_RESTAURANTS);

            return;
        }

        int userID = user.getId();

        String path = String.format(EndPointUrl.DINR_FETCH_FAVORITE_FOR_USER, userID) + "?user_token=" + DinrSession.getInstance().getUser(mContext).getToken();

        DinrClient.get(path, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new gsonUTCdateAdapter()).create();
                DinrSession.getInstance().setNotifyRestaurants(gson.fromJson(response.toString(), NotifyRestaurants.class));

                Log.e("getNotifyRestaurants onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response + "\ngson" + gson);

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_GET_NOTIFY_RESTAURANTS);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("getNotifyRestaurants onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);


                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_GET_NOTIFY_RESTAURANTS);

            }

        });
    }

    public void setFavoriteRestaurant(String userID, int restaurantID) {

        String path = String.format(EndPointUrl.DINR_FAVORITE_RESTAURANT, userID) + "?user_token=" + DinrSession.getInstance().getUser(mContext).getToken() + "&id=" + restaurantID;

        HttpEntity entity = new StringEntity("", HTTP.UTF_8);

        DinrClient.post(path, entity, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("setFavoriteRestaurant onSuccess 1 ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response);

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_SET_NOTIFY_RESTAURANT);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray arrayResponse) {
                Log.e("setFavoriteRestaurant onSuccess 2 ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + arrayResponse);

                mainClass.onSuccess(null, EndPointUrl.DINR_EVENT_SET_NOTIFY_RESTAURANT);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("setFavoriteRestaurant onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_SET_NOTIFY_RESTAURANT);

            }
        });

    }

    public void deleteFavoriteRestaurant(String userID, int restaurantID) {

        String path = String.format(EndPointUrl.DINR_UNFAVORITE_RESTAURANT, userID, restaurantID) + "?user_token=" + DinrSession.getInstance().getUser(mContext).getToken();

        DinrClient.delete(path, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("deleteFavoriteRestaurant onSuccess ", "path " + path + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response);

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_REMOVE_NOTIFY_RASTAURANT);

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("deleteFavoriteRestaurant onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_REMOVE_NOTIFY_RASTAURANT);

            }
        });
    }

    public void getCities() {
        String path = EndPointUrl.DINR_GET_CITIES;

        DinrClient.get(path, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new gsonUTCdateAdapter()).create();
                DinrSession.getInstance().setCities(mContext, gson.fromJson(response.toString(), Cities.class));
                Log.e("getCities onSuccess ", "path " + path + " \nheaders " + " \nresponse " + response + "\ngson" + gson);

                mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_GET_CITIES);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("getCities onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + " \nerrorResponse " + errorResponse);

                mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_GET_CITIES);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String string, Throwable throwable) {
                super.onFailure(statusCode, headers, string, throwable);
                Log.e("getCities onFailure ", "path" + path + "\nstatusCode" + statusCode + "\nheaders " + " \nerrorResponse " + string);
                mainClass.onFailure(null, EndPointUrl.DINR_EVENT_GET_CITIES);
            }
        });
    }

    public void getClosestCity() {

        String path = EndPointUrl.DINR_GET_CLOSEST_CITY;

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }

            if (location != null) {
                path += "?lat=" + location.getLatitude() + "&lng=" + location.getLongitude();
            } else {
                path += "?lat=45.5288714" + "&lng=-73.6137561";
            }

            String finalPath = path;
            DinrClient.get(path, null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new gsonUTCdateAdapter()).create();
                    DinrSession.getInstance().setSelectedCity(gson.fromJson(response.toString(), CloseCity.class).getCity());
                    Log.e("getClosestCity onSuccess ", "path " + finalPath + " \nheaders " + Arrays.toString(headers) + " \nresponse " + response + "\ngson" + gson);

                    mainClass.onSuccess(response, EndPointUrl.DINR_EVENT_GET_CLOCEST_CITY);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.e("getClosestCity onFailure ", "path" + finalPath + "\nstatusCode" + statusCode + "\nheaders " + Arrays.toString(headers) + " \nerrorResponse " + errorResponse);

                    mainClass.onFailure(errorResponse, EndPointUrl.DINR_EVENT_GET_CLOCEST_CITY);
                }

            });
        }
    }

}

package com.godynamo.dinr.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.api.EndPointUrl;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.model.Reservation;
import com.godynamo.dinr.tools.Utils;
import com.godynamo.dinr.tools.gsonUTCdateAdapter;
import com.godynamo.dinr.ui.CancelReservationDialog;
import com.godynamo.dinr.ui.ErrorDialog;
import com.godynamo.dinr.ui.SuccessDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by dankovassev on 2015-02-05.
 */
public class ActivityPreviousReservation extends BaseDinrActivity {

    private TextView restaurantName;
    private TextView restaurantAddress;
    private TextView reservationTime;
    private TextView reservationDetails;
    private TextView restaurantPhone;
    private TextView noReservationYet;
    private Button dialogButtonCancel;
    private String restaurantReservationTime = "";
    private String reservationDetail = "";
    private int reservationID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_reservation);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.action_bar_back_button);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.custom_action_bar_title);
        textView.setText(this.getResources().getString(R.string.tonights_reservation));

        ImageView backButton = (ImageView) actionBar.getCustomView().findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        restaurantName = (TextView) findViewById(R.id.restaurant_name);
        reservationTime = (TextView) findViewById(R.id.restaurant_reservation_time);
        reservationDetails = (TextView) findViewById(R.id.restaurant_reservation_details);
        restaurantAddress = (TextView) findViewById(R.id.restaurant_address);
        restaurantPhone = (TextView) findViewById(R.id.restaurant_phone);
        dialogButtonCancel = (Button) findViewById(R.id.dialogButtonCancelReservation);
        noReservationYet = (TextView) findViewById(R.id.no_reservation);


        restaurantName.setVisibility(View.INVISIBLE);
        reservationTime.setVisibility(View.INVISIBLE);
        reservationDetails.setVisibility(View.INVISIBLE);
        restaurantAddress.setVisibility(View.INVISIBLE);
        restaurantPhone.setVisibility(View.INVISIBLE);
        dialogButtonCancel.setVisibility(View.INVISIBLE);

        try {
            wrapper.getLastReservation(DinrSession.getInstance().getUser(this).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject obj, String event) {

        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_RETRIEVE_RESERVATION_TONIGHT)) {

            try {

                if (((JSONArray) obj.get("restaurant")).length() > 0) {

                    final JSONObject reservedRestaurant = ((JSONArray) obj.get("restaurant")).getJSONObject(0);
                    JSONArray reservation = (JSONArray) reservedRestaurant.get("reservations");

                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new gsonUTCdateAdapter()).create();
                    Reservation[] reservations = gson.fromJson(reservation.toString(), Reservation[].class);
                    ArrayList<Reservation> al = new ArrayList<>(Arrays.asList(reservations));

                    boolean reserved = false;

                    for (Reservation r : al) {

                        String startTime = null, endTime = null;
                        if (r.getStartTime() != null)
                            startTime = Utils.getTimeFromDate(r.getStartTime());

                        if (r.getEndTime() != null)
                            endTime = Utils.getTimeFromDate(r.getEndTime());

                        if (startTime != null && endTime != null)
                            restaurantReservationTime = startTime + " to " + endTime;
                        else if (startTime != null)
                            restaurantReservationTime = startTime;
                        /*if (r.getEndTime() == null) {
                            restaurantReservationTime = String.format("%02d", r.getStartTime().getHours()) + ":" + String.format("%02d", r.getStartTime().getMinutes());
                        } else {
                            restaurantReservationTime = String.format("%02d", r.getStartTime().getHours()) + ":" + String.format("%02d", r.getStartTime().getMinutes()) + " to " + String.format("%02d", r.getEndTime().getHours()) + ":" + String.format("%02d", r.getEndTime().getMinutes());
                        }*/

                        reservationDetail = r.getTable_detail();
                        reservationID = r.getId();
                        reserved = true;

                    }

                    if (reserved) {

                        restaurantName.setText(reservedRestaurant.getString("name"));
                        reservationTime.setText(restaurantReservationTime);
                        reservationDetails.setText(reservationDetail);
                        restaurantAddress.setText(reservedRestaurant.getString("streetAddress") + ", " + reservedRestaurant.getString("city") + ", " + reservedRestaurant.getString("provinceCode") + ", " + reservedRestaurant.getString("postalCode"));
                        restaurantPhone.setText(reservedRestaurant.getString("phoneNumber"));

                        restaurantName.setVisibility(View.VISIBLE);
                        reservationTime.setVisibility(View.VISIBLE);
                        reservationDetails.setVisibility(View.VISIBLE);
                        restaurantAddress.setVisibility(View.VISIBLE);
                        restaurantPhone.setVisibility(View.VISIBLE);

                        noReservationYet.setVisibility(View.GONE);

                        dialogButtonCancel.setVisibility(View.VISIBLE);
                        final Activity activity = this;

                        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    final int id = reservedRestaurant.getInt("id");
                                    new CancelReservationDialog(activity, id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        restaurantName.setVisibility(View.INVISIBLE);
                        reservationTime.setVisibility(View.INVISIBLE);
                        reservationDetails.setVisibility(View.INVISIBLE);
                        restaurantAddress.setVisibility(View.INVISIBLE);
                        restaurantPhone.setVisibility(View.INVISIBLE);
                        dialogButtonCancel.setVisibility(View.INVISIBLE);
                    }

                } else {

                    noReservationYet.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_RESTAURANT_CANCEL_RESERVATION)) {
            new SuccessDialog(this, this.getResources().getString(R.string.cancel_reservation), this.getResources().getString(R.string.your_reservation_has_been_canceled));
        }
    }


    @Override
    public void onFailure(JSONObject errorResponse, String event) {
        new ErrorDialog(_context, getString(R.string.error), errorResponse.toString());
    }

    public void cancelReservation(int id) {
        try {
            wrapper.cancelReservation(id, reservationID);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


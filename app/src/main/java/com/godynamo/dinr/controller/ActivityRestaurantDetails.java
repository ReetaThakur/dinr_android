package com.godynamo.dinr.controller;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.api.APIWrapper;
import com.godynamo.dinr.api.EndPointUrl;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.model.NotifyRestaurants;
import com.godynamo.dinr.model.Opening;
import com.godynamo.dinr.model.Restaurant;
import com.godynamo.dinr.ui.ConfirmReservationDialog;
import com.godynamo.dinr.ui.ConfirmReservationNumberDialog;
import com.godynamo.dinr.ui.ErrorDialog;
import com.godynamo.dinr.ui.RestaurantDetailsOpeningAdapter;
import com.godynamo.dinr.ui.RestaurantDetailsPagerAdapter;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dankovassev on 15-01-29.
 */
public class ActivityRestaurantDetails extends BaseDinrActivity {

    private ViewPager mViewPager;
    private RestaurantDetailsPagerAdapter adapter;
    private RestaurantDetailsOpeningAdapter openingAdapter;
    private ImageView backButton;
    private Button bookNowButton;
    private Context context;
    private int restaurantID;
    private ArrayList<Opening> openings;
    private ArrayList<Restaurant> restaurantsWithOpenHours;
    private String restaurantReservationTime;
    private NumberPicker numberPicker;
    private int selectedValue;
    private ImageView favorite;

    private Restaurant restaurant;

    private Boolean isFavorite = false;

    private com.godynamo.dinr.ui.CustomTextView noOpeningsTextView;

    private ImageView infoIcon;

    private com.godynamo.dinr.ui.CustomTextView noCreditCardTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant_details);

        context = this;

        Bundle extras = getIntent().getExtras();
        restaurantID = extras.getInt("clickedID");

        noOpeningsTextView = (com.godynamo.dinr.ui.CustomTextView) findViewById(R.id.no_opening_message);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.action_bar_favorite);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.custom_action_bar_title);

        infoIcon = (ImageView) findViewById(R.id.info_icon);

        noCreditCardTextView = (com.godynamo.dinr.ui.CustomTextView) findViewById(R.id.credit_card_missing_text);

        if (DinrSession.getInstance().getPaymentInfo() != null && DinrSession.getInstance().getPaymentInfo().getCreditCardNumber() != null) {
            noCreditCardTextView.setVisibility(View.GONE);
        } else {
            noCreditCardTextView.setVisibility(View.VISIBLE);
        }

        noCreditCardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityPayment.class);
                context.startActivity(intent);
            }
        });

        List<Restaurant> allRestaurants = DinrSession.getInstance().getRestaurants();

        for (Restaurant restaurant : allRestaurants) {
            if (restaurant.getId() == restaurantID) {
                this.restaurant = restaurant;
            }
        }

        backButton = (ImageView) actionBar.getCustomView().findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        favorite = (ImageView) actionBar.getCustomView().findViewById(R.id.favorite);
        bookNowButton = (Button) findViewById(R.id.book_now_button);

        if (DinrSession.getInstance().getNotifyRestaurants() != null) {

            List<Restaurant> notifyRestaurants = DinrSession.getInstance().getNotifyRestaurants().getRestaurants();

            for (Restaurant restaurant : notifyRestaurants) {
                if (restaurant.getId() == restaurantID) {
                    isFavorite = true;
                    this.restaurant = restaurant;
                }
            }
        }

        if (restaurant.getDescription() != null && !restaurant.getDescription().equalsIgnoreCase("")) {
            infoIcon.setVisibility(View.VISIBLE);
        } else {
            infoIcon.setVisibility(View.INVISIBLE);
        }

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_info);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.info_text);
                text.setText(restaurant.getDescription());

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonSelect);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        textView.setText(restaurant.getName());

        if (isFavorite) {
            favorite.setImageResource(R.drawable.icon_favorites_yellow);
        } else {
            favorite.setImageResource(R.drawable.icon_favorites_white);
        }

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DinrSession.getInstance().getPaymentInfo().getCreditCardNumber() == null) {
                    Intent intent = new Intent(context, ActivityPayment.class);
                    context.startActivity(intent);
                    return;
                }

                if (isFavorite) {
                    wrapper.deleteFavoriteRestaurant("" + DinrSession.getInstance().getUser(context).getId(), restaurant.getId());
                } else {
                    wrapper.setFavoriteRestaurant("" + DinrSession.getInstance().getUser(context).getId(), restaurant.getId());
                }
            }
        });

        adapter = new RestaurantDetailsPagerAdapter(this, restaurant.getPhotos());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);

        openings = new ArrayList<>();

        for (Opening o : restaurant.getOpenings()) {

            if (o.getState().equalsIgnoreCase("free")) {
                openings.add(o);
            }
        }

        if (openings.size() == 0) {
            noOpeningsTextView.setVisibility(View.VISIBLE);

            if (isFavorite) {
                bookNowButton.setText("Remove notification");
            } else {
                bookNowButton.setText("Notify me");
            }


        } else {
            noOpeningsTextView.setVisibility(View.INVISIBLE);
            bookNowButton.setText("Book now");

        }

        openingAdapter = new RestaurantDetailsOpeningAdapter(this, openings);
        ListView listView = (ListView) findViewById(R.id.openings_list);
        listView.setAdapter(openingAdapter);

        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DinrSession.getInstance().getPaymentInfo() == null || DinrSession.getInstance().getPaymentInfo().getCreditCardNumber() == null) {
                    Intent intent = new Intent(context, ActivityPayment.class);
                    context.startActivity(intent);
                    return;
                }

                int hourID = openingAdapter.getClickedID();

                if (hourID != -1) {

                    Opening opening = openings.get(hourID);

                    final String restaurantName = restaurant.getName();

                    restaurantReservationTime = "";

                    if (opening.getEnd_time() == null) {
                        restaurantReservationTime = String.format("%02d", opening.getStart_time().getHours()) + ":" + String.format("%02d", opening.getStart_time().getMinutes());
                    } else {
                        restaurantReservationTime = String.format("%02d", opening.getStart_time().getHours()) + ":" + String.format("%02d", opening.getStart_time().getMinutes()) + " to " + String.format("%02d", opening.getEnd_time().getHours()) + ":" + String.format("%02d", opening.getEnd_time().getMinutes());
                    }

                    final String restaurantReservationDetails = opening.getTable_type();
                    final String restaurantAddress = restaurant.getStreetAddress();
                    final String restaurantPhone = restaurant.getPhoneNumber();

                    final ConfirmReservationNumberDialog numberDialog = new ConfirmReservationNumberDialog(context);

                    Button select = (Button) numberDialog.findViewById(R.id.dialogButtonSelect);
                    Button cancelNumberDialog = (Button) numberDialog.findViewById(R.id.dialogButtonCancelDialog);

                    numberPicker = (NumberPicker) numberDialog.findViewById(R.id.number_picker);
                    setDividerColor(numberPicker, context.getResources().getColor(R.color.white));
                    numberPicker.setMaxValue(opening.getMax_seats());
                    numberPicker.setMinValue(opening.getMin_seats());
                    numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                    select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedValue = numberPicker.getValue();
                            new ConfirmReservationDialog(context,
                                    restaurantName,
                                    restaurantReservationTime,
                                    selectedValue + " @ " + restaurantReservationDetails,
                                    restaurantAddress,
                                    restaurantPhone);
                            numberDialog.dismiss();

                        }
                    });

                    cancelNumberDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            numberDialog.dismiss();
                        }
                    });

                    numberDialog.show();


                } else if (openings.size() == 0) {

                    if (isFavorite) {
                        wrapper.deleteFavoriteRestaurant("" + DinrSession.getInstance().getUser(context).getId(), restaurant.getId());
                    } else {
                        wrapper.setFavoriteRestaurant("" + DinrSession.getInstance().getUser(context).getId(), restaurant.getId());
                    }
                }
            }
        });
    }

    public void sendReservation() {
        int hourID = openingAdapter.getClickedOpeniingID();

        if (hourID != -1) {
            JSONObject jsonObject = new JSONObject();

            try {

                jsonObject.put("user_token", DinrSession.getInstance().getUser(this).getToken());
                jsonObject.put("opening_id", hourID);
                jsonObject.put("party_size", selectedValue);

                StringEntity entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);

                wrapper.reserveRestaurant("" + restaurantID, entity);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onSuccess(JSONObject obj, String event) {

        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_RESTAURANT_RESERVATION)) {
            Intent intent = new Intent(this, ActivityPreviousReservation.class);
            startActivity(intent);
            finish();
        }

        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_SET_NOTIFY_RESTAURANT)) {

            favorite.setImageResource(R.drawable.icon_favorites_yellow);

            isFavorite = true;

            if (isFavorite) {
                bookNowButton.setText("Remove notification");
            } else {
                bookNowButton.setText("Notify me");
            }


            wrapper.getNotifyRestaurants(DinrSession.getInstance().getUser(this));
        }

        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_REMOVE_NOTIFY_RASTAURANT)) {

            favorite.setImageResource(R.drawable.icon_favorites_white);

            isFavorite = false;
            if (isFavorite) {
                bookNowButton.setText("Remove notification");
            } else {
                bookNowButton.setText("Notify me");
            }

            wrapper.getNotifyRestaurants(DinrSession.getInstance().getUser(this));

        }

    }

    @Override
    public void onFailure(JSONObject errorResponse, String event) {

        try {
            new ErrorDialog(this, this.getString(R.string.error), errorResponse.get("error").toString());

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorDialog(this, this.getString(R.string.error), this.getString(R.string.oops_something_went_wrong));

        }
    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}

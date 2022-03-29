package com.godynamo.dinr.api;

import com.godynamo.dinr.BuildConfig;

/**
 * Created by dankovassev on 14-11-27.
 */
public interface EndPointUrl {

    //APIs
    String root = BuildConfig.URL_ROOT;

    String DINR_USER_ID = "/%1$s";

    String DINR_RESTAURANT_ID = "/%1$s";

    String DINR_USER = root +  "/users" + DINR_USER_ID;

    String DINR_GET_CITIES = root +"/cities";  //GET

    String DINR_GET_CLOSEST_CITY = root + "/cities/geosearches/closest";

    String DINR_RETRIEVE_USER_PAYMENT = DINR_USER +"/payment";  //GET

    String DINR_CREATE_UPDATE_USER_PAYMENT = DINR_USER +"/payment"; //POST

    String DINR_USER_RETRIEVE_RESERVATION_TONIGHT = DINR_USER + "/reservations/tonight";

    String DINR_USER_RETRIVE_RESERVATION_PAST = DINR_USER + "/reservations/past";

    String DINR_CREATE_USER = root + "/users";

    String DINR_AUTHENTICATE_USER = DINR_CREATE_USER + "/authenticate";

    String DINR_AUTHENTICATE_USER_FACEBOOK = DINR_CREATE_USER + "/fb_create";

    String DINR_USER_SIGN_OUT = DINR_USER +  "/sign_out";

    String DINR_RETRIVE_RESTAURANT = root + "/restaurants" + DINR_RESTAURANT_ID;

    String DINR_LIST_ALL_RESTAURANTS = root +  "/restaurants";

    String DINR_LIST_ALL_RESTAURANTS_CLOSEST = DINR_LIST_ALL_RESTAURANTS + "/geosearches/closest";

    String DINR_FAVORITE_RESTAURANT = DINR_USER + "/favorites";

    String DINR_FETCH_FAVORITE_FOR_USER = DINR_USER + "/favorites";

    String DINR_UNFAVORITE_RESTAURANT = DINR_USER + "/favorites/%2$s";

    String DINR_RESTAURANT_OPENINGS = DINR_RETRIVE_RESTAURANT +  "/openings";

    String DINR_RESTAURANT_CANCEL_RESERVATION = DINR_RETRIVE_RESTAURANT +  "/reservations/%2$s/cancel";

    String DINR_RETRIVE_RESTAURANT_RESERVATIONS = DINR_RETRIVE_RESTAURANT +  "/reservations";

    String DINR_RESET_PASSWORD = root + "/forgotten_passwords";


    //URLs
    String DINR_FAQ_URL = "http://getdinr.com/%@/globalized_pages/about";

    String DINR_TERMS_URL =  "http://getdinr.com/%@/globalized_pages/terms";

    String DINR_PRIVACY_URL =  "http://getdinr.com/%@/globalized_pages/privacy";


    //EVENTS
    String DINR_EVENT_LOAD_USER = "event_load_user";

    String DINR_EVENT_AUTHENTICATE_USER_EMAIL = "event_email_sign_in";

    String DINR_EVENT_AUTHENTICATE_USER_FACEBOOK = "event_email_sign_in";

    String DINR_EVENT_CREATE_USER = "event_create_user";

    String DINR_EVENT_PASSWORD_RESET = "event_password_reset";

    String DINR_EVENT_USER_SIGN_OUT = "event_user_sign_out";

    String DINR_EVENT_GET_ALL_RESTAURANTS = "event_get_all_restaurants";

    String DINR_EVENT_RESTAURANT_RESERVATION = "event_restaurant_reservation";

    String DINR_EVENT_RETRIEVE_USER_PAYMENT = "event_retrieve_user_payment";

    String DINR_EVENT_CREATE_UPDATE_USER_PAYMENT = "event_create_update_user_payment";

    String DINR_EVENT_RETRIEVE_RESERVATION_TONIGHT = "event_retrieve_reservation_tonight";

    String DINR_EVENT_RESTAURANT_CANCEL_RESERVATION = "event_restaurant_cancel_reservation";

    String DINR_EVENT_GET_CITIES = "event_get_cities";

    String DINR_EVENT_GET_CLOCEST_CITY = "event_get_closest_city";

    String DINR_EVENT_GET_NOTIFY_RESTAURANTS = "event_get_favorite_restaurants";

    String DINR_EVENT_SET_NOTIFY_RESTAURANT = "event_set_favorite_restaurant";

    String DINR_EVENT_REMOVE_NOTIFY_RASTAURANT = "event_remove_favorite_restaurants";


}

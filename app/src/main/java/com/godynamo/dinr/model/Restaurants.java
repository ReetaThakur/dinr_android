package com.godynamo.dinr.model;

import java.util.List;

/**
 * Created by dankovassev on 15-01-28.
 */
public class Restaurants {

    private List<Restaurant> closest_restaurants;

    public List<Restaurant> getRestaurants() {
        return closest_restaurants;
    }

    public void setRestaurants(List<Restaurant> closest_restaurants) {
        this.closest_restaurants = closest_restaurants;
    }
}

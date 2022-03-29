package com.godynamo.dinr.model;

import java.util.ArrayList;

/**
 * Created by danko on 3/4/2016.
 */
public class Cities {

    ArrayList<City> cities;

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }


    @Override
    public String toString() {
        return "Cities{" +
                "cities=" + cities +
                '}';
    }
}

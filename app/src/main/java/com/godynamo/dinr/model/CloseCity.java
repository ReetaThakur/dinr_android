package com.godynamo.dinr.model;

/**
 * Created by danko on 3/4/2016.
 */
public class CloseCity {

    private City city;
    Double distance;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "CloseCity{" +
                "city=" + city +
                ", distance=" + distance +
                '}';
    }
}

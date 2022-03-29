package com.godynamo.dinr.model;

/**
 * Created by danko on 3/4/2016.
 */
public class City {

    Integer id;
    String name_en;
    String name_fr;
    String readable_name;
    String latitude;
    String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_fr() {
        return name_fr;
    }

    public void setName_fr(String name_fr) {
        this.name_fr = name_fr;
    }

    public String getReadable_name() {
        return readable_name;
    }

    public void setReadable_name(String readable_name) {
        this.readable_name = readable_name;
    }


    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name_en='" + name_en + '\'' +
                ", name_fr='" + name_fr + '\'' +
                ", readable_name='" + readable_name + '\'' +
                '}';
    }
}

package com.godynamo.dinr.model;

import java.util.List;

/**
 * Created by dankovassev on 14-11-27.
 */
public class Restaurant {

    public static final String STATUS_OPEN = "open";
    public static final String STATUS_CLOSED = "closed";

    private int id;
    private String name;
    private String latitude;
    private String longitude;
    private String streetAddress;
    private String city;
    private String cityFr;
    private String provinceCode;
    private String provinceCodeFr;
    private String postalCode;
    private String phoneNumber;
    private String mainPhoto;
    private String status;
    private List<Photo> photos;
    private List<Opening> openings;

    private String description;
    private String descriptionFr;

    private boolean favorite;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionFr() {
        return descriptionFr;
    }

    public void setDescriptionFr(String descriptionFr) {
        this.descriptionFr = descriptionFr;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Opening> getOpenings() {
        return openings;
    }

    public void setOpenings(List<Opening> openings) {
        this.openings = openings;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCityFr() {
        return cityFr;
    }

    public void setCityFr(String cityFr) {
        this.cityFr = cityFr;
    }

    public String getProvinceCodeFr() {
        return provinceCodeFr;
    }

    public void setProvinceCodeFr(String provinceCodeFr) {
        this.provinceCodeFr = provinceCodeFr;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "openings=" + openings +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", cityFr='" + cityFr + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", provinceCodeFr='" + provinceCodeFr + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", mainPhoto='" + mainPhoto + '\'' +
                ", photos=" + photos +
                '}';
    }
}

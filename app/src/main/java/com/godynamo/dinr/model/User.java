package com.godynamo.dinr.model;

/**
 * Created by dankovassev on 15-02-01.
 */
public class User {
    private int fbSignupOrSigninCount;
    private int id;
    private String lastName;
    private boolean hasPaymentInfo;
    private String phoneNumber;
    private int signInCount;
    private String token;
    private int manualSignupCount;
    private String photoUrl;
    private String firstName;
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFbSignupOrSigninCount() {
        return fbSignupOrSigninCount;
    }

    public void setFbSignupOrSigninCount(int fbSignupOrSigninCount) {
        this.fbSignupOrSigninCount = fbSignupOrSigninCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isHasPaymentInfo() {
        return hasPaymentInfo;
    }

    public void setHasPaymentInfo(boolean hasPaymentInfo) {
        this.hasPaymentInfo = hasPaymentInfo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSignInCount() {
        return signInCount;
    }

    public void setSignInCount(int signInCount) {
        this.signInCount = signInCount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getManualSignupCount() {
        return manualSignupCount;
    }

    public void setManualSignupCount(int manualSignupCount) {
        this.manualSignupCount = manualSignupCount;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "User{" +
                "fbSignupOrSigninCount=" + fbSignupOrSigninCount +
                ", id=" + id +
                ", lastName='" + lastName + '\'' +
                ", hasPaymentInfo=" + hasPaymentInfo +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", signInCount=" + signInCount +
                ", token='" + token + '\'' +
                ", manualSignupCount=" + manualSignupCount +
                ", photoUrl='" + photoUrl + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}

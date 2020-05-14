package com.dexter.myhome.model;

public class User {
    private ApartmentInfo ApartmentInfo;
    private Profile Profile;

    public com.dexter.myhome.model.ApartmentInfo getApartmentInfo() {
        return ApartmentInfo;
    }

    public void setApartmentInfo(com.dexter.myhome.model.ApartmentInfo apartmentInfo) {
        ApartmentInfo = apartmentInfo;
    }

    public com.dexter.myhome.model.Profile getProfile() {
        return Profile;
    }

    public void setProfile(com.dexter.myhome.model.Profile profile) {
        Profile = profile;
    }
}

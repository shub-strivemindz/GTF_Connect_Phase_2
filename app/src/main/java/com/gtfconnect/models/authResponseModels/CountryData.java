package com.gtfconnect.models.authResponseModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class CountryData {


    @NonNull
    @SerializedName("CountryID")
    @Expose
    @PrimaryKey
    private Integer countryID;

    @SerializedName("name")
    @Expose
    private String countryName;

    @SerializedName("phonecode")
    @Expose
    private Integer phoneCode;

    public Integer getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(Integer phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(@NonNull Integer countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}

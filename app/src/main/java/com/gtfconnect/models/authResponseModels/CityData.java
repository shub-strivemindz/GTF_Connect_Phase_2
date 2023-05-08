package com.gtfconnect.models.authResponseModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class CityData {


    @NonNull
    @SerializedName("CityID")
    @Expose
    @PrimaryKey
    private Integer cityID;

    @SerializedName("name")
    @Expose
    private String cityName;


    public Integer getCityID() {
        return cityID;
    }

    public void setCityID(@NonNull Integer cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}

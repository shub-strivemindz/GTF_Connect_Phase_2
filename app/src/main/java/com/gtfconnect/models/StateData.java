package com.gtfconnect.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class StateData {


    @NonNull
    @SerializedName("StateID")
    @Expose
    @PrimaryKey
    private Integer stateID;

    @SerializedName("name")
    @Expose
    private String stateName;



    public Integer getStateID() {
        return stateID;
    }

    public void setStateID(@NonNull Integer stateID) {
        this.stateID = stateID;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}


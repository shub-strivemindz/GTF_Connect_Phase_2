package com.gtfconnect.models.channelDashboardModels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ChannelResponseModel {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<ChannelDashboardDataModel> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ChannelDashboardDataModel> getData() {
        return data;
    }

    public void setData(List<ChannelDashboardDataModel> data) {
        this.data = data;
    }

}










package com.gtfconnect.models.channelResponseModel.channelDashboardModels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "channel_dashboard_data")
public class ChannelResponseModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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










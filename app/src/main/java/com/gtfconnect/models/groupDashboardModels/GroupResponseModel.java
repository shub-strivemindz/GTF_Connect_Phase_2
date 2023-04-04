package com.gtfconnect.models.groupDashboardModels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtfconnect.models.channelDashboardModels.ChannelDashboardDataModel;

import java.util.List;


public class GroupResponseModel {

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
    private List<GroupDashboardDataModel> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<GroupDashboardDataModel> getData() {
        return data;
    }

    public void setData(List<GroupDashboardDataModel> data) {
        this.data = data;
    }

}










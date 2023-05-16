package com.gtfconnect.models.groupChannelModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;

import java.util.List;

public class GroupChannelInfoResponseModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private InfoDbEntity data;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InfoDbEntity getData() {
        return data;
    }

    public void setData(InfoDbEntity data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}











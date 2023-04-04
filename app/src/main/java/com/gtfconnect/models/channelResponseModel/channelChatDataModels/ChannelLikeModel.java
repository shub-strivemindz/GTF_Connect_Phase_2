package com.gtfconnect.models.channelResponseModel.channelChatDataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ChannelLikeModel {

    @SerializedName("LikeID")
    @Expose
    private Integer likeID;
    @SerializedName("GroupChannelID")
    @Expose
    private Integer groupChannelID;
    @SerializedName("GCMemberID")
    @Expose
    private Integer gCMemberID;
    @SerializedName("GroupChatID")
    @Expose
    private Integer groupChatID;
    @SerializedName("UserID")
    @Expose
    private Integer userID;
    @SerializedName("isLike")
    @Expose
    private Integer isLike;
    @SerializedName("CreatedAt")
    @Expose
    private String createdAt;
    @SerializedName("UpdatedAt")
    @Expose
    private String updatedAt;

    public Integer getLikeID() {
        return likeID;
    }

    public void setLikeID(Integer likeID) {
        this.likeID = likeID;
    }

    public Integer getGroupChannelID() {
        return groupChannelID;
    }

    public void setGroupChannelID(Integer groupChannelID) {
        this.groupChannelID = groupChannelID;
    }

    public Integer getGCMemberID() {
        return gCMemberID;
    }

    public void setGCMemberID(Integer gCMemberID) {
        this.gCMemberID = gCMemberID;
    }

    public Integer getGroupChatID() {
        return groupChatID;
    }

    public void setGroupChatID(Integer groupChatID) {
        this.groupChatID = groupChatID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}

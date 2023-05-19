package com.gtfconnect.models.commonGroupChannelResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaListModel {

    @SerializedName("GroupChatMediaID")
    @Expose
    private Integer groupChatMediaID;
    @SerializedName("GroupChatID")
    @Expose
    private Integer groupChatID;
    @SerializedName("GroupChannelID")
    @Expose
    private Integer groupChannelID;
    @SerializedName("GCMemberID")
    @Expose
    private Integer gCMemberID;
    @SerializedName("ChatCommentId")
    @Expose
    private String chatCommentId;
    @SerializedName("StoragePath")
    @Expose
    private String storagePath;
    @SerializedName("StorageType")
    @Expose
    private String storageType;
    @SerializedName("MediaTypeId")
    @Expose
    private String mediaTypeId;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("MimeType")
    @Expose
    private String mimeType;
    @SerializedName("FileName")
    @Expose
    private String fileName;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("CreatedAt")
    @Expose
    private String createdAt;
    @SerializedName("UpdatedAt")
    @Expose
    private String updatedAt;

    public Integer getGroupChatMediaID() {
        return groupChatMediaID;
    }

    public void setGroupChatMediaID(Integer groupChatMediaID) {
        this.groupChatMediaID = groupChatMediaID;
    }

    public Integer getGroupChatID() {
        return groupChatID;
    }

    public void setGroupChatID(Integer groupChatID) {
        this.groupChatID = groupChatID;
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

    public String getChatCommentId() {
        return chatCommentId;
    }

    public void setChatCommentId(String chatCommentId) {
        this.chatCommentId = chatCommentId;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getMediaTypeId() {
        return mediaTypeId;
    }

    public void setMediaTypeId(String mediaTypeId) {
        this.mediaTypeId = mediaTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

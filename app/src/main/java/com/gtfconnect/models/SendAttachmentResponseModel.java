package com.gtfconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class SendAttachmentResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }


    public class Datum {

        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("GroupChatMediaID")
        @Expose
        private Integer groupChatMediaID;
        @SerializedName("GroupChatID")
        @Expose
        private Object groupChatID;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("GCMemberID")
        @Expose
        private Integer gCMemberID;
        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("StoragePath")
        @Expose
        private String storagePath;
        @SerializedName("StorageType")
        @Expose
        private String storageType;
        @SerializedName("MimeType")
        @Expose
        private String mimeType;
        @SerializedName("FileName")
        @Expose
        private String fileName;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("ChatCommentId")
        @Expose
        private Object chatCommentId;
        @SerializedName("MediaTypeId")
        @Expose
        private Object mediaTypeId;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getGroupChatMediaID() {
            return groupChatMediaID;
        }

        public void setGroupChatMediaID(Integer groupChatMediaID) {
            this.groupChatMediaID = groupChatMediaID;
        }

        public Object getGroupChatID() {
            return groupChatID;
        }

        public void setGroupChatID(Object groupChatID) {
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

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
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

        public Object getChatCommentId() {
            return chatCommentId;
        }

        public void setChatCommentId(Object chatCommentId) {
            this.chatCommentId = chatCommentId;
        }

        public Object getMediaTypeId() {
            return mediaTypeId;
        }

        public void setMediaTypeId(Object mediaTypeId) {
            this.mediaTypeId = mediaTypeId;
        }

    }
}
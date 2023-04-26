package com.gtfconnect.models.savedMessageModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SavedMessageResponseModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



    public class Data {

        @SerializedName("list")
        @Expose
        private List<ListData> list;
        @SerializedName("current_page")
        @Expose
        private Integer currentPage;
        @SerializedName("from")
        @Expose
        private Integer from;
        @SerializedName("to")
        @Expose
        private Integer to;
        @SerializedName("per_page")
        @Expose
        private String perPage;
        @SerializedName("has_more")
        @Expose
        private Boolean hasMore;
        @SerializedName("base_url")
        @Expose
        private String baseUrl;

        public List<ListData> getList() {
            return list;
        }

        public void setList(List<ListData> list) {
            this.list = list;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }

        public String getPerPage() {
            return perPage;
        }

        public void setPerPage(String perPage) {
            this.perPage = perPage;
        }

        public Boolean getHasMore() {
            return hasMore;
        }

        public void setHasMore(Boolean hasMore) {
            this.hasMore = hasMore;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

    }
    public class ListData {

        @SerializedName("SavedMessageID")
        @Expose
        private Integer savedMessageID;
        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("Content")
        @Expose
        private String content;
        @SerializedName("ReactionID")
        @Expose
        private Object reactionID;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("GroupChatID")
        @Expose
        private Integer groupChatID;
        @SerializedName("saved_message_media")
        @Expose
        private java.util.List<SavedMessageMedium> savedMessageMedia;

        public Integer getSavedMessageID() {
            return savedMessageID;
        }

        public void setSavedMessageID(Integer savedMessageID) {
            this.savedMessageID = savedMessageID;
        }

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Object getReactionID() {
            return reactionID;
        }

        public void setReactionID(Object reactionID) {
            this.reactionID = reactionID;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public Integer getGroupChannelID() {
            return groupChannelID;
        }

        public void setGroupChannelID(Integer groupChannelID) {
            this.groupChannelID = groupChannelID;
        }

        public Integer getGroupChatID() {
            return groupChatID;
        }

        public void setGroupChatID(Integer groupChatID) {
            this.groupChatID = groupChatID;
        }

        public java.util.List<SavedMessageMedium> getSavedMessageMedia() {
            return savedMessageMedia;
        }

        public void setSavedMessageMedia(java.util.List<SavedMessageMedium> savedMessageMedia) {
            this.savedMessageMedia = savedMessageMedia;
        }

    }
    public class SavedMessageMedium {

        @SerializedName("ID")
        @Expose
        private Integer id;
        @SerializedName("SavedMessageID")
        @Expose
        private Integer savedMessageID;
        @SerializedName("StoragePath")
        @Expose
        private String storagePath;
        @SerializedName("StorageType")
        @Expose
        private String storageType;
        @SerializedName("StorageService")
        @Expose
        private String storageService;
        @SerializedName("MediaTypeID")
        @Expose
        private Object mediaTypeID;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("MimeType")
        @Expose
        private String mimeType;
        @SerializedName("FileName")
        @Expose
        private String fileName;
        @SerializedName("FileSize")
        @Expose
        private Integer fileSize;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getSavedMessageID() {
            return savedMessageID;
        }

        public void setSavedMessageID(Integer savedMessageID) {
            this.savedMessageID = savedMessageID;
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

        public String getStorageService() {
            return storageService;
        }

        public void setStorageService(String storageService) {
            this.storageService = storageService;
        }

        public Object getMediaTypeID() {
            return mediaTypeID;
        }

        public void setMediaTypeID(Object mediaTypeID) {
            this.mediaTypeID = mediaTypeID;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public Integer getFileSize() {
            return fileSize;
        }

        public void setFileSize(Integer fileSize) {
            this.fileSize = fileSize;
        }

    }
}



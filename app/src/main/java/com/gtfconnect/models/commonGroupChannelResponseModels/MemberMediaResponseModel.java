package com.gtfconnect.models.commonGroupChannelResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MemberMediaResponseModel {

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

        @SerializedName("document")
        @Expose
        private List<Document> document;
        @SerializedName("media")
        @Expose
        private List<Medium> media;
        @SerializedName("userInfo")
        @Expose
        private UserInfo userInfo;
        @SerializedName("base_url_node")
        @Expose
        private String baseUrlNode;
        @SerializedName("base_url")
        @Expose
        private String baseUrl;

        public List<Document> getDocument() {
            return document;
        }

        public void setDocument(List<Document> document) {
            this.document = document;
        }

        public List<Medium> getMedia() {
            return media;
        }

        public void setMedia(List<Medium> media) {
            this.media = media;
        }


        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public String getBaseUrlNode() {
            return baseUrlNode;
        }

        public void setBaseUrlNode(String baseUrlNode) {
            this.baseUrlNode = baseUrlNode;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
    public class Document {

        @SerializedName("GroupChatMediaID")
        @Expose
        private Integer groupChatMediaID;
        @SerializedName("StoragePath")
        @Expose
        private String storagePath;
        @SerializedName("StorageType")
        @Expose
        private String storageType;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("MimeType")
        @Expose
        private String mimeType;
        @SerializedName("FileName")
        @Expose
        private String fileName;
        @SerializedName("GCMemberID")
        @Expose
        private Integer gCMemberID;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;

        public Integer getGroupChatMediaID() {
            return groupChatMediaID;
        }

        public void setGroupChatMediaID(Integer groupChatMediaID) {
            this.groupChatMediaID = groupChatMediaID;
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

        public Integer getGCMemberID() {
            return gCMemberID;
        }

        public void setGCMemberID(Integer gCMemberID) {
            this.gCMemberID = gCMemberID;
        }

        public Integer getGroupChannelID() {
            return groupChannelID;
        }

        public void setGroupChannelID(Integer groupChannelID) {
            this.groupChannelID = groupChannelID;
        }

    }

    public class Medium {

        @SerializedName("GroupChatMediaID")
        @Expose
        private Integer groupChatMediaID;
        @SerializedName("StoragePath")
        @Expose
        private String storagePath;
        @SerializedName("StorageType")
        @Expose
        private String storageType;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("MimeType")
        @Expose
        private String mimeType;
        @SerializedName("FileName")
        @Expose
        private String fileName;
        @SerializedName("GCMemberID")
        @Expose
        private Integer gCMemberID;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;

        public Integer getGroupChatMediaID() {
            return groupChatMediaID;
        }

        public void setGroupChatMediaID(Integer groupChatMediaID) {
            this.groupChatMediaID = groupChatMediaID;
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

        public Integer getGCMemberID() {
            return gCMemberID;
        }

        public void setGCMemberID(Integer gCMemberID) {
            this.gCMemberID = gCMemberID;
        }

        public Integer getGroupChannelID() {
            return groupChannelID;
        }

        public void setGroupChannelID(Integer groupChannelID) {
            this.groupChannelID = groupChannelID;
        }

    }
    public class UserInfo {

        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("Firstname")
        @Expose
        private String firstname;
        @SerializedName("Lastname")
        @Expose
        private String lastname;
        @SerializedName("ProfileImage")
        @Expose
        private String profileImage;
        @SerializedName("user_report")
        @Expose
        private Object userReport;
        @SerializedName("gc_member")
        @Expose
        private GcMember gcMember;
        @SerializedName("ProfileThumbnail")
        @Expose
        private String profileThumbnail;

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public Object getUserReport() {
            return userReport;
        }

        public void setUserReport(Object userReport) {
            this.userReport = userReport;
        }

        public GcMember getGcMember() {
            return gcMember;
        }

        public void setGcMember(GcMember gcMember) {
            this.gcMember = gcMember;
        }

        public String getProfileThumbnail() {
            return profileThumbnail;
        }

        public void setProfileThumbnail(String profileThumbnail) {
            this.profileThumbnail = profileThumbnail;
        }

    }

    public class GcMember {

        @SerializedName("GCMemberID")
        @Expose
        private Integer gCMemberID;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("Status")
        @Expose
        private String status;

        public Integer getGCMemberID() {
            return gCMemberID;
        }

        public void setGCMemberID(Integer gCMemberID) {
            this.gCMemberID = gCMemberID;
        }

        public Integer getGroupChannelID() {
            return groupChannelID;
        }

        public void setGroupChannelID(Integer groupChannelID) {
            this.groupChannelID = groupChannelID;
        }

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
}





package com.gtfconnect.models.commonGroupChannelResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupChannelBlocklistResponseModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("status")
    @Expose
    private Integer status;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public class Datum {

        @SerializedName("GCMemberID")
        @Expose
        private Integer gCMemberID;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("IsAdmin")
        @Expose
        private Integer isAdmin;
        @SerializedName("CreatedBy")
        @Expose
        private String createdBy;
        @SerializedName("CreatorID")
        @Expose
        private Integer creatorID;
        @SerializedName("IsDummy")
        @Expose
        private Integer isDummy;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("unreadcount")
        @Expose
        private Integer unreadcount;
        @SerializedName("LastLeftOn")
        @Expose
        private Object lastLeftOn;
        @SerializedName("LastRejoinOn")
        @Expose
        private Object lastRejoinOn;
        @SerializedName("user")
        @Expose
        private User user;

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

        public Integer getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(Integer isAdmin) {
            this.isAdmin = isAdmin;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public Integer getCreatorID() {
            return creatorID;
        }

        public void setCreatorID(Integer creatorID) {
            this.creatorID = creatorID;
        }

        public Integer getIsDummy() {
            return isDummy;
        }

        public void setIsDummy(Integer isDummy) {
            this.isDummy = isDummy;
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

        public Integer getUnreadcount() {
            return unreadcount;
        }

        public void setUnreadcount(Integer unreadcount) {
            this.unreadcount = unreadcount;
        }

        public Object getLastLeftOn() {
            return lastLeftOn;
        }

        public void setLastLeftOn(Object lastLeftOn) {
            this.lastLeftOn = lastLeftOn;
        }

        public Object getLastRejoinOn() {
            return lastRejoinOn;
        }

        public void setLastRejoinOn(Object lastRejoinOn) {
            this.lastRejoinOn = lastRejoinOn;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }
    public class User {

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

    }
}



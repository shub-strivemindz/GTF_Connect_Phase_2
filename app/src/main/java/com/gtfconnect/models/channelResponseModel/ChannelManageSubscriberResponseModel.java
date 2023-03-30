package com.gtfconnect.models.channelResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelManageSubscriberResponseModel {

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

    }
    public class ListData {

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
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("member_subscription")
        @Expose
        private MemberSubscription memberSubscription;

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

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public MemberSubscription getMemberSubscription() {
            return memberSubscription;
        }

        public void setMemberSubscription(MemberSubscription memberSubscription) {
            this.memberSubscription = memberSubscription;
        }

    }
    public class MemberSubscription {

        @SerializedName("MemberSubscriptionID")
        @Expose
        private Integer memberSubscriptionID;
        @SerializedName("GCMemberID")
        @Expose
        private Integer gCMemberID;
        @SerializedName("SubscriptionEndDate")
        @Expose
        private String subscriptionEndDate;

        public Integer getMemberSubscriptionID() {
            return memberSubscriptionID;
        }

        public void setMemberSubscriptionID(Integer memberSubscriptionID) {
            this.memberSubscriptionID = memberSubscriptionID;
        }

        public Integer getGCMemberID() {
            return gCMemberID;
        }

        public void setGCMemberID(Integer gCMemberID) {
            this.gCMemberID = gCMemberID;
        }

        public String getSubscriptionEndDate() {
            return subscriptionEndDate;
        }

        public void setSubscriptionEndDate(String subscriptionEndDate) {
            this.subscriptionEndDate = subscriptionEndDate;
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




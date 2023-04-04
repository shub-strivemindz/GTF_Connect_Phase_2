package com.gtfconnect.models.channelResponseModel.channelChatDataModels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "channel_chat_data")
public class ChannelChatResponseModel {

    @PrimaryKey(autoGenerate = true)
    private int id;


        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("data")
        @Expose
        private Data data;

        /*private int GroupChannelID;

    public int getGroupChannelID() {
        return GroupChannelID;
    }

    public void setGroupChannelID(int groupChannelID) {
        GroupChannelID = groupChannelID;
    }*/


    public Boolean getSuccess () {
        return success;
    }

        public void setSuccess (Boolean success){
        this.success = success;
    }

        public Data getData () {
        return data;
    }

        public void setData (Data data){
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public class Data {

            @SerializedName("baseUrl")
            @Expose
            private String baseUrl;
            @SerializedName("subscriptionCount")
            @Expose
            private Integer subscriptionCount;
            @SerializedName("chatData")
            @Expose
            private ChatData chatData;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public Integer getSubscriptionCount() {
                return subscriptionCount;
            }

            public void setSubscriptionCount(Integer subscriptionCount) {
                this.subscriptionCount = subscriptionCount;
            }

            public ChatData getChatData() {
                return chatData;
            }

            public void setChatData(ChatData chatData) {
                this.chatData = chatData;
            }

        }

        public class ChatData {

            @SerializedName("count")
            @Expose
            private Integer count;
            @SerializedName("rows")
            @Expose
            private List<ChannelRowListDataModel> rows;

            public Integer getCount() {
                return count;
            }

            public void setCount(Integer count) {
                this.count = count;
            }

            public List<ChannelRowListDataModel> getRows() {
                return rows;
            }

            public void setRows(List<ChannelRowListDataModel> rows) {
                this.rows = rows;
            }

        }




        public class Quote {

            @SerializedName("GroupChatID")
            @Expose
            private String groupChatID;
            @SerializedName("GroupChannelID")
            @Expose
            private Integer groupChannelID;
            @SerializedName("GCMemberID")
            @Expose
            private Integer gCMemberID;
            @SerializedName("UserID")
            @Expose
            private Integer userID;
            @SerializedName("Message")
            @Expose
            private String message;
            @SerializedName("GroupChatRefID")
            @Expose
            private String groupChatRefID;
            @SerializedName("AllowDiscussion")
            @Expose
            private Integer allowDiscussion;
            @SerializedName("OriginalViews")
            @Expose
            private Integer originalViews;
            @SerializedName("DummyViews")
            @Expose
            private Integer dummyViews;
            @SerializedName("ReactionsCount")
            @Expose
            private Integer reactionsCount;
            @SerializedName("DummyUserID")
            @Expose
            private String dummyUserID;
            @SerializedName("IsRead")
            @Expose
            private Integer isRead;
            @SerializedName("CreatedAt")
            @Expose
            private String createdAt;
            @SerializedName("UpdatedAt")
            @Expose
            private String updatedAt;
            @SerializedName("user")
            @Expose
            private User user;

            public String getGroupChatID() {
                return groupChatID;
            }

            public void setGroupChatID(String groupChatID) {
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

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getGroupChatRefID() {
                return groupChatRefID;
            }

            public void setGroupChatRefID(String groupChatRefID) {
                this.groupChatRefID = groupChatRefID;
            }

            public Integer getAllowDiscussion() {
                return allowDiscussion;
            }

            public void setAllowDiscussion(Integer allowDiscussion) {
                this.allowDiscussion = allowDiscussion;
            }

            public Integer getOriginalViews() {
                return originalViews;
            }

            public void setOriginalViews(Integer originalViews) {
                this.originalViews = originalViews;
            }

            public Integer getDummyViews() {
                return dummyViews;
            }

            public void setDummyViews(Integer dummyViews) {
                this.dummyViews = dummyViews;
            }

            public Integer getReactionsCount() {
                return reactionsCount;
            }

            public void setReactionsCount(Integer reactionsCount) {
                this.reactionsCount = reactionsCount;
            }

            public String getDummyUserID() {
                return dummyUserID;
            }

            public void setDummyUserID(String dummyUserID) {
                this.dummyUserID = dummyUserID;
            }

            public Integer getIsRead() {
                return isRead;
            }

            public void setIsRead(Integer isRead) {
                this.isRead = isRead;
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
            private String userID;
            @SerializedName("GTFUserID")
            @Expose
            private Integer gTFUserID;
            @SerializedName("Email")
            @Expose
            private String email;
            @SerializedName("Phone")
            @Expose
            private String phone;
            @SerializedName("Firstname")
            @Expose
            private String firstname;
            @SerializedName("Lastname")
            @Expose
            private String lastname;
            @SerializedName("ProfileImage")
            @Expose
            private String profileImage;
            @SerializedName("api_token")
            @Expose
            private String apiToken;
            @SerializedName("LastLoginAt")
            @Expose
            private String lastLoginAt;
            @SerializedName("CreatedAt")
            @Expose
            private String createdAt;
            @SerializedName("UpdatedAt")
            @Expose
            private String updatedAt;

            public String getUserID() {
                return userID;
            }

            public void setUserID(String userID) {
                this.userID = userID;
            }

            public Integer getGTFUserID() {
                return gTFUserID;
            }

            public void setGTFUserID(Integer gTFUserID) {
                this.gTFUserID = gTFUserID;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
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

            public String getApiToken() {
                return apiToken;
            }

            public void setApiToken(String apiToken) {
                this.apiToken = apiToken;
            }

            public String getLastLoginAt() {
                return lastLoginAt;
            }

            public void setLastLoginAt(String lastLoginAt) {
                this.lastLoginAt = lastLoginAt;
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



    }

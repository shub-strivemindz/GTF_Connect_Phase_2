package com.gtfconnect.models.commonGroupChannelResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLastMessageSentResponseModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private DataItem data;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public DataItem getData() {
            return data;
        }

        public void setData(DataItem data) {
            this.data = data;
        }

    }
    public class DataItem {

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
        private Object groupChatRefID;
        @SerializedName("AllowDiscussion")
        @Expose
        private Integer allowDiscussion;
        @SerializedName("OriginalViews")
        @Expose
        private Integer originalViews;
        @SerializedName("DummyViews")
        @Expose
        private Integer dummyViews;
        @SerializedName("reactionscount")
        @Expose
        private Integer reactionscount;
        @SerializedName("DummyUserID")
        @Expose
        private String dummyUserID;
        @SerializedName("IsRead")
        @Expose
        private Integer isRead;
        @SerializedName("ChatType")
        @Expose
        private String chatType;
        @SerializedName("commentcount")
        @Expose
        private Integer commentcount;
        @SerializedName("ChatStatus")
        @Expose
        private Integer chatStatus;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;

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

        public Object getGroupChatRefID() {
            return groupChatRefID;
        }

        public void setGroupChatRefID(Object groupChatRefID) {
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

        public Integer getReactionscount() {
            return reactionscount;
        }

        public void setReactionscount(Integer reactionscount) {
            this.reactionscount = reactionscount;
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

        public String getChatType() {
            return chatType;
        }

        public void setChatType(String chatType) {
            this.chatType = chatType;
        }

        public Integer getCommentcount() {
            return commentcount;
        }

        public void setCommentcount(Integer commentcount) {
            this.commentcount = commentcount;
        }

        public Integer getChatStatus() {
            return chatStatus;
        }

        public void setChatStatus(Integer chatStatus) {
            this.chatStatus = chatStatus;
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
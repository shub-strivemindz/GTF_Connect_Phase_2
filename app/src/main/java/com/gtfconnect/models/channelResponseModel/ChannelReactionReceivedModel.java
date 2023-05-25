package com.gtfconnect.models.channelResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.models.commonGroupChannelResponseModels.MediaListModel;

import java.util.List;

public class ChannelReactionReceivedModel {

    @SerializedName("messageId")
    @Expose
    private String messageId;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {

        @SerializedName("baseUrl")
        @Expose
        private String baseUrl;
        @SerializedName("chatDetail")
        @Expose
        private ChannelRowListDataModel chatDetail;
        @SerializedName("likeStatus")
        @Expose
        private LikeStatus likeStatus;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public ChannelRowListDataModel getChatDetail() {
            return chatDetail;
        }

        public void setChatDetail(ChannelRowListDataModel chatDetail) {
            this.chatDetail = chatDetail;
        }

        public LikeStatus getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(LikeStatus likeStatus) {
            this.likeStatus = likeStatus;
        }

    }

    public class LikeStatus {

        @SerializedName("LikeID")
        @Expose
        private Integer likeID;
        @SerializedName("GroupChatID")
        @Expose
        private Integer groupChatID;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("ReactionID")
        @Expose
        private String reactionID;
        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("GCMemberID")
        @Expose
        private Integer gCMemberID;
        @SerializedName("isLike")
        @Expose
        private Integer isLike;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;

        public Integer getLikeID() {
            return likeID;
        }

        public void setLikeID(Integer likeID) {
            this.likeID = likeID;
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

        public String getReactionID() {
            return reactionID;
        }

        public void setReactionID(String reactionID) {
            this.reactionID = reactionID;
        }

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public Integer getGCMemberID() {
            return gCMemberID;
        }

        public void setGCMemberID(Integer gCMemberID) {
            this.gCMemberID = gCMemberID;
        }

        public Integer getIsLike() {
            return isLike;
        }

        public void setIsLike(Integer isLike) {
            this.isLike = isLike;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }
}


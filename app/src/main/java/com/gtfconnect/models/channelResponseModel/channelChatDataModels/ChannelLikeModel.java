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
    @SerializedName("ReactionID")
    @Expose
    private String reactionID;
    @SerializedName("isLike")
    @Expose
    private Integer isLike;
    @SerializedName("CreatedAt")
    @Expose
    private String createdAt;
    @SerializedName("UpdatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("reaction")
    @Expose
    private Reaction reaction;

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

    public String getReactionID() {
        return reactionID;
    }

    public void setReactionID(String reactionID) {
        this.reactionID = reactionID;
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

    public Reaction getReaction() {
        return reaction;
    }

    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }


    public class Reaction {

        @SerializedName("ReactionID")
        @Expose
        private Integer reactionID;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("EmojiCode")
        @Expose
        private String emojiCode;
        @SerializedName("Slug")
        @Expose
        private String slug;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;

        public Integer getReactionID() {
            return reactionID;
        }

        public void setReactionID(Integer reactionID) {
            this.reactionID = reactionID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEmojiCode() {
            return emojiCode;
        }

        public void setEmojiCode(String emojiCode) {
            this.emojiCode = emojiCode;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
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

    }
}

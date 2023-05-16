package com.gtfconnect.models.channelResponseModel.channelChatDataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtfconnect.models.groupChannelModels.MediaListModel;

import java.util.ArrayList;
import java.util.List;

public class ChannelRowListDataModel {

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
    @SerializedName("ChatType")
    @Expose
    private String chatType;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(int chatStatus) {
        this.chatStatus = chatStatus;
    }

    @SerializedName("CommentCount")
    @Expose
    private int commentCount;
    @SerializedName("ChatStatus")
    @Expose
    private int chatStatus;
    @SerializedName("CreatedAt")
    @Expose
    private String createdAt;
    @SerializedName("UpdatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("user")
    @Expose
    private ChannelChatResponseModel.User user;
    @SerializedName("like")
    @Expose
    private List<ChannelLikeModel> like;
    @SerializedName("commentData")
    @Expose
    private List<ChannelChatCommentModel> commentData = new ArrayList<>();
    @SerializedName("quote")
    @Expose
    private ChannelChatResponseModel.Quote quote;
    @SerializedName("media")
    @Expose
    private List<MediaListModel> media;


    private boolean showPostSelection = false;

    private boolean isAudioDownloaded = false;


    private boolean postSelected = false;

    public boolean isPostSelected() {
        return postSelected;
    }

    public void setPostSelected(boolean postSelected) {
        this.postSelected = postSelected;
    }

    public boolean isShowPostSelection() {
        return showPostSelection;
    }

    public void setShowPostSelection(boolean showPostSelection) {
        this.showPostSelection = showPostSelection;
    }

    public boolean isAudioDownloaded() {
        return isAudioDownloaded;
    }

    public void setAudioDownloaded(boolean audioDownloaded) {
        isAudioDownloaded = audioDownloaded;
    }

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

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
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

    public ChannelChatResponseModel.User getUser() {
        return user;
    }

    public void setUser(ChannelChatResponseModel.User user) {
        this.user = user;
    }

    public List<ChannelLikeModel> getLike() {
        return like;
    }

    public void setLike(List<ChannelLikeModel> like) {
        this.like = like;
    }

    public List<ChannelChatCommentModel> getCommentData() {
        return commentData;
    }

    public void setCommentData(List<ChannelChatCommentModel> commentData) {
        this.commentData = commentData;
    }

    public ChannelChatResponseModel.Quote getQuote() {
        return quote;
    }

    public void setQuote(ChannelChatResponseModel.Quote quote) {
        this.quote = quote;
    }

    public List<MediaListModel> getMedia() {
        return media;
    }

    public void setMedia(List<MediaListModel> media) {
        this.media = media;
    }

}

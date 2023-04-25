package com.gtfconnect.models.groupResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GroupChatResponseModel {

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
        private List<Row> rows;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }

    }

    public class Like {

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
        @SerializedName("isLike")
        @Expose
        private Integer isLike;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;

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

    }

    public class Row {

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
        @SerializedName("ReactionsCount")
        @Expose
        private Integer reactionsCount;
        @SerializedName("DummyUserID")
        @Expose
        private Object dummyUserID;
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
        private GroupChatResponseModel.User user;
        @SerializedName("like")
        @Expose
        private List<GroupChatResponseModel.Like> like;
        @SerializedName("commentData")
        @Expose
        private List<GroupChatResponseModel.CommentResponseModel> commentData = new ArrayList<>();
        @SerializedName("quote")
        @Expose
        private GroupChatResponseModel.Quote quote;
        @SerializedName("media")
        @Expose
        private List<GroupChatResponseModel.Medium> media;


        private boolean isPostSelected = false;

        private boolean showPostSelection = false;

        public boolean isPostSelected() {
            return isPostSelected;
        }

        public void setPostSelected(boolean postSelected) {
            isPostSelected = postSelected;
        }

        public boolean isShowPostSelection() {
            return showPostSelection;
        }

        public void setShowPostSelection(boolean showPostSelection) {
            this.showPostSelection = showPostSelection;
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

        public Integer getReactionsCount() {
            return reactionsCount;
        }

        public void setReactionsCount(Integer reactionsCount) {
            this.reactionsCount = reactionsCount;
        }

        public Object getDummyUserID() {
            return dummyUserID;
        }

        public void setDummyUserID(Object dummyUserID) {
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

        public GroupChatResponseModel.User getUser() {
            return user;
        }

        public void setUser(GroupChatResponseModel.User user) {
            this.user = user;
        }

        public List<GroupChatResponseModel.Like> getLike() {
            return like;
        }

        public void setLike(List<GroupChatResponseModel.Like> like) {
            this.like = like;
        }

        public List<GroupChatResponseModel.CommentResponseModel> getCommentData() {
            return commentData;
        }

        public void setCommentData(List<GroupChatResponseModel.CommentResponseModel> commentData) {
            this.commentData = commentData;
        }

        public GroupChatResponseModel.Quote getQuote() {
            return quote;
        }

        public void setQuote(GroupChatResponseModel.Quote quote) {
            this.quote = quote;
        }

        public List<GroupChatResponseModel.Medium> getMedia() {
            return media;
        }

        public void setMedia(List<GroupChatResponseModel.Medium> media) {
            this.media = media;
        }

    }


    public class CommentResponseModel {

        @SerializedName("CommentID")
        @Expose
        private Integer commentID;
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
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("user")
        @Expose
        private GroupChatResponseModel.User user;

        public Integer getCommentID() {
            return commentID;
        }

        public void setCommentID(Integer commentID) {
            this.commentID = commentID;
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

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
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

        public GroupChatResponseModel.User getUser() {
            return user;
        }

        public void setUser(GroupChatResponseModel.User user) {
            this.user = user;
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
        @SerializedName("ReactionsCount")
        @Expose
        private Integer reactionsCount;
        @SerializedName("DummyUserID")
        @Expose
        private Object dummyUserID;
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

        public Integer getReactionsCount() {
            return reactionsCount;
        }

        public void setReactionsCount(Integer reactionsCount) {
            this.reactionsCount = reactionsCount;
        }

        public Object getDummyUserID() {
            return dummyUserID;
        }

        public void setDummyUserID(Object dummyUserID) {
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
        private Object profileImage;
        @SerializedName("api_token")
        @Expose
        private String apiToken;
        @SerializedName("LastLoginAt")
        @Expose
        private Object lastLoginAt;
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

        public Object getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(Object profileImage) {
            this.profileImage = profileImage;
        }

        public String getApiToken() {
            return apiToken;
        }

        public void setApiToken(String apiToken) {
            this.apiToken = apiToken;
        }

        public Object getLastLoginAt() {
            return lastLoginAt;
        }

        public void setLastLoginAt(Object lastLoginAt) {
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

    public class Medium {

        @SerializedName("GroupChatMediaID")
        @Expose
        private Integer groupChatMediaID;
        @SerializedName("GroupChatID")
        @Expose
        private Integer groupChatID;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("GCMemberID")
        @Expose
        private Integer gCMemberID;
        @SerializedName("ChatCommentId")
        @Expose
        private Object chatCommentId;
        @SerializedName("StoragePath")
        @Expose
        private String storagePath;
        @SerializedName("StorageType")
        @Expose
        private String storageType;
        @SerializedName("MediaTypeId")
        @Expose
        private Object mediaTypeId;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("MimeType")
        @Expose
        private String mimeType;
        @SerializedName("FileName")
        @Expose
        private String fileName;
        @SerializedName("UserID")
        @Expose
        private Object userID;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;

        public Integer getGroupChatMediaID() {
            return groupChatMediaID;
        }

        public void setGroupChatMediaID(Integer groupChatMediaID) {
            this.groupChatMediaID = groupChatMediaID;
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

        public Integer getGCMemberID() {
            return gCMemberID;
        }

        public void setGCMemberID(Integer gCMemberID) {
            this.gCMemberID = gCMemberID;
        }

        public Object getChatCommentId() {
            return chatCommentId;
        }

        public void setChatCommentId(Object chatCommentId) {
            this.chatCommentId = chatCommentId;
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

        public Object getMediaTypeId() {
            return mediaTypeId;
        }

        public void setMediaTypeId(Object mediaTypeId) {
            this.mediaTypeId = mediaTypeId;
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

        public Object getUserID() {
            return userID;
        }

        public void setUserID(Object userID) {
            this.userID = userID;
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





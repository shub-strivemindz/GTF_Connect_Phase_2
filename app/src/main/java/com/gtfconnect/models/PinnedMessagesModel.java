package com.gtfconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PinnedMessagesModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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


    public class Chat {

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
        @SerializedName("CommentCount")
        @Expose
        private Integer commentCount;
        @SerializedName("ChatStatus")
        @Expose
        private Integer chatStatus;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("media")
        @Expose
        private List<Medium> media;

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

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
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

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public List<Medium> getMedia() {
            return media;
        }

        public void setMedia(List<Medium> media) {
            this.media = media;
        }

    }

    public class Datum {

        @SerializedName("pinmessagesID")
        @Expose
        private Integer pinmessagesID;
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
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("chat")
        @Expose
        private Chat chat;
        @SerializedName("group")
        @Expose
        private Group group;

        private boolean audioDownloaded = false;

        public Integer getgCMemberID() {
            return gCMemberID;
        }

        public void setgCMemberID(Integer gCMemberID) {
            this.gCMemberID = gCMemberID;
        }

        public boolean isAudioDownloaded() {
            return audioDownloaded;
        }

        public void setAudioDownloaded(boolean audioDownloaded) {
            this.audioDownloaded = audioDownloaded;
        }

        public Integer getPinmessagesID() {
            return pinmessagesID;
        }

        public void setPinmessagesID(Integer pinmessagesID) {
            this.pinmessagesID = pinmessagesID;
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

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }

        public Group getGroup() {
            return group;
        }

        public void setGroup(Group group) {
            this.group = group;
        }

    }

    public class Group {

        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Description")
        @Expose
        private String description;
        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("AccessType")
        @Expose
        private String accessType;
        @SerializedName("ProfileImage")
        @Expose
        private String profileImage;
        @SerializedName("PublicLink")
        @Expose
        private Object publicLink;
        @SerializedName("PrivateLink")
        @Expose
        private Object privateLink;
        @SerializedName("IsTrending")
        @Expose
        private Integer isTrending;
        @SerializedName("TrendingLabel")
        @Expose
        private Object trendingLabel;
        @SerializedName("MemberCount")
        @Expose
        private String memberCount;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;

        public Integer getGroupChannelID() {
            return groupChannelID;
        }

        public void setGroupChannelID(Integer groupChannelID) {
            this.groupChannelID = groupChannelID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccessType() {
            return accessType;
        }

        public void setAccessType(String accessType) {
            this.accessType = accessType;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public Object getPublicLink() {
            return publicLink;
        }

        public void setPublicLink(Object publicLink) {
            this.publicLink = publicLink;
        }

        public Object getPrivateLink() {
            return privateLink;
        }

        public void setPrivateLink(Object privateLink) {
            this.privateLink = privateLink;
        }

        public Integer getIsTrending() {
            return isTrending;
        }

        public void setIsTrending(Integer isTrending) {
            this.isTrending = isTrending;
        }

        public Object getTrendingLabel() {
            return trendingLabel;
        }

        public void setTrendingLabel(Object trendingLabel) {
            this.trendingLabel = trendingLabel;
        }

        public String getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(String memberCount) {
            this.memberCount = memberCount;
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
        private Integer userID;
        @SerializedName("FileSize")
        @Expose
        private Integer fileSize;
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

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public Integer getFileSize() {
            return fileSize;
        }

        public void setFileSize(Integer fileSize) {
            this.fileSize = fileSize;
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

}
package com.gtfconnect.models.channelResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtfconnect.models.groupResponseModel.GroupResponseModel;

import java.util.List;

public class ChannelResponseModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<ChannelResponseModel.Datum> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ChannelResponseModel.Datum> getData() {
        return data;
    }

    public void setData(List<ChannelResponseModel.Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("GCMemberID")
        @Expose
        private String gCMemberID;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("UserID")
        @Expose
        private String userID;
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
        @SerializedName("unreadcount")
        @Expose
        private String unreadcount;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("group")
        @Expose
        private GroupResponseModel.Group group;

        public String getGCMemberID() {
            return gCMemberID;
        }

        public void setGCMemberID(String gCMemberID) {
            this.gCMemberID = gCMemberID;
        }

        public Integer getGroupChannelID() {
            return groupChannelID;
        }

        public void setGroupChannelID(Integer groupChannelID) {
            this.groupChannelID = groupChannelID;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
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

        public String getUnreadcount() {
            return unreadcount;
        }

        public void setUnreadcount(String unreadcount) {
            this.unreadcount = unreadcount;
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

        public GroupResponseModel.Group getGroup() {
            return group;
        }

        public void setGroup(GroupResponseModel.Group group) {
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
        private Object profileImage;
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
        @SerializedName("message")
        @Expose
        private List<GroupResponseModel.Message> message;

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

        public Object getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(Object profileImage) {
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

        public List<GroupResponseModel.Message> getMessage() {
            return message;
        }

        public void setMessage(List<GroupResponseModel.Message> message) {
            this.message = message;
        }

    }

    public class Message {

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
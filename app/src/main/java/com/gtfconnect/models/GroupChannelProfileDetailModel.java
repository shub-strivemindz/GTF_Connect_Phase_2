package com.gtfconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupChannelProfileDetailModel {

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

        @SerializedName("gc_info")
        @Expose
        private GcInfo gcInfo;
        @SerializedName("gc_setting")
        @Expose
        private GcSetting gcSetting;
        @SerializedName("gc_member_setting")
        @Expose
        private GcMemberSetting gcMemberSetting;
        @SerializedName("gc_permission")
        @Expose
        private GcPermission gcPermission;
        @SerializedName("gc_reaction")
        @Expose
        private GcReaction gcReaction;
        @SerializedName("gc_admin")
        @Expose
        private GcAdmin gcAdmin;
        @SerializedName("gc_member_subscription_plan")
        @Expose
        private GcMemberSubscriptionPlan gcMemberSubscriptionPlan;
        @SerializedName("gc_subscription_plan")
        @Expose
        private List<GcSubscriptionPlan> gcSubscriptionPlan;

        public GcInfo getGcInfo() {
            return gcInfo;
        }

        public void setGcInfo(GcInfo gcInfo) {
            this.gcInfo = gcInfo;
        }

        public GcSetting getGcSetting() {
            return gcSetting;
        }

        public void setGcSetting(GcSetting gcSetting) {
            this.gcSetting = gcSetting;
        }

        public GcMemberSetting getGcMemberSetting() {
            return gcMemberSetting;
        }

        public void setGcMemberSetting(GcMemberSetting gcMemberSetting) {
            this.gcMemberSetting = gcMemberSetting;
        }

        public GcPermission getGcPermission() {
            return gcPermission;
        }

        public void setGcPermission(GcPermission gcPermission) {
            this.gcPermission = gcPermission;
        }

        public GcReaction getGcReaction() {
            return gcReaction;
        }

        public void setGcReaction(GcReaction gcReaction) {
            this.gcReaction = gcReaction;
        }

        public GcAdmin getGcAdmin() {
            return gcAdmin;
        }

        public void setGcAdmin(GcAdmin gcAdmin) {
            this.gcAdmin = gcAdmin;
        }

        public GcMemberSubscriptionPlan getGcMemberSubscriptionPlan() {
            return gcMemberSubscriptionPlan;
        }

        public void setGcMemberSubscriptionPlan(GcMemberSubscriptionPlan gcMemberSubscriptionPlan) {
            this.gcMemberSubscriptionPlan = gcMemberSubscriptionPlan;
        }

        public List<GcSubscriptionPlan> getGcSubscriptionPlan() {
            return gcSubscriptionPlan;
        }

        public void setGcSubscriptionPlan(List<GcSubscriptionPlan> gcSubscriptionPlan) {
            this.gcSubscriptionPlan = gcSubscriptionPlan;
        }

    }
    public class DummyUser {
    }

    public class GcAdmin {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("list")
        @Expose
        private List<ListDetail> list;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<ListDetail> getList() {
            return list;
        }

        public void setList(List<ListDetail> list) {
            this.list = list;
        }

    }
    public class GcInfo {

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
        private String publicLink;
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
        private Integer memberCount;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("CreatedBy")
        @Expose
        private Integer createdBy;
        @SerializedName("ShowPopupInfo")
        @Expose
        private Integer showPopupInfo;
        @SerializedName("PopupInfoImage")
        @Expose
        private Object popupInfoImage;
        @SerializedName("is_product")
        @Expose
        private Integer isProduct;

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

        public String getPublicLink() {
            return publicLink;
        }

        public void setPublicLink(String publicLink) {
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

        public Integer getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(Integer memberCount) {
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

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public Integer getShowPopupInfo() {
            return showPopupInfo;
        }

        public void setShowPopupInfo(Integer showPopupInfo) {
            this.showPopupInfo = showPopupInfo;
        }

        public Object getPopupInfoImage() {
            return popupInfoImage;
        }

        public void setPopupInfoImage(Object popupInfoImage) {
            this.popupInfoImage = popupInfoImage;
        }

        public Integer getIsProduct() {
            return isProduct;
        }

        public void setIsProduct(Integer isProduct) {
            this.isProduct = isProduct;
        }

    }
    public class GcMemberSetting {
    }
    public class GcMemberSubscriptionPlan {
    }
    public class GcPermission {

        @SerializedName("SendMessage")
        @Expose
        private Integer sendMessage;
        @SerializedName("SendMedia")
        @Expose
        private Integer sendMedia;
        @SerializedName("SendStickerGIF")
        @Expose
        private Integer sendStickerGIF;
        @SerializedName("EmbedLinks")
        @Expose
        private Integer embedLinks;
        @SerializedName("PinMessage")
        @Expose
        private Integer pinMessage;
        @SerializedName("SlowMode")
        @Expose
        private Integer slowMode;

        public Integer getSendMessage() {
            return sendMessage;
        }

        public void setSendMessage(Integer sendMessage) {
            this.sendMessage = sendMessage;
        }

        public Integer getSendMedia() {
            return sendMedia;
        }

        public void setSendMedia(Integer sendMedia) {
            this.sendMedia = sendMedia;
        }

        public Integer getSendStickerGIF() {
            return sendStickerGIF;
        }

        public void setSendStickerGIF(Integer sendStickerGIF) {
            this.sendStickerGIF = sendStickerGIF;
        }

        public Integer getEmbedLinks() {
            return embedLinks;
        }

        public void setEmbedLinks(Integer embedLinks) {
            this.embedLinks = embedLinks;
        }

        public Integer getPinMessage() {
            return pinMessage;
        }

        public void setPinMessage(Integer pinMessage) {
            this.pinMessage = pinMessage;
        }

        public Integer getSlowMode() {
            return slowMode;
        }

        public void setSlowMode(Integer slowMode) {
            this.slowMode = slowMode;
        }

    }
    public class GcReaction {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("reactions_list")
        @Expose
        private List<Reactions> reactionsList;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<Reactions> getReactionsList() {
            return reactionsList;
        }

        public void setReactionsList(List<Reactions> reactionsList) {
            this.reactionsList = reactionsList;
        }

    }
    public class GcSetting {

        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("IsNotification")
        @Expose
        private Integer isNotification;
        @SerializedName("SignedMsg")
        @Expose
        private Integer signedMsg;
        @SerializedName("EnableReactions")
        @Expose
        private Integer enableReactions;
        @SerializedName("RestrictSharingContent")
        @Expose
        private Integer restrictSharingContent;
        @SerializedName("AllowDiscussion")
        @Expose
        private Integer allowDiscussion;
        @SerializedName("AllowMemberOnly")
        @Expose
        private Integer allowMemberOnly;
        @SerializedName("EnableManipulateViews")
        @Expose
        private Integer enableManipulateViews;
        @SerializedName("ManipulateViewsPercent")
        @Expose
        private Integer manipulateViewsPercent;
        @SerializedName("FakeMemberCount")
        @Expose
        private Integer fakeMemberCount;
        @SerializedName("DummyMsgEnabled")
        @Expose
        private Integer dummyMsgEnabled;
        @SerializedName("DummyUser")
        @Expose
        private DummyUser dummyUser;

        public Integer getGroupChannelID() {
            return groupChannelID;
        }

        public void setGroupChannelID(Integer groupChannelID) {
            this.groupChannelID = groupChannelID;
        }

        public Integer getIsNotification() {
            return isNotification;
        }

        public void setIsNotification(Integer isNotification) {
            this.isNotification = isNotification;
        }

        public Integer getSignedMsg() {
            return signedMsg;
        }

        public void setSignedMsg(Integer signedMsg) {
            this.signedMsg = signedMsg;
        }

        public Integer getEnableReactions() {
            return enableReactions;
        }

        public void setEnableReactions(Integer enableReactions) {
            this.enableReactions = enableReactions;
        }

        public Integer getRestrictSharingContent() {
            return restrictSharingContent;
        }

        public void setRestrictSharingContent(Integer restrictSharingContent) {
            this.restrictSharingContent = restrictSharingContent;
        }

        public Integer getAllowDiscussion() {
            return allowDiscussion;
        }

        public void setAllowDiscussion(Integer allowDiscussion) {
            this.allowDiscussion = allowDiscussion;
        }

        public Integer getAllowMemberOnly() {
            return allowMemberOnly;
        }

        public void setAllowMemberOnly(Integer allowMemberOnly) {
            this.allowMemberOnly = allowMemberOnly;
        }

        public Integer getEnableManipulateViews() {
            return enableManipulateViews;
        }

        public void setEnableManipulateViews(Integer enableManipulateViews) {
            this.enableManipulateViews = enableManipulateViews;
        }

        public Integer getManipulateViewsPercent() {
            return manipulateViewsPercent;
        }

        public void setManipulateViewsPercent(Integer manipulateViewsPercent) {
            this.manipulateViewsPercent = manipulateViewsPercent;
        }

        public Integer getFakeMemberCount() {
            return fakeMemberCount;
        }

        public void setFakeMemberCount(Integer fakeMemberCount) {
            this.fakeMemberCount = fakeMemberCount;
        }

        public Integer getDummyMsgEnabled() {
            return dummyMsgEnabled;
        }

        public void setDummyMsgEnabled(Integer dummyMsgEnabled) {
            this.dummyMsgEnabled = dummyMsgEnabled;
        }

        public DummyUser getDummyUser() {
            return dummyUser;
        }

        public void setDummyUser(DummyUser dummyUser) {
            this.dummyUser = dummyUser;
        }

    }
    public class GcSubscriptionPlan {

        @SerializedName("GCSubscriptionPlanID")
        @Expose
        private Integer gCSubscriptionPlanID;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("SubscriptionPlanID")
        @Expose
        private Integer subscriptionPlanID;
        @SerializedName("Badge")
        @Expose
        private Object badge;
        @SerializedName("subscription_plan")
        @Expose
        private SubscriptionPlan subscriptionPlan;

        public Integer getGCSubscriptionPlanID() {
            return gCSubscriptionPlanID;
        }

        public void setGCSubscriptionPlanID(Integer gCSubscriptionPlanID) {
            this.gCSubscriptionPlanID = gCSubscriptionPlanID;
        }

        public Integer getGroupChannelID() {
            return groupChannelID;
        }

        public void setGroupChannelID(Integer groupChannelID) {
            this.groupChannelID = groupChannelID;
        }

        public Integer getSubscriptionPlanID() {
            return subscriptionPlanID;
        }

        public void setSubscriptionPlanID(Integer subscriptionPlanID) {
            this.subscriptionPlanID = subscriptionPlanID;
        }

        public Object getBadge() {
            return badge;
        }

        public void setBadge(Object badge) {
            this.badge = badge;
        }

        public SubscriptionPlan getSubscriptionPlan() {
            return subscriptionPlan;
        }

        public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
            this.subscriptionPlan = subscriptionPlan;
        }

    }
    public class ListDetail {

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

    }
    public class Reactions {

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

    }
    public class SubscriptionPlan {

        @SerializedName("SubscriptionPlanID")
        @Expose
        private Integer subscriptionPlanID;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Description")
        @Expose
        private String description;
        @SerializedName("NetPrice")
        @Expose
        private String netPrice;
        @SerializedName("TaxAmount")
        @Expose
        private String taxAmount;
        @SerializedName("TotalPrice")
        @Expose
        private String totalPrice;
        @SerializedName("SubscriptionType")
        @Expose
        private String subscriptionType;
        @SerializedName("PaymentTerm")
        @Expose
        private String paymentTerm;
        @SerializedName("TermDurationID")
        @Expose
        private Integer termDurationID;
        @SerializedName("term_duration")
        @Expose
        private TermDuration termDuration;

        public Integer getSubscriptionPlanID() {
            return subscriptionPlanID;
        }

        public void setSubscriptionPlanID(Integer subscriptionPlanID) {
            this.subscriptionPlanID = subscriptionPlanID;
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

        public String getNetPrice() {
            return netPrice;
        }

        public void setNetPrice(String netPrice) {
            this.netPrice = netPrice;
        }

        public String getTaxAmount() {
            return taxAmount;
        }

        public void setTaxAmount(String taxAmount) {
            this.taxAmount = taxAmount;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getSubscriptionType() {
            return subscriptionType;
        }

        public void setSubscriptionType(String subscriptionType) {
            this.subscriptionType = subscriptionType;
        }

        public String getPaymentTerm() {
            return paymentTerm;
        }

        public void setPaymentTerm(String paymentTerm) {
            this.paymentTerm = paymentTerm;
        }

        public Integer getTermDurationID() {
            return termDurationID;
        }

        public void setTermDurationID(Integer termDurationID) {
            this.termDurationID = termDurationID;
        }

        public TermDuration getTermDuration() {
            return termDuration;
        }

        public void setTermDuration(TermDuration termDuration) {
            this.termDuration = termDuration;
        }

    }
    public class TermDuration {

        @SerializedName("TermDurationID")
        @Expose
        private Integer termDurationID;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("ValueInDays")
        @Expose
        private Integer valueInDays;

        public Integer getTermDurationID() {
            return termDurationID;
        }

        public void setTermDurationID(Integer termDurationID) {
            this.termDurationID = termDurationID;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getValueInDays() {
            return valueInDays;
        }

        public void setValueInDays(Integer valueInDays) {
            this.valueInDays = valueInDays;
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
        private Object profileImage;

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

        public Object getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(Object profileImage) {
            this.profileImage = profileImage;
        }

    }
}








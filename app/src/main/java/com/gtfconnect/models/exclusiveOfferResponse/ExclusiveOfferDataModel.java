package com.gtfconnect.models.exclusiveOfferResponse;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "exclusive_offer_data")
public class ExclusiveOfferDataModel {

    @PrimaryKey
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
    @SerializedName("IsTrending")
    @Expose
    private Integer isTrending;
    @SerializedName("MemberCount")
    @Expose
    private Integer memberCount;
    @SerializedName("TrendingLabel")
    @Expose
    private String trendingLabel;
    @SerializedName("ProfileImage")
    @Expose
    private String profileImage;
    @SerializedName("PopupInfoImage")
    @Expose
    private String popupInfoImage;
    @SerializedName("ShowPopupInfo")
    @Expose
    private Integer showPopupInfo;
    @SerializedName("setting")
    @Expose
    private Setting setting;

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

    public Integer getIsTrending() {
        return isTrending;
    }

    public void setIsTrending(Integer isTrending) {
        this.isTrending = isTrending;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public String getTrendingLabel() {
        return trendingLabel;
    }

    public void setTrendingLabel(String trendingLabel) {
        this.trendingLabel = trendingLabel;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPopupInfoImage() {
        return popupInfoImage;
    }

    public void setPopupInfoImage(String popupInfoImage) {
        this.popupInfoImage = popupInfoImage;
    }

    public Integer getShowPopupInfo() {
        return showPopupInfo;
    }

    public void setShowPopupInfo(Integer showPopupInfo) {
        this.showPopupInfo = showPopupInfo;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }



    public class Setting {

        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("FakeMemberCount")
        @Expose
        private Integer fakeMemberCount;

        public Integer getGroupChannelID() {
            return groupChannelID;
        }

        public void setGroupChannelID(Integer groupChannelID) {
            this.groupChannelID = groupChannelID;
        }

        public Integer getFakeMemberCount() {
            return fakeMemberCount;
        }

        public void setFakeMemberCount(Integer fakeMemberCount) {
            this.fakeMemberCount = fakeMemberCount;
        }

    }
}



package com.gtfconnect.models.groupResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostDeleteModel {

    @SerializedName("GroupChatID")
    @Expose
    private Integer groupChatID;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getGroupChatID() {
        return groupChatID;
    }

    public void setGroupChatID(Integer groupChatID) {
        this.groupChatID = groupChatID;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }



    public class Data {

        @SerializedName("deleteStatus")
        @Expose
        private List<Integer> deleteStatus;
        @SerializedName("requestData")
        @Expose
        private RequestData requestData;

        public List<Integer> getDeleteStatus() {
            return deleteStatus;
        }

        public void setDeleteStatus(List<Integer> deleteStatus) {
            this.deleteStatus = deleteStatus;
        }

        public RequestData getRequestData() {
            return requestData;
        }

        public void setRequestData(RequestData requestData) {
            this.requestData = requestData;
        }

    }
    public class RequestData {

        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("GCMemberID")
        @Expose
        private Integer gCMemberID;
        @SerializedName("GroupChatID")
        @Expose
        private Integer groupChatID;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;

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

    }
}
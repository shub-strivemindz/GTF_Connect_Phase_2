package com.gtfconnect.models.groupResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GroupCommentResponseModel {

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

        @SerializedName("comment")
        @Expose
        private Object comment;
        @SerializedName("chatDetail")
        @Expose
        private GroupChatResponseModel.Row chatDetail;

        public GroupChatResponseModel.Row getChatDetail() {
            return chatDetail;
        }

        public void setChatDetail(GroupChatResponseModel.Row chatDetail) {
            this.chatDetail = chatDetail;
        }

        public Object getComment() {
            return comment;
        }

        public void setComment(Object comment) {
            this.comment = comment;
        }

    }

}


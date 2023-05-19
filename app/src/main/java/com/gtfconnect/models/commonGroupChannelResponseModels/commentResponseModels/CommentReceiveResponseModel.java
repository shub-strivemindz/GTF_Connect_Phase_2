package com.gtfconnect.models.commonGroupChannelResponseModels.commentResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;


public class CommentReceiveResponseModel {

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
        private ChannelRowListDataModel chatDetail;

        public ChannelRowListDataModel getChatDetail() {
            return chatDetail;
        }

        public void setChatDetail(ChannelRowListDataModel chatDetail) {
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


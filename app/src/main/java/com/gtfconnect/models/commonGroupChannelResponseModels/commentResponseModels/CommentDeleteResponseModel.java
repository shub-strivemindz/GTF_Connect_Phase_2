package com.gtfconnect.models.commonGroupChannelResponseModels.commentResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatCommentModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatResponseModel;

import java.util.List;

public class CommentDeleteResponseModel {

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

        @SerializedName("commentList")
        @Expose
        private List<ChannelChatCommentModel> commentList;
        @SerializedName("comment")
        @Expose
        private List<Integer> comment;

        public List<ChannelChatCommentModel> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<ChannelChatCommentModel> commentList) {
            this.commentList = commentList;
        }

        public List<Integer> getComment() {
            return comment;
        }

        public void setComment(List<Integer> comment) {
            this.comment = comment;
        }

    }
}
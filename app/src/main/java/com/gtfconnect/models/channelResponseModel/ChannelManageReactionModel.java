package com.gtfconnect.models.channelResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelManageReactionModel {

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

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("list")
        @Expose
        private List<ListData> list;
        @SerializedName("current_page")
        @Expose
        private String currentPage;
        @SerializedName("from")
        @Expose
        private Integer from;
        @SerializedName("to")
        @Expose
        private Integer to;
        @SerializedName("per_page")
        @Expose
        private String perPage;
        @SerializedName("has_more")
        @Expose
        private Boolean hasMore;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<ListData> getList() {
            return list;
        }

        public void setList(List<ListData> list) {
            this.list = list;
        }

        public String getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }

        public String getPerPage() {
            return perPage;
        }

        public void setPerPage(String perPage) {
            this.perPage = perPage;
        }

        public Boolean getHasMore() {
            return hasMore;
        }

        public void setHasMore(Boolean hasMore) {
            this.hasMore = hasMore;
        }

    }
    public class ListData {

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
        @SerializedName("gcReactionStatus")
        @Expose
        private String gcReactionStatus;

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

        public String getGcReactionStatus() {
            return gcReactionStatus;
        }

        public void setGcReactionStatus(String gcReactionStatus) {
            this.gcReactionStatus = gcReactionStatus;
        }
    }
}








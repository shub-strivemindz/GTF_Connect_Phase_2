package com.gtfconnect.models.exclusiveOfferResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExclusiveOfferResponseModel {

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

        @SerializedName("list")
        @Expose
        private List<ExclusiveOfferDataModel> list;
        @SerializedName("current_page")
        @Expose
        private Integer currentPage;
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

        public List<ExclusiveOfferDataModel> getList() {
            return list;
        }

        public void setList(List<ExclusiveOfferDataModel> list) {
            this.list = list;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
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
}







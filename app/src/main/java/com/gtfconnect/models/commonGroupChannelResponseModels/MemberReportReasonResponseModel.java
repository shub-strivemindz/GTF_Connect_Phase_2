package com.gtfconnect.models.commonGroupChannelResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MemberReportReasonResponseModel {

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
        private List<ReasonList> list;
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

        public List<ReasonList> getList() {
            return list;
        }

        public void setList(List<ReasonList> list) {
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
    public class ReasonList {

        @SerializedName("ReportReasonID")
        @Expose
        private Integer reportReasonID;
        @SerializedName("ReasonText")
        @Expose
        private String reasonText;
        @SerializedName("ReasonCode")
        @Expose
        private String reasonCode;


        private boolean isReasonSelected = false;

        public boolean isReasonSelected() {
            return isReasonSelected;
        }

        public void setReasonSelected(boolean reasonSelected) {
            isReasonSelected = reasonSelected;
        }

        public Integer getReportReasonID() {
            return reportReasonID;
        }

        public void setReportReasonID(Integer reportReasonID) {
            this.reportReasonID = reportReasonID;
        }

        public String getReasonText() {
            return reasonText;
        }

        public void setReasonText(String reasonText) {
            this.reasonText = reasonText;
        }

        public String getReasonCode() {
            return reasonCode;
        }

        public void setReasonCode(String reasonCode) {
            this.reasonCode = reasonCode;
        }

    }
}


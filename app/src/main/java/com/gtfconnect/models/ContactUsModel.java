package com.gtfconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ContactUsModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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

    public class Data {

        @SerializedName("ContactUrl")
        @Expose
        private String contactUrl;
        @SerializedName("ContactFacebook")
        @Expose
        private String contactFacebook;
        @SerializedName("ContactInsta")
        @Expose
        private String contactInsta;
        @SerializedName("ContactEmail")
        @Expose
        private String contactEmail;
        @SerializedName("ContactNumber")
        @Expose
        private String contactNumber;
        @SerializedName("ContactAddress")
        @Expose
        private String contactAddress;
        @SerializedName("ContactTelegram")
        @Expose
        private String contactTelegram;

        public String getContactUrl() {
            return contactUrl;
        }

        public void setContactUrl(String contactUrl) {
            this.contactUrl = contactUrl;
        }

        public String getContactFacebook() {
            return contactFacebook;
        }

        public void setContactFacebook(String contactFacebook) {
            this.contactFacebook = contactFacebook;
        }

        public String getContactInsta() {
            return contactInsta;
        }

        public void setContactInsta(String contactInsta) {
            this.contactInsta = contactInsta;
        }

        public String getContactEmail() {
            return contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getContactAddress() {
            return contactAddress;
        }

        public void setContactAddress(String contactAddress) {
            this.contactAddress = contactAddress;
        }

        public String getContactTelegram() {
            return contactTelegram;
        }

        public void setContactTelegram(String contactTelegram) {
            this.contactTelegram = contactTelegram;
        }

    }
}
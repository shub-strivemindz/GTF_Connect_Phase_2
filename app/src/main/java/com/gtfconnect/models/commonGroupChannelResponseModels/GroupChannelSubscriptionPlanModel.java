package com.gtfconnect.models.commonGroupChannelResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupChannelSubscriptionPlanModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public class Datum {

        @SerializedName("GCSubscriptionPlanID")
        @Expose
        private Integer gCSubscriptionPlanID;
        @SerializedName("SubscriptionPlanID")
        @Expose
        private Integer subscriptionPlanID;
        @SerializedName("GroupChannelID")
        @Expose
        private Integer groupChannelID;
        @SerializedName("Badge")
        @Expose
        private Object badge;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("subscription_plan")
        @Expose
        private SubscriptionPlan subscriptionPlan;

        public Integer getGCSubscriptionPlanID() {
            return gCSubscriptionPlanID;
        }

        public void setGCSubscriptionPlanID(Integer gCSubscriptionPlanID) {
            this.gCSubscriptionPlanID = gCSubscriptionPlanID;
        }

        public Integer getSubscriptionPlanID() {
            return subscriptionPlanID;
        }

        public void setSubscriptionPlanID(Integer subscriptionPlanID) {
            this.subscriptionPlanID = subscriptionPlanID;
        }

        public Integer getGroupChannelID() {
            return groupChannelID;
        }

        public void setGroupChannelID(Integer groupChannelID) {
            this.groupChannelID = groupChannelID;
        }

        public Object getBadge() {
            return badge;
        }

        public void setBadge(Object badge) {
            this.badge = badge;
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

        public SubscriptionPlan getSubscriptionPlan() {
            return subscriptionPlan;
        }

        public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
            this.subscriptionPlan = subscriptionPlan;
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
        @SerializedName("ProductID")
        @Expose
        private Object productID;
        @SerializedName("ProductType")
        @Expose
        private Object productType;
        @SerializedName("ProductSubType")
        @Expose
        private Object productSubType;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("ProfileImage")
        @Expose
        private Object profileImage;
        @SerializedName("term_duration")
        @Expose
        private TermDuration termDuration;
        @SerializedName("product")
        @Expose
        private Object product;
        @SerializedName("product_name")
        @Expose
        private String productName;

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

        public Object getProductID() {
            return productID;
        }

        public void setProductID(Object productID) {
            this.productID = productID;
        }

        public Object getProductType() {
            return productType;
        }

        public void setProductType(Object productType) {
            this.productType = productType;
        }

        public Object getProductSubType() {
            return productSubType;
        }

        public void setProductSubType(Object productSubType) {
            this.productSubType = productSubType;
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

        public Object getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(Object profileImage) {
            this.profileImage = profileImage;
        }

        public TermDuration getTermDuration() {
            return termDuration;
        }

        public void setTermDuration(TermDuration termDuration) {
            this.termDuration = termDuration;
        }

        public Object getProduct() {
            return product;
        }

        public void setProduct(Object product) {
            this.product = product;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
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
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UpdatedAt")
        @Expose
        private String updatedAt;

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

    }
}

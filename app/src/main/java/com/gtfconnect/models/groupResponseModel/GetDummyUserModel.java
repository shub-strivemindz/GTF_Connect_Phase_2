package com.gtfconnect.models.groupResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GetDummyUserModel {

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
        private List<ListItems> list;

        public List<ListItems> getList() {
            return list;
        }

        public void setList(List<ListItems> list) {
            this.list = list;
        }

    }

    public class ListItems {

        @SerializedName("DummyUserID")
        @Expose
        private Integer dummyUserID;
        @SerializedName("isAdmin")
        @Expose
        private Integer isAdmin;
        @SerializedName("Firstname")
        @Expose
        private String firstname;
        @SerializedName("Lastname")
        @Expose
        private String lastname;

        public Integer getDummyUserID() {
            return dummyUserID;
        }

        public void setDummyUserID(Integer dummyUserID) {
            this.dummyUserID = dummyUserID;
        }

        public Integer getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(Integer isAdmin) {
            this.isAdmin = isAdmin;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

    }

}





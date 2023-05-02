package com.gtfconnect.roomDB.dbEntities.dashboardDbEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardResponseModel {



    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }



    public class Data {

        @SerializedName("gcImageBasePath")
        @Expose
        private String gcImageBasePath;
        @SerializedName("gcData")
        @Expose
        private List<DashboardListEntity> gcData;

        public String getGcImageBasePath() {
            return gcImageBasePath;
        }

        public void setGcImageBasePath(String gcImageBasePath) {
            this.gcImageBasePath = gcImageBasePath;
        }

        public List<DashboardListEntity> getGcData() {
            return gcData;
        }

        public void setGcData(List<DashboardListEntity> gcData) {
            this.gcData = gcData;
        }

    }

}




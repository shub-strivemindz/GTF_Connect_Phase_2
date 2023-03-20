package com.gtfconnect.models.groupResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupMessageReceivedModel {

        @SerializedName("saveMsg")
        @Expose
        private SaveMsg saveMsg;

        public SaveMsg getSaveMsg() {
            return saveMsg;
        }

        public void setSaveMsg(SaveMsg saveMsg) {
            this.saveMsg = saveMsg;
        }


        public class SaveMsg {

            @SerializedName("baseUrl")
            @Expose
            private String baseUrl;
            @SerializedName("getData")
            @Expose
            private GroupChatResponseModel.Row getData;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public GroupChatResponseModel.Row getGetData() {
                return getData;
            }

            public void setGetData(GroupChatResponseModel.Row getData) {
                this.getData = getData;
            }

        }
    }
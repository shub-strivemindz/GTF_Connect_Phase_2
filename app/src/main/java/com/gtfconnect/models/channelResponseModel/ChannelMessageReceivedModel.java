package com.gtfconnect.models.channelResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;

public class ChannelMessageReceivedModel {

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
            private ChannelRowListDataModel getData;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public ChannelRowListDataModel getGetData() {
                return getData;
            }

            public void setGetData(ChannelRowListDataModel getData) {
                this.getData = getData;
            }

        }
    }
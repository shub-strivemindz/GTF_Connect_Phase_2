package com.gtfconnect.models.channelResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
            private ChannelChatResponseModel.Row getData;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public ChannelChatResponseModel.Row getGetData() {
                return getData;
            }

            public void setGetData(ChannelChatResponseModel.Row getData) {
                this.getData = getData;
            }

        }
    }









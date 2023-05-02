package com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "channel_chat_header")
public class GroupChannelChatHeaderDbEntity {

    String baseUrl = "";

    @PrimaryKey
    @NonNull
    String groupChannelID = "";

    String subscriptionCount = "";

    String count = "";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getGroupChannelID() {
        return groupChannelID;
    }

    public void setGroupChannelID(String groupChannelID) {
        this.groupChannelID = groupChannelID;
    }

    public String getSubscriptionCount() {
        return subscriptionCount;
    }

    public void setSubscriptionCount(String subscriptionCount) {
        this.subscriptionCount = subscriptionCount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}

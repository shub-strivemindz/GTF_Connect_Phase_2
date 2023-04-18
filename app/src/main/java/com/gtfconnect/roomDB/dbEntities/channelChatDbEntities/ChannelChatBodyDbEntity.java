package com.gtfconnect.roomDB.dbEntities.channelChatDbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;

import retrofit2.http.Header;
/*, foreignKeys = @ForeignKey
        (entity = ChannelChatHeaderDbEntity.class,
                parentColumns = "groupChannelID",
                childColumns = "groupChannelId"
        )*/
@Entity(tableName = "channel_chat_body")
public class ChannelChatBodyDbEntity {

    @PrimaryKey
    @NonNull
    int groupChatID= 0;

    private String groupChannelId = "";

    private int page = 0;


    private ChannelRowListDataModel rows = new ChannelRowListDataModel();


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getGroupChannelId() {
        return groupChannelId;
    }

    public void setGroupChannelId(String groupChannelId) {
        this.groupChannelId = groupChannelId;
    }


    public int getGroupChatID() {
        return groupChatID;
    }

    public void setGroupChatID(int groupChatID) {
        this.groupChatID = groupChatID;
    }

    public String getGroupChannelID() {
        return groupChannelId;
    }

    public void setGroupChannelID(String groupChannelID) {
        this.groupChannelId = groupChannelID;
    }

    public ChannelRowListDataModel getRows() {
        return rows;
    }

    public void setRows(ChannelRowListDataModel rows) {
        this.rows = rows;
    }
}

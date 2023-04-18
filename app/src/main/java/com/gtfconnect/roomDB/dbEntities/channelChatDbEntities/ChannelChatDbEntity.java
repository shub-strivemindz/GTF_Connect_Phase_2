package com.gtfconnect.roomDB.dbEntities.channelChatDbEntities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

public class ChannelChatDbEntity {

    @Embedded
    public ChannelChatHeaderDbEntity chatHeaderDbEntity;

    @Relation(
            parentColumn = "groupChannelID",
            entityColumn = "groupChannelId"
    )
    public List<ChannelChatBodyDbEntity> chatBodyDbEntitiesLists;


    public ChannelChatHeaderDbEntity getChatHeaderDbEntity() {
        return chatHeaderDbEntity;
    }

    public void setChatHeaderDbEntity(ChannelChatHeaderDbEntity chatHeaderDbEntity) {
        this.chatHeaderDbEntity = chatHeaderDbEntity;
    }

    public List<ChannelChatBodyDbEntity> getChatBodyDbEntitiesLists() {
        return chatBodyDbEntitiesLists;
    }

    public void setChatBodyDbEntitiesLists(List<ChannelChatBodyDbEntity> chatBodyDbEntitiesLists) {
        this.chatBodyDbEntitiesLists = chatBodyDbEntitiesLists;
    }
}

package com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class GroupChannelChatDbEntity {

    @Embedded
    public GroupChannelChatHeaderDbEntity chatHeaderDbEntity;

    @Relation(
            parentColumn = "groupChannelID",
            entityColumn = "groupChannelId"
    )
    public List<GroupChannelChatBodyDbEntity> chatBodyDbEntitiesLists;


    public GroupChannelChatHeaderDbEntity getChatHeaderDbEntity() {
        return chatHeaderDbEntity;
    }

    public void setChatHeaderDbEntity(GroupChannelChatHeaderDbEntity chatHeaderDbEntity) {
        this.chatHeaderDbEntity = chatHeaderDbEntity;
    }

    public List<GroupChannelChatBodyDbEntity> getChatBodyDbEntitiesLists() {
        return chatBodyDbEntitiesLists;
    }

    public void setChatBodyDbEntitiesLists(List<GroupChannelChatBodyDbEntity> chatBodyDbEntitiesLists) {
        this.chatBodyDbEntitiesLists = chatBodyDbEntitiesLists;
    }
}

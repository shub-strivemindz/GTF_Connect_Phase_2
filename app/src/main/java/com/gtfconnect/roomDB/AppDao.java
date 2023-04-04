package com.gtfconnect.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gtfconnect.models.channelDashboardModels.ChannelDashboardDataModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatResponseModel;
import com.gtfconnect.models.channelDashboardModels.ChannelResponseModel;
import com.gtfconnect.models.groupDashboardModels.GroupDashboardDataModel;
import com.gtfconnect.models.groupDashboardModels.GroupResponseModel;

import java.util.List;

@Dao
public interface AppDao {

    // -------------------------------------------------------------------- Channel Dashboard Response Data -----------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertChannelDashboardData(ChannelDashboardDataModel channelResponseModel);

    @Query("delete from channel_dashboard_data")
    void delete_channel_dashboard_data();

    @Query("select * from  channel_dashboard_data")
    LiveData<List<ChannelDashboardDataModel>> getDashboardChannelData();



    // -------------------------------------------------------------------- Channel Chat Response Data -----------------------------------------------------


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertChannelChatData(ChannelChatResponseModel channelChatResponseModel);

    @Query("delete from channel_chat_data")
    void delete_channel_chat_data();

    @Query("select * from  channel_chat_data")
    LiveData<ChannelChatResponseModel> getChannelChatData();





    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertGroupDashboardData(GroupDashboardDataModel groupResponseModel);

    @Query("delete from group_dashboard_data")
    void delete_group_dashboard_data();

    @Query("select * from  group_dashboard_data")
    LiveData<List<GroupDashboardDataModel>> getGroupDashboardData();



    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRoom(RoomResult roomResult);*/
/*
//    @Query("UPDATE userdata SET viewRecipient=:viewRecipient")
//    void updateViewRecipient(boolean viewRecipient);

    @Query("delete from userdata")
    void delete();

    @Query("delete from roomdata")
    void deleteRoom();

    @Query("select * from userdata")
    LiveData<Result> getUser();

    @Query("select * from  roomdata")
    LiveData<RoomResult> getRoom();

    @Query("UPDATE userdata SET client_bean_name =:name,profilePic=:url,bio =:bio")
    void update(String name, String url, String bio);

    @Query("UPDATE roomdata SET backgroundImage=:url")
    void updateRoomTheme(String url);

    @Query("UPDATE roomdata SET seatedFreely=:url")
    void updateRadio(Boolean url);


    @Query("UPDATE roomdata SET name =:name,roomImage=:url")
    void updateEdit(String name, String url);

    @Query("UPDATE userdata SET tagIds =:tags")
    void updateTags(List<TagId> tags);

    @Query("UPDATE userdata SET frameUrl =:img")
    void updateFrame(String img);*/

}
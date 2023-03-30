package com.gtfconnect.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gtfconnect.models.channelResponseModel.channelDashboardModels.ChannelResponseModel;

import java.util.List;

@Dao
public interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChannelDashboardData(ChannelResponseModel channelResponseModel);

    @Query("delete from channel_dashboard_data")
    void delete();

    @Query("select * from  channel_dashboard_data")
    LiveData<ChannelResponseModel> getDashboardChannelData();

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
package com.gtfconnect.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;

import com.gtfconnect.roomDB.dbEntities.UserProfileDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatBodyDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatHeaderDbEntity;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GroupChannelGalleryEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;

import java.util.List;

@Dao
public interface AppDao {



    // -------------------------------------------------------------------- User Profile Response Data -----------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserProfileData(UserProfileDbEntity userProfileDbEntity);


    @Query("delete from user_profile_info_data")
    void delete_user_profile_data();


    @Query("select * from  user_profile_info_data")
    LiveData<UserProfileDbEntity> getUserProfileData();






    // -------------------------------------------------------------------- Exclusive Offer Response Data -----------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExclusiveOfferData(List<ExclusiveOfferDataModel> exclusiveOfferDataModel);

    @Query("delete from exclusive_offer_data where groupChannelID = :groupChannelID")
    void deleteExclusiveOffer(int groupChannelID);

    @Query("delete from exclusive_offer_data")
    void delete_exclusive_offer_data();

    @Query("select * from  exclusive_offer_data")
    LiveData<List<ExclusiveOfferDataModel>> getExclusiveOfferList();













    // -------------------------------------------------------------------- Save Image Response Data -----------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImageInGallery(GroupChannelGalleryEntity groupChannelGalleryEntity);


    @Query("delete from group_channel_gallery")
    void delete_gallery_data();


    @Query("select * from group_channel_gallery where GalleryType = 'PROFILE'")
    LiveData<GroupChannelGalleryEntity> get_profile_image();










    // ====================================================================== Group Channel Detail Info =======================================================



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroupChannelInfo(InfoDbEntity infoDbEntity);

    /*@Query("delete from exclusive_offer_data where groupChannelID = :groupChannelID")
    void deleteExclusiveOffer(int groupChannelID);*/

    @Query("delete from group_channel_info_data")
    void delete_group_channel_info_data();

    @Query("select * from  group_channel_info_data where groupChannelID = :groupChannelID")
    LiveData<InfoDbEntity> getGroupChannelInfo(int groupChannelID);

















    // -------------------------------------------------------------------- Dashboard Response Data -----------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDashboardData(List<DashboardListEntity> dashboardResponseModel);

    @Query("delete from dashboard_data")
    void delete_dashboard_data();

    @Query("select * from  dashboard_data where dashboardType = :dashboardType")
    LiveData<List<DashboardListEntity>> getDashboardData(String dashboardType);




    // -------------------------------------------------------------------- Channel Chat Response Data -----------------------------------------------------



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChannelChatHeaderData(GroupChannelChatHeaderDbEntity chatHeaderDbEntity);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChannelChatBodyData(List<GroupChannelChatBodyDbEntity> chatBodyDb);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChannelChatRowBodyData(GroupChannelChatBodyDbEntity chatBodyDb);


    @Query("delete from channel_chat_header")
    void delete_channel_header_data();

    @Query("delete from channel_chat_body")
    void delete_channel_body_data();


    @Query("select * from  channel_chat_body JOIN channel_chat_header ON channel_chat_header.groupChannelID = channel_chat_body.groupChannelId where channel_chat_body.groupChannelId = :id ORDER BY CASE WHEN :isAsc = 1 THEN channel_chat_body.groupChatID END DESC")
    LiveData<GroupChannelChatDbEntity> getChannelChatData(String id, int isAsc);




    @Query("delete from channel_chat_body where groupChatID = :groupChatID")
    void remove_chat_from_database(int groupChatID);



    @Query("delete from channel_chat_body where groupChannelId = :groupChannelID")
    void remove_group_channel_body_from_database(String groupChannelID);

    @Query("delete from channel_chat_header where groupChannelID = :groupChannelID")
    void remove_group_channel_header_from_database(int groupChannelID);



    //@Query("DELETE FROM channel_chat_body where  NOT IN (SELECT id from tableName ORDER BY id DESC LIMIT 20)")


    /*@Query("delete from channel_chat_header")
    void delete_channel_chat_data();


    @Query("select * from  channel_chat_header")
    LiveData<ChannelChatResponseModel> getChannelChatHeaderData();*/





 /* @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertChannelChatData(ChannelChatResponseModel channelChatResponseModel);

    @Query("delete from channel_chat_data")
    void delete_channel_chat_data();

    @Query("select * from  channel_chat_data")
    LiveData<ChannelChatResponseModel> getChannelChatData();*/




   /* @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertChannelChatRowData(ChannelChatResponseModel channelChatResponseModel);

    @Query("delete from channel_chat_data")
    void delete_channel_chat_row_data();

    @Query("select * from  channel_chat_data")
    LiveData<ChannelChatResponseModel> getChannelChatRowData();







*/












/*
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertGroupDashboardData(GroupDashboardDataModel groupResponseModel);

    @Query("delete from group_dashboard_data")
    void delete_group_dashboard_data();

    @Query("select * from  group_dashboard_data")
    LiveData<List<GroupDashboardDataModel>> getGroupDashboardData();*/



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
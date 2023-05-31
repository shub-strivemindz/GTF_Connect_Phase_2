package com.gtfconnect.roomDB;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;

import com.gtfconnect.roomDB.dbEntities.UserProfileDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatBodyDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatDbEntity;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GroupChannelGalleryEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;

import java.util.List;
import java.util.concurrent.Executors;

public class DatabaseRepo {

    private AppDao appDao;

    public DatabaseRepo(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        appDao = appDatabase.appDao();
    }


    // ================================================================ User Profile Data =====================================================================


    public void insertUserProfileData(UserProfileDbEntity userProfileDbEntity) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.delete_user_profile_data();
                appDao.insertUserProfileData(userProfileDbEntity);

            }
        });
    }

    public LiveData<UserProfileDbEntity> getUserProfileData() {
        return appDao.getUserProfileData();
    }












    // ================================================================ Gallery Data =====================================================================

    public void insertImageInGallery(GroupChannelGalleryEntity galleryEntity) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.delete_gallery_data();
                appDao.insertImageInGallery(galleryEntity);

            }
        });
    }

    public LiveData<GroupChannelGalleryEntity> getProfileImage() {
        return appDao.get_profile_image();
    }











// ==================================================================== Group Channel Detail Info =====================================================


    public void insertGroupChannelInfo(InfoDbEntity infoDbEntity) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.insertGroupChannelInfo(infoDbEntity);
            }
        });
    }

    public LiveData<InfoDbEntity> getGroupChannelInfo(int groupChannelID) {
        return appDao.getGroupChannelInfo(groupChannelID);
    }





















    // ================================================================ Channel Data =====================================================================

    public void insertExclusiveOfferData(List<ExclusiveOfferDataModel> exclusiveOfferDataModel) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.delete_exclusive_offer_data();
                appDao.insertExclusiveOfferData(exclusiveOfferDataModel);

            }
        });
    }



    public void deleteExclusiveOffer(int groupChannelID) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.deleteExclusiveOffer(groupChannelID);

            }
        });
    }



    public LiveData<List<ExclusiveOfferDataModel>> getExclusiveOfferData() {
        return appDao.getExclusiveOfferList();
    }















    // ================================================================ Channel Data =====================================================================

    public void insertDashboardData(List<DashboardListEntity> dashboardListEntities) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //appDao.delete_channel_dashboard_data();
                appDao.insertDashboardData(dashboardListEntities);

            }
        });
    }

    public LiveData<List<DashboardListEntity>> getDashboardData(String dashboardType) {
        return appDao.getDashboardData(dashboardType);
    }



    /*public void insertChannelChatData(ChannelChatResponseModel channelDashboardData) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.delete_channel_chat_data();
                appDao.insertChannelChatData(channelDashboardData);
            }
        });
    }
    public LiveData<ChannelChatResponseModel> getChannelChatData() {
        return appDao.getChannelChatData();
    }*/



    // ================================================================ Group Data =====================================================================
/*

    public void insertGroupDashboardData(GroupDashboardDataModel groupDashboardData) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.delete_group_dashboard_data();
                appDao.insertGroupDashboardData(groupDashboardData);

            }
        });
    }

    public LiveData<List<GroupDashboardDataModel>> getGroupDashboardData() {
        return appDao.getGroupDashboardData();
    }
*/






    public void delete_database(){
        appDao.delete_dashboard_data();
        appDao.delete_channel_body_data();
        appDao.delete_channel_header_data();
        appDao.delete_exclusive_offer_data();
        appDao.delete_user_profile_data();
        appDao.delete_group_channel_info_data();
        appDao.delete_gallery_data();
    }
















































    public void insertChannelRowData(GroupChannelChatBodyDbEntity chatBodyRowDb) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //appDao.delete();
                appDao.insertChannelChatRowBodyData(chatBodyRowDb);

            }
        });
    }




    public void insertChannelChatData(GroupChannelChatDbEntity channelChatDb) {
       /* Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.delete_channel_header_data();
                appDao.insertChannelChatHeaderData(channelChatDb.chatHeaderDbEntity);
            }
        });
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.delete_channel_body_data();
                appDao.insertChannelChatBodyData(channelChatDb.chatBodyDbEntitiesLists);
            }
        });*/
        new insertChannelChatAsync(appDao).execute(channelChatDb);
    }

    private static class insertChannelChatAsync extends AsyncTask<GroupChannelChatDbEntity, Void, Void> {
        private AppDao appDaoAsync;

        insertChannelChatAsync(AppDao appDao) {
            appDaoAsync = appDao;
        }

        @Override
        protected Void doInBackground(GroupChannelChatDbEntity... chatDbEntities) {

            appDaoAsync.insertChannelChatHeaderData(chatDbEntities[0].chatHeaderDbEntity);

            appDaoAsync.insertChannelChatBodyData(chatDbEntities[0].chatBodyDbEntitiesLists);
            return null;
        }
    }

    public LiveData<GroupChannelChatDbEntity> getChannelChatData(String groupChannelID, int isAsc) {
        return appDao.getChannelChatData(groupChannelID,isAsc);
    }






    public void remove_chat_from_database(int groupChatID){
        appDao.remove_chat_from_database(groupChatID);
    }


    public void remove_group_channel_from_database(int groupChatID){

        appDao.remove_group_channel_header_from_database(groupChatID);
        appDao.remove_group_channel_body_from_database(groupChatID+"");
    }
}
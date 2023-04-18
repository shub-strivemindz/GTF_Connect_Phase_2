package com.gtfconnect.roomDB;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.gtfconnect.models.channelDashboardModels.ChannelDashboardDataModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatResponseModel;
import com.gtfconnect.models.channelDashboardModels.ChannelResponseModel;
import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;
import com.gtfconnect.models.groupDashboardModels.GroupDashboardDataModel;
import com.gtfconnect.models.groupDashboardModels.GroupResponseModel;
import com.gtfconnect.roomDB.dbEntities.channelChatDbEntities.ChannelChatBodyDbEntity;
import com.gtfconnect.roomDB.dbEntities.channelChatDbEntities.ChannelChatDbEntity;

import java.util.List;
import java.util.concurrent.Executors;

public class DatabaseRepo {

    private AppDao appDao;

    public DatabaseRepo(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        appDao = appDatabase.appDao();
    }







    // ================================================================ Channel Data =====================================================================

    public void insertExclusiveOfferData(ExclusiveOfferDataModel exclusiveOfferDataModel) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.delete_exclusive_offer_data();
                appDao.insertExclusiveOfferData(exclusiveOfferDataModel);

            }
        });
    }

    public LiveData<List<ExclusiveOfferDataModel>> getExclusiveOfferData() {
        return appDao.getExclusiveOfferList();
    }















    // ================================================================ Channel Data =====================================================================

    public void insertChannelDashboardData(ChannelDashboardDataModel channelDashboardData) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.delete_channel_dashboard_data();
                appDao.insertChannelDashboardData(channelDashboardData);

            }
        });
    }

    public LiveData<List<ChannelDashboardDataModel>> getChannelDashboardData() {
        return appDao.getDashboardChannelData();
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






    public void delete_database(){
        appDao.delete_group_dashboard_data();
        appDao.delete_channel_dashboard_data();
        appDao.delete_channel_body_data();
        appDao.delete_channel_header_data();
        appDao.delete_exclusive_offer_data();
    }
















































    public void insertChannelRowData(ChannelChatBodyDbEntity chatBodyRowDb) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //appDao.delete();
                appDao.insertChannelChatRowBodyData(chatBodyRowDb);

            }
        });
    }




    public void insertChannelChatData(ChannelChatDbEntity channelChatDb) {
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

    private static class insertChannelChatAsync extends AsyncTask<ChannelChatDbEntity, Void, Void> {
        private AppDao appDaoAsync;

        insertChannelChatAsync(AppDao appDao) {
            appDaoAsync = appDao;
        }

        @Override
        protected Void doInBackground(ChannelChatDbEntity... chatDbEntities) {

            appDaoAsync.insertChannelChatHeaderData(chatDbEntities[0].chatHeaderDbEntity);

            appDaoAsync.insertChannelChatBodyData(chatDbEntities[0].chatBodyDbEntitiesLists);
            return null;
        }
    }

    public LiveData<ChannelChatDbEntity> getChannelChatData(String groupChannelID, int isAsc) {
        return appDao.getChannelChatData(groupChannelID,isAsc);
    }
}

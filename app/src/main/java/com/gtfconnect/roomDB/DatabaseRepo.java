package com.gtfconnect.roomDB;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.gtfconnect.models.channelDashboardModels.ChannelDashboardDataModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatResponseModel;
import com.gtfconnect.models.channelDashboardModels.ChannelResponseModel;
import com.gtfconnect.models.groupDashboardModels.GroupDashboardDataModel;
import com.gtfconnect.models.groupDashboardModels.GroupResponseModel;

import java.util.List;
import java.util.concurrent.Executors;

public class DatabaseRepo {

    private AppDao appDao;

    public DatabaseRepo(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        appDao = appDatabase.appDao();
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
}

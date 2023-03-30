package com.gtfconnect.roomDB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gtfconnect.models.channelResponseModel.channelDashboardModels.ChannelResponseModel;

import java.util.concurrent.Executors;

public class DatabaseRepo {

    private AppDao appDao;

    public DatabaseRepo(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        appDao = appDatabase.appDao();
    }

    public void insertUser(ChannelResponseModel channelDashboardData) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDao.delete();
                appDao.insertChannelDashboardData(channelDashboardData);

            }
        });
    }


    public LiveData<ChannelResponseModel> getChannelDashboardData() {
        return appDao.getDashboardChannelData();
    }
}

package com.gtfconnect.roomDB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gtfconnect.models.channelResponseModel.channelDashboardModels.ChannelResponseModel;

public class DatabaseViewModel extends AndroidViewModel {

    private DatabaseRepo repo;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        repo = new DatabaseRepo(application);
    }


    public void insertUser(ChannelResponseModel result) {
        repo.insertUser(result);
    }

    public LiveData<ChannelResponseModel> getChannelDashboardData() {
        return repo.getChannelDashboardData();
    }
}

package com.gtfconnect.roomDB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gtfconnect.models.channelDashboardModels.ChannelDashboardDataModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatResponseModel;
import com.gtfconnect.models.channelDashboardModels.ChannelResponseModel;
import com.gtfconnect.models.groupDashboardModels.GroupDashboardDataModel;
import com.gtfconnect.models.groupDashboardModels.GroupResponseModel;

import java.util.List;

public class DatabaseViewModel extends AndroidViewModel {

    private DatabaseRepo repo;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        repo = new DatabaseRepo(application);
    }


    public void insertChannels(ChannelDashboardDataModel result) {
        repo.insertChannelDashboardData(result);
    }

    public LiveData<List<ChannelDashboardDataModel>> getChannels() {
        return repo.getChannelDashboardData();
    }


    /*public void insertChannelChat(ChannelChatResponseModel result) {
        repo.insertChannelChatData(result);
    }

    public LiveData<ChannelChatResponseModel> getChannelChannelData() {
        return repo.getChannelChatData();
    }*/


    public void insertGroups(GroupDashboardDataModel result) {
        repo.insertGroupDashboardData(result);
    }

    public LiveData<List<GroupDashboardDataModel>> getGroups() {
        return repo.getGroupDashboardData();
    }
}

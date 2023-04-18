package com.gtfconnect.roomDB;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gtfconnect.models.channelDashboardModels.ChannelDashboardDataModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatResponseModel;
import com.gtfconnect.models.channelDashboardModels.ChannelResponseModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;
import com.gtfconnect.models.groupDashboardModels.GroupDashboardDataModel;
import com.gtfconnect.models.groupDashboardModels.GroupResponseModel;
import com.gtfconnect.roomDB.dbEntities.channelChatDbEntities.ChannelChatBodyDbEntity;
import com.gtfconnect.roomDB.dbEntities.channelChatDbEntities.ChannelChatDbEntity;

import java.util.List;

public class DatabaseViewModel extends AndroidViewModel {

    private DatabaseRepo repo;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        repo = new DatabaseRepo(application);
    }



    public void insertExclusiveOffer(ExclusiveOfferDataModel dataModel) {
        repo.insertExclusiveOfferData(dataModel);
    }

    public LiveData<List<ExclusiveOfferDataModel>> getExclusiveOfferData() {
        return repo.getExclusiveOfferData();
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




















    public void insertChannelChatData(ChannelChatDbEntity result) {
        repo.insertChannelChatData(result);
    }


    public void insertChannelChatRowData(ChannelChatBodyDbEntity channelRowListDataModel){
        repo.insertChannelRowData(channelRowListDataModel);
    }


    public LiveData<ChannelChatDbEntity> getChannelChatData(String groupChannelID, int isAsc) {
        Log.d("run","twice");
        return repo.getChannelChatData(groupChannelID,isAsc);
    }


















    public void delete_database()
    {
        repo.delete_database();
    }
}

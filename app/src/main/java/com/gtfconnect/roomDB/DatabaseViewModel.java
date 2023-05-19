package com.gtfconnect.roomDB;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;

import com.gtfconnect.roomDB.dbEntities.UserProfileDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatBodyDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatDbEntity;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GroupChannelGalleryEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;

import java.util.List;

public class DatabaseViewModel extends AndroidViewModel {

    private DatabaseRepo repo;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        repo = new DatabaseRepo(application);
    }





    public void insertUserProfileData(UserProfileDbEntity dataModel) {
        repo.insertUserProfileData(dataModel);
    }

    public LiveData<UserProfileDbEntity> getUserProfileData() {
        return  repo.getUserProfileData();
    }








    public void insertImageInGallery(GroupChannelGalleryEntity dataModel) {
        repo.insertImageInGallery(dataModel);
    }

    public LiveData<GroupChannelGalleryEntity> getProfileImage() {
        return  repo.getProfileImage();
    }







    public void insertGroupChannelInfo(InfoDbEntity dataModel) {
        repo.insertGroupChannelInfo(dataModel);
    }


    public LiveData<InfoDbEntity> getGroupChannelInfo(int groupChannelID) {
        return repo.getGroupChannelInfo(groupChannelID);
    }










    public void insertExclusiveOffer(List<ExclusiveOfferDataModel> dataModel) {
        repo.insertExclusiveOfferData(dataModel);
    }



    public void deleteExclusiveOffer(int groupChannelID) {
        repo.deleteExclusiveOffer(groupChannelID);
    }



    public LiveData<List<ExclusiveOfferDataModel>> getExclusiveOfferData() {
        return Transformations.distinctUntilChanged(repo.getExclusiveOfferData());
    }









    public void insertDashboardData(List<DashboardListEntity> result) {
        repo.insertDashboardData(result);
    }

    public LiveData<List<DashboardListEntity>> getDashboardList(String dashboardType) {
        return repo.getDashboardData(dashboardType);
    }


    /*public void insertChannelChat(ChannelChatResponseModel result) {
        repo.insertChannelChatData(result);
    }

    public LiveData<ChannelChatResponseModel> getChannelChannelData() {
        return repo.getChannelChatData();
    }*/

/*

    public void insertGroups(GroupDashboardDataModel result) {
        repo.insertGroupDashboardData(result);
    }

    public LiveData<List<GroupDashboardDataModel>> getGroups() {
        return repo.getGroupDashboardData();
    }
*/




















    public void insertChannelChatData(GroupChannelChatDbEntity result) {
        repo.insertChannelChatData(result);
    }


    public void insertChannelChatRowData(GroupChannelChatBodyDbEntity channelRowListDataModel){
        repo.insertChannelRowData(channelRowListDataModel);
    }


    public LiveData<GroupChannelChatDbEntity> getGroupChannelChatData(String groupChannelID, int isAsc) {
        Log.d("run","twice");
        return repo.getChannelChatData(groupChannelID,isAsc);
    }



    public void remove_chat_from_database(int groupChatID){
        repo.remove_chat_from_database(groupChatID);
    }


    public void remove_group_channel_from_database(int groupChannelD){
        repo.remove_group_channel_from_database(groupChannelD);
    }










    public void delete_database()
    {
        repo.delete_database();
    }
}

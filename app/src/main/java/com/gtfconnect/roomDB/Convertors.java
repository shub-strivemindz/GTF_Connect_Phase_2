package com.gtfconnect.roomDB;

import android.util.Log;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatCommentModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatResponseModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelLikeModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelMediaResponseModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.models.channelDashboardModels.ChannelDashboardDataModel;
import com.gtfconnect.models.groupDashboardModels.GroupDashboardDataModel;

import java.lang.reflect.Type;
import java.util.List;

public class Convertors {


    @TypeConverter
    public ChannelDashboardDataModel.Group jsonToChannelDashboardData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<ChannelDashboardDataModel.Group>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String channelDashboardDataToJson(ChannelDashboardDataModel.Group specializations) {
        return new Gson().toJson(specializations);
    }


   /* @TypeConverter
    public List<ChannelRowListDataModel> jsonToChannelChatRowData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<List<ChannelRowListDataModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String channelChatRowDataToJson(List<ChannelRowListDataModel> specializations) {
        return new Gson().toJson(specializations);
    }

    @TypeConverter
    public List<ChannelLikeModel> jsonToChannelLikeData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<List<ChannelLikeModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String channelLikeDataToJson(List<ChannelLikeModel> specializations) {
        return new Gson().toJson(specializations);
    }


    @TypeConverter
    public List<ChannelChatCommentModel> jsonToChannelChatCommentData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<List<ChannelChatCommentModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String channelChatCommentDataToJson(List<ChannelChatCommentModel> specializations) {
        return new Gson().toJson(specializations);
    }



    @TypeConverter
    public List<ChannelMediaResponseModel> jsonToChannelMediaData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<List<ChannelMediaResponseModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String channelMediaDataToJson(List<ChannelMediaResponseModel> specializations) {
        return new Gson().toJson(specializations);
    }*/


    @TypeConverter
    public ChannelChatResponseModel.Data jsonToChannelChatData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<ChannelChatResponseModel.Data>() {
        }.getType();


        Log.d("LocalDB",json);
        return gson.fromJson(json, type);


    }

    @TypeConverter
    public String channelChatDataToJson(ChannelChatResponseModel.Data specializations) {
        return new Gson().toJson(specializations);
    }







    @TypeConverter
    public GroupDashboardDataModel.Group jsonToGroupDashboardData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<GroupDashboardDataModel.Group>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String groupDashboardDataToJson(GroupDashboardDataModel.Group specializations) {
        return new Gson().toJson(specializations);
    }

}

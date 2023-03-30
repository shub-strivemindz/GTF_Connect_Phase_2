package com.gtfconnect.roomDB;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.models.channelResponseModel.channelDashboardModels.ChannelDashboardDataModel;

import java.lang.reflect.Type;
import java.util.List;

public class Convertors {


    @TypeConverter
    public List<ChannelDashboardDataModel> jsonToChannelDashboardData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<List<ChannelDashboardDataModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String channelDashboardDataToJson(List<ChannelDashboardDataModel> specializations) {
        return new Gson().toJson(specializations);
    }
}

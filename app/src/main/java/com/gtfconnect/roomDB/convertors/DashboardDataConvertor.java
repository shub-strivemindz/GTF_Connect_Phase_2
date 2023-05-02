package com.gtfconnect.roomDB.convertors;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;

import java.lang.reflect.Type;

public class DashboardDataConvertor {



    @TypeConverter
    public DashboardListEntity.Group jsonToDashboardData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<DashboardListEntity.Group>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String DashboardDataToJson(DashboardListEntity.Group specializations) {
        return new Gson().toJson(specializations);
    }

}

package com.gtfconnect.roomDB.convertors;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.roomDB.dbEntities.UserProfileDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;

import java.lang.reflect.Type;
import java.util.List;

public class UserProfileDataConvertor {



    @TypeConverter
    public UserProfileDbEntity.ProfileInfo jsonToProfileData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<UserProfileDbEntity.ProfileInfo>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String ProfileDataToJson(UserProfileDbEntity.ProfileInfo specializations) {
        return new Gson().toJson(specializations);
    }





    @TypeConverter
    public UserProfileDbEntity.UserRoleInfo jsonToUserRoleInfoData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<UserProfileDbEntity.UserRoleInfo>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String UserRoleInfoDataToJson(UserProfileDbEntity.UserRoleInfo specializations) {
        return new Gson().toJson(specializations);
    }





    @TypeConverter
    public List<UserProfileDbEntity.UserSetting> jsonToUserSettingData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<List<UserProfileDbEntity.UserSetting>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String UserSettingDataToJson(List<UserProfileDbEntity.UserSetting> specializations) {
        return new Gson().toJson(specializations);
    }








    @TypeConverter
    public List<Object> jsonToUserPermissionData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String UserPermissionDataToJson(List<Object> specializations) {
        return new Gson().toJson(specializations);
    }

}

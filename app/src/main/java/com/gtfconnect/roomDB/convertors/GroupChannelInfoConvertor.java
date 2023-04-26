package com.gtfconnect.roomDB.convertors;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;

import java.lang.reflect.Type;
import java.util.List;

public class GroupChannelInfoConvertor {




    @TypeConverter
    public InfoDbEntity.GcInfo jsonToGcInfoData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<InfoDbEntity.GcInfo>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String GcInfoDataToJson(InfoDbEntity.GcInfo specializations) {
        return new Gson().toJson(specializations);
    }






    @TypeConverter
    public InfoDbEntity.GcSetting jsonToGcSettingData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<InfoDbEntity.GcSetting>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String GcSettingDataToJson(InfoDbEntity.GcSetting specializations) {
        return new Gson().toJson(specializations);
    }





    @TypeConverter
    public InfoDbEntity.GcMemberInfo jsonToGcMemberInfoData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<InfoDbEntity.GcMemberInfo>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String GcMemberInfoDataToJson(InfoDbEntity.GcMemberInfo specializations) {
        return new Gson().toJson(specializations);
    }




    @TypeConverter
    public InfoDbEntity.GcMemberSetting jsonToGcMemberSettingData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<InfoDbEntity.GcMemberSetting>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String GcMemberSettingDataToJson(InfoDbEntity.GcMemberSetting specializations) {
        return new Gson().toJson(specializations);
    }





    @TypeConverter
    public InfoDbEntity.GcPermission jsonToGcPermissionData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<InfoDbEntity.GcPermission>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String GcPermissionDataToJson(InfoDbEntity.GcPermission specializations) {
        return new Gson().toJson(specializations);
    }




    @TypeConverter
    public InfoDbEntity.GcAdmin jsonToGcAdminData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<InfoDbEntity.GcAdmin>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String GcAdminDataToJson(InfoDbEntity.GcAdmin specializations) {
        return new Gson().toJson(specializations);
    }




    @TypeConverter
    public InfoDbEntity.GcMemberSubscriptionPlan jsonToGcMemberSubscriptionPlanData(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<InfoDbEntity.GcMemberSubscriptionPlan>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String GcMemberSubscriptionPlanDataToJson(InfoDbEntity.GcMemberSubscriptionPlan specializations) {
        return new Gson().toJson(specializations);
    }




    @TypeConverter
    public List<InfoDbEntity.GcSubscriptionPlan> jsonToSubscriptionPlanListData(String json) {


        Gson gson = new Gson();
        Type type = new TypeToken<List<InfoDbEntity.GcSubscriptionPlan>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String SubscriptionPlanListDataToJson(List<InfoDbEntity.GcSubscriptionPlan> specializations) {
        return new Gson().toJson(specializations);
    }
}

package com.gtfconnect.database.repository;

import android.app.Application;

import com.google.gson.JsonElement;
import com.gtfconnect.controller.RestAdapter;
import com.gtfconnect.controller.RestService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

public class ConnectRepo {

    private final RestService restService;

    public ConnectRepo(Application application) {
        restService = RestAdapter.getAuthProfileAdapter(application.getApplicationContext());
    }


    public Observable<JsonElement> getUserProfile(String api_token) {
        return restService.getUserProfile(api_token);
    }

    public Observable<JsonElement> get_groupChannel_subscription(int id,String endPoint, String api_token,String device_type, String device_token) {
        return restService.get_groupChannel_subscription(id, endPoint, api_token, device_type, device_token);
    }



    public Observable<JsonElement> update_group_channel_profile(int id, String api_token,String device_type, String device_token, Map<String,Object> params) {
        return restService.update_group_channel_profile(id, api_token, device_type, device_token,params);
    }



    public Observable<JsonElement> get_group_channel_info(int id, String api_token,String device_type, String device_token) {
        return restService.get_group_channel_info(id, api_token, device_type, device_token);
    }




    public Observable<JsonElement> update_group_channel_permission_settings(int id, String api_token,String device_type, String device_token, Map<String,Object> params) {
        return restService.update_group_channel_permission_settings(id, api_token, device_type, device_token,params);
    }


    public Observable<JsonElement> get_group_channel_manage_reaction_list(int id, String api_token,String device_type, String device_token, int page,int per_page, int isActive) {
        return restService.get_group_channel_manage_reaction_list(id, api_token, device_type, device_token,page,per_page,isActive);
    }



    public Observable<JsonElement> get_group_channel_manage_subscriber_list(int id, String api_token,String device_type, String device_token, int page,int per_page) {
        return restService.get_group_channel_manage_subscriber_list(id, api_token, device_type, device_token,page,per_page);
    }


    public Observable<JsonElement> update_group_channel_settings(int id, String api_token,String device_type, String device_token, Map<String,Object> params) {
        return restService.update_group_channel_settings(id, api_token, device_type, device_token,params);
    }




    public Observable<JsonElement> update_group_channel_reaction_list(int id, String api_token,String device_type, String device_token, Map<String,Object> params) {
        return restService.update_group_channel_reaction_list(id, api_token, device_type, device_token,params);
    }


    public Observable<JsonElement> get_exclusive_offers(String api_token, String device_type, String device_token,String search, int page,int per_page) {
        return restService.get_exclusive_offers(api_token,device_type,device_token,page,per_page);
    }



    public Observable<JsonElement> get_dummy_user_list(int id, String api_token,String device_type, String device_token) {
        return restService.get_group_dummy_user_list(id, api_token, device_type, device_token);
    }


    public Observable<JsonElement> update_dummy_user_list(int id, String api_token,String device_type, String device_token,Map<String,Object> params) {
        return restService.update_group_dummy_user_list(id, api_token, device_type, device_token,params);
    }


    public Observable<JsonElement> update_group_channel_reactions_settings(int id,String endPoint, String api_token,String device_type, String device_token, Map<String,Object> params) {
        return restService.update_group_channel_reactions_settings(id, endPoint, api_token, device_type, device_token,params);
    }



    public Observable<JsonElement> get_group_channel_member_media(int id, String api_token,String device_type, String device_token,String memberID) {
        return restService.get_group_channel_member_media(id,api_token,device_type,device_token,memberID);
    }




    public Observable<JsonElement> get_saved_messages(String api_token,String device_type, String device_token,int per_page,int page) {
        return restService.get_saved_messages(api_token,device_type,device_token,per_page,page);
    }




    public Observable<JsonElement> save_group_channel_message(String api_token,String device_type, String device_token,int channelID,String action,Map<String,Object> chatIDList) {
        return restService.save_group_channel_message(api_token,device_type,device_token,channelID,action,chatIDList);
    }

    public Observable<JsonElement> save_personal_message(String api_token, String device_type, String device_token, String message, String action, List<MultipartBody.Part> files) {
        return restService.save_personal_message(api_token,device_type,device_token,message,action,files);
    }



    public Observable<JsonElement> delete_saved_message(int id,String api_token,String device_type, String device_token) {
        return restService.delete_saved_message(id,api_token,device_type,device_token);
    }





    public Observable<JsonElement> leave_group_channel(int id,String api_token,String device_type, String device_token) {
        return restService.leave_group_channel(id,api_token,device_type,device_token);
    }


    public Observable<JsonElement> rejoin_group_channel(int id,String api_token,String device_type, String device_token) {
        return restService.rejoin_group_channel(id,api_token,device_type,device_token);
    }

    public Observable<JsonElement> join_group_channel(int id,String api_token,String device_type, String device_token,Map<String,Object> params) {
        return restService.join_group_channel(id,api_token,device_type,device_token,params);
    }

}

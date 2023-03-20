package com.gtfconnect.database.repository;

import android.app.Application;

import com.google.gson.JsonElement;
import com.gtfconnect.controller.RestAdapter;
import com.gtfconnect.controller.RestService;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

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


    public Observable<JsonElement> get_admin_group_channel_settings(int id,String endPoint, String api_token,String device_type, String device_token) {
        return restService.get_admin_group_channel_settings(id, endPoint, api_token, device_type, device_token);
    }



    public Observable<JsonElement> update_group_channel_profile(int id,String endPoint, String api_token,String device_type, String device_token, Map<String,Object> params) {
        return restService.update_group_channel_profile(id, endPoint, api_token, device_type, device_token,params);
    }



    public Observable<JsonElement> update_group_channel_permission_settings(int id,String endPoint, String api_token,String device_type, String device_token, Map<String,Object> params) {
        return restService.update_group_channel_permission_settings(id, endPoint, api_token, device_type, device_token,params);
    }



    public Observable<JsonElement> update_group_channel_reactions_settings(int id,String endPoint, String api_token,String device_type, String device_token, Map<String,Object> params) {
        return restService.update_group_channel_reactions_settings(id, endPoint, api_token, device_type, device_token,params);
    }
}

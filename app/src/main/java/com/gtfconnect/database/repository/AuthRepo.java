package com.gtfconnect.database.repository;

import android.app.Application;

import com.google.gson.JsonElement;
import com.gtfconnect.controller.RestAdapter;
import com.gtfconnect.controller.RestService;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

public class AuthRepo {
    /*private final AppDao appDao;
    private final AppDatabase appDatabase;*/
    private final RestService restService;



    public AuthRepo(Application application) {
        restService = RestAdapter.getAuthAdapter(application.getApplicationContext());

    }


    public Observable<JsonElement> login(String device_type,String device_token,Map<String, Object> map) {
        return restService.login(device_type,device_token,map);
    }

    public Observable<JsonElement> registration(String deviceToken,Map<String, Object> map) {
        return restService.registration(deviceToken,map);
    }

    public Observable<JsonElement> verifyOTP(Map<String, Object> map) {
        return restService.verifyOTP(map);
    }

    public Observable<JsonElement> resendOTP(String email) {
        return restService.resendOTP(email);
    }

    public Observable<JsonElement> forgetPassword(String email) {
        return restService.forgetPassword(email);
    }


    public Observable<JsonElement> updatePassword(String api_token,String device_type,String device_token,Map<String,Object> params) {
        return restService.updatePassword(api_token, device_type, device_token, params);
    }



    public Observable<JsonElement> getCountryList() {
        return restService.getCountryData();
    }


    public Observable<JsonElement> getStateList(int countryCode) {
        return restService.getStateList(countryCode);
    }


    public Observable<JsonElement> getCityList(int stateCode) {
        return restService.getCityList(stateCode);
    }

    public Observable<JsonElement> getUserProfile(String api_token) {
        return restService.getUserProfile(api_token);
    }



    public Observable<JsonElement> getContactUs() {
        return restService.get_contact_us();
    }



    public Observable<JsonElement> updateUserProfile(String api_token,String device_type,String device_token,String fromGtfConnect,Map<String,Object> params) {
        return restService.updateUserProfile(api_token,device_type,device_token,fromGtfConnect,params);
    }




    public Observable<JsonElement> update_profile_pic(String api_token, int userID, MultipartBody.Part image) {
        return restService.update_profile_pic(api_token,userID,image);
    }


    public Observable<JsonElement> get_cms_data(String device_type,String device_token,String type){
        return restService.get_cms_page_data(device_type,device_token,type);
    }


}

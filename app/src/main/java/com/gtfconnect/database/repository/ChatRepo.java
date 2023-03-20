package com.gtfconnect.database.repository;

import android.app.Application;

import com.google.gson.JsonElement;
import com.gtfconnect.controller.RestAdapter;
import com.gtfconnect.controller.RestService;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.FieldMap;
import retrofit2.http.PartMap;

public class ChatRepo {


    private final RestService restService;


    public ChatRepo(Application application) {
        restService = RestAdapter.getChatAdapter(application.getApplicationContext());
    }


    public Observable<JsonElement> pinMessage(Map<String, Object> param) {
        return restService.pinMessage(param);
    }


    public Observable<JsonElement> getPinnedMessages(Map<String, Object> param) {
        return restService.getPinnedMessages(param);
    }


    public Observable<JsonElement> removePinnedMessage(Map<String, Object> param) {
        return restService.removePinnedMessage(param);
    }


    public Observable<JsonElement> removeAllPinnedMessage(Map<String, Object> param) {
        return restService.removeAllPinnedMessage(param);
    }


    public Observable<JsonElement> getEmojiList() {
        return restService.getEmojiList();
    }


   public Observable<JsonElement> attachFile(Map<String, Object> params,
                                             List<MultipartBody.Part> files) {
        return restService.attachFile(
                params,files);
    }
}

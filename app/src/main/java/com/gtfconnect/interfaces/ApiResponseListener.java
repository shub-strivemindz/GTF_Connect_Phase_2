package com.gtfconnect.interfaces;

import static com.gtfconnect.controller.Status.LOADING;

import android.util.Log;

import com.google.gson.JsonObject;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.exceptions.AuthenticationException;
import com.gtfconnect.exceptions.ForbiddenException;
import com.gtfconnect.exceptions.LaunchException;
import com.gtfconnect.exceptions.ServerException;

public interface ApiResponseListener {

    default void  putResponse(ApiResponse apiResponse, Rest rest)
    {
        Log.v("ButtonIssue","status: "+apiResponse.status);
        switch (apiResponse.status) {
            case LOADING:
                onLoading();
                break;

            case SUCCESS:
                Log.v("ButtonIssue","SUCCESS");
                rest.dismissProgressdialog();
                if (apiResponse.data.getAsJsonObject().has("data")) {
                    onDataRender(apiResponse.data.getAsJsonObject());
                } else {
                    onResponseRender(apiResponse.data.getAsJsonObject());
                }
                break;

            case ERROR:
                Log.v("ButtonIssue","ERROR");
                rest.dismissProgressdialog();
                if (apiResponse.error instanceof AuthenticationException) {
                    onAuthFailure(apiResponse.error.getMessage());
                } else if (apiResponse.error instanceof ServerException) {
                    onServerFailure(apiResponse.error.getMessage());
                } else if (apiResponse.error instanceof ForbiddenException) {
                    onForbidden(apiResponse.error.getMessage());
                } else if (apiResponse.error instanceof LaunchException) {
                    if (apiResponse.data.getAsJsonObject().has("data")) {
                        onLaunchFailure(apiResponse.data.getAsJsonObject());
                    }
                }else {
                    onOtherFailure(apiResponse.error.getMessage());
                }
                break;

            default:
                Log.v("ButtonIssue","default");
                rest.dismissProgressdialog();
                break;
        }
    }

    void onLoading();
    void onDataRender(JsonObject jsonObject);
    void onResponseRender(JsonObject jsonObject);
    void onAuthFailure(String message);
    void onServerFailure(String message);
    void onForbidden(String message);
    void onLaunchFailure(JsonObject jsonObject);
    void onOtherFailure(String message);


}

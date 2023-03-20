package com.gtfconnect.controller;



import static com.gtfconnect.controller.Status.ERROR;
import static com.gtfconnect.controller.Status.LOADING;
import static com.gtfconnect.controller.Status.SUCCESS;

import android.util.Log;

import com.google.gson.JsonElement;
import com.gtfconnect.exceptions.AuthenticationException;
import com.gtfconnect.exceptions.ForbiddenException;
import com.gtfconnect.exceptions.LaunchException;
import com.gtfconnect.exceptions.ServerException;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class ApiResponse {
    public final Status status;

    @Nullable
    public final JsonElement data;

    @Nullable
    public final Throwable error;

    private ApiResponse(Status status, @Nullable JsonElement data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
        Log.v("ButtonIssue","Api response Constructor");
    }
    public static ApiResponse loading() {
        return new ApiResponse(LOADING, null, null);
    }

    public static ApiResponse success(@NonNull JsonElement data) {


        if (data.isJsonNull())
        {
            return new ApiResponse(ERROR,null,new NullPointerException("Null response occurs"));
        }
        if (data.getAsJsonObject().get("status").getAsInt()==401)
        {

            return new ApiResponse(ERROR,null,new AuthenticationException(data.getAsJsonObject().get("message").getAsString()));
        }
        if (data.getAsJsonObject().get("status").getAsInt()==500)
        {
            return new ApiResponse(ERROR,null,new ServerException(data.getAsJsonObject().get("message").getAsString()));
        }
        if (data.getAsJsonObject().get("status").getAsInt()==400)
        {
            return new ApiResponse(ERROR,null,new ServerException(data.getAsJsonObject().get("message").getAsString()));
        }
        if (data.getAsJsonObject().get("status").getAsInt()==403)
        {
            return new ApiResponse(ERROR,null,new ForbiddenException(data.getAsJsonObject().get("message").getAsString()));
        }
        if (data.getAsJsonObject().get("status").getAsInt()==406 || data.getAsJsonObject().get("status").getAsInt()==423)
        {
            return new ApiResponse(ERROR,data,new LaunchException(data.getAsJsonObject().get("message").getAsString()));
        }
        Log.v("ButtonIssue","Api response SUCCESS");
        return new ApiResponse(SUCCESS, data, null);
    }

    public static ApiResponse error(@NonNull Throwable error) {
        Log.v("ButtonIssue","Api response ERROR"+error.getMessage());
        return new ApiResponse(ERROR, null, error);
    }

}

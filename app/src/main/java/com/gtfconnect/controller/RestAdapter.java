package com.gtfconnect.controller;


import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestAdapter {
    //private static final String API_AUTH_URL = ApiUrls.BASE_AUTH_URL;
    private static final String API_AUTH_URL = ApiUrls.AUTH_BASE_URL;
    private static final String API_CONNECT_V1_URL = ApiUrls.GET_CONNECT_V1_BASE_URL;

    private static final String API_CONNECT_URL = ApiUrls.GET_CONNECT_BASE_URL;

   /* public static RestService getAuthAdapter(Context context) {
        OkHttpClient client = getOkHttpClient(context);

//        client.interceptors().add(new AddCookiesInterceptor());
       // client.interceptors().add(new ReceivedCookiesInterceptor());
        return new Retrofit.Builder()
                .baseUrl(API_AUTH_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)

                .addConverterFactory(GsonConverterFactory.create())
                .build()

                .create(RestService.class);
    }*/

    public static RestService getChatAdapter(Context context) {
        OkHttpClient client = getOkHttpClient(context);

//        client.interceptors().add(new AddCookiesInterceptor());
        // client.interceptors().add(new ReceivedCookiesInterceptor());
        return new Retrofit.Builder()
                .baseUrl(API_CONNECT_V1_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)

                .addConverterFactory(GsonConverterFactory.create())
                .build()

                .create(RestService.class);
    }

    public static RestService getAuthAdapter(Context context) {
        OkHttpClient client = getOkHttpClient(context);

        //client.interceptors().add(new AddCookiesInterceptor());
        // client.interceptors().add(new ReceivedCookiesInterceptor());
        return new Retrofit.Builder()
                .baseUrl(API_AUTH_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)

                .addConverterFactory(GsonConverterFactory.create())
                .build()

                .create(RestService.class);
    }


    public static RestService getAuthProfileAdapter(Context context) {
        OkHttpClient client = getOkHttpClient(context);

        //client.interceptors().add(new AddCookiesInterceptor());
        // client.interceptors().add(new ReceivedCookiesInterceptor());
        return new Retrofit.Builder()
                .baseUrl(API_CONNECT_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)

                .addConverterFactory(GsonConverterFactory.create())
                .build()

                .create(RestService.class);
    }


    private static OkHttpClient getOkHttpClient(Context context) {

        OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();


        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Log.e("response", message));
        //okClientBuilder.addInterceptor(new AddCookiesInterceptor(context)); // VERY VERY IMPORTANT
        //okClientBuilder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okClientBuilder.addInterceptor(httpLoggingInterceptor);


        long CONNECTION_TIMEOUT = 200;
        okClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);




        return okClientBuilder.build();
    }


}

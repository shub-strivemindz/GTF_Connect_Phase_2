package com.gtfconnect.services;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.gtfconnect.controller.ApiUrls;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


public class SocketService  extends Application {
    public static Socket socketInstance;

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        createSocket();
        SocketService.context = getApplicationContext();
        socketInstance.connect();
    }

    private void createSocket() {
        try {
            socketInstance = IO.socket(ApiUrls.SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocketInstance() {
        return socketInstance;
    }

    public static Context getAppContext() {
        return SocketService.context;
    }
}
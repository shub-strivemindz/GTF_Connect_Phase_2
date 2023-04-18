package com.gtfconnect.services;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.gtfconnect.controller.ApiUrls;

import java.net.URISyntaxException;
import java.util.logging.Handler;

import io.socket.client.IO;
import io.socket.client.Socket;


public class SocketService  extends Application {
    public static Socket socketInstance;

    public static Context context;



    @Override
    public void onCreate() {
        super.onCreate();

        //Handler handler = new Handler(Looper.getMainLooper());

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




    /**
     * Can get crashed as it holds 5 seconds delay recursive socket function :::::
     *//*

    public void initializeSocketRunCheck()
    {
        handler.postDelayed(new Runnable() {
            public void run() {
                socketCheck();
                handler.postDelayed(this,FIVE_SECONDS_DELAY);
            }
        }, FIVE_SECONDS_DELAY);
    }

    public void de_initializeSocketRunCheck()
    {
        handler.removeCallbacksAndMessages(null);
    }

    public void socketCheck()
    {
        if (socketInstance ==null || !socketInstance.connected())
        {
            // rest.ShowDialogue();

            SocketService instance = (SocketService) getApplication();
            socketInstance = instance.getSocketInstance();
            socketInstance.connect();

            Log.d("Socket -----", String.valueOf(socketInstance.connected()));
        }
        else{
            rest.dismissProgressdialog();
        }
    }*/

}
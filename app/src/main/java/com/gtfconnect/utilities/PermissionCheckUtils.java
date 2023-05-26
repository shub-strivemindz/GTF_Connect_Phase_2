package com.gtfconnect.utilities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


import java.util.Objects;

public class PermissionCheckUtils  {

    public static boolean checkChatPermissions(Context mContext,int requestType){

        boolean permissionsNotFound;

        if (requestType == Constants.REQUEST_ALL_MEDIA_PERMISSIONS){
            permissionsNotFound = checkAllMediaPermissions(mContext);
        } else if (requestType == Constants.REQUEST_ATTACHMENT_MEDIA_PERMISSIONS) {
            permissionsNotFound = checkAttachmentPermissions(mContext);
        }
        else if (requestType == Constants.REQUEST_AUDIO_PERMISSIONS){
            permissionsNotFound = checkAudioPermissions(mContext);
        }
        else{
            permissionsNotFound = false;
        }


        Log.d("permission_check",""+permissionsNotFound);
        return permissionsNotFound;
    }




    private static boolean checkAllMediaPermissions(Context mContext){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(mContext, CAMERA) == PackageManager.PERMISSION_DENIED;
        }
        else {
            return ContextCompat.checkSelfPermission(mContext, CAMERA) == PackageManager.PERMISSION_DENIED
                    && ContextCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    && ContextCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    && ContextCompat.checkSelfPermission(mContext, RECORD_AUDIO) == PackageManager.PERMISSION_DENIED;
        }
    }



    private static boolean checkAttachmentPermissions(Context mContext){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(mContext, CAMERA) == PackageManager.PERMISSION_DENIED;
        }
        else {
            return ContextCompat.checkSelfPermission(mContext, CAMERA) == PackageManager.PERMISSION_DENIED
                    && ContextCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    && ContextCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED;
        }
    }




    private static boolean checkAudioPermissions(Context mContext){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(mContext, RECORD_AUDIO) == PackageManager.PERMISSION_DENIED;
        }
        else {
            return ContextCompat.checkSelfPermission(mContext, RECORD_AUDIO) == PackageManager.PERMISSION_DENIED
                    && ContextCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED;
        }
    }
}

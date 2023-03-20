package com.gtfconnect.utilities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.gtfconnect.BuildConfig;
import com.gtfconnect.R;
import com.gtfconnect.interfaces.AttachmentUploadListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AttachmentUploadUtils {


    private static String SHARED_FOLDER = "shared";

    private static String FILE_NAME_PREFIX = "Connect_";

    private static String FILE_NAME_EXTENSION = ".jpg";

    private static String SHARED_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID +".myfileprovider";

    private static final String TAG = AttachmentUploadUtils.class.getSimpleName();

    public static boolean checkPermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED;
    }

   public static void showPictureDialog(final Context context,final AttachmentUploadListener listener){


       BottomSheetDialog pin_attachment_dialog = new BottomSheetDialog(context);
       pin_attachment_dialog.setContentView(R.layout.bottomsheet_pin_attachment);
       pin_attachment_dialog.show();

       MaterialCardView media = (MaterialCardView) pin_attachment_dialog.findViewById(R.id.media);
       media.setOnClickListener(view1 -> {
           listener.onClickGallery();
           pin_attachment_dialog.dismiss();
       });

      MaterialCardView document = (MaterialCardView) pin_attachment_dialog.findViewById(R.id.document);
       document.setOnClickListener(view1 -> {
           listener.onClickDocument();
           pin_attachment_dialog.dismiss();
       });


       MaterialCardView camera = (MaterialCardView) pin_attachment_dialog.findViewById(R.id.camera);
       camera.setOnClickListener(view1 -> {
           listener.onClickCamera(takePhotoFromCamera(context));
           pin_attachment_dialog.dismiss();
       });


       ImageView close = pin_attachment_dialog.findViewById(R.id.close);
       close.setOnClickListener(view1 -> pin_attachment_dialog.dismiss());
    }





    public static String gettingString(Context context, int resId){
        return context.getResources().getString(resId);
    }

    public static Intent takePhotoFromCamera (Context mContext) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File sharedFolder = new File(mContext.getFilesDir(), SHARED_FOLDER);
        sharedFolder.mkdirs();
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(new Date());
            String pictureFile = FILE_NAME_PREFIX + timeStamp;



            File capturedImageFilePath = File.createTempFile(pictureFile,  FILE_NAME_EXTENSION, sharedFolder);

            capturedImageFilePath.createNewFile();

            //Log.e(TAG,capturedImageFilePath.getAbsolutePath());

            Uri outputFileUri = FileProvider.getUriForFile(mContext, SHARED_PROVIDER_AUTHORITY, capturedImageFilePath);
            Log.e(TAG,capturedImageFilePath.getAbsolutePath());
            Log.e(TAG,outputFileUri.getPath());

            cameraIntent.putExtra( "image_path", capturedImageFilePath.getAbsolutePath());
            cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG,"path === " + cameraIntent.getStringExtra("image_path"));
        return cameraIntent;
    }


}

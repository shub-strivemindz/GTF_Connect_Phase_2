package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.R;
import com.gtfconnect.databinding.ActivityPreviewMediaBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.MultiPreviewListener;
import com.gtfconnect.models.groupChannelModels.MediaListModel;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;
import com.gtfconnect.ui.adapters.ImagePreviewAdapter;
import com.gtfconnect.utilities.LocalGalleryUtil;
import com.gtfconnect.utilities.SnapOneItemView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MultiPreviewScreen extends AppCompatActivity implements MultiPreviewListener {

    ActivityPreviewMediaBinding binding;

    List<MediaListModel> mediaList;

    String postBaseUrl = "";

    String title_name = "";

    ImagePreviewAdapter imagePreviewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPreviewMediaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mediaList = new ArrayList<>();


        Intent intent = getIntent();
        postBaseUrl = intent.getStringExtra("base_url");
        title_name = intent.getStringExtra("title");

        boolean isSelf = intent.getBooleanExtra("isSelf",false);

        if (isSelf || title_name.equalsIgnoreCase("You")){
            binding.mediaDownload.setVisibility(View.GONE);
        }
        else{
            binding.mediaDownload.setVisibility(View.VISIBLE);
        }


        Gson gson = new Gson();
        Type type = new TypeToken<List<MediaListModel>>(){}.getType();
        String mediaObject = intent.getStringExtra("mediaList");
        mediaList = gson.fromJson(mediaObject, type);


        LinearSnapHelper linearSnapHelper = new SnapOneItemView();
        linearSnapHelper.attachToRecyclerView(binding.previewRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        imagePreviewAdapter = new ImagePreviewAdapter(this,mediaList,postBaseUrl,this);
        binding.previewRecycler.setHasFixedSize(true);
        binding.previewRecycler.setLayoutManager(linearLayoutManager);
        binding.previewRecycler.setAdapter(imagePreviewAdapter);


        binding.previewTitle.setText(title_name);

        binding.backClick.setOnClickListener(view -> onBackPressed());




        binding.mediaDownload.setOnClickListener(view -> {

            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() >= 0){
                imagePreviewAdapter.downloadAttachment(linearLayoutManager.findFirstCompletelyVisibleItemPosition());
            }

            //imagePreviewAdapter.
            //imagePreviewAdapter.downloadAttachment();
        });
    }

    @Override
    public void imageLoading(boolean isImageLoaded) {
        if (isImageLoaded){
            binding.mediaDownload.setVisibility(View.VISIBLE);
        }
        else{
            binding.mediaDownload.setVisibility(View.GONE);
        }
    }

    @Override
    public void downloadVideo(String groupChannelID,String groupChatID,String videoUrl) {

        Log.d("videoUrl",videoUrl);

        //downloadAndOpenInvoice(videoUrl);
        //doInBackground("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");

        new Thread() {
            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {



                        boolean isDownloading = true;

                        String downloadPath = "/" + "connect_files" + "/" + groupChannelID + "/" + groupChatID + "/"
                                + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
                                + ".mp4";

                        String fileDownloadPath = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS) + downloadPath;


                        Log.d("LocalFilePath", fileDownloadPath);
                        Log.d("WebFilePath", videoUrl);

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoUrl));
                        request.setDescription("Downloading");
                        request.setMimeType("video/mp4");
                        request.setTitle("File :");
                        request.allowScanningByMediaScanner();
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                downloadPath);
                        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);


                        DownloadManager.Query query = null;
                        query = new DownloadManager.Query();
                        Cursor c = null;
                        if (query != null) {
                            query.setFilterByStatus(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING);
                        } else {
                            //return flag;
                        }

                        while (isDownloading) {
                            c = manager.query(query);

                        /*int bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        if (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            c = false;
                        }*/
                            boolean isFileDownloaded = false;

                            if (c.moveToFirst()) {
                                //Log.i("FLAG", "Downloading");
                                @SuppressLint("Range") int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

                                if (status == DownloadManager.STATUS_RUNNING) {
                                    @SuppressLint("Range") long totalBytes = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                    if (totalBytes > 0) {
                                        @SuppressLint("Range") long downloadedBytes = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                        final int progress = (int) ((downloadedBytes * 100L) / totalBytes);
                                        Log.d("download_status", "" + progress);

                                        isFileDownloaded = totalBytes == downloadedBytes;
                                    }
                                }
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {

                                    if (!isFileDownloaded){
                                        Log.i("FLAG", "not completed");
                                        isDownloading = false;
                                    }
                                    else {
                                        Log.i("FLAG", "done");
                                        isDownloading = false;
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }.start();
    }



    /*private void downloadAndOpenInvoice(String videoUrl) {
        //loader_show.setVisibility(View.VISIBLE);
        LinearLayout loader_show = null;

        Observable.fromCallable(() -> {
                    String pdfName = "video";
                    File file = null;
                    String pdfUrl = videoUrl;
                    if (getIntent().getStringExtra("old") != null && !getIntent().getStringExtra("old").isEmpty()) {
                        file = downloadFile(this, pdfUrl, pdfName, loader_show, getIntent().getStringExtra("old"));
                    } else {
                        file = downloadFile(this, pdfUrl, pdfName, loader_show, null);
                    }


                    return file;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    Toast.makeText(this, "Video downloaded successfully!", Toast.LENGTH_SHORT).show();
                    //viewPdf(file, PdfFileShow.this, loader_show);
                });

    }


    public static String getAppDir(Context context, String folderName) {
        String downloadPath = "/" + "connect_files" + "/"
                + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
                + ".mp4";

        String fileDownloadPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + downloadPath;

        return fileDownloadPath;
        //return context.getExternalFilesDir(null).getAbsolutePath() + File.separator + folderName + File.separator;
    }


    public File downloadFile(Activity mActivity, String url, String fileName, LinearLayout loader_show, String old_file) throws GeneralSecurityException, IOException {
// write the document content
        File pdfFile = null;


        File fileDir = new File(getAppDir(mActivity, ".Notes")); //Invoice folder inside your app directory
        if (!fileDir.exists()) {
            boolean mkdirs = fileDir.mkdirs();
        }
        pdfFile = new File(getAppDir(mActivity, "Notes"), fileName);
        if (old_file != null) {
            File pdfFileold = new File(getAppDir(mActivity, "Notes"), old_file);
            if (pdfFileold.exists())
                pdfFileold.delete();
        }

//Invoice folder inside your app directory
        *//*String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        EncryptedFile encryptedFile = new EncryptedFile.Builder(
                pdfFile,
                this,
                masterKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build();*//*

        if (!pdfFile.exists()) {
           // FileOutputStream encryptedOutputStream = encryptedFile.openFileOutput();
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);



// Set the content type of the response to PDF




// Write the buffered input stream to the output stream
            DataOutputStream fos = new DataOutputStream(new FileOutputStream(pdfFile));
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            fos.write(buffer);
            fos.flush();
            fos.close();

        }
        return pdfFile;
    }*/
}

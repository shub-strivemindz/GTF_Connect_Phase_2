package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.databinding.ActivityPreviewMediaBinding;
import com.gtfconnect.interfaces.MultiPreviewListener;
import com.gtfconnect.models.commonGroupChannelResponseModels.MediaListModel;
import com.gtfconnect.ui.adapters.ImagePreviewAdapter;
import com.gtfconnect.utilities.SnapOneItemView;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MultiPreviewScreen extends AppCompatActivity implements MultiPreviewListener {

    ActivityPreviewMediaBinding binding;

    List<MediaListModel> mediaList;

    String postBaseUrl = "";

    String title_name = "";

    ImagePreviewAdapter imagePreviewAdapter;

    ExoPlayer simpleExoPlayer;

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


    @Override
    protected void onPause() {
        super.onPause();
        imagePreviewAdapter.pauseExoPlayer();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        imagePreviewAdapter.destroyExoPlayer();
    }
}

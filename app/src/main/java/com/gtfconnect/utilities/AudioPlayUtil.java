package com.gtfconnect.utilities;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.audiowaveform.WaveformSeekBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class AudioPlayUtil {

    static Context ctx;

    static boolean isAudioPlaying = false;
    static ValueAnimator animator;

    static WaveformSeekBar waveformSeekBar;

    public static void playAudioAnimation(Context context,WaveformSeekBar seekBar,long duration){

        ctx = context;

        waveformSeekBar = seekBar;

        waveformSeekBar.setWaveform(createWaveform(), true);
        waveformSeekBar.setProgressInPercentage(0);


        animator = ValueAnimator.ofFloat(0f,1f);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                Log.v("Animator Time",String.valueOf((float) animation.getAnimatedValue()));
                waveformSeekBar.animate();
                waveformSeekBar.setProgressInPercentage((float) animation.getAnimatedValue());
                isAudioPlaying = true;
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // start your activity here
                waveformSeekBar.setProgressInPercentage(0);
                isAudioPlaying = false;
            }
        });


        animator.start();
    }


    public static void stopPlayback(Activity activity)
    {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isAudioPlaying){
                    waveformSeekBar.setProgressInPercentage(0);
                    animator.end();
                    isAudioPlaying = false;

                }
            }
        });
    }




    public static long getAudioDuration(String audioFilePath)
    {
        Uri uri = Uri.parse(audioFilePath);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(ctx,uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Long.parseLong(durationStr);
    }



    public static String getAudioDurationTime(long duration){
        Date date = new Date(duration);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }





    public static int[] createWaveform() {

        final int length = 45;
        final int[] values = new int[length];
        final int[] bar = {10,20,30,40,30,20,10,20,30};
        int count = 0;

        for(int i=0;i<5;i++){
            for(int j=0;j<9;j++)
            {
                values[count] = bar[j];
                count++;
            }
        }

        return values;
    }




    public static boolean checkFileExistence(String groupChannelID,String groupChatID) {

        boolean isFileFound = false;

        String localPath = "/" + "connect_audio_files" + "/";

        String filePath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "";

        File directory = new File(filePath + localPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                if (groupChannelID.equalsIgnoreCase(files[i].getName())) {

                    localPath = "/" + "connect_audio_files" + "/" + groupChannelID + "/";

                    File subDirectory = new File(filePath + localPath);
                    File[] subFiles = subDirectory.listFiles();

                    if (subFiles != null) {
                        for (int j = 0; j < subFiles.length; j++) {
                            if (subFiles[j].getName().equalsIgnoreCase(groupChatID)) {
                                if (subFiles[j].length() != 0) {
                                    isFileFound = true;
                                    break;
                                }
                            }
                        }
                    }
                    Log.d("Files", "FileName:" + files[i].getName());
                }
            }
        }
        return isFileFound;
    }


    public static String getSavedAudioFilePath(String groupChannelID,String groupChatID) {
        String tempFilePath = "";

        String localPath = "/" + "connect_audio_files" + "/";

        String filePath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "";

        File directory = new File(filePath + localPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                if (groupChannelID.equalsIgnoreCase(files[i].getName())) {

                    localPath = "/" + "connect_audio_files" + "/" + groupChannelID + "/";

                    File subDirectory = new File(filePath + localPath);
                    File[] subFiles = subDirectory.listFiles();

                    if (subFiles != null) {
                        for (int j = 0; j < subFiles.length; j++) {

                            if (subFiles[j].getName().equalsIgnoreCase(groupChatID)) {

                                localPath = "/" + "connect_audio_files" + "/" + groupChannelID + "/" + groupChatID +"/";

                                File subSubDirectory = new File(filePath + localPath);
                                File[] subSubFiles = subSubDirectory.listFiles();

                                if (subSubFiles != null && subSubFiles.length > 0){

                                        tempFilePath = subSubFiles[0].getPath();
                                        Log.d("FoubdedFilePath", tempFilePath);
                                        break;
                                }

                            }
                        }
                    }
                    Log.d("Files", "FileName:" + files[i].getName());
                }
            }
        }
        return tempFilePath;
    }
}


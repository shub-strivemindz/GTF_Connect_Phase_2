package com.gtfconnect.utilities;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.audiowaveform.WaveformSeekBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Random;

/*
public class AudioPlaynRecordUtil {


    MediaRecorder mMediaRecorder;
    MediaPlayer mMediaPlayer;

    static Context ctx;


    public static void playAudio(Context context,WaveformSeekBar seekBar){

        ctx = context;

        seekBar.setWaveform(createWaveform(), true);
        seekBar.setProgressInPercentage(0);


        ValueAnimator animator = ValueAnimator.ofFloat(0f,1f);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                Log.v("Animator Time",String.valueOf((float) animation.getAnimatedValue()));
                seekBar.animate();
                seekBar.setProgressInPercentage((float) animation.getAnimatedValue());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // start your activity here
            }
        });
        animator.start();
    }




    private static long getAudioDuration()
    {
        Uri uri = Uri.parse(AudioSave);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(ctx,uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Long.parseLong(durationStr);

    }




    private static int[] createWaveform() {

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

    //this function for play the recording
    private void RecordPlay() {

        btn_record.setEnabled(false);
        btn_stop_record.setEnabled(true);

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(AudioSave);
            mMediaPlayer.prepare();


        } catch (IOException e) {
            e.printStackTrace();
        }
        //play recording here
        mMediaPlayer.start();

        Snackbar.make(cl_main, "Recording Playing", Snackbar.LENGTH_SHORT).show();
    }




    //this function record the audio
    public void RecordReady() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setOutputFile(AudioSave);
    }

    //create file for save the audio
    public String CreateFile(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int j = 0;
        while (j < string) {
            stringBuilder.append(mAudioname.charAt(mRandom.nextInt(mAudioname.length())));
            j++;
        }
        return stringBuilder.toString();
    }

    //check permission is given or not
    public boolean CheckPermission() {
        int first = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int first1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return first == PackageManager.PERMISSION_GRANTED && first1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] Results) {
        super.onRequestPermissionsResult(requestCode, permissions, Results);
        switch (requestCode) {
            case RequestPermissionCode:
                if (Results.length > 0) {
                    boolean StoragePermission = Results[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = Results[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    //give below permission for audio capture
    private void RequestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }
}
*/

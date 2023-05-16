package com.gtfconnect.interfaces;

import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.audiowaveform.WaveformSeekBar;

public interface PinnedMessageListener {

    public void deleteSinglePinMessage(int pinnedMessageId);

    public void gotoMessage(String groupChatId);

    void downloadAudio(String audioPostUrl, String groupChannelID, String groupChatID, WaveformSeekBar seekBar, LottieAnimationView progressBar, ImageView downloadPlayPic);

    void playAudio(String audioPostUrl, WaveformSeekBar seekBar,long duration);

    void viewMemberProfile(int userID,int gcMemberId, int groupChatId, int groupChannelId );
}

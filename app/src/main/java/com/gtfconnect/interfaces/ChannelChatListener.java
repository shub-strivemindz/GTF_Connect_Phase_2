package com.gtfconnect.interfaces;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.audiowaveform.WaveformSeekBar;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public interface ChannelChatListener {

    void pinMessage(int gcMemberId,int GroupChannelId,int userId,int groupChatId);

    void sendQuotedMessage(View view, String groupChatId, String oldMessage, String username, String time,int mediaCount,String previewUrl);

    void searchQuoteMessage(int index,String groupChatId);

    void likePost(int userID, int groupChannelId, int gcMemberID, int groupChatId, int like);

    void likeAsEmote(int userID, int groupChannelId, int gcMemberID, int groupChatId, int like, int position, RelativeLayout likeRootView,ImageView likeIcon, TextView reactionIcon);


    void deletePost(int userID,int gcMemberId, int groupChatId, int groupChannelId );


    void commentMessage(int position, int userID,int gcMemberId, int groupChatId, int groupChannelId );


    void downloadAudio(String audioPostUrl, String groupChannelID, String groupChatID, WaveformSeekBar seekBar, LottieAnimationView progressBar, ImageView downloadPlayPic);

    void playAudio(String audioPostUrl, WaveformSeekBar seekBar,long duration);

    void viewMemberProfile(int userID,int gcMemberId, int groupChatId, int groupChannelId );

    void viewSelfProfile();

    void forwardMultiplePost(int selectedCount,int chatID,boolean isMessageSelected);

    void toggleMultipleMessageSelection(boolean toggleSelection);

    void saveMessage(int chatID);

    void initiateCommentScreen(String data,String profileBaseUrl,String postBaseUrl,String userID,boolean isDiscussionAllowed);


    void autoPlayVideo(int position, String post_path, PlayerView videoPlayer, ProgressBar progressBar);

}

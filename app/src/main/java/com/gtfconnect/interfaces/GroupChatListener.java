package com.gtfconnect.interfaces;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.audiowaveform.WaveformSeekBar;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;

import java.util.ArrayList;

public interface GroupChatListener {


    public void pinMessage(int gcMemberId,int GroupChannelId,int userId,int groupChatId);
    void sendQuotedMessage(View view, String groupChatId, String oldMessage, String username, String time,int mediaCount,String previewUrl);

    public void searchQuoteMessage(int index,String groupChatId);

    public void likePost(int userID, int groupChannelId, int gcMemberID, int groupChatId, int like);

    public void likeAsEmote(int position, ImageView likeRootView);


    public void deletePost(int userID,int gcMemberId, int groupChatId, int groupChannelId );

    void commentMessage(int position, int userID,int gcMemberId, int groupChatId, int groupChannelId );

    void downloadAudio(String audioPostUrl, String groupChannelID, String groupChatID, WaveformSeekBar seekBar, LottieAnimationView progressBar, ImageView downloadPlayPic);

    void playAudio(String audioPostUrl, WaveformSeekBar seekBar,long duration);

    void viewMemberProfile(int userID,int gcMemberId, int groupChatId, int groupChannelId );

    void forwardMultiplePost(int selectedPostCount);

    void saveMessage(int chatID);

    void updateChatList(ArrayList<ChannelRowListDataModel> list);
    void initiateCommentScreen(String data,String profileBaseUrl,String postBaseUrl,String userID);

}

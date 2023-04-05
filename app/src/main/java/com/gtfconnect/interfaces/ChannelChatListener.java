package com.gtfconnect.interfaces;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

public interface ChannelChatListener {

    void pinMessage(int gcMemberId,int GroupChannelId,int userId,int groupChatId);

    void sendQuotedMessage(View view, String groupChatId, String oldMessage, String username, String time);

    void searchQuoteMessage(int index,String groupChatId);

    void likePost(int userID, int groupChannelId, int gcMemberID, int groupChatId, int like);

    void likeAsEmote(int position, ImageView likeRootView);


    void deletePost(int userID,int gcMemberId, int groupChatId, int groupChannelId );


    void commentMessage(int position, int userID,int gcMemberId, int groupChatId, int groupChannelId );

}

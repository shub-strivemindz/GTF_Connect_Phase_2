package com.gtfconnect.interfaces;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;

import java.util.ArrayList;

public interface GroupChatListener {


    public void pinMessage(int gcMemberId,int GroupChannelId,int userId,int groupChatId);

    public void sendQuotedMessage(View view, String groupChatId, String oldMessage, String username, String time);

    public void searchQuoteMessage(int index,String groupChatId);

    public void likePost(int userID, int groupChannelId, int gcMemberID, int groupChatId, int like);

    public void likeAsEmote(int position, ImageView likeRootView);


    public void deletePost(int userID,int gcMemberId, int groupChatId, int groupChannelId );


    void forwardMultiplePost(int selectedPostCount,boolean showPostSelection);

    void updateChatList(ArrayList<ChannelRowListDataModel> list);

}

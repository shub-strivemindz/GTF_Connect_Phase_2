package com.gtfconnect.interfaces;

public interface GroupCommentListener {

    public void oldMessage(String oldMessage,int commentID,int position);
    public void notifyDeleteComment(int commentID,int chatID,int channelID,int userID);
}

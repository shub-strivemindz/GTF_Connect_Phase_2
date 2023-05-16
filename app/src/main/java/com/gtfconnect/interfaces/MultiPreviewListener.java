package com.gtfconnect.interfaces;

public interface MultiPreviewListener {

    void imageLoading(boolean isImageLoaded);

    void downloadVideo(String groupChannelID,String groupChatID,String videoUrl);
}

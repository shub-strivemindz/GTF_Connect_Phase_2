package com.gtfconnect.interfaces;

public interface PinnedMessageListener {

    public void deleteSinglePinMessage(int pinnedMessageId);

    public void gotoMessage(String groupChatId);
}

package com.gtfconnect.interfaces;

public interface ChannelSettingListener {

    void callPermissionClass();
    void updateAccessTypeStatus(int status);
    void updateSignMessageStatus(int status);
    void updateDiscussionStatus(int status);
    void updateViewChatHistoryStatus(int status);
    void updateManipulateViewsStatus(int status,int percent);

    void callManageReactionsClass(int status);
}

package com.gtfconnect.interfaces;

public interface DashboardMessageCountListener {

    void getMessageRecentCount(int count);

    void getMessageChannelCount(int count);

    void getMessageGroupCount(int count);

    void getMessageMentorCount(int count);
}

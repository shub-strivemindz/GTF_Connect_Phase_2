package com.gtfconnect.interfaces;

import android.content.Intent;

public interface AttachmentUploadListener {

    void onClickGallery();
    void onClickCamera(Intent intent);

    void onClickDocument();
}

package com.gtfconnect.models;

import android.net.Uri;

public class ImagePreviewModel {

    Uri uri;
    boolean isActive;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

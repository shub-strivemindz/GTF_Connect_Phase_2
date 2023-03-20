package com.gtfconnect.interfaces;

import android.net.Uri;

import com.gtfconnect.models.ImagePreviewModel;

import java.util.ArrayList;

public interface ImagePreviewListener {
    public void imagePreviewListener(int index, ArrayList<ImagePreviewModel> uriList);
}

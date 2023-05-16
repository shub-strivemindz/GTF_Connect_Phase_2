package com.gtfconnect.utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.gtfconnect.R;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GalleryTypeStatus;

public class GlideUtils {

    public static void loadImage(Context context,ImageView imageContainer, String url){
        //Setting up loader on post
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(circularProgressDrawable);
        requestOptions.error(R.drawable.no_image_background);
        requestOptions.skipMemoryCache(false);
        requestOptions.fitCenter();

        Glide.with(context).load(url).
                fitCenter().apply(requestOptions).
                transition(DrawableTransitionOptions.withCrossFade()).into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageContainer.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        imageContainer.setImageDrawable(placeholder);
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        imageContainer.setImageDrawable(circularProgressDrawable);
                        super.onLoadStarted(placeholder);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                });
    }
}

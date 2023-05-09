package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gtfconnect.R;
import com.gtfconnect.databinding.ActivityGifPreviewBinding;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.LocalGalleryUtil;
import com.gtfconnect.utilities.Utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class GifPreviewScreen extends AppCompatActivity {

    ActivityGifPreviewBinding binding;

    private File gifFile ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGifPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Uri gifUri = Uri.parse(getIntent().getStringExtra("gif"));
        if (gifUri !=null) {
            loadGif(gifUri);
        }
        else{
            setResult(Constants.NO_GIF_FOUND,new Intent());
            finish();
        }





        binding.sendMessage.setOnClickListener(v -> {

            Intent resultIntent = new Intent();

            String message = binding.type.getText().toString().trim();
            if (message != null){
                resultIntent.putExtra("message",message);
            }
            else{
                resultIntent.putExtra("message","");
            }
            if (gifFile != null){
                resultIntent.putExtra("gif",gifFile);

                setResult(Constants.SHARE_GIF,resultIntent);
                finish();
            }
            else{
                setResult(Constants.NO_GIF_FOUND,resultIntent);
                finish();
            }
        });
    }


    private void loadGif(Uri gifUri)
    {

        //Setting up loader on post
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(circularProgressDrawable);
        requestOptions.error(R.drawable.image_not_found);
        requestOptions.skipMemoryCache(true);
        requestOptions.fitCenter();

        Glide.with(this).load(gifUri).
                fitCenter().apply(requestOptions).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        try {
                            gifFile = LocalGalleryUtil.saveGifToGallery(GifPreviewScreen.this,(GifDrawable) resource);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                }).
                transition(DrawableTransitionOptions.withCrossFade()).into(binding.preview);
    }




}

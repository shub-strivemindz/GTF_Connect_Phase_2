package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.gtfconnect.R;
import com.gtfconnect.databinding.ActivityGifPreviewBinding;
import com.gtfconnect.utilities.Constants;

public class GifPreviewScreen extends AppCompatActivity {

    ActivityGifPreviewBinding binding;

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
            //String message
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
                fitCenter().apply(requestOptions).
                transition(DrawableTransitionOptions.withCrossFade()).into(binding.preview);
    }




}

package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gtfconnect.R;
import com.gtfconnect.databinding.ActivityImagePreviewBinding;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.LocalGalleryUtil;

public class ImagePreviewScreen extends AppCompatActivity {


    ActivityImagePreviewBinding binding;

    private Drawable imageDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImagePreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.download.setVisibility(View.GONE);

        String url = getIntent().getStringExtra("image");

        if (url !=null && !url.isEmpty()) {
            loadImage(url);
        }
        else{
            setResult(Constants.NO_IMAGE_FOUND,new Intent());
            finish();
        }

        binding.close.setOnClickListener(v -> finish());

        binding.download.setOnClickListener(v -> {
            if (LocalGalleryUtil.saveImageToGallery(this,imageDrawable)){
                finish();
            }

        });
    }


    private void loadImage(String imagePath){

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(circularProgressDrawable);
        requestOptions.error(R.drawable.image_not_found);
        requestOptions.skipMemoryCache(false);
        requestOptions.fitCenter();

        Glide.with(this).load(imagePath).
                fitCenter().apply(requestOptions).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.download.setVisibility(View.GONE);
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.download.setVisibility(View.VISIBLE);
                        imageDrawable = resource;
                        return false;
                    }
                }).
                transition(DrawableTransitionOptions.withCrossFade()).into(binding.preview);
    }
}

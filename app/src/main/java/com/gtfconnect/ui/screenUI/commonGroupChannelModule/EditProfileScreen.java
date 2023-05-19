package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityEditGroupChannelProfileBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.models.commonGroupChannelResponseModels.GroupChannelInfoResponseModel;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.ui.screenUI.HomeScreen;
import com.gtfconnect.ui.screenUI.channelModule.ChannelManageReactionScreen;
import com.gtfconnect.utilities.AttachmentUploadUtils;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.FetchPath;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfileScreen extends AppCompatActivity implements ApiResponseListener {

    ActivityEditGroupChannelProfileBinding binding;

    private Rest rest;

    private ApiResponseListener listener;

    private ConnectViewModel connectViewModel;

    private String imageUrl = "";

    private String title = "";

    private String description = "";

    private int requestType ;

    private File selectedMedia;

    private String api_token;

    private String accessType;

    private String type;

    private int id;
    

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditGroupChannelProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");

        init();

        imageUrl = getIntent().getStringExtra("image");

        title = getIntent().getStringExtra("title");

        description = getIntent().getStringExtra("description");


        accessType = getIntent().getStringExtra("accessType");

        type = getIntent().getStringExtra("type");

        id = Integer.parseInt(PreferenceConnector.readString(this,PreferenceConnector.GC_CHANNEL_ID,""));
        
        if (imageUrl != null){
            //Setting up loader on post
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(circularProgressDrawable);
            requestOptions.skipMemoryCache(false);
            requestOptions.fitCenter();

            Glide.with(this).load(imageUrl).
                    fitCenter().apply(requestOptions).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            binding.editImage.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).
                    transition(DrawableTransitionOptions.withCrossFade()).into(binding.profileImage);
        }
        
        if (title != null){
            binding.title.setText(title);
        }
        else{
            title = "";
        }
        
        if (description != null){
            binding.description.setText(description);
        }
        else{
            description = "";
        }
        

        binding.profileImage.setOnClickListener(view -> {
            BottomSheetDialog media_type = new BottomSheetDialog(this);
            media_type.setContentView(R.layout.bottomsheet_select_media_type_2);

            TextView cancel = media_type.findViewById(R.id.cancel);
            TextView gallery = media_type.findViewById(R.id.gallery);
            TextView camera = media_type.findViewById(R.id.camera);



            cancel.setOnClickListener(view1 -> media_type.dismiss());

            gallery.setOnClickListener(view1 -> {
                accessMediaImage();
                media_type.dismiss();
            });

            camera.setOnClickListener(view1 -> {
                accessCamera();
                media_type.dismiss();
            });

            media_type.show();
        });

        
        binding.updateDescription.setOnClickListener(view -> validationCheck());
    }




    private void init() {


        rest = new Rest(this,true,false);
        listener = this;

        connectViewModel = new ViewModelProvider(this).get(ConnectViewModel.class);
        connectViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("Profile Listener Called ---", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse, rest);
                }
            }
        });
    }

    
    
    private void validationCheck()
    {
        String updatedTitle = binding.title.getText().toString().trim();
        String updatedDescription = binding.description.getText().toString().trim();

        Map<String,Object> params = new HashMap<>();
        
        if (updatedTitle.equalsIgnoreCase(title) && updatedDescription.equalsIgnoreCase(description) && selectedMedia == null){
            Toast.makeText(this, "Nothing to update!", Toast.LENGTH_SHORT).show();
        } else if (selectedMedia != null) {

            params.put("Name",title);
            params.put("Description",description);
            params.put("Type",type);
            params.put("AccessType",accessType);


            connectViewModel.update_groupChannel_profile(id,api_token,params,selectedMedia);

        } else{

            params.put("Name",title);
            params.put("Description",description);
            params.put("Type",type);
            params.put("AccessType",accessType);


            connectViewModel.update_groupChannel_profile(id,api_token,params,null);
        }


        title = updatedTitle;
        description = updatedDescription;
    }









    private void accessMediaImage() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_PICK);
//        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), Constants.SELECT_PICTURE_REQUEST_CODE);

/*
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);*/
    }

    private void accessCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        Uri singleImageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


        Intent cameraIntent = AttachmentUploadUtils.takePhotoFromCamera(this);
        selectedMedia = new File(Objects.requireNonNull(cameraIntent.getStringExtra("image_path")));
        startActivityForResult(cameraIntent, Constants.CAPTURE_IMAGE_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {

            Bitmap bitmap = Utils.decodeFile(selectedMedia);
            Uri selected_camera_uri = Utils.getImageUri(this, bitmap, 1024.0f, 10240.0f);
            selectedMedia = new File(Objects.requireNonNull(FetchPath.getPath(this, selected_camera_uri)));

            GlideUtils.loadImage(this,binding.profileImage,selectedMedia.getAbsolutePath());

        } else if (requestCode == Constants.SELECT_PICTURE_REQUEST_CODE && resultCode == RESULT_OK) {

            if (data.getData() != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    selectedMedia = new File(Objects.requireNonNull(FetchPath.getPath(this, imageUri)));

                    GlideUtils.loadImage(this,binding.profileImage,selectedMedia.getAbsolutePath());
                }
            }
        } else {
            if (requestCode == Constants.SELECT_PICTURE_REQUEST_CODE) {
                Toast.makeText(this, "You haven't picked any image", Toast.LENGTH_LONG).show();
            }
            if (requestCode == Constants.CAPTURE_IMAGE_REQUEST_CODE) {
                Toast.makeText(this, "You haven't capture any image", Toast.LENGTH_LONG).show();
            }
        }
    }




    




    @Override
    public void onLoading() {
        rest.ShowDialogue();
    }



    @Override
    public void onDataRender(JsonObject jsonObject) {
        Log.d("rendered","successfully!");
        renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        Log.d("rendered","successfully!");
        renderResponse(jsonObject);

    }

    @Override
    public void onAuthFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(this, LoginScreen.class));
        finishAffinity();
    }

    @Override
    public void onServerFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onForbidden(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLaunchFailure(JsonObject jsonObject) {
        Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOtherFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void renderResponse(JsonObject response)
    {
        Log.d("rendered",response.toString());

        Toast.makeText(this, "Details updated successfully!", Toast.LENGTH_SHORT).show();
        setResult(Constants.PROFILE_DETAILS_UPDATED,new Intent());
        finish();

    }
}

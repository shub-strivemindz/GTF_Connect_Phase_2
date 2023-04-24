package com.gtfconnect.ui.screenUI.userProfileModule;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityUserProfileBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.models.ImagePreviewModel;
import com.gtfconnect.models.LoginResponseModel;
import com.gtfconnect.models.ProfileResponseModel;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GalleryTypeStatus;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GroupChannelGalleryEntity;
import com.gtfconnect.ui.screenUI.HomeScreen;
import com.gtfconnect.ui.screenUI.authModule.LoginScreen;
import com.gtfconnect.utilities.AttachmentUploadUtils;
import com.gtfconnect.utilities.FetchPath;
import com.gtfconnect.utilities.LocalGalleryUtil;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.AuthViewModel;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class UserProfileScreen extends AppCompatActivity implements ApiResponseListener{
    ActivityUserProfileBinding binding;

    private final int SELECT_PICTURE_REQUEST_CODE = 1001;

    private final int CAPTURE_IMAGE_REQUEST_CODE = 1002;

    private final int REQUEST_USER_PROFILE_DATA = 1;

    private final int UPDATE_USER_PROFILE_PIC = 2;

    private int requestType ;

    private File selectedMedia;

    private Rest rest;

    private AuthViewModel authViewModel;

    private ConnectViewModel connectViewModel;

    private ApiResponseListener listener;

    private String api_token;

    private int gtf_user_id;

    private ProfileResponseModel profileResponseModel;

    private GroupChannelGalleryEntity galleryEntity;

    private DatabaseViewModel databaseViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gtf_user_id = PreferenceConnector.readInteger(this, PreferenceConnector.GTF_USER_ID, 0);

        init();

        binding.updateProfilePic.setOnClickListener(v -> {
            BottomSheetDialog media_type = new BottomSheetDialog(this);
            media_type.setContentView(R.layout.bottomsheet_select_media_type_2);

            TextView cancel = media_type.findViewById(R.id.cancel);
            TextView gallery = media_type.findViewById(R.id.gallery);
            TextView camera = media_type.findViewById(R.id.camera);



            cancel.setOnClickListener(view -> media_type.dismiss());

            gallery.setOnClickListener(view -> {
                accessMediaImage();
                media_type.dismiss();
            });

            camera.setOnClickListener(view -> {
                accessCamera();
                media_type.dismiss();
            });

            media_type.show();
        });


        // Navigate to Update Profile Info :
        binding.editProfile.setOnClickListener(view -> startActivity(new Intent(UserProfileScreen.this, UpdateUserInfoScreen.class)));

        // Navigate to Update Password :
        binding.updatePassword.setOnClickListener(view -> startActivity(new Intent(UserProfileScreen.this, UpdatePasswordScreen.class)));

        // Navigate to Saved Messages :
        binding.savedMessages.setOnClickListener(view -> startActivity(new Intent(UserProfileScreen.this, SavedMessagesScreen.class)));

        // Navigate to Blocklist :
        binding.blocklist.setOnClickListener(view -> startActivity(new Intent(UserProfileScreen.this, BlocklistScreen.class)));

        // Navigate to Contact Us :
        binding.contactUs.setOnClickListener(view -> startActivity(new Intent(UserProfileScreen.this, ContactUsScreen.class)));

        // Navigate to Previous Screen :
        binding.back.setOnClickListener(view -> startActivity(new Intent(UserProfileScreen.this, HomeScreen.class)));


        // Dialog for Sign Out  :
        binding.signOut.setOnClickListener(view -> {
            Dialog sign_out_dialog = new Dialog(UserProfileScreen.this);

            sign_out_dialog.setContentView(R.layout.dialog_sign_out);
            sign_out_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            sign_out_dialog.setCancelable(false);
            sign_out_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView sign_out = sign_out_dialog.findViewById(R.id.sign_out);
            TextView cancel = sign_out_dialog.findViewById(R.id.cancel);

            sign_out.setOnClickListener(v -> {

                PreferenceConnector.writeBoolean(UserProfileScreen.this,PreferenceConnector.IS_USER_LOGGED,false);
                startActivity(new Intent(UserProfileScreen.this, LoginScreen.class));
                sign_out_dialog.dismiss();
                finishAffinity();
            });

            cancel.setOnClickListener(v -> sign_out_dialog.dismiss());

            sign_out_dialog.show();
        });
    }




    private void init(){

        rest = new Rest(this,false,false);
        listener = this;

        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("Profile Listener Called ---", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse, rest);
                }

            }
        });


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

        api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");

        requestType = REQUEST_USER_PROFILE_DATA;
        connectViewModel.getUserProfile(api_token);
    }




    private void setProfileData()
    {
        String name = PreferenceConnector.readString(this,PreferenceConnector.FIRST_NAME,"")+" "+PreferenceConnector.readString(this,PreferenceConnector.LAST_NAME,"");
        binding.profileTitle.setText(name);
        binding.username.setText(PreferenceConnector.readString(this,PreferenceConnector.EMAIL_ID,""));

    }




    // =============================================================== Getting Images Locally from database =================================================
    private void loadLocalData(){

        galleryEntity = new GroupChannelGalleryEntity();

        databaseViewModel.getProfileImage().observe(this, groupChannelGalleryEntity -> {

            if (groupChannelGalleryEntity != null && groupChannelGalleryEntity.getImageData() != null) {

                galleryEntity = groupChannelGalleryEntity;

                Bitmap profileImage = LocalGalleryUtil.getImageFromDB(groupChannelGalleryEntity.getImageData());
                binding.profileImage.setImageBitmap(profileImage);
            }
        });
    }













    private void accessMediaImage() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_PICK);
//        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE_REQUEST_CODE);

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
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {

            Bitmap bitmap = Utils.decodeFile(selectedMedia);
            Uri selected_camera_uri = Utils.getImageUri(this, bitmap, 1024.0f, 10240.0f);
            selectedMedia = new File(Objects.requireNonNull(FetchPath.getPath(this, selected_camera_uri)));

            api_token = PreferenceConnector.readString(this, PreferenceConnector.API_CONNECT_TOKEN, "");

            requestType = UPDATE_USER_PROFILE_PIC;
            authViewModel.updateProfilePic(api_token, gtf_user_id, selectedMedia);

            rest.ShowDialogue();

        } else if (requestCode == SELECT_PICTURE_REQUEST_CODE && resultCode == RESULT_OK) {

            if (data.getData() != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    selectedMedia = new File(Objects.requireNonNull(FetchPath.getPath(this, imageUri)));

                    api_token = PreferenceConnector.readString(this, PreferenceConnector.API_CONNECT_TOKEN, "");

                    requestType = UPDATE_USER_PROFILE_PIC;
                    authViewModel.updateProfilePic(api_token, gtf_user_id, selectedMedia);

                    rest.ShowDialogue();
                }
            }
        } else {
            if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
                Toast.makeText(this, "You haven't picked any image", Toast.LENGTH_LONG).show();
            }
            if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
                Toast.makeText(this, "You haven't capture any image", Toast.LENGTH_LONG).show();
            }
        }
    }




    private void loadImage(String imageUrl, ImageView image)
    {
        //Setting up loader on post
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(circularProgressDrawable);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.error(R.drawable.image_not_found);
        requestOptions.skipMemoryCache(false);
        requestOptions.fitCenter();

        Glide.with(this).load(imageUrl).
                fitCenter().apply(requestOptions).
                transition(DrawableTransitionOptions.withCrossFade()).into(image);

    }






    @Override
    protected void onResume() {
        super.onResume();
        setProfileData();
        loadLocalData();
    }



    @Override
    public void onLoading() {
        //rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(HomeScreen.this,LoginScreen.class));
        //finishAffinity();
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






    private void renderResponse(JsonObject jsonObject){

        Gson gson;
        Type type;

        if (requestType == REQUEST_USER_PROFILE_DATA){

            profileResponseModel = new ProfileResponseModel();

            gson = new Gson();
            type = new TypeToken<ProfileResponseModel>(){}.getType();


            profileResponseModel = gson.fromJson(jsonObject,type);

            if (galleryEntity != null && galleryEntity.getImageUrl()!=null) {
                if (!galleryEntity.getImageUrl().equalsIgnoreCase(profileResponseModel.getData().getProfileInfo().getProfileImage())) {
                    saveAndLoadImage("Condition 1");
                }
            }
            else{
                if (profileResponseModel != null && profileResponseModel.getData() != null && profileResponseModel.getData().getProfileInfo() != null){
                    if (profileResponseModel.getData().getProfileInfo().getProfileThumbnail() != null)
                    {
                        saveAndLoadImage("Condition 2");
                    }
                }
            }
        }
        else if (requestType == UPDATE_USER_PROFILE_PIC) {

            api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");

            requestType = REQUEST_USER_PROFILE_DATA;
            connectViewModel.getUserProfile(api_token);
        }
    }



    private void saveAndLoadImage(String tag)
    {

        Log.d("Function",tag);


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

        Glide.with(this).load(profileResponseModel.getData().getProfileInfo().getProfileImage()).
                listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        // Saving Image

                        databaseViewModel.insertImageInGallery(LocalGalleryUtil.saveImage("Profile", GalleryTypeStatus.PROFILE.name(),resource,profileResponseModel.getData().getProfileInfo().getProfileImage(),profileResponseModel.getData().getProfileInfo().getProfileThumbnail(),0,0));
                        return false;
                    }
                }).
                fitCenter().apply(requestOptions).
                transition(DrawableTransitionOptions.withCrossFade()).into(binding.profileImage);

    }
}

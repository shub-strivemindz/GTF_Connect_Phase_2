package com.gtfconnect.ui.screenUI;

import static com.gtfconnect.services.SocketService.socketInstance;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
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
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.R;


import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityHomeBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.DashboardMessageCountListener;
import com.gtfconnect.interfaces.UnreadCountHeaderListener;
import com.gtfconnect.models.ProfileResponseModel;
import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;
import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferResponseModel;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GalleryTypeStatus;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GroupChannelGalleryEntity;
import com.gtfconnect.services.InternetService;
import com.gtfconnect.services.SocketService;
import com.gtfconnect.ui.fragments.RecentFragment;
import com.gtfconnect.ui.screenUI.authModule.LoginScreen;
import com.gtfconnect.ui.screenUI.userProfileModule.UserProfileScreen;
import com.gtfconnect.utilities.LocalGalleryUtil;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.AuthViewModel;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeScreen extends AppCompatActivity implements UnreadCountHeaderListener, InternetService.ReceiverListener,ApiResponseListener, DashboardMessageCountListener  {



    private ActivityHomeBinding binding;


    private final int GET_PROFILE_DATA = 101;
    private final int GET_EXCLUSIVE_OFFER = 102;


    private boolean isSearchBarOpened = false;

    private static boolean exitDoublePressed = false;


    private Dialog searchDialog;

    //private Socket mSocket;

    private boolean isProfileDataEmpty = true;

    private final static int FIVE_SECONDS_DELAY = 5000;

    private static Handler handler = new Handler();

    private ConnectViewModel connectViewModel;


    private Rest rest;
    private ApiResponseListener listener;
    private AuthViewModel authViewModel;

    private int requestType ;


    private ProfileResponseModel profileResponseModel;

    private DatabaseViewModel databaseViewModel;

    private GroupChannelGalleryEntity galleryEntity;

    private List<ExclusiveOfferDataModel> exclusiveOfferDataModels;

    private List<Long> exclusive_inserted_row_ids;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();

        PreferenceConnector.writeBoolean(this,PreferenceConnector.IS_USER_LOGGED,true);


        Log.d("USER ID ", String.valueOf(PreferenceConnector.readInteger(this,PreferenceConnector.GTF_USER_ID,0)));

        //-------------------------------------------------------------  Shared Preference Data ----------------------------------------------------------------


        //-------------------------------------------------------------   ----------------------------------------------------------------


        setBottomNavigation();

        // profileResponse= new ProfileResponse();

        /*authViewModel.getUserProfile(
                PreferenceConnector.readString(this,PreferenceConnector.API_GTF_TOKEN_,""),
                "test_token",
                "android");*/


        // Bottom sheet for Mute Notifications
        binding.muteNotifications.setOnClickListener(view -> {
            BottomSheetDialog mute_notification_dialog = new BottomSheetDialog(HomeScreen.this);
            mute_notification_dialog.setContentView(R.layout.bottomsheet_mute_notification);
            mute_notification_dialog.show();
        });

        // Navigate to User Profile
        binding.userProfile.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, UserProfileScreen.class)));


        // Expandable search bar
      /*  binding.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSearchBarOpened) {
                    //binding.search.setFocusable(false);
                    binding.searchBarCard.setCardElevation(0);
                    binding.searchBarCard.setCardBackgroundColor(getColor(R.color.search_bar));

                    binding.searchIcon.setImageResource(R.drawable.search);
                    binding.searchBarContainer.setVisibility(View.GONE);
                    // Hide Soft Keyboard
                    Utils.softKeyboard(HomeScreen.this,false,binding.search);

                    isSearchBarOpened = false;
                }
            }
        });*/

    /*    binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isSearchBarOpened) {
                    binding.searchBarCard.setCardElevation(4);
                    binding.searchBarCard.setCardBackgroundColor(getColor(R.color.white));

                    binding.searchBarContainer.setVisibility(View.VISIBLE);
                    binding.searchIcon.setImageResource(R.drawable.close);

                    isSearchBarOpened = true;
                    *//*searchDialog = new Dialog(HomeScreen.this);
                    searchDialog.setContentView(R.layout.dialog_search_bar);
                    searchDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    searchDialog.setCancelable(false);
                    searchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    searchDialog.show();*//*

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
    }

    private void init() {

        Utils.registerInternetReceiver(this);
        initializeSocketRunCheck();

        setDefaultNotificationCount();

        rest = new Rest(this,false,false);
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


        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        requestType = GET_PROFILE_DATA;
        connectViewModel.getUserProfile(PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, ""));




        Log.d("api_token","1 = "+PreferenceConnector.readString(this,PreferenceConnector.API_CONNECT_TOKEN,""));
        Log.d("api_token","2 = "+PreferenceConnector.readString(this,PreferenceConnector.API_GTF_TOKEN_,""));
    }


    // =============================================================== Getting Images Locally from database =================================================
    private void loadLocalData(){

        galleryEntity = new GroupChannelGalleryEntity();

        // ===================================================== Profile Image Loader ================================================================

        databaseViewModel.getProfileImage().observe(this, groupChannelGalleryEntity -> {

            if (groupChannelGalleryEntity != null && groupChannelGalleryEntity.getImageData() != null) {

                galleryEntity = groupChannelGalleryEntity;

                Bitmap profileImage = LocalGalleryUtil.getImageFromDB(groupChannelGalleryEntity.getImageData());
                binding.profileImage.setImageBitmap(profileImage);
            }
        });


        // =================================================== Just to check and update Exclusive Offer List =====================================================

        /*exclusiveOfferDataModels = new ArrayList<>();
        //Get exclusive offers :
        databaseViewModel.getExclusiveOfferData().observe(this, getExclusiveOfferList -> {
            if (getExclusiveOfferList != null && !getExclusiveOfferList.isEmpty()) {

                Log.d("Exclusive","run_time_repeat");
                Log.d("Exclusive"," DB size ===== "+getExclusiveOfferList.size());


*//*                for (int i=0;i<exclusive_inserted_row_ids.size();i++){

                    for (int j=0;j<getExclusiveOfferList.size();j++) {
                        if (getExclusiveOfferList.get(j).getGroupChannelID() == exclusive_inserted_row_ids.get(i).intValue()) {
                            exclusiveOfferDataModels.add(getExclusiveOfferList.get(j));
                            break;
                        }
                    }
                }*//*


                exclusiveOfferDataModels = new ArrayList<>();
                exclusiveOfferDataModels.addAll(getExclusiveOfferList);
            }
        });*/

    }









    private void setBottomNavigation() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavOptions navOptions = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(navController.getGraph().getStartDestination(), false)
                .build();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        //binding.listHeader.setText("Recent");
                        navController.navigate(R.id.recent, null, navOptions);
                        break;
                    case 1:
                        //binding.listHeader.setText("Channel");
                        navController.navigate(R.id.channel, null, navOptions);
                        break;
                    case 2:
                        //binding.listHeader.setText("Group");
                        navController.navigate(R.id.group, null, navOptions);
                        break;
                    case 3:
                        //binding.listHeader.setText("Mentor");
                        navController.navigate(R.id.mentor, null, navOptions);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        /*if (hasReachedHomeTab){
            finish();
        }
        else {
            super.onBackPressed();
        }*/
        //binding.tabLayout.getTabAt(0).select();

        if (exitDoublePressed) {
            //rest.dismissProgressdialog();
            super.onBackPressed();
            finish();
            return;
        }

        exitDoublePressed = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> exitDoublePressed = false, 2000);

        //runOnUiThread(() -> rest.dismissProgressdialog());
    }

    private void setProfileDetails() {

        binding.firstName.setText(PreferenceConnector.readString(this,PreferenceConnector.FIRST_NAME,""));
        binding.lastName.setText(PreferenceConnector.readString(this,PreferenceConnector.LAST_NAME,""));
        //binding.userMail.setText(PreferenceConnector.readString(this,PreferenceConnector.EMAIL_ID,""));

    }


    @Override
    public void getUnreadCount(int count) {
      /*  if (count == 0) {
            binding.unreadContainer.setVisibility(View.GONE);
        } else {
            binding.unreadContainer.setVisibility(View.VISIBLE);
            binding.unreadCount.setText(String.valueOf(count));
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSearchBarOpened = false;

        loadLocalData();

        binding.greeting.setText(Utils.getDashboardGreeting());
        setProfileDetails();

        Utils.checkInternetConnection(this,this);
    }


    /**
     * Can get crashed as it holds 5 seconds delay recursive socket function :::::
     */

    public void initializeSocketRunCheck()
    {
        handler.postDelayed(new Runnable() {
            public void run() {
                socketCheck();
                handler.postDelayed(this,FIVE_SECONDS_DELAY);
            }
        }, FIVE_SECONDS_DELAY);
    }

    public void de_initializeSocketRunCheck()
    {
        handler.removeCallbacksAndMessages(null);
    }

    public void socketCheck()
    {
        if (socketInstance ==null || !socketInstance.connected())
        {
           // rest.ShowDialogue();

            SocketService instance = (SocketService) getApplication();
            socketInstance = instance.getSocketInstance();
            socketInstance.connect();

            Log.d("Socket -----", String.valueOf(socketInstance.connected()));
        }
        else{
            rest.dismissProgressdialog();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        de_initializeSocketRunCheck();
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        if(!isConnected) {

            //Utils.showSnackMessage(this, binding.search, "Internet not available!");
        }
    }



    @Override
    public void onLoading() {
       // rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderResponse(jsonObject);

    }

    @Override
    public void onAuthFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomeScreen.this,LoginScreen.class));
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


    private void renderResponse(JsonObject jsonObject)
    {

        if (requestType == GET_PROFILE_DATA){

            profileResponseModel = new ProfileResponseModel();

          Gson gson = new Gson();
          Type  type = new TypeToken<ProfileResponseModel>(){}.getType();


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


            Log.d("api_called","exclusive");
            requestType = GET_EXCLUSIVE_OFFER;
            connectViewModel.get_exclusive_offers(PreferenceConnector.readString(this,PreferenceConnector.API_GTF_TOKEN_,""),"",1);

        }
        else if (requestType == GET_EXCLUSIVE_OFFER) {
            Log.d("exclusive_response",jsonObject.toString());


            Gson gson = new Gson();
            Type type = new TypeToken<ExclusiveOfferResponseModel>(){}.getType();

            ExclusiveOfferResponseModel exclusiveOfferResponseModel = gson.fromJson(jsonObject,type);

                if (exclusiveOfferResponseModel != null && exclusiveOfferResponseModel.getData() != null && exclusiveOfferResponseModel.getData().getList() != null && !exclusiveOfferResponseModel.getData().getList().isEmpty()) {
                    String response = new Gson().toJson(exclusiveOfferResponseModel);
                    PreferenceConnector.writeString(this,PreferenceConnector.DASHBOARD_DATA+"/exclusive",response);


                    Intent intent = new Intent();
                    intent.putExtra("value",true);
                    intent.setAction("send_exclusive");
                    sendBroadcast(intent);


                   /* Bundle bundle = new Bundle();
                    bundle.putBoolean("value", true);

                    RecentFragment recentFragment = new RecentFragment();
                    recentFragment.setArguments(bundle);*/
                }
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

                        databaseViewModel.insertImageInGallery(LocalGalleryUtil.saveImage("Profile", GalleryTypeStatus.PROFILE.name(),resource,profileResponseModel.getData().getProfileInfo().getProfileImage(),profileResponseModel.getData().getProfileInfo().getProfileThumbnail(),0));
                        return false;
                    }
                }).
                fitCenter().apply(requestOptions).
                transition(DrawableTransitionOptions.withCrossFade()).into(binding.profileImage);

    }














    private void setDefaultNotificationCount(){
        if (PreferenceConnector.readInteger(this,"recent/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0) > 0){

            int count = PreferenceConnector.readInteger(this,"recent/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0);
            binding.recentNotificationContainer.setVisibility(View.VISIBLE);
            binding.recentNotificationCount.setText(count+"");

            Log.d("notification_count","Shared Preference = "+PreferenceConnector.readInteger(this,"recent/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0));
        }
        else{
            binding.recentNotificationContainer.setVisibility(View.INVISIBLE);
        }

        if (PreferenceConnector.readInteger(this,"channel/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0) > 0){

            int count = PreferenceConnector.readInteger(this,"channel/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0);
            binding.channelNotificationContainer.setVisibility(View.VISIBLE);
            binding.channelNotificationCount.setText(count+"");

            Log.d("notification_count","Shared Preference = "+PreferenceConnector.readInteger(this,"channel/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0));
        }
        else{
            binding.channelNotificationContainer.setVisibility(View.INVISIBLE);
        }

        if (PreferenceConnector.readInteger(this,"group/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0) > 0){

            int count = PreferenceConnector.readInteger(this,"group/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0);
            binding.groupNotificationContainer.setVisibility(View.VISIBLE);
            binding.groupNotificationCount.setText(count+"");

            Log.d("notification_count","Shared Preference = "+PreferenceConnector.readInteger(this,"group/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0));
        }
        else{
            binding.groupNotificationContainer.setVisibility(View.INVISIBLE);
        }

        if (PreferenceConnector.readInteger(this,"mentor/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0) > 0){

            int count = PreferenceConnector.readInteger(this,"mentor/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0);
            binding.mentorNotificationContainer.setVisibility(View.VISIBLE);
            binding.mentorNotificationCount.setText(count+"");

            Log.d("notification_count","Shared Preference = "+PreferenceConnector.readInteger(this,"mentor/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0));
        }
        else{
            binding.mentorNotificationContainer.setVisibility(View.INVISIBLE);
        }
    }






    @Override
    public void getMessageRecentCount(int count) {
        if (count <= 0){
            Log.d("notification_count","No count found");

            binding.recentNotificationContainer.setVisibility(View.INVISIBLE);
        }
        else{
            Log.d("notification_count",""+count);

            binding.recentNotificationContainer.setVisibility(View.VISIBLE);
            binding.recentNotificationCount.setText(count+"");
        }
    }

    @Override
    public void getMessageChannelCount(int count) {
        if (count <= 0){
            Log.d("notification_count","No count found");

            binding.channelNotificationContainer.setVisibility(View.INVISIBLE);
        }
        else{
            Log.d("notification_count",""+count);

            binding.channelNotificationContainer.setVisibility(View.VISIBLE);
            binding.channelNotificationCount.setText(count+"");
        }
    }

    @Override
    public void getMessageGroupCount(int count) {
        if (count <= 0){
            Log.d("notification_count","No count found");


            binding.groupNotificationContainer.setVisibility(View.INVISIBLE);
        }
        else{
            Log.d("notification_count",""+count);

            binding.groupNotificationContainer.setVisibility(View.VISIBLE);
            binding.groupNotificationCount.setText(String.valueOf(count));
        }
    }

    @Override
    public void getMessageMentorCount(int count) {
        if (count <= 0){
            Log.d("notification_count","No count found");

            binding.mentorNotificationContainer.setVisibility(View.INVISIBLE);
        }
        else{
            Log.d("notification_count",""+count);

            binding.mentorNotificationContainer.setVisibility(View.VISIBLE);
            binding.mentorNotificationCount.setText(count+"");
        }
    }
}

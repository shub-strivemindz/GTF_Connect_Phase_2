package com.gtfconnect.ui.screenUI.channelModule;

import static com.gtfconnect.services.SocketService.context;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityChannelProfileBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.ChannelSettingListener;
import com.gtfconnect.models.GroupChannelProfileDetailModel;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.DocumentAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.LinkAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.MediaAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.SettingAdapter;
import com.gtfconnect.ui.screenUI.HomeScreen;
import com.gtfconnect.ui.screenUI.authModule.LoginScreen;
import com.gtfconnect.ui.screenUI.groupModule.GroupEditProfileScreen;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.AuthViewModel;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ChannelProfileScreen extends AppCompatActivity implements ApiResponseListener, ChannelSettingListener {

    ActivityChannelProfileBinding binding;

    GroupChannelProfileDetailModel profileDetailModel;

    private final int GET_PROFILE_DETAIL = 201;

    private final int UPDATE_GC_SETTING = 301;

    private final int UPDATE_GC_REACTION_SETTING = 302;

    private final int UPDATED_GC_REACTION_SETTING = 303;

    private final int GET_UPDATED_GC_SETTING = 401;

    private int requestType = 0;

    private final int GC_REFRESH_UPDATED_DATA_CODE = 1001;

    private final int GC_REFRESH_UPDATED_REACTION_CODE = 1002;

    private Rest rest;
    private ApiResponseListener listener;
    private ConnectViewModel connectViewModel;

    private SettingAdapter settingViewAdapter;

    private int channelID;
    String api_token;

    Map<String,Object> params;

    private boolean isReactionsUpdated = false;

    private boolean isNotificationMute = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChannelProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        channelID = Integer.parseInt(PreferenceConnector.readString(this, PreferenceConnector.GC_CHANNEL_ID, ""));
        api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");


        init();

        binding.editProfile.setOnClickListener(view -> startActivity(new Intent(ChannelProfileScreen.this, GroupEditProfileScreen.class)));

        binding.title2.setOnClickListener(view -> startActivity(new Intent(ChannelProfileScreen.this,ChannelAdminSubscribersScreen.class)));

        binding.muteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNotificationMute){
                    binding.notificationText.setText("Unmute");
                }

               /* BottomSheetDialog mute_notification_dialog = new BottomSheetDialog(ChannelProfileScreen.this);
                mute_notification_dialog.setContentView(R.layout.bottomsheet_mute_notification);
                mute_notification_dialog.show();*/
            }
        });

        binding.backClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        // Dialog for Leave Channel  :
        binding.leaveChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog leave_channel_dialog = new Dialog(ChannelProfileScreen.this);

                leave_channel_dialog.setContentView(R.layout.dialog_leave_channel);
                leave_channel_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                leave_channel_dialog.setCancelable(false);
                leave_channel_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView leave = leave_channel_dialog.findViewById(R.id.sign_out);
                TextView cancel = leave_channel_dialog.findViewById(R.id.cancel);

                leave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //  Dialog for Leave Channel  :
                        leave_channel_dialog.dismiss();

                        Dialog leave_dialog = new Dialog(ChannelProfileScreen.this);

                        leave_dialog.setContentView(R.layout.dialog_left_channel);
                        leave_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        leave_dialog.setCancelable(false);
                        leave_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        leave_dialog.show();
                        new Thread(() -> {
                            try {
                                Thread.sleep(2000);
                                leave_dialog.dismiss();
                                startActivity(new Intent(ChannelProfileScreen.this, HomeScreen.class));
                                finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }).start();

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        leave_channel_dialog.dismiss();
                    }
                });

                leave_channel_dialog.show();
            }
        });


        // Dialog for Copy Link to Dashboard  :
        binding.copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog copyLink_dialog = new Dialog(ChannelProfileScreen.this);

                copyLink_dialog.setContentView(R.layout.dialog_copy_link);
                copyLink_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                copyLink_dialog.setCancelable(false);
                copyLink_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView message = (TextView) copyLink_dialog.findViewById(R.id.dialog_message);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                if (profileDetailModel.getData().getGcInfo().getPublicLink() != null && !profileDetailModel.getData().getGcInfo().getPublicLink().isEmpty()) {
                    ClipData clip = ClipData.newPlainText("Channel Link", profileDetailModel.getData().getGcInfo().getPublicLink());
                    clipboard.setPrimaryClip(clip);
                } else {
                    message.setText("No link found to copy");
                }


                copyLink_dialog.show();
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        copyLink_dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }).start();
            }
        });



        // Setting default tab 0 adapter ------
        DocumentAdapter documentViewAdapter = new DocumentAdapter(4);
        binding.profileRecycler.setHasFixedSize(true);
        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ChannelProfileScreen.this));
        binding.profileRecycler.setAdapter(documentViewAdapter);


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        DocumentAdapter documentViewAdapter = new DocumentAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ChannelProfileScreen.this));
                        binding.profileRecycler.setAdapter(documentViewAdapter);
                        break;

                    case 1:
                        LinkAdapter linkViewAdapter = new LinkAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ChannelProfileScreen.this));
                        binding.profileRecycler.setAdapter(linkViewAdapter);
                        break;

                    case 2:
                        MediaAdapter mediaViewAdapter = new MediaAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ChannelProfileScreen.this));
                        binding.profileRecycler.setAdapter(mediaViewAdapter);
                        break;

                    case 3:
                        settingViewAdapter = new SettingAdapter(ChannelProfileScreen.this, profileDetailModel, ChannelProfileScreen.this);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ChannelProfileScreen.this));
                        binding.profileRecycler.setAdapter(settingViewAdapter);
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


    private void init() {

        rest = new Rest(this,false,false);

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

        requestType = GET_PROFILE_DETAIL;
        connectViewModel.get_admin_group_channel_settings(channelID,api_token,"android","test");

    }



    private void setProfileDetails()
    {

        if (profileDetailModel.getData()!=null)
        {
            if(profileDetailModel.getData().getGcInfo()!=null)
            {
                if (!profileDetailModel.getData().getGcInfo().getName().isEmpty() && profileDetailModel.getData().getGcInfo().getName()!=null)
                {
                    binding.title.setText(profileDetailModel.getData().getGcInfo().getName());
                }
                if (profileDetailModel.getData().getGcInfo().getMemberCount() != 0 && profileDetailModel.getData().getGcInfo().getMemberCount()!=null)
                {
                    binding.subscriberCount.setText(String.valueOf(profileDetailModel.getData().getGcInfo().getMemberCount()));
                }
                else {
                    binding.title2.setVisibility(View.GONE);
                }
                if (!profileDetailModel.getData().getGcInfo().getProfileImage().isEmpty() && profileDetailModel.getData().getGcInfo().getProfileImage()!=null)
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

                    Glide.with(this).load(profileDetailModel.getData().getGcInfo().getProfileImage()).
                            fitCenter().apply(requestOptions).
                            transition(DrawableTransitionOptions.withCrossFade()).into(binding.logo);
                }

            }
        }
    }




    @Override
    public void onLoading() {
        rest.ShowDialogue();
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

        if (requestType == GET_PROFILE_DETAIL) {
            Gson gson = new Gson();
            Type type = new TypeToken<GroupChannelProfileDetailModel>() {
            }.getType();

            profileDetailModel = new GroupChannelProfileDetailModel();
            profileDetailModel = gson.fromJson(response, type);

            if (isReactionsUpdated){
                settingViewAdapter.updateData(profileDetailModel);
            }
            else {
                setProfileDetails();
            }
        }
        else if (requestType == UPDATE_GC_SETTING) {

            requestType = GET_UPDATED_GC_SETTING;
            connectViewModel.get_admin_group_channel_settings(channelID,api_token,"android","test");

        }
        else if (requestType == GET_UPDATED_GC_SETTING) {

            Gson gson = new Gson();
            Type type = new TypeToken<GroupChannelProfileDetailModel>() {
            }.getType();

            profileDetailModel = new GroupChannelProfileDetailModel();
            profileDetailModel = gson.fromJson(response, type);

            settingViewAdapter.updateData(profileDetailModel);

        } else if (requestType == UPDATE_GC_REACTION_SETTING) {

            requestType = UPDATED_GC_REACTION_SETTING;
            connectViewModel.get_admin_group_channel_settings(channelID,api_token,"android","test");

        } else if (requestType == UPDATED_GC_REACTION_SETTING) {

            Intent intent = new Intent(this, ChannelManageReactionScreen.class);
            Gson gson = new Gson();
            String data = gson.toJson(profileDetailModel);
            intent.putExtra("data",data);
            startForResult.launch(intent);
        }
    }


    @Override
    public void callPermissionClass() {
        Intent intent = new Intent(this, ChannelManagePermissionScreen.class);
        Gson gson = new Gson();
        String data = gson.toJson(profileDetailModel);
        intent.putExtra("data",data);
        startForResult.launch(intent);
    }

    @Override
    public void updateAccessTypeStatus(int status) {
        requestType = UPDATE_GC_SETTING;
        params = new HashMap<>();
        params.put("Name",profileDetailModel.getData().getGcInfo().getName());
        params.put("Description",profileDetailModel.getData().getGcInfo().getDescription());
        params.put("Type",profileDetailModel.getData().getGcInfo().getType());

        if (status == 0){
            params.put("AccessType","private");
        }
        else{
            params.put("AccessType","public");
        }
        connectViewModel.update_groupChannel_profile(channelID,api_token,"android","test",params);
    }

    @Override
    public void updateSignMessageStatus(int status) {
        requestType = UPDATE_GC_SETTING;
        params = new HashMap<>();
        params.put("SignedMsg",status);
        connectViewModel.update_groupChannel_settings(channelID,api_token,"android","test",params);
    }

    @Override
    public void updateDiscussionStatus(int status) {
        requestType = UPDATE_GC_SETTING;
        params = new HashMap<>();
        params.put("AllowDiscussion",status);
        connectViewModel.update_groupChannel_settings(channelID,api_token,"android","test",params);
    }

    @Override
    public void updateViewChatHistoryStatus(int status) {

    }

    @Override
    public void updateManipulateViewsStatus(int status,int percent) {
        requestType = UPDATE_GC_SETTING;
        params = new HashMap<>();
        params.put("EnableManipulateViews", status);
        params.put("ManipulateViewsPercent", percent);
        connectViewModel.update_groupChannel_settings(channelID,api_token,"android","test",params);
    }

    @Override
    public void callManageReactionsClass(int status) {

        if (status == 0) {
            requestType = UPDATE_GC_REACTION_SETTING;
            params = new HashMap<>();
            params.put("EnableReactions", status);
            connectViewModel.update_groupChannel_settings(channelID, api_token, "android", "test", params);
        }
        else{

            Intent intent = new Intent(this, ChannelManageReactionScreen.class);
            Gson gson = new Gson();
            String data = gson.toJson(profileDetailModel);
            intent.putExtra("data",data);
            startForResult.launch(intent);
        }


    }

// -------------------------------------------------------------- Handling all update from Other Screens -----------------------------------------------------------

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == GC_REFRESH_UPDATED_DATA_CODE) {

                    binding.tabLayout.getTabAt(3).select();

                    requestType = GET_PROFILE_DETAIL;
                    connectViewModel.get_admin_group_channel_settings(channelID,api_token,"android","test");
                }
                else if (result.getResultCode() == GC_REFRESH_UPDATED_REACTION_CODE) {

                    binding.tabLayout.getTabAt(3).select();

                    requestType = GET_PROFILE_DETAIL;
                    isReactionsUpdated = true;
                    connectViewModel.get_admin_group_channel_settings(channelID,api_token,"android","test");
                }
            });
}
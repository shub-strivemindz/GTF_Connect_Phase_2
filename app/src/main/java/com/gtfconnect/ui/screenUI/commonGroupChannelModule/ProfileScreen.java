package com.gtfconnect.ui.screenUI.commonGroupChannelModule;


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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
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
import com.gtfconnect.models.commonGroupChannelResponseModels.GroupChannelInfoResponseModel;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.roomDB.dbEntities.UserProfileDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.DocumentAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.MediaAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.SettingAdapter;
import com.gtfconnect.ui.screenUI.HomeScreen;
import com.gtfconnect.ui.screenUI.channelModule.ChannelAdminSubscribersScreen;
import com.gtfconnect.ui.screenUI.channelModule.ChannelChatsScreen;
import com.gtfconnect.ui.screenUI.recentModule.ExclusiveOfferScreen;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ProfileScreen extends AppCompatActivity implements ApiResponseListener, ChannelSettingListener {

    ActivityChannelProfileBinding binding;

    private InfoDbEntity profileDetailModel;

    private final int GET_GROUP_CHANNEL_INFO = 201;

    private final int UPDATE_GC_SETTING = 301;

    private final int UPDATE_GC_REACTION_SETTING = 302;

    private final int UPDATED_GC_REACTION_SETTING = 303;

    private final int GET_UPDATED_GC_SETTING = 401;

    private int requestType = 0;

    private final int GC_REFRESH_UPDATED_DATA_CODE = 1001;

    private final int GC_REFRESH_UPDATED_REACTION_CODE = 1002;


    private DatabaseViewModel databaseViewModel;


    private Rest rest;
    private ApiResponseListener listener;
    private ConnectViewModel connectViewModel;

    private SettingAdapter settingViewAdapter;

    private int channelID;
    String api_token;

    Map<String, Object> params;

    private UserProfileDbEntity userProfileData;

    private boolean isReactionsUpdated = false;

    private boolean isNotificationEnabled;

    private boolean isUpdateNotificationSettingCalled = false;

    private String viewType = "";

    private String userStatus = "";


    private boolean isSettingEnabled = false;
    private boolean isDiscussionEnabled = false;


    private String profile_image = "";
    private String member_count = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChannelProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        channelID = Integer.parseInt(PreferenceConnector.readString(this, PreferenceConnector.GC_CHANNEL_ID, ""));
        api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");

        viewType = getIntent().getStringExtra("viewType");
        if (viewType == null) {
            viewType = "";
        } else {
            if (viewType.equalsIgnoreCase(Constants.Group_Channel_TYPE_1)) {
                binding.editProfile.setText("Edit Group");
            } else {
                binding.editProfile.setText("Edit Channel");
            }
        }


        init();


        binding.editProfile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileScreen.this, EditProfileScreen.class);
            intent.putExtra("image", profileDetailModel.getGcInfo().getProfileImage());
            intent.putExtra("title", profileDetailModel.getGcInfo().getName());
            intent.putExtra("description", profileDetailModel.getGcInfo().getDescription());

            intent.putExtra("accessType", profileDetailModel.getGcInfo().getAccessType());
            intent.putExtra("type", profileDetailModel.getGcInfo().getType());
            startForResult.launch(intent);
        });

        binding.title2.setOnClickListener(view -> startActivity(new Intent(ProfileScreen.this, ChannelAdminSubscribersScreen.class)));

        binding.logo.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileScreen.this, ImagePreviewScreen.class);

            if (profileDetailModel != null && profileDetailModel.getGcInfo() != null) {
                if (profileDetailModel.getGcInfo().getProfileImage() != null && !profileDetailModel.getGcInfo().getProfileImage().isEmpty()) {
                    intent.putExtra("image", profileDetailModel.getGcInfo().getProfileImage());
                    startActivity(intent);
                }
            }
        });

        binding.muteNotification.setOnClickListener(view -> {

            requestType = UPDATE_GC_SETTING;
            params = new HashMap<>();

            if (isNotificationEnabled) {
                params.put("IsNotification", 0);
                isNotificationEnabled = false;
            } else {
                params.put("IsNotification", 1);
                isNotificationEnabled = true;
            }
            connectViewModel.update_groupChannel_settings(channelID, api_token, params);
        });

        binding.backClick.setOnClickListener(view -> finish());


        // Dialog for Leave Channel  :
        binding.leaveChannel.setOnClickListener(view -> {

            if (userStatus.equalsIgnoreCase(Constants.USER_ACTIVE)) {

                Dialog leave_channel_dialog = new Dialog(ProfileScreen.this);

                leave_channel_dialog.setContentView(R.layout.dialog_leave_channel);
                leave_channel_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                leave_channel_dialog.setCancelable(false);
                leave_channel_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView group_channel_name = leave_channel_dialog.findViewById(R.id.group_channel_name);
                TextView leave = leave_channel_dialog.findViewById(R.id.sign_out);
                TextView cancel = leave_channel_dialog.findViewById(R.id.cancel);

                if (profileDetailModel != null && profileDetailModel.getGcInfo() != null) {
                    group_channel_name.setText(profileDetailModel.getGcInfo().getName());
                }

                leave.setOnClickListener(v -> {

                    leave_channel_dialog.dismiss();

                    requestType = Constants.LEAVE_GROUP_CHANNEL;
                    connectViewModel.leave_group_channel(profileDetailModel.getGcMemberInfo().getGCMemberID(), api_token);


                });

                cancel.setOnClickListener(v -> leave_channel_dialog.dismiss());

                leave_channel_dialog.show();
            }
            if (userStatus.equalsIgnoreCase(Constants.USER_LEFT)) {

                Dialog rejoin_channel_dialog = new Dialog(ProfileScreen.this);

                rejoin_channel_dialog.setContentView(R.layout.dialog_rejoin_channel);
                rejoin_channel_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rejoin_channel_dialog.setCancelable(false);
                rejoin_channel_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView group_channel_name = rejoin_channel_dialog.findViewById(R.id.group_channel_name);
                TextView leave = rejoin_channel_dialog.findViewById(R.id.sign_out);
                TextView cancel = rejoin_channel_dialog.findViewById(R.id.cancel);

                if (profileDetailModel != null && profileDetailModel.getGcInfo() != null) {
                    group_channel_name.setText(profileDetailModel.getGcInfo().getName());
                }

                leave.setOnClickListener(v -> {

                    rejoin_channel_dialog.dismiss();

                    requestType = Constants.REJOIN_GROUP_CHANNEL;
                    connectViewModel.rejoin_group_channel(profileDetailModel.getGcMemberInfo().getGCMemberID(), api_token);


                });

                cancel.setOnClickListener(v -> rejoin_channel_dialog.dismiss());

                rejoin_channel_dialog.show();
            }
            if (userStatus.equalsIgnoreCase(Constants.USER_RENEW_PLAN)){
                String data = new Gson().toJson(profileDetailModel.getGcSubscriptionPlan());

                Intent intent = new Intent(ProfileScreen.this, ExclusiveOfferScreen.class);
                intent.putExtra("plans",data);
                intent.putExtra("profile_image",profile_image);
                intent.putExtra("member_count",member_count);
                startActivity(intent);
                finish();
            }


        });


        // Dialog for Copy Link to Dashboard  :
        binding.copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog copyLink_dialog = new Dialog(ProfileScreen.this);

                copyLink_dialog.setContentView(R.layout.dialog_copy_link);
                copyLink_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                copyLink_dialog.setCancelable(false);
                copyLink_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView message = (TextView) copyLink_dialog.findViewById(R.id.dialog_message);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                if (profileDetailModel.getGcInfo().getPublicLink() != null && !profileDetailModel.getGcInfo().getPublicLink().isEmpty()) {
                    ClipData clip = ClipData.newPlainText("Channel Link", profileDetailModel.getGcInfo().getPublicLink());
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
        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ProfileScreen.this));
        binding.profileRecycler.setAdapter(documentViewAdapter);


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        DocumentAdapter documentViewAdapter = new DocumentAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ProfileScreen.this));
                        binding.profileRecycler.setAdapter(documentViewAdapter);
                        break;

                    case 1:
                        MediaAdapter mediaViewAdapter = new MediaAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ProfileScreen.this));
                        binding.profileRecycler.setAdapter(mediaViewAdapter);
                        break;

                    case 2:
                    default:
                        settingViewAdapter = new SettingAdapter(ProfileScreen.this, profileDetailModel, isSettingEnabled, isDiscussionEnabled, ProfileScreen.this, viewType);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ProfileScreen.this));
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


    private void loadLocalData() {

        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        databaseViewModel.getGroupChannelInfo(channelID).observe(this, infoDbEntity -> {
            if (infoDbEntity != null) {
                this.profileDetailModel = infoDbEntity;
                rest.dismissProgressdialog();

                if(profileDetailModel != null) {
                    checkGroupChannelSettings();
                }

                if (requestType == GET_UPDATED_GC_SETTING) {
                    updateNotificationSetting();
                    settingViewAdapter.updateData(profileDetailModel);
                } else {
                    updateNotificationSetting();

                    if (isReactionsUpdated) {
                        settingViewAdapter.updateData(profileDetailModel);
                    } else {
                        setProfileDetails();
                    }
                }

            }
        });


        databaseViewModel.getUserProfileData().observe(this, new Observer<UserProfileDbEntity>() {
            @Override
            public void onChanged(UserProfileDbEntity userProfileDbEntity) {

                if (userProfileDbEntity != null) {
                    userProfileData = userProfileDbEntity;
                    checkUserAdminPermission();
                }
            }
        });
    }


    private void init() {


        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);


        rest = new Rest(this, false, false);

        rest = new Rest(this, true, false);
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

        requestType = GET_GROUP_CHANNEL_INFO;
        connectViewModel.get_group_channel_info(channelID, api_token);

    }


    private void setProfileDetails() {

        if (profileDetailModel != null) {


            if (profileDetailModel.getGcInfo() != null ){
                if (profileDetailModel.getGcInfo().getProfileImage() != null) {
                    profile_image = profileDetailModel.getGcInfo().getProfileImage();
                }
                else{
                    profile_image = "";
                }

                if (profileDetailModel.getGcInfo().getMemberCount() != null){
                    member_count = String.valueOf(profileDetailModel.getGcInfo().getMemberCount());
                }
                else{
                    member_count = "";
                }
            }
            else{
                profile_image = "";
                member_count = "";
            }



            if (profileDetailModel.getGcInfo() != null) {
                if (!profileDetailModel.getGcInfo().getName().isEmpty() && profileDetailModel.getGcInfo().getName() != null) {
                    binding.title.setText(profileDetailModel.getGcInfo().getName());
                }
                if (profileDetailModel.getGcInfo().getMemberCount() != 0 && profileDetailModel.getGcInfo().getMemberCount() != null) {
                    binding.subscriberCount.setText(String.valueOf(profileDetailModel.getGcInfo().getMemberCount()));
                } else {
                    binding.title2.setVisibility(View.GONE);
                }
                if (profileDetailModel.getGcInfo().getProfileImage() != null && !profileDetailModel.getGcInfo().getProfileImage().isEmpty()) {
                    //Setting up loader on post
                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(circularProgressDrawable);
                    requestOptions.error(R.drawable.no_image_logo_background);
                    requestOptions.skipMemoryCache(false);
                    requestOptions.fitCenter();

                    Glide.with(this).load(profileDetailModel.getGcInfo().getProfileImage()).
                            fitCenter().apply(requestOptions).
                            transition(DrawableTransitionOptions.withCrossFade()).into(binding.logo);
                }
            }
        }
    }


    private void updateNotificationSetting() {
        if (profileDetailModel != null && profileDetailModel != null) {
            if (profileDetailModel.getGcSetting() != null) {
                if (profileDetailModel.getGcSetting().getIsNotification() != null) {

                    if (profileDetailModel.getGcSetting().getIsNotification() == 0) {
                        isNotificationEnabled = false;
                        binding.notificationText.setText("Unmute");
                        binding.notificationIcon.setImageDrawable(getResources().getDrawable(R.drawable.unmute));
                    } else {
                        isNotificationEnabled = true;
                        binding.notificationText.setText("Mute");
                        binding.notificationIcon.setImageDrawable(getResources().getDrawable(R.drawable.mute));
                    }

                    Log.d("Mute_status", profileDetailModel.getGcSetting().getIsNotification() + "");
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadLocalData();
    }

    @Override
    public void onLoading() {
        if (requestType == GET_GROUP_CHANNEL_INFO) {
            if (profileDetailModel == null) {
                rest.ShowDialogue();
            }
        } else if (requestType == Constants.LEAVE_GROUP_CHANNEL) {
            rest.ShowDialogue();
        } else {
            rest.ShowDialogue();
        }
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        Log.d("rendered", "successfully!");
        renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        Log.d("rendered", "successfully!");
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


    private void renderResponse(JsonObject response) {
        Log.d("rendered", "successfully!");

        if (requestType == GET_GROUP_CHANNEL_INFO) {
            Gson gson = new Gson();
            Type type = new TypeToken<GroupChannelInfoResponseModel>() {
            }.getType();


            GroupChannelInfoResponseModel groupChannelInfoResponseModel = gson.fromJson(response, type);

            if (groupChannelInfoResponseModel != null && groupChannelInfoResponseModel.getData() != null) {
                InfoDbEntity data;
                data = groupChannelInfoResponseModel.getData();
                data.setGroupChannelID(channelID);

                databaseViewModel.insertGroupChannelInfo(data);
            }
        } else if (requestType == UPDATE_GC_SETTING) {

            requestType = GET_UPDATED_GC_SETTING;
            connectViewModel.get_group_channel_info(channelID, api_token);


        } else if (requestType == GET_UPDATED_GC_SETTING) {

            Gson gson = new Gson();
            Type type = new TypeToken<GroupChannelInfoResponseModel>() {
            }.getType();


            GroupChannelInfoResponseModel groupChannelInfoResponseModel = gson.fromJson(response, type);

            if (groupChannelInfoResponseModel != null && groupChannelInfoResponseModel.getData() != null) {
                InfoDbEntity data;
                data = groupChannelInfoResponseModel.getData();
                data.setGroupChannelID(channelID);

                databaseViewModel.insertGroupChannelInfo(data);
            }

        } else if (requestType == UPDATE_GC_REACTION_SETTING) {

            requestType = UPDATED_GC_REACTION_SETTING;
            connectViewModel.get_group_channel_info(channelID, api_token);

        } else if (requestType == UPDATED_GC_REACTION_SETTING) {

            Intent intent = new Intent(this, ManageReactionScreen.class);
            Gson gson = new Gson();
            String data = gson.toJson(profileDetailModel);
            intent.putExtra("data", data);
            startForResult.launch(intent);
        } else if (requestType == Constants.LEAVE_GROUP_CHANNEL) {

            //  Dialog for Leave Channel  :

            Dialog leave_dialog = new Dialog(ProfileScreen.this);

            leave_dialog.setContentView(R.layout.dialog_left_channel);
            leave_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            leave_dialog.setCancelable(false);
            leave_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            leave_dialog.show();
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    leave_dialog.dismiss();
                    startActivity(new Intent(ProfileScreen.this, HomeScreen.class));
                    finishAffinity();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();

        } else if (requestType == Constants.REJOIN_GROUP_CHANNEL) {

            Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show();

            requestType = GET_GROUP_CHANNEL_INFO;
            connectViewModel.get_group_channel_info(channelID, PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, ""));


        }
    }


    @Override
    public void callPermissionClass() {
        Intent intent = new Intent(this, ManagePermissionScreen.class);
        Gson gson = new Gson();
        String data = gson.toJson(profileDetailModel);
        intent.putExtra("data", data);
        startForResult.launch(intent);
    }

    @Override
    public void updateAccessTypeStatus(int status) {
        requestType = UPDATE_GC_SETTING;
        params = new HashMap<>();

        params.put("Name", profileDetailModel.getGcInfo().getName());
        params.put("Description", profileDetailModel.getGcInfo().getDescription());
        params.put("Type", profileDetailModel.getGcInfo().getType());

        if (status == 0) {
            params.put("AccessType", "private");
        } else {
            params.put("AccessType", "public");
        }
        connectViewModel.update_groupChannel_profile(channelID, api_token, params, null);
    }

    @Override
    public void updateSignMessageStatus(int status) {
        requestType = UPDATE_GC_SETTING;
        params = new HashMap<>();
        params.put("SignedMsg", status);
        connectViewModel.update_groupChannel_settings(channelID, api_token, params);
    }

    @Override
    public void updateDiscussionStatus(int status) {
        requestType = UPDATE_GC_SETTING;
        params = new HashMap<>();
        params.put("AllowDiscussion", status);
        connectViewModel.update_groupChannel_settings(channelID, api_token, params);
    }

    @Override
    public void updateViewChatHistoryStatus(int status) {
        requestType = UPDATE_GC_SETTING;
        params = new HashMap<>();
        params.put("ChatHistoryIsEnable", status);
        connectViewModel.update_groupChannel_settings(channelID, api_token, params);
    }

    @Override
    public void updateManipulateViewsStatus(int status, int percent) {
        requestType = UPDATE_GC_SETTING;
        params = new HashMap<>();
        params.put("EnableManipulateViews", status);
        params.put("ManipulateViewsPercent", percent);
        connectViewModel.update_groupChannel_settings(channelID, api_token, params);
    }

    @Override
    public void callManageReactionsClass(int status) {

        if (status == 0) {
            requestType = UPDATE_GC_REACTION_SETTING;
            params = new HashMap<>();
            params.put("EnableReactions", status);
            connectViewModel.update_groupChannel_settings(channelID, api_token, params);
        } else {

            Intent intent = new Intent(this, ManageReactionScreen.class);
            Gson gson = new Gson();
            String data = gson.toJson(profileDetailModel);
            intent.putExtra("data", data);
            startForResult.launch(intent);
        }


    }

// -------------------------------------------------------------- Handling all update from Other Screens -----------------------------------------------------------

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == GC_REFRESH_UPDATED_DATA_CODE) {

                    binding.tabLayout.getTabAt(3).select();

                    requestType = GET_GROUP_CHANNEL_INFO;
                    connectViewModel.get_group_channel_info(channelID, api_token);
                } else if (result.getResultCode() == GC_REFRESH_UPDATED_REACTION_CODE) {

                    binding.tabLayout.getTabAt(3).select();

                    requestType = GET_GROUP_CHANNEL_INFO;
                    isReactionsUpdated = true;
                    connectViewModel.get_group_channel_info(channelID, api_token);
                } else if (result.getResultCode() == Constants.PROFILE_DETAILS_UPDATED) {

                    requestType = GET_GROUP_CHANNEL_INFO;
                    connectViewModel.get_group_channel_info(channelID, api_token);
                }
            });


    // -------------------------------------------------------------- Check User & Admin Permissions -----------------------------------------------------------

    private void checkUserAdminPermission() {

        if (userProfileData != null && userProfileData.getUserRoleInfo() != null) {
            if (userProfileData.getUserRoleInfo().getPrimary() != null && userProfileData.getUserRoleInfo().getPrimary().getRole() != null) {
                if (userProfileData.getUserRoleInfo().getPrimary().getRole().getSlug() != null) {

                    String userType = userProfileData.getUserRoleInfo().getPrimary().getRole().getSlug();

                    // =============================================  SUPER ADMIN CHECK  ===============================================================

                    if (userType.equalsIgnoreCase("user") || userType.equalsIgnoreCase("admin")) {
                        checkAdminChecks();
                    }

                    // =============================================  SUPER ADMIN CHECK  ===============================================================
                    else if (!userType.equalsIgnoreCase("mentor")) {
                        binding.editProfile.setVisibility(View.VISIBLE);
                        isSettingEnabled = true;
                        isDiscussionEnabled = true;
                    }
                }
            }
        }

    }


    private void checkAdminChecks() {

        /**
         * Admin Allowed Permission Check ======================
         * 1. gc_edit_profile
         * 2. enable_disable_discussion
         * 3. gc_setting
         */

        if (userProfileData.getUserRoleInfo().getPrimary().getRole().getRolePermissions() != null && !userProfileData.getUserRoleInfo().getPrimary().getRole().getRolePermissions().isEmpty()) {

            for (int i = 0; i < userProfileData.getUserRoleInfo().getPrimary().getRole().getRolePermissions().size(); i++) {

                if (userProfileData.getUserRoleInfo().getPrimary().getRole().getRolePermissions().get(i).getPermission() != null) {

                    int permissionValue = 0;

                    if (userProfileData.getUserRoleInfo().getPrimary().getRole().getRolePermissions().get(i).getPermissionValue() != null) {
                        permissionValue = userProfileData.getUserRoleInfo().getPrimary().getRole().getRolePermissions().get(i).getPermissionValue();
                    }

                    if (userProfileData.getUserRoleInfo().getPrimary().getRole().getRolePermissions().get(i).getPermission().getName() != null) {


                        if (userProfileData.getUserRoleInfo().getPrimary().getRole().getRolePermissions().get(i).getPermission().getName().equalsIgnoreCase("gc_edit_profile")) {
                            if (permissionValue == 1) {
                                binding.editProfile.setVisibility(View.VISIBLE);
                            } else {
                                binding.editProfile.setVisibility(View.GONE);
                            }
                        } else if (userProfileData.getUserRoleInfo().getPrimary().getRole().getRolePermissions().get(i).getPermission().getName().equalsIgnoreCase("gc_setting")) {
                            if (permissionValue == 1) {
                                isSettingEnabled = true;
                            } else {
                                isSettingEnabled = false;
                            }
                        } else if (userProfileData.getUserRoleInfo().getPrimary().getRole().getRolePermissions().get(i).getPermission().getName().equalsIgnoreCase("enable_disable_discussion")) {
                            if (permissionValue == 1) {
                                isDiscussionEnabled = true;
                            } else {
                                isDiscussionEnabled = false;
                            }
                        }
                    }
                }
            }

        }


        /**
         * Special User Case Admin Allowed Permission Check ==========
         1. gc_edit_profile
         * 2. enable_disable_discussion
         * 3. gc_setting
         */


        if (userProfileData.getUserPermission() != null && !userProfileData.getUserPermission().isEmpty()) {

            for (int i = 0; i < userProfileData.getUserPermission().size(); i++) {

                if (userProfileData.getUserPermission().get(i).getPermission() != null) {

                    int permissionValue = 0;

                    if (userProfileData.getUserPermission().get(i).getCustomValue() != null) {
                        permissionValue = Integer.parseInt(userProfileData.getUserPermission().get(i).getCustomValue());
                    }

                    if (userProfileData.getUserPermission().get(i).getPermission().getName() != null) {


                        if (userProfileData.getUserPermission().get(i).getPermission().getName().equalsIgnoreCase("gc_edit_profile")) {
                            if (permissionValue == 1) {
                                binding.editProfile.setVisibility(View.VISIBLE);
                            } else {
                                binding.editProfile.setVisibility(View.GONE);
                            }
                        } else if (userProfileData.getUserPermission().get(i).getPermission().getName().equalsIgnoreCase("gc_setting")) {
                            if (permissionValue == 1) {
                                isSettingEnabled = true;
                            } else {
                                isSettingEnabled = false;
                            }
                        } else if (userProfileData.getUserPermission().get(i).getPermission().getName().equalsIgnoreCase("enable_disable_discussion")) {
                            if (permissionValue == 1) {
                                isDiscussionEnabled = true;
                            } else {
                                isDiscussionEnabled = false;
                            }
                        }
                    }
                }

            }
        }


        getGroupChannelAdminChecks();
    }


    private void getGroupChannelAdminChecks() {

        if (profileDetailModel != null) {

            if (profileDetailModel.getGcMemberInfo() != null && profileDetailModel.getGcMemberInfo().getStatus() != null) {

                if (profileDetailModel.getGcMemberInfo().getStatus().equalsIgnoreCase("active")) {

                    binding.joinRejoinIcon.setImageDrawable(getResources().getDrawable(R.drawable.leave));
                    binding.joinRejoinTitle.setText("Leave");
                } else if (profileDetailModel.getGcMemberInfo().getStatus().equalsIgnoreCase("inactive")) {

                    binding.joinRejoinIcon.setImageDrawable(getResources().getDrawable(R.drawable.rejoin));
                    binding.joinRejoinTitle.setText("Rejoin");
                } else {
                    binding.joinRejoinIcon.setImageDrawable(getResources().getDrawable(R.drawable.leave));
                    binding.joinRejoinTitle.setText("Leave");
                }
            }
        }
    }


    private void checkGroupChannelSettings() {

        if (profileDetailModel.getGcMemberInfo() != null) {
            if (profileDetailModel.getGcMemberInfo().getStatus() != null) {
                if (profileDetailModel.getGcMemberInfo().getStatus().equalsIgnoreCase("active")) {


                    binding.copyLink.setVisibility(View.VISIBLE);
                    binding.muteNotification.setVisibility(View.VISIBLE);

                    binding.joinRejoinTitle.setText("Leave");
                    binding.joinRejoinIcon.setImageDrawable(getDrawable(R.drawable.leave));

                    userStatus = Constants.USER_ACTIVE;
                } else {

                    binding.joinRejoinTitle.setText("Rejoin");
                    binding.joinRejoinIcon.setImageDrawable(getDrawable(R.drawable.rejoin));

                    binding.copyLink.setVisibility(View.GONE);
                    binding.muteNotification.setVisibility(View.GONE);

                    userStatus = Constants.USER_LEFT;
                }
            }
        }


        if (profileDetailModel.getGcMemberSubscriptionPlan() != null && profileDetailModel.getGcMemberSubscriptionPlan().getIsExpired() != null) {

            if (profileDetailModel.getGcMemberSubscriptionPlan().getIsExpired() == 1) {

                userStatus = Constants.USER_RENEW_PLAN;


            }
        }


    }
}
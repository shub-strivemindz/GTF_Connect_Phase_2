package com.gtfconnect.ui.screenUI;

import static com.gtfconnect.services.SocketService.socketInstance;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gtfconnect.R;


import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityHomeBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.UnreadCountHeaderListener;
import com.gtfconnect.services.InternetService;
import com.gtfconnect.services.SocketService;
import com.gtfconnect.ui.adapters.DashboardPagerAdapter;
import com.gtfconnect.ui.screenUI.authModule.LoginScreen;
import com.gtfconnect.ui.screenUI.userProfileModule.UserProfileScreen;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.AuthViewModel;
import com.gtfconnect.viewModels.ConnectViewModel;

import org.w3c.dom.Text;

import io.socket.client.Socket;

public class HomeScreen extends AppCompatActivity implements UnreadCountHeaderListener, InternetService.ReceiverListener,ApiResponseListener {

    private ActivityHomeBinding binding;
    private boolean isSearchBarOpened = false;
    //private boolean hasReachedHomeTab = true;

    private static boolean exitDoublePressed = false;



    private Dialog searchDialog;

    private Socket mSocket;

    private boolean isProfileDataEmpty = true;

    private final static int FIVE_SECONDS_DELAY = 5000;

    private static Handler handler = new Handler();

    private ConnectViewModel connectViewModel;

    private String[] tab_name_list =  {"Home","Channel","Group","Mentor"};
    private int[] tab_icon_list = {R.drawable.home,R.drawable.channel,R.drawable.group,R.drawable.mentor};

    private Rest rest;
    private ApiResponseListener listener;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();

        binding.dashboardViewPager.setUserInputEnabled(false);
        binding.dashboardViewPager.setAdapter(createBottomSheetAdapter());
        new TabLayoutMediator(binding.tabLayout, binding.dashboardViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        //tab.setText(approvalStatus[position]);
                        TextView tabItem = (TextView) LayoutInflater.from(HomeScreen.this).inflate(R.layout.custom_tab_item, null);
                        tabItem.setText(tab_name_list[position]);
                        tabItem.setCompoundDrawablesWithIntrinsicBounds(0, tab_icon_list[position], 0, 0);
                        tab.setCustomView(tabItem);
                    }
                }).attach();


        PreferenceConnector.writeBoolean(this,PreferenceConnector.IS_USER_LOGGED,true);


        Log.d("USER ID ", String.valueOf(PreferenceConnector.readInteger(this,PreferenceConnector.GTF_USER_ID,0)));

        //-------------------------------------------------------------  Shared Preference Data ----------------------------------------------------------------


        //-------------------------------------------------------------   ----------------------------------------------------------------


        //setBottomNavigation();

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
       /* binding.searchIcon.setOnClickListener(new View.OnClickListener() {
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
        });

        binding.search.addTextChangedListener(new TextWatcher() {
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

        connectViewModel.getUserProfile(PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, ""));


    }



    private DashboardPagerAdapter createBottomSheetAdapter() {
        setBottomNavigation();
        DashboardPagerAdapter adapter = new DashboardPagerAdapter(this,4);
        return adapter;
    }









    private void setBottomNavigation() {

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tabItem = (TextView) tab.getCustomView();

                tabItem.setTextColor(getResources().getColor(R.color.theme_green));
                tabItem.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.theme_green));
                tab.setCustomView(tabItem);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tabItem = (TextView) tab.getCustomView();

                tabItem.setTextColor(getResources().getColor(R.color.textGroupMessageColor));
                tabItem.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.textGroupMessageColor));
                //setTabTextViewDrawableColor(tabItem,getResources().getColor(R.color.textGroupMessageColor));
                tab.setCustomView(tabItem);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setTabTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
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


    private void setBadgeCount()
    {

    }


    @Override
    public void getUnreadCount(int count) {
        if (count == 0) {
            //binding.unreadContainer.setVisibility(View.GONE);
        } else {
            //binding.unreadContainer.setVisibility(View.VISIBLE);
            //binding.unreadCount.setText(String.valueOf(count));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //binding.searchBarContainer.setVisibility(View.GONE);
       // binding.searchIcon.setImageResource(R.drawable.search);
        isSearchBarOpened = false;


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
            //rest.ShowDialogue();

            SocketService instance = (SocketService) getApplication();
            mSocket = instance.getSocketInstance();
            mSocket.connect();

            Log.d("Socket -----", String.valueOf(mSocket.connected()));
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
        //renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        //renderResponse(jsonObject);

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
}
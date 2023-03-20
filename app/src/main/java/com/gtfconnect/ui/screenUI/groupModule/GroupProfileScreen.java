package com.gtfconnect.ui.screenUI.groupModule;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.gtfconnect.R;
import com.gtfconnect.databinding.ActivityChannelHomeBinding;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.DocumentAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.LinkAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.MediaAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.SettingAdapter;
import com.gtfconnect.ui.screenUI.HomeScreen;

public class GroupProfileScreen extends AppCompatActivity {

    ActivityChannelHomeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChannelHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.editProfile.setOnClickListener(view -> startActivity(new Intent(GroupProfileScreen.this,GroupEditProfileScreen.class)));


        binding.muteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog mute_notification_dialog = new BottomSheetDialog(GroupProfileScreen.this);
                mute_notification_dialog.setContentView(R.layout.bottomsheet_mute_notification);
                mute_notification_dialog.show();
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
                Dialog leave_channel_dialog = new Dialog(GroupProfileScreen.this);

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

                        Dialog leave_dialog = new Dialog(GroupProfileScreen.this);

                        leave_dialog.setContentView(R.layout.dialog_left_channel);
                        leave_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        leave_dialog.setCancelable(false);
                        leave_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        leave_dialog.show();
                        new Thread(() -> {
                            try {
                                Thread.sleep(2000);
                                leave_dialog.dismiss();
                                startActivity(new Intent(GroupProfileScreen.this, HomeScreen.class));
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
                Dialog copyLink_dialog = new Dialog(GroupProfileScreen.this);

                copyLink_dialog.setContentView(R.layout.dialog_copy_link);
                copyLink_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                copyLink_dialog.setCancelable(false);
                copyLink_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        DocumentAdapter documentViewAdapter= new DocumentAdapter(4);
        binding.profileRecycler.setHasFixedSize(true);
        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(GroupProfileScreen.this));
        binding.profileRecycler.setAdapter(documentViewAdapter);



        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0:
                        DocumentAdapter documentViewAdapter= new DocumentAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(GroupProfileScreen.this));
                        binding.profileRecycler.setAdapter(documentViewAdapter);
                        break;

                    case 1:
                        LinkAdapter linkViewAdapter= new LinkAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(GroupProfileScreen.this));
                        binding.profileRecycler.setAdapter(linkViewAdapter);
                        break;

                    case 2:
                        MediaAdapter mediaViewAdapter= new MediaAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(GroupProfileScreen.this));
                        binding.profileRecycler.setAdapter(mediaViewAdapter);
                        break;

                    case 3:
                        SettingAdapter settingViewAdapter= new SettingAdapter(GroupProfileScreen.this);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(GroupProfileScreen.this));
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
}

package com.gtfconnect.ui.screenUI.channelModule;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.gtfconnect.R;
import com.gtfconnect.databinding.ActivityGroupChannelMemberProfileBinding;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.DocumentAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.LinkAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.MediaAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.SettingAdapter;

public class ChannelMemberProfileScreen extends AppCompatActivity {

    ActivityGroupChannelMemberProfileBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGroupChannelMemberProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.blockUserContainer.setOnClickListener(view -> {
            Dialog block_user_dialog = new Dialog(this);

            block_user_dialog.setContentView(R.layout.dialog_block_user);
            block_user_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            block_user_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView block = block_user_dialog.findViewById(R.id.block);
            TextView cancel = block_user_dialog.findViewById(R.id.cancel);

            block.setOnClickListener(view1 -> block_user_dialog.dismiss());
            cancel.setOnClickListener(view1 ->  block_user_dialog.dismiss());

            block_user_dialog.show();
        });


        binding.reportUser.setOnClickListener(view -> {
            BottomSheetDialog report_user_bottomSheet = new BottomSheetDialog(this);
            report_user_bottomSheet.setContentView(R.layout.bottomsheet_report_user_options);

            MaterialCardView report_user =  report_user_bottomSheet.findViewById(R.id.report_user);
            ImageView back = report_user_bottomSheet.findViewById(R.id.back);

            report_user.setOnClickListener(view1 -> report_user_bottomSheet.dismiss());
            back.setOnClickListener(view1 -> report_user_bottomSheet.dismiss());


            report_user_bottomSheet.show();
        });




        DocumentAdapter documentViewAdapter = new DocumentAdapter(4);
        binding.profileRecycler.setHasFixedSize(true);
        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ChannelMemberProfileScreen.this));
        binding.profileRecycler.setAdapter(documentViewAdapter);


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        DocumentAdapter documentViewAdapter = new DocumentAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ChannelMemberProfileScreen.this));
                        binding.profileRecycler.setAdapter(documentViewAdapter);
                        break;

                    case 1:
                        LinkAdapter linkViewAdapter = new LinkAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ChannelMemberProfileScreen.this));
                        binding.profileRecycler.setAdapter(linkViewAdapter);
                        break;

                    case 2:
                        MediaAdapter mediaViewAdapter = new MediaAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ChannelMemberProfileScreen.this));
                        binding.profileRecycler.setAdapter(mediaViewAdapter);
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

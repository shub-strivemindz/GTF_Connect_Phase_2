package com.gtfconnect.ui.screenUI.userProfileModule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityUserProfileBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.models.LoginResponseModel;
import com.gtfconnect.models.ProfileResponseModel;
import com.gtfconnect.ui.screenUI.HomeScreen;
import com.gtfconnect.ui.screenUI.authModule.LoginScreen;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.AuthViewModel;

import java.lang.reflect.Type;

public class UserProfileScreen extends AppCompatActivity{

    ActivityUserProfileBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

    private void setProfileData()
    {
        String name = PreferenceConnector.readString(this,PreferenceConnector.FIRST_NAME,"")+" "+PreferenceConnector.readString(this,PreferenceConnector.LAST_NAME,"");
        binding.profileTitle.setText(name);
        binding.username.setText(PreferenceConnector.readString(this,PreferenceConnector.EMAIL_ID,""));

    }


    @Override
    protected void onResume() {
        super.onResume();
        setProfileData();
    }
}

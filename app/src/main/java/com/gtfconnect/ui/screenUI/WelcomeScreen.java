package com.gtfconnect.ui.screenUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;


import com.gtfconnect.databinding.ActivityWelcomeScreenBinding;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.ui.screenUI.authModule.LoginScreen;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.PreferenceConnector;

public class WelcomeScreen extends AppCompatActivity {
    ActivityWelcomeScreenBinding activityWelcomeScreenBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWelcomeScreenBinding= ActivityWelcomeScreenBinding.inflate(getLayoutInflater());

        initUI();

        setContentView(activityWelcomeScreenBinding.getRoot());

        PreferenceConnector.writeBoolean(this,PreferenceConnector.IS_EXCLUSIVE_REFRESHED,false);


        new Handler().postDelayed(() -> {

            if (PreferenceConnector.readBoolean(WelcomeScreen.this,PreferenceConnector.IS_USER_LOGGED,false))
                startActivity(new Intent(WelcomeScreen.this, HomeScreen.class));
            else
                startActivity(new Intent(WelcomeScreen.this, LoginScreen.class));
            finish();
        },1500);
    }



    private void initUI(){

        DatabaseViewModel databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);;
        databaseViewModel.getUserProfileData().observe(this, userProfileDbEntity -> {

            if (userProfileDbEntity != null && userProfileDbEntity.getUserSetting() != null && !userProfileDbEntity.getUserSetting().isEmpty()){

                for (int i=0;i< userProfileDbEntity.getUserSetting().size();i++){

                    if (userProfileDbEntity.getUserSetting().get(i).getSettingID() != null && userProfileDbEntity.getUserSetting().get(i).getSettingValue() != null) {
                         if (userProfileDbEntity.getUserSetting().get(i).getSettingID() == Constants.DARK_MODE_SETTING_ID) {

                            if (userProfileDbEntity.getUserSetting().get(i).getSettingValue().equalsIgnoreCase("1")){
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            }
                            else{
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            }
                        }
                         else{
                             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                         }
                    }
                    else{
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                }
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }
}
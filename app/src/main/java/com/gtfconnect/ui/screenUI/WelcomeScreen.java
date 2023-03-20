package com.gtfconnect.ui.screenUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;


import com.gtfconnect.databinding.ActivityWelcomeScreenBinding;
import com.gtfconnect.ui.screenUI.authModule.LoginScreen;
import com.gtfconnect.utilities.PreferenceConnector;

public class WelcomeScreen extends AppCompatActivity {
    ActivityWelcomeScreenBinding activityWelcomeScreenBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWelcomeScreenBinding= ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(activityWelcomeScreenBinding.getRoot());

        activityWelcomeScreenBinding.startButton.setOnClickListener(view -> {

            if (PreferenceConnector.readBoolean(this,PreferenceConnector.IS_USER_LOGGED,false))
                startActivity(new Intent(WelcomeScreen.this, HomeScreen.class));
            else
                startActivity(new Intent(WelcomeScreen.this, LoginScreen.class));
            finish();
        });

    }
}
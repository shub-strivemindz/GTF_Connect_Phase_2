/*
package com.gtfconnect.ui.screenUI.authModule.registerModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gtfconnect.R;
import com.gtfconnect.databinding.ActivityHowDidYouFindUsBinding;

import java.util.ArrayList;
import java.util.List;

public class HowDidYouFindUs extends AppCompatActivity {

    ActivityHowDidYouFindUsBinding binding;

    private final int SELECTED = 0;
    private final int UNSELECTED = 1;

    private boolean isFirstTime = true;

    private View layout;
    private ImageView icon;
    private TextView title;



    boolean youtubeToggle = false,instagramToggle= false,facebookToggle= false,friendsToggle= false,googleToggle= false,quoraToggle= false,otherToggle= false,telegramToggle=false;
    boolean anythingToggled = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHowDidYouFindUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.closeFindUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HowDidYouFindUs.this,RegisterScreen3.class));
            }
        });


        binding.telegram.setOnClickListener(view -> selectionCheck(binding.telegram,binding.telegramIcon,binding.telegramTitle));


        binding.facebook.setOnClickListener(view -> selectionCheck(binding.facebook,binding.facebookIcon,binding.facebookTitle));



        binding.youtube.setOnClickListener(view -> selectionCheck(binding.youtube,binding.youtubeIcon,binding.youtubeTitle));


        binding.instagram.setOnClickListener(view -> selectionCheck(binding.instagram,binding.instagramIcon,binding.instagramTitle));


        binding.friends.setOnClickListener(view -> selectionCheck(binding.friends,binding.friendsIcon,binding.friendsTitle));


        binding.google.setOnClickListener(view -> selectionCheck(binding.google,binding.googleIcon,binding.googleTitle));


        binding.quora.setOnClickListener(view -> selectionCheck(binding.quora,binding.quoraIcon,binding.quoraTitle));


        binding.others.setOnClickListener(view -> selectionCheck(binding.others,binding.othersIcon,binding.othersTitle));
    }


    private void selectionCheck(View view, ImageView imageView, TextView textView)
    {
                view.setBackgroundColor(getColor(R.color.theme_green));
                imageView.setColorFilter(getColor(R.color.white));
                textView.setTextColor(getColor(R.color.white));


                if (!isFirstTime)
                {
                    layout.setBackgroundColor(getResources().getColor(R.color.background));
                    icon.setColorFilter(getResources().getColor(R.color.theme_green));
                    title.setTextColor(getResources().getColor(R.color.tab_grey));
                }

                layout = view;
                icon = imageView;
                title = textView;
                isFirstTime = false;

    }

}
*/

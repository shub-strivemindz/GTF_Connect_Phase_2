package com.gtfconnect.ui.screenUI.authModule;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gtfconnect.databinding.ActivityTermsConditionsBinding;
import com.gtfconnect.utilities.Constants;

public class TermsConditionsScreen extends AppCompatActivity {

    ActivityTermsConditionsBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTermsConditionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String action_bar_header = getIntent().getStringExtra("key_header");

        if (action_bar_header != null){
            if (action_bar_header.equalsIgnoreCase(Constants.CMS_TYPE_TERMS)){
                binding.toolbarText.setText("Terms & Conditions");
            }
            if (action_bar_header.equalsIgnoreCase(Constants.CMS_TYPE_POLICIES)){
                binding.toolbarText.setText("Privacy Policies");
            }
        }

        String value = getIntent().getStringExtra("data");
        Log.d("terms_and_policies",value);
        /*if (value.contains("\n"))
            value =value.replace("\n","");*/

        value = value.replace("[\n]"," ");
        binding.data.setText(Html.fromHtml(value));
    }
}

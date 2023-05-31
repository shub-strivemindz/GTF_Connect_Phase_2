
package com.gtfconnect.ui.screenUI.authModule.registerModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gtfconnect.R;
import com.gtfconnect.databinding.ActivityHowDidYouFindUsBinding;
import com.gtfconnect.models.authResponseModels.LocationPickerModel;
import com.gtfconnect.ui.adapters.authModuleAdapter.HowDidYouFindUsAdapter;
import com.gtfconnect.ui.adapters.authModuleAdapter.LocationPickerAdapter;
import com.gtfconnect.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class HowDidYouFindUs extends AppCompatActivity {

    ActivityHowDidYouFindUsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHowDidYouFindUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        HowDidYouFindUsAdapter howDidYouFindUsAdapter = new HowDidYouFindUsAdapter(this);
        binding.findUsRecycler.setHasFixedSize(true);
        binding.findUsRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.findUsRecycler.setAdapter(howDidYouFindUsAdapter);



        binding.closeFindUs.setOnClickListener(view -> {

            String pickedValue = howDidYouFindUsAdapter.getSelectedValue();
            boolean isOtherSelected = false;


            if (pickedValue != null && !pickedValue.trim().isEmpty()) {

                if (pickedValue.equalsIgnoreCase("Others")){
                    isOtherSelected = true;
                }

                Intent intent = new Intent();
                intent.putExtra("isOtherSelected",isOtherSelected);
                intent.putExtra("find_text", pickedValue);
                setResult(Constants.HOW_DID_YOU_FIND_US, intent);
                finish();
            }
            else{
                Intent intent = new Intent();
                intent.putExtra("isOtherSelected", false);
                intent.putExtra("find_text", "");
                setResult(Constants.HOW_DID_YOU_FIND_US, intent);
                finish();
            }
        });
    }

}

/*
package com.gtfconnect.ui.screenUI.authModule.registerModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gtfconnect.databinding.ActivityRegister2Binding;
import com.gtfconnect.utilities.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class RegisterScreen2 extends AppCompatActivity {

    ActivityRegister2Binding binding;

    Map<String, Object> registrationData = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegister2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registrationData= (Map<String, Object>) getIntent().getSerializableExtra("MapData");


        binding.navigateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationCheck();
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(RegisterScreen2.this, RegisterScreen1.class));
                onBackPressed();
            }
        });


        */
/*binding.howDidFindUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterScreen2.this,HowDidYouFindUs.class));
            }
        });*//*

    }



    private void validationCheck() {
        String gstNumber = binding.gstNumber.getText().toString().trim();
        String companyName = binding.companyName.getText().toString().trim();
        String additionalInfo = binding.additionalInfo.getText().toString().trim();
        //String howDidYouFindUs = binding..getText().toString().trim();

        //Todo: Add the condition check ......

        */
/*if (howDidYouFindUs.isEmpty())
        {

        } else {


            Map<String, Object> registrationData = new HashMap<>();
            registrationData.put("GstNo", gstNumber);
            registrationData.put("AdditionalInfo", additionalInfo);
            registrationData.put("CompanyName", companyName);


            registrationData.put("find_us", "telegram");



            Intent intent = new Intent(RegisterScreen1.this, RegisterScreen2.class);
            intent.putExtra("MapData", (Serializable) registrationData);
            startActivity(intent);
        }*//*



        registrationData.put("GstNo", gstNumber);
        registrationData.put("AdditionalInfo", additionalInfo);
        registrationData.put("CompanyName", companyName);


        //Todo: Replace the hard coded string
        registrationData.put("find_us", "telegram");



        Intent intent = new Intent(RegisterScreen2.this, RegisterScreen3.class);
        intent.putExtra("MapData", (Serializable) registrationData);
        startActivity(intent);
    }
}
*/

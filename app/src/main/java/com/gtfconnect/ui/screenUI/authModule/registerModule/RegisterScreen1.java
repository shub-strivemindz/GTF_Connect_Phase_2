package com.gtfconnect.ui.screenUI.authModule.registerModule;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.gtfconnect.R;
import com.gtfconnect.databinding.ActivityRegister1Binding;
import com.gtfconnect.ui.screenUI.HomeScreen;
import com.gtfconnect.ui.screenUI.authModule.LoginScreen;
import com.gtfconnect.ui.screenUI.userProfileModule.UpdateUserInfoScreen;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class RegisterScreen1 extends AppCompatActivity {

    ActivityRegister1Binding binding;

    MaterialStyledDatePickerDialog.OnDateSetListener date_picker;
    String date_of_birth = "",gender = "";

    String how_did_you_find_us = "";

    private boolean is_find_us_others = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegister1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navigateNext.setOnClickListener(view -> validationCheck());

        binding.back.setOnClickListener(view -> finish());


        binding.howDidFindUs.setOnClickListener(view -> startForActivityResultLauncher.launch(new Intent(RegisterScreen1.this, HowDidYouFindUs.class)));





        binding.openDatePicker.setOnClickListener(view -> {

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    view.getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth, date_picker, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();
        });
        date_picker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;

                if (month<10) {
                    if (dayOfMonth<10) {
                        date_of_birth = year + "-0" + month + "-0" + dayOfMonth;
                    }
                    else {
                        date_of_birth = year + "-0" + month + "-" + dayOfMonth;
                    }
                }
                else {
                    if (dayOfMonth<10) {
                        date_of_birth = year + "-" + month + "-0" + dayOfMonth;
                    }
                    else {
                        date_of_birth = year + "-" + month + "-" + dayOfMonth;
                    }
                }
                binding.dob.setText(date_of_birth);
            }
        };

        binding.genderSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog gender_selection_dialog = new BottomSheetDialog(RegisterScreen1.this);
                gender_selection_dialog.setContentView(R.layout.bottomsheet_choose_gender);

                RadioGroup genderGroup = gender_selection_dialog.findViewById(R.id.gender_radio_group);
                RadioButton maleCheck = gender_selection_dialog.findViewById(R.id.male);
                RadioButton femaleCheck = gender_selection_dialog.findViewById(R.id.female);
                RadioButton otherCheck = gender_selection_dialog.findViewById(R.id.others);

                if (gender != null && !gender.isEmpty()){
                    switch (gender)
                    {
                        case "Female":
                            femaleCheck.setChecked(true);
                            break;
                        case "Others":
                            otherCheck.setChecked(true);
                            break;
                        default:
                            maleCheck.setChecked(true);
                    }
                }

                gender_selection_dialog.show();

                genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        int selectedPosition = genderGroup.getCheckedRadioButtonId();
                        RadioButton radioSexButton=(RadioButton) gender_selection_dialog.findViewById(selectedPosition);

                        gender = radioSexButton.getText().toString().trim();

                        binding.gender.setText(gender);
                        switch (gender) {
                            case "Male":
                                binding.genderIcon.setImageResource(R.drawable.male);
                                break;
                            case "Female":
                                binding.genderIcon.setImageResource(R.drawable.female);
                                break;
                            case "Others":
                                binding.genderIcon.setImageResource(R.drawable.others);
                                break;
                            default:
                                binding.genderIcon.setImageResource(R.drawable.male);
                        }
                        gender_selection_dialog.dismiss();
                    }
                });

            }
        });
    }


    private void validationCheck()
    {
        String firstName = binding.firstName.getText().toString().trim();
        String lastName = binding.lastName.getText().toString().trim();
        String emailId = binding.emailId.getText().toString().trim();
        String number = binding.number.getText().toString().trim();

        if(firstName.isEmpty()) {

            Utils.showSnackMessage(this, binding.firstName, "please enter FirstName..");
            binding.firstName.requestFocus();
        }

        else if (lastName.isEmpty()) {
            Utils.showSnackMessage(this, binding.lastName, "please enter LastName");
            binding.lastName.requestFocus();
        }

        else if (!Utils.checkEmail_validation(emailId)) {
            Utils.showSnackMessage(this, binding.emailId, "Please enter valid email");
            binding.emailId.requestFocus();
        }

        else if (gender.isEmpty())
        {
            Utils.showSnackMessage(this, binding.gender, "Please choose valid gender");
            binding.gender.requestFocus();
        }

        else if (date_of_birth.isEmpty() )
        {
            Utils.showSnackMessage(this, binding.dob, "Please choose valid date of birth");
            binding.dob.requestFocus();
        }

        else if((binding.postalCode.getSelectedCountryCodeWithPlus().equals("+91") && number.length()!=10))
        {
            Utils.showSnackMessage(this, binding.number, "Enter valid contact number!");
            binding.number.requestFocus();
        } else if (!binding.postalCode.getSelectedCountryCodeWithPlus().equals("+91") && number.length() < 6) {

            Utils.showSnackMessage(this, binding.number, "Enter valid contact number!");
            binding.number.requestFocus();
        } else if(!binding.postalCode.getSelectedCountryCodeWithPlus().equals("+91") && number.length()<5)
        {
            Utils.showSnackMessage(this, binding.number, "Mobile Number can't be less than 5 digits");
            binding.number.requestFocus();
        } else if (how_did_you_find_us.trim().isEmpty()) {

            Utils.showSnackMessage(this,binding.howDidFindUs,"Select valid how did you find us!");
        } else {


            Map<String, Object> registrationData = new HashMap<>();
            registrationData.put("FirstName", firstName);
            registrationData.put("LastName", lastName);
            registrationData.put("Email", emailId);
            registrationData.put("DOB", date_of_birth);
            registrationData.put("Phone", number);
            registrationData.put("Gender", gender);
            registrationData.put("PhoneCode", binding.postalCode.getSelectedCountryCodeWithPlus());
            registrationData.put("find_us",how_did_you_find_us);
            if (is_find_us_others){
                registrationData.put("find_us_other_text",binding.howDidFindUsOtherText.getText().toString().trim());
            }

            PreferenceConnector.writeString(this,PreferenceConnector.REGISTRATION_ONE_TIME_EMAIL,emailId);


            Intent intent = new Intent(RegisterScreen1.this, RegisterScreen3.class);
            intent.putExtra("MapData",(Serializable) registrationData);
            startActivity(intent);
        }
    }





    ActivityResultLauncher<Intent> startForActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Constants.HOW_DID_YOU_FIND_US) {

                    if (result.getData() != null) {

                        is_find_us_others = result.getData().getBooleanExtra("isOtherSelected",false);

                        if (is_find_us_others){
                            binding.howDidFindUsOther.setVisibility(View.VISIBLE);
                        }
                        else{
                            binding.howDidFindUsOther.setVisibility(View.GONE);
                        }

                        how_did_you_find_us = result.getData().getStringExtra("find_text");
                        binding.howDidFindUsText.setText(how_did_you_find_us);
                    }
                }
            });

}

package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.audiowaveform.WaveformSeekBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityPinnedMessageBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.PinnedMessageListener;
import com.gtfconnect.models.PinnedMessagesModel;
import com.gtfconnect.ui.adapters.commonGroupChannelAdapters.GroupChannelPinnedMessageAdapter;
import com.gtfconnect.ui.screenUI.channelModule.ChannelChatsScreen;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.ChatViewModel;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PinnedMessageScreen extends AppCompatActivity implements ApiResponseListener, PinnedMessageListener {

    ActivityPinnedMessageBinding binding;

    private int DELETE_PINNED_MESSAGE = 1;

    private int DELETE_ALL_PINNED_MESSAGE = 2;

    private int PINNED_MESSAGE_DATA = 3;

    private int requestType;

    private Rest rest;

    private ApiResponseListener listener;
    private ChatViewModel chatViewModel;

    private PinnedMessagesModel pinnedMessagesModel;

    private GroupChannelPinnedMessageAdapter pinnedMessageViewAdapter;

    private String postBaseUrl = "";

    private String profileBaseUrl = "";

    Integer userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPinnedMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = PreferenceConnector.readInteger(this,PreferenceConnector.CONNECT_USER_ID,0);

        postBaseUrl = getIntent().getStringExtra("post_base_url");
        profileBaseUrl = getIntent().getStringExtra("profile_base_url");

        init();

        binding.backClick.setOnClickListener(view -> onBackPressed());

        binding.removeAll.setOnClickListener(view -> {
            deleteAllMessage();
        });
    }


    public void init()
    {
        // Getting API data fetch
        //auth_rest = new Rest(this);            // for authentication ------------
        rest = new Rest(this,false,false);                              // for other apis ----------------
        listener = this;
        //appDao = AppDatabase.getInstance(getApplication()).appDao();
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("API Call Listener ----", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse,rest);
                }

            }
        });

        fetchData();
    }

    private void fetchData()
    {
        requestType = PINNED_MESSAGE_DATA;

        Map<String,Object> param = new HashMap<>();
        param.put("GroupChannelID", PreferenceConnector.readString(this,PreferenceConnector.GC_CHANNEL_ID,"0"));
        param.put("UserID",PreferenceConnector.readInteger(this,PreferenceConnector.CONNECT_USER_ID,0));

        chatViewModel.getPinnedMessages(param);
    }


    private void deleteAllMessage()
    {
        requestType = DELETE_ALL_PINNED_MESSAGE;

        Map<String,Object> param = new HashMap<>();
        param.put("GroupChannelID", PreferenceConnector.readString(this,PreferenceConnector.GC_CHANNEL_ID,""));
        param.put("UserID",PreferenceConnector.readInteger(this,PreferenceConnector.CONNECT_USER_ID,0));

        chatViewModel.removeAllPinnedMessage(param);
    }

    private void showBottomSheetPopUp(String message)
    {
        Dialog pin_dialog = new Dialog(this);
        pin_dialog.setContentView(R.layout.dialog_pinned_message);
        pin_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pin_dialog.setCancelable(false);
        pin_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView text = pin_dialog.findViewById(R.id.title);
        text.setText(message);

        pin_dialog.show();
        new Handler().postDelayed(pin_dialog::dismiss,2000);
    }


    private void loadDataToAdapter()
    {
        binding.messageCount.setText(String.valueOf(pinnedMessagesModel.getData().getAllPinData().size()));

// Loading pinned message data ------
        pinnedMessageViewAdapter= new GroupChannelPinnedMessageAdapter(this,pinnedMessagesModel.getData().getAllPinData(),this,postBaseUrl,profileBaseUrl);
        binding.pinnedMessageRecycler.setHasFixedSize(true);
        binding.pinnedMessageRecycler.setLayoutManager(new LinearLayoutManager(PinnedMessageScreen.this));
        binding.pinnedMessageRecycler.setAdapter(pinnedMessageViewAdapter);
    }

    @Override
    public void deleteSinglePinMessage(int pinnedMessageId) {
        requestType = DELETE_PINNED_MESSAGE;

        Map<String,Object> params = new HashMap<>();
        params.put("pinmessagesID",pinnedMessageId);
        params.put("UserID",userId);

        chatViewModel.removePinnedMessage(params);
    }

    @Override
    public void gotoMessage(String groupChatId) {
        Intent intent = new Intent();
        intent.putExtra("groupChatId",groupChatId);
        setResult(Constants.SEARCH_PINNED_MESSAGE,intent);
        finish();
    }

    @Override
    public void downloadAudio(String audioPostUrl, String groupChannelID, String groupChatID, WaveformSeekBar seekBar, LottieAnimationView progressBar, ImageView downloadPlayPic) {

    }

    @Override
    public void playAudio(String audioPostUrl, WaveformSeekBar seekBar, long duration) {

    }

    @Override
    public void viewMemberProfile(int userID, int gcMemberId, int groupChatId, int groupChannelId) {

    }


    @Override
    public void onLoading() {
        rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        Log.d("Pinned Message ","data render "+jsonObject.toString());
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        Log.d("Pinned Message ","data render "+jsonObject.toString());
    }

    @Override
    public void onAuthFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServerFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onForbidden(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLaunchFailure(JsonObject jsonObject) {
        Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOtherFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    private void renderResponse(JsonObject jsonObject)
    {

        if (requestType == PINNED_MESSAGE_DATA)
        {
            Gson gson = new Gson();
            pinnedMessagesModel = new PinnedMessagesModel();
            Type type = new TypeToken<PinnedMessagesModel>() {
            }.getType();

            pinnedMessagesModel = gson.fromJson(jsonObject, type);
            if(pinnedMessagesModel.getData()!=null)
            {
                loadDataToAdapter();
            }
            else {
                Utils.showSnackMessage(this,binding.getRoot(),"No Pinned Message Found");
            }
        }
        else if(requestType == DELETE_PINNED_MESSAGE)
        {
            showBottomSheetPopUp(jsonObject.get("message").toString());
            fetchData();
        }
        else if (requestType == DELETE_ALL_PINNED_MESSAGE) {
            showBottomSheetPopUp(jsonObject.get("message").toString());
            fetchData();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

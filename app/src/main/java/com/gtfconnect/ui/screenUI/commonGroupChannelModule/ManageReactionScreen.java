package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityManageReactionsBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.models.channelResponseModel.ChannelManageReactionModel;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.ManageReactionsListAdapter;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageReactionScreen extends AppCompatActivity implements ApiResponseListener{

    ActivityManageReactionsBinding binding;

    ChannelManageReactionModel reactionModel;

    private final int GC_REFRESH_UPDATED_REACTION_CODE = 1002;

    private final int GET_GC_REACTION_LIST = 100;

    private final int UPDATE_GC_REACTION_STATUS = 101;
    private final int UPDATE_GC_REACTION_LIST = 102;

    private int requestType = 0;

    private Rest rest;
    private ApiResponseListener listener;
    private ConnectViewModel connectViewModel;

    private ManageReactionsListAdapter adapter;

    private int channelID;

    private String api_token;

    private InfoDbEntity detailModel;

    private boolean isScrolling = false;

    private boolean isDataLoadedFirstTime = true;

    private boolean hasMore = true;
    private int currentPage = 0;

    Parcelable recyclerViewState;

    private int totalItem;

    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageReactionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reactionModel = new ChannelManageReactionModel();

        binding.back.setOnClickListener(view -> onBackPressed());

        channelID = Integer.parseInt(PreferenceConnector.readString(this, PreferenceConnector.GC_CHANNEL_ID, ""));
        api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");


        detailModel = new InfoDbEntity();
        Gson gson = new Gson();
        String responseData = getIntent().getStringExtra("data");
        detailModel = gson.fromJson(responseData, InfoDbEntity.class);


        init();

        binding.reactionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    binding.reactionSwitchText.setText("On");
                }
                else {
                    binding.reactionSwitchText.setText("Off");
                    Map<String, Object> params = new HashMap<>();
                    params.put("EnableReactions",0);
                    requestType = UPDATE_GC_REACTION_STATUS;
                    connectViewModel.update_groupChannel_settings(channelID,api_token,params);
                }
            }
        });


        binding.updateReaction.setOnClickListener(view -> {
            requestType = UPDATE_GC_REACTION_LIST;

            ArrayList<Integer> data = adapter.getReactionList();

            Map<String,Object> params = new HashMap<>();

            if (data != null && !data.isEmpty()){


                for (int i=0;i<data.size();i++){
                    params.put("ReactionID["+i+"]",data.get(i));
                }

                Log.d("params :",params.toString());
                requestType = UPDATE_GC_REACTION_LIST;
                connectViewModel.update_group_channel_reaction_list(channelID,api_token,params);
            }
            else{
                Utils.showSnackMessage(ManageReactionScreen.this,binding.getRoot(),"Please select at least one reaction to update!");
            }
        });



        binding.reactionsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                recyclerViewState = binding.reactionsRecycler.getLayoutManager().onSaveInstanceState(); // save recycleView state
                totalItem = mLayoutManager.getItemCount();
                if (isScrolling && mLayoutManager.findLastCompletelyVisibleItemPosition() == totalItem - 1 && dy > 0 && hasMore) {

                    isScrolling = false;
                    currentPage++;

                    getReactionFromList();

                }
            }

        });
    }





    private void init() {

        rest = new Rest(this,false,false);

        rest = new Rest(this,true,false);
        listener = this;

        connectViewModel = new ViewModelProvider(this).get(ConnectViewModel.class);
        connectViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("Profile Listener Called ---", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse, rest);
                }
            }
        });

        currentPage = 1;
        getReactionFromList();
    }




    private void getReactionFromList(){
        requestType = GET_GC_REACTION_LIST;
        connectViewModel.get_group_channel_manage_reaction_list(channelID,api_token,currentPage,25,0);
    }



    @Override
    public void onLoading() {
        rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
       /* Toast.makeText(this, "Permissions Updated", Toast.LENGTH_SHORT).show();
        setResult(GC_PERMISSION_UPDATED_CODE,new Intent());
        finish();
        Log.d("json response : ",jsonObject.toString());*/
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        /*Toast.makeText(this, "Permissions Updated", Toast.LENGTH_SHORT).show();
        setResult(GC_PERMISSION_UPDATED_CODE,new Intent());
        finish();
        Log.d("json response : ",jsonObject.toString());*/

    }

    @Override
    public void onAuthFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(this, LoginScreen.class));
        finishAffinity();
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


    private void renderResponse(JsonObject jsonObject){

        if (requestType == GET_GC_REACTION_LIST) {
            Gson gson = new Gson();
            Type type = new TypeToken<ChannelManageReactionModel>() {
            }.getType();


            ChannelManageReactionModel channelManageReactionModel = gson.fromJson(jsonObject, type);
            if (isDataLoadedFirstTime){
                reactionModel = channelManageReactionModel;

                mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

                adapter = new ManageReactionsListAdapter(this, reactionModel);
                binding.reactionsRecycler.setHasFixedSize(true);
                binding.reactionsRecycler.setLayoutManager(mLayoutManager);
                binding.reactionsRecycler.setAdapter(adapter);

                binding.reactionsRecycler.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                isDataLoadedFirstTime = false;
            }
            else {
                reactionModel.getData().getList().addAll(channelManageReactionModel.getData().getList());
                adapter.updateReactionList(reactionModel);
            }


            if (channelManageReactionModel.getData().getHasMore()){
                hasMore = true;
            }
            else{
                hasMore = false;
            }

        }
        else if (requestType == UPDATE_GC_REACTION_STATUS) {
            setResult(GC_REFRESH_UPDATED_REACTION_CODE,new Intent());
            finish();
        }
        else if (requestType == UPDATE_GC_REACTION_LIST) {
            Utils.showSnackMessage(this,binding.getRoot(),"List Updated Successfully!");
        }
    }
}

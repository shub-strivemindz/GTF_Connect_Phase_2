package com.gtfconnect.ui.fragments;

import static com.gtfconnect.services.SocketService.socketInstance;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.FragmentGroupViewBinding;
import com.gtfconnect.databinding.FragmentRecentViewBinding;
import com.gtfconnect.models.groupDashboardModels.GroupResponseModel;
import com.gtfconnect.roomDB.AppDao;
import com.gtfconnect.roomDB.AppDatabase;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.ui.adapters.ExclusiveOfferAdapter;
import com.gtfconnect.ui.adapters.GroupViewAdapter;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.lang.reflect.Type;

import io.socket.client.Ack;

public class RecentFragment extends Fragment {

    private FragmentRecentViewBinding binding;

    private GroupResponseModel responseModel;

    private JSONObject jsonRawObject;
    private int userId;

    Rest rest;

    private boolean isSearchClicked = false;
    private AppDao appDao;

    private DatabaseViewModel databaseViewModel;

    private int localDBDataSize = 0;




    public RecentFragment() {}


    public static RecentFragment newInstance() {
        RecentFragment fragment = new RecentFragment();

        /*Bundle args = new Bundle();
        args.putInt(ARG_COUNT, regionCount);
        fragment.setArguments(args);*/
        return fragment;
    }


  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        *//*if (getArguments() != null) {
            viewCount = getArguments().getInt(ARG_COUNT);
        }*//*
    }
*/









    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecentViewBinding.inflate(inflater, container, false);
        rest = new Rest(getContext(),false,false);


        init();

        binding.searchView.setVisibility(View.GONE);


        binding.searchIcon.setOnClickListener(view -> {
            binding.searchView.setVisibility(View.VISIBLE);
            binding.container.setVisibility(View.GONE);
            isSearchClicked = true;
        });

        binding.searchClose.setOnClickListener(view -> {
            binding.searchView.setVisibility(View.GONE);
            binding.container.setVisibility(View.VISIBLE);
            isSearchClicked = false;
        });


        ExclusiveOfferAdapter exclusiveOfferAdapter= new ExclusiveOfferAdapter(getActivity());
        binding.exclusiveViewList.setHasFixedSize(true);
        binding.exclusiveViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.exclusiveViewList.setAdapter(exclusiveOfferAdapter);


        // Todo : un comment when implemented all 4 tabs socket
        userId = PreferenceConnector.readInteger(requireContext(),PreferenceConnector.GTF_USER_ID,0);
        Log.d("USER ID ",String.valueOf(userId));

        messageReceived();

        return binding.getRoot();
    }



    private void init()
    {
        appDao = AppDatabase.getInstance(requireActivity().getApplication()).appDao();
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        loadLocalData();

    }


    private void loadLocalData()
    {
        databaseViewModel.getGroups().observe(requireActivity(), groupResponseModel -> {
            if (groupResponseModel != null && !groupResponseModel.isEmpty()) {


                localDBDataSize = groupResponseModel.size();

                responseModel = new GroupResponseModel();
                responseModel.setData(groupResponseModel);

                loadDataToAdapter();

                updateGroupDashboardSocket();
            } else {
                updateGroupDashboardSocket();
            }
        });
    }





    private void updateGroupDashboardSocket()
    {
        Gson gson = new Gson();
        Type type;

        type = new TypeToken<GroupResponseModel>() {
        }.getType();

        responseModel = new GroupResponseModel();


        try {
            jsonRawObject = new JSONObject();
            jsonRawObject.put("userId", userId);
            jsonRawObject.put("filter","group");

            Log.d("AUTH USER CREDENTIALS -----",jsonRawObject.toString());

            //getActivity().runOnUiThread(() -> rest.ShowDialogue());
            socketInstance.emit("auth-user", jsonRawObject, (Ack) args -> {

                //getActivity().runOnUiThread(() -> rest.dismissProgressdialog());

                JSONObject responseData = (JSONObject) args[0];
                Log.d("Group Data ----", responseData.toString());

                try {
                    if (responseData.getString("success").equalsIgnoreCase("false")) {
                        getActivity().runOnUiThread(() -> {
                            // rest.dismissProgressdialog();
                            Toast.makeText(getContext(), "Socket Connection Refused", Toast.LENGTH_SHORT).show();
                        });
                    }
                    else {

                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(responseData.toString());

                        responseModel = gson.fromJson(gsonObject, type);

                        /*if (responseModel.getData() != null)
                            unreadMessageListener.getUnreadCount(responseModel.getData().size());
                        else
                            unreadMessageListener.getUnreadCount(0);*/

                        if (responseData == null) {
                            Utils.showSnackMessage(getContext(), binding.getRoot(), "No Data Found");
                            Log.d("authenticateUserAndFetchData -- ", "Error");
                        } else {
                            Log.d("authenticateUserAndFetchData -- ", String.valueOf(responseData));

                            getActivity().runOnUiThread(() -> {
                                if (responseModel.getData() != null) {
                                    if (localDBDataSize != responseModel.getData().size()) {
                                        for (int i = 0; i < responseModel.getData().size(); i++) {
                                            databaseViewModel.insertGroups(responseModel.getData().get(i));
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.d("Socket : ","Group Chat Exception ---"+e.toString());
                }
            });
        }
        catch (Exception e){
            Log.d("JsonException ---",e.toString());
        }
    }

    private void loadDataToAdapter()
    {
        GroupViewAdapter groupViewAdapter= new GroupViewAdapter(getActivity(),responseModel);
        binding.recentViewList.setHasFixedSize(true);
        binding.recentViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recentViewList.setAdapter(groupViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLocalData();
        //updateGroupDashboardSocket();
    }


    private void messageReceived() {

        socketInstance.on("messageReceived", args -> {
            //updateGroupDashboardSocket();
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        //socketInstance.off("messageReceived");
    }
}


package com.gtfconnect.ui.fragments;

import static com.gtfconnect.services.SocketService.socketInstance;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.gtfconnect.interfaces.UnreadCountHeaderListener;
import com.gtfconnect.models.channelDashboardModels.ChannelResponseModel;
import com.gtfconnect.models.groupDashboardModels.GroupResponseModel;
import com.gtfconnect.roomDB.AppDao;
import com.gtfconnect.roomDB.AppDatabase;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.ui.adapters.GroupViewAdapter;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.lang.reflect.Type;

import io.socket.client.Ack;
import io.socket.emitter.Emitter;

public class GroupFragment extends Fragment {

    private FragmentGroupViewBinding binding;

    private GroupResponseModel responseModel;

    private JSONObject jsonRawObject;
    private int userId ;

    private UnreadCountHeaderListener unreadMessageListener;
    private Rest rest;

    private AppDao appDao;

    private DatabaseViewModel databaseViewModel;

    private int localDBDataSize = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupViewBinding.inflate(inflater, container, false);


        rest = new Rest(getContext(),false,false);

        init();

        // Todo : un comment when implemented all 4 tabs socket
        userId = PreferenceConnector.readInteger(requireContext(),PreferenceConnector.GTF_USER_ID,0);
        Log.d("USER ID ",String.valueOf(userId));

        //updateGroupDashboardSocket();
        messageReceived();

        return binding.getRoot();
    }



    private void init()
    {
        appDao = AppDatabase.getInstance(requireActivity().getApplication()).appDao();
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        //loadLocalData();

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

                        if (responseModel.getData() == null)
                        {
                            Log.d("Response_model","NUll Object");
                        }

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
        binding.groupViewList.setHasFixedSize(true);
        binding.groupViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.groupViewList.setAdapter(groupViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLocalData();
        //updateGroupDashboardSocket();
    }


    private void messageReceived() {

        /*socketInstance.on("messageReceived", args -> {

            Log.d("Message_Received_on","Group Fragment");
            //updateGroupDashboardSocket();
        });*/
    }

    @Override
    public void onStop() {
        super.onStop();
        //socketInstance.off("messageReceived");
    }
}

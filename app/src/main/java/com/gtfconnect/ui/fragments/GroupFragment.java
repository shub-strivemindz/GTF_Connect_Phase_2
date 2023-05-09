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
import com.gtfconnect.roomDB.AppDao;
import com.gtfconnect.roomDB.AppDatabase;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardResponseModel;
import com.gtfconnect.ui.adapters.GroupViewAdapter;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.socket.client.Ack;
import io.socket.emitter.Emitter;

public class GroupFragment extends Fragment {

    private FragmentGroupViewBinding binding;

    private DashboardResponseModel responseModel;

    private List<DashboardListEntity> gcList;

    private JSONObject jsonRawObject;
    private int userId ;

    private UnreadCountHeaderListener unreadMessageListener;

    private AppDao appDao;

    private DatabaseViewModel databaseViewModel;

    private int localDBDataSize = 0;

    private GroupViewAdapter groupViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupViewBinding.inflate(inflater, container, false);

        init();

        // Todo : un comment when implemented all 4 tabs socket
        userId = PreferenceConnector.readInteger(requireContext(),PreferenceConnector.GTF_USER_ID,0);
        Log.d("USER ID ",String.valueOf(userId));

        //updateGroupDashboardSocket();
        messageReceived();

        responseModel = new DashboardResponseModel();

        gcList = new ArrayList<>();
        loadDataToAdapter();

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
        String response2 = PreferenceConnector.readString(getContext(),PreferenceConnector.DASHBOARD_DATA+"/group","");
        Type type2 = new TypeToken<DashboardResponseModel>(){}.getType();
        responseModel = new Gson().fromJson(response2,type2);


        if (responseModel != null && responseModel.getData() != null && responseModel.getData().getGcData() != null && !responseModel.getData().getGcData().isEmpty()){
            gcList = new ArrayList<>();
            gcList.addAll(responseModel.getData().getGcData());

            groupViewAdapter.updateList(gcList);
        }
        updateGroupDashboardSocket();
    }



    private void updateGroupDashboardSocket()
    {
        Gson gson = new Gson();
        Type type;

        type = new TypeToken<DashboardResponseModel>() {
        }.getType();

        responseModel = new DashboardResponseModel();


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

                        if (responseData == null) {
                            Utils.showSnackMessage(getContext(), binding.getRoot(), "No Data Found");
                            Log.d("authenticateUserAndFetchData -- ", "Error");
                        } else {
                            Log.d("authenticateUserAndFetchData -- ", String.valueOf(responseData));

                            getActivity().runOnUiThread(() -> {
                                if (responseModel.getData() != null && responseModel.getData().getGcData() != null) {

                                    String response = new Gson().toJson(responseModel);
                                    PreferenceConnector.writeString(getContext(),PreferenceConnector.DASHBOARD_DATA+"/"+"group",response);

                                    gcList = new ArrayList<>();
                                    gcList.addAll(responseModel.getData().getGcData());

                                    groupViewAdapter.updateList(gcList);
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
        groupViewAdapter= new GroupViewAdapter(getActivity(),gcList);
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

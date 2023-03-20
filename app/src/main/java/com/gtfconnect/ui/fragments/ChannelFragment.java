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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.FragmentChannelViewBinding;
import com.gtfconnect.models.channelResponseModel.ChannelResponseModel;
import com.gtfconnect.ui.adapters.ChannelViewAdapter;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.lang.reflect.Type;

import io.socket.client.Ack;

public class ChannelFragment extends Fragment {

    private FragmentChannelViewBinding binding;

    private ChannelResponseModel responseModel;

    private JSONObject jsonRawObject;
    private int userId;

    Rest rest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentChannelViewBinding.inflate(inflater, container, false);

        rest = new Rest(getContext(),false,false);

        userId = PreferenceConnector.readInteger(requireContext(),PreferenceConnector.GTF_USER_ID,0);

        updateChannelDashboardSocket();
        messageReceived();

        // Todo : Unset count to real data
        //unreadMessageListener.getUnreadCount(0);

        // Load Recent List Data -----
        /*ChannelViewAdapter recentViewAdapter= new ChannelViewAdapter(requireContext(),3);
        binding.channelViewList.setHasFixedSize(true);
        binding.channelViewList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.channelViewList.setAdapter(recentViewAdapter);*/

        return binding.getRoot();
    }

    private void updateChannelDashboardSocket()
    {
        Gson gson = new Gson();
        Type type;

        type = new TypeToken<ChannelResponseModel>() {
        }.getType();

        responseModel = new ChannelResponseModel();


        try {
            jsonRawObject = new JSONObject();
            jsonRawObject.put("userId", userId);
            jsonRawObject.put("filter","channel");

           // getActivity().runOnUiThread(() -> rest.ShowDialogue());

            socketInstance.emit("auth-user", jsonRawObject, (Ack) args -> {

                //getActivity().runOnUiThread(() -> rest.dismissProgressdialog());

                JSONObject responseData = (JSONObject) args[0];
                Log.d("Channel Data ----", responseData.toString());

                try {
                    if (responseData.getString("success").equalsIgnoreCase("false")) {
                        getActivity().runOnUiThread(() -> {

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

                            getActivity().runOnUiThread(() -> loadDataToAdapter());
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.d("Socket : ","Channel Chat Exception ---"+e.toString());
                }
            });
        }
        catch (Exception e){
            Log.d("JsonException ---",e.toString());
        }
    }

    private void loadDataToAdapter()
    {
        Log.d("Group Data ----"," reposne -----"+responseModel);



        ChannelViewAdapter groupViewAdapter= new ChannelViewAdapter(getActivity(),responseModel);
        binding.channelViewList.setHasFixedSize(true);
        binding.channelViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.channelViewList.setAdapter(groupViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateChannelDashboardSocket();
    }


    private void messageReceived() {

        socketInstance.on("messageReceived", args -> {
            updateChannelDashboardSocket();
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        socketInstance.off("messageReceived");
    }
}

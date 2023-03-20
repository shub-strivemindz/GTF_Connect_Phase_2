package com.gtfconnect.ui.screenUI.groupModule;

import static com.gtfconnect.services.SocketService.socketInstance;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.databinding.ActivityCommentSectionBinding;
import com.gtfconnect.interfaces.GroupCommentListener;
import com.gtfconnect.models.groupResponseModel.GroupCommentResponseModel;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;
import com.gtfconnect.ui.adapters.groupChatAdapter.GroupCommentAdapter;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import io.socket.emitter.Emitter;

public class GroupCommentScreen extends AppCompatActivity implements GroupCommentListener {

    ActivityCommentSectionBinding binding;

    GroupChatResponseModel.Row detail;

    private boolean replyOnComment = false;

    private GroupCommentResponseModel responseModel;

    private JSONObject jsonRawObject;

    GroupCommentAdapter commentViewAdapter;

    private boolean isCommentEditable = false;

    private int commentID = 0;

    private LinearLayoutManager mLayoutManager;

    String userID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentSectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        userID = String.valueOf(PreferenceConnector.readInteger(this,PreferenceConnector.CONNECT_USER_ID,0));

        commentReceiver();

        replyOnComment = getIntent().getBooleanExtra("replyOnComment",false);


        //userID = getIntent().getStringExtra("userID");

        Log.d("Getting user ID ---",userID);
        //detail = new GroupChatResponseModel.Row();

        Gson gson = new Gson();
        detail = gson.fromJson(getIntent().getStringExtra("userDetail"), GroupChatResponseModel.Row.class);

        initializeData();

        editCommentListener();
        deleteCommentListener();


        if (detail != null) {

            mLayoutManager = new LinearLayoutManager(this);

            commentViewAdapter = new GroupCommentAdapter(this, detail.getCommentData(),userID,this);
            binding.commentRecycler.setHasFixedSize(true);
            binding.commentRecycler.setLayoutManager(mLayoutManager);
            binding.commentRecycler.setAdapter(commentViewAdapter);
        }

        if (replyOnComment)
        {
            binding.msg.requestFocus();
            Utils.softKeyboard(this,true,binding.msg);

            if (detail.getCommentData() != null){
                if (detail.getCommentData().size() != 0) {

                    /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
                    //final int scrollTo = binding.commentRecycler.getChildAt(detail.getCommentData().size() - 1).getY();

                    binding.nestedScroll.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.nestedScroll.fling(0);
                            int lastIndex = detail.getCommentData().size() - 1;
                            final RecyclerView.ViewHolder viewHolder = binding.commentRecycler.findViewHolderForAdapterPosition(lastIndex);
                            float scrollTo = viewHolder.itemView.getY();
                            binding.nestedScroll.smoothScrollTo(0, (int) scrollTo);
                        }
                    });

                    //binding.commentRecycler.getLayoutManager().scrollToPosition(detail.getCommentData().size() - 1);

                }
            }
        }

        binding.sendMessage.setOnClickListener(view -> {
            sendMessage(binding.msg.getText().toString(),view);
        });

        binding.backClick.setOnClickListener(view -> onBackPressed());
    }


    private void initializeData()
    {
        Log.d("data..........",getIntent().getStringExtra("userDetail"));

        binding.firstName.setText(detail.getUser().getFirstname());
        binding.firstName.setText(detail.getUser().getLastname());

        binding.time.setText(Utils.getDisplayableTime(detail.getUser().getUpdatedAt()));
        binding.message.setText(detail.getMessage());

        if (detail.getDummyViews() == 0)
            binding.postImageContainer.setVisibility(View.GONE);

    }


    private void sendMessage(String message,View view)
    {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        int id = Integer.parseInt(userID);
        int groupChannelId = detail.getGroupChannelID();
        int gcMemberID = detail.getGCMemberID();
        Integer groupChatId = Integer.parseInt(detail.getGroupChatID());

        if (message != null){
            if (!(message.equalsIgnoreCase(""))) {



                try {

                    jsonRawObject = new JSONObject();

                    jsonRawObject.put("UserID", id);
                    jsonRawObject.put("GroupChannelID", groupChannelId);
                    jsonRawObject.put("GroupChatID", groupChatId);
                    jsonRawObject.put("GCMemberID",gcMemberID);
                    jsonRawObject.put("comment", message);


                    if (isCommentEditable)
                    {
                        jsonRawObject.put("CommentID",commentID);

                        Log.v("Send Message Params --", jsonRawObject.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                socketInstance.emit("commentEdit", jsonRawObject);
                                    isCommentEditable = false;
                            }
                        });

                    }
                    else {
                        jsonRawObject.put("GCMemberID", gcMemberID);

                        Log.v("Send Message Params --", jsonRawObject.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                socketInstance.emit("comment", jsonRawObject);
                                isCommentEditable = false;
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.d("sendMessage Exception ----", e.toString());
                }
            }
            else {
                isCommentEditable = false;
                Toast.makeText(this, "Type message!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            isCommentEditable = false;
            Toast.makeText(this, "Type message!", Toast.LENGTH_SHORT).show();
        }

        binding.msg.setText("");
    }

    private void commentReceiver()
    {
        responseModel = new GroupCommentResponseModel();

        Gson gson = new Gson();
        Type type;

        type = new TypeToken<GroupCommentResponseModel>() {
        }.getType();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.on("commentted", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {

                        JSONObject data = (JSONObject) args[0];
                        Log.d("Comment Data :",data.toString());

                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject)jsonParser.parse(data.toString());

                        responseModel = gson.fromJson(gsonObject, type);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                detail = responseModel.getData().getChatDetail();
                                commentViewAdapter= new GroupCommentAdapter(GroupCommentScreen.this,detail.getCommentData(),userID,GroupCommentScreen.this);
                                binding.commentRecycler.setHasFixedSize(true);
                                binding.commentRecycler.setLayoutManager(new LinearLayoutManager(GroupCommentScreen.this));
                                binding.commentRecycler.setAdapter(commentViewAdapter);
                            }
                        });


                        try {
                            Log.d("Latest Comment :",data.getJSONObject("data").getJSONObject("comment").toString());
                        }
                        catch (Exception e)
                        {
                            Log.d("Error fetching comment ",e.toString());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Todo: Remove when live user ID has been used

                                    GroupCommentResponseModel comment = new GroupCommentResponseModel();


                           /* if (detail.getGroupChatID() == )
                            if (userID != 3) {
                                if (data.getString("type").equalsIgnoreCase("start")) {
                                    binding.userSubscribers.setText(data.getString("Firstname"));
                                    binding.subscriberSubtitle.setText("is typing");
                                }
                                if (data.getString("type").equalsIgnoreCase("end")) {
                                    binding.userSubscribers.setText(String.valueOf(subscribers));
                                    binding.subscriberSubtitle.setText("subscribers");
                                }
                            }*/
                                }
                                catch(Exception e)
                                {
                                    Log.d("Typing header exception --",e.toString());
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void oldMessage(String oldMessage,int commentID,int position) {

        binding.msg.setText(oldMessage);
        binding.msg.requestFocus();
        this.commentID = commentID;

        isCommentEditable = true;

        Utils.softKeyboard(this,true,binding.msg);
        binding.msg.setSelection(oldMessage.length());

        //scrollToPosition(position);
    }

    private void editCommentListener()
    {

        Gson gson = new Gson();
        Type type;

        type = new TypeToken<List<GroupChatResponseModel.CommentResponseModel>>() {
        }.getType();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.on("commentEditData", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONArray response = new JSONArray();
                        JSONObject data = (JSONObject) args[0];
                        Log.d("Comment Data :",data.toString());

                        try {
                            response = data.getJSONObject("data").getJSONArray("commentList");
                        }
                        catch (Exception e)
                        {
                            Log.d("Edit Comment Listener error :",e.toString());
                        }


                        /*JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject)jsonParser.parse(response.toString());*/

                        //detail.getCommentData()

                        Log.d("Edit Comment Response",response.toString());
                        detail.setCommentData(gson.fromJson(response.toString(), type));


                        //responseModel = gson.fromJson(gsonObject, type);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //detail = responseModel.getData().getChatDetail();
                                commentViewAdapter.updateList(detail.getCommentData());
                            }
                        });

                    }
                });
            }
        });
    }


    @Override
    public void notifyDeleteComment(int commentID,int chatID,int channelID,int userID) {

        jsonRawObject = new JSONObject();

        try {
            jsonRawObject.put("CommentID",commentID);
            jsonRawObject.put("UserID",PreferenceConnector.readInteger(this,PreferenceConnector.CONNECT_USER_ID,0));
            jsonRawObject.put("GroupChatID",chatID);
            jsonRawObject.put("GroupChannelID",channelID);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socketInstance.emit("commentDelete", jsonRawObject);
                }
            });
        }
        catch (Exception e)
        {
            Log.d("Delete Comment Exception ---",e.toString());
        }

    }


    private void deleteCommentListener()
    {

        Gson gson = new Gson();
        Type type;

        type = new TypeToken<List<GroupChatResponseModel.CommentResponseModel>>() {
        }.getType();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.on("commentDeleteData", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONArray response = new JSONArray();
                        JSONObject data = (JSONObject) args[0];
                        Log.d("Comment Data :","after delete --"+data.toString());

                        try {
                            response = data.getJSONObject("data").getJSONArray("commentList");
                        }
                        catch (Exception e)
                        {
                            Log.d("Edit Comment Listener error :",e.toString());
                        }


                        detail.setCommentData(gson.fromJson(response.toString(), type));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //detail = responseModel.getData().getChatDetail();
                                commentViewAdapter.updateList(detail.getCommentData());
                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroyListeners();
        finish();

    }


    private void destroyListeners()
    {
        // Todo : Remove all ON listeners of socket

            socketInstance.off("commentEditData");
            socketInstance.off("commentDeleteData");
            socketInstance.off("commentted");

    }
}

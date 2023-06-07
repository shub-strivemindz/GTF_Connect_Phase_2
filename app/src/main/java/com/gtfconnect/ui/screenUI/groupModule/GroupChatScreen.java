package com.gtfconnect.ui.screenUI.groupModule;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.gtfconnect.services.SocketService.socketInstance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.audiorecorder.OnRecordListener;
import com.example.audiowaveform.WaveformSeekBar;
import com.example.flyingreactionanim.Directions;
import com.example.flyingreactionanim.ZeroGravityAnimation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gowtham.library.utils.TrimType;
import com.gowtham.library.utils.TrimVideo;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityGroupChatBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.AttachmentUploadListener;
import com.gtfconnect.interfaces.GroupChatListener;
import com.gtfconnect.interfaces.ImagePreviewListener;
import com.gtfconnect.interfaces.SelectEmoteReaction;
import com.gtfconnect.interfaces.UpdateGroupDummyUserListener;
import com.gtfconnect.models.EmojiListModel;
import com.gtfconnect.models.ImagePreviewModel;
import com.gtfconnect.models.PinnedMessagesModel;
import com.gtfconnect.models.SendAttachmentResponseModel;
import com.gtfconnect.models.channelResponseModel.ChannelCommentResponseModel;
import com.gtfconnect.models.channelResponseModel.ChannelManageReactionModel;
import com.gtfconnect.models.channelResponseModel.ChannelMessageReceivedModel;
import com.gtfconnect.models.channelResponseModel.ChannelReactionReceivedModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatResponseModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.models.commonGroupChannelResponseModels.GroupChannelInfoResponseModel;
import com.gtfconnect.models.groupResponseModel.GetDummyUserModel;
import com.gtfconnect.models.commonGroupChannelResponseModels.commentResponseModels.CommentReceiveResponseModel;
import com.gtfconnect.models.groupResponseModel.GroupMessageReceivedModel;
import com.gtfconnect.models.groupResponseModel.PostDeleteModel;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.roomDB.dbEntities.UserProfileDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.ui.adapters.ForwardPersonListAdapter;
import com.gtfconnect.ui.adapters.ImageMiniPreviewAdapter;
import com.gtfconnect.ui.adapters.groupChatAdapter.DummyUserListAdapter;
import com.gtfconnect.ui.adapters.groupChatAdapter.GroupChatAdapter;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.MemberProfileScreen;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.ProfileScreen;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.GifPreviewScreen;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.CommentScreen;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.PinnedMessageScreen;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.VideoPreviewScreen;
import com.gtfconnect.ui.screenUI.recentModule.ExclusiveOfferScreen;
import com.gtfconnect.ui.screenUI.userProfileModule.UserProfileScreen;
import com.gtfconnect.utilities.AttachmentUploadUtils;
import com.gtfconnect.utilities.AudioPlayUtil;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.CustomEditText;
import com.gtfconnect.utilities.FetchPath;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.PermissionCheckUtils;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.ChatViewModel;
import com.gtfconnect.viewModels.ConnectViewModel;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.socket.client.Ack;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GroupChatScreen extends AppCompatActivity implements ApiResponseListener, GroupChatListener, ImagePreviewListener, UpdateGroupDummyUserListener {

    ActivityGroupChatBinding binding;

    private JSONObject jsonRawObject;
    Integer gcMemberID, userID;
    int channelID;

    private final int REQUEST_PIN_MESSAGE = 1;

    private final int REQUEST_EMOJI_LIST = 2;

    private final int REQUEST_UPLOAD_FILE = 3;

    private final int PINNED_MESSAGE_COUNT = 4;

    private final int GET_DUMMY_USER = 5;

    private final int UPDATE_DUMMY_USER = 6;

    private final int GET_GROUP_CHANNEL_INFO = 7;


    private int requestType;

    private ArrayList<ImagePreviewModel> multipleImageUri;

    private Uri singleImageUri;

    private long mLastClickTime;

    private File selectedMedia;

    private boolean isMessageQuoted = false;


    private boolean isUserTyping = false;

    private int localSavedChatCount = 0;

    private ChannelMessageReceivedModel receivedMessage;

    private boolean isScrollDownHighlighted = false;

    private boolean enableSearchScroll = false;
    private int typeCount = 0;

    private ChannelChatResponseModel responseModel;

    private ChannelChatResponseModel localResponseModel;

    private boolean isMessageNotFound = true;

    Parcelable recyclerViewState;

    private int totalItem;

    LinearLayoutManager mLayoutManager;

    private int currentPage = 1;

    ArrayList<GroupMessageReceivedModel> receivedMessageList;
    private ArrayList<ChannelRowListDataModel> list;

    private boolean isScrolling = false;

    private int subscribers = 0;
    GroupChatAdapter groupViewAdapter;

    private boolean isListLoadedOnce = false;

    private String groupChatIDRef;

    private int likeGroupChatID;

    private boolean isSelfMessageReceived = false;

    // Params to search quoted data :
    private boolean loadSearchData, isDataAvailable;
    private int searchedTillPosition;

    private Rest rest;

    private ApiResponseListener listener;

    private ChatViewModel chatViewModel;

    private EmojiListModel emojiListModel;

    private ArrayList<File> attachmentFileList;

    private String mediaIDs;
    private boolean isAnyFileAttached = false;

    private int attachment_request_code = 0;

    private boolean isAttachmentSend = false;

    private String messageText = "";

    private String postBaseUrl = "";

    private int pinMessageCount = 0;

    private ChannelCommentResponseModel commentResponseModel;

    // creating a variable for media recorder object class.
    private MediaRecorder mRecorder;

    // creating a variable for mediaplayer class
    private MediaPlayer mPlayer;

    // string variable is created for storing a file name
    private static String mFileName = null;


    private boolean isUserTypingMessage = false;

    private int selectedImageUriIndex;


    private GetDummyUserModel getDummyUserModel;

    private String default_initials = "";

    private ChannelManageReactionModel reactionModel;

    private ArrayList<Integer> selectedForwardedMessageIDs;

    ConnectViewModel connectViewModel;

    private boolean showPostSelectionCheckBox = false;


    private DatabaseViewModel databaseViewModel;

    private InfoDbEntity infoDbEntity;


    private MediaRecorder mMediaRecorder;

    MediaPlayer mediaPlayer;
    private String audioFilePath = "";

    private GroupChannelChatDbEntity databaseEntity;

    private String api_token = "";

    private String profileBaseUrl = "";

    private String userType = "";

    private UserProfileDbEntity userProfileData;


    private String userStatus = "";

    private int permission_read_request_count ;
    private int permission_write_request_count ;
    private int permission_audio_request_count ;
    private int permission_camera_request_count ;



    boolean isAutoPlayGif = false;
    boolean isAutoPlayVideo = false;

    private DummyUserListAdapter dummyUserListAdapter;

    private BottomSheetDialog dummy_user_bottomSheet;

    private boolean isGcProfileEnabled = true;

    Animation blinkAnimation;


    boolean isSendMediaEnabled = false;

    boolean isSendMessageEnabled = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Lifecycle Check ", "In the onCreate() event");

        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        checkEnabledPermissions();



        list = new ArrayList<>();
        receivedMessageList = new ArrayList<>();

        //destroyListeners();
        gcMemberID = Integer.parseInt(PreferenceConnector.readString(this, PreferenceConnector.GC_MEMBER_ID, ""));
        userID = PreferenceConnector.readInteger(this, PreferenceConnector.CONNECT_USER_ID, 0);

        channelID = Integer.parseInt(PreferenceConnector.readString(this, PreferenceConnector.GC_CHANNEL_ID, ""));

        api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");

        String userName = PreferenceConnector.readString(this, PreferenceConnector.GC_NAME, "");
        binding.userName.setText(userName);

        currentPage = 1;
        refreshChannelChatSocket();

        loadDataToAdapter();


        userTypingListener();
        messageReceivedListener();
        updateLikeListener();
        deletePostListener();
        commentReceiver();

        databaseEntity = new GroupChannelChatDbEntity();

        binding.quoteContainer.setVisibility(View.GONE);
        binding.attachmentContainer.setVisibility(View.GONE);
        binding.forwardContainer.setVisibility(View.GONE);

        binding.sendMessage.setVisibility(View.GONE);
        binding.recordButton.setVisibility(View.VISIBLE);
        binding.pinAttachment.setVisibility(View.VISIBLE);

        binding.blurFrame.setVisibility(View.GONE);


        rest = new Rest(this, false, false);

        binding.iconContainer.setVisibility(View.GONE);

        receivedMessageList = new ArrayList<>();

        currentPage = 1;
        loadLocalData();

        getReactionAndInitializeViewModel();

        initiateClickListeners();
        sendMessageAndAudioRecorderEvents();







        binding.chats.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int position = mLayoutManager.findFirstVisibleItemPosition();

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                    if(blinkAnimation != null && blinkAnimation.isInitialized() && blinkAnimation.hasStarted()){
                        blinkAnimation.cancel();
                    }


                    isScrolling = true;

                    if (position != -1) {
                        binding.chipDateContainer.setVisibility(View.VISIBLE);

                        if (!groupViewAdapter.getChipDate(mLayoutManager.findLastVisibleItemPosition()).isEmpty()) {
                            binding.chipDate.setText(groupViewAdapter.getChipDate(mLayoutManager.findLastVisibleItemPosition()));

                            // ============================================= Getting AutoPlay Video PlayBack ======================================
                            //groupViewAdapter.setCheckAutoPlayFunctionality(mLayoutManager.findLastVisibleItemPosition());
                        } else {
                            binding.chipDateContainer.setVisibility(View.GONE);
                        }
                    }
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                    if (position != -1) {

                        binding.chipDateContainer.setVisibility(View.VISIBLE);

                        if (!groupViewAdapter.getChipDate(mLayoutManager.findLastVisibleItemPosition()).isEmpty()) {
                            binding.chipDate.setText(groupViewAdapter.getChipDate(mLayoutManager.findLastVisibleItemPosition()));

                            // ============================================= Getting AutoPlay Video PlayBack ======================================
                            //groupViewAdapter.setCheckAutoPlayFunctionality(mLayoutManager.findLastVisibleItemPosition());
                        }
                        else {
                            binding.chipDateContainer.setVisibility(View.GONE);
                        }
                    }
                    binding.chipDateContainer.setVisibility(View.GONE);
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {

                    if(blinkAnimation != null && blinkAnimation.isInitialized() && blinkAnimation.hasStarted()){
                        blinkAnimation.cancel();
                    }

                    if (position != -1) {
                        binding.chipDateContainer.setVisibility(View.VISIBLE);

                        if (!groupViewAdapter.getChipDate(mLayoutManager.findLastVisibleItemPosition()).isEmpty()) {
                            binding.chipDate.setText(groupViewAdapter.getChipDate(mLayoutManager.findLastVisibleItemPosition()));

                            // ============================================= Getting AutoPlay Video PlayBack ======================================
                            //groupViewAdapter.setCheckAutoPlayFunctionality(mLayoutManager.findLastVisibleItemPosition());
                        } else {
                            binding.chipDateContainer.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                recyclerViewState = binding.chats.getLayoutManager().onSaveInstanceState(); // save recycleView state
                totalItem = mLayoutManager.getItemCount();

                if(blinkAnimation != null && blinkAnimation.isInitialized() && blinkAnimation.hasStarted()){
                    blinkAnimation.cancel();
                }

                if (isScrolling && mLayoutManager.findLastCompletelyVisibleItemPosition() == totalItem - 1 && dy < 0) {

                    Log.d("ChannelGroupChat", "totalItem = " + totalItem);
                    binding.chipDateContainer.setVisibility(View.GONE);

                    isScrolling = false;

                    //currentPage = databaseEntity.getChatBodyDbEntitiesLists().get(0).getPage();

                    currentPage++;

                    Log.d("Page", "" + currentPage);

                    //binding.loader.setVisibility(View.VISIBLE);

                    updateChannelChatSocket(true);

                } else {
                    if (!groupViewAdapter.getChipDate(mLayoutManager.findFirstVisibleItemPosition()).isEmpty()) {
                        //Log.d("Chip Date = ", "" + groupViewAdapter.getChipDate(mLayoutManager.findFirstVisibleItemPosition()));
                        binding.chipDate.setText(groupViewAdapter.getChipDate(mLayoutManager.findLastVisibleItemPosition()));
                    } else {
                        binding.chipDateContainer.setVisibility(View.GONE);
                    }
                }

                if (mLayoutManager.findFirstVisibleItemPosition() > 5) {
                    binding.iconContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.iconContainer.setVisibility(View.GONE);
                }


            }

        });

        binding.type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.sendMessage.setVisibility(View.VISIBLE);
                binding.recordButton.setVisibility(View.GONE);

                isUserTyping = true;
                typeCount = charSequence.length();

                if (typeCount == 0) {
                    binding.sendMessage.setVisibility(View.GONE);
                    binding.recordButton.setVisibility(View.VISIBLE);

                    isUserTypingMessage = false;
                    endTypingListener();
                } else {
                    isUserTypingMessage = true;
                    startTypingListener();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        if (pinMessageCount == 0) {
            binding.pinnedMessageCountContainer.setVisibility(View.GONE);
        } else {
            binding.pinnedMessageCountContainer.setVisibility(View.VISIBLE);
        }

    }



    private void initiateClickListeners() {
        // Navigate for Member Chat
        binding.memberTitle.setOnClickListener(view -> {
            Intent intent = new Intent(GroupChatScreen.this, ProfileScreen.class);
            intent.putExtra("viewType",Constants.Group_Channel_TYPE_2);
            startActivity(intent);
        });

        binding.pin.setOnClickListener(view -> {

            if (pinMessageCount == 0) {
                Utils.showSnackMessage(GroupChatScreen.this, binding.pin, "No Pinned Message Found!");
            } else {
                Intent i = new Intent(GroupChatScreen.this, PinnedMessageScreen.class);
                i.putExtra("post_base_url", postBaseUrl);
                i.putExtra("profile_base_url",profileBaseUrl);
                startForActivityResultLauncher.launch(i);
            }
        });

        binding.backClick.setOnClickListener(view -> onBackPressed());


        // Bottom sheet for Mute Notifications
        binding.pinAttachment.setOnClickListener(view -> {
            openAttachmentDialog();
        });

        binding.iconContainer.setOnClickListener(view -> {
            //currentPage = 1;

            if (isScrollDownHighlighted) {
                isScrollDownHighlighted = false;
                binding.arrowIcon.setColorFilter(getResources().getColor(R.color.tab_grey));
            }


            scrollToPosition(0,false);
            binding.iconContainer.setVisibility(View.GONE);

        });


        binding.footerStatusTag.setOnClickListener(view -> {

            if (userStatus.equalsIgnoreCase(Constants.USER_LEFT)) {
                if (infoDbEntity.getGcMemberInfo().getGCMemberID() != null) {
                    requestType = Constants.REJOIN_GROUP_CHANNEL;
                    connectViewModel.rejoin_group_channel(infoDbEntity.getGcMemberInfo().getGCMemberID(), api_token);
                }
            }
            else if (userStatus.equalsIgnoreCase(Constants.USER_RENEW_PLAN)) {

                String data = new Gson().toJson(infoDbEntity.getGcSubscriptionPlan());

                Intent intent = new Intent(GroupChatScreen.this, ExclusiveOfferScreen.class);
                intent.putExtra("plans",data);
                startActivity(intent);
                finish();
            }
        });






        binding.forwardMessage.setOnClickListener(view -> forwardSaveMessage());







        binding.closeQuoteEditor.setOnClickListener(view -> {
            binding.quoteContainer.setVisibility(View.GONE);

            toggleSearchContainer(1,1,2);

            isMessageQuoted = false;
        });

        binding.sendMessage.setOnClickListener(view -> {


            if (isUserTypingMessage) {

                binding.sendMessage.setVisibility(View.VISIBLE);
                binding.recordButton.setVisibility(View.GONE);

                endTypingListener();
                messageText = binding.type.getText().toString().trim();

                Log.d("SendMessage","Button Clicked");
                Log.d("SendMessage","isAnyFileAttached = "+isAnyFileAttached);

                if (isAnyFileAttached) {
                    if (messageText != null && !messageText.equalsIgnoreCase("")) {
                        callAttachmentApi();
                    } else {
                        messageText = "";
                        callAttachmentApi();
                    }
                } else {

                    if (messageText != null && !messageText.equalsIgnoreCase("")) {
                        validateSendMessage(messageText, binding.type);
                        messageText = null;
                    } else {
                        Utils.showSnackMessage(this, binding.type, "Type Message !");
                        binding.imagePreviewLayout.setVisibility(View.GONE);
                        isAnyFileAttached = false;
                        isAttachmentSend = false;
                    }
                }
            } else {

                messageText = binding.type.getText().toString().trim();

                Log.d("SendMessage","Button Clicked");
                Log.d("SendMessage","isAnyFileAttached = "+isAnyFileAttached);

                if (isAnyFileAttached) {

                    binding.sendMessage.setVisibility(View.VISIBLE);
                    binding.recordButton.setVisibility(View.GONE);

                    callAttachmentApi();
                }
                else{
                    if (messageText != null && !messageText.equalsIgnoreCase("")) {
                        validateSendMessage(messageText, binding.type);
                        messageText = null;
                    } else {
                        Utils.showSnackMessage(this, binding.type, "Type Message !");
                        binding.imagePreviewLayout.setVisibility(View.GONE);
                        isAnyFileAttached = false;
                        isAttachmentSend = false;
                    }
                }
            }
        });




        binding.dummyUsers.setOnClickListener(view -> {
            dummy_user_bottomSheet = new BottomSheetDialog(GroupChatScreen.this);
            dummy_user_bottomSheet.setContentView(R.layout.bottomsheet_dummy_user_list);

            RecyclerView dummy_user_recycler = dummy_user_bottomSheet.findViewById(R.id.dummy_user_list);
            LinearLayout toggleNotification = dummy_user_bottomSheet.findViewById(R.id.toggle_notification);

            dummyUserListAdapter= new DummyUserListAdapter(GroupChatScreen.this,getDummyUserModel,this);
            dummy_user_recycler.setHasFixedSize(true);
            dummy_user_recycler.setLayoutManager(new LinearLayoutManager(this));
            dummy_user_recycler.setAdapter(dummyUserListAdapter);

            toggleNotification.setOnClickListener(view1 -> {

                Map<String,Object> params = new HashMap<>();
                params.put("DummyUserID",0);
                params.put("IsAdmin",1);

                requestType = UPDATE_DUMMY_USER;
                connectViewModel.update_dummy_user_list(channelID,PreferenceConnector.readString(this,PreferenceConnector.API_GTF_TOKEN_,""),params);

                dummy_user_bottomSheet.dismiss();
            });

            dummy_user_bottomSheet.show();
        });






        binding.closeAttachmentContainer.setOnClickListener(view -> {
            isAnyFileAttached = false;
            binding.attachmentContainer.setVisibility(View.GONE);
        });
    }


    private void sendMessageAndAudioRecorderEvents() {


        binding.recordButton.setRecordView(binding.recordView);
        binding.recordView.setOnBasketAnimationEndListener(() -> {
            Log.d("RecordView", "Basket Animation Finished");
            binding.footerSearchContainer.setVisibility(View.VISIBLE);
            binding.recordView.setVisibility(View.GONE);
        });

        binding.recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..

                audioFilePath = "";
                binding.footerSearchContainer.setVisibility(View.GONE);
                binding.recordView.setVisibility(View.VISIBLE);

                recordAudio();
                Log.d("RecordView", "onStart");
            }

            @Override
            public void onCancel() {
                //On Swipe To Cancel
                Log.d("RecordView", "onCancel");
                stopRecording();
                Utils.deleteAudioFile(audioFilePath);
                /*binding.footerSearchContainer.setVisibility(View.VISIBLE);
                binding.recordView.setVisibility(View.GONE);*/
            }

            @Override
            public void onFinish(long recordTime, boolean limitReached) {
                //Stop Recording..
                //limitReached to determine if the Record was finished when time limit reached.
                //String time = getHumanTimeText(recordTime);
                Log.d("RecordView", "onFinish");
                stopRecording();

                File audioFile = new File(audioFilePath);

                attachmentFileList = new ArrayList<>();
                attachmentFileList.add(audioFile);
                isAnyFileAttached = true;

                attachment_request_code = Constants.RECORD_AUDIO_REQUEST_CODE;
                callAttachmentApi();

                binding.footerSearchContainer.setVisibility(View.VISIBLE);
                binding.recordView.setVisibility(View.GONE);

            }

            @Override
            public void onLessThanSecond() {
                //When the record time is less than One Second
                Log.d("RecordView", "onLessThanSecond");
                binding.footerSearchContainer.setVisibility(View.VISIBLE);
                binding.recordView.setVisibility(View.GONE);

                stopRecording();
                Utils.deleteAudioFile(audioFilePath);
            }

            @Override
            public void onLock() {
                //When Lock gets activated
                Log.d("RecordView", "onLock");
            }

        });
    }


    private void recordAudio() {
        //check the permission for the record audio and for save audio write external storage

        if (PermissionCheckUtils.checkChatPermissions(this,Constants.REQUEST_AUDIO_PERMISSIONS)) {
            requestPermissions(new String[]{RECORD_AUDIO,WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_AUDIO_PERMISSIONS);
        } else {

            audioFilePath = Utils.getAudioFilePath();

            //RecordReady
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mMediaRecorder.setOutputFile(audioFilePath);


            try {
                mMediaRecorder.prepare();
                //start the recording
                mMediaRecorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecording() {
        try {
            mMediaRecorder.stop();
        } catch (Exception e) {
            Log.d("Recorder_Exception", e.toString());
        }
    }


    private void getReactionAndInitializeViewModel() {

        // Getting API data fetch
        rest = new Rest(this, false, true);
        listener = this;
        //appDao = AppDatabase.getInstance(getApplication()).appDao();
        connectViewModel = new ViewModelProvider(this).get(ConnectViewModel.class);
        connectViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("API Call Listener ----", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse, rest);
                }

            }
        });


        requestType = GET_GROUP_CHANNEL_INFO;
        connectViewModel.get_group_channel_info(channelID,PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, ""));
    }


    public void init() {

        rest = new Rest(this, false, false);
        listener = this;
        //appDao = AppDatabase.getInstance(getApplication()).appDao();
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("API Call Listener ----", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse, rest);
                }

            }
        });

        requestType = PINNED_MESSAGE_COUNT;

        Map<String, Object> param = new HashMap<>();
        param.put("GroupChannelID", PreferenceConnector.readString(this, PreferenceConnector.GC_CHANNEL_ID, "0"));
        param.put("UserID", PreferenceConnector.readInteger(this, PreferenceConnector.CONNECT_USER_ID, 0));

        chatViewModel.getPinnedMessages(param);
    }


    private void loadLocalData() {

        Log.d("group_channel","Group ID = "+channelID);

        databaseViewModel.getGroupChannelInfo(channelID).observe(this, infoDbEntity -> {
            if (infoDbEntity != null) {
                this.infoDbEntity = infoDbEntity;
                setProfileInfo();

                groupViewAdapter.updateGcPermission(infoDbEntity);
                checkGroupChannelSettings();
            }
        });




        databaseViewModel.getUserProfileData().observe(this, new Observer<UserProfileDbEntity>() {
            @Override
            public void onChanged(UserProfileDbEntity userProfileDbEntity) {

            }
        });

        list = new ArrayList<>();

        String chatData = PreferenceConnector.readString(this,PreferenceConnector.CHANNEL_CHAT_DATA+"/"+channelID,"");
        Type type = new TypeToken<ChannelChatResponseModel>(){}.getType();
        responseModel = new Gson().fromJson(chatData,type);

        if(responseModel != null && responseModel.getData() != null) {

            if (responseModel.getData().getMediaUrl() != null){
                postBaseUrl = responseModel.getData().getMediaUrl();
            }


            if (responseModel.getData().getBaseUrl() != null){
                profileBaseUrl = responseModel.getData().getBaseUrl();
            }

            if (responseModel.getData().getChatData() != null && responseModel.getData().getChatData().getRows() != null) {
                list.addAll(responseModel.getData().getChatData().getRows());

                groupViewAdapter.updateList(list,postBaseUrl,profileBaseUrl);
            }
            if (responseModel.getData().getSubscriptionCount() != null) {
                subscribers = responseModel.getData().getSubscriptionCount();
                binding.userSubscribers.setText(String.valueOf(subscribers));
            }

        }
    }




    private void toggleSearchContainer(int togglePinAttachment, int toggleRecordAttachment, int toggleSendMessage){

        if (togglePinAttachment == 1){
            if (isSendMediaEnabled) {
                binding.pinAttachment.setVisibility(View.VISIBLE);
            }
            else{
                binding.pinAttachment.setVisibility(View.GONE);
            }
        }
        if (togglePinAttachment == 2){
            binding.pinAttachment.setVisibility(View.GONE);
        }

        if (toggleRecordAttachment == 1){
            if (isSendMediaEnabled) {
                binding.recordButton.setVisibility(View.VISIBLE);
            }
            else{
                binding.recordButton.setVisibility(View.GONE);
            }
        }
        if (toggleRecordAttachment == 2){
            binding.recordButton.setVisibility(View.GONE);
        }

        if (toggleSendMessage == 1){
            if (isSendMessageEnabled) {
                binding.sendMessage.setVisibility(View.VISIBLE);
            }
            else{
                binding.sendMessage.setVisibility(View.GONE);
            }
        }
        if (toggleSendMessage == 2){
            binding.sendMessage.setVisibility(View.GONE);
        }
    }






    private void setProfileInfo(){

        if (infoDbEntity != null){
            if (infoDbEntity.getGcInfo() != null){
                if (infoDbEntity.getGcInfo().getProfileImage() != null){
                    GlideUtils.loadImage(this,binding.groupChannelLogo,infoDbEntity.getGcInfo().getProfileImage());
                }
            }
        }
    }





    private void scrollToPosition(int position,boolean isMessageQuoted) {

        binding.chats.scrollToPosition(position);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isMessageQuoted) {

                    View getRoot = mLayoutManager.findViewByPosition(position);
                    blinkAnimation = AnimationUtils.loadAnimation(GroupChatScreen.this, R.anim.view_blink_fadder);

                    getRoot.startAnimation(blinkAnimation);
                }
            }
        }, 1000);
    }

    private void loadDataToAdapter() {


        /**
         * Set user settings
         */


        databaseViewModel.getUserProfileData().observe(this, userProfileDbEntity -> {


            if (userProfileDbEntity != null && userProfileDbEntity.getUserSetting() != null && !userProfileDbEntity.getUserSetting().isEmpty()){

                for (int i=0;i<userProfileDbEntity.getUserSetting().size();i++){
                    if (userProfileDbEntity.getUserSetting().get(i).getName()!= null && !userProfileDbEntity.getUserSetting().get(i).getName().isEmpty()){
                        if (userProfileDbEntity.getUserSetting().get(i).getName().equalsIgnoreCase("gif")){
                            if (userProfileDbEntity.getUserSetting().get(i).getSettingValue() != null && !userProfileDbEntity.getUserSetting().get(i).getSettingValue().isEmpty()){
                                if (userProfileDbEntity.getUserSetting().get(i).getSettingValue().equalsIgnoreCase("1")){
                                    isAutoPlayGif = true;
                                }
                            }
                        } else if (userProfileDbEntity.getUserSetting().get(i).getName().equalsIgnoreCase("video")){
                            if (userProfileDbEntity.getUserSetting().get(i).getSettingValue() != null && !userProfileDbEntity.getUserSetting().get(i).getSettingValue().isEmpty()){
                                if (userProfileDbEntity.getUserSetting().get(i).getSettingValue().equalsIgnoreCase("1")){
                                    isAutoPlayVideo = true;
                                }
                            }
                        }
                    }
                }

            }
        });






        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);

        // Load Comments List Data -----
        /*groupViewAdapter = new ChannelChatAdapter(this, list, String.valueOf(userID), postBaseUrl, profileBaseUrl,infoDbEntity,this,binding.chats);
        binding.chats.setHasFixedSize(true);
        binding.chats.setLayoutManager(mLayoutManager);
        binding.chats.setAdapter(groupViewAdapter);

        binding.chats.getLayoutManager().onRestoreInstanceState(recyclerViewState);*/



        groupViewAdapter = new GroupChatAdapter(this, list, String.valueOf(userID), postBaseUrl, profileBaseUrl,infoDbEntity,this,isAutoPlayVideo,isAutoPlayGif);
        binding.chats.setHasFixedSize(true);
        binding.chats.setLayoutManager(mLayoutManager);
        binding.chats.setAdapter(groupViewAdapter);

        binding.chats.getLayoutManager().onRestoreInstanceState(recyclerViewState);


    }




    private void setDummyUserData(){

        String userInitials = "";
        boolean checkFlag = false;

        if (getDummyUserModel != null &&
                getDummyUserModel.getData() != null &&
                getDummyUserModel.getData().getList()!= null &&
                !getDummyUserModel.getData().getList().isEmpty()){


            for (int i=0;i<getDummyUserModel.getData().getList().size();i++){
                if (getDummyUserModel.getData().getList().get(i).getIsAdmin() == 1){
                    userInitials = Utils.getUserInitials(getDummyUserModel.getData().getList().get(i).getFirstname(),getDummyUserModel.getData().getList().get(i).getLastname());
                    checkFlag = true;
                    break;
                }
            }

            if(!checkFlag)
            {
                userInitials = Utils.getUserInitials(getDummyUserModel.getData().getList().get(0).getFirstname(),getDummyUserModel.getData().getList().get(0).getLastname());
            }

            binding.dummyUserInitials.setText(userInitials);
            binding.dummyUserContainer.setVisibility(View.VISIBLE);
        }
    }




    private void refreshChannelChatSocket() {
        Gson gson = new Gson();
        Type type;

        type = new TypeToken<ChannelChatResponseModel>() {
        }.getType();

        responseModel = new ChannelChatResponseModel();

        try {
            jsonRawObject = new JSONObject();
            jsonRawObject.put("GCMemberID", gcMemberID);
            jsonRawObject.put("GroupChannelID", channelID);
            jsonRawObject.put("userId", userID);
            jsonRawObject.put("page", currentPage);

            Log.d("Chat list params --", jsonRawObject.toString());

            socketInstance.emit("chatList", jsonRawObject, (Ack) args -> {

                runOnUiThread(() -> rest.dismissProgressdialog());

                JSONObject responseData = (JSONObject) args[0];
                Log.d("Group Chat Data ----", responseData.toString());

                JsonParser jsonParser = new JsonParser();
                JsonObject gsonObject = (JsonObject) jsonParser.parse(responseData.toString());

                responseModel = gson.fromJson(gsonObject, type);

                runOnUiThread(() -> {

                    if(responseModel != null && responseModel.getData() != null) {


                        if (responseModel.getData().getBaseUrl() != null){
                            profileBaseUrl = responseModel.getData().getBaseUrl();
                        }

                        if (responseModel.getData().getMediaUrl() != null){
                            postBaseUrl = responseModel.getData().getMediaUrl();
                        }

                        if (responseModel.getData().getChatData() != null && responseModel.getData().getChatData().getRows() != null && !responseModel.getData().getChatData().getRows().isEmpty()) {

                            if (currentPage == 1){
                                String response = new Gson().toJson(responseModel);
                                PreferenceConnector.writeString(this,PreferenceConnector.CHANNEL_CHAT_DATA+"/"+channelID,response);

                                list = new ArrayList<>();
                                list.addAll(responseModel.getData().getChatData().getRows());


                                readMessages(Integer.parseInt(responseModel.getData().getChatData().getRows().get(0).getGroupChatID()));

                                scrollToPosition(0,false);
                            }
                            else{
                                list.addAll(responseModel.getData().getChatData().getRows());
                            }

                            groupViewAdapter.updateList(list,postBaseUrl,profileBaseUrl);
                        }
                        if (responseModel.getData().getSubscriptionCount() != null) {
                            subscribers = responseModel.getData().getSubscriptionCount();
                            binding.userSubscribers.setText(String.valueOf(subscribers));
                        }
                    }
                });


            });
        } catch (Exception e) {
            Log.d("JsonException ---", e.toString());
        }
    }

    private void updateChannelChatSocket(boolean isScrolling) {
        isListLoadedOnce = true;

        Gson gson = new Gson();
        Type type;

        type = new TypeToken<ChannelChatResponseModel>() {
        }.getType();

        responseModel = new ChannelChatResponseModel();

        try {
            jsonRawObject = new JSONObject();
            jsonRawObject.put("GCMemberID", gcMemberID);
            jsonRawObject.put("GroupChannelID", channelID);
            jsonRawObject.put("userId", userID);
            jsonRawObject.put("page", currentPage);

            Log.d("Chat list params --", jsonRawObject.toString());

            socketInstance.emit("chatList", jsonRawObject, (Ack) args -> {

                if (!isScrolling)
                    runOnUiThread(() -> rest.dismissProgressdialog());

                JSONObject responseData = (JSONObject) args[0];
                Log.d("Group Chat Data ----", responseData.toString());

                JsonParser jsonParser = new JsonParser();
                JsonObject gsonObject = (JsonObject) jsonParser.parse(responseData.toString());

                responseModel = gson.fromJson(gsonObject, type);


                if (responseData == null) {
                    Utils.showSnackMessage(this, binding.getRoot(), "No Data Found");
                    Log.d("authenticateUserAndFetchData -- ", "Error");
                } else {

                    runOnUiThread(() -> {

                        if(responseModel != null && responseModel.getData() != null) {

                            if (responseModel.getData().getMediaUrl() != null) {
                                postBaseUrl = responseModel.getData().getMediaUrl();
                            }

                            if (responseModel.getData().getBaseUrl() != null){
                                profileBaseUrl = responseModel.getData().getBaseUrl();
                            }

                            if (responseModel.getData().getChatData() != null && responseModel.getData().getChatData().getRows() != null && !responseModel.getData().getChatData().getRows().isEmpty()) {

                                if (currentPage == 1){
                                    String response = new Gson().toJson(responseModel);
                                    PreferenceConnector.writeString(this,PreferenceConnector.CHANNEL_CHAT_DATA+"/"+channelID,response);

                                    list = new ArrayList<>();
                                    list.addAll(responseModel.getData().getChatData().getRows());

                                    readMessages(Integer.parseInt(responseModel.getData().getChatData().getRows().get(0).getGroupChatID()));
                                }
                                else{
                                    list.addAll(responseModel.getData().getChatData().getRows());
                                }

                                groupViewAdapter.updateList(list,postBaseUrl,profileBaseUrl);
                            }
                            if (responseModel.getData().getSubscriptionCount() != null) {
                                subscribers = responseModel.getData().getSubscriptionCount();
                                binding.userSubscribers.setText(String.valueOf(subscribers));
                            }

                        }
                    });
                }

            });
        } catch (Exception e) {
            Log.d("JsonException ---", e.toString());
        }
    }




    private void userTypingListener() {
        socketInstance.on("userTyping", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                Log.d("User Typing Data :=>", data.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (Integer.parseInt(data.getString("userId")) != userID) {
                                if (data.getString("type").equalsIgnoreCase("start")) {
                                    binding.userSubscribers.setText(data.getString("Firstname"));
                                    binding.subscriberSubtitle.setText("is typing");
                                }
                                if (data.getString("type").equalsIgnoreCase("end")) {
                                    binding.userSubscribers.setText(String.valueOf(subscribers));
                                    binding.subscriberSubtitle.setText("subscribers");
                                }
                            }
                        } catch (Exception e) {
                            Log.d("Typing header exception --", e.toString());
                        }
                    }
                });
            }
        });
    }

    private void startTypingListener() {
        try {

            jsonRawObject = new JSONObject();
            jsonRawObject.put("GroupChannelID", channelID);
            jsonRawObject.put("userId", userID);
            jsonRawObject.put("type", "start");

            socketInstance.emit("typing", jsonRawObject);

        } catch (Exception e) {
            Log.d("startTyping  Exception ----", e.toString());
        }
    }

    private void endTypingListener() {
        try {

            jsonRawObject = new JSONObject();
            jsonRawObject.put("GroupChannelID", channelID);
            jsonRawObject.put("userId", userID);
            jsonRawObject.put("type", "end");

            socketInstance.emit("typing", jsonRawObject);

        } catch (Exception e) {
            Log.d("startTyping  Exception ----", e.toString());
        }
    }

    private void validateSendMessage(String message, View view) {
        binding.quoteContainer.setVisibility(View.GONE);

        toggleSearchContainer(1,1,2);

        binding.type.setText("");
        Utils.softKeyboard(this, false, binding.type);

        binding.type.setText("");

        String userID = this.userID.toString();

        if (message != null) {

            try {
                jsonRawObject = new JSONObject();
                jsonRawObject.put("GCMemberID", gcMemberID);
                jsonRawObject.put("GroupChannelID", channelID);
                jsonRawObject.put("UserID", userID);

                if (isAttachmentSend) {
                    jsonRawObject.put("ChatType", "file");
                    jsonRawObject.put("MediaIds", mediaIDs);

                    jsonRawObject.put("Message", message);
                    sendMessage(jsonRawObject);
                    Log.v("Send Message Params --", jsonRawObject.toString());
                } else {
                    jsonRawObject.put("ChatType", "msg");


                    jsonRawObject.put("Message", message);

                    if (isMessageQuoted) {
                        jsonRawObject.put("GroupChatRefID", groupChatIDRef);
                    }

                    sendMessage(jsonRawObject);
                    Log.v("Send Message Params --", jsonRawObject.toString());
                }

            } catch (Exception e) {
                Log.d("sendMessage Exception ----", e.toString());
            }
        }
        else {
            isMessageQuoted = false;
            isAttachmentSend = false;
            Toast.makeText(this, "Type message!", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendMessage(JSONObject jsonRawObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.emit("sendMessage", jsonRawObject, (Ack) args -> {
                    //boolean isMsgSent = (boolean) args[0];

                    Log.d("Message Send Log", args[0].toString());

                    isMessageQuoted = false;
                    isAttachmentSend = false;

                    //currentPage = 1;
                    //refreshChannelChatSocket();
                            /*if (isMsgSent) {
                                Log.d("Msg sent ", "successfully");
                                //scrollToFirstPosition();
                                currentPage = 1;
                                refreshGroupChatSocket();
                            } else {
                                Log.d("Msg sent ", "failed");
                            }*/

                });
            }
        });
    }

    private void messageReceivedListener() {

        Log.d("socket_connection>>", "" + socketInstance.connected());
        Log.d("Message_Received_Listener","Channel Message Listener Method");

        socketInstance.on("messageReceived", args -> {

            Log.d("Message_Received_Listener", "Message Received");

            receivedMessage = new ChannelMessageReceivedModel();

            Gson gson = new Gson();
            Type type;

            type = new TypeToken<ChannelMessageReceivedModel>() {
            }.getType();

            isScrollDownHighlighted = true;


            JSONObject data = (JSONObject) args[0];
            Log.d("Message Received Data :", data.toString());
            Log.d("Message Received", "Listener");

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(data.toString());

            receivedMessage = gson.fromJson(gsonObject, type);

            Log.d("received_message",receivedMessage.getSaveMsg().getBaseUrl());

            if (!receivedMessage.getSaveMsg().getGetData().getUser().getUserID().equalsIgnoreCase(String.valueOf(PreferenceConnector.readInteger(this, PreferenceConnector.CONNECT_USER_ID, 0)))) {
                binding.arrowIcon.setColorFilter(getResources().getColor(R.color.theme_green));
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (receivedMessage != null && receivedMessage.getSaveMsg() != null && receivedMessage.getSaveMsg().getGetData() != null){
                        list.add(0,receivedMessage.getSaveMsg().getGetData());
                        groupViewAdapter.updateChat(list);
                        binding.chats.scrollToPosition(0);
                    }
                    else{
                        Log.d("received_message","getting null or empty data in above condition");
                    }

                }
            });
        });
    }

    private void updateLikeListener() {

        Gson gson = new Gson();
        Type type;

        Log.d("Like Listener :::: ", "Called");
        type = new TypeToken<ChannelReactionReceivedModel>() {
        }.getType();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.on("likeUpdate", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {

                        JSONObject data = (JSONObject) args[0];
                        Log.d("Like Data :", data.toString());


                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(data.toString());

                        ChannelReactionReceivedModel like_response = gson.fromJson(gsonObject, type);
                        //Log.d("Like response from model", String.valueOf(like_response.getData().getChatDetail().getLike().get(0).getIsLike()));


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int messageID = data.getInt("messageId");

                                    if (messageID == likeGroupChatID) {
                                        for (int i = 0; i < list.size(); i++) {
                                            if (list.get(i).getGroupChatID().equalsIgnoreCase(String.valueOf(likeGroupChatID))) {
                                                if (list.get(i).getLike() != null && !list.get(i).getLike().isEmpty()) {

                                                    Log.d("Is liked User ---", "Not Null case : " + list.get(i).getLike().get(0).getIsLike().toString());

                                                    list.get(i).getLike().remove(0);
                                                    list.get(i).getLike().add(0,like_response.getData().getChatDetail().getLike().get(0));
                                                    groupViewAdapter.updateLikeResponse(i,list);

                                                    /*if (list.get(i).getLike().get(0).getIsLike() == 1) {
                                                        list.get(i).getLike().get(0).setIsLike(0);
                                                        groupViewAdapter.updateList(list,postBaseUrl,profileBaseUrl);
                                                        break;
                                                    } else {
                                                        list.get(i).getLike().get(0).setIsLike(1);
                                                        groupViewAdapter.updateList(list,postBaseUrl,profileBaseUrl);
                                                        break;
                                                    }*/
                                                } else {
                                                    list.get(i).getLike().add(0, like_response.getData().getChatDetail().getLike().get(0));

                                                    Log.d("Is liked User ---", "Null case : " + list.get(i).getLike().get(0).getIsLike().toString());
                                                    groupViewAdapter.updateLikeResponse(i,list);
                                                    break;
                                                }
                                            }
                                        }

                                    }
                                } catch (Exception e) {
                                    Log.d("Getting like message id exception :", e.toString());
                                }
                            }
                        });


                    }
                });
            }
        });
    }


    private void commentReceiver() {
        commentResponseModel = new ChannelCommentResponseModel();

        Gson gson = new Gson();
        Type type;

        type = new TypeToken<ChannelCommentResponseModel>() {
        }.getType();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.on("commentted", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {

                        JSONObject data = (JSONObject) args[0];
                        Log.d("Comment Data :", data.toString());

                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(data.toString());

                        commentResponseModel = gson.fromJson(gsonObject, type);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchedTillPosition = 0;
                                isMessageNotFound = true;
                                loadSearchData = false;
                                isDataAvailable = true;
                                searchMessageInList(commentResponseModel.getGroupChatID().toString(), false, true, false);
                            }
                        });


                        try {
                            Log.d("Latest Comment :", data.getJSONObject("data").getJSONObject("comment").toString());
                        } catch (Exception e) {
                            Log.d("Error fetching comment ", e.toString());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Todo: Remove when live user ID has been used

                                    CommentReceiveResponseModel comment = new CommentReceiveResponseModel();


                            /*if (detail.getGroupChatID() == )
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
                                } catch (Exception e) {
                                    Log.d("Typing header exception --", e.toString());
                                }
                            }
                        });
                    }
                });
            }
        });
    }


    private void readMessages(int lastChatMessage)
    {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                jsonRawObject = new JSONObject();

                Log.d("read_message","Entered Method");

                try{
                    jsonRawObject.put("GroupChannelID",channelID);
                    jsonRawObject.put("UserID",userID);
                    jsonRawObject.put("GroupChatID",lastChatMessage);

                    Log.d("read_message","params = "+jsonRawObject.toString());
                }
                catch (Exception e){
                    Log.d("json_exception","read message exception"+e);
                }



                socketInstance.emit("readMessage", jsonRawObject, (Ack) args -> {
                    //boolean isMsgSent = (boolean) args[0];
                    Log.d("read_message","Entered socket method");
                });
            }
        });
    }




    /*private void deleteCommentListener()
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
                        commentDeleteResponseModel.setCommentData(gson.fromJson(response.toString(), type));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchMessageInList(commentDeleteResponseModel.getGroupChatID().toString(),false,false,true);
                            }
                        });
                    }
                });
            }
        });
    }*/


    private void searchMessageInList(String groupChatId, boolean hasPostDeleted, boolean commentSearch, boolean commentDeleteSearch) {

        if (isMessageNotFound) {
            if (loadSearchData) {
                Gson gson = new Gson();
                Type type;
                type = new TypeToken<ChannelChatResponseModel>() {
                }.getType();

                responseModel = new ChannelChatResponseModel();


                try {
                    jsonRawObject = new JSONObject();
                    jsonRawObject.put("GCMemberID", gcMemberID);
                    jsonRawObject.put("GroupChannelID", channelID);
                    jsonRawObject.put("userId", userID);
                    jsonRawObject.put("page", currentPage);

                    Log.d("Chat list params --", jsonRawObject.toString());

                    socketInstance.emit("chatList", jsonRawObject, (Ack) args -> {

                        JSONObject responseData = (JSONObject) args[0];
                        Log.d("Group Chat Data ----", responseData.toString());

                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(responseData.toString());

                        responseModel = gson.fromJson(gsonObject, type);

                        if (responseData == null) {
                            Utils.showSnackMessage(GroupChatScreen.this, binding.getRoot(), "No Data Found");
                            Log.d("authenticateUserAndFetchData -- ", "Error");
                            isDataAvailable = false;
                        } else if (responseModel.getData().getChatData().getRows() == null || responseModel.getData().getChatData().getRows().isEmpty()) {
                            isDataAvailable = false;
                        } else {

                            runOnUiThread(() -> {

                                list.addAll(responseModel.getData().getChatData().getRows());
                                groupViewAdapter.updateList(list,postBaseUrl,profileBaseUrl);
                                loadSearchData = false;
                                currentPage++;

                                searchMessageInList(groupChatId, hasPostDeleted, commentSearch, commentDeleteSearch);
                            });

                        }
                    });

                } catch (Exception e) {
                    Log.d("JsonException ---", e.toString());
                }
            } else {
                for (int i = searchedTillPosition; i < list.size(); i++) {
                    if (groupChatId.equalsIgnoreCase(list.get(i).getGroupChatID())) {

                        int position = i;
                        if (hasPostDeleted) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list.remove(searchedTillPosition);
                                    groupViewAdapter.updateList(list,postBaseUrl,profileBaseUrl);
                                }
                            });
                            rest.dismissProgressdialog();
                            isMessageNotFound = false;
                            break;
                        } else if (commentSearch) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list.get(position).setCommentData(commentResponseModel.getData().getChatDetail().getCommentData());
                                    groupViewAdapter.updateList(list,postBaseUrl,profileBaseUrl);
                                }
                            });
                            rest.dismissProgressdialog();
                            isMessageNotFound = false;
                            break;
                        }
                        /*else if (commentDeleteSearch) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (commentDeleteResponseModel.getCommentData() != null) {
                                        list.get(searchedTillPosition).setCommentData(commentDeleteResponseModel.getCommentData());
                                        groupViewAdapter.updateList(list);
                                    }
                                    else {
                                        list.get(searchedTillPosition).setCommentCount(0);
                                        groupViewAdapter.updateList(list);
                                    }
                                }
                            });
                            rest.dismissProgressdialog();
                            isMessageNotFound = false;
                            break;
                        }*/
                        else {
                            scrollToPosition(i,true);
                            rest.dismissProgressdialog();
                            isMessageNotFound = false;
                            break;
                        }
                    }
                    searchedTillPosition = i;
                }
                loadSearchData = true;
                searchMessageInList(groupChatId, hasPostDeleted, commentSearch, commentDeleteSearch);
            }
        }
    }


    private void deletePostListener() {
        Gson gson = new Gson();
        Type type;
        type = new TypeToken<PostDeleteModel>() {
        }.getType();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                socketInstance.on("postDeleteData", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject response = (JSONObject) args[0];

                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(response.toString());

                        PostDeleteModel postDeleteModel = gson.fromJson(gsonObject, type);
                        searchMessageInList(postDeleteModel.getGroupChatID().toString(), true, false, false);

                    }
                });
            }
        });

    }


    // -------------------------------------------------------------- Api Calls -----------------------------------------------------------------------------

    private void callAttachmentApi() {

        Map<String, Object> params = new HashMap<>();
        params.put("GCMemberID", gcMemberID);
        params.put("GroupChannelID", channelID);
        params.put("UserID", this.userID.toString());


        List<MultipartBody.Part> files = new ArrayList<>();

        if (attachment_request_code == Constants.CAPTURE_IMAGE_REQUEST_CODE) {

            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("image/*"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("files", attachmentFileList.get(0).getName(), part);
            files.add(attachment);
        } else if (attachment_request_code == Constants.SELECT_VIDEO_REQUEST_CODE) {

            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("video/*"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("files", attachmentFileList.get(0).getName(), part);
            files.add(attachment);
        } else if (attachment_request_code == Constants.SELECT_DOCUMENT_REQUEST_CODE) {

            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("document/*"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("files", attachmentFileList.get(0).getName(), part);
            files.add(attachment);

        } else if (attachment_request_code == Constants.SELECT_PICTURE_REQUEST_CODE) {
            for (int i = 0; i < attachmentFileList.size(); i++) {
                RequestBody part =
                        RequestBody.create(
                                MediaType.parse("image/*"),
                                attachmentFileList.get(i)
                        );

                MultipartBody.Part attachment = MultipartBody.Part.createFormData("files", attachmentFileList.get(i).getName(), part);
                files.add(attachment);
            }
        } else if (attachment_request_code == Constants.RECORD_AUDIO_REQUEST_CODE) {
            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("audio/webm;codecs=opus"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("files", attachmentFileList.get(0).getName(), part);
            files.add(attachment);
        } else if (attachment_request_code == Constants.SHARE_GIF) {
            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("image/gif"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("files", attachmentFileList.get(0).getName(), part);
            files.add(attachment);
        }

        Log.d("callAttachmentApi:", "file = "+files.size());
        Log.d("callAttachmentApi:", "params = "+params);

        attachment_request_code = 0;
        requestType = REQUEST_UPLOAD_FILE;
        chatViewModel.attachFile(params, files);

    }


    // -------------------------------------------------------------------Access Storage Implementation ----------------------------------------------------------------


    private void openAttachmentDialog() {

        if (PermissionCheckUtils.checkChatPermissions(this,Constants.REQUEST_ATTACHMENT_MEDIA_PERMISSIONS)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_ATTACHMENT_MEDIA_PERMISSIONS);
        } else {
            AttachmentUploadUtils.showPictureDialog(
                    this,
                    new AttachmentUploadListener() {
                        @Override
                        public void onClickGallery() {
                            accessMedia();
                        }

                        @Override
                        public void onClickCamera(Intent intent) {
                            accessCamera();
                        }

                        @Override
                        public void onClickDocument() {
                            accessDocument();
                        }
                    }
            );
        }
    }

    private void accessMedia() {
        BottomSheetDialog media_type = new BottomSheetDialog(this);
        media_type.setContentView(R.layout.bottomsheet_select_media_type);

        TextView cancel = media_type.findViewById(R.id.cancel);
        TextView image = media_type.findViewById(R.id.image);
        TextView video = media_type.findViewById(R.id.video);


        cancel.setOnClickListener(view -> media_type.dismiss());

        image.setOnClickListener(view -> {
            accessMediaImage();
            media_type.dismiss();
        });

        video.setOnClickListener(view -> {
            accessMediaVideo();
            media_type.dismiss();
        });

        media_type.show();
    }

    private void accessMediaImage() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        //i.setAction(Intent.ACTION_PICK);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), Constants.SELECT_PICTURE_REQUEST_CODE);
    }

    private void accessMediaVideo() {
        // intent of the type image
        Intent i = new Intent();
        i.setType("video/*");
        i.setAction(Intent.ACTION_PICK);
        startActivityForResult(i, Constants.SELECT_VIDEO_REQUEST_CODE);
    }

    private void accessCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        singleImageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


        Intent cameraIntent = AttachmentUploadUtils.takePhotoFromCamera(this);
        selectedMedia = new File(Objects.requireNonNull(cameraIntent.getStringExtra("image_path")));
        startActivityForResult(cameraIntent, Constants.CAPTURE_IMAGE_REQUEST_CODE);

    }


    private void accessDocument() {

        /*String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        i.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        i.addCategory(Intent.CATEGORY_OPENABLE);*/
//i.setType("*/*");


        String[] mimeTypes = {"application/pdf"};

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("pdf/*");
        i.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        i.addCategory(Intent.CATEGORY_OPENABLE);

        resultLauncher.launch(i);
        //startActivityForResult(i,Constants.SELECT_DOCUMENT_REQUEST_CODE);
        //resultLauncher.launch(i);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        Log.d("permission_log","requested");
        if (requestCode == Constants.REQUEST_ALL_MEDIA_PERMISSIONS) {

            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                permission_camera_request_count ++;
                PreferenceConnector.writeInteger(GroupChatScreen.this,PreferenceConnector.CAMERA_PERMISSION_COUNT,permission_camera_request_count);
            }
            if (grantResults[1] != PackageManager.PERMISSION_GRANTED){
                permission_write_request_count ++;
                PreferenceConnector.writeInteger(GroupChatScreen.this,PreferenceConnector.WRITE_STORAGE_PERMISSION_COUNT,permission_write_request_count);
            }
            if (grantResults[2] != PackageManager.PERMISSION_GRANTED){
                permission_read_request_count ++;
                PreferenceConnector.writeInteger(GroupChatScreen.this,PreferenceConnector.READ_STORAGE_PERMISSION_COUNT,permission_read_request_count);
            }
            if (grantResults[3] != PackageManager.PERMISSION_GRANTED){
                permission_audio_request_count ++;
                PreferenceConnector.writeInteger(GroupChatScreen.this,PreferenceConnector.MICROPHONE_PERMISSION_COUNT,permission_audio_request_count);
            }

        } else if (requestCode == Constants.REQUEST_ATTACHMENT_MEDIA_PERMISSIONS) {


            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                openAttachmentDialog();
            } else {

                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permission_camera_request_count++;
                    PreferenceConnector.writeInteger(GroupChatScreen.this, PreferenceConnector.CAMERA_PERMISSION_COUNT, permission_camera_request_count);
                }
                if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    permission_write_request_count++;
                    PreferenceConnector.writeInteger(GroupChatScreen.this, PreferenceConnector.WRITE_STORAGE_PERMISSION_COUNT, permission_write_request_count);
                }
                if (grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    permission_read_request_count++;
                    PreferenceConnector.writeInteger(GroupChatScreen.this, PreferenceConnector.READ_STORAGE_PERMISSION_COUNT, permission_read_request_count);
                }
                Toast.makeText(this, "Please grant required permissions!", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == Constants.REQUEST_AUDIO_PERMISSIONS) {

            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permission_audio_request_count++;
                    PreferenceConnector.writeInteger(GroupChatScreen.this, PreferenceConnector.CAMERA_PERMISSION_COUNT, permission_audio_request_count);
                }
                if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    permission_write_request_count++;
                    PreferenceConnector.writeInteger(GroupChatScreen.this, PreferenceConnector.WRITE_STORAGE_PERMISSION_COUNT, permission_write_request_count);
                }


                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    recordAudio();
                } else {
                    Toast.makeText(this, "Please grant required permissions!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                recordAudio();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.SELECT_PICTURE_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            isAnyFileAttached = true;
            attachment_request_code = Constants.SELECT_PICTURE_REQUEST_CODE;

            multipleImageUri = new ArrayList<>();
            attachmentFileList = new ArrayList<>();

            if (data.getClipData() != null) {
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {

                    Uri imageUri = data.getClipData().getItemAt(i).getUri();

                    ImagePreviewModel model = new ImagePreviewModel();
                    model.setUri(imageUri);
                    multipleImageUri.add(i, model);
                    attachmentFileList.add(new File(Objects.requireNonNull(FetchPath.getPath(this, imageUri))));


                    Log.d("file_name",attachmentFileList.get(0).getName());
                    Log.d("Uri Data", imageUri.toString());
                }


            } else {
                isAnyFileAttached = true;
                Uri imageUri = data.getData();

                ImagePreviewModel model = new ImagePreviewModel();
                model.setUri(imageUri);
                model.setActive(false);
                multipleImageUri.add(model);

                try {
                    attachmentFileList.add(new File(Objects.requireNonNull(FetchPath.getPath(this, imageUri))));
                    Log.d("file_name",attachmentFileList.get(0).getName());
                } catch (Exception e) {
                    Log.d("Image error", e.toString());
                }
            }

            previewImage();
        } else if (requestCode == Constants.CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            isAnyFileAttached = true;
            attachment_request_code = Constants.CAPTURE_IMAGE_REQUEST_CODE;

            multipleImageUri = new ArrayList<>();
            attachmentFileList = new ArrayList<>();

            Bitmap bitmap = Utils.decodeFile(selectedMedia);
            Uri selected_camera_uri = Utils.getImageUri(this, bitmap, 1024.0f, 10240.0f);


            attachmentFileList.add(new File(Objects.requireNonNull(FetchPath.getPath(this, selected_camera_uri))));

            Log.d("file_name",attachmentFileList.get(0).getName());

            ImagePreviewModel model = new ImagePreviewModel();
            model.setUri(selected_camera_uri);
            model.setActive(false);
            multipleImageUri.add(model);

            previewImage();

        } /*else if (requestCode == Constants.SELECT_DOCUMENT_REQUEST_CODE) {

            isAnyFileAttached = true;

            attachment_request_code = Constants.SELECT_DOCUMENT_REQUEST_CODE;
            attachmentFileList = new ArrayList<>();

            if (data != null) {
                Uri sUri = data.getData();
                attachmentFileList.add(new File(Objects.requireNonNull(FetchPath.getPath(this,sUri))));


                callAttachmentApi();
            }

        }*/ else if (requestCode == Constants.SELECT_VIDEO_REQUEST_CODE && resultCode == RESULT_OK) {
            isAnyFileAttached = true;


            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {


                    TrimVideo.activity(String.valueOf(imageUri))
                            //.setCompressOption(new CompressOption()) //empty constructor for default compress option
                            .setTrimType(TrimType.DEFAULT)
                            .setHideSeekBar(true)
                            .start(this, startForResult);
                    /*startActivity(new Intent(this, VideoActivity.class)
                            .putExtra("videourl",attachmentFileList.get(0).getAbsolutePath())
                            .putExtra("start_time","0")
                            .putExtra("end_time","0"));*/
                } catch (Exception e) {
                    Log.v("Video Fetch Error", e.toString());
                }

                //Log.d("Video Attachment ",attachmentFileList.get(0).getAbsolutePath());
            }
        } else {
            if (requestCode == Constants.SELECT_PICTURE_REQUEST_CODE) {
                Toast.makeText(this, "You haven't picked any image", Toast.LENGTH_LONG).show();
            } else if (requestCode == Constants.SELECT_DOCUMENT_REQUEST_CODE) {
                Toast.makeText(this, "You haven't picked any document", Toast.LENGTH_LONG).show();
            } else if (requestCode == Constants.SELECT_VIDEO_REQUEST_CODE) {
                Toast.makeText(this, "You haven't picked any video", Toast.LENGTH_LONG).show();
            } else if (requestCode == Constants.CAPTURE_IMAGE_REQUEST_CODE) {
                Toast.makeText(this, "You haven't capture image", Toast.LENGTH_LONG).show();
            }

            attachment_request_code = 0;
        }
    }


    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @SuppressLint("WrongConstant")
                @Override
                public void onActivityResult(ActivityResult result) {

                    isAnyFileAttached = true;
                    attachment_request_code = Constants.SELECT_DOCUMENT_REQUEST_CODE;
                    attachmentFileList = new ArrayList<>();

                    Intent data = result.getData();

                    if (data != null) {
                        Uri sUri = data.getData();

                        binding.attachmentContainer.setVisibility(View.VISIBLE);

                        binding.sendMessage.setVisibility(View.VISIBLE);
                        binding.recordButton.setVisibility(View.GONE);

                        attachmentFileList.add(new File(copyFileToInternalStorage(sUri)));

                        //binding.attachmentTypeImage.setBackground(getResources().getDrawable(R.drawable.document));
                        //binding.attachmentTypeTitle.setText(attachmentFileList.get(0).getName());
                        //binding.attachmentContainer.setVisibility(View.VISIBLE);
                    }
                    else{
                        binding.attachmentContainer.setVisibility(View.GONE);
                    }
                }
            });


    private String copyFileToInternalStorage(Uri uri) {
        Uri returnUri = uri;

        Cursor returnCursor = getContentResolver().query(returnUri, new String[]{
                OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
        }, null, null, null);


        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));

        File output;
        if (!name.equals("")) {
            File dir = new File(getFilesDir() + "/" + name);
            if (!dir.exists()) {
                dir.mkdir();
            }
            output = new File(getFilesDir() + "/" + name + "/" + name);
        } else {
            output = new File(getFilesDir() + "/" + name);
        }
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(output);
            int read = 0;
            int bufferSize = 1024;
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }

            inputStream.close();
            outputStream.close();

        } catch (Exception e) {

            Log.e("Exception", e.getMessage());
        }

        return output.getPath();
    }


    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                attachment_request_code = Constants.SELECT_VIDEO_REQUEST_CODE;

                multipleImageUri = new ArrayList<>();
                attachmentFileList = new ArrayList<>();

                try {
                    if (result.getResultCode() == Activity.RESULT_OK &&
                            result.getData() != null) {
                        Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));
                        Log.v("DATAVIDEO", "uri: " + uri);
                        String videoPath = uri.getPath();
                        Log.v("DATAVIDEO", "Path: " + videoPath);

                        Uri videoUri = Uri.fromFile(new File(videoPath));
                        Log.v("DATAVIDEO", "videoUri: " + videoUri);

                        File video = new File(videoPath);

                        long fileSizeInBytes = video.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;

                        Log.v("VIDEOSIZE", "video size in KB:  " + fileSizeInKB);
                        Log.v("VIDEOSIZE", "video size in MB:  " + fileSizeInMB);

                        callPreviewVideo(video);

                        /*attachmentFileList.add(video);

                        binding.attachmentTypeImage.setBackground(getResources().getDrawable(R.drawable.autoplay_video));
                        binding.attachmentTypeTitle.setText(attachmentFileList.get(0).getName());
                        binding.attachmentContainer.setVisibility(View.VISIBLE);
*/

                        //responseType = "addVideo";
                        //campaignViewModel.AddVideo(accessToken, videoName, String.valueOf(videoSectionId), video, "1", isMobileRecorded);
                   /* Map<String,Object> header = new HashMap<>();
                    header.put("Content-Type","application/octet-stream;");
                    header.put("accessToken",accessToken);
                    campaignViewModel.DummyAddVideo(header,videoName, String.valueOf(videoSectionId),video,"1");*/
                    } else if (result.getResultCode() == 201) {
                        //getVideo();
                    }/*else if (result.getResultCode() == 201){
                        String videoPath = result.getData().getStringExtra("uri");
                        File video = new File(videoPath);
                        responseType = "addVideo";
                        campaignViewModel.AddVideo(accessToken, videoName, String.valueOf(videoSectionId), video, "1", isMobileRecorded);
                    }*/
                } catch (Exception e) {
                    Log.d("trim_exception",e.toString());
                }
            });


    private void callPreviewVideo(File savedVideoFile){
        Intent intent = new Intent(GroupChatScreen.this, VideoPreviewScreen.class);
        intent.putExtra("video",savedVideoFile);
        startForActivityResultLauncher.launch(intent);
    }


    // ---------------------------------------------------------------Interface Listeners ----------------------------------------------------------------

    @Override
    public void sendQuotedMessage(View view, String groupChatId, String oldMessage, String username, String time,int mediaCount,String previewUrl) {

        Log.d("Quote Listener Data ---", groupChatId + "     " + oldMessage + "     " + username + "     " + time);


        isMessageQuoted = true;

        if (mediaCount > 0 ){

            binding.quoteMediaContainer.setVisibility(View.VISIBLE);
            GlideUtils.loadImage(this,binding.quoteMediaPreview,previewUrl);

            if (mediaCount > 1){
                binding.quoteMediaCount.setVisibility(View.VISIBLE);
                binding.quoteMediaCount.setText("+ "+mediaCount);
            }
            else{
                binding.quoteMediaCount.setVisibility(View.GONE);
            }
        }
        else{
            binding.quoteMediaContainer.setVisibility(View.GONE);
        }

        binding.quoteContainer.setVisibility(View.VISIBLE);

        binding.sendMessage.setVisibility(View.VISIBLE);
        binding.recordButton.setVisibility(View.GONE);
        binding.pinAttachment.setVisibility(View.GONE);


        binding.oldMessage.setText(oldMessage);
        binding.oldMsgTime.setText(time);
        binding.oldMsgUser.setText(username);

        groupChatIDRef = groupChatId;

        //scrollToPosition(0);

        binding.type.requestFocus();
        //Utils.softKeyboard(GroupChatsScreen.this,true,type);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void likePost(int userID, int groupChannelId, int gcMemberID, int groupChatId, int like) {
        jsonRawObject = new JSONObject();
        likeGroupChatID = groupChatId;


        try {
            jsonRawObject.put("UserID", userID);
            jsonRawObject.put("GroupChannelID", groupChannelId);
            jsonRawObject.put("GCMemberID", gcMemberID);
            jsonRawObject.put("GroupChatID", groupChatId);
            jsonRawObject.put("isLike", like);

            Log.v("Like Message Params --", jsonRawObject.toString());
            Log.v("Like Message Params --", jsonRawObject.toString());

            socketInstance.emit("like", jsonRawObject);
        } catch (Exception e) {
            Log.d("Like Exception ------", e.toString());
        }
    }

    @Override
    public void likeAsEmote(int position, ImageView rootView) {
        /*Utils.showDialog(position, this, rootView, reactionModel, new SelectEmoteReaction() {
            @Override
            public void selectEmoteReaction(int id, String emoji_code, String emoji_name) {
                for (int i = 0; i < 15; i++) {
                    playAnimation(Utils.textAsBitmap(GroupChatScreen.this, emoji_code));
                }
            }
        });*/
    }


    private void playAnimation(Bitmap resID) {
        ZeroGravityAnimation animation = new ZeroGravityAnimation();
        animation.setCount(1);
        animation.setScalingFactor(0.2f);
        animation.setOriginationDirection(Directions.BOTTOM);
        animation.setDestinationDirection(Directions.TOP);
        animation.setImage(resID);
        animation.setAnimationListener(new Animation.AnimationListener() {
                                           @Override
                                           public void onAnimationStart(Animation animation) {

                                           }

                                           @Override
                                           public void onAnimationEnd(Animation animation) {

                                           }

                                           @Override
                                           public void onAnimationRepeat(Animation animation) {

                                           }
                                       }
        );

        ViewGroup container = findViewById(R.id.animation_holder);
        animation.play(this, container);
    }


    @Override
    public void deletePost(int userID, int gcMemberId, int groupChatId, int groupChannelId) {

        try {
            jsonRawObject = new JSONObject();
            jsonRawObject.put("GCMemberID", gcMemberId);
            jsonRawObject.put("GroupChannelID", channelID);
            jsonRawObject.put("GroupChatID", groupChatId);
            jsonRawObject.put("UserID", userID);


            socketInstance.emit("postDelete", jsonRawObject);
        } catch (Exception e) {
            Log.d("delete post exception", e.toString());
        }


// Todo.......................................................... Implement Delete Listener
/*
        isMessageNotFound = true;
        loadSearchData = false;
        isDataAvailable = true;
        searchedTillPosition = 0;
        rest.ShowDialogue();
        searchMessageInList(String.valueOf(groupChatId));*/
    }

    @Override
    public void commentMessage(int position, int userID, int gcMemberId, int groupChatId, int groupChannelId) {
        Log.d("Index_Position", "" + position);

        //mLayoutManager.findViewByPosition(position);

        binding.type.requestFocus();
        Utils.softKeyboard(this, true, binding.type);


       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollToPosition(position);
            }
        },300);*/

    }

    @Override
    public void downloadAudio(String audioPostUrl, String groupChannelID, String groupChatID, WaveformSeekBar seekBar, LottieAnimationView progressBar, ImageView downloadPlayPic) {

        new Thread() {
            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isDownloading = true;
                        progressBar.setAnimation(R.raw.round_loader);
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.playAnimation();

                        String downloadPath = "/" + "connect_audio_files" + "/" + groupChannelID + "/" + groupChatID + "/"
                                + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
                                + ".mp3";

                        String fileDownloadPath = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS) + downloadPath;


                        Log.d("LocalFilePath", fileDownloadPath);
                        Log.d("WebFilePath", audioPostUrl);

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(audioPostUrl));
                        request.setDescription("Downloading");
                        request.setMimeType("audio");
                        request.setTitle("File :");
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                downloadPath);
                        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);


                        DownloadManager.Query query = null;
                        query = new DownloadManager.Query();
                        Cursor c = null;
                        if (query != null) {
                            query.setFilterByStatus(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING);
                        } else {
                            //return flag;
                        }

                        while (isDownloading) {
                            c = manager.query(query);

                        /*int bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        if (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            c = false;
                        }*/

                            if (c.moveToFirst()) {
                                //Log.i("FLAG", "Downloading");
                                @SuppressLint("Range") int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

                                if (status == DownloadManager.STATUS_RUNNING) {
                                    @SuppressLint("Range") long totalBytes = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                    if (totalBytes > 0) {
                                        @SuppressLint("Range") long downloadedBytes = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                        final int progress = (int) ((downloadedBytes * 100L) / totalBytes);
                                        Log.d("download_status", "" + progress);
                                        progressBar.setProgress(progress);
                                    }


                                    //final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                                    //progressBar.setProgress(i);
                                }
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                    Log.i("FLAG", "done");
                                    isDownloading = false;


                                    progressBar.cancelAnimation();
                                    progressBar.setVisibility(View.GONE);
                                    groupViewAdapter.downloadComplete(groupChatID);
                              /*  long duration = AudioPlayUtil.getAudioDuration(fileDownloadPath);
                                runOnUiThread(() -> {
                                    downloadPlayPic.setImageDrawable(getResources().getDrawable(R.drawable.play));
                                    AudioPlayUtil.playAudioAnimation(GroupChatScreen.this, seekBar, duration);
                                });*/
                                    //groupViewAdapter.playAudio(fileDownloadPath);

                                }
                            }
                        }
                    }
                });

            }
        }.start();
    }

    @Override
    public void playAudio(String audioPostPath, WaveformSeekBar seekBar, long duration) {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            //here the play recording has been stop
            mediaPlayer.stop();
            mediaPlayer.release();
            AudioPlayUtil.stopPlayback(this);
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioPostPath);
            mediaPlayer.prepare();


        } catch (IOException e) {
            e.printStackTrace();
        }
        //play recording here
        AudioPlayUtil.playAudioAnimation(this, seekBar, duration);
        mediaPlayer.start();
    }

    @Override
    public void viewMemberProfile(int userID, int gcMemberId, int groupChatId, int groupChannelId) {

        Intent intent = new Intent(GroupChatScreen.this, MemberProfileScreen.class);
        intent.putExtra("gc_member_id",String.valueOf(gcMemberId));
        startActivity(intent);

    }

    @Override
    public void viewSelfProfile() {
        startActivity(new Intent(GroupChatScreen.this, UserProfileScreen.class));
    }

    @Override
    public void forwardMultiplePost(int selectedCount,int chatID,boolean isMessageSelected) {

        Integer groupChannelChatID = chatID;

        if (selectedCount <= 0){
            selectedForwardedMessageIDs = new ArrayList<>();
            binding.forwardContainer.setVisibility(View.GONE);
        }
        else{

            if (selectedCount == 1){
                selectedForwardedMessageIDs = new ArrayList<>();
            }

            if (isMessageSelected) {
                if (!selectedForwardedMessageIDs.contains(groupChannelChatID)) {
                    selectedForwardedMessageIDs.add(groupChannelChatID);
                }
            }
            else{
                selectedForwardedMessageIDs.remove(groupChannelChatID);
            }

            binding.forwardContainer.setVisibility(View.VISIBLE);
            binding.forwardCount.setText(String.valueOf(selectedCount));
        }
    }

    @Override
    public void toggleMultipleMessageSelection(boolean toggleSelection) {

        for (int i=0;i<list.size();i++){
            list.get(i).setShowPostSelection(toggleSelection);

            if (!toggleSelection){
                list.get(i).setPostSelected(false);
            }
        }
        groupViewAdapter.updateMultipleMessageSelection(list);
    }

    @Override
    public void saveMessage(int chatID) {


        Map<String,Object> params = new HashMap<>();
        params.put("GroupChatID[]",chatID);

        requestType = Constants.SAVE_MESSAGE_REQUEST_CODE;
        connectViewModel.save_group_channel_message(api_token,params);
    }

    @Override
    public void initiateCommentScreen(String data, String profileBaseUrl, String postBaseUrl, String userID,boolean isDiscussionAllowed) {

        Intent intent = new Intent(GroupChatScreen.this, CommentScreen.class);

        intent.putExtra("userDetail", data);
        intent.putExtra("profileBaseUrl",profileBaseUrl);
        intent.putExtra("postBaseUrl",postBaseUrl);
        intent.putExtra("userID", userID);
        intent.putExtra("isDiscussionAllowed",isDiscussionAllowed);
        startActivity(intent);
    }

    @Override
    public void imagePreviewListener(int index, ArrayList<ImagePreviewModel> uriList) {
        selectedImageUriIndex = index;
        binding.imagePreview.setImageURI(uriList.get(index).getUri());

    }

    @Override
    public void searchQuoteMessage(int index, String groupChatId) {

        isMessageNotFound = true;
        loadSearchData = false;
        isDataAvailable = true;

        searchedTillPosition = index;


        //rest.ShowDialogue();
        searchMessageInList(groupChatId, false, false, false);

    }

    @Override
    public void pinMessage(int gcMemberId, int GroupChannelId, int userId, int groupChatId) {
        Map<String, Object> param = new HashMap<>();
        param.put("GCMemberID", gcMemberId);
        param.put("GroupChannelID", GroupChannelId);
        param.put("UserID", String.valueOf(PreferenceConnector.readInteger(this, PreferenceConnector.CONNECT_USER_ID, 0)));
        param.put("GroupChatID", groupChatId);

        Log.d("Pin Message", " Params ----" + param.toString());

        requestType = REQUEST_PIN_MESSAGE;
        chatViewModel.pinMessage(param);
    }


    @Override
    public void updateDummyUser(int dummyUserId, int isAdmin) {

        Map<String,Object> params = new HashMap<>();
        params.put("DummyUserID",dummyUserId);
        params.put("IsAdmin",isAdmin);

        if (dummy_user_bottomSheet != null && dummy_user_bottomSheet.isShowing()){
            dummy_user_bottomSheet.dismiss();
        }

        requestType = UPDATE_DUMMY_USER;
        connectViewModel.update_dummy_user_list(channelID,PreferenceConnector.readString(this,PreferenceConnector.API_GTF_TOKEN_,""),params);
    }


    @Override
    public void onLoading() {
        if (requestType == Constants.SAVE_MESSAGE_REQUEST_CODE || requestType == REQUEST_UPLOAD_FILE || requestType == Constants.REJOIN_GROUP_CHANNEL){
            rest.ShowDialogue();
        }
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
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


    // ------------------------------------------------------------------Required Methods --------------------------------------------------------------------

    private void previewImage() {
        binding.pinAttachment.setVisibility(View.GONE);
        binding.sendMessage.setVisibility(View.VISIBLE);
        binding.recordButton.setVisibility(View.GONE);

        ImageMiniPreviewAdapter imageMiniPreviewAdapter = new ImageMiniPreviewAdapter(this, multipleImageUri, this);
        binding.miniImagePreviewRecycler.setHasFixedSize(true);
        binding.miniImagePreviewRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.miniImagePreviewRecycler.setAdapter(imageMiniPreviewAdapter);

        binding.close.setOnClickListener(view -> {
            binding.imagePreviewLayout.setVisibility(View.GONE);
            isAnyFileAttached = false;
        });

        binding.deleteSelectedImage.setOnClickListener(view -> {

            Log.d("Entered", " 2 true........................");

            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            if (multipleImageUri.size() == 1) {
                multipleImageUri.clear();
                Toast.makeText(this, "You haven't picked any image", Toast.LENGTH_LONG).show();
                imageMiniPreviewAdapter.updateList(multipleImageUri, 0);
                binding.imagePreviewLayout.setVisibility(View.GONE);

                toggleSearchContainer(1,1,2);

                isAnyFileAttached = false;
            } else {
                if (selectedImageUriIndex == 0) {
                    binding.imagePreview.setImageURI(multipleImageUri.get(selectedImageUriIndex + 1).getUri());
                    multipleImageUri.get(selectedImageUriIndex + 1).setActive(true);

                    multipleImageUri.remove(selectedImageUriIndex);
                    imageMiniPreviewAdapter.updateList(multipleImageUri, selectedImageUriIndex);
                } else {
                    binding.imagePreview.setImageURI(multipleImageUri.get(selectedImageUriIndex - 1).getUri());
                    multipleImageUri.get(selectedImageUriIndex - 1).setActive(true);

                    multipleImageUri.remove(selectedImageUriIndex);
                    imageMiniPreviewAdapter.updateList(multipleImageUri, selectedImageUriIndex - 1);
                }
            }
        });

        binding.imagePreviewLayout.setVisibility(View.VISIBLE);

    }


    private void showBottomSheetPopUp(String message) {
        Dialog pin_dialog = new Dialog(this);
        pin_dialog.setContentView(R.layout.dialog_pinned_message);
        pin_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pin_dialog.setCancelable(false);
        pin_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView text = pin_dialog.findViewById(R.id.title);
        text.setText(message);

        pin_dialog.show();


        requestType = PINNED_MESSAGE_COUNT;

        Map<String, Object> param = new HashMap<>();
        param.put("GroupChannelID", PreferenceConnector.readString(this, PreferenceConnector.GC_CHANNEL_ID, "0"));
        param.put("UserID", PreferenceConnector.readInteger(this, PreferenceConnector.CONNECT_USER_ID, 0));

        chatViewModel.getPinnedMessages(param);


        new Handler().postDelayed(pin_dialog::dismiss, 2000);
    }


    private void renderResponse(JsonObject jsonObject) {
        Gson gson = new Gson();

        if (requestType == REQUEST_PIN_MESSAGE) {
            pinMessageCount += 1;
            binding.pinnedMessageCountContainer.setVisibility(View.VISIBLE);
            binding.pinnedMessageCount.setText(String.valueOf(pinMessageCount));
            showBottomSheetPopUp(jsonObject.get("message").toString());

        } else if (requestType == REQUEST_EMOJI_LIST) {
            gson = new Gson();
            Type type = new TypeToken<ChannelManageReactionModel>() {
            }.getType();


            reactionModel = gson.fromJson(jsonObject, type);

               /* mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                adapter = new ManageReactionsListAdapter(this, reactionModel);
                binding.reactionsRecycler.setHasFixedSize(true);
                binding.reactionsRecycler.setLayoutManager(mLayoutManager);
                binding.reactionsRecycler.setAdapter(adapter);
                binding.reactionsRecycler.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                isDataLoadedFirstTime = false;*/

            init();

        }
        else if (requestType == GET_GROUP_CHANNEL_INFO) {
            gson = new Gson();
            Type type = new TypeToken<GroupChannelInfoResponseModel>() {
            }.getType();


            GroupChannelInfoResponseModel groupChannelInfoResponseModel = gson.fromJson(jsonObject, type);

            if (groupChannelInfoResponseModel != null && groupChannelInfoResponseModel.getData() != null) {
                InfoDbEntity data;
                data = groupChannelInfoResponseModel.getData();
                data.setGroupChannelID(channelID);

                databaseViewModel.insertGroupChannelInfo(data);
            }

            checkGroupChannelSettings();

            requestType = GET_DUMMY_USER;
            connectViewModel.get_dummy_user_list(channelID,api_token);

        } else if (requestType == GET_DUMMY_USER){


            getDummyUserModel = new GetDummyUserModel();
            Type type = new TypeToken<GetDummyUserModel>() {
            }.getType();

            getDummyUserModel = gson.fromJson(jsonObject,type);

            Log.d("dummy_user",jsonObject.toString());

            setDummyUserData();

            requestType = REQUEST_EMOJI_LIST;
            connectViewModel.get_group_channel_manage_reaction_list(channelID, api_token,  currentPage, 25, 1);
        }
        else if (requestType == REQUEST_UPLOAD_FILE) {


            toggleSearchContainer(1,1,2);

            Log.d("UPLOAD IMAGE", jsonObject.toString());

            SendAttachmentResponseModel sendAttachmentResponseModel = new SendAttachmentResponseModel();


            Type type = new TypeToken<SendAttachmentResponseModel>() {
            }.getType();

            sendAttachmentResponseModel = gson.fromJson(jsonObject, type);

            mediaIDs = "";
            for (int i = 0; i < sendAttachmentResponseModel.getData().size(); i++) {
                if (i == sendAttachmentResponseModel.getData().size() - 1) {
                    mediaIDs += sendAttachmentResponseModel.getData().get(i).getGroupChatMediaID().toString();
                    Log.d("Media IDs", "Single loop -----" + mediaIDs);
                } else {
                    mediaIDs += sendAttachmentResponseModel.getData().get(i).getGroupChatMediaID().toString() + ",";
                    Log.d("Media IDs", "Double loop -----" + mediaIDs);
                }
            }

            Log.d("Media IDs", mediaIDs);

            binding.imagePreviewLayout.setVisibility(View.GONE);
            binding.attachmentContainer.setVisibility(View.GONE);
            isAnyFileAttached = false;
            isAttachmentSend = true;


            if (messageText != null && !messageText.equalsIgnoreCase("")) {
                validateSendMessage(messageText, binding.type);
                messageText = null;
            } else {
                messageText = "";
                validateSendMessage(messageText, binding.type);
                messageText = null;
            }


        } else if (requestType == Constants.REJOIN_GROUP_CHANNEL) {

            Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();

            requestType = GET_GROUP_CHANNEL_INFO;
            connectViewModel.get_group_channel_info(channelID,PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, ""));


        } else if (requestType == PINNED_MESSAGE_COUNT) {
            Type type = new TypeToken<PinnedMessagesModel>() {
            }.getType();

            PinnedMessagesModel pinnedMessagesModel = gson.fromJson(jsonObject, type);

            if (pinnedMessagesModel!= null && pinnedMessagesModel.getData() != null && pinnedMessagesModel.getData().getAllPinData() != null && !pinnedMessagesModel.getData().getAllPinData().isEmpty()) {

                binding.pinMessageContainer.setVisibility(View.VISIBLE);
                binding.pinnedMessageCountContainer.setVisibility(View.VISIBLE);

                pinMessageCount = pinnedMessagesModel.getData().getAllPinData().size();
                binding.pinnedMessageCount.setText(String.valueOf(pinMessageCount));

                String username = "";

                if (pinnedMessagesModel.getData().getAllPinData().get(0).getChat() != null) {

                    if (pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMessage() != null && !pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMessage().trim().isEmpty()) {

                        binding.pinMediaContainer.setVisibility(View.GONE);

                        binding.lastPinMessage.setText(pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMessage());

                        if (pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getUser() != null) {
                            if (pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getUser().getFirstname() != null) {
                                username = pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getUser().getFirstname();
                            }
                            if (pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getUser().getLastname() != null) {
                                username += " " + pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getUser().getLastname();
                            }
                            binding.lastPinUser.setText(username);
                        }
                    } else {

                        if (pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMedia() != null && !pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMedia().isEmpty()){

                            binding.pinMediaContainer.setVisibility(View.VISIBLE);

                            if (pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMedia().get(0).getMimeType() != null){

                                if (Utils.checkFileType(pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMedia().get(0).getMimeType()).equalsIgnoreCase("image")){
                                    if (Utils.isFileTypeGif(pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMedia().get(0).getMimeType())){
                                        binding.lastPinMessage.setText("GIF");
                                        loadFile(pinnedMessagesModel,"gif");
                                    }
                                    else{
                                        binding.lastPinMessage.setText("IMAGE");
                                        loadFile(pinnedMessagesModel,"image");
                                    }
                                }
                                else if (Utils.checkFileType(pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMedia().get(0).getMimeType()).equalsIgnoreCase("video")){
                                    binding.lastPinMessage.setText("VIDEO");
                                    loadFile(pinnedMessagesModel,"video");
                                }
                                else{
                                    binding.lastPinMessage.setText("DOCUMENT");
                                    loadFile(pinnedMessagesModel,"document");
                                }
                            }

                            int mediaCount = pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMedia().size();

                            if (mediaCount > 1){
                                binding.pinMediaCount.setVisibility(View.VISIBLE);
                                binding.pinMediaCount.setText("+ "+mediaCount);

                            }
                            else{
                                binding.pinMediaCount.setVisibility(View.GONE);
                            }

                        }

                        if (pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getUser() != null) {
                            if (pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getUser().getFirstname() != null) {
                                username = pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getUser().getFirstname();
                            }
                            if (pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getUser().getLastname() != null) {
                                username += " " + pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getUser().getLastname();
                            }
                            binding.lastPinUser.setText(username);
                        }
                    }
                }

                Log.d("pin_message","messageCount = "+pinMessageCount);
            }
            else {
                Log.d("pin_message","messageCount = "+pinMessageCount);
                binding.pinMessageContainer.setVisibility(View.GONE);
                pinMessageCount = 0;
            }
        } else if (requestType == Constants.SAVE_MESSAGE_REQUEST_CODE) {
            Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
        } else if (requestType == UPDATE_DUMMY_USER) {
            Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
        }
    }




    private void loadFile(PinnedMessagesModel pinnedMessagesModel,String fileType){
        if (pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMedia().get(0).getStoragePath() != null &&
                pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMedia().get(0).getFileName() != null) {

            String imageUrl = postBaseUrl + pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMedia().get(0).getStoragePath() +
                    pinnedMessagesModel.getData().getAllPinData().get(0).getChat().getMedia().get(0).getFileName();

            Log.d("image_path_url",imageUrl);
            if (fileType.equalsIgnoreCase("image") || fileType.equalsIgnoreCase("gif")) {
                GlideUtils.loadImage(this, binding.pinMediaPreview, imageUrl);
            }
            else if (fileType.equalsIgnoreCase("video")) {
                // Todo : Add condition in case of Video
            }
            else{
                // Todo : Add condition in case of Document
            }
        }
    }




    private void destroyListeners() {
        // Todo : Remove all ON listeners of socket

        socketInstance.off("userTyping");
        socketInstance.off("messageReceived");
        socketInstance.off("likeUpdate");
        socketInstance.off("postDeleteData");



    }


    //--------------------------------------------------------------------- Android Lifecycle Checks --------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Lifecycle Check ", "In the onResume() event");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroyListeners();
        finish();
    }

    public void onStart() {
        super.onStart();
        Log.d("Lifecycle Check ", "In the onStart() event");
    }

    public void onRestart() {
        super.onRestart();
        Log.d("Lifecycle Check ", "In the onRestart() event");
    }

    public void onPause() {
        super.onPause();

        // Pausing video player if any video playing
        groupViewAdapter.pauseExoPlayer();
        Log.d("Lifecycle Check ", "In the onPause() event");
    }

    public void onStop() {
        super.onStop();
        Log.d("Lifecycle Check ", "In the onStop() event");

        //recyclerViewState = binding.chats.getLayoutManager().onSaveInstanceState();
    }

    public void onDestroy() {
        super.onDestroy();
        groupViewAdapter.destroyExoPlayer();
        Log.d("Lifecycle Check ", "In the onDestroy() event");
    }







    // -------------------------------------------------------------- Handling all update from Other Screens -----------------------------------------------------------

    ActivityResultLauncher<Intent> startForActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Constants.SHARE_GIF) {

                    attachment_request_code = Constants.SHARE_GIF;

                    if ((File)result.getData().getExtras().get("gif") != null) {
                        File pictureFile = (File) result.getData().getExtras().get("gif");

                        attachmentFileList = new ArrayList<>();
                        attachmentFileList.add(pictureFile);

                        isAnyFileAttached = true;

                        Log.d("GIF","gif found");
                        callAttachmentApi();
                    }
                    else{
                        Log.d("GIF","No gif found");
                    }


                }
                else if (result.getResultCode() == Constants.NO_GIF_FOUND) {
                    Utils.showSnackMessage(this,binding.getRoot(),"GIF not available");
                }
                else if (result.getResultCode() == Constants.SELECT_VIDEO_REQUEST_CODE) {

                    if ((File)result.getData().getExtras().get("video") != null) {
                        File videoFile = (File) result.getData().getExtras().get("video");

                        attachment_request_code = Constants.SELECT_VIDEO_REQUEST_CODE;

                        attachmentFileList = new ArrayList<>();
                        attachmentFileList.add(videoFile);

                        isAnyFileAttached = true;

                        Log.d("Video","video found");
                        callAttachmentApi();
                    }
                    else{
                        Log.d("Video","No Video found");
                    }

                }
                else if (result.getResultCode() == Constants.NO_VIDEO_FOUND) {
                    Utils.showSnackMessage(this,binding.getRoot(),"Video not available");
                }
                else if (result.getResultCode() == Constants.SEARCH_PINNED_MESSAGE) {

                    String groupChatId = result.getData().getStringExtra("groupChatId");
                    searchedTillPosition = 0;
                    isMessageNotFound = true;
                    loadSearchData = false;
                    isDataAvailable = true;

                    //rest.ShowDialogue();
                    searchMessageInList(groupChatId, false, false, false);
                }
            });



    private void checkGroupChannelSettings(){



        /**
         *  User setting checks
         */


        if (infoDbEntity.getGcPermission() != null){

            if (infoDbEntity.getGcPermission().getSendMessage() != null) {
                if (infoDbEntity.getGcPermission().getSendMessage() != 1) {

                    binding.footerStatusTag.setVisibility(View.VISIBLE);
                    binding.searchContainer.setVisibility(View.GONE);


                    binding.footerStatusTag.setBackgroundColor(getColor(R.color.sendMessageDialogBackgroundColor));
                    binding.footerStatusTag.setTextColor(getColor(R.color.authEditText));
                    binding.footerStatusTag.setText("Please contact admin.");

                    isSendMessageEnabled = false;
                } else {

                    isSendMessageEnabled = true;
                    binding.searchContainer.setVisibility(View.VISIBLE);

                }
            }
            else{

                isSendMessageEnabled = false;



                binding.footerStatusTag.setVisibility(View.GONE);
                binding.searchContainer.setVisibility(View.GONE);
            }


            if (infoDbEntity.getGcPermission().getSendMedia() != null){

                if (infoDbEntity.getGcPermission().getSendMedia() != 1){

                    isSendMediaEnabled = false;
                    toggleSearchContainer(2,2,1);
                }
                else{

                    isSendMediaEnabled = true;
                    toggleSearchContainer(1,1,2);
                }
            }
            else{

                isSendMediaEnabled = false;
                binding.pinAttachment.setVisibility(View.GONE);
            }


            if (infoDbEntity.getGcPermission().getSendStickerGIF() != null){

                if (infoDbEntity.getGcPermission().getSendStickerGIF() != 1){
                    toggleSendGif(false);
                }
                else{
                    toggleSendGif(true);
                }
            }
            else{
                toggleSendGif(false);
            }
        }








        /**
         *  USER Status and Subscription Plan Check
         */

        if(infoDbEntity.getGcMemberSubscriptionPlan() != null && infoDbEntity.getGcMemberSubscriptionPlan().getIsExpired() != null){

            if (infoDbEntity.getGcMemberSubscriptionPlan().getIsExpired() == 1){

                // Todo Redirection to Subscription Page

                userStatus = Constants.USER_RENEW_PLAN;

                isGcProfileEnabled = false;

                binding.searchSubContainer.setVisibility(View.GONE);
                binding.footerStatusTag.setVisibility(View.VISIBLE);

                binding.blurFrame.setVisibility(View.VISIBLE);
                binding.blurFrame.setBlur(this,binding.blurFrame);

                binding.footerStatusTag.setBackgroundColor(getColor(R.color.theme_green));
                binding.footerStatusTag.setTextColor(getColor(R.color.white));
                binding.footerStatusTag.setText("Renew Subscription Plan");

                binding.blurFrame.setVisibility(View.GONE);

            }
            else {

                if (infoDbEntity.getGcMemberInfo() != null) {
                    if (infoDbEntity.getGcMemberInfo().getStatus() != null) {

                        Log.d("user_status",infoDbEntity.getGcMemberInfo().getStatus());

                        if (infoDbEntity.getGcMemberInfo().getStatus().equalsIgnoreCase("blocked")) {

                            isGcProfileEnabled = true;

                            binding.searchSubContainer.setVisibility(View.GONE);
                            binding.footerStatusTag.setVisibility(View.VISIBLE);

                            binding.blurFrame.setVisibility(View.VISIBLE);
                            binding.blurFrame.setBlur(this,binding.blurFrame);

                            binding.footerStatusTag.setBackgroundColor(getColor(R.color.sendMessageDialogBackgroundColor));
                            binding.footerStatusTag.setTextColor(getColor(R.color.authEditText));
                            binding.footerStatusTag.setText("Please contact admin.");

                            userStatus = Constants.USER_BLOCKED;

                        } else if (infoDbEntity.getGcMemberInfo().getStatus().equalsIgnoreCase("active")) {

                            isGcProfileEnabled = true;

                            binding.searchSubContainer.setVisibility(View.VISIBLE);
                            binding.footerStatusTag.setVisibility(View.GONE);

                            binding.blurFrame.setVisibility(View.GONE);

                            userStatus = Constants.USER_ACTIVE;

                        } else {

                            isGcProfileEnabled = false;

                            binding.searchSubContainer.setVisibility(View.GONE);
                            binding.footerStatusTag.setVisibility(View.VISIBLE);
                            binding.blurFrame.setVisibility(View.VISIBLE);
                            binding.blurFrame.setBlur(this,binding.blurFrame);

                            binding.footerStatusTag.setBackgroundColor(getColor(R.color.theme_green));
                            binding.footerStatusTag.setTextColor(getColor(R.color.white));
                            binding.footerStatusTag.setText("Rejoin Channel");

                            userStatus = Constants.USER_LEFT;
                        }
                    }
                }
            }
        }
        else{
            if (infoDbEntity.getGcMemberInfo() != null) {
                if (infoDbEntity.getGcMemberInfo().getStatus() != null) {
                    if (infoDbEntity.getGcMemberInfo().getStatus().equalsIgnoreCase("blocked")) {

                        isGcProfileEnabled = true;

                        binding.searchSubContainer.setVisibility(View.GONE);
                        binding.footerStatusTag.setVisibility(View.VISIBLE);

                        binding.blurFrame.setVisibility(View.VISIBLE);
                        binding.blurFrame.setBlur(this,binding.blurFrame);

                        binding.footerStatusTag.setBackgroundColor(getColor(R.color.sendMessageDialogBackgroundColor));
                        binding.footerStatusTag.setTextColor(getColor(R.color.authEditText));
                        binding.footerStatusTag.setText("Please contact admin.");


                        userStatus = Constants.USER_BLOCKED;

                    } else if (infoDbEntity.getGcMemberInfo().getStatus().equalsIgnoreCase("active")) {

                        isGcProfileEnabled = true;

                        binding.searchSubContainer.setVisibility(View.VISIBLE);
                        binding.footerStatusTag.setVisibility(View.GONE);

                        binding.blurFrame.setVisibility(View.GONE);

                        userStatus = Constants.USER_ACTIVE;
                    } else {

                        isGcProfileEnabled = false;

                        binding.searchSubContainer.setVisibility(View.GONE);
                        binding.footerStatusTag.setVisibility(View.VISIBLE);

                        binding.blurFrame.setVisibility(View.VISIBLE);
                        binding.blurFrame.setBlur(this,binding.blurFrame);

                        binding.footerStatusTag.setBackgroundColor(getColor(R.color.theme_green));
                        binding.footerStatusTag.setTextColor(getColor(R.color.white));
                        binding.footerStatusTag.setText("Rejoin Channel");

                        userStatus = Constants.USER_LEFT;
                    }
                }
            }
        }





        if (PreferenceConnector.readString(this,PreferenceConnector.USER_TYPE,"").equalsIgnoreCase("super-admin")){

            binding.footerStatusTag.setVisibility(View.GONE);

            isGcProfileEnabled = true;
            isSendMediaEnabled = true;
            isSendMessageEnabled = true;

            binding.searchContainer.setVisibility(View.VISIBLE);
            binding.blurFrame.setVisibility(View.GONE);

            toggleSearchContainer(1,0,0);
            toggleSendGif(true);
        }
    }



    private void toggleSendGif(boolean enableSendGif){
        // Setting GIF | Image Insertion in Search EditText
        binding.type.setKeyBoardInputCallbackListener(new CustomEditText.KeyBoardInputCallbackListener() {
            @Override
            public void onCommitContent(InputContentInfoCompat inputContentInfo,
                                        int flags, Bundle opts) {

                if (enableSendGif) {
                    //you will get your gif/png/jpg here in inputContentInfo
                    // You can use a webView or ImageView to load the gif

                    Uri linkUri = inputContentInfo.getLinkUri();

                    Intent intent = new Intent(GroupChatScreen.this, GifPreviewScreen.class);
                    intent.putExtra("gif", linkUri.toString());
                    startForActivityResultLauncher.launch(intent);

                    //mWebView.loadUrl(linkUri != null ? linkUri.toString() : "null");

                }
                else{
                    Toast.makeText(GroupChatScreen.this, "Sharing Gif not supported!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void forwardSaveMessage(){

        /*int channelID = list.get(position).getGroupChannelID();
        int chatID = Integer.parseInt(list.get(position).getGroupChatID());*/

        int chatID = 0;

        Dialog forward_dialog = new Dialog(this);

        forward_dialog.setContentView(R.layout.dialog_forward_message);
        forward_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        forward_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        ImageView close = (ImageView) forward_dialog.findViewById(R.id.close);
        LinearLayout saveMessage = (LinearLayout) forward_dialog.findViewById(R.id.save_message);
        RecyclerView personList = (RecyclerView) forward_dialog.findViewById(R.id.forward_person_list_recycler);



        saveMessage.setOnClickListener(view -> {

            if (selectedForwardedMessageIDs != null && !selectedForwardedMessageIDs.isEmpty()){

                Map<String,Object> params = new HashMap<>();
                params.put("GroupChannelID",channelID);
                params.put("Action","WithGcChat");


                for (int i=0;i<selectedForwardedMessageIDs.size();i++){
                    params.put("GroupChatID["+i+"]",selectedForwardedMessageIDs.get(i));
                }


                Log.d("forwarded_message_params",params.toString());

                requestType = Constants.SAVE_MESSAGE_REQUEST_CODE;
                connectViewModel.save_group_channel_message(api_token,params);
            }
        });


        close.setOnClickListener(view1 -> {

            for (int i=0;i<list.size();i++){
                list.get(i).setShowPostSelection(false);
                list.get(i).setPostSelected(false);
            }

            groupViewAdapter.updateMultipleMessageSelection(list);
            binding.forwardContainer.setVisibility(View.GONE);


            forward_dialog.dismiss();
        });




        //int channelID = list.get(position).getGroupChannelID();
        //int chatID = Integer.parseInt(list.get(position).getGroupChatID());

        ForwardPersonListAdapter forwardPersonListAdapter = new ForwardPersonListAdapter(this,channelID,chatID);
        personList.setHasFixedSize(true);
        personList.setLayoutManager(new LinearLayoutManager(this));
        personList.setAdapter(forwardPersonListAdapter);

        forwardPersonListAdapter.setOnSaveMessageClickListener(chatID1 -> {
            //channelChatListener.saveMessage(chatID1);
            forward_dialog.dismiss();
        });

        forward_dialog.show();
    }




    private void checkEnabledPermissions(){

        permission_read_request_count = PreferenceConnector.readInteger(this,PreferenceConnector.READ_STORAGE_PERMISSION_COUNT,0);
        permission_write_request_count = PreferenceConnector.readInteger(this,PreferenceConnector.WRITE_STORAGE_PERMISSION_COUNT,0);
        permission_audio_request_count = PreferenceConnector.readInteger(this,PreferenceConnector.MICROPHONE_PERMISSION_COUNT,0);
        permission_camera_request_count = PreferenceConnector.readInteger(this,PreferenceConnector.CAMERA_PERMISSION_COUNT,0);


        if (PermissionCheckUtils.checkChatPermissions(this,Constants.REQUEST_ALL_MEDIA_PERMISSIONS)){
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO}, Constants.REQUEST_ALL_MEDIA_PERMISSIONS);
        }
    }
}
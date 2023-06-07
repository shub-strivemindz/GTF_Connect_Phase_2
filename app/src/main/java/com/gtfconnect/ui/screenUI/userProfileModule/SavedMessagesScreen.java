package com.gtfconnect.ui.screenUI.userProfileModule;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

import com.example.audiorecorder.OnRecordListener;
import com.example.audiowaveform.WaveformSeekBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gowtham.library.utils.TrimType;
import com.gowtham.library.utils.TrimVideo;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivitySavedMessageBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.AttachmentUploadListener;
import com.gtfconnect.interfaces.ImagePreviewListener;
import com.gtfconnect.interfaces.SavedMessageListener;
import com.gtfconnect.models.ImagePreviewModel;
import com.gtfconnect.models.ProfileResponseModel;
import com.gtfconnect.models.savedMessageModels.SavedMessageResponseModel;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.ui.adapters.ImageMiniPreviewAdapter;
import com.gtfconnect.ui.adapters.userProfileAdapter.SavedMessageAdapter;
import com.gtfconnect.ui.screenUI.HomeScreen;
import com.gtfconnect.ui.screenUI.authModule.LoginScreen;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.GifPreviewScreen;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.VideoPreviewScreen;
import com.gtfconnect.ui.screenUI.groupModule.GroupChatScreen;
import com.gtfconnect.ui.screenUI.userProfileModule.SavedMessagesScreen;
import com.gtfconnect.utilities.AttachmentUploadUtils;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.CustomEditText;
import com.gtfconnect.utilities.FetchPath;
import com.gtfconnect.utilities.PermissionCheckUtils;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SavedMessagesScreen extends AppCompatActivity implements ApiResponseListener , SavedMessageListener , ImagePreviewListener {

    ActivitySavedMessageBinding binding;

    private int requestType;


    private Rest rest;

    private ConnectViewModel connectViewModel;

    private ApiResponseListener listener;

    private DatabaseViewModel databaseViewModel;

    private int userID;

    private String api_token;

    private MediaPlayer mediaPlayer;

    private MediaRecorder mMediaRecorder;

    private long audioDuration = 0;

    private String postBaseUrl;

    private String audioFilePath;

    private ArrayList<File> attachmentFileList;

    private ArrayList<ImagePreviewModel> multipleImageUri;

    List<MultipartBody.Part> files;

    private boolean isAttachmentSend = false;

    private boolean isAnyFileAttached = false;

    private File selectedMedia;

    private Uri singleImageUri;
    private int attachment_request_code = 0;

    private long mLastClickTime;

    private int selectedImageUriIndex;

    private boolean isUserTyping = false;

    private int typeCount = 0;

    String messageText;

    private String userStatus = "";

    private int permission_read_request_count ;
    private int permission_write_request_count ;
    private int permission_audio_request_count ;
    private int permission_camera_request_count ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userID = PreferenceConnector.readInteger(this, PreferenceConnector.CONNECT_USER_ID, 0);
        api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");

        checkEnabledPermissions();

        init();
        sendMessageAndAudioRecorderEvents();


        // Navigate to Previous Screen :
        binding.back.setOnClickListener(view -> finish());


        binding.type.setKeyBoardInputCallbackListener(new CustomEditText.KeyBoardInputCallbackListener() {
            @Override
            public void onCommitContent(InputContentInfoCompat inputContentInfo,
                                        int flags, Bundle opts) {

                    Uri linkUri = inputContentInfo.getLinkUri();

                    Intent intent = new Intent(SavedMessagesScreen.this, GifPreviewScreen.class);
                    intent.putExtra("gif", linkUri.toString());
                    startForActivityResultLauncher.launch(intent);

                    //mWebView.loadUrl(linkUri != null ? linkUri.toString() : "null");

            }
        });


        binding.pinAttachment.setOnClickListener(view -> {
            openAttachmentDialog();
        });

        binding.closeAttachmentContainer.setOnClickListener(view -> {
            isAnyFileAttached = false;
            binding.attachmentContainer.setVisibility(View.GONE);
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

                    isUserTyping = false;
                } else {
                    isUserTyping = true;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.sendMessage.setOnClickListener(view -> validateSendMessage());
    }


    private void init() {

        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        rest = new Rest(this,false,false);
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



        requestType = Constants.GET_SAVED_MESSAGE;
        connectViewModel.get_saved_messages(api_token,20,1);
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
                validateSendMessage();

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

    private void validateSendMessage(){

        String message = "";

        Map<String, Object> params = new HashMap<>();

        if (isAnyFileAttached){
            addAttachments();
            message = binding.type.getText().toString().trim();

            if (!message.isEmpty()){
                params.put("Content",message);
            }
            params.put("Action","Manually");

            requestType = Constants.SAVE_MESSAGE_MANUALLY;
            connectViewModel.save_personal_message_with_attachment(api_token,params,files);
        }
        else{

            message = binding.type.toString().trim();
            files = new ArrayList<>();

            if (!message.isEmpty()){
                params.put("Content",message);
                params.put("Action","Manually");

                requestType = Constants.SAVE_MESSAGE_MANUALLY;
                connectViewModel.save_personal_message(api_token,params);
            }
            else{
                Utils.showSnackMessage(this, binding.type, "Type Message !");
            }
        }
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
                PreferenceConnector.writeInteger(SavedMessagesScreen.this,PreferenceConnector.CAMERA_PERMISSION_COUNT,permission_camera_request_count);
            }
            if (grantResults[1] != PackageManager.PERMISSION_GRANTED){
                permission_write_request_count ++;
                PreferenceConnector.writeInteger(SavedMessagesScreen.this,PreferenceConnector.WRITE_STORAGE_PERMISSION_COUNT,permission_write_request_count);
            }
            if (grantResults[2] != PackageManager.PERMISSION_GRANTED){
                permission_read_request_count ++;
                PreferenceConnector.writeInteger(SavedMessagesScreen.this,PreferenceConnector.READ_STORAGE_PERMISSION_COUNT,permission_read_request_count);
            }
            if (grantResults[3] != PackageManager.PERMISSION_GRANTED){
                permission_audio_request_count ++;
                PreferenceConnector.writeInteger(SavedMessagesScreen.this,PreferenceConnector.MICROPHONE_PERMISSION_COUNT,permission_audio_request_count);
            }

        } else if (requestCode == Constants.REQUEST_ATTACHMENT_MEDIA_PERMISSIONS) {


            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                openAttachmentDialog();
            } else {

                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permission_camera_request_count++;
                    PreferenceConnector.writeInteger(SavedMessagesScreen.this, PreferenceConnector.CAMERA_PERMISSION_COUNT, permission_camera_request_count);
                }
                if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    permission_write_request_count++;
                    PreferenceConnector.writeInteger(SavedMessagesScreen.this, PreferenceConnector.WRITE_STORAGE_PERMISSION_COUNT, permission_write_request_count);
                }
                if (grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    permission_read_request_count++;
                    PreferenceConnector.writeInteger(SavedMessagesScreen.this, PreferenceConnector.READ_STORAGE_PERMISSION_COUNT, permission_read_request_count);
                }
                Toast.makeText(this, "Please grant required permissions!", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == Constants.REQUEST_AUDIO_PERMISSIONS) {

            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permission_audio_request_count++;
                    PreferenceConnector.writeInteger(SavedMessagesScreen.this, PreferenceConnector.CAMERA_PERMISSION_COUNT, permission_audio_request_count);
                }
                if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    permission_write_request_count++;
                    PreferenceConnector.writeInteger(SavedMessagesScreen.this, PreferenceConnector.WRITE_STORAGE_PERMISSION_COUNT, permission_write_request_count);
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
        Intent intent = new Intent(SavedMessagesScreen.this, VideoPreviewScreen.class);
        intent.putExtra("video",savedVideoFile);
        startForActivityResultLauncher.launch(intent);
    }



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

                binding.pinAttachment.setVisibility(View.VISIBLE);
                binding.sendMessage.setVisibility(View.GONE);
                binding.recordButton.setVisibility(View.VISIBLE);

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






    // -------------------------------------------------------------- Api Calls -----------------------------------------------------------------------------

    private void addAttachments() {

        files = new ArrayList<>();

        if (attachment_request_code == Constants.CAPTURE_IMAGE_REQUEST_CODE) {

            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("image/*"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("File[]", attachmentFileList.get(0).getName(), part);
            files.add(attachment);
        } else if (attachment_request_code == Constants.SELECT_VIDEO_REQUEST_CODE) {

            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("video/*"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("File[]", attachmentFileList.get(0).getName(), part);
            files.add(attachment);
        } else if (attachment_request_code == Constants.SELECT_DOCUMENT_REQUEST_CODE) {

            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("document/*"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("File[]", attachmentFileList.get(0).getName(), part);
            files.add(attachment);

        } else if (attachment_request_code == Constants.SELECT_PICTURE_REQUEST_CODE) {
            for (int i = 0; i < attachmentFileList.size(); i++) {
                RequestBody part =
                        RequestBody.create(
                                MediaType.parse("image/*"),
                                attachmentFileList.get(i)
                        );

                MultipartBody.Part attachment = MultipartBody.Part.createFormData("File[]", attachmentFileList.get(i).getName(), part);
                files.add(attachment);
            }
        } else if (attachment_request_code == Constants.RECORD_AUDIO_REQUEST_CODE) {
            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("audio/webm;codecs=opus"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("File[]", attachmentFileList.get(0).getName(), part);
            files.add(attachment);
        } else if (attachment_request_code == Constants.SHARE_GIF) {
            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("image/gif"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("File[]", attachmentFileList.get(0).getName(), part);
            files.add(attachment);
        }


        /*attachment_request_code = 0;
        requestType = REQUEST_UPLOAD_FILE;
        chatViewModel.attachFile(params, files);*/

    }




    @Override
    public void onLoading() {
         rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderResponse(jsonObject);

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


    private void renderResponse(JsonObject jsonObject) {

        if (requestType == Constants.GET_SAVED_MESSAGE) {

            Gson gson = new Gson();
            Type type = new TypeToken<SavedMessageResponseModel>() {
            }.getType();


            SavedMessageResponseModel savedMessageResponseModel = gson.fromJson(jsonObject, type);

            if (savedMessageResponseModel != null && savedMessageResponseModel.getData() != null) {

                if (savedMessageResponseModel.getData().getBaseUrl() != null){
                    postBaseUrl = savedMessageResponseModel.getData().getBaseUrl();
                }
                else{
                    postBaseUrl = "";
                }

                if ( savedMessageResponseModel.getData().getList() != null && !savedMessageResponseModel.getData().getList().isEmpty()) {

                    // load saved messages  ------
                    SavedMessageAdapter savedMessageViewAdapter = new SavedMessageAdapter(this, savedMessageResponseModel.getData().getList(), String.valueOf(userID), postBaseUrl, this);
                    binding.savedMessageRecycler.setHasFixedSize(true);
                    binding.savedMessageRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
                    binding.savedMessageRecycler.setAdapter(savedMessageViewAdapter);

                }

            }
        } else if (requestType == Constants.DELETE_SAVED_MESSAGE) {
            Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();

            requestType = Constants.GET_SAVED_MESSAGE;
            connectViewModel.get_saved_messages(api_token,20,1);
        }
        else if (requestType == Constants.SAVE_MESSAGE_MANUALLY){

            Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();

            requestType = Constants.GET_SAVED_MESSAGE;
            connectViewModel.get_saved_messages(api_token,20,1);
        }
    }

    @Override
    public void deleteSavedPost(int messageID) {
        requestType = Constants.DELETE_SAVED_MESSAGE;
        connectViewModel.delete_saved_message(messageID,api_token);
    }

    @Override
    public void playAudio(String path, WaveformSeekBar seekBar) {


        // initializing media player
        mediaPlayer = new MediaPlayer();

        // below line is use to set the audio
        // stream type for our media player.
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(path);
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare();
            mediaPlayer.start();



        } catch (IOException e) {
            e.printStackTrace();
        }
        // below line is use to display a toast message.
        //Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void pauseAudio(String path, WaveformSeekBar seekBar) {

        // checking the media player
        // if the audio is playing or not.


        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            // pausing the media player if media player
            // is playing we are calling below line to
            // stop our media player.
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

            // below line is to display a message
            // when media player is paused.
            //Toast.makeText(this, "Audio has been paused", Toast.LENGTH_SHORT).show();
        } else {
            // this method is called when media
            // player is not playing.
            Toast.makeText(this, "Audio has not played", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void imagePreviewListener(int index, ArrayList<ImagePreviewModel> uriList) {
        selectedImageUriIndex = index;
        binding.imagePreview.setImageURI(uriList.get(index).getUri());
    }







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
                        validateSendMessage();
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
                        validateSendMessage();
                    }
                    else{
                        Log.d("Video","No Video found");
                    }

                }
                else if (result.getResultCode() == Constants.NO_VIDEO_FOUND) {
                    Utils.showSnackMessage(this,binding.getRoot(),"Video not available");
                }
            });
}

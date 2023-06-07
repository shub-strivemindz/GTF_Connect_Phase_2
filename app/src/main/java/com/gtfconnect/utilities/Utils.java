package com.gtfconnect.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.gtfconnect.R;
import com.gtfconnect.interfaces.PaymentWebPortalListener;
import com.gtfconnect.interfaces.ReportReasonListener;
import com.gtfconnect.interfaces.SelectCityListener;
import com.gtfconnect.interfaces.SelectCountryListener;
import com.gtfconnect.interfaces.SelectEmoteReaction;
import com.gtfconnect.interfaces.SelectStateListener;
import com.gtfconnect.models.authResponseModels.CityData;
import com.gtfconnect.models.authResponseModels.CountryData;
import com.gtfconnect.models.authResponseModels.StateData;
import com.gtfconnect.models.channelResponseModel.ChannelManageReactionModel;
import com.gtfconnect.models.commonGroupChannelResponseModels.MemberReportReasonResponseModel;
import com.gtfconnect.services.InternetService;
import com.gtfconnect.ui.adapters.EmojiReactionListAdapter;
import com.gtfconnect.ui.adapters.authModuleAdapter.CityListAdapter;
import com.gtfconnect.ui.adapters.authModuleAdapter.CountryListAdapter;
import com.gtfconnect.ui.adapters.commonGroupChannelAdapters.MemberReportReasonListAdapter;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class Utils {

    private static boolean isSearchFocused = false;

    private static  BroadcastReceiver internetReceiver;


    static int reasonID;
    static String reasonTitle;
    static String reasonCode;


    public static void showSnackMessage(Context context, View view, String message) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Snackbar snackbar1 = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar1.show();
    }




    public static Uri getImageUri(Context inContext, Bitmap inImage, float maxWidth, float maxHeight) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + " - " + Calendar.getInstance().getTime()+"temp", null);
        String file_path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), compressImage(String.valueOf(Uri.parse(path)), inContext, maxWidth, maxHeight), "Title" + " - " + Calendar.getInstance().getTime(), null);

        return Uri.parse(file_path);
    }
    private static Bitmap compressImage(String imageUri, Context mContext, float maxWidth, float maxHeight) {

        Log.v("ImageFile","imageUri :" + imageUri);
        String filePath = getRealPathFromURI(imageUri, mContext);
        Log.v("ImageFile","filePath :" + filePath);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        //float maxHeight = 1024.0f; //800.0f
        //float maxWidth = 768.0f;//800.0f
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File fdelete = new File(filePath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.v("ImageFile","file Deleted :" + filePath);
            } else {
                Log.v("ImageFile","file not Deleted :" + filePath);
            }
        }
        return scaledBitmap;

    }
    private static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "EYS/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");

    }

    public static String getAudioFilePath() {

        String videoFilePath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/" + "connect_audio" + "_"
                + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
                + ".MP4";

        return videoFilePath;
    }


    public static boolean deleteAudioFile(String filePath){
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
            Log.d("File","Deleted");
            return true;
        }
        else{
            Log.d("File","Not Deleted");
            return false;
        }
    }


    public static String getRealPathFromURI(String contentURI, Context mContext) {
        Uri contentUri = Uri.parse(contentURI);
        @SuppressLint("Recycle") Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static Bitmap decodeFile(File f) {

        Bitmap b = null;

        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            if (o.outHeight > 800 || o.outWidth > 800) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(800 /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b;
    }


    public static String checkFileType(String mimeType)
    {
        String arr[] = mimeType.split("/", 2);

        String fileType = arr[0];
        String theRest = arr[1];

        return fileType;
    }



    public static boolean isFileTypeGif(String mimeType){

        String arr[] = mimeType.split("/", 2);

        String fileType = arr[0];
        String theRest = arr[1];

        return theRest.equalsIgnoreCase("gif");
    }




    public static Bitmap textAsBitmap(Context context,String text) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(280);
        paint.setColor(context.getColor(R.color.white));
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }





    // ======================================================= Can be used to for slide pop up from side edges of view ============================
    public static PopupWindow showDialog(int position, Context context, RelativeLayout reactionView, ChannelManageReactionModel emojiListModel, SelectEmoteReaction listener) { //, EditUserDialogListener editUserDialogListener
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_window, null);
            PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(R.style.my_popup_style);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.update(0, 0, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            int[] location = new int[2];
            reactionView.getLocationOnScreen(location);
            popupWindow.showAtLocation(reactionView, Gravity.NO_GRAVITY, location[0], location[1] - 160);

            View container = (View) popupWindow.getContentView().getParent();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
            p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = 0.3f;
            wm.updateViewLayout(container, p);

            RecyclerView reactionRecycler = layout.findViewById(R.id.reaction_recycler);

            EmojiReactionListAdapter emojiReactionListAdapter = new EmojiReactionListAdapter(context,emojiListModel);
            reactionRecycler.setHasFixedSize(true);
            reactionRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            reactionRecycler.setAdapter(emojiReactionListAdapter);

            emojiReactionListAdapter.setOnRecyclerViewItemClickListener((id, emoji_code,emoji_name) -> {
                listener.selectEmoteReaction(id,emoji_code,emoji_name);
                popupWindow.dismiss();
            });


            return popupWindow;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }













    private static boolean checkUiNightMode(Context context)
    {
        switch (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                return true;
            case Configuration.UI_MODE_NIGHT_NO:
            default:
                return false;
        }
    }











/*    public static PopupWindow showDialog(int position, Activity context, ImageView chatView, EmojiListModel emojiListModel){

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the custom popup layout
        View inflatedView = layoutInflater.inflate(R.layout.popup_window, null,false);
        // find the ListView in the popup layout
        //ListView listView = (ListView)inflatedView.findViewById(R.id.commentsListView);

        //LinearLayout headerView = (LinearLayout)inflatedView.findViewById(R.id.headerLayout);
        // get device size
        Display display = context.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
//        mDeviceHeight = size.y;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;


        // fill the data to the list items
        //setSimpleList(listView);


        // set height depends on the device size
        PopupWindow popWindow = new PopupWindow(inflatedView, width,height-50, true );
        // set a background drawable with rounders corners
        //popWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_bg));

        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popWindow.setAnimationStyle(R.style.my_popup_style);

        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(chatView, Gravity.BOTTOM, 0,100);

        return popWindow;
    }*/


    public static void setEmoji(String unicode,TextView view)
    {
        view.setText(String.valueOf(Character.toChars(Integer.parseInt(unicode.substring(2), 16))));
    }




    public static void Show_Select_Country_list(final Context context, final List<CountryData> arrayList, final SelectCountryListener listener)
        {

            Dialog place_selection_dialog = new Dialog(context);

            place_selection_dialog.setContentView(R.layout.bottomsheet_place_selector);
            place_selection_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            place_selection_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /*BottomSheetDialog place_selection_dialog = new BottomSheetDialog(RegisterScreen3.this);
        place_selection_dialog.setContentView(R.layout.bottomsheet_place_selector);*/

            RecyclerView placeRecycler = place_selection_dialog.findViewById(R.id.place_recycler);
            EditText title = place_selection_dialog.findViewById(R.id.title);
            ImageView close_icon = place_selection_dialog.findViewById(R.id.close_icon);

            CountryListAdapter countryListAdapter= new CountryListAdapter(context,arrayList);
            placeRecycler.setHasFixedSize(true);
            placeRecycler.setLayoutManager(new LinearLayoutManager(context));
            placeRecycler.setAdapter(countryListAdapter);

            countryListAdapter.setOnRecyclerViewItemClickListener((position, item_name,phoneCode) -> {
                place_selection_dialog.dismiss();
                listener.Select_value(position, item_name,phoneCode);

            });

            close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSearchFocused)
                        place_selection_dialog.dismiss();
                }
            });


            title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b)
                    {
                        close_icon.setImageResource(R.drawable.close);
                        isSearchFocused = true;
                    }
                    else {
                        close_icon.setImageResource(R.drawable.search);
                        isSearchFocused = false;
                    }

                }
            });

            title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    List<CountryData> temp = new ArrayList();
                    for (CountryData country : arrayList) {
                        if (country.getCountryName().toLowerCase().contains(charSequence.toString())) {
                            temp.add(country);
                        }
                    }
                    if (temp.size() == 0) {
                        //text_no_data_found.setVisibility(View.VISIBLE);
                    } else {
                        //text_no_data_found.setVisibility(View.GONE);
                    }
                    countryListAdapter.updateList(temp);

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            place_selection_dialog.show();

        }



    public static void Show_Select_State_list(final Context context, final List<StateData> arrayList, final SelectStateListener listener) {
        {

            Dialog place_selection_dialog = new Dialog(context);

            place_selection_dialog.setContentView(R.layout.bottomsheet_place_selector);
            place_selection_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            place_selection_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /*BottomSheetDialog place_selection_dialog = new BottomSheetDialog(RegisterScreen3.this);
        place_selection_dialog.setContentView(R.layout.bottomsheet_place_selector);*/



            place_selection_dialog.show();

        }
    }



    public static void Show_Select_City_list(final Context context, final List<CityData> arrayList, final SelectCityListener listener) {
        {

            Dialog place_selection_dialog = new Dialog(context);

            place_selection_dialog.setContentView(R.layout.bottomsheet_place_selector);
            place_selection_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            place_selection_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /*BottomSheetDialog place_selection_dialog = new BottomSheetDialog(RegisterScreen3.this);
        place_selection_dialog.setContentView(R.layout.bottomsheet_place_selector);*/

            RecyclerView placeRecycler = place_selection_dialog.findViewById(R.id.place_recycler);
            EditText title = place_selection_dialog.findViewById(R.id.title);
            ImageView close_icon = place_selection_dialog.findViewById(R.id.close_icon);

            CityListAdapter cityListAdapter= new CityListAdapter(context,arrayList);
            placeRecycler.setHasFixedSize(true);
            placeRecycler.setLayoutManager(new LinearLayoutManager(context));
            placeRecycler.setAdapter(cityListAdapter);


            close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSearchFocused)
                        place_selection_dialog.dismiss();
                }
            });

            cityListAdapter.setOnRecyclerViewItemClickListener((id,item_name) -> {
                place_selection_dialog.dismiss();
                listener.Select_value(id,item_name);
            });


            title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b)
                    {
                        close_icon.setImageResource(R.drawable.close);
                        isSearchFocused = true;
                    }
                    else {
                        close_icon.setImageResource(R.drawable.search);
                        isSearchFocused = false;
                    }

                }
            });

            title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    List<CityData> temp = new ArrayList();
                    for (CityData city : arrayList) {
                        if (city.getCityName().toLowerCase().contains(charSequence.toString())) {
                            temp.add(city);
                        }
                    }
                    if (temp.size() == 0) {
                        //text_no_data_found.setVisibility(View.VISIBLE);
                    } else {
                        //text_no_data_found.setVisibility(View.GONE);
                    }
                    cityListAdapter.updateList(temp);

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            place_selection_dialog.show();

        }
    }




    public static void ShowReportReasonList(final Context context, final List<MemberReportReasonResponseModel.ReasonList> arrayList, final ReportReasonListener listener) {
        {
            reasonID = 0;
            reasonTitle = "";
            reasonCode = "";


            BottomSheetDialog report_user_bottomSheet = new BottomSheetDialog(context);

            report_user_bottomSheet.setContentView(R.layout.bottomsheet_report_user_options);
            report_user_bottomSheet.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            report_user_bottomSheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            MaterialCardView report_user =  report_user_bottomSheet.findViewById(R.id.report_user);
            ImageView back = report_user_bottomSheet.findViewById(R.id.back);

            back.setOnClickListener(view1 -> report_user_bottomSheet.dismiss());

            RecyclerView placeRecycler = report_user_bottomSheet.findViewById(R.id.reason_list_recycler);

            MemberReportReasonListAdapter memberReportReasonListAdapter= new MemberReportReasonListAdapter(context,arrayList);
            placeRecycler.setHasFixedSize(true);
            placeRecycler.setLayoutManager(new LinearLayoutManager(context));
            placeRecycler.setAdapter(memberReportReasonListAdapter);


            report_user.setOnClickListener(view1 -> {

                if (memberReportReasonListAdapter.isReasonSelected()){
                    listener.SelectReason(reasonID,reasonTitle,reasonCode);
                    report_user_bottomSheet.dismiss();
                }
                else{
                    Toast.makeText(context, "No reason selected!", Toast.LENGTH_SHORT).show();
                }

            });

            memberReportReasonListAdapter.setOnRecyclerViewItemClickListener((id,reason,code) -> {
                reasonID = id;
                reasonTitle = reason;
                reasonCode = code;
            });


            report_user_bottomSheet.show();

        }
    }





    public static boolean checkEmail_validation(String target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static String getTime(String dateString){

        String datetime = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");

        SimpleDateFormat d = new SimpleDateFormat("HH:mm a");
        try {

            Date convertedDate = inputFormat.parse(dateString);

            Calendar cal = Calendar.getInstance();
            cal.setTime(convertedDate);
            cal.add(Calendar.HOUR, 7);
            cal.add(Calendar.MINUTE,30);
            Date calculatedTime = cal.getTime();



            datetime = d.format(calculatedTime);

        } catch (ParseException e) {

        }
        return datetime;

    }



    public static String getDashboardGreeting()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String currentTime = sdf.format(new Date());

        int hour = Integer.parseInt(currentTime);

        if (hour >= 0 && hour < 12){
            return "Good Morning!";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon!";
        }
        else{
            return "Good Evening!";
        }
    }


    public static String getHeaderDate(String chatDate) {
        String tempChatDate = chatDate;
        String serverCurrentDate = "";
        String currentDate = "";

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(chatDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, hh:mm aa"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            chatDate = dateFormatter.format(value);


            //current date
            String OurDate = chatDate;
            String[] separated = OurDate.split(",");
            serverCurrentDate = separated[0];
            String ServerCurrentDate1 = separated[1];
            currentDate = new SimpleDateFormat("dd MMM", Locale.getDefault()).format(new Date());
            yesterdayDate();
            if (currentDate.equalsIgnoreCase(serverCurrentDate)) {
                chatDate = getTime(tempChatDate);
            } else if (serverCurrentDate.equalsIgnoreCase(yesterdayDate())) {
                chatDate = "Yesterday";
            }
            else{
                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM");
                chatDate = format.format(value);
            }
        } catch (Exception e) {
            chatDate = "00-00-0000 00:00";
        }
        return chatDate;
    }

    public static String getChatBoxTimeStamp(String chatDate ){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(chatDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm aa"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            chatDate = dateFormatter.format(value);

        } catch (Exception e) {
            chatDate = "00-00-0000 00:00";
        }
        return chatDate;
    }


    public static String getSavedMessageChipDate(String chipDate){
        String tempChatDate = chipDate;
        String serverCurrentDate = "";
        String currentDate = "";

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(chipDate);

            /*SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, hh:mm aa"); //this format changeable
            //dateFormatter.setTimeZone(TimeZone.getDefault());
            chipDate = dateFormatter.format(value);*/

            SimpleDateFormat format = new SimpleDateFormat("dd MMM, hh:mm aa");
            chipDate = format.format(value);


         /*   //current date
            String OurDate = chipDate;
            String[] separated = OurDate.split(",");
            serverCurrentDate = separated[0];
            String ServerCurrentDate1 = separated[1];
            currentDate = new SimpleDateFormat("dd MMM", Locale.getDefault()).format(new Date());
            yesterdayDate();
            if (currentDate.equalsIgnoreCase(serverCurrentDate)) {
                chipDate = "Today";
            } else if (serverCurrentDate.equalsIgnoreCase(yesterdayDate())) {
                chipDate = "Yesterday";
            }
            else{
                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM");
                chipDate = format.format(value);
            }*/
        } catch (Exception e) {
            chipDate = "00-00-0000 00:00";
        }
        return chipDate;
    }


    public static String getChipDate(String chipDate){
        String tempChatDate = chipDate;
        String serverCurrentDate = "";
        String currentDate = "";

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(chipDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, hh:mm aa"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            chipDate = dateFormatter.format(value);


            //current date
            String OurDate = chipDate;
            String[] separated = OurDate.split(",");
            serverCurrentDate = separated[0];
            String ServerCurrentDate1 = separated[1];
            currentDate = new SimpleDateFormat("dd MMM", Locale.getDefault()).format(new Date());
            yesterdayDate();
            if (currentDate.equalsIgnoreCase(serverCurrentDate)) {
                chipDate = "Today";
            } else if (serverCurrentDate.equalsIgnoreCase(yesterdayDate())) {
                chipDate = "Yesterday";
            }
            else{
                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM");
                chipDate = format.format(value);
            }
        } catch (Exception e) {
            chipDate = "00-00-0000 00:00";
        }
        return chipDate;
    }


    private static String yesterdayDate() {

        DateFormat dateFormat = new SimpleDateFormat("dd MMM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesDate = dateFormat.format(cal.getTime());
        return yesDate;
    }

    /*private static int compareDateTime(String dateTime)
    {

        Date systemDate = new Date();
        Date systemTime = new Date();
        Date chatDate= new Date();
        Date chatTime= new Date();

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

        String currentDate = dateFormatter.format(todayDate);
        String currentTime = timeFormatter.format(todayDate);



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");

        try {
            // Setting System Date & Time
            systemDate = dateFormatter.parse(currentDate);
            systemTime  = dateFormatter.parse(currentTime);



            // Setting Chat Date & Time
            String currentDateTime = sdf.format(time);
            chatDate = dateFormatter.parse(currentDateTime);
        }
        catch (Exception e){
            Log.v("Exception ","on converting chat date");
        }

        if (systemDate.compareTo(chatDate) == 0){
            // Current Date & Chat Date ======= Same
            return 0;
        } else if (systemDate.compareTo(chatDate) > 0) {
            // Current Date is after Chat Date
            return 1;
        }
        else{
            // Current Date is before Chat Date
            return 2;
        }
    }*/


    private static Date subtractHoursToTime(Date date,int hours)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }


    public static void softKeyboard(Activity activity,boolean showSoftKeyboard,EditText editText) {
        View view = activity.getCurrentFocus();
        editText.requestFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

            if (showSoftKeyboard){

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.SHOW_IMPLICIT);


            }
            else {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }


    public static void checkInternetConnection(Context context,InternetService.ReceiverListener listener) {


            // initialize intent filter
            IntentFilter intentFilter = new IntentFilter();

            // add action
            intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

            // register receiver
            context.registerReceiver(new InternetService(), intentFilter);

            // Initialize listener
            InternetService.Listener = listener;

            // Initialize connectivity manager
            ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            // Initialize network info
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            // get connection status
            boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

            Log.d("network status",String.valueOf(isConnected));

    }



    /*public static String getDisplayableTime(String dateString) {

        long time = 0;
        String msgDate = "";

        try {
            //String dateString = "30/09/2014";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
            Date date = sdf.parse(dateString);

            // ---------------------------------------------------------Can be Changed as per Condition : Subtracting 6 hours from time -----------------------

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR, 6);
            cal.add(Calendar.MINUTE,30);
            Date calculatedTime = cal.getTime();

            // --------------------------------------------------------------------------------------------------------------------------------------------

            msgDate = DateFormat.getDateInstance().format(calculatedTime);
            Log.v("getDisplayableDate",String.valueOf(msgDate));


            time = calculatedTime.getTime();
            Log.v("getDisplayableTime",String.valueOf(time));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference=0;
        Long currentTime = java.lang.System.currentTimeMillis();

        if(currentTime > time)
        {
            difference= currentTime - time;
            final long seconds = difference/1000;
            final long minutes = seconds/60;
            final long hours = minutes/60;
            final long days = hours/24;
            final long months = days/31;
            final long years = days/365;

            if (seconds < 0)
            {
                return "not yet";
            }
            else if (seconds < 60)
            {
                return seconds == 1 ? "a second ago" : seconds + " seconds ago";
            }
            else if (seconds < 120)
            {
                return "a minute ago";
            }
            else if (seconds < 2700) // 45 * 60
            {
                return minutes + " minutes ago";
            }
            else if (seconds < 5400) // 90 * 60
            {
                return "an hour ago";
            }
            else if (seconds < 86400) // 24  60  60
            {
                return hours + " hours ago";
            }
            else if (seconds < 172800) // 48  60  60
            {
                return "yesterday";
            }
            *//*else if (seconds < 2592000) // 30  24  60 * 60
            {
                return days + " days ago";
            }
            else if (seconds < 31104000) // 12  30  24  60  60
            {

                return months <= 1 ? "one month ago" : days + " months ago";
            }*//*
            else
            {
                return msgDate;
            }
        }
        return null;
    }*/


    public static void registerInternetReceiver(Context context)
    {
        internetReceiver = new InternetService();
        context.registerReceiver(internetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static void unregisterInternetReceiver(Context context)
    {
        context.unregisterReceiver(internetReceiver);
    }



    public static String getUserInitials(String firstName,String lastName){

        String title = "";

        if (firstName != null && !firstName.isEmpty()) {
            title = "" + firstName.charAt(0);
        }
        if (lastName != null && !lastName.isEmpty()) {
            title += "" + lastName.charAt(0);
        }


            /*String[] separated_name = name.split(" ");

            int wordCount = separated_name.length;
            Log.d("userInitialData ","name = "+name+" count = "+wordCount);

            if (wordCount > 1){
                String last_name = separated_name[wordCount-1];
                title += ""+last_name.charAt(0);
            }*/
        return title.toUpperCase();
    }



























    public static String getValidityDuration(int duration){

        String validity = "";


        if (duration <= 3600) {

            if (duration/30 > 12){

                validity = ((duration/30)/12)+" Years";
            }
            else if (duration/30 == 12){
                validity = "1 Year";
            } else if (duration/30 == 9) {
                validity = "9 Months";
            }
            else if (duration/30 == 6) {
                validity = "6 Months";
            }
            else if (duration/30 == 3) {
                validity = "3 Months";
            }
            else if (duration/30 == 1) {
                validity = "1 Month";
            }
            else{
                Log.d("term_duration_validity",duration+ " days");
                validity = duration/30+" Months";
            }
        } else {
            validity = "Unlimited";
        }







        if (duration >= 28 && duration <= 31){
            validity = "1 Month";
        }
        else if (duration >= 28 && duration <= 31){

        }
        else if (duration >= 28 && duration <= 31){

        }

        return validity;
    }










    public static void convertSecondsToHours(String slowModeDuration){

        int hour;
        int min;
        int sec ;


        long duration = Long.parseLong(slowModeDuration);

        // Calculating total hours =====================
        long hourValue = (long) duration % 3600;
        hour = (int) duration / 3600;


        Log.d("convertSecondsToHours","slowModeDuration = "+duration);
        Log.d("convertSecondsToHours","hourValue = "+hourValue);
        Log.d("convertSecondsToHours","hour = "+hour);



        // Calculating total minutes =====================
        min = (int) hourValue / 60;


        // Calculating total minutes =====================
        sec = (int) hourValue % 60;

        Log.d("convertSecondsToHours","min = "+min);
        Log.d("convertSecondsToHours","sec = "+sec);

        slowModeDuration = "";

        if (hour <= 0){
            slowModeDuration = "00";
        } else if (hour < 10) {
            slowModeDuration = "0"+hour;
        }
        else{
            slowModeDuration = String.valueOf(hour);
        }

        slowModeDuration += ":";


        if (min <= 0){
            slowModeDuration += "00";
        } else if (min < 10) {
            slowModeDuration += "0"+min;
        }
        else{
            slowModeDuration += String.valueOf(min);
        }


        slowModeDuration += ":";


        if (sec <= 0){
            slowModeDuration += "00";
        } else if (sec < 10) {
            slowModeDuration += "0"+sec;
        }
        else{
            slowModeDuration += String.valueOf(sec);
        }


        Log.d("convertSecondsToHours","time = "+slowModeDuration);


    }
















    public static void ShowDialogSuccess(final Context context, String text, final PaymentWebPortalListener listener) {
        {
            Dialog dialog = new Dialog(Objects.requireNonNull(context));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.payment_success_dialog);
            LottieAnimationView animationView =dialog.findViewById(R.id.lav_actionBar);
            CardView ok_button =dialog.findViewById(R.id.ok_button);
            TextView dialoge_text =dialog.findViewById(R.id.dialoge_text);
            dialoge_text.setText(text);
            animationView.setAnimation(R.raw.payment_success);
            animationView.playAnimation();
            ok_button.setOnClickListener(v -> {
                listener.OkClick();
                animationView.cancelAnimation();
                dialog.dismiss();
            });
            Window window = dialog.getWindow();
            WindowManager.LayoutParams layoutParams = Objects.requireNonNull(window).getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }





}

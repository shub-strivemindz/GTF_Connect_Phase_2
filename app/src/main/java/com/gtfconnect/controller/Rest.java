package com.gtfconnect.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.gtfconnect.R;

import java.util.Objects;

import retrofit2.Callback;


public class Rest {

    public boolean isAuthApi = false;

    public boolean isAuthProfileApi = false;
    public Context ctx;
    public Dialog dialog;
    Callback callback;
    RestService restService;
    public LottieAnimationView animationView;


    public Rest(Context context,boolean isAuthCallback,boolean isAuthProfileCallback) {
        ctx = context;
        isAuthApi = isAuthCallback;
        isAuthProfileApi = isAuthProfileCallback;
        init();
    }

    public Rest(Context ctx, Callback callback) {
        this.callback = callback;
        this.ctx = ctx;
        init();
    }



    public static void printLog(String msg) {
        Log.e("hommzi", msg);
    }



    private void init() {
        dialog = new Dialog(ctx);
        animationView = new LottieAnimationView(ctx);
        dialog.setCancelable(false);

        if (isAuthApi) {
            restService = RestAdapter.getAuthAdapter(ctx);
            //Toast.makeText(ctx, "Auth Implementation", Toast.LENGTH_SHORT).show();
            isAuthApi = false;
        } else if (isAuthProfileApi) {
            restService = RestAdapter.getAuthProfileAdapter(ctx);
            //Toast.makeText(ctx, "Profile Url Implementation", Toast.LENGTH_SHORT).show();
            isAuthProfileApi = false;
        } else
            restService = RestAdapter.getChatAdapter(ctx);
    }

    public boolean isInterentAvaliable() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }

    public void AlertForInternet() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        alert.setMessage("Internet Not available");
        alert.setPositiveButton("Retry", (dialogInterface, i) -> dismissProgressdialog());
        alert.setNegativeButton("Cancel", (dialogInterface, i) -> dismissProgressdialog());
        alert.show();
    }

    public void ShowDialogue() {
        Log.v("DialogShow","dialog");


        dialog.setContentView(R.layout.loading_item);
        LottieAnimationView animationView = dialog.findViewById(R.id.loading_animation);
        animationView.setAnimation(R.raw.round_loader);
        animationView.playAnimation();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = Objects.requireNonNull(window).getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
    }

    public void dismissProgressdialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                Log.v("DialogShow","DISMISS");
                animationView.cancelAnimation();
                dialog.dismiss();
            }
        } catch (Exception e) {
            animationView.cancelAnimation();
            dialog.dismiss();
            Rest.printLog("" + e);
        }
    }





}
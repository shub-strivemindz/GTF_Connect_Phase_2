package com.gtfconnect.ui.screenUI.paymentModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;

import com.gtfconnect.R;
import com.gtfconnect.controller.ApiUrls;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.interfaces.PaymentWebPortalListener;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentWebPortal extends Activity implements Callback<Object> {

    private WebView web_visitWeb;
    private Rest rest;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_payment_portal);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        overridePendingTransition(R.anim.payment_trans_left_in, R.anim.payment_trans_left_out);
        rest = new Rest(this, this);
        initView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initView() {
        web_visitWeb = findViewById(R.id.web_visitWeb);

        ImageView img_haderBack = findViewById(R.id.img_haderBack);

        /*String userId = PreferenceConnector.readString(this, PreferenceConnector.KEY_USER_ID, "");
        String url = ApiUrls.Payment_URL + userId;*/

        WebSettings settings = web_visitWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        web_visitWeb.getSettings().setSupportMultipleWindows(true);

        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(web_visitWeb, true);

        //web_visitWeb.loadUrl(url);
        web_visitWeb.setWebChromeClient(new WebChromeClient());
        img_haderBack.setOnClickListener(v -> onBackPressed());
        rest.ShowDialogue();
        web_visitWeb.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {


            }

            @Override
            public void onPageFinished(WebView view, String url) {

                try {

                    Log.e("url>>>>>", "" + url);
                    if (url.contains("?payment_status=success")) {

                        Uri uri = Uri.parse(url);
                        String reward ="";

                        if(uri.getQueryParameter("reward_points")!=null) {
                            reward = uri.getQueryParameter("reward_points");
                        }
                        rest.dismissProgressdialog();
                        if(reward.equals("0") || reward.isEmpty()) {
                            Utils.ShowDialogSuccess(PaymentWebPortal.this, "Payment Successfully Done", new PaymentWebPortalListener() {
                                @Override
                                public void OkClick() {


                                    /*Intent intent = new Intent(PaymentWebPortal.this, MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();*/
                                }
                            });
                        }else
                        {
                            Utils.ShowDialogSuccess(PaymentWebPortal.this, "Congratulations!!You've won " + reward + "  reward points. You can redeem it on your next purchase.", new PaymentWebPortalListener() {
                                @Override
                                public void OkClick() {

                                    /*Intent intent = new Intent(Web_View_Page.this, MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();*/
                                }
                            });
                        }
                    }
                    else if (!url.contains("?pending_webhook=1")) {
                        rest.dismissProgressdialog();

                    }

                } catch (Exception exception) {
                    rest.dismissProgressdialog();
                    exception.printStackTrace();
                }


            }


        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int[] scrcoords = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.payment_trans_right_in, R.anim.payment_trans_right_out);
    }

    @Override
    public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {

    }

    @Override
    public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {

    }

    @Override
    protected void onDestroy() {
        rest.dismissProgressdialog();
        super.onDestroy();
    }
}



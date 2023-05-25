package com.gtfconnect.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings;

import java.util.Map;

public class PreferenceConnector {

    public static final String GTF_USER_ID= "AUTH_USER_ID";
    public static final String CONNECT_USER_ID= "CONNECT_USER_ID";
    private static final String PREF_NAME = "GTF_CONNECT";
    private static final int MODE = Context.MODE_PRIVATE;

    public static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";

    public static final String KEY_IS_USERINFO = "KEY_IS_USER_INFO";

    public static final String IS_USER_LOGGED = "USER_LOGGED_IN";

    public static final String API_GTF_TOKEN_ = "API_TOKEN_0";

    public static final String API_CONNECT_TOKEN = "API_TOKEN_1";

    public static final String FIRST_NAME = "first_name";

    public static final String LAST_NAME = "last_name";

    public static final String EMAIL_ID = "email_id";

    public static final String REGISTRATION_ONE_TIME_EMAIL = "registration_email";




    // Group OR Channel Data History values ----
    public static final String CHANNEL_CHAT_DATA = "channel_chat_data";

    public static final String GROUP_CHAT_DATA = "group_chat_data";


    public static final String DASHBOARD_DATA = "dashboard_data";




    // Group OR Channel Key values ----
    public static final String GC_MEMBER_ID = "group_member_id";
    public static final String GC_CHANNEL_ID = "group_channel_id";
    public static final String GC_NAME = "group_name";

    public static final String IS_EXCLUSIVE_REFRESHED = "is_exclusive_offer_refreshed";


    public static final String MEDIA_LIST = "mediaList";


    public static final String TOTAL_UNREAD_NOTIFICATION_COUNT = "total_unread_notification_count";








    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }


    public static void writeString(Context context, String key, String string) {
        getEditor(context).putString(key, string).commit();
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    private static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void cleanPreferences(Context context) {
        getPreferences(context).edit().clear().apply();
    }

    public static Map<String, ?> getAllKeys(Context context) {
        return getPreferences(context).getAll();
    }
   /* public static void logout(Context context)
    {
        PreferenceConnector.cleanPrefrences(context);
        context.startActivity(new Intent(context,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static Map<String,Object> logoutParams(Context context) {
        Map<String, Object> map = new HashMap<>();
        map.put("DeviceToken", PreferenceConnector.readString(context, PreferenceConnector.DEVICE_TOKEN, ""));
        map.put("DeviceType", "android");
        map.put("userID", readString(context, MAINUSER_ID,""));
        map.put("api_token", PreferenceConnector.readString(context, PreferenceConnector.KEY_API_TOKEN, ""));
        Log.d("asldkflksdnflnsdal", "logoutParams: "+map);
        return map;

    }*/


    public static String getDeviceId(Context context)
    {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}

package com.ptplanner.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by ltp on 08/07/15.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();



    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    public static SharedPreferences slot;

    public static SharedPreferences preferCheckAppState;
    public static SharedPreferences.Editor edit;
    public String isAppRunning;


    public static SharedPreferences preferCheckAppStateNotificationState;
    public static SharedPreferences.Editor editNotificationState;
    public String isAppRunningNotificationState;

    public static SharedPreferences preferCheckAppStateNotificationStateChat;
    public static SharedPreferences.Editor editNotificationStateChat;
    public String isAppRunningNotificationStateChat;

    public static SharedPreferences preferGroupCheckAppState;
    public static SharedPreferences.Editor editGroup;
    public String isAppRunningGroup;

    public static SharedPreferences checkNotificationSetting;
    public static SharedPreferences.Editor editNotification;
    public String isNotificationON;

    public static SharedPreferences checkSoundSetting;
    public static SharedPreferences.Editor editSound;
    public String isSoundON;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this;

        slot = getSharedPreferences("slotValue", Context.MODE_PRIVATE);

        SharedPreferences.Editor editorslot =  AppController.slot.edit();
        editorslot.clear();
        editorslot.putString("value","");
        editorslot.commit();

        preferCheckAppState = getSharedPreferences("CHECK_APP_STATE", Context.MODE_PRIVATE);
        preferGroupCheckAppState = getSharedPreferences("CHECK_CHAT_ROOM_STATE", Context.MODE_PRIVATE);
        checkNotificationSetting = getSharedPreferences("CHECK_NOTIFICATION_SETTING", Context.MODE_PRIVATE);
        checkSoundSetting = getSharedPreferences("CHECK_SOUND_SETTING", Context.MODE_PRIVATE);
        preferCheckAppStateNotificationState = getSharedPreferences("NOTIFICATION_STATE", Context.MODE_PRIVATE);
        preferCheckAppStateNotificationStateChat = getSharedPreferences("NOTIFICATION_STATE", Context.MODE_PRIVATE);
    }

    // -- Sound ---
    public static String isAlermSet() {
        return checkSoundSetting.getString("SoundState", "");
    }

    public static void setIsAlermSet(String isSoundON) {
        editSound = checkSoundSetting.edit();
        editSound.putString("SoundState", isSoundON);
        editSound.commit();
    }
    // -- End ---


    /*
    @@ Koushik
    Implements for Luncher Icon Batch Count
     */



    public static void K_SetLuncherAndNotificationCount(int no){
        editNotification = checkNotificationSetting.edit();
        editNotification.putInt("NotificationNo", no);
        editNotification.commit();
    }

    public static int K_getNotificationCount()
    {
        return checkNotificationSetting.getInt("NotificationNo", 0);
    }


    // -- Notification State ---




    public static String isNotificationState() {
        return preferCheckAppStateNotificationState.getString("NotificationShow", "");
    }

    public static String isNotificationStateChat() {
        return preferCheckAppStateNotificationStateChat.getString("NotificationShowChat", "");
    }

    public static void setIsNotificationStateChat(String isNotificationState) {
        editNotificationStateChat = preferCheckAppStateNotificationStateChat.edit();
        editNotificationStateChat.putString("NotificationShowChat", isNotificationState);
        editNotificationStateChat.commit();
    }

    public static void setIsNotificationState(String isNotificationState) {
        editNotificationState = preferCheckAppStateNotificationState.edit();
        editNotificationState.putString("NotificationShow", isNotificationState);
        editNotificationState.commit();
    }
    // -- End ---

    // -- Notification ---
    public static String isNotificationON() {
        return checkNotificationSetting.getString("NotificationState", "");
    }

    public static void setIsNotificationON(String isNotificationON) {
        editNotification = checkNotificationSetting.edit();
        editNotification.putString("NotificationState", isNotificationON);
        editNotification.commit();
    }
    // -- End ---

    // -- Chat Room ---
    public static String isAppRunningGroup() {
        return preferGroupCheckAppState.getString("AppGroupChatState", "");
    }

    public static void setIsAppRunningGroup(String isAppRunningGroup) {
        editGroup = preferGroupCheckAppState.edit();
        editGroup.putString("AppGroupChatState", isAppRunningGroup);
        editGroup.commit();
    }
    // -- End ---

    // -- Single Chat ---
    public static String isAppRunning() {
        return preferCheckAppState.getString("AppState", "");
    }

    public static void setIsAppRunning(String isAppRunning) {
        edit = preferCheckAppState.edit();
        edit.putString("AppState", isAppRunning);
        edit.commit();
    }
    // -- End ---

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


//    @@KOUSHIK for LOGOUT for Acount Deactivatiion
    public static boolean Acdeactivation=false;
    public static String DeactivationAlert="";
}
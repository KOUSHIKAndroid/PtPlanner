package com.ptplanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ptplanner.datatype.LoginDataType;
import com.ptplanner.fragment.ChatDetailsFragment;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.security.AccessController.getContext;

public class SplashActivity extends Activity {

    SharedPreferences loginPreferences;
    ConnectionDetector cd;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    static final String TAG = "GCMRegistration";
    String SENDER_ID = "302080859614";
    String regid = "", msg = "";
    GoogleCloudMessaging gcm;


    SharedPreferences dateSavePreference, dateSaveCalendarPopUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_splash);
        /////////////////////////////////////////////////////






        setContentView(R.layout.activity_splash);

        cd = new ConnectionDetector(SplashActivity.this);

        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if (cd.isConnectingToInternet()) {
            gcm = GoogleCloudMessaging.getInstance(SplashActivity.this);
            regid = getRegistrationId(SplashActivity.this);

            if (regid.isEmpty()) {
                registerInBackground();
            } else {
//                Log.d("Reg", "ID Shared Pref => " + regid);
                AppConfig.appRegId = regid;
                sendRegistrationIdToBackend();
            }
        }else {
            AppConfig.loginDatatype = new LoginDataType(
                    loginPreferences.getString("UserId", ""),
                    loginPreferences.getString("Username", ""),
                    loginPreferences.getString("Password", ""));
            if (!AppConfig.loginDatatype.getSiteUserId().equals("")) {
                Intent intent = new Intent(SplashActivity.this, LandScreenActivity.class);
                startActivity(intent);
                finish();
            }
        }
        dateSavePreference = getSharedPreferences("DateTimeDiary", Context.MODE_PRIVATE);
        dateSaveCalendarPopUP = getSharedPreferences("DateTime", Context.MODE_PRIVATE);

        getDynamicUrl();
    }

    private void registerInBackground() {
        (new getGCMID()).execute();
    }

    class getGCMID extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(SplashActivity.this);
                }
                regid = gcm.register(SENDER_ID);
                msg = regid;
            } catch (IOException ex) {
                msg = "";
            }
//            Log.d("GCM", msg);
//            Log.d("GCM--------", regid);
            return null;
        }

        protected void onPostExecute(Void resultt) {
            super.onPostExecute(resultt);
            if (msg.equals("")) {

            } else {
                sendRegistrationIdToBackend();
            }

        }
    }


    private void sendRegistrationIdToBackend() {
        // Your implementation here.
//        Log.d("In", "########My own Method ######");
//        Log.v("MY", "Registration ID: " + regid);
        AppConfig.appRegId = regid;
        SavePreferences("DEVICE_TOKEN", regid);
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            // Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            //  Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(SplashActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    public void getDynamicUrl(){
        new AsyncTask<Void, Void, Void>() {
            private String respose = "";
            private String url = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                url = "http://www.pt-planner.com/app_control/fetch_app_domain";
//                url = "http://esolz.co.in/lab6/ptplanner/app_control/fetch_app_domain";
                url = AppConfig.HOST+"app_control/fetch_app_domain";

                Log.i("url", "" + url);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {
                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(6000, TimeUnit.MILLISECONDS).build();
                        Request request = new Request.Builder().url(url).build();
                        Response response = client.newCall(request).execute();
                        respose = response.body().string();
                        Log.i("response", "respose_::" + respose);
                        Log.i("response", "respose_ww_message::" + response.message());
                        Log.i("response", "respose_ww_headers::" + response.headers());
                        Log.i("response", "respose_ww_isRedirect::" + response.isRedirect());
                        Log.i("response", "respose_ww_body::" + respose);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled()) {
                    try {
                        if(respose!=null){
                            JSONObject jsonObject = new JSONObject(respose);
                            AppConfig.HOST=jsonObject.getString("app_domain");
                            Log.i("app_domain_from_net",jsonObject.getString("app_domain"));
                        }
                        else {
                            Log.i("app_domain_from_static",AppConfig.HOST);
                        }


                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                try {

                                    SharedPreferences.Editor editorDiary = dateSavePreference.edit();
                                    editorDiary.clear();
                                    editorDiary.commit();

                                    SharedPreferences.Editor editorCalendorPopup = dateSaveCalendarPopUP.edit();
                                    editorCalendorPopup.clear();
                                    editorCalendorPopup.commit();

                                    if (loginPreferences.getString("Remember", "").equals("remember")) {
                                        //  AppController.setIsNotificationState("NO"); //noti
                                        try {
                                            if (getIntent().getStringExtra("notiSentBy") == null) {
                                                AppConfig.loginDatatype = new LoginDataType(
                                                        loginPreferences.getString("UserId", ""),
                                                        loginPreferences.getString("Username", ""),
                                                        loginPreferences.getString("Password", ""));
                                                Log.d("CALL","LANDSCREEN");
                                                Intent intent = new Intent(SplashActivity.this, LandScreenActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                AppConfig.loginDatatype = new LoginDataType(
                                                        loginPreferences.getString("UserId", ""),
                                                        loginPreferences.getString("Username", ""),
                                                        loginPreferences.getString("Password", ""));
                                                Intent intent = new Intent(SplashActivity.this, ChatDetailsFragment.class);
                                                intent.putExtra("notiSentBy", getIntent().getStringExtra("notiSentBy"));
                                                intent.putExtra("notiSentTo", getIntent().getStringExtra("notiSentTo"));
                                                intent.putExtra("notiSenderName", getIntent().getStringExtra("notiSenderName"));
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        } catch (Exception e) {
                                            AppConfig.loginDatatype = new LoginDataType(
                                                    loginPreferences.getString("UserId", ""),
                                                    loginPreferences.getString("Username", ""),
                                                    loginPreferences.getString("Password", ""));
                                            Intent intent = new Intent(SplashActivity.this, LandScreenActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else {

                                        SharedPreferences.Editor editor = loginPreferences.edit();
                                        editor.clear();
                                        editor.commit();
                                        Log.d("CALL", "LOGIN");
                                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 1000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}

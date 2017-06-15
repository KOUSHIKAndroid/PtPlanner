package com.ptplanner;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ptplanner.adapter.Progress_ViewPgr_Adapter;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.CalenderFragmentDatatype;
import com.ptplanner.datatype.LoginDataType;
import com.ptplanner.datatype.ZoomCurrentImageDataType;
import com.ptplanner.fragment.AllGraphFragment;
import com.ptplanner.fragment.AppointmantFragment;
import com.ptplanner.fragment.AppointmentListFragment;
import com.ptplanner.fragment.BookAppointmentFragment;
import com.ptplanner.fragment.CalenderFragment;
import com.ptplanner.fragment.ChatDetailsFragment;
import com.ptplanner.fragment.DiaryFragment;
import com.ptplanner.fragment.DietFragment;
import com.ptplanner.fragment.Messagefragment;
import com.ptplanner.fragment.ProfileFragment;
import com.ptplanner.fragment.ProgressFragment;
import com.ptplanner.fragment.TrainingFragment;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.AppController;
import com.ptplanner.helper.BadgeUtils;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LandScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FrameLayout fragContainer;
    TitilliumRegular TXT_Logout,TXT_Privacy;
    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton, llBottomBar;
    RelativeLayout llMessagebutton, rllandcontainer;
    Dialog logoutChooser;
    LinearLayout llYes, llNo;
    TextView txtMSGCount, txtCal, txtApnt, txtPrg, txtMsg;
    ImageView imgCal, imgApnt, imgPrg, imgMsg;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    String getIntentValue = "", getIntentValueMSG = "";
    View VPage;
    CalenderFragmentDatatype calDtype;

    public String PUSHER_API_KEY = "676b2632a600d73a2182";
    public String PUSHER_CHANNEL = "test_channel";
    public String PUSHER_EVENT = "my_event";

    LinearLayout  transparentView;
    TextView llMsgcount;
    boolean isMSGCountVisible = false;
    Messagefragment msg_fragment;

    public Pusher mPusher;
    String sendTo = "", sendBy = "", msg = "", dateTime = "";

    AlarmManager am;
    String stringSave, stringRecieve;
    LoginDataType loginDataType;
    //end

//    @@KOUSHIK

    boolean Ac_status = true;
    String logoutExcep = "";
    SharedPreferences sharedPreferences;
    SharedPreferences dateSavePreference, dateSaveCalendarPopUP;
    SharedPreferences loginPreferences;
    String UserID = "";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);

//@@ KOUSHIIK
        mRegistrationBroadcastReceiver = new LandBroadcastReceiver();
        sharedPreferences = getSharedPreferences("DateTime", Context.MODE_PRIVATE);
        dateSavePreference = getSharedPreferences("DateTimeDiary", Context.MODE_PRIVATE);
        dateSaveCalendarPopUP = getSharedPreferences("DateTime", Context.MODE_PRIVATE);
        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);


        //////////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.landingscreen_main);
        /////////////////////////////////////////////////////

        transparentView = (LinearLayout) findViewById(R.id.inVisible);
        fragContainer = (FrameLayout) findViewById(R.id.fragment_container);
        llBottomBar = (LinearLayout) findViewById(R.id.landpage_layout_bottom);
        rllandcontainer = (RelativeLayout) findViewById(R.id.landpage_holder_content);


        llCalenderButton = (LinearLayout) findViewById(R.id.calenderbutton);
        llBlockAppoinmentButton = (LinearLayout) findViewById(R.id.blockappoinmentbutton);
        llProgressButton = (LinearLayout) findViewById(R.id.progressbutton);
        llMessagebutton = (RelativeLayout) findViewById(R.id.messagebutton);

        txtCal = (TextView) findViewById(R.id.txt_cal);
        txtApnt = (TextView) findViewById(R.id.txt_apnt);
        txtPrg = (TextView) findViewById(R.id.txt_prg);
        txtMsg = (TextView) findViewById(R.id.txt_msg);

        imgCal = (ImageView) findViewById(R.id.img_cal);
        imgApnt = (ImageView) findViewById(R.id.img_apnt);
        imgPrg = (ImageView) findViewById(R.id.img_prg);
        imgMsg = (ImageView) findViewById(R.id.img_msg);

        llMsgcount = (TextView) findViewById(R.id.ll_msgcount);
        llMsgcount.setVisibility(View.GONE);

        logoutChooser = new Dialog(LandScreenActivity.this, R.style.DialogSlideAnim);
        logoutChooser.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logoutChooser.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        logoutChooser.getWindow().setGravity(Gravity.CENTER);
        logoutChooser.setContentView(R.layout.dialog_logout_option);
        logoutChooser.setCanceledOnTouchOutside(true);

        llYes = (LinearLayout) logoutChooser.findViewById(R.id.ll_yes);
        llNo = (LinearLayout) logoutChooser.findViewById(R.id.ll_no);
        llYes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutChooser.dismiss();
                loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                UserID = loginPreferences.getString("UserId", "");
                getLogout(UserID);
            }
        });

        llNo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutChooser.dismiss();
            }
        });


        /*
                Push Count Show After App on if any notifiations is pending
         */

        if(AppController.K_getNotificationCount()>0) {
            llMsgcount.setText("" + AppController.K_getNotificationCount());
            llMsgcount.setVisibility(View.VISIBLE);
            BadgeUtils.setBadge(this,AppController.K_getNotificationCount());
        }






         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TXT_Logout=(TitilliumRegular)navigationView.findViewById(R.id.TXT_Logout);
        TXT_Privacy=(TitilliumRegular)navigationView.findViewById(R.id.TXT_Privacy);


        TXT_Logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                logoutChooser.show();
            }
        });

        TXT_Privacy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(LandScreenActivity.this,PrivacyPolicy.class);
                startActivity(intent);
            }
        });

        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        fragmentManager = getSupportFragmentManager();

        try {
            getIntentValue = getIntent().getExtras().getString("MSG");

            if (getIntentValue.equals("ChatDetailsFragment")) {

                txtCal.setTextColor(Color.parseColor("#A4A4A5"));
                txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
                txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
                txtMsg.setTextColor(Color.parseColor("#22A7F0"));
                imgCal.setBackgroundResource(R.drawable.cal);
                imgApnt.setBackgroundResource(R.drawable.apnt);
                imgPrg.setBackgroundResource(R.drawable.prg);
                imgMsg.setBackgroundResource(R.drawable.msgclick);

//                Bundle bundleChecking = new Bundle();
//                bundleChecking.putString("MSG_FROM : ", "ChatDetailsFragment");

                fragmentTransaction = fragmentManager.beginTransaction();
                ProfileFragment prl_fragment = new ProfileFragment();
                fragmentTransaction.replace(R.id.fragment_container, prl_fragment,"FROM_MSG_DETAILS");
                fragmentTransaction.commit();

            } else if (getIntentValue.equals("MSGFragment")) {

                Bundle bundlePush = new Bundle();
                bundlePush.putString("isRefreshPusher : ", "NO");

                txtCal.setTextColor(Color.parseColor("#A4A4A5"));
                txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
                txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
                txtMsg.setTextColor(Color.parseColor("#22A7F0"));
                imgCal.setBackgroundResource(R.drawable.cal);
                imgApnt.setBackgroundResource(R.drawable.apnt);
                imgPrg.setBackgroundResource(R.drawable.prg);
                imgMsg.setBackgroundResource(R.drawable.msgfill);

                fragmentTransaction = fragmentManager.beginTransaction();
                msg_fragment = new Messagefragment();
                msg_fragment.setArguments(bundlePush);
                fragmentTransaction.replace(R.id.fragment_container, msg_fragment);
//                int count = fragmentManager.getBackStackEntryCount();
//                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            } else {
                txtCal.setTextColor(Color.parseColor("#A4A4A5"));
                txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
                txtPrg.setTextColor(Color.parseColor("#22A7F0"));
                txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
                imgCal.setBackgroundResource(R.drawable.cal);
                imgApnt.setBackgroundResource(R.drawable.apnt);
                imgPrg.setBackgroundResource(R.drawable.prgclick2);
                imgMsg.setBackgroundResource(R.drawable.msg);

                fragmentTransaction = fragmentManager.beginTransaction();
                ProgressFragment prg_fragment = new ProgressFragment();
                fragmentTransaction.replace(R.id.fragment_container, prg_fragment);
//                int count = fragmentManager.getBackStackEntryCount();
//                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ------------------- Calendar Fragment
            fragmentTransaction = fragmentManager.beginTransaction();
            CalenderFragment cal_fragment = new CalenderFragment();
            fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
//            int count = fragmentManager.getBackStackEntryCount();
//            fragmentTransaction.addToBackStack(String.valueOf(count));
            fragmentTransaction.commit();

            txtCal.setTextColor(Color.parseColor("#22A7F0"));
            txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
            txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
            txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
            imgCal.setBackgroundResource(R.drawable.calclick);
            imgApnt.setBackgroundResource(R.drawable.apnt);
            imgPrg.setBackgroundResource(R.drawable.prg);
            imgMsg.setBackgroundResource(R.drawable.msg);

        }

        llCalenderButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                SharedPreferences.Editor editorslot =  AppController.slot.edit();
                editorslot.clear();
                editorslot.putString("value","");
                editorslot.commit();
                // ------------------- Calendar Fragment
                txtCal.setTextColor(Color.parseColor("#22A7F0"));
                txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
                txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
                txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
                imgCal.setBackgroundResource(R.drawable.calclick);
                imgApnt.setBackgroundResource(R.drawable.apnt);
                imgPrg.setBackgroundResource(R.drawable.prg);
                imgMsg.setBackgroundResource(R.drawable.msg);

                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                AppConfig.currentDate = calendar.get(Calendar.DATE);
                AppConfig.currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonth = (calendar.get(Calendar.MONTH));
                AppConfig.currentYear = calendar.get(Calendar.YEAR);
                AppConfig.firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

                Calendar calendarBookApp = Calendar.getInstance(Locale.getDefault());
                AppConfig.currentDateBookApp = calendarBookApp.get(Calendar.DATE);
                AppConfig.currentDayBookApp = calendarBookApp.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonthBookApp = (calendarBookApp.get(Calendar.MONTH));
                AppConfig.currentYearBookApp = calendarBookApp.get(Calendar.YEAR);
                AppConfig.firstDayPositionBookApp = calendarBookApp.get(Calendar.DAY_OF_WEEK);

                fragmentTransaction = fragmentManager.beginTransaction();
                CalenderFragment cal_fragment = new CalenderFragment();
                fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
//                int count = fragmentManager.getBackStackEntryCount();
//                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();

            }
        });
        llBlockAppoinmentButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // ------------------- Book Appointment Fragment
                txtCal.setTextColor(Color.parseColor("#A4A4A5"));
                txtApnt.setTextColor(Color.parseColor("#22A7F0"));
                txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
                txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
                imgCal.setBackgroundResource(R.drawable.cal);
                imgApnt.setBackgroundResource(R.drawable.apntclick2);
                imgPrg.setBackgroundResource(R.drawable.prg);
                imgMsg.setBackgroundResource(R.drawable.msg);

                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                AppConfig.currentDate = calendar.get(Calendar.DATE);
                AppConfig.currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonth = (calendar.get(Calendar.MONTH));
                AppConfig.currentYear = calendar.get(Calendar.YEAR);
                AppConfig.firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

                Calendar calendarBookApp = Calendar.getInstance(Locale.getDefault());
                AppConfig.currentDateBookApp = calendarBookApp.get(Calendar.DATE);
                AppConfig.currentDayBookApp = calendarBookApp.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonthBookApp = (calendarBookApp.get(Calendar.MONTH));
                AppConfig.currentYearBookApp = calendarBookApp.get(Calendar.YEAR);
                AppConfig.firstDayPositionBookApp = calendarBookApp.get(Calendar.DAY_OF_WEEK);

                fragmentTransaction = fragmentManager.beginTransaction();
                BookAppointmentFragment bookapnt_fragment = new BookAppointmentFragment();
                fragmentTransaction.replace(R.id.fragment_container, bookapnt_fragment);
//                int count = fragmentManager.getBackStackEntryCount();
//                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();

            }
        });
        llProgressButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // ------------------- Progress Fragment


                SharedPreferences.Editor editorslot =  AppController.slot.edit();
                editorslot.clear();
                editorslot.putString("value","");
                editorslot.commit();


                txtCal.setTextColor(Color.parseColor("#A4A4A5"));
                txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
                txtPrg.setTextColor(Color.parseColor("#22A7F0"));
                txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
                imgCal.setBackgroundResource(R.drawable.cal);
                imgApnt.setBackgroundResource(R.drawable.apnt);
                imgPrg.setBackgroundResource(R.drawable.prgclick2);
                imgMsg.setBackgroundResource(R.drawable.msg);

                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                AppConfig.currentDate = calendar.get(Calendar.DATE);
                AppConfig.currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonth = (calendar.get(Calendar.MONTH));
                AppConfig.currentYear = calendar.get(Calendar.YEAR);
                AppConfig.firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

                Calendar calendarBookApp = Calendar.getInstance(Locale.getDefault());
                AppConfig.currentDateBookApp = calendarBookApp.get(Calendar.DATE);
                AppConfig.currentDayBookApp = calendarBookApp.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonthBookApp = (calendarBookApp.get(Calendar.MONTH));
                AppConfig.currentYearBookApp = calendarBookApp.get(Calendar.YEAR);
                AppConfig.firstDayPositionBookApp = calendarBookApp.get(Calendar.DAY_OF_WEEK);

                fragmentTransaction = fragmentManager.beginTransaction();
                ProgressFragment prg_fragment = new ProgressFragment();
                fragmentTransaction.replace(R.id.fragment_container, prg_fragment);
//                int count = fragmentManager.getBackStackEntryCount();
//                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();

            }
        });
        llMessagebutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // ------------------- Message Fragment


                SharedPreferences.Editor editorslot =  AppController.slot.edit();
                editorslot.clear();
                editorslot.putString("value","");
                editorslot.commit();

                txtCal.setTextColor(Color.parseColor("#A4A4A5"));
                txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
                txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
                txtMsg.setTextColor(Color.parseColor("#22A7F0"));
                imgCal.setBackgroundResource(R.drawable.cal);
                imgApnt.setBackgroundResource(R.drawable.apnt);
                imgPrg.setBackgroundResource(R.drawable.prg);
                imgMsg.setBackgroundResource(R.drawable.msgfill);

                llMsgcount.setVisibility(View.GONE);

                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                AppConfig.currentDate = calendar.get(Calendar.DATE);
                AppConfig.currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonth = (calendar.get(Calendar.MONTH));
                AppConfig.currentYear = calendar.get(Calendar.YEAR);
                AppConfig.firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

                Calendar calendarBookApp = Calendar.getInstance(Locale.getDefault());
                AppConfig.currentDateBookApp = calendarBookApp.get(Calendar.DATE);
                AppConfig.currentDayBookApp = calendarBookApp.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonthBookApp = (calendarBookApp.get(Calendar.MONTH));
                AppConfig.currentYearBookApp = calendarBookApp.get(Calendar.YEAR);
                AppConfig.firstDayPositionBookApp = calendarBookApp.get(Calendar.DAY_OF_WEEK);

                fragmentTransaction = fragmentManager.beginTransaction();
                msg_fragment = new Messagefragment();
                fragmentTransaction.replace(R.id.fragment_container, msg_fragment);
//                int count = fragmentManager.getBackStackEntryCount();
//                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();

            }
        });

        pusherFunctionally();

    }

    public void pusherFunctionally() {
        mPusher = new Pusher(PUSHER_API_KEY);
        mPusher.connect(new ConnectionEventListener() {

            //@Override
            public void onError(String message, String code, Exception e) {
                 Log.i("@@ ---->>On Error : : ", "message :" + message + "\n code :" + code + "\n Exception :" + e.toString());
            }

            //@Override
            public void onConnectionStateChange(ConnectionStateChange change) {

            }
        }, ConnectionState.ALL);

        Channel mPuserChannel = mPusher.subscribe(PUSHER_CHANNEL);
        mPuserChannel.bind(PUSHER_EVENT, new SubscriptionEventListener() {

            //@Override
            public void onEvent(String channelName, String eventName, String data) {
                //Log.i("---->>On Event : : ", "channelName :" + channelName + "\n eventName :" + eventName + "\n data :" + data);

                try {
                    JSONObject jObject = new JSONObject(data.toString());
//                    Log.i("OBJECT : ", data.toString());
                    sendTo = jObject.getString("sent_to");
                    sendBy = jObject.getString("sent_by");
                    msg = jObject.getString("message");
                    dateTime = jObject.getString("send_time");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (AppController.isAppRunning().equals("YES")) {
                                AppController.K_SetLuncherAndNotificationCount(AppController.K_getNotificationCount()+1);

                                llMsgcount.setText(""+AppController.K_getNotificationCount());
                                llMsgcount.setVisibility(View.VISIBLE);
                                BadgeUtils.setBadge(LandScreenActivity.this,AppController.K_getNotificationCount());
                                Log.d("@@ KOUSHIK",""+"BRADAVAFVAHHA");

                            } else {
                                Log.d("@@ KOUSHIK"," else "+"BRADAVAFVAHHA");

                                AppController.K_SetLuncherAndNotificationCount(0);
                                llMsgcount.setVisibility(View.GONE);

                                BadgeUtils.clearBadge(LandScreenActivity.this);
                                try {
                                    msg_fragment.refreshMSGFragment(sendBy, msg, dateTime);
                                } catch (Exception e) {
//                                    Log.i("Exception : ", "LandScreen refreshMSGFragment " + e.toString());
                                }
                            }
                        }
                    });


                } catch (Exception e) {
//                    Log.i("Pusher Exception : ", e.toString());
                }
            }

        });

        mPusher.connect();

    }


    public void setOneTimeAlarm(String dateString) {
        Intent intent = new Intent(this, TimeAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), pendingIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPusher.connect();
        AppController.setIsNotificationState("NO");
        AppController.setIsNotificationStateChat("NO");
        Log.i("State :  ", "Land onStart " + AppController.isNotificationState());
    }

    @Override
    public void onResume() {
        super.onResume();
        AppController.setIsNotificationState("NO");
        AppController.setIsNotificationStateChat("NO");
        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        Ac_status = loginPreferences.getBoolean("AC_Activation", false);

        Log.d("PUTS STATUS=", "" + Ac_status);
        AppConfig.loginDatatype = new LoginDataType(
                loginPreferences.getString("UserId", ""),
                loginPreferences.getString("Username", ""),
                loginPreferences.getString("Password", ""));

        /*
            @@Koushik
            Manage Logout when login via other device and app is off or background
            with clear batch count from luncher notification
         */

        if(AppController.K_getNotificationCount()>0) {
            llMsgcount.setText("" + AppController.K_getNotificationCount());
            llMsgcount.setVisibility(View.VISIBLE);
            BadgeUtils.setBadge(this,AppController.K_getNotificationCount());
        }

        if (Ac_status == false) {
//            getLogout_APP_Background();
            getLogout(UserID);
        }




        final IntentFilter filter = new IntentFilter();
        filter.addAction("message");
        LocalBroadcastManager.getInstance(LandScreenActivity.this)
                .registerReceiver(mRegistrationBroadcastReceiver, filter);

        Log.i("State : ", "Land onResume " + AppController.isNotificationState());
    }


    //    ---@@ KOUSHIK----
    private void getLogout_APP_Background() {
        dateSavePreference = getSharedPreferences("DateTimeDiary", Context.MODE_PRIVATE);
        dateSaveCalendarPopUP = getSharedPreferences("DateTime", Context.MODE_PRIVATE);
        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        UserID = loginPreferences.getString("UserId", "");
        Ac_status = loginPreferences.getBoolean("AC_Activation", false);
        sharedPreferences = getSharedPreferences("DateTime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.clear();
        editor1.commit();

        SharedPreferences.Editor editorDiary = dateSavePreference.edit();
        editorDiary.clear();
        editorDiary.commit();

        SharedPreferences.Editor editorCalendorPopup = dateSaveCalendarPopUP.edit();
        editorCalendorPopup.clear();
        editorCalendorPopup.commit();

        AppConfig.appointmentArrayList.clear();
        AppConfig.programArrayList.clear();
        AppConfig.mealArrayList.clear();
        AppConfig.availableDateArrayList.clear();

        SharedPreferences.Editor editor = loginPreferences.edit();
        editor.clear();
        editor.commit();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        AppConfig.currentDate = calendar.get(Calendar.DATE);
        AppConfig.currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        AppConfig.currentMonth = (calendar.get(Calendar.MONTH));
        AppConfig.currentYear = calendar.get(Calendar.YEAR);
        AppConfig.firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        Calendar calendarBookApp = Calendar.getInstance(Locale.getDefault());
        AppConfig.currentDateBookApp = calendarBookApp.get(Calendar.DATE);
        AppConfig.currentDayBookApp = calendarBookApp.getActualMinimum(Calendar.DAY_OF_MONTH);
        AppConfig.currentMonthBookApp = (calendarBookApp.get(Calendar.MONTH));
        AppConfig.currentYearBookApp = calendarBookApp.get(Calendar.YEAR);
        AppConfig.firstDayPositionBookApp = calendarBookApp.get(Calendar.DAY_OF_WEEK);
        AppController.Acdeactivation = true;
        Intent intent = new Intent(LandScreenActivity.this, LoginActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppController.setIsNotificationState("YES");
        AppController.setIsNotificationStateChat("YES");


        LocalBroadcastManager.getInstance(LandScreenActivity.this)
                .unregisterReceiver(mRegistrationBroadcastReceiver);
//        Log.i("State : ", "Land onPause " + AppController.isNotificationState());
    }

    @Override
    public void onStop() {
        super.onStop();
        mPusher.disconnect();
        AppController.setIsNotificationState("YES");
        AppController.setIsNotificationStateChat("YES");

        Log.i("State : ", "Land onStop " + AppController.isNotificationState());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppController.setIsNotificationState("YES");
        AppController.setIsNotificationStateChat("YES");

        Log.i("State : ", "Land onDestroy " + AppController.isNotificationState());
    }

    public void Show_FullScreen_ViewPager(Context con, ArrayList<ZoomCurrentImageDataType> obj) {
        if (obj.isEmpty()) {
            //Toast.makeText(con, "No Images Load First...", Toast.LENGTH_SHORT).show();
        } else {
            fragContainer.setVisibility(View.GONE);
            llBottomBar.setVisibility(View.GONE);
//            Log.d("pager", "" + obj);
            VPage = getLayoutInflater().inflate(R.layout.frag_progress_images_dialog, null, false);
            rllandcontainer.addView(VPage);
            ViewPager pager = (ViewPager) VPage.findViewById(R.id.pager_prog_img);
            RelativeLayout close = (RelativeLayout) VPage.findViewById(R.id.close_pager);
            ///////////////////////////////////////////////////////////////////////

            /////////////////////////////////////////////////////////////////////

            PagerAdapter mPagerAdapter = new Progress_ViewPgr_Adapter(con, obj);
            pager.setAdapter(mPagerAdapter);
            pager.setCurrentItem(obj.size());

            close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    fragContainer.setVisibility(View.VISIBLE);
                    llBottomBar.setVisibility(View.VISIBLE);
                    VPage.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void DrowerOpen() {
        drawer.openDrawer(GravityCompat.START);
    }

    //end code here

    //    @@KOUSHIK
    private class LandBroadcastReceiver extends BroadcastReceiver {

        private static final String TAG = "@@KOUSHIK BrodReceiver ";


        @Override
        public void onReceive(Context context, Intent intent) {
            String broadcast_action = intent.getAction();
            String Logoutmsg = intent.getExtras().getString("LOGOUT");
            Log.d(TAG, "onReceive: " + broadcast_action);
            Log.d(TAG, "onReceive: " + Logoutmsg);



          UserID = loginPreferences.getString("UserId", "");
            Ac_status = loginPreferences.getBoolean("AC_Activation", false);
//            Toast.makeText(LandScreenActivity.this, "utloggning framgångsrik", Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.clear();
            editor1.commit();

            SharedPreferences.Editor editorDiary = dateSavePreference.edit();
            editorDiary.clear();
            editorDiary.commit();

            SharedPreferences.Editor editorCalendorPopup = dateSaveCalendarPopUP.edit();
            editorCalendorPopup.clear();
            editorCalendorPopup.commit();

            AppConfig.appointmentArrayList.clear();
            AppConfig.programArrayList.clear();
            AppConfig.mealArrayList.clear();
            AppConfig.availableDateArrayList.clear();

            SharedPreferences.Editor editor = loginPreferences.edit();
            editor.clear();
            editor.commit();

            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            AppConfig.currentDate = calendar.get(Calendar.DATE);
            AppConfig.currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
            AppConfig.currentMonth = (calendar.get(Calendar.MONTH));
            AppConfig.currentYear = calendar.get(Calendar.YEAR);
            AppConfig.firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

            Calendar calendarBookApp = Calendar.getInstance(Locale.getDefault());
            AppConfig.currentDateBookApp = calendarBookApp.get(Calendar.DATE);
            AppConfig.currentDayBookApp = calendarBookApp.getActualMinimum(Calendar.DAY_OF_MONTH);
            AppConfig.currentMonthBookApp = (calendarBookApp.get(Calendar.MONTH));
            AppConfig.currentYearBookApp = calendarBookApp.get(Calendar.YEAR);
            AppConfig.firstDayPositionBookApp = calendarBookApp.get(Calendar.DAY_OF_WEEK);
            AppController.Acdeactivation = true;
            Intent intente = new Intent(LandScreenActivity.this, LoginActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intente);

//            if(Logoutmsg.equalsIgnoreCase("Account logged out"))
//                AppController.DeactivationAlert="Någon har loggat in från en annan enhet och du har nu loggats ut";
//            else if(Logoutmsg.equalsIgnoreCase("Account Deactivated"))
//                AppController.DeactivationAlert="kontot har inaktiverats , kontakta din tränare .";


//            Toast.makeText(LandScreenActivity.this,R.string.msgaccdeactivate,Toast.LENGTH_SHORT).show();
//            getLogout();

//            LandScreenActivity.this.change_settings(broadcast_action);
        }
    }

    public void getLogout(final String userId) {

        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(LandScreenActivity.this);
        pDialog.setTitle("Logga ut");
        pDialog.setMessage("Vänta...");
        pDialog.setCancelable(false);

        AsyncTask<Void, Void, Void> allEvents = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    logoutExcep = "";


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "login/android_logout?user_id=" + userId)
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.i("response", "" + response.body().string());

                } catch (Exception e) {
                    logoutExcep = e.toString();
                }
                Log.i("Logout : ", AppConfig.HOST + "login/android_logout?user_id=" + userId);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                //////////////////////////////////////////////////////////

                pDialog.dismiss();
                if (logoutExcep.equalsIgnoreCase("")) {

                    Toast.makeText(LandScreenActivity.this, "utloggning framgångsrik", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                    editor1.clear();
                    editor1.commit();

                    SharedPreferences.Editor editorDiary = dateSavePreference.edit();
                    editorDiary.clear();
                    editorDiary.commit();

                    SharedPreferences.Editor editorCalendorPopup = dateSaveCalendarPopUP.edit();
                    editorCalendorPopup.clear();
                    editorCalendorPopup.commit();

                    AppConfig.appointmentArrayList.clear();
                    AppConfig.programArrayList.clear();
                    AppConfig.mealArrayList.clear();
                    AppConfig.availableDateArrayList.clear();

                    SharedPreferences.Editor editor = loginPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    AppConfig.currentDate = calendar.get(Calendar.DATE);
                    AppConfig.currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                    AppConfig.currentMonth = (calendar.get(Calendar.MONTH));
                    AppConfig.currentYear = calendar.get(Calendar.YEAR);
                    AppConfig.firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

                    Calendar calendarBookApp = Calendar.getInstance(Locale.getDefault());
                    AppConfig.currentDateBookApp = calendarBookApp.get(Calendar.DATE);
                    AppConfig.currentDayBookApp = calendarBookApp.getActualMinimum(Calendar.DAY_OF_MONTH);
                    AppConfig.currentMonthBookApp = (calendarBookApp.get(Calendar.MONTH));
                    AppConfig.currentYearBookApp = calendarBookApp.get(Calendar.YEAR);
                    AppConfig.firstDayPositionBookApp = calendarBookApp.get(Calendar.DAY_OF_WEEK);

                    Intent intent = new Intent(LandScreenActivity.this, LoginActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    LandScreenActivity.this.finish();
                    startActivity(intent);
                } else {
                    Log.i("Logout : ", logoutExcep);
                    Toast.makeText(LandScreenActivity.this, "logga ut misslyckade", Toast.LENGTH_SHORT).show();
                }
            }

        };
        allEvents.execute();

    }
    public void getLogout() {

        dateSavePreference = getSharedPreferences("DateTimeDiary", Context.MODE_PRIVATE);
        dateSaveCalendarPopUP = getSharedPreferences("DateTime", Context.MODE_PRIVATE);
        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        UserID = loginPreferences.getString("UserId", "");
        Ac_status = loginPreferences.getBoolean("AC_Activation", false);
        sharedPreferences = getSharedPreferences("DateTime", Context.MODE_PRIVATE);
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(LandScreenActivity.this);
        pDialog.setTitle("Logga ut");
        pDialog.setMessage("Vänta...");
        pDialog.setCancelable(false);

        AsyncTask<Void, Void, Void> allEvents = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    logoutExcep = "";



                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "login/android_logout?user_id=" + UserID)
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.i("response",""+response.body().string());

                } catch (Exception e) {
                    logoutExcep = e.toString();
                }
                Log.i("Logout : ", AppConfig.HOST + "login/android_logout?user_id=" + UserID);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                //////////////////////////////////////////////////////////

                pDialog.dismiss();
                if (logoutExcep.equalsIgnoreCase("")) {

                    Toast.makeText(LandScreenActivity.this, "utloggning framgångsrik", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                    editor1.clear();
                    editor1.commit();

                    SharedPreferences.Editor editorDiary = dateSavePreference.edit();
                    editorDiary.clear();
                    editorDiary.commit();

                    SharedPreferences.Editor editorCalendorPopup = dateSaveCalendarPopUP.edit();
                    editorCalendorPopup.clear();
                    editorCalendorPopup.commit();

                    AppConfig.appointmentArrayList.clear();
                    AppConfig.programArrayList.clear();
                    AppConfig.mealArrayList.clear();
                    AppConfig.availableDateArrayList.clear();

                    SharedPreferences.Editor editor = loginPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    AppConfig.currentDate = calendar.get(Calendar.DATE);
                    AppConfig.currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                    AppConfig.currentMonth = (calendar.get(Calendar.MONTH));
                    AppConfig.currentYear = calendar.get(Calendar.YEAR);
                    AppConfig.firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

                    Calendar calendarBookApp = Calendar.getInstance(Locale.getDefault());
                    AppConfig.currentDateBookApp = calendarBookApp.get(Calendar.DATE);
                    AppConfig.currentDayBookApp = calendarBookApp.getActualMinimum(Calendar.DAY_OF_MONTH);
                    AppConfig.currentMonthBookApp = (calendarBookApp.get(Calendar.MONTH));
                    AppConfig.currentYearBookApp = calendarBookApp.get(Calendar.YEAR);
                    AppConfig.firstDayPositionBookApp = calendarBookApp.get(Calendar.DAY_OF_WEEK);
                    AppController.Acdeactivation = true;
                    Intent intent = new Intent(LandScreenActivity.this, LoginActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);
                } else {
                    Log.i("Logout : ", logoutExcep);
                    Toast.makeText(LandScreenActivity.this, "logga ut misslyckade", Toast.LENGTH_SHORT).show();
                }
            }

        };
        allEvents.execute();

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (fragContainer.getVisibility() == View.GONE && llBottomBar.getVisibility() == View.GONE && VPage.getVisibility() == View.VISIBLE) {
            fragContainer.setVisibility(View.VISIBLE);
            llBottomBar.setVisibility(View.VISIBLE);
            VPage.setVisibility(View.GONE);
        }else if(fragment instanceof AppointmantFragment)
        {
            super.onBackPressed();
        }
        else if(fragment instanceof TrainingFragment)
        {
            super.onBackPressed();
        }
        else if(fragment instanceof AllGraphFragment)
        {
            super.onBackPressed();
        }
        else if(fragment instanceof DietFragment)
        {
            super.onBackPressed();
        }else if(fragment instanceof DiaryFragment)
        {
            super.onBackPressed();
        }else if(fragment instanceof AppointmentListFragment)
        {
            super.onBackPressed();
        }else if(fragment instanceof ProfileFragment)
        {
            try {
                if(fragment.getTag().equalsIgnoreCase("FROM_MSG_DETAILS"))
                {
                    Intent intent = new Intent(LandScreenActivity.this, ChatDetailsFragment.class);
                    intent.putExtra("msgUserName", AppConfig.PT_NAME);
                    intent.putExtra("msgUserId", AppConfig.PT_ID);
                    startActivity(intent);
                    finish();
                }else {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    BookAppointmentFragment bookapnt_fragment = new BookAppointmentFragment();
                    fragmentTransaction.replace(R.id.fragment_container, bookapnt_fragment);
                    fragmentTransaction.commit();
                }

            } catch (Exception e) {
                Intent intent = new Intent(LandScreenActivity.this, ChatDetailsFragment.class);
                intent.putExtra("msgUserName", AppConfig.PT_NAME);
                intent.putExtra("msgUserId", AppConfig.PT_ID);
                startActivity(intent);
                finish();

            }
        }
        else {
            //super.onBackPressed();
        }


    }
}
package com.ptplanner.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ptplanner.K_DataBase.Database;
import com.ptplanner.K_DataBase.LocalDataResponse;
import com.ptplanner.K_DataBase.OFFLineDataSave;
import com.ptplanner.Khelper.Internet_Informer;
import com.ptplanner.LandScreenActivity;
import com.ptplanner.R;
import com.ptplanner.ReminderService;
import com.ptplanner.TimeAlarm;
import com.ptplanner.customviews.HelveticaHeavy;
import com.ptplanner.customviews.TitilliumLight;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.AllEventsDatatype;
import com.ptplanner.datatype.AllExercisesDataType;
import com.ptplanner.datatype.AppointDataType;
import com.ptplanner.datatype.AvailableDateDataType;
import com.ptplanner.datatype.CalendarEventDataType;
import com.ptplanner.datatype.ExerciseSetsDataype;
import com.ptplanner.datatype.LoginDataType;
import com.ptplanner.datatype.MealDateDataType;
import com.ptplanner.datatype.ProgramDateDataType;
import com.ptplanner.dialog.ShowCalendarPopUp;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.AppController;
import com.ptplanner.helper.ConnectionDetector;
import com.ptplanner.helper.PtpLoader;
import com.ptplanner.helper.ReturnCalendarDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//@SuppressLint("NewApi")
public class CalenderFragment extends Fragment implements Internet_Informer {

    JSONArray jArrAppointment, jArrProgram, jArrMeal, jArrDiary, jarrayAvailableDate;
    CalendarEventDataType calEventData;
    String[] MealData, ProgramData, AvailableAppointmentDate, AppointmentData;
    ProgramDateDataType programDateDataType;
    MealDateDataType mealDateDataType;
    AppointDataType appointDataType;
    AvailableDateDataType availableDate;
    ProgressDialog progressDialog;
    View calenderView;
    LinearLayout rlTraining, rlDiet, rlDiary, showCalender, llList, logOut;
    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton;
    RelativeLayout llMessagebutton, appointment, rlContent;
    Dialog dialog;
    DatePicker datePicker;
    TimePicker timePicker;
    LinearLayout cancel, done;
    TitilliumLight txtRemindme;
    int mYear, mMonth, mDay, mHour, mMinute;
    String type, hour, min, appointment_string;
    View fView;
    LinearLayout dialogRemindme;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Bundle bundle;
    SharedPreferences sharedPreferences;
    ShowCalendarPopUp showCalPopup;
    String saveString = "";
    PtpLoader Loader;
    // -- Calendar Instance

    //    int currentYear, currentMonth, currentDay, currentDate, firstDayPosition;
//    Calendar calendar;
    SimpleDateFormat dayFormat, monthFormat, dateFormat, dateFormatDialog, targetDateFormat;
    Date dateChange;
    String date = "";
    String PAGE_DATE = "";
    String[] positionPre = {};
    int previousDayPosition;
    TitilliumSemiBold txtMonth, txtAppointment, powerchest;
    HelveticaHeavy txtDay;
    TitilliumLight txtTrainingDone, txtDiet, txtDiary, txt_Offline;
    ProgressBar prg_appointment, prg_content;
    String urlResponse = "", exception = "", urlResponsePr = "", exceptionPr = "", programName = "", logoutExcep = "";
    ConnectionDetector connectionDetector;
    AllEventsDatatype allEventsDatatype;


    SharedPreferences loginPreferences;
    Timer timer;
    LinearLayout transparentView;
    AlarmManager alarmManager;
    MyReceiver myReceiver;
    LandScreenActivity activity;
    LinearLayout appointmentButton, progressButton, Offline;
    RelativeLayout messageButton;
    Context context;

    TextView txtCal, txtApnt, txtPrg, txtMsg;
    ImageView imgCal, imgApnt, imgPrg, imgMsg;


    SharedPreferences userId;
    SharedPreferences dateSavePreference, dateSaveCalendarPopUP;

    Database LocalDatabase;

    RelativeLayout navigation_left, navigation_right;


    public class INTERNETRECIEVER extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("@@ Conection ", " Calll");
            boolean Connection = intent.getExtras().getBoolean("NET");
            if (Connection) {
                getAllEvents(PAGE_DATE);
                Log.d("@@ Conection ", " Connected");
            } else {
//                Offline.setVisibility(View.GONE);
                Log.d("@@ Conection ", " Disconnected");
            }

        }
    }

    private BroadcastReceiver receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        appointment_string = getResources().getString(R.string.Appointments);
        activity = (LandScreenActivity) getActivity();
        LocalDatabase = new Database(getActivity());
        receiver = new INTERNETRECIEVER();
        IntentFilter filter = new IntentFilter();
        filter.addAction("INTERNET");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
        //////////////////////////////////////////////////////////////////////////

        appointmentButton = (LinearLayout) getActivity().findViewById(R.id.blockappoinmentbutton);
        progressButton = (LinearLayout) getActivity().findViewById(R.id.progressbutton);
        messageButton = (RelativeLayout) getActivity().findViewById(R.id.messagebutton);
        ///////////////////////////////////////////////////////////////////////////
        fView = inflater.inflate(R.layout.frag_calender, container, false);

        Loader = new PtpLoader(getActivity());

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        this.fView = inflater.inflate(R.layout.frag_calender, container, false);
        /////////////////////////////////////////////////////

        fragmentManager = getActivity().getSupportFragmentManager();
        transparentView = (LinearLayout) fView.findViewById(R.id.transparentView);
        rlTraining = (LinearLayout) fView.findViewById(R.id.rl_training);
        rlDiet = (LinearLayout) fView.findViewById(R.id.rl_diet);
        rlDiary = (LinearLayout) fView.findViewById(R.id.rl_diary);
        Offline = (LinearLayout) fView.findViewById(R.id.Offline);
        Offline.setVisibility(View.GONE);
        appointment = (RelativeLayout) fView.findViewById(R.id.appointment);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        dialogRemindme = (LinearLayout) fView.findViewById(R.id.remindme);
        dialogRemindme.setVisibility(View.GONE);
        txtRemindme = (TitilliumLight) fView.findViewById(R.id.txt_remindme);
        txt_Offline = (TitilliumLight) fView.findViewById(R.id.txt_Offline);
        showCalender = (LinearLayout) fView.findViewById(R.id.show_cal);
        logOut = (LinearLayout) fView.findViewById(R.id.logout);
        txtDay = (HelveticaHeavy) fView.findViewById(R.id.txt_day);
        txtMonth = (TitilliumSemiBold) fView.findViewById(R.id.txt_month);
        txtAppointment = (TitilliumSemiBold) fView.findViewById(R.id.txt_appointment);
        powerchest = (TitilliumSemiBold) fView.findViewById(R.id.powerchest);
        txtAppointment.setVisibility(View.GONE);
        rlContent = (RelativeLayout) fView.findViewById(R.id.rl_content);
        rlContent.setVisibility(View.GONE);

        txtTrainingDone = (TitilliumLight) fView.findViewById(R.id.txt_training_done);
        txtDiet = (TitilliumLight) fView.findViewById(R.id.txt_diet);
        txtDiary = (TitilliumLight) fView.findViewById(R.id.txt_diary);

        llList = (LinearLayout) fView.findViewById(R.id.ll_list);
        llList.setVisibility(View.VISIBLE);

        prg_appointment = (ProgressBar) fView.findViewById(R.id.prg_appointent);
        prg_appointment.setVisibility(View.GONE);
        prg_content = (ProgressBar) fView.findViewById(R.id.prg_content);
        prg_content.setVisibility(View.GONE);
        powerchest.setText("");
        connectionDetector = new ConnectionDetector(getActivity());

//        calendar = Calendar.getInstance(Locale.getDefault());
//        currentDate = calendar.get(Calendar.DATE);
//        currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
//        currentMonth = (calendar.get(Calendar.MONTH));
//        currentYear = calendar.get(Calendar.YEAR);
//        firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);


        dayFormat = new SimpleDateFormat("d");
        monthFormat = new SimpleDateFormat("MMMM");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dateFormatDialog = new SimpleDateFormat("dd/MM hh:mm");
//        targetDateFormat = new SimpleDateFormat("dd/MM hh:mm aa");
//        targetDateFormat = new SimpleDateFormat("dd/MM kk:mm");
//        targetDateFormat = new SimpleDateFormat("kk:mm");
        targetDateFormat = new SimpleDateFormat("hh:mm");

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        dateSavePreference = this.getActivity().getSharedPreferences("DateTimeDiary", Context.MODE_PRIVATE);
        dateSaveCalendarPopUP = this.getActivity().getSharedPreferences("DateTime", Context.MODE_PRIVATE);
        userId = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        saveString = userId.getString("UserId", "");
        // -- Show Calendar
        showCalPopup = new ShowCalendarPopUp(getActivity(), "program");

        showCalender.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                    ///////////////////edit by suraj//////////////////
                    // -- Show Calendar
                    calenderView = view;
                    AppConfig.appointmentArrayList.clear();
                    AppConfig.programArrayList.clear();
                    AppConfig.mealArrayList.clear();
                    AppConfig.availableDateArrayList.clear();
                    getAllEvent();
                    ///////////////////by suraj//////////////////
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }
            }
            //------getting date
        });


        loginPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);


        logOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                logoutChooser.show();

                ((LandScreenActivity) getActivity()).DrowerOpen();
            }
        });


//        Offline.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Loader.Show();
//                Log.d("@@ Date-",PAGE_DATE+"jj");
//                new OFFLineDataSave(getActivity()) {
//                    @Override
//                    public void OnsaveSucess(String Response) {
//                        Log.d("@@ KDIARY",Response);
//                        txt_Offline.setText(getString(R.string.offlineAvilable));
//                        Offline.setClickable(false);
//                       Offline.setBackgroundColor(Color.parseColor("#00000000"));
//                        Loader.Dismiss();
//
//                    }
//
//                    @Override
//                    public void OnsaveError(String Error) {
//                        Log.d("@@ KDIARY Err",Error);
//                        Offline.setClickable(true);
//                        txt_Offline.setText(getString(R.string.offlinemode));
//                        Offline.setBackground(getResources().getDrawable(R.drawable.calbtnbg1));
//                        Loader.Dismiss();
//                    }
//                }.SaveDiary(PAGE_DATE);
//            }
//        });


        if (connectionDetector.isConnectingToInternet()) {
            try {
                if (AppConfig.changeDate.equalsIgnoreCase("")) {
                    try {
                        Log.i("SoutrikToday : ", "Calender inside try");

                        dateChange = dateFormat.parse(getArguments().getString("DateChange"));

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateFormat.parse(getArguments().getString("DateChange")));
                        AppConfig.calendar = cal;


                        getAllEvents(getArguments().getString("DateChange"));
                        PAGE_DATE = "" + getArguments().getString("DateChange");
                        txtDay.setText("" + dayFormat.format(dateChange));
                        txtMonth.setText("" + monthFormat.format(dateChange));

                    } catch (Exception e) {

                        if (dateChange == null) {


                            final Calendar c = Calendar.getInstance();
                            AppConfig.calendar = c;

                            AppConfig.currentDate = c.get(Calendar.DATE);
                            AppConfig.currentDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
                            AppConfig.currentMonth = (c.get(Calendar.MONTH));
                            AppConfig.currentYear = c.get(Calendar.YEAR);
                            AppConfig.firstDayPosition = c.get(Calendar.DAY_OF_WEEK);

                            txtDay.setText("" + dayFormat.format(c.getTime()));
                            txtMonth.setText("" + monthFormat.format(c.getTime()));
                            date = "" + dateFormat.format(c.getTime());
                            PAGE_DATE = date;
                            getAllEvents(date);
                        }
                    }
                } else {
                    try {
                        dateChange = dateFormat.parse(AppConfig.changeDate);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateFormat.parse(AppConfig.changeDate));
                        AppConfig.calendar = cal;

                        getAllEvents(AppConfig.changeDate);
                        PAGE_DATE = AppConfig.changeDate;
                        txtDay.setText("" + dayFormat.format(dateChange));
                        txtMonth.setText("" + monthFormat.format(dateChange));
                    } catch (Exception eex) {
                        eex.printStackTrace();
                        try {
                            final Calendar c = Calendar.getInstance();
                            AppConfig.calendar = c;

                            AppConfig.currentDate = c.get(Calendar.DATE);
                            AppConfig.currentDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
                            AppConfig.currentMonth = (c.get(Calendar.MONTH));
                            AppConfig.currentYear = c.get(Calendar.YEAR);
                            AppConfig.firstDayPosition = c.get(Calendar.DAY_OF_WEEK);

                            txtDay.setText("" + dayFormat.format(c.getTime()));
                            txtMonth.setText("" + monthFormat.format(c.getTime()));
                            date = "" + dateFormat.format(c.getTime());
                            PAGE_DATE = date;
                            getAllEvents(date);
                        } catch (Exception ew) {
                            ew.printStackTrace();
                        }
                    }
                }


            } catch (Exception etex) {
                etex.printStackTrace();
            }
        } else {
            /*
            @Koushik
            Offline Handling here?
             */


//            Offline.setVisibility(View.GONE);
            try {
                if (AppConfig.changeDate.equalsIgnoreCase("")) {
                    try {
                        Log.i("SoutrikToday : ", "Calender inside try");

                        dateChange = dateFormat.parse(getArguments().getString("DateChange"));

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateFormat.parse(getArguments().getString("DateChange")));
                        AppConfig.calendar = cal;

                        PAGE_OFFLINEDATASHOW(getArguments().getString("DateChange"));
                        PAGE_DATE = "" + getArguments().getString("DateChange");
                        txtDay.setText("" + dayFormat.format(dateChange));
                        txtMonth.setText("" + monthFormat.format(dateChange));

                    } catch (Exception e) {

                        if (dateChange == null) {


                            final Calendar c = Calendar.getInstance();
                            AppConfig.calendar = c;

                            AppConfig.currentDate = c.get(Calendar.DATE);
                            AppConfig.currentDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
                            AppConfig.currentMonth = (c.get(Calendar.MONTH));
                            AppConfig.currentYear = c.get(Calendar.YEAR);
                            AppConfig.firstDayPosition = c.get(Calendar.DAY_OF_WEEK);

                            txtDay.setText("" + dayFormat.format(c.getTime()));
                            txtMonth.setText("" + monthFormat.format(c.getTime()));
                            date = "" + dateFormat.format(c.getTime());
                            PAGE_DATE = date;
                            PAGE_OFFLINEDATASHOW(date);
                        }
                    }
                } else {
                    try {
                        dateChange = dateFormat.parse(AppConfig.changeDate);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateFormat.parse(AppConfig.changeDate));
                        AppConfig.calendar = cal;

                        PAGE_OFFLINEDATASHOW(date);
                        PAGE_DATE = AppConfig.changeDate;
                        txtDay.setText("" + dayFormat.format(dateChange));
                        txtMonth.setText("" + monthFormat.format(dateChange));
                    } catch (Exception eex) {
                        eex.printStackTrace();
                        try {
                            final Calendar c = Calendar.getInstance();
                            AppConfig.calendar = c;

                            AppConfig.currentDate = c.get(Calendar.DATE);
                            AppConfig.currentDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
                            AppConfig.currentMonth = (c.get(Calendar.MONTH));
                            AppConfig.currentYear = c.get(Calendar.YEAR);
                            AppConfig.firstDayPosition = c.get(Calendar.DAY_OF_WEEK);

                            txtDay.setText("" + dayFormat.format(c.getTime()));
                            txtMonth.setText("" + monthFormat.format(c.getTime()));
                            date = "" + dateFormat.format(c.getTime());
                            PAGE_DATE = date;
                            PAGE_OFFLINEDATASHOW(date);
                        } catch (Exception ew) {
                            ew.printStackTrace();
                        }
                    }
                }


            } catch (Exception etex) {
                etex.printStackTrace();
            }


//            new AlertDialog.Builder(getActivity())
//                    .setMessage(getResources().getString(R.string.no_internet))
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).show();
//            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }

        // -- Shared Preference
        sharedPreferences = getActivity().getSharedPreferences("DateTime", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("dateTime", "").equals("")) {
            String string;
            txtRemindme.setText(string = getResources().getString(R.string.RemindMe));
        } else {
            Date convertedDate = new Date();
            try {
                Log.i("@bodhi", "at calender fragment i got : " + sharedPreferences.getString("dateTime", "") + " trying to parse with :\"dd/MM hh:mm\"");
                convertedDate = dateFormatDialog.parse(sharedPreferences.getString("dateTime", ""));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Date d=new Date(convertedDate.getTime()+28800000);
            txtRemindme.setText(sharedPreferences.getString("dateTime", ""));
        }

        // ---------- Reminder Dialog
        dialog = new Dialog(getActivity());
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reminder_dialog);
        dialog.setCancelable(false);
        datePicker = (DatePicker) dialog.findViewById(R.id.date);
        datePicker.setCalendarViewShown(false);
        timePicker = (TimePicker) dialog.findViewById(R.id.time);

        timePicker.setIs24HourView(true);

        cancel = (LinearLayout) dialog.findViewById(R.id.ll_cancel);
        done = (LinearLayout) dialog.findViewById(R.id.ll_done);

        Calendar now = Calendar.getInstance();

        datePicker.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                null
        );

        timePicker.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(now.get(Calendar.MINUTE));


        appointment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                bundle = new Bundle();
                try {
                    DateFormat originalFormatToday = new SimpleDateFormat("yyyy-MMMM-dd");
                    DateFormat targetFormatToday = new SimpleDateFormat("yyyy-MM-dd");
                    Date todayDate = null;

                    bundle.putString("DateChange", PAGE_DATE);
                    Log.i("@ko : Try try ", PAGE_DATE);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("@ko", "got date on exception " + AppConfig.selected_date_from_calender);
                    bundle.putString("DateChange", AppConfig.selected_date_from_calender);
                    // Log.i("DateChange : Catch ", date);
                }


                try {
                    Log.d("@@KO", " " + PAGE_DATE);
                    if (Integer.parseInt(allEventsDatatype.getTotal_appointment()) > 0 && Integer.parseInt(allEventsDatatype.getTotal_appointment()) < 2) {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        AppointmantFragment app_fragment = new AppointmantFragment();
                        app_fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_container, app_fragment);
                        int count = fragmentManager.getBackStackEntryCount();
                        fragmentTransaction.addToBackStack(String.valueOf(count));
                        fragmentTransaction.commit();
                    } else if (Integer.parseInt(allEventsDatatype.getTotal_appointment()) > 1) {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        AppointmentListFragment app_list_fragment = new AppointmentListFragment();
                        app_list_fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_container, app_list_fragment);
                        int count = fragmentManager.getBackStackEntryCount();
                        fragmentTransaction.addToBackStack(String.valueOf(count));
                        fragmentTransaction.commit();
                    } else {

                    }
                } catch (NullPointerException e) {

                } catch (Exception e) {
                }

            }
        });

        // --- Training Fragment
        rlTraining.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Bundle bundleTraining = new Bundle();
                try {
                    bundleTraining.putString("DateChange", getArguments().getString("DateChange"));
                } catch (Exception e) {
                    bundleTraining.putString("DateChange", date);
                }

                fragmentTransaction = fragmentManager.beginTransaction();
                TrainingFragment trn_fragment = new TrainingFragment();
                trn_fragment.setArguments(bundleTraining);
                fragmentTransaction.replace(R.id.fragment_container, trn_fragment);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        // --- Diet Fragment
        rlDiet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                bundle = new Bundle();
                try {
                    bundle.putString("DateChange", getArguments().getString("DateChange"));
                } catch (Exception e) {
                    bundle.putString("DateChange", date);
                }
                //  bundle.putString("DateChange",getArguments().getString("DateChange"));
                fragmentTransaction = fragmentManager.beginTransaction();
                DietFragment diet_fragment = new DietFragment();
                diet_fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, diet_fragment);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        // --- Diary Fragment
        rlDiary.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Bundle bundleDiary = new Bundle();
                try {
                    bundleDiary.putString("DateChange", getArguments().getString("DateChange"));
                } catch (Exception e) {
                    bundleDiary.putString("DateChange", date);
                }

                fragmentTransaction = fragmentManager.beginTransaction();
                DiaryFragment diary_fragment = new DiaryFragment();
                diary_fragment.setArguments(bundleDiary);
                fragmentTransaction.replace(R.id.fragment_container, diary_fragment);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        llCalenderButton = (LinearLayout) getActivity().findViewById(R.id.calenderbutton);
        llBlockAppoinmentButton = (LinearLayout) getActivity().findViewById(R.id.blockappoinmentbutton);
        llProgressButton = (LinearLayout) getActivity().findViewById(R.id.progressbutton);
        llMessagebutton = (RelativeLayout) getActivity().findViewById(R.id.messagebutton);

        txtCal = (TextView) getActivity().findViewById(R.id.txt_cal);
        txtApnt = (TextView) getActivity().findViewById(R.id.txt_apnt);
        txtPrg = (TextView) getActivity().findViewById(R.id.txt_prg);
        txtMsg = (TextView) getActivity().findViewById(R.id.txt_msg);

        imgCal = (ImageView) getActivity().findViewById(R.id.img_cal);
        imgApnt = (ImageView) getActivity().findViewById(R.id.img_apnt);
        imgPrg = (ImageView) getActivity().findViewById(R.id.img_prg);
        imgMsg = (ImageView) getActivity().findViewById(R.id.img_msg);

        txtCal.setTextColor(Color.parseColor("#22A7F0"));
        txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
        txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
        txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
        imgCal.setBackgroundResource(R.drawable.calclick);
        imgApnt.setBackgroundResource(R.drawable.apnt);
        imgPrg.setBackgroundResource(R.drawable.prg);
        imgMsg.setBackgroundResource(R.drawable.msg);
        llCalenderButton.setClickable(false);
        llBlockAppoinmentButton.setClickable(true);
        llProgressButton.setClickable(true);
        llMessagebutton.setClickable(true);

        return fView;
    }

    private void PAGE_OFFLINEDATASHOW(final String date) {
        AppConfig.OfflineDate = date;

        final Calendar c = Calendar.getInstance();
        String CurrentDate = "" + dateFormat.format(c.getTime());
        if (CurrentDate.equals(date))

        {
            LocalDatabase.GET_Caleder_Frag_Fetails(date, new LocalDataResponse() {
                @Override
                public void OnSuccess(String Response) {
                    JSONObject jOBJ = null;
                    try {

                    /*
                      Offline button Display
                     */
//                    txt_Offline.setText(getString(R.string.offlineAvilable));
//                    Offline.setClickable(false);
//                    Offline.setBackgroundColor(Color.parseColor("#00000000"));
//                    final Calendar c = Calendar.getInstance();
//                    String CurrentDate = "" + dateFormat.format(c.getTime());
//                    if(CurrentDate.equals(date))
//                    {
//                        Offline.setVisibility(View.VISIBLE);
//                    }else {
//                        Offline.setVisibility(View.INVISIBLE);
//                    }
                   /*
                   Offline Button Display End
                    */

                        jOBJ = new JSONObject(Response);

                        Log.d("@@ OFLINE data- ", Response);

                        allEventsDatatype = new AllEventsDatatype(jOBJ.getString("total_meal"),
                                jOBJ.getString("total_appointment"),
                                jOBJ.getString("diary_text"),
                                jOBJ.getString("total_training_exercises"),
                                jOBJ.getString("total_training_exercise_finished"),
                                jOBJ.getString("total_training_programs"),
                                jOBJ.getString("total_training_programs_finished"));
                        allEventsDatatype.setNextBookingTime(jOBJ.getString("lowest_book_time"));
                        JSONArray jsonArray = jOBJ.getJSONArray("all_exercises");


                        // if(jsonArray.length() > 0) {
                        AppConfig.allExercisesDataTypeArrayList = new ArrayList<AllExercisesDataType>();
                        AppConfig.exerciseSetsDataypeArrayList = new ArrayList<ExerciseSetsDataype>();

                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                            try {
                                JSONArray jArr = jsonObject.getJSONArray("exercise_sets");
                                if (jArr.length() > 0) {
                                    for (int k = 0; k < jArr.length(); k++) {
                                        JSONObject jObject = jArr.getJSONObject(k);
                                        ExerciseSetsDataype exerciseSetsDataype = new ExerciseSetsDataype(
                                                jObject.getString("reps"),
                                                jObject.getString("kg")
                                        );
                                        AppConfig.exerciseSetsDataypeArrayList.add(exerciseSetsDataype);
                                    }
                                }
                            } catch (Exception ex) {
                                Log.i("exercise_sets", ex.toString());
                            }
                            AllExercisesDataType allExercisesDataType = new AllExercisesDataType(
                                    jsonObject.getString("user_program_id"),
                                    jsonObject.getString("exercise_id"),
                                    jsonObject.getString("exercise_title"),
                                    jsonObject.getString("instruction"),
                                    AppConfig.exerciseSetsDataypeArrayList
                            );
                            allExercisesDataType.setTraingingPageData(jsonObject.getJSONObject("exercise_inDetails").toString());
                            AppConfig.allExercisesDataTypeArrayList.add(allExercisesDataType);
                        }
                        if (jOBJ.getString("training_enable").equals("N")) {
                            rlTraining.setVisibility(View.GONE);
                            fView.findViewById(R.id.DIV_1).setVisibility(View.GONE);
                        } else {
                            rlTraining.setVisibility(View.VISIBLE);
                            fView.findViewById(R.id.DIV_1).setVisibility(View.VISIBLE);
                        }
                        if (jOBJ.getString("meal_enable").equals("N")) {
                            rlDiet.setVisibility(View.GONE);
                            fView.findViewById(R.id.DIV_2).setVisibility(View.GONE);
                        } else {
                            rlDiet.setVisibility(View.VISIBLE);
                            fView.findViewById(R.id.DIV_2).setVisibility(View.VISIBLE);
                        }
                        if (jOBJ.getString("diary_enable").equals("N")) {
                            rlDiary.setVisibility(View.GONE);
                            fView.findViewById(R.id.DIV_3).setVisibility(View.GONE);

                        } else {
                            rlDiary.setVisibility(View.VISIBLE);
                            fView.findViewById(R.id.DIV_3).setVisibility(View.VISIBLE);
                        }
                        if (!allEventsDatatype.getNextBookingTime().equalsIgnoreCase("")) {
                            dialogRemindme.setVisibility(View.VISIBLE);
                        } else {
                            dialogRemindme.setVisibility(View.GONE);

                        }


                        /////////////////////////////////////////////////////////
                        appointmentButton.setClickable(true);
                        appointmentButton.setEnabled(true);
                        messageButton.setClickable(true);
                        messageButton.setEnabled(true);
                        progressButton.setClickable(true);
                        progressButton.setEnabled(true);

                        if (!allEventsDatatype.getNextBookingTime().equalsIgnoreCase("")) {
//                    ***************** ALERM SET FROM NEXT BOOKING  DTIME IF YES*********

                            DateFormat JSONDFORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Date JSONDATE_NEXTALERM = null;

                            Calendar Temp = Calendar.getInstance();
                            try {
                                JSONDATE_NEXTALERM = JSONDFORMAT.parse(allEventsDatatype.getNextBookingTime());
                                Log.i("@@ NEXT SELF CH ", " " + allEventsDatatype.getNextBookingTime());
                                Temp.setTime(JSONDATE_NEXTALERM);
                                mDay = Temp.get(Calendar.DAY_OF_MONTH);
                                mMonth = Temp.get(Calendar.MONTH) + 1;
                                mYear = Temp.get(Calendar.YEAR);
                                mHour = Temp.get(Calendar.HOUR);
                                mMinute = Temp.get(Calendar.MINUTE);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            String dateAndTime = mDay + "/" + mMonth + "  " + mHour + ":" + mMinute;
                            String[] MainString = allEventsDatatype.getNextBookingTime().split(" ");
                            String[] Time = MainString[1].split(":");


                            Date convertedDate = new Date();
                            try {
                                String TimeTakeTemp = Time[0] + ":" + Time[1];


                                Editor editor = sharedPreferences.edit();
                                editor.putString("dateTime", "" + TimeTakeTemp);

                                editor.commit();
                                txtRemindme.setText(TimeTakeTemp);
                            } catch (Exception e) {
                                Log.i("Shared exc", e.toString());
                            }

                            try {
                                convertedDate = dateFormatDialog.parse(dateAndTime);

                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            Calendar current = Calendar.getInstance();

                            if (Temp.compareTo(current) <= 0) {

                            } else {
                                txtRemindme.setText(sharedPreferences.getString("dateTime", ""));
                                setOneTimeAlarm(Temp);
                            }
                        } else {
                            try {
                                txtRemindme.setText(getResources().getString(R.string.reminder_dialog_remindme));

                            } catch (Exception e) {

                            }

                        }


                        if (exception.equals("") & isAdded()) {
                            prg_appointment.setVisibility(View.GONE);
                            prg_content.setVisibility(View.GONE);
                            txtAppointment.setVisibility(View.VISIBLE);
                            rlContent.setVisibility(View.VISIBLE);
                            llList.setVisibility(View.VISIBLE);

                            if (Integer.parseInt(allEventsDatatype.getTotal_appointment()) == 0) {
                                txtAppointment.setText(getResources().getString(R.string.NoAppointment));
                            } else if (Integer.parseInt(allEventsDatatype.getTotal_appointment()) == 1) {
                                txtAppointment.setText(allEventsDatatype.getTotal_appointment() + "  " + getResources().getString(R.string.SingleAppointment));
                            } else {
                                txtAppointment.setText(allEventsDatatype.getTotal_appointment() + "  " + getResources().getString(R.string.MultipointAppointments));
                            }

                            if (Integer.parseInt(allEventsDatatype.getTotal_training_exercises()) == 0) {
                                txtTrainingDone.setText(" - ");
                            } else {
                                txtTrainingDone.setText(allEventsDatatype.getTotal_training_exercise_finished() +
                                        "/" + allEventsDatatype.getTotal_training_exercises() + " " + getResources().getString(R.string.ExerciseDone));
                            }

                            if (allEventsDatatype.getDiary_text().equalsIgnoreCase("")) {
                                txtDiary.setText("");
                            } else {
                                txtDiary.setText("\"" + allEventsDatatype.getDiary_text() + "\"");
                            }

                            if (Integer.parseInt(allEventsDatatype.getTotal_meal()) == 0) {
                                txtDiet.setText(" - ");
                            } else if (Integer.parseInt(allEventsDatatype.getTotal_meal()) == 1) {
                                txtDiet.setText(allEventsDatatype.getTotal_meal() + " " + getResources().getString(R.string.Meal));
                            } else {
                                txtDiet.setText(allEventsDatatype.getTotal_meal() + " " + getResources().getString(R.string.Meals));
                            }
                            try {
                                if (AppConfig.allExercisesDataTypeArrayList.size() > 0) {
                                    LocalDatabase.GET_ProgramDetails(AppConfig.allExercisesDataTypeArrayList.get(0).getUser_program_id(), new LocalDataResponse() {
                                        @Override
                                        public void OnSuccess(String Response) {
                                            try {
                                                Log.d("@@ Progra,-", Response);
                                                programName = new JSONObject(Response).getString("program_name");
                                                if (programName.equals("")) {
                                                    powerchest.setText("Vilodag");
                                                } else {
                                                    powerchest.setText("" + programName);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void OnNotfound(String NotFound) {
                                            powerchest.setText("Vilodag");
                                        }
                                    });
                                } else {
                                    powerchest.setText("Vilodag");
                                }
                            } catch (Exception e) {
                                powerchest.setText("Vilodag");
                            }
                        } else {
                            Log.i("*--Excep : ", exception);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("jOBJ", "" + jOBJ);


                }

                @Override
                public void OnNotfound(String NotFound) {
                    rlTraining.setVisibility(View.GONE);
                    fView.findViewById(R.id.DIV_1).setVisibility(View.GONE);
                    rlDiet.setVisibility(View.GONE);
                    fView.findViewById(R.id.DIV_2).setVisibility(View.GONE);
                    rlDiary.setVisibility(View.GONE);
                    fView.findViewById(R.id.DIV_3).setVisibility(View.GONE);
                    powerchest.setText("");
                }
            });
        } else {
            rlTraining.setVisibility(View.GONE);
            fView.findViewById(R.id.DIV_1).setVisibility(View.GONE);
            rlDiet.setVisibility(View.GONE);
            fView.findViewById(R.id.DIV_2).setVisibility(View.GONE);
            rlDiary.setVisibility(View.GONE);
            fView.findViewById(R.id.DIV_3).setVisibility(View.GONE);
            powerchest.setText("");
        }
    }

    public void setOneTimeAlarm(Calendar targetCal) {
        try {
            AppController.setIsAlermSet("YES");
            Intent intent = new Intent(getActivity(), TimeAlarm.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void getAllEvents(final String date) {

        AppConfig.OfflineDate = date;
        AsyncTask<Void, Void, Void> allEvents = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();


//                final Calendar c = Calendar.getInstance();
//                String CurrentDate = "" + dateFormat.format(c.getTime());
//                if(CurrentDate.equals(date))
//                {
//                    Offline.setVisibility(View.VISIBLE);
//                }else {
//                    Offline.setVisibility(View.INVISIBLE);
//                }

                LocalDatabase.GET_Diary_Frag_Fetails(date, new LocalDataResponse() {
                    @Override
                    public void OnSuccess(String Response) {
//                        Log.d("@@@ DATAT-",Response+"hsbdh");
//                        txt_Offline.setText(getString(R.string.offlineAvilable));
//                        Offline.setClickable(false);
//                        Offline.setBackgroundColor(Color.parseColor("#00000000"));

//                        Offline.setClickable(true);
//                        txt_Offline.setText(getString(R.string.offlinemode));
//                        Offline.setBackground(getResources().getDrawable(R.drawable.calbtnbg1));
                    }

                    @Override
                    public void OnNotfound(String NotFound) {
                        Log.d("@@@ DATAT-", NotFound);
//                        Offline.setClickable(true);
//                        txt_Offline.setText(getString(R.string.offlinemode));
//                        Offline.setBackground(getResources().getDrawable(R.drawable.calbtnbg1));
                    }
                });


                messageButton.setClickable(false);
                messageButton.setEnabled(false);
                progressButton.setClickable(false);
                progressButton.setEnabled(false);
                appointmentButton.setClickable(false);
                appointmentButton.setEnabled(false);
                dialogRemindme.setVisibility(View.GONE);
                ///////////////////////////////////////////////////
                prg_appointment.setVisibility(View.VISIBLE);
                prg_content.setVisibility(View.VISIBLE);
                txtAppointment.setVisibility(View.GONE);
                rlContent.setVisibility(View.GONE);
                llList.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/get_all_events_for_date/?" +
                                    "client_id=" + saveString +
                                    "&date_val=" + date)
                            .build();
                    Log.i("ALL EVENTS- URL", AppConfig.HOST + "app_control/get_all_events_for_date/?" +
                            "client_id=" + saveString +
                            "&date_val=" + date);
                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ", "" + jOBJ);


                    allEventsDatatype = new AllEventsDatatype(jOBJ.getString("total_meal"),
                            jOBJ.getString("total_appointment"),
                            jOBJ.getString("diary_text"),
                            jOBJ.getString("total_training_exercises"),
                            jOBJ.getString("total_training_exercise_finished"),
                            jOBJ.getString("total_training_programs"),
                            jOBJ.getString("total_training_programs_finished"));
                    allEventsDatatype.setNextBookingTime(jOBJ.getString("lowest_book_time"));
                    JSONArray jsonArray = jOBJ.getJSONArray("all_exercises");


                    // if(jsonArray.length() > 0) {
                    AppConfig.allExercisesDataTypeArrayList = new ArrayList<AllExercisesDataType>();
                    AppConfig.exerciseSetsDataypeArrayList = new ArrayList<ExerciseSetsDataype>();

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        try {
                            JSONArray jArr = jsonObject.getJSONArray("exercise_sets");
                            if (jArr.length() > 0) {
                                for (int k = 0; k < jArr.length(); k++) {
                                    JSONObject jObject = jArr.getJSONObject(k);
                                    ExerciseSetsDataype exerciseSetsDataype = new ExerciseSetsDataype(
                                            jObject.getString("reps"),
                                            jObject.getString("kg")
                                    );
                                    AppConfig.exerciseSetsDataypeArrayList.add(exerciseSetsDataype);
                                }
                            }
                        } catch (Exception ex) {
                            Log.i("exercise_sets", ex.toString());
                        }
                        AllExercisesDataType allExercisesDataType = new AllExercisesDataType(
                                jsonObject.getString("user_program_id"),
                                jsonObject.getString("exercise_id"),
                                jsonObject.getString("exercise_title"),
                                jsonObject.getString("instruction"),
                                AppConfig.exerciseSetsDataypeArrayList
                        );
                        allExercisesDataType.setTraingingPageData(jsonObject.getJSONObject("exercise_inDetails").toString());
                        AppConfig.allExercisesDataTypeArrayList.add(allExercisesDataType);
                    }
//
                    Log.d("getallEventsForDate", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }

                Log.d("URL", AppConfig.HOST + "app_control/get_all_events_for_date/?" +
                        "client_id=" + saveString +
                        "&date_val=" + date);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);


                try {

                    final Calendar c = Calendar.getInstance();
                    String CurrentDate = "" + dateFormat.format(c.getTime());
                    if (CurrentDate.equals(date)) {
                        Loader.Show();
                        new OFFLineDataSave(getActivity()) {
                            @Override
                            public void OnsaveSucess(String Response) {

                                Loader.Dismiss();

                            }

                            @Override
                            public void OnsaveError(String Error) {
                                Log.d("@@ KIARY Err", Error);

                                Loader.Dismiss();
                            }
                        }.SaveDiary(PAGE_DATE);
                    }


                    JSONObject jOBJ = new JSONObject(urlResponse);
                    JSONArray jsonArray = jOBJ.getJSONArray("all_exercises");

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        View itemview = getActivity().getLayoutInflater().inflate(R.layout.training_viewpager_adapter, null);
                        ImageView imgExercise = (ImageView) itemview.findViewById(R.id.img_exercise);
                        Glide.with(getActivity())
                                .load(jsonObject.getJSONObject("exercise_inDetails").getString("exercise_image")).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.drawable.no_progress_images)
                                .into(imgExercise);
                    }


                    LocalDatabase.SetCalenderPageData(date, urlResponse, new LocalDataResponse() {
                        @Override
                        public void OnSuccess(String Response) {
                            Log.d("@@ SAVE RES ", Response);

                        }

                        @Override
                        public void OnNotfound(String NotFound) {
                            Log.d("@@ SAVE Error  ", NotFound);
                        }
                    });


                    if (jOBJ.getString("training_enable").equals("N")) {
                        rlTraining.setVisibility(View.GONE);
                        fView.findViewById(R.id.DIV_1).setVisibility(View.GONE);
                    } else {
                        rlTraining.setVisibility(View.VISIBLE);
                        fView.findViewById(R.id.DIV_1).setVisibility(View.VISIBLE);
                    }
                    if (jOBJ.getString("meal_enable").equals("N")) {
                        rlDiet.setVisibility(View.GONE);
                        fView.findViewById(R.id.DIV_2).setVisibility(View.GONE);
                    } else {
                        rlDiet.setVisibility(View.VISIBLE);
                        fView.findViewById(R.id.DIV_2).setVisibility(View.VISIBLE);
                    }
                    if (jOBJ.getString("diary_enable").equals("N")) {
                        rlDiary.setVisibility(View.GONE);
                        fView.findViewById(R.id.DIV_3).setVisibility(View.GONE);

                    } else {
                        rlDiary.setVisibility(View.VISIBLE);
                        fView.findViewById(R.id.DIV_3).setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                if(!allEventsDatatype.getTotal_appointment().equals("0") && !allEventsDatatype.getNextBookingTime().equalsIgnoreCase("")){
                if (!allEventsDatatype.getNextBookingTime().equalsIgnoreCase("")) {
                    dialogRemindme.setVisibility(View.VISIBLE);
                } else {
                    dialogRemindme.setVisibility(View.GONE);

                }


                /////////////////////////////////////////////////////////
                appointmentButton.setClickable(true);
                appointmentButton.setEnabled(true);
                messageButton.setClickable(true);
                messageButton.setEnabled(true);
                progressButton.setClickable(true);
                progressButton.setEnabled(true);


                if (!allEventsDatatype.getNextBookingTime().equalsIgnoreCase("")) {
//                    ***************** ALERM SET FROM NEXT BOOKING  DTIME IF YES*********

                    DateFormat JSONDFORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date JSONDATE_NEXTALERM = null;

                    Calendar Temp = Calendar.getInstance();
                    try {
                        JSONDATE_NEXTALERM = JSONDFORMAT.parse(allEventsDatatype.getNextBookingTime());
                        Log.i("@@ NEXT SELF CH ", " " + allEventsDatatype.getNextBookingTime());
                        Temp.setTime(JSONDATE_NEXTALERM);
                        mDay = Temp.get(Calendar.DAY_OF_MONTH);
                        mMonth = Temp.get(Calendar.MONTH) + 1;
                        mYear = Temp.get(Calendar.YEAR);
                        mHour = Temp.get(Calendar.HOUR);
                        mMinute = Temp.get(Calendar.MINUTE);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    String dateAndTime = mDay + "/" + mMonth + "  " + mHour + ":" + mMinute;
                    String[] MainString = allEventsDatatype.getNextBookingTime().split(" ");
                    String[] Time = MainString[1].split(":");


                    Date convertedDate = new Date();
                    try {
                        String TimeTakeTemp = Time[0] + ":" + Time[1];

                        Log.i("@@ KOU SELF CH ", " " + TimeTakeTemp);


                        Editor editor = sharedPreferences.edit();
//                        editor.putString("dateTime", "" + targetDateFormat.format(JSONDATE_NEXTALERM.getTime()));
                        editor.putString("dateTime", "" + TimeTakeTemp);

//                        Log.i("@@KOUSHIK", "DATE " + targetDateFormat.format(Temp.getTime()));
                        Log.i("@@KOUSHIK", "DATE " + TimeTakeTemp);
                        editor.commit();
//                        txtRemindme.setText(targetDateFormat.format(JSONDATE_NEXTALERM.getTime()));
                        txtRemindme.setText(TimeTakeTemp);
                    } catch (Exception e) {
                        Log.i("Shared exc", e.toString());
                    }

                    try {
                        convertedDate = dateFormatDialog.parse(dateAndTime);
                        Log.i("@@KOUSHIK", "DATE2 " + dateFormatDialog.parse(dateAndTime));
                        Log.i("@@KOUSHIK", "DATE er " + targetDateFormat.format(convertedDate.getTime()));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    ////       txtRemindme.setText("" + targetDateFormat.format(convertedDate));


                    Calendar current = Calendar.getInstance();

                    if (Temp.compareTo(current) <= 0) {
//                        Toast.makeText(getActivity(), "Ogiltig Date/Tid", Toast.LENGTH_LONG).show();

                    } else {
                        txtRemindme.setText(sharedPreferences.getString("dateTime", ""));
                        setOneTimeAlarm(Temp);
                    }
                } else {
                    try {
                        txtRemindme.setText(getResources().getString(R.string.reminder_dialog_remindme));

                    } catch (Exception e) {

                    }

                }


                if (exception.equals("") & isAdded()) {
                    prg_appointment.setVisibility(View.GONE);
                    prg_content.setVisibility(View.GONE);
                    txtAppointment.setVisibility(View.VISIBLE);
                    rlContent.setVisibility(View.VISIBLE);
                    llList.setVisibility(View.VISIBLE);

                    if (Integer.parseInt(allEventsDatatype.getTotal_appointment()) == 0) {
                        txtAppointment.setText(getResources().getString(R.string.NoAppointment));
                    } else if (Integer.parseInt(allEventsDatatype.getTotal_appointment()) == 1) {
                        txtAppointment.setText(allEventsDatatype.getTotal_appointment() + "  " + getResources().getString(R.string.SingleAppointment));
                    } else {
                        txtAppointment.setText(allEventsDatatype.getTotal_appointment() + "  " + getResources().getString(R.string.MultipointAppointments));
                    }

                    if (Integer.parseInt(allEventsDatatype.getTotal_training_exercises()) == 0) {
                        txtTrainingDone.setText(" - ");
                    } else {
                        txtTrainingDone.setText(allEventsDatatype.getTotal_training_exercise_finished() +
                                "/" + allEventsDatatype.getTotal_training_exercises() + " " + getResources().getString(R.string.ExerciseDone));
                    }

                    if (allEventsDatatype.getDiary_text().equalsIgnoreCase("")) {
                        txtDiary.setText("");
                    } else {
                        txtDiary.setText("\"" + allEventsDatatype.getDiary_text() + "\"");
                    }

                    if (Integer.parseInt(allEventsDatatype.getTotal_meal()) == 0) {
                        txtDiet.setText(" - ");
                    } else if (Integer.parseInt(allEventsDatatype.getTotal_meal()) == 1) {
                        txtDiet.setText(allEventsDatatype.getTotal_meal() + " " + getResources().getString(R.string.Meal));
                    } else {
                        txtDiet.setText(allEventsDatatype.getTotal_meal() + " " + getResources().getString(R.string.Meals));
                    }
                    try {
                        if (AppConfig.allExercisesDataTypeArrayList.size() > 0) {
                            getProgramName(AppConfig.allExercisesDataTypeArrayList.get(0).getUser_program_id());
                        } else {
                            powerchest.setText("Vilodag");
                        }
                    } catch (Exception e) {
                        powerchest.setText("Vilodag");
                    }
                } else {
                    Log.i("*--Excep : ", exception);
                }
            }
        };
        allEvents.execute();
    }

    public void getProgramName(final String programID) {

        AsyncTask<Void, Void, Void> allEvents = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {//soutrik
                    exceptionPr = "";
                    urlResponsePr = "";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/get_program_name/?user_program_id=" + programID)
                            .build();
                    Log.i("get_program_name", "" + AppConfig.HOST + "app_control/get_program_name/?user_program_id=" + programID);
                    Response response = client.newCall(request).execute();
                    urlResponsePr = response.body().string();
                    LocalDatabase.SetProgramData(programID, urlResponsePr, new LocalDataResponse() {
                        @Override
                        public void OnSuccess(String Response) {

                        }

                        @Override
                        public void OnNotfound(String NotFound) {

                        }
                    });
                    JSONObject jOBJ = new JSONObject(urlResponsePr);
                    Log.i("get_program_name_jOBJ", "" + jOBJ);

                    programName = jOBJ.getString("program_name");

                } catch (Exception e) {
                    exceptionPr = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                //////////////////////////////////////////////////////////

                if (exceptionPr.equalsIgnoreCase("")) {
                    if (programName.equals("")) {
                        powerchest.setText("Vilodag");
                    } else {
                        powerchest.setText("" + programName);
                    }
                } else {
                    powerchest.setText("Vilodag");
                    Log.i("*--Excep : ", exception);
                }
            }

        };
        allEvents.execute();

    }

    @Override
    public void OnAvilable(boolean Res) {

        Log.d("@@ AVilabale", " Yes");

    }

    @Override
    public void OnUnAvilable(boolean Res) {
        Log.d("@@ AVilabale", " No");
    }


    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            txtRemindme.setText(getResources().getString(R.string.reminder_dialog_remindme));
            Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            dialog.dismiss();

            AppController.setIsAlermSet("NO");

            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("PT-Planner");
            alertDialog.setMessage("Du har en påminnelse från PT-Planner");
            alertDialog.setIcon(R.mipmap.fitnesslogo);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            if (intent.getStringExtra("MODE").equals("OPEN")) {
                alertDialog.show();
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ReminderService.MY_EVENT_ACTION);
        getActivity().registerReceiver(myReceiver, intentFilter);


        /**
         ********************* Addition for calender navigation ************************************
         */
        navigation_left = (RelativeLayout) view.findViewById(R.id.navigate_left);
        navigation_right = (RelativeLayout) view.findViewById(R.id.navigate_right);


        navigation_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("NavFlow : ", "Left click");

                Calendar c = Calendar.getInstance();
                c.setTime(AppConfig.calendar.getTime());
                c.add(Calendar.DATE, -1);
                Log.i("NavFlow : ", "got date :: " + dayFormat.format(c.getTime()));
                AppConfig.calendar = c;
                AppConfig.currentDate = c.get(Calendar.DATE);
                AppConfig.currentDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonth = (c.get(Calendar.MONTH));
                AppConfig.currentYear = c.get(Calendar.YEAR);
                AppConfig.firstDayPosition = c.get(Calendar.DAY_OF_WEEK);

                AppConfig.changeDate = dateFormat.format(c.getTime());
                AppConfig.bookAppDateChange = dateFormat.format(c.getTime());

                txtDay.setText("" + dayFormat.format(c.getTime()));
                txtMonth.setText("" + monthFormat.format(c.getTime()));

                date = "" + dateFormat.format(c.getTime());
                PAGE_DATE = date;
                if (connectionDetector.isConnectingToInternet())
                    getAllEvents(date);
                else
                    PAGE_OFFLINEDATASHOW(date);
            }
        });
        navigation_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("NavFlow : ", "right click");

                Calendar c = Calendar.getInstance();
                c.setTime(AppConfig.calendar.getTime());
                c.add(Calendar.DATE, 1);
                Log.i("NavFlow : ", "got date :: " + dayFormat.format(c.getTime()));
                AppConfig.calendar = c;
                AppConfig.currentDate = c.get(Calendar.DATE);
                AppConfig.currentDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonth = (c.get(Calendar.MONTH));
                AppConfig.currentYear = c.get(Calendar.YEAR);
                AppConfig.firstDayPosition = c.get(Calendar.DAY_OF_WEEK);

                AppConfig.changeDate = dateFormat.format(c.getTime());
                AppConfig.bookAppDateChange = dateFormat.format(c.getTime());

                txtDay.setText("" + dayFormat.format(c.getTime()));
                txtMonth.setText("" + monthFormat.format(c.getTime()));

                date = "" + dateFormat.format(c.getTime());
                PAGE_DATE = date;
                if (connectionDetector.isConnectingToInternet())
                    getAllEvents(date);
                else
                    PAGE_OFFLINEDATASHOW(date);

            }
        });
        /**
         * *****************************************************************************************
         */

    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.setIsAppRunning("YES");
//        AppController.setIsNotificationState("NO");
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ReminderService.MY_EVENT_ACTION);
        getActivity().registerReceiver(myReceiver, intentFilter);
        AppController.setIsAppRunning("YES");
//        AppController.setIsNotificationState("NO");
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            AppController.setIsAppRunning("YES");
//            AppController.setIsNotificationState("NO");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            getActivity().unregisterReceiver(myReceiver);
            AppController.setIsAppRunning("YES");
//            AppController.setIsNotificationState("YES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Editor editor = sharedPreferences.edit();
        // editor.clear();
        // editor.commit();
        AppController.setIsAppRunning("YES");
//        AppController.setIsNotificationState("YES");
    }

    public void getAllEvent() {
        AsyncTask<Void, Void, Void> event = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                SharedPreferences loginPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                AppConfig.loginDatatype = new LoginDataType(
                        loginPreferences.getString("UserId", ""),
                        loginPreferences.getString("Username", ""),
                        loginPreferences.getString("Password", ""));

                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    exception = "";
                    urlResponse = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/mark_calender?client_id="
                                    + AppConfig.loginDatatype.getSiteUserId())
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ", "" + jOBJ);

                    Log.i("mark_calender_Url", "" + AppConfig.HOST + "app_control/mark_calender?client_id="
                            + AppConfig.loginDatatype.getSiteUserId());


                    jArrProgram = jOBJ.getJSONArray("program_date");
                    jArrMeal = jOBJ.getJSONArray("meal_date");
                    jArrDiary = jOBJ.getJSONArray("diary_date");
                    jArrAppointment = jOBJ.getJSONArray("appointment_date");
                    jarrayAvailableDate = jOBJ.getJSONArray("available_date");

                    calEventData = new CalendarEventDataType();
                    for (int i = 0; i < jArrProgram.length(); i++) {
                        calEventData.setProgram_date(jArrProgram.getString(i));

                        ProgramData = jArrProgram.getString(i).split("-");
                        programDateDataType = new ProgramDateDataType(
                                ProgramData[2], ProgramData[1], ProgramData[0]);

                        AppConfig.programArrayList.add(programDateDataType);
                    }
                    for (int j = 0; j < jArrMeal.length(); j++) {
                        calEventData.setMeal_date(jArrMeal.getString(j));

                        MealData = jArrMeal.getString(j).split("-");
                        mealDateDataType = new MealDateDataType(MealData[2],
                                MealData[1], MealData[0]);

                        AppConfig.mealArrayList.add(mealDateDataType);

                    }
                    for (int l = 0; l < jArrAppointment.length(); l++) {

                        String[] appDate = jArrAppointment.getString(l).split(" ");

                        calEventData.setAppointment_date(appDate[0]);

                        AppointmentData = appDate[0].split("-");
                        appointDataType = new AppointDataType(
                                AppointmentData[2], AppointmentData[1],
                                AppointmentData[0]);

                        AppConfig.appointmentArrayList.add(appointDataType);
                    }
                    //////////////////////////////////////////////////////////////////////////////
                    for (int m = 0; m < jarrayAvailableDate.length(); m++) {

                        String[] appDate = jarrayAvailableDate.getString(m).split(" ");

                        calEventData.setAvailable_date_date(appDate[0]);

                        AvailableAppointmentDate = appDate[0].split("-");
                        availableDate = new AvailableDateDataType(
                                AvailableAppointmentDate[2], AvailableAppointmentDate[1],
                                AvailableAppointmentDate[0]);

                        AppConfig.availableDateArrayList.add(availableDate);
                    }
//            //////////////////////////////////////////////////////////////////////////////////
                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (exception.equals("")) {
                    showCalPopup.getLayouts();

                    Calendar pre = (Calendar) AppConfig.calendar.clone();
                    pre.set(Calendar.MONTH, AppConfig.currentMonth);
                    pre.set(Calendar.YEAR, AppConfig.currentYear);
                    pre.set(Calendar.DATE, 1);
                    positionPre = pre.getTime().toString().split(" ");

//                    for (int i = 0; i < positionPre.length; i++) {
//                        Log.i("positionPre[" + i + "]", "" + positionPre[i]);
//                    }
                    previousDayPosition = ReturnCalendarDetails.getPosition(positionPre[0]);


                    showCalPopup.getCalendar(
                            ReturnCalendarDetails.getCurrentMonth(positionPre[1]),
                            ReturnCalendarDetails.getPosition(positionPre[0]),
                            Integer.parseInt(positionPre[5])
                    );

                    showCalPopup.showAtLocation(calenderView, Gravity.CENTER_HORIZONTAL, 0, -20);
                } else {
                    System.out.println("@@ Exception: " + exception);
                }
                progressDialog.dismiss();
            }
        };
        event.execute();
    }
}

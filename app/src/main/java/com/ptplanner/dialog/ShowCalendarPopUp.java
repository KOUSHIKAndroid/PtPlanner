package com.ptplanner.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ptplanner.R;
import com.ptplanner.customviews.HelveticaHeavy;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.AppointDataType;
import com.ptplanner.datatype.AvailableDateDataType;
import com.ptplanner.datatype.CalendarEventDataType;
import com.ptplanner.datatype.ClickDateDataType;
import com.ptplanner.datatype.DiaryDataType;
import com.ptplanner.datatype.EventDataType;
import com.ptplanner.datatype.MealDateDataType;
import com.ptplanner.datatype.ProgramDateDataType;
import com.ptplanner.fragment.BookAppointmentFragment;
import com.ptplanner.fragment.CalenderFragment;
import com.ptplanner.fragment.DietFragment;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import com.ptplanner.helper.ReturnCalendarDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowCalendarPopUp extends PopupWindow implements OnClickListener {


    Context context;
    String TYPE = "";
    TitilliumRegular[] textViewArray, txtApp;
    RelativeLayout[] llArray;
    LinearLayout[] eventViewArr, eventViewRoundArr;
    TitilliumRegular txt_currentdatemonth, txt_currentyear;
    LinearLayout llHide, btnNext, btnPre, llToday;
    View popupView;
    String dateChange = "";
    SimpleDateFormat simpleDateFormat;
    ProgressDialog progressDialog;
    Calendar SelctedCalender = null;

    // -- Calendar Instance
//    Calendar calendar;
//    int currentYear, currentMonth, currentDay, currentDate, firstDayPosition,
//            currentMonthLength, previousDayPosition, nextDayPosition;

    String month = "", day = "";
    Date date;

    int l;
    String[] positionPre = {}, positionNext = {};

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Bundle bundle;

    String urlResponse = "", exception = "";
    JSONArray jArrAppointment, jArrProgram, jArrMeal, jArrDiary, jarrayAvailableDate;
    CalendarEventDataType calEventData;
    AppointDataType appointDataType;
    ProgramDateDataType programDateDataType;
    MealDateDataType mealDateDataType;
    DiaryDataType diaryDataType;
    ArrayList<EventDataType> arrDay;
    ArrayList<EventDataType> appDay, availableDay;
    EventDataType eventDataType;
    String[] AppointmentData, ProgramData, MealData, DiaryData, AvailableAppointmentDate;
    ClickDateDataType clickDateDataType;
    AvailableDateDataType availableDate;
    HelveticaHeavy txtDay;
    TitilliumSemiBold txtMonth;
    SimpleDateFormat dayFormat, monthFormat, dateFormat;
    String date1 = "";

    SharedPreferences dateSavePreference, dateSavePreferenceDiary;

    public ShowCalendarPopUp(final Context context, final String TYPE) {
        super(context);

        this.context = context;
        this.TYPE = TYPE;

        setContentView(LayoutInflater.from(context).inflate(R.layout.popup_showcalender, null));
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupView = getContentView();
        setFocusable(true);

        llHide = (LinearLayout) popupView.findViewById(R.id.ll_hide);
        llToday = (LinearLayout) popupView.findViewById(R.id.ll_today);
        txt_currentyear = (TitilliumRegular) popupView.findViewById(R.id.txt_currentyear);
        txt_currentdatemonth = (TitilliumRegular) popupView.findViewById(R.id.txt_currentdatemonth);
        btnPre = (LinearLayout) popupView.findViewById(R.id.btn_pre);
        btnNext = (LinearLayout) popupView.findViewById(R.id.btn_next);

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        // -- Shared Preference
        dateSavePreference = context.getSharedPreferences("DateTime", Context.MODE_PRIVATE);
        dateSavePreferenceDiary = context.getSharedPreferences("DateTimeDiary", Context.MODE_PRIVATE);

        getLayouts();
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            ///////////////////edit by suraj//////////////////
            // -- Show Calendar
            if (AppConfig.appointmentArrayList.size() <= 0) {
                getAllEvent();
                Log.i("getAllEvent:::", "getAllEvent1");
            } else {
                getAllEvent();
                Log.i("getAllEvent:::", "getAllEvent2");
                AppConfig.appointmentArrayList.clear();
                AppConfig.programArrayList.clear();
                AppConfig.mealArrayList.clear();
                AppConfig.availableDateArrayList.clear();
            }
            ///////////////////by suraj//////////////////
        } else {
//            Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }


        // ------------------- Previous Month
        btnPre.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                getLayouts();

                Calendar pre = (Calendar) AppConfig.calendar.clone();

                AppConfig.currentMonth--;

                if (AppConfig.currentMonth == 0) {
                    AppConfig.currentMonth = 12;
                    AppConfig.currentYear--;
                }
                pre.set(Calendar.MONTH, AppConfig.currentMonth);
                pre.set(Calendar.YEAR, AppConfig.currentYear);
                pre.set(Calendar.DATE, 1);

                positionPre = pre.getTime().toString().split(" ");
                getCalendar(
                        ReturnCalendarDetails.getCurrentMonth(positionPre[1]),
                        ReturnCalendarDetails.getPosition(positionPre[0]),
                        Integer.parseInt(positionPre[5])
                );

            }
        });
        // ------------------- Next Month
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                getLayouts();

                Calendar next = (Calendar) AppConfig.calendar.clone();

                if (AppConfig.currentMonth > 11) {
                    AppConfig.currentMonth = 1;
                    AppConfig.currentYear++;
                } else {
                    AppConfig.currentMonth++;
                }

                next.set(Calendar.MONTH, AppConfig.currentMonth);
                next.set(Calendar.YEAR, AppConfig.currentYear);
                next.set(Calendar.DATE, 1);

                positionNext = next.getTime().toString().split(" ");
                getCalendar(
                        ReturnCalendarDetails.getCurrentMonth(positionNext[1]),
                        ReturnCalendarDetails.getPosition(positionNext[0]),
                        Integer.parseInt(positionNext[5])
                );
            }
        });
        btnPre.performClick();
        btnNext.performClick();

        llHide.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        llToday.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //////////////////////////////////////////////////////////////////////////
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                bundle = new Bundle();
                bundle.putString("DateChange", "" + today);
//
//                Log.i("*--Today : ", "" + today); //tilbaka
                final Calendar c = Calendar.getInstance();
                int yy = c.get(Calendar.YEAR);
                int mm = c.get(Calendar.MONTH);
                int dd = c.get(Calendar.DAY_OF_MONTH);

                AppConfig.currentDate = c.get(Calendar.DATE);
                AppConfig.currentDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonth = (c.get(Calendar.MONTH));
                AppConfig.currentYear = c.get(Calendar.YEAR);
                AppConfig.firstDayPosition = c.get(Calendar.DAY_OF_WEEK);

                String dateToday = yy + "-" + (mm + 1) + "-" + dd;
                DateFormat originalFormatToday = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat targetFormatToday = new SimpleDateFormat("yyyy-MMMM-dd");
                Date todayDate = null;
                try {
                    todayDate = originalFormatToday.parse(dateToday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = targetFormatToday.format(todayDate);
                AppConfig.changeDate = formattedDate;

                DateFormat originalMonthFormatToday = new SimpleDateFormat("MM");
                DateFormat targetMonthFormatToday = new SimpleDateFormat("MMMM");
                Date todayMonthDate = null;
                try {
                    todayMonthDate = originalMonthFormatToday.parse(dateToday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedMonthDate = targetMonthFormatToday.format(todayMonthDate);

                bundle = new Bundle();
                bundle.putString("DateChange", formattedDate);
                bundle.putString("MONTH", formattedMonthDate);
                bundle.putString("DAY", "" + dd);

                SharedPreferences.Editor editor = dateSavePreference.edit();
                editor.clear();
                editor.putString("ClickDatePreferenceType", "" + TYPE);
                editor.putString("ClickDatePreference", "" + formattedDate);
                editor.commit();

                SharedPreferences.Editor editor1 = dateSavePreferenceDiary.edit();
                editor1.clear();
                editor1.commit();


                ///////////////////////////////////////////////////////////////////////////
                dismiss();

                if (TYPE.equals("program")) {
                    fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    CalenderFragment cal_fragment = new CalenderFragment();
                    cal_fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
                    fragmentTransaction.commit();
                } else if (TYPE.equals("appointment")) {
                    fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    BookAppointmentFragment bookapp_fragment = new BookAppointmentFragment();
                    bookapp_fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_container, bookapp_fragment);
                    fragmentTransaction.commit();
                } else {
                    fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    DietFragment dietFragment = new DietFragment();
                    dietFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_container, dietFragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    public void getCalendar(int currentMonth, int indexOfDayOne, int curyear) {

        int i = 0, j = 0, k = 0, day = 1;
        int currentMonthLength = ReturnCalendarDetails.getMonthLenth(currentMonth, curyear);

        Calendar current = (Calendar) AppConfig.calendar.clone();

        current.set(Calendar.MONTH, currentMonth - 1);
        current.set(Calendar.YEAR, curyear);
        current.set(Calendar.DATE, AppConfig.currentDate);

        SimpleDateFormat sformat = new SimpleDateFormat("MMMM");


        txt_currentdatemonth.setText("" + ReturnCalendarDetails.getCurrentMonthName(currentMonth));
        txt_currentyear.setText("" + curyear);

        Calendar checkingCal = Calendar.getInstance();

        int today = checkingCal.get(Calendar.DATE);
        int toMonth = checkingCal.get(Calendar.MONTH);
        int toYear = checkingCal.get(Calendar.YEAR);
        
        /*
        @Koushik
        Add Flow Date Start
         */

        int Pre_day = 0, Pre_month = 0, Pre_Year = 0;

        Log.d("@@@ Type-- ", dateSavePreference.getString("ClickDatePreferenceType", ""));
        Log.d("@@@ Date-- ", dateSavePreference.getString("ClickDatePreference", ""));
        Log.d("@@@ Date2-- ", dateSavePreferenceDiary.getString("ClickDatePreference", ""));
        try {

            SelctedCalender = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
            SelctedCalender.setTime(sdf.parse(dateSavePreference.getString("ClickDatePreference", "")));

            if (SelctedCalender != null) {
                Pre_day = SelctedCalender.get(Calendar.DATE);
                Pre_month = SelctedCalender.get(Calendar.MONTH);
                Pre_Year = SelctedCalender.get(Calendar.YEAR);
            }
        } catch (Exception e) {
            Log.e("@@@ Error ", "" + e.getMessage());
        }
/*
        @Koushik
        Add Flow Date End
         */


        drawEvent(indexOfDayOne);

        int todayShared = 0, toMonthShared = 0, toYearShared = 0;
        if (!dateSavePreference.getString("ClickDatePreference", "").equals("")) {
            String[] dateArr = dateSavePreference.getString("ClickDatePreference", "").split("-");

            toYearShared = Integer.parseInt(dateArr[0]);
            toMonthShared = ReturnCalendarDetails.getCurrentMonth(dateArr[1].substring(0, 3));
            todayShared = Integer.parseInt(dateArr[2]);
        }

        // --- Set Current Month
        for (i = indexOfDayOne; i < (currentMonthLength + indexOfDayOne); i++) {
            textViewArray[i].setText("" + day);
            textViewArray[i].setTag("" + day);
            textViewArray[i].setEnabled(true);
            textViewArray[i].setClickable(true);

            textViewArray[i].setTextColor(Color.BLACK);
            day++;

            textViewArray[i].setOnClickListener(this);

            String textValue = "";

            if (curyear == toYear) {
                if (currentMonth == (toMonth + 1)) {
                    if (textViewArray[(today + indexOfDayOne) - 1].getText().toString().equals("" + today)) {
                        textViewArray[(today + indexOfDayOne) - 1].setTextColor(Color.parseColor("#FF0000"));
                        llArray[(today + indexOfDayOne) - 1].setBackgroundResource(R.drawable.selected_day);
                    } else {
                        textViewArray[(today + indexOfDayOne) - 1].setTextColor(Color.parseColor("#000000"));
                        llArray[(today + indexOfDayOne) - 1].setBackgroundResource(0);
                    }
                }
            }

//@@ KOUSHIK Add Fllow date

            if (SelctedCalender != null && curyear == Pre_Year) {
                if (currentMonth == (Pre_month + 1)) {
                    if (textViewArray[(Pre_day + indexOfDayOne) - 1].getText().toString().equals("" + Pre_day)) {
                        textViewArray[(Pre_day + indexOfDayOne) - 1].setTextColor(Color.parseColor("#0080FF"));
                        llArray[(Pre_day + indexOfDayOne) - 1].setBackgroundResource(R.drawable.selected_day);
                    } else {
                        textViewArray[(today + indexOfDayOne) - 1].setTextColor(Color.parseColor("#000000"));
                        llArray[(today + indexOfDayOne) - 1].setBackgroundResource(0);
                    }
                }
            }


            if (toYearShared != 0 && toMonthShared != 0 && todayShared != 0) {
                if (curyear == toYearShared) {
                    if (currentMonth == toMonthShared) {
//                        textViewArray[(todayShared + indexOfDayOne) - 1].setTextColor(Color.parseColor("#22d23a"));
//                        if (dateSavePreference.getString("ClickDatePreferenceType", "").equals("program")
//                                || dateSavePreference.getString("ClickDatePreferenceType", "").equals("diet")) {
                        llArray[(todayShared + indexOfDayOne) - 1].setBackgroundResource(R.drawable.selected_day);
//                        } else {
//                            textViewArray[(todayShared + indexOfDayOne) - 1].setBackgroundResource(0);
//                            SharedPreferences.Editor editor = dateSavePreference.edit();
//                            editor.clear();
//                            editor.commit();
//                        }
                    }
                }

            } else {
            }
        }
        day = 1;

        // --- Set Next Month

        for (j = i; j < textViewArray.length; j++) {
            textViewArray[j].setText(""); //day
            //textViewArray[j].setTextColor(Color.parseColor("#BDBDBD"));
            llArray[j].setBackgroundResource(0);
            textViewArray[j].setEnabled(false);
            textViewArray[j].setClickable(false);
            day++;
        }
        day = 1;

        if (currentMonth == 1) {
            currentMonth = 12;
            curyear--;
        } else {
            currentMonth--;
        }
        int tempcurrentMonthLength = ReturnCalendarDetails.getMonthLenth(
                currentMonth, curyear);

        if (indexOfDayOne != 0) {
            // --- Set Previous Month
            for (k = indexOfDayOne - 1; k >= 0; k--) {
//                textViewArray[k].setText("" + tempcurrentMonthLength);
                textViewArray[k].setText("");
                //textViewArray[k].setTextColor(Color.parseColor("#BDBDBD"));
                llArray[k].setBackgroundResource(0);
                textViewArray[k].setEnabled(false);
                textViewArray[k].setClickable(false);
                tempcurrentMonthLength--;
            }
        }

    }

    public void getLayouts() {
        llArray = new RelativeLayout[42];
        llArray[0] = (RelativeLayout) popupView.findViewById(R.id.ll7);
        llArray[1] = (RelativeLayout) popupView.findViewById(R.id.ll8);
        llArray[2] = (RelativeLayout) popupView.findViewById(R.id.ll9);
        llArray[3] = (RelativeLayout) popupView.findViewById(R.id.ll10);
        llArray[4] = (RelativeLayout) popupView.findViewById(R.id.ll11);
        llArray[5] = (RelativeLayout) popupView.findViewById(R.id.ll12);
        llArray[6] = (RelativeLayout) popupView.findViewById(R.id.ll13);
        llArray[7] = (RelativeLayout) popupView.findViewById(R.id.ll14);
        llArray[8] = (RelativeLayout) popupView.findViewById(R.id.ll15);
        llArray[9] = (RelativeLayout) popupView.findViewById(R.id.ll16);
        llArray[10] = (RelativeLayout) popupView.findViewById(R.id.ll17);
        llArray[11] = (RelativeLayout) popupView.findViewById(R.id.ll18);
        llArray[12] = (RelativeLayout) popupView.findViewById(R.id.ll19);
        llArray[13] = (RelativeLayout) popupView.findViewById(R.id.ll20);
        llArray[14] = (RelativeLayout) popupView.findViewById(R.id.ll21);
        llArray[15] = (RelativeLayout) popupView.findViewById(R.id.ll22);
        llArray[16] = (RelativeLayout) popupView.findViewById(R.id.ll23);
        llArray[17] = (RelativeLayout) popupView.findViewById(R.id.ll24);
        llArray[18] = (RelativeLayout) popupView.findViewById(R.id.ll25);
        llArray[19] = (RelativeLayout) popupView.findViewById(R.id.ll26);
        llArray[20] = (RelativeLayout) popupView.findViewById(R.id.ll27);
        llArray[21] = (RelativeLayout) popupView.findViewById(R.id.ll28);
        llArray[22] = (RelativeLayout) popupView.findViewById(R.id.ll29);
        llArray[23] = (RelativeLayout) popupView.findViewById(R.id.ll30);
        llArray[24] = (RelativeLayout) popupView.findViewById(R.id.ll31);
        llArray[25] = (RelativeLayout) popupView.findViewById(R.id.ll32);
        llArray[26] = (RelativeLayout) popupView.findViewById(R.id.ll33);
        llArray[27] = (RelativeLayout) popupView.findViewById(R.id.ll34);
        llArray[28] = (RelativeLayout) popupView.findViewById(R.id.ll35);
        llArray[29] = (RelativeLayout) popupView.findViewById(R.id.ll36);
        llArray[30] = (RelativeLayout) popupView.findViewById(R.id.ll37);
        llArray[31] = (RelativeLayout) popupView.findViewById(R.id.ll38);
        llArray[32] = (RelativeLayout) popupView.findViewById(R.id.ll39);
        llArray[33] = (RelativeLayout) popupView.findViewById(R.id.ll40);
        llArray[34] = (RelativeLayout) popupView.findViewById(R.id.ll41);
        llArray[35] = (RelativeLayout) popupView.findViewById(R.id.ll42);
        llArray[36] = (RelativeLayout) popupView.findViewById(R.id.ll43);
        llArray[37] = (RelativeLayout) popupView.findViewById(R.id.ll44);
        llArray[38] = (RelativeLayout) popupView.findViewById(R.id.ll45);
        llArray[39] = (RelativeLayout) popupView.findViewById(R.id.ll46);
        llArray[40] = (RelativeLayout) popupView.findViewById(R.id.ll47);
        llArray[41] = (RelativeLayout) popupView.findViewById(R.id.ll48);

        for (int a = 0; a < llArray.length; a++) {
            llArray[a].setBackgroundColor(Color.TRANSPARENT);
        }

        textViewArray = new TitilliumRegular[42];
        textViewArray[0] = (TitilliumRegular) popupView.findViewById(R.id.txt7);
        textViewArray[1] = (TitilliumRegular) popupView.findViewById(R.id.txt8);
        textViewArray[2] = (TitilliumRegular) popupView.findViewById(R.id.txt9);
        textViewArray[3] = (TitilliumRegular) popupView
                .findViewById(R.id.txt10);
        textViewArray[4] = (TitilliumRegular) popupView
                .findViewById(R.id.txt11);
        textViewArray[5] = (TitilliumRegular) popupView
                .findViewById(R.id.txt12);
        textViewArray[6] = (TitilliumRegular) popupView
                .findViewById(R.id.txt13);
        textViewArray[7] = (TitilliumRegular) popupView
                .findViewById(R.id.txt14);
        textViewArray[8] = (TitilliumRegular) popupView
                .findViewById(R.id.txt15);
        textViewArray[9] = (TitilliumRegular) popupView
                .findViewById(R.id.txt16);
        textViewArray[10] = (TitilliumRegular) popupView
                .findViewById(R.id.txt17);
        textViewArray[11] = (TitilliumRegular) popupView
                .findViewById(R.id.txt18);
        textViewArray[12] = (TitilliumRegular) popupView
                .findViewById(R.id.txt19);
        textViewArray[13] = (TitilliumRegular) popupView
                .findViewById(R.id.txt20);
        textViewArray[14] = (TitilliumRegular) popupView
                .findViewById(R.id.txt21);
        textViewArray[15] = (TitilliumRegular) popupView
                .findViewById(R.id.txt22);
        textViewArray[16] = (TitilliumRegular) popupView
                .findViewById(R.id.txt23);
        textViewArray[17] = (TitilliumRegular) popupView
                .findViewById(R.id.txt24);
        textViewArray[18] = (TitilliumRegular) popupView
                .findViewById(R.id.txt25);
        textViewArray[19] = (TitilliumRegular) popupView
                .findViewById(R.id.txt26);
        textViewArray[20] = (TitilliumRegular) popupView
                .findViewById(R.id.txt27);
        textViewArray[21] = (TitilliumRegular) popupView
                .findViewById(R.id.txt28);
        textViewArray[22] = (TitilliumRegular) popupView
                .findViewById(R.id.txt29);
        textViewArray[23] = (TitilliumRegular) popupView
                .findViewById(R.id.txt30);
        textViewArray[24] = (TitilliumRegular) popupView
                .findViewById(R.id.txt31);
        textViewArray[25] = (TitilliumRegular) popupView
                .findViewById(R.id.txt32);
        textViewArray[26] = (TitilliumRegular) popupView
                .findViewById(R.id.txt33);
        textViewArray[27] = (TitilliumRegular) popupView
                .findViewById(R.id.txt34);
        textViewArray[28] = (TitilliumRegular) popupView
                .findViewById(R.id.txt35);
        textViewArray[29] = (TitilliumRegular) popupView
                .findViewById(R.id.txt36);
        textViewArray[30] = (TitilliumRegular) popupView
                .findViewById(R.id.txt37);
        textViewArray[31] = (TitilliumRegular) popupView
                .findViewById(R.id.txt38);
        textViewArray[32] = (TitilliumRegular) popupView
                .findViewById(R.id.txt39);
        textViewArray[33] = (TitilliumRegular) popupView
                .findViewById(R.id.txt40);
        textViewArray[34] = (TitilliumRegular) popupView
                .findViewById(R.id.txt41);
        textViewArray[35] = (TitilliumRegular) popupView
                .findViewById(R.id.txt42);
        textViewArray[36] = (TitilliumRegular) popupView
                .findViewById(R.id.txt43);
        textViewArray[37] = (TitilliumRegular) popupView
                .findViewById(R.id.txt44);
        textViewArray[38] = (TitilliumRegular) popupView
                .findViewById(R.id.txt45);
        textViewArray[39] = (TitilliumRegular) popupView
                .findViewById(R.id.txt46);
        textViewArray[40] = (TitilliumRegular) popupView
                .findViewById(R.id.txt47);
        textViewArray[41] = (TitilliumRegular) popupView
                .findViewById(R.id.txt48);

        for (int i = 0; i < textViewArray.length; i++) {
            //textViewArray[i].setOnClickListener(this);
        }

        eventViewArr = new LinearLayout[42];
        eventViewArr[0] = (LinearLayout) popupView
                .findViewById(R.id.event_style1);
        eventViewArr[1] = (LinearLayout) popupView
                .findViewById(R.id.event_style2);
        eventViewArr[2] = (LinearLayout) popupView
                .findViewById(R.id.event_style3);
        eventViewArr[3] = (LinearLayout) popupView
                .findViewById(R.id.event_style4);
        eventViewArr[4] = (LinearLayout) popupView
                .findViewById(R.id.event_style5);
        eventViewArr[5] = (LinearLayout) popupView
                .findViewById(R.id.event_style6);
        eventViewArr[6] = (LinearLayout) popupView
                .findViewById(R.id.event_style7);
        eventViewArr[7] = (LinearLayout) popupView
                .findViewById(R.id.event_style8);
        eventViewArr[8] = (LinearLayout) popupView
                .findViewById(R.id.event_style9);
        eventViewArr[9] = (LinearLayout) popupView
                .findViewById(R.id.event_style10);
        eventViewArr[10] = (LinearLayout) popupView
                .findViewById(R.id.event_style11);
        eventViewArr[11] = (LinearLayout) popupView
                .findViewById(R.id.event_style12);
        eventViewArr[12] = (LinearLayout) popupView
                .findViewById(R.id.event_style13);
        eventViewArr[13] = (LinearLayout) popupView
                .findViewById(R.id.event_style14);
        eventViewArr[14] = (LinearLayout) popupView
                .findViewById(R.id.event_style15);
        eventViewArr[15] = (LinearLayout) popupView
                .findViewById(R.id.event_style16);
        eventViewArr[16] = (LinearLayout) popupView
                .findViewById(R.id.event_style17);
        eventViewArr[17] = (LinearLayout) popupView
                .findViewById(R.id.event_style18);
        eventViewArr[18] = (LinearLayout) popupView
                .findViewById(R.id.event_style19);
        eventViewArr[19] = (LinearLayout) popupView
                .findViewById(R.id.event_style20);
        eventViewArr[20] = (LinearLayout) popupView
                .findViewById(R.id.event_style21);
        eventViewArr[21] = (LinearLayout) popupView
                .findViewById(R.id.event_style22);
        eventViewArr[22] = (LinearLayout) popupView
                .findViewById(R.id.event_style23);
        eventViewArr[23] = (LinearLayout) popupView
                .findViewById(R.id.event_style24);
        eventViewArr[24] = (LinearLayout) popupView
                .findViewById(R.id.event_style25);
        eventViewArr[25] = (LinearLayout) popupView
                .findViewById(R.id.event_style26);
        eventViewArr[26] = (LinearLayout) popupView
                .findViewById(R.id.event_style27);
        eventViewArr[27] = (LinearLayout) popupView
                .findViewById(R.id.event_style28);
        eventViewArr[28] = (LinearLayout) popupView
                .findViewById(R.id.event_style29);
        eventViewArr[29] = (LinearLayout) popupView
                .findViewById(R.id.event_style30);
        eventViewArr[30] = (LinearLayout) popupView
                .findViewById(R.id.event_style31);
        eventViewArr[31] = (LinearLayout) popupView
                .findViewById(R.id.event_style32);
        eventViewArr[32] = (LinearLayout) popupView
                .findViewById(R.id.event_style33);
        eventViewArr[33] = (LinearLayout) popupView
                .findViewById(R.id.event_style34);
        eventViewArr[34] = (LinearLayout) popupView
                .findViewById(R.id.event_style35);
        eventViewArr[35] = (LinearLayout) popupView
                .findViewById(R.id.event_style36);
        eventViewArr[36] = (LinearLayout) popupView
                .findViewById(R.id.event_style37);
        eventViewArr[37] = (LinearLayout) popupView
                .findViewById(R.id.event_style38);
        eventViewArr[38] = (LinearLayout) popupView
                .findViewById(R.id.event_style39);
        eventViewArr[39] = (LinearLayout) popupView
                .findViewById(R.id.event_style40);
        eventViewArr[40] = (LinearLayout) popupView
                .findViewById(R.id.event_style41);
        eventViewArr[41] = (LinearLayout) popupView
                .findViewById(R.id.event_style42);

        for (int i = 0; i < eventViewArr.length; i++) {
            eventViewArr[i].setVisibility(View.GONE);
        }

        // ----------

        eventViewRoundArr = new LinearLayout[42];
        eventViewRoundArr[0] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round1);
        eventViewRoundArr[1] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round2);
        eventViewRoundArr[2] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round3);
        eventViewRoundArr[3] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round4);
        eventViewRoundArr[4] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round5);
        eventViewRoundArr[5] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round6);
        eventViewRoundArr[6] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round7);
        eventViewRoundArr[7] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round8);
        eventViewRoundArr[8] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round9);
        eventViewRoundArr[9] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round10);
        eventViewRoundArr[10] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round11);
        eventViewRoundArr[11] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round12);
        eventViewRoundArr[12] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round13);
        eventViewRoundArr[13] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round14);
        eventViewRoundArr[14] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round15);
        eventViewRoundArr[15] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round16);
        eventViewRoundArr[16] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round17);
        eventViewRoundArr[17] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round18);
        eventViewRoundArr[18] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round19);
        eventViewRoundArr[19] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round20);
        eventViewRoundArr[20] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round21);
        eventViewRoundArr[21] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round22);
        eventViewRoundArr[22] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round23);
        eventViewRoundArr[23] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round24);
        eventViewRoundArr[24] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round25);
        eventViewRoundArr[25] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round26);
        eventViewRoundArr[26] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round27);
        eventViewRoundArr[27] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round28);
        eventViewRoundArr[28] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round29);
        eventViewRoundArr[29] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round30);
        eventViewRoundArr[30] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round31);
        eventViewRoundArr[31] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round32);
        eventViewRoundArr[32] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round33);
        eventViewRoundArr[33] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round34);
        eventViewRoundArr[34] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round35);
        eventViewRoundArr[35] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round36);
        eventViewRoundArr[36] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round37);
        eventViewRoundArr[37] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round38);
        eventViewRoundArr[38] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round39);
        eventViewRoundArr[39] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round40);
        eventViewRoundArr[40] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round41);
        eventViewRoundArr[41] = (LinearLayout) popupView
                .findViewById(R.id.event_style_round42);

        for (int i = 0; i < eventViewRoundArr.length; i++) {
            eventViewRoundArr[i].setVisibility(View.GONE);
        }

        // ----------

        txtApp = new TitilliumRegular[42];
        txtApp[0] = (TitilliumRegular) popupView.findViewById(R.id.app1);
        txtApp[1] = (TitilliumRegular) popupView.findViewById(R.id.app2);
        txtApp[2] = (TitilliumRegular) popupView.findViewById(R.id.app3);
        txtApp[3] = (TitilliumRegular) popupView.findViewById(R.id.app4);
        txtApp[4] = (TitilliumRegular) popupView.findViewById(R.id.app5);
        txtApp[5] = (TitilliumRegular) popupView.findViewById(R.id.app6);
        txtApp[6] = (TitilliumRegular) popupView.findViewById(R.id.app7);
        txtApp[7] = (TitilliumRegular) popupView.findViewById(R.id.app8);
        txtApp[8] = (TitilliumRegular) popupView.findViewById(R.id.app9);
        txtApp[9] = (TitilliumRegular) popupView.findViewById(R.id.app10);
        txtApp[10] = (TitilliumRegular) popupView.findViewById(R.id.app11);
        txtApp[11] = (TitilliumRegular) popupView.findViewById(R.id.app12);
        txtApp[12] = (TitilliumRegular) popupView.findViewById(R.id.app13);
        txtApp[13] = (TitilliumRegular) popupView.findViewById(R.id.app14);
        txtApp[14] = (TitilliumRegular) popupView.findViewById(R.id.app15);
        txtApp[15] = (TitilliumRegular) popupView.findViewById(R.id.app16);
        txtApp[16] = (TitilliumRegular) popupView.findViewById(R.id.app17);
        txtApp[17] = (TitilliumRegular) popupView.findViewById(R.id.app18);
        txtApp[18] = (TitilliumRegular) popupView.findViewById(R.id.app19);
        txtApp[19] = (TitilliumRegular) popupView.findViewById(R.id.app20);
        txtApp[20] = (TitilliumRegular) popupView.findViewById(R.id.app21);
        txtApp[21] = (TitilliumRegular) popupView.findViewById(R.id.app22);
        txtApp[22] = (TitilliumRegular) popupView.findViewById(R.id.app23);
        txtApp[23] = (TitilliumRegular) popupView.findViewById(R.id.app24);
        txtApp[24] = (TitilliumRegular) popupView.findViewById(R.id.app25);
        txtApp[25] = (TitilliumRegular) popupView.findViewById(R.id.app26);
        txtApp[26] = (TitilliumRegular) popupView.findViewById(R.id.app27);
        txtApp[27] = (TitilliumRegular) popupView.findViewById(R.id.app28);
        txtApp[28] = (TitilliumRegular) popupView.findViewById(R.id.app29);
        txtApp[29] = (TitilliumRegular) popupView.findViewById(R.id.app30);
        txtApp[30] = (TitilliumRegular) popupView.findViewById(R.id.app31);
        txtApp[31] = (TitilliumRegular) popupView.findViewById(R.id.app32);
        txtApp[32] = (TitilliumRegular) popupView.findViewById(R.id.app33);
        txtApp[33] = (TitilliumRegular) popupView.findViewById(R.id.app34);
        txtApp[34] = (TitilliumRegular) popupView.findViewById(R.id.app35);
        txtApp[35] = (TitilliumRegular) popupView.findViewById(R.id.app36);
        txtApp[36] = (TitilliumRegular) popupView.findViewById(R.id.app37);
        txtApp[37] = (TitilliumRegular) popupView.findViewById(R.id.app38);
        txtApp[38] = (TitilliumRegular) popupView.findViewById(R.id.app39);
        txtApp[39] = (TitilliumRegular) popupView.findViewById(R.id.app40);
        txtApp[40] = (TitilliumRegular) popupView.findViewById(R.id.app41);
        txtApp[41] = (TitilliumRegular) popupView.findViewById(R.id.app42);

        for (int i = 0; i < txtApp.length; i++) {
            txtApp[i].setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        dateChange = txt_currentyear.getText().toString() + "-"
                + txt_currentdatemonth.getText().toString() + "-"
                + view.getTag();
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MMM-dd");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = originalFormat.parse(dateChange);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        AppConfig.changeDate = formattedDate;
        AppConfig.bookAppDateChange = formattedDate;


        SharedPreferences.Editor editor = dateSavePreference.edit();
        editor.clear();
        editor.putString("ClickDatePreferenceType", "" + TYPE);
        editor.putString("ClickDatePreference", "" + dateChange);
        editor.commit();

        SharedPreferences.Editor editor1 = dateSavePreferenceDiary.edit();
        editor1.clear();
        editor1.putString("ClickDatePreference", "" + dateChange);
        editor1.commit();
        /**
         * App config selected_date_from_calender  used by bodhi
         */
        AppConfig.selected_date_from_calender = formattedDate;


        bundle = new Bundle();
        bundle.putString("DateChange", formattedDate);
        bundle.putString("MONTH", txt_currentdatemonth.getText().toString());
        bundle.putString("DAY", view.getTag().toString());
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

        clickDateDataType = new ClickDateDataType(
                view.getTag().toString(),
                txt_currentdatemonth.getText().toString(),
                txt_currentyear.getText().toString(),
                true
        );
        view.setBackgroundResource(R.drawable.selected_day);

        if (TYPE.equals("program")) {
            fragmentTransaction = fragmentManager.beginTransaction();
            CalenderFragment cal_fragment = new CalenderFragment();
            cal_fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
            fragmentTransaction.commit();
        } else if (TYPE.equals("appointment")) {
            fragmentTransaction = fragmentManager.beginTransaction();
            BookAppointmentFragment bookapp_fragment = new BookAppointmentFragment();
            bookapp_fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container,
                    bookapp_fragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction = fragmentManager.beginTransaction();
            DietFragment dietFragment = new DietFragment();
            dietFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container,
                    dietFragment);
            fragmentTransaction.commit();
        }

        dismiss();
    }

    public void drawEvent(int indexOfDayOne) {
        arrDay = new ArrayList<EventDataType>();
        appDay = new ArrayList<EventDataType>();
        availableDay = new ArrayList<EventDataType>();


        if (TYPE.equals("program")) {
            for (int i = 0; i < AppConfig.programArrayList.size(); i++) {

                if (AppConfig.programArrayList.get(i).getYear()
                        .equals(txt_currentyear.getText().toString())) {
                    DateFormat originalFormat = new SimpleDateFormat("MM");
                    DateFormat targetFormat = new SimpleDateFormat("MMMM");
                    try {
                        date = originalFormat.parse(AppConfig.programArrayList
                                .get(i).getMonth());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedMonth = targetFormat.format(date);
                    if (formattedMonth.equalsIgnoreCase(txt_currentdatemonth.getText()
                            .toString())) {
                        eventDataType = new EventDataType(
                                AppConfig.programArrayList.get(i).getDay(),
                                TYPE, false);
                        arrDay.add(eventDataType);
                    }
                }
            }

            for (int j = 0; j < AppConfig.appointmentArrayList.size(); j++) {

                if (AppConfig.appointmentArrayList.get(j).getYear().equals(txt_currentyear.getText().toString())) {
                    DateFormat originalFormat = new SimpleDateFormat("MM");
                    DateFormat targetFormat = new SimpleDateFormat("MMMM");
                    try {
                        date = originalFormat.parse(AppConfig.appointmentArrayList.get(j).getMonth());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedMonth = targetFormat.format(date);
                    if (formattedMonth.equalsIgnoreCase(txt_currentdatemonth.getText().toString())) {
                        eventDataType = new EventDataType(
                                AppConfig.appointmentArrayList.get(j).getDay(),
                                TYPE, true);
                        arrDay.add(eventDataType);
                        Log.d("##", AppConfig.appointmentArrayList.get(j).getDay());
                    }
                }
//                }   // Comment out 1 Aug 2017 by Koushik for client Appointment issue
            }
        } else if (TYPE.equals("appointment")) {
            for (int i = 0; i < AppConfig.appointmentArrayList.size(); i++) {

                if (AppConfig.appointmentArrayList.get(i).getYear()
                        .equals(txt_currentyear.getText().toString())) {
                    DateFormat originalFormat = new SimpleDateFormat("MM");
                    DateFormat targetFormat = new SimpleDateFormat("MMMM");
                    try {
                        date = originalFormat
                                .parse(AppConfig.appointmentArrayList.get(i)
                                        .getMonth());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedMonth = targetFormat.format(date);
//                    Log.i("!!CAL ", " formattedMonth " + formattedMonth);
                    if (formattedMonth.equalsIgnoreCase(txt_currentdatemonth.getText()
                            .toString())) {
                        eventDataType = new EventDataType(
                                AppConfig.appointmentArrayList.get(i).getDay(),
                                TYPE, false);
                        arrDay.add(eventDataType);

                    }
                }
            }
            ////////////////////////////////////////////////////////////////

            for (int z = 0; z < AppConfig.availableDateArrayList.size(); z++) {

                if (AppConfig.availableDateArrayList.get(z).getYear()
                        .equals(txt_currentyear.getText().toString())) {
                    DateFormat originalFormat = new SimpleDateFormat("MM");
                    DateFormat targetFormat = new SimpleDateFormat("MMMM");
                    try {
                        date = originalFormat
                                .parse(AppConfig.availableDateArrayList.get(z)
                                        .getMonth());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedMonth = targetFormat.format(date);
                    if (formattedMonth.equalsIgnoreCase(txt_currentdatemonth.getText()
                            .toString())) {
                        eventDataType = new EventDataType(
                                AppConfig.availableDateArrayList.get(z).getDay(),
                                TYPE, false);
                        availableDay.add(eventDataType);

                    }
                }
            }
            //////////////////////////////////////////////////////////////////


        } else

        {
            for (int i = 0; i < AppConfig.mealArrayList.size(); i++) {
                if (AppConfig.mealArrayList.get(i).getYear()
                        .equals(txt_currentyear.getText().toString())) {
                    DateFormat originalFormat = new SimpleDateFormat("MM");
                    DateFormat targetFormat = new SimpleDateFormat("MMMM");
                    try {
                        date = originalFormat.parse(AppConfig.mealArrayList
                                .get(i).getMonth());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedMonth = targetFormat.format(date);
                    if (formattedMonth.equalsIgnoreCase(txt_currentdatemonth.getText()
                            .toString())) {
                        eventDataType = new EventDataType(AppConfig.mealArrayList.get(i).getDay(), TYPE, false);
                        arrDay.add(eventDataType);
                    }
                }
            }
        }


        for (int j = 0; j < textViewArray.length; j++) {
            for (int i = 0; i < arrDay.size(); i++) {

                String textValue = "";

                if (textViewArray[j].getText().toString().length() < 2) {
                    textValue = "0" + textViewArray[j].getText().toString();
                } else {
                    textValue = textViewArray[j].getText().toString();
                }

                if (textValue.equals(arrDay.get(i).getMarkedDay())) {

                    if (arrDay.get(i).getTypeEvent().equals("appointment")) {

                        eventViewArr[(indexOfDayOne - 1)
                                + Integer
                                .parseInt(arrDay.get(i).getMarkedDay())]
                                .setVisibility(View.VISIBLE);
                        txtApp[(indexOfDayOne - 1)
                                + Integer
                                .parseInt(arrDay.get(i).getMarkedDay())]
                                .setVisibility(View.VISIBLE);


                    } else if (arrDay.get(i).getTypeEvent().equals("program")) {

                        if (arrDay.get(i).isSelected()) {
                            eventViewArr[(indexOfDayOne - 1)
                                    + Integer
                                    .parseInt(arrDay.get(i).getMarkedDay())]
                                    .setVisibility(View.VISIBLE);
                            txtApp[(indexOfDayOne - 1)
                                    + Integer
                                    .parseInt(arrDay.get(i).getMarkedDay())]
                                    .setVisibility(View.VISIBLE);

                        } else {
                            ///////////////////////////////////////////////

                            //////////////////////////////////////////////
                            eventViewRoundArr[(indexOfDayOne - 1)
                                    + Integer
                                    .parseInt(arrDay.get(i).getMarkedDay())]
                                    .setVisibility(View.VISIBLE);

//                            eventViewRoundArr[(indexOfDayOne - 1)
//                                    + Integer
//                                    .parseInt(arrDay.get(i).getMarkedDay())]
//                                    .setBackgroundResource(R.drawable.circle_event_green);
                        }

                    } else {
                        eventViewRoundArr[(indexOfDayOne - 1)
                                + Integer
                                .parseInt(arrDay.get(i).getMarkedDay())]
                                .setVisibility(View.VISIBLE);

                        eventViewRoundArr[(indexOfDayOne - 1)
                                + Integer
                                .parseInt(arrDay.get(i).getMarkedDay())]
                                .setBackgroundResource(R.drawable.circle_event_green);
                    }
                }
            }
        }

        //////////////////////////////////////////////////////////////////////
        for (int j = 0; j < textViewArray.length; j++) {
            for (int i = 0; i < availableDay.size(); i++) {
                String value = "";
                if (textViewArray[j].getText().toString().length() < 2) {
                    value = "0" + textViewArray[j].getText().toString();
                } else {
                    value = textViewArray[j].getText().toString();
                }
                if (value.equals(availableDay.get(i).getMarkedDay())) {
                    if (availableDay.get(i).getTypeEvent().equals("appointment")) {
                        eventViewRoundArr[(indexOfDayOne - 1)
                                + Integer
                                .parseInt(availableDay.get(i).getMarkedDay())]
                                .setVisibility(View.VISIBLE);
//                        eventViewRoundArr[(indexOfDayOne - 1)
//                                + Integer
//                                .parseInt(availableDay.get(i).getMarkedDay())]
//                                .setBackgroundResource(R.drawable.circle_event_green);
                    }

                }
            }
        }
        /////////////////////////////////////////////////////////////////////////
    }

    public void getAllEvent() {

        AsyncTask<Void, Void, Void> event = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    exception = "";
                    urlResponse = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST
                                    + "app_control/mark_calender?client_id="
                                    + AppConfig.loginDatatype.getSiteUserId())
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ", "" + jOBJ);

                    Log.i("mark_calender_Url", "" + AppConfig.HOST + "app_control/mark_calender?client_id=" + AppConfig.loginDatatype.getSiteUserId());


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
                try {
                    progressDialog.dismiss();
                    if (exception.equals("")) {

                    } else {
                        System.out.println("@@ Exception: " + exception);
                    }
                } catch (Exception e) {

                }
            }
        };
        event.execute();
    }
}
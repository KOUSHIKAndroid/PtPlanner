package com.ptplanner.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ptplanner.K_DataBase.Database;
import com.ptplanner.K_DataBase.LocalDataResponse;
import com.ptplanner.R;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumRegularEdit;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.CalendarEventDataType;
import com.ptplanner.datatype.DateRespectiveDiaryDataType;
import com.ptplanner.datatype.DiaryDataType;
import com.ptplanner.datatype.EventDataType;
import com.ptplanner.dialog.ShowCalendarPopUp;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.CircleTransform;
import com.ptplanner.helper.ConnectionDetector;
import com.ptplanner.helper.ReturnCalendarDetails;
import com.ptplanner.helper.Trns;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DiaryFragment extends Fragment {

    View fView;

    LinearLayout back, showCalender;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    // -- Calendar Instance
    Calendar calendar;
    int currentYear, currentDay, currentDate, firstDayPosition, previousDayPosition;
    Date date;

    int l;
    String[] positionPre = {}, positionNext = {};

    float balencing_coefficient_ = -1000.0f;
    float CENTRE_Y_OF_THE_SCREEN = 0.0f;
    float OPEN_CLOSE_BOUNDERY_Y = 0.0f;
    int c = 0;
    LinearLayout coverLayout;
    ScrollView scrollView1maincon;

    TitilliumRegular[] textViewArray, txtApp;
    RelativeLayout[] llArray;
    LinearLayout[] eventViewArr;
    TitilliumRegular txt_currentdatemonth, txt_currentyear;
    LinearLayout llHide, btnNext, btnPre;

    ArrayList<EventDataType> arrDay;
    EventDataType eventDataType;

    ShowCalendarPopUp showCalPopup;

    LinearLayout llMiddle, llCoverLayout;
    TitilliumRegular txtView;
    boolean isBottom = false;
    ViewGroup.LayoutParams params;
    RelativeLayout ll_header;

    ImageView imgClient;
    TitilliumSemiBold txtClientName, txtDate;
    LinearLayout llEditDiary, llAddDiary, llDone;
    TitilliumRegularEdit etDetails;
    ProgressBar pBar;
    ScrollView scrView;

    ConnectionDetector cd;
    String dateChange = "", formattedDate = "";
    DateRespectiveDiaryDataType dateRespectiveDiaryDataType;
    String exception = "", urlResponse = "", diaryCalendar, diaryHide;

    DiaryDataType diaryDataType;
    String[] DiaryData;
    CalendarEventDataType calEventData;
    ArrayList<DiaryDataType> diaryArrayList = new ArrayList<DiaryDataType>();
    SimpleDateFormat dateFormat;

    Date changeDate;
    LinearLayout appointmentButton, progressButton;
    RelativeLayout messageButton;

    String saveString;
    SharedPreferences userId;

    SharedPreferences dateSavePreference, dateSavePreferenceNormal;

    int todayShared = 0, toMonthShared = 0, toYearShared = 0;
    int currentMonth, indexOfDayOne, curyear;
    int INDEX_OFTHEDAY;
    int integer = 0, viewId = -1, indexInteger = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        diaryCalendar = getResources().getString(R.string.diaryFragment_calendar);
        diaryHide = getResources().getString(R.string.diaryFragment_hide);
        fView = inflater.inflate(R.layout.frag_diary, container, false);

        ////////////////////////////////////////////////////////////////
        appointmentButton = (LinearLayout) getActivity().findViewById(R.id.blockappoinmentbutton);
        progressButton = (LinearLayout) getActivity().findViewById(R.id.progressbutton);
        messageButton = (RelativeLayout) getActivity().findViewById(R.id.messagebutton);
        //////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        this.fView = inflater.inflate(R.layout.frag_diary, container, false);
        /////////////////////////////////////////////////////

        fragmentManager = getActivity().getSupportFragmentManager();

        // diary click event shared preference
        dateSavePreference = getActivity().getSharedPreferences("DateTimeDiary", Context.MODE_PRIVATE);
        dateSavePreferenceNormal = getActivity().getSharedPreferences("DateTime", Context.MODE_PRIVATE);

        cd = new ConnectionDetector(getActivity());

        back = (LinearLayout) fView.findViewById(R.id.back);
        showCalender = (LinearLayout) fView.findViewById(R.id.show_cal);

        llMiddle = (LinearLayout) fView.findViewById(R.id.middle);
        llCoverLayout = (LinearLayout) fView.findViewById(R.id.coverlayout);
        txtView = (TitilliumRegular) fView.findViewById(R.id.txt_view);
        txtView.setText(diaryCalendar);
        ll_header = (RelativeLayout) fView.findViewById(R.id.ll_header);

        txt_currentyear = (TitilliumRegular) fView.findViewById(R.id.txt_currentyear);
        txt_currentdatemonth = (TitilliumRegular) fView.findViewById(R.id.txt_currentdatemonth);
        btnPre = (LinearLayout) fView.findViewById(R.id.btn_pre);
        btnNext = (LinearLayout) fView.findViewById(R.id.btn_next);

        // calendar = Calendar.getInstance(Locale.getDefault());

        currentDate = AppConfig.calendar.get(Calendar.DATE);
        currentDay = AppConfig.calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        currentMonth = (AppConfig.calendar.get(Calendar.MONTH) + 1);
        currentYear = AppConfig.calendar.get(Calendar.YEAR);
        firstDayPosition = AppConfig.calendar.get(Calendar.DAY_OF_WEEK);


        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //showCalPopup = new ShowCalendarPopUp(getActivity(), "diary");
        userId = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        saveString = userId.getString("UserId", "");

        // ------------------- Previous Month
        btnPre.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                getLayouts();

                Calendar pre = (Calendar) AppConfig.calendar.clone();

                currentMonth--;

                if (currentMonth == 0) {
                    currentMonth = 12;
                    currentYear--;
                }
                pre.set(Calendar.MONTH, currentMonth);
                pre.set(Calendar.YEAR, currentYear);
                pre.set(Calendar.DATE, 1);

                positionPre = pre.getTime().toString().split(" ");
                previousDayPosition = ReturnCalendarDetails.getPosition(positionPre[0]);

                getCalendar(
                        ReturnCalendarDetails.getCurrentMonth(positionPre[1]),
                        ReturnCalendarDetails.getPosition(positionPre[0]),
                        Integer.parseInt(positionPre[5]));

            }
        });
        // ------------------- Next Month
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                getLayouts();

                Calendar next = (Calendar) AppConfig.calendar.clone();

                if (currentMonth > 11) {
                    currentMonth = 1;
                    currentYear++;
                } else {
                    currentMonth++;
                }

                next.set(Calendar.MONTH, currentMonth);
                next.set(Calendar.YEAR, currentYear);
                next.set(Calendar.DATE, 1);

                positionNext = next.getTime().toString().split(" ");
                getCalendar(
                        ReturnCalendarDetails.getCurrentMonth(positionNext[1]),
                        ReturnCalendarDetails.getPosition(positionNext[0]),
                        Integer.parseInt(positionNext[5]));
            }
        });
        btnPre.performClick();

        imgClient = (ImageView) fView.findViewById(R.id.img_client);

        txtClientName = (TitilliumSemiBold) fView.findViewById(R.id.txt_client_name);
        txtDate = (TitilliumSemiBold) fView.findViewById(R.id.txt_date);
//        txtDiaryHeading = (TitilliumSemiBold) fView.findViewById(R.id.txt_diaryheading);
//        txtDiaryHeading.setVisibility(View.GONE);

        llEditDiary = (LinearLayout) fView.findViewById(R.id.ll_editdiary);
        llAddDiary = (LinearLayout) fView.findViewById(R.id.ll_adddiary);
        llDone = (LinearLayout) fView.findViewById(R.id.ll_done);

        etDetails = (TitilliumRegularEdit) fView.findViewById(R.id.et_details);
        etDetails.clearFocus();
        pBar = (ProgressBar) fView.findViewById(R.id.pbar);
        pBar.setVisibility(View.GONE);

        scrView = (ScrollView) fView.findViewById(R.id.scrollView1maincon);
        scrView.setVisibility(View.GONE);

        llEditDiary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etDetails.setEnabled(true);
                etDetails.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etDetails, InputMethodManager.SHOW_IMPLICIT);
                llDone.setVisibility(View.VISIBLE);
            }
        });

        llAddDiary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etDetails.setEnabled(true);
                etDetails.setText("");
                etDetails.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etDetails, InputMethodManager.SHOW_IMPLICIT);
                llDone.setVisibility(View.VISIBLE);
            }
        });

        llDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if(etDetails.getText().toString().matches("")){
                if (!etDetails.getText().toString().equals("")) {
                    String string = etDetails.getText().toString();

                    String stringDate = txtDate.getText().toString();
                    String newString = string.replaceAll(" ", "%20");
                    newString = URLEncoder.encode(string);

                    //// addDiary(etDetails.getText().toString(), txtDate.getText().toString());
                    addDiary(newString, stringDate);

//                    Log.d("Message", newString);

                } else {
                    //  addDiary(etDetails.getText().toString(),txtDate.getText().toString());

                }
            }
        });

//        if (cd.isConnectingToInternet()) {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            getDiaryDetails(dateFormat.format(calendar.getTime()));
//        } else {
//            Toast.makeText(getActivity(), "No internet coinnection.", Toast.LENGTH_SHORT).show();
//        }
        if (cd.isConnectingToInternet()) {
            try {
                changeDate = dateFormat.parse(getArguments().getString("DateChange"));

//                Log.d("DAY==", getArguments().getString("DateChange"));

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(getArguments().getString("DateChange")));
                AppConfig.calendar = cal;

                getDiaryDetails(getArguments().getString("DateChange"));

            } catch (Exception e) {
                Log.d("Date Exception : ", e.toString());
                getDiaryDetails(dateFormat.format(AppConfig.calendar.getTime()));
            }
            //getDietList("2015-07-03");
        } else {
            try {
                changeDate = dateFormat.parse(getArguments().getString("DateChange"));

//                Log.d("DAY==", getArguments().getString("DateChange"));

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(getArguments().getString("DateChange")));
                AppConfig.calendar = cal;

                OffLineData(getArguments().getString("DateChange"));

            } catch (Exception e) {
                Log.d("Date Exception : ", e.toString());
                OffLineData(dateFormat.format(AppConfig.calendar.getTime()));
            }
        }

        params = llCoverLayout.getLayoutParams();
        showCalender.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (isBottom) {
                    llCoverLayout.clearAnimation();
                    TranslateAnimation tAnim = new TranslateAnimation(0.0f, 0.0f, (llCoverLayout.getY()), 0.0f);
                    tAnim.setDuration(500);
                    tAnim.setFillAfter(true);
                    tAnim.setInterpolator(new AnticipateOvershootInterpolator(1.0f, 1.0f));
                    llCoverLayout.startAnimation(tAnim);
                    tAnim.setAnimationListener(new
                                                       Animation.AnimationListener() {

                                                           @Override
                                                           public void onAnimationEnd(Animation arg0) {

                                                               llCoverLayout.clearAnimation();
                                                               llCoverLayout.setY(ll_header.getHeight());

                                                               txtView.setText(diaryCalendar);
                                                               isBottom = false;

                                                               params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                                                               llCoverLayout.setLayoutParams(params);
                                                           }

                                                           @Override
                                                           public void onAnimationRepeat(Animation arg0) { // TODO


                                                           }

                                                           @Override
                                                           public void onAnimationStart(Animation arg0) { // TODO


                                                           }

                                                       });
                } else {
                    llCoverLayout.clearAnimation();
                    TranslateAnimation tAnim = new TranslateAnimation(0.0f, 0.0f, 0.0f, llMiddle.getHeight());
                    tAnim.setDuration(500);
                    tAnim.setFillAfter(true);
                    tAnim.setInterpolator(new AnticipateOvershootInterpolator(1.0f, 1.0f));
                    llCoverLayout.startAnimation(tAnim);
                    tAnim.setAnimationListener(new
                                                       Animation.AnimationListener() {

                                                           @Override
                                                           public void onAnimationEnd(Animation arg0) {

                                                               llCoverLayout.clearAnimation();
                                                               llCoverLayout.setY((llMiddle.getHeight() + ll_header.getHeight()));

                                                               txtView.setText(diaryHide);
                                                               isBottom = true;

                                                               params.height = llCoverLayout.getHeight() -
                                                                       (llMiddle.getHeight() - ll_header.getHeight());

                                                               llCoverLayout.setLayoutParams(params);
                                                           }

                                                           @Override
                                                           public void onAnimationRepeat(Animation arg0) { // TODO


                                                           }

                                                           @Override
                                                           public void onAnimationStart(Animation arg0) { // TODO


                                                           }

                                                       });
                }
            }

        });


        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

//                SharedPreferences.Editor editor = dateSavePreference.edit();
//                editor.clear();
//                editor.commit();


                Bundle bundle = new Bundle();
                bundle.putString("DateChange", getArguments().getString("DateChange"));

                fragmentTransaction = fragmentManager.beginTransaction();
                CalenderFragment cal_fragment = new CalenderFragment();
                cal_fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
//                int count = fragmentManager.getBackStackEntryCount();
//                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        return fView;
    }

    private void OffLineData(final String dateChange) {
        pBar.setVisibility(View.VISIBLE);
        llEditDiary.setVisibility(View.GONE);
        llAddDiary.setVisibility(View.GONE);
        ////////////////////////////////////////////
        progressButton.setClickable(false);
        progressButton.setEnabled(false);
        appointmentButton.setClickable(false);
        appointmentButton.setEnabled(false);
        messageButton.setClickable(false);
        messageButton.setEnabled(false);
        ///////////////////////////////////////////
        scrView.setVisibility(View.GONE);

        llAddDiary.setVisibility(View.GONE);
        llEditDiary.setVisibility(View.GONE);
        llDone.setVisibility(View.GONE);
        etDetails.setEnabled(false);

        new Database(getActivity()).GET_Diary_Frag_Fetails(dateChange, new LocalDataResponse() {
            @Override
            public void OnSuccess(String Response) {
                try {
                    JSONObject jOBJ = new JSONObject(Response);
                    Log.i("jOBJ",""+jOBJ);
                    dateRespectiveDiaryDataType = new DateRespectiveDiaryDataType(
                            jOBJ.getString("client_id"),
                            jOBJ.getString("client_name"),
                            jOBJ.getString("client_image"),
                            jOBJ.getString("client_email"),
                            jOBJ.getString("client_about"),
                            jOBJ.getString("diary_id"),
                            jOBJ.getString("diary_heading"),
                            jOBJ.getString("dairy_text")
                    );
                    pBar.setVisibility(View.GONE);
                    scrView.setVisibility(View.VISIBLE);
                    ///////////////////////////////////////////////////
                    progressButton.setClickable(true);
                    progressButton.setEnabled(true);
                    appointmentButton.setClickable(true);
                    appointmentButton.setEnabled(true);
                    messageButton.setClickable(true);
                    messageButton.setEnabled(true);

                    //////////////////////////////////////////////////

//                        Glide.with(getActivity()).load(dateRespectiveDiaryDataType.getClient_image()).placeholder(R.drawable.no_image_available_placeholdder)
//                                .transform(new Trns()).resize(400, 400).centerInside().into(imgClient);
Glide.with(getActivity()).load(dateRespectiveDiaryDataType.getClient_image()).placeholder(R.drawable.no_image_available_placeholdder)
                                .transform(new CircleTransform(getActivity())).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgClient);

                        txtClientName.setText(dateRespectiveDiaryDataType.getClient_name());
                        txtDate.setText(dateChange);

//                    if (dateRespectiveDiaryDataType.getDiary_heading().equals("")) {
//                        txtDiaryHeading.setVisibility(View.GONE);
//                    } else {
//                        txtDiaryHeading.setVisibility(View.GONE);
//                        txtDiaryHeading.setText(dateRespectiveDiaryDataType.getDiary_heading());
//                    }

                        if (dateRespectiveDiaryDataType.getDairy_text().equals("")) {
                            etDetails.setText(getResources().getString(R.string.fra_diary_longString));

                        } else {
//                        Log.i("!! Diary Text : ", dateRespectiveDiaryDataType.getDairy_text());
                            etDetails.setText(dateRespectiveDiaryDataType.getDairy_text());

                        }


                } catch (Exception ex) {
                    exception = ex.toString();
                }
            }

            @Override
            public void OnNotfound(String NotFound) {

            }
        });


    }

    public void getDiaryDetails(final String dateVal) {
        AsyncTask<Void, Void, Void> dietListDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
                ////////////////////////////////////////////
                progressButton.setClickable(false);
                progressButton.setEnabled(false);
                appointmentButton.setClickable(false);
                appointmentButton.setEnabled(false);
                messageButton.setClickable(false);
                messageButton.setEnabled(false);
                ///////////////////////////////////////////
                scrView.setVisibility(View.GONE);

                llAddDiary.setVisibility(View.GONE);
                llEditDiary.setVisibility(View.GONE);
                llDone.setVisibility(View.GONE);
                etDetails.setEnabled(false);


            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/get_date_respective_diary?logged_in_user=" + saveString
                                    + "&date_val=" + dateVal)
                            .build();



                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    try {
                        JSONObject jOBJ = new JSONObject(urlResponse);
                        Log.i("jOBJ",""+jOBJ);
                        dateRespectiveDiaryDataType = new DateRespectiveDiaryDataType(
                                jOBJ.getString("client_id"),
                                jOBJ.getString("client_name"),
                                jOBJ.getString("client_image"),
                                jOBJ.getString("client_email"),
                                jOBJ.getString("client_about"),
                                jOBJ.getString("diary_id"),
                                jOBJ.getString("diary_heading"),
                                jOBJ.getString("dairy_text")
                        );

                    } catch (Exception ex) {
                        exception = ex.toString();
                    }


                    Log.d("Diary @@ URL : ", "http://esolz.co.in/lab6/ptplanner/app_control/get_date_respective_diary?logged_in_user=" + saveString
                            + "&date_val=" + dateVal);

                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pBar.setVisibility(View.GONE);
                scrView.setVisibility(View.VISIBLE);
                ///////////////////////////////////////////////////
                progressButton.setClickable(true);
                progressButton.setEnabled(true);
                appointmentButton.setClickable(true);
                appointmentButton.setEnabled(true);
                messageButton.setClickable(true);
                messageButton.setEnabled(true);

                //////////////////////////////////////////////////
                if (exception.equals("")) {
                    Picasso.with(getActivity()).load(dateRespectiveDiaryDataType.getClient_image()).placeholder(R.drawable.no_image_available_placeholdder)
                            .transform(new Trns()).resize(400, 400).centerInside().into(imgClient);

                    txtClientName.setText(dateRespectiveDiaryDataType.getClient_name());
                    txtDate.setText(dateVal);

//                    if (dateRespectiveDiaryDataType.getDiary_heading().equals("")) {
//                        txtDiaryHeading.setVisibility(View.GONE);
//                    } else {
//                        txtDiaryHeading.setVisibility(View.GONE);
//                        txtDiaryHeading.setText(dateRespectiveDiaryDataType.getDiary_heading());
//                    }

                    if (dateRespectiveDiaryDataType.getDairy_text().equals("")) {
                        etDetails.setText(getResources().getString(R.string.fra_diary_longString));
                        llEditDiary.setVisibility(View.GONE);
                        llAddDiary.setVisibility(View.VISIBLE);
                    } else {
//                        Log.i("!! Diary Text : ", dateRespectiveDiaryDataType.getDairy_text());
                        etDetails.setText(dateRespectiveDiaryDataType.getDairy_text());
                        llEditDiary.setVisibility(View.VISIBLE);
                        llAddDiary.setVisibility(View.GONE);
                    }

                } else {
                    // Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }
        };
        dietListDetails.execute();
    }

    public void getCalendar(int currentMonth, int indexOfDayOne, int curyear) {
        INDEX_OFTHEDAY = indexOfDayOne;
        int i = 0, j = 0, k = 0, day = 1;
        int currentMonthLength = ReturnCalendarDetails.getMonthLenth(currentMonth, curyear);

        final int MONTH = currentMonth;
        final int YEAR = curyear;

        final Calendar current = (Calendar) AppConfig.calendar.clone();

        current.set(Calendar.MONTH, currentMonth - 1);
        current.set(Calendar.YEAR, curyear);
        current.set(Calendar.DATE, currentDate);

        SimpleDateFormat sformat = new SimpleDateFormat("MMMM");

        txt_currentdatemonth.setText("" + ReturnCalendarDetails.getCurrentMonthName(currentMonth));
        txt_currentyear.setText("" + curyear);

        Calendar checkingCal = Calendar.getInstance();

        final int today = checkingCal.get(Calendar.DATE);
        final int toMonth = checkingCal.get(Calendar.MONTH);
        final int toYear = checkingCal.get(Calendar.YEAR);

        drawEvent(indexOfDayOne);
        if (!dateSavePreference.getString("ClickDatePreference", "").equals("")) {
            String[] dateArr = dateSavePreference.getString("ClickDatePreference", "").split("-");

            toYearShared = Integer.parseInt(dateArr[0]);
            toMonthShared = ReturnCalendarDetails.getCurrentMonth(dateArr[1].substring(0, 3));
            todayShared = Integer.parseInt(dateArr[2]);
        } else {

        }

        // --- Set Current Month
        for (i = indexOfDayOne; i < (currentMonthLength + indexOfDayOne); i++) {
            textViewArray[i].setText("" + day);
            textViewArray[i].setTag("" + day);

            textViewArray[i].setEnabled(true);
            textViewArray[i].setClickable(true);

            textViewArray[i].setTextColor(Color.BLACK);
            day++;


            if (integer != currentMonth) {
                textViewArray[i].setBackgroundResource(0);

            } else {
                if (viewId == -1) {
                } else if (viewId != -1)
                    textViewArray[viewId].setBackgroundResource(R.drawable.selected_day);
            }

            if (curyear == toYear) {
                if (currentMonth == (toMonth + 1)) {
                    if (textViewArray[(today + indexOfDayOne) - 1].getText().toString().equals("" + today)) {
                        textViewArray[(today + indexOfDayOne) - 1].setTextColor(Color.parseColor("#FF0000"));
                        textViewArray[(today + indexOfDayOne) - 1].setBackgroundResource(R.drawable.selected_day);
                    } else {
                        textViewArray[(today + indexOfDayOne) - 1].setTextColor(Color.parseColor("#000000"));
                        //textViewArray[(today + indexOfDayOne) - 1].setBackgroundResource(0);
                    }
                }
            }


//            if (toYearShared != 0 && toMonthShared != 0 && todayShared != 0) {
//                if (curyear == toYearShared) {
//                    if (currentMonth == toMonthShared) {
//                        textViewArray[(todayShared + indexOfDayOne) - 1].setBackgroundResource(R.drawable.selected_day);
//                        indexInteger = indexOfDayOne;
//
//                    }
//                }
//
//            }

            if (toYearShared != 0 && toMonthShared != 0 && todayShared != 0) {
                if (curyear == toYearShared) {
                    if (currentMonth == toMonthShared) {
                        textViewArray[(todayShared + indexOfDayOne) - 1].setBackgroundResource(R.drawable.selected_day);
                        indexInteger = indexOfDayOne;
                    }
                }

            }

            textViewArray[i].setOnClickListener(new OnClickListener() {
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
                    String formattedDateDiary = targetFormat.format(date);
                    AppConfig.changeDate = formattedDateDiary;
                    AppConfig.bookAppDateChange = formattedDateDiary;

                    SharedPreferences.Editor editorN = dateSavePreferenceNormal.edit();
                    editorN.clear();
                    editorN.putString("ClickDatePreference", "" + dateChange);
                    editorN.commit();

                    SharedPreferences.Editor editor = dateSavePreference.edit();
                    editor.clear();
                    editor.putString("ClickDatePreference", "" + dateChange);
                    editor.commit();


                    int index = 0;
                    for (int i = 0; i < textViewArray.length; i++) {
                        if (textViewArray[i].getId() == view.getId()) {

                            index = i;
                            viewId = i;
                            textViewArray[index].setBackgroundResource(R.drawable.selected_day);
                        } else {
                            if (textViewArray[(today + INDEX_OFTHEDAY) - 1].getId() == textViewArray[i].getId()) {
                                if (YEAR == toYear) {
                                    if (MONTH == (toMonth + 1)) {
                                        if (textViewArray[(today + INDEX_OFTHEDAY) - 1].getText().toString().equals("" + today)) {
                                            textViewArray[(today + INDEX_OFTHEDAY) - 1].setBackgroundResource(R.drawable.selected_day);
                                        } else {
                                            textViewArray[(today + INDEX_OFTHEDAY) - 1].setBackgroundResource(0);
                                        }
                                    }
                                }
                            } else {
                                textViewArray[i].setBackgroundResource(0);
                            }
                        }

                    }


                    formattedDate = targetFormat.format(date);
                    // Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_LONG).show();
//                    getDiaryDetails(formattedDate);

                    llCoverLayout.clearAnimation();
                    TranslateAnimation tAnim = new TranslateAnimation(0.0f, 0.0f,
                            (llCoverLayout.getY()), 0.0f);
                    tAnim.setDuration(500);
                    tAnim.setFillAfter(true);
                    tAnim.setInterpolator(new AnticipateOvershootInterpolator(1.0f, 1.0f));
                    llCoverLayout.startAnimation(tAnim);
                    tAnim.setAnimationListener(new
                                                       Animation.AnimationListener() {

                                                           @Override
                                                           public void onAnimationEnd(Animation arg0) {

                                                               llCoverLayout.clearAnimation();
                                                               llCoverLayout.setY(ll_header.getHeight());

                                                               txtView.setText("Kalender");
                                                               isBottom = false;

                                                               params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                                                               llCoverLayout.setLayoutParams(params);

                                                               getDiaryDetails(formattedDate);
                                                           }

                                                           @Override
                                                           public void onAnimationRepeat(Animation arg0) { // TODO


                                                           }

                                                           @Override
                                                           public void onAnimationStart(Animation arg0) { // TODO


                                                           }

                                                       });

                }
            });
        }
        day = 1;

        // --- Set Next Month

        for (j = i; j < textViewArray.length; j++) {
//            textViewArray[j].setText("" + day);
            textViewArray[j].setText("");
//            textViewArray[j].setTextColor(Color.parseColor("#BDBDBD"));
            textViewArray[j].setBackgroundResource(0);
            textViewArray[j].setEnabled(false);
            textViewArray[j].setClickable(false);
            llArray[j].setBackgroundResource(0);
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
//                textViewArray[k].setTextColor(Color.parseColor("#BDBDBD"));
                textViewArray[k].setBackgroundResource(0);
                textViewArray[k].setEnabled(false);
                textViewArray[k].setClickable(false);
                llArray[k].setBackgroundResource(0);
                tempcurrentMonthLength--;
            }
        }


    }

    public void drawEvent(int indexOfDayOne) {
        arrDay = new ArrayList<EventDataType>();
        arrDay.clear();
        getAllEvent(indexOfDayOne);
    }

    public void getLayouts() {
        llArray = new RelativeLayout[42];
        llArray[0] = (RelativeLayout) fView.findViewById(R.id.ll7);
        llArray[1] = (RelativeLayout) fView.findViewById(R.id.ll8);
        llArray[2] = (RelativeLayout) fView.findViewById(R.id.ll9);
        llArray[3] = (RelativeLayout) fView.findViewById(R.id.ll10);
        llArray[4] = (RelativeLayout) fView.findViewById(R.id.ll11);
        llArray[5] = (RelativeLayout) fView.findViewById(R.id.ll12);
        llArray[6] = (RelativeLayout) fView.findViewById(R.id.ll13);
        llArray[7] = (RelativeLayout) fView.findViewById(R.id.ll14);
        llArray[8] = (RelativeLayout) fView.findViewById(R.id.ll15);
        llArray[9] = (RelativeLayout) fView.findViewById(R.id.ll16);
        llArray[10] = (RelativeLayout) fView.findViewById(R.id.ll17);
        llArray[11] = (RelativeLayout) fView.findViewById(R.id.ll18);
        llArray[12] = (RelativeLayout) fView.findViewById(R.id.ll19);
        llArray[13] = (RelativeLayout) fView.findViewById(R.id.ll20);
        llArray[14] = (RelativeLayout) fView.findViewById(R.id.ll21);
        llArray[15] = (RelativeLayout) fView.findViewById(R.id.ll22);
        llArray[16] = (RelativeLayout) fView.findViewById(R.id.ll23);
        llArray[17] = (RelativeLayout) fView.findViewById(R.id.ll24);
        llArray[18] = (RelativeLayout) fView.findViewById(R.id.ll25);
        llArray[19] = (RelativeLayout) fView.findViewById(R.id.ll26);
        llArray[20] = (RelativeLayout) fView.findViewById(R.id.ll27);
        llArray[21] = (RelativeLayout) fView.findViewById(R.id.ll28);
        llArray[22] = (RelativeLayout) fView.findViewById(R.id.ll29);
        llArray[23] = (RelativeLayout) fView.findViewById(R.id.ll30);
        llArray[24] = (RelativeLayout) fView.findViewById(R.id.ll31);
        llArray[25] = (RelativeLayout) fView.findViewById(R.id.ll32);
        llArray[26] = (RelativeLayout) fView.findViewById(R.id.ll33);
        llArray[27] = (RelativeLayout) fView.findViewById(R.id.ll34);
        llArray[28] = (RelativeLayout) fView.findViewById(R.id.ll35);
        llArray[29] = (RelativeLayout) fView.findViewById(R.id.ll36);
        llArray[30] = (RelativeLayout) fView.findViewById(R.id.ll37);
        llArray[31] = (RelativeLayout) fView.findViewById(R.id.ll38);
        llArray[32] = (RelativeLayout) fView.findViewById(R.id.ll39);
        llArray[33] = (RelativeLayout) fView.findViewById(R.id.ll40);
        llArray[34] = (RelativeLayout) fView.findViewById(R.id.ll41);
        llArray[35] = (RelativeLayout) fView.findViewById(R.id.ll42);
        llArray[36] = (RelativeLayout) fView.findViewById(R.id.ll43);
        llArray[37] = (RelativeLayout) fView.findViewById(R.id.ll44);
        llArray[38] = (RelativeLayout) fView.findViewById(R.id.ll45);
        llArray[39] = (RelativeLayout) fView.findViewById(R.id.ll46);
        llArray[40] = (RelativeLayout) fView.findViewById(R.id.ll47);
        llArray[41] = (RelativeLayout) fView.findViewById(R.id.ll48);

        for (int a = 0; a < llArray.length; a++) {
            llArray[a].setBackgroundColor(Color.TRANSPARENT);
        }

        textViewArray = new TitilliumRegular[42];
        textViewArray[0] = (TitilliumRegular) fView.findViewById(R.id.txt7);
        textViewArray[1] = (TitilliumRegular) fView.findViewById(R.id.txt8);
        textViewArray[2] = (TitilliumRegular) fView.findViewById(R.id.txt9);
        textViewArray[3] = (TitilliumRegular) fView.findViewById(R.id.txt10);
        textViewArray[4] = (TitilliumRegular) fView.findViewById(R.id.txt11);
        textViewArray[5] = (TitilliumRegular) fView.findViewById(R.id.txt12);
        textViewArray[6] = (TitilliumRegular) fView.findViewById(R.id.txt13);
        textViewArray[7] = (TitilliumRegular) fView.findViewById(R.id.txt14);
        textViewArray[8] = (TitilliumRegular) fView.findViewById(R.id.txt15);
        textViewArray[9] = (TitilliumRegular) fView.findViewById(R.id.txt16);
        textViewArray[10] = (TitilliumRegular) fView.findViewById(R.id.txt17);
        textViewArray[11] = (TitilliumRegular) fView.findViewById(R.id.txt18);
        textViewArray[12] = (TitilliumRegular) fView.findViewById(R.id.txt19);
        textViewArray[13] = (TitilliumRegular) fView.findViewById(R.id.txt20);
        textViewArray[14] = (TitilliumRegular) fView.findViewById(R.id.txt21);
        textViewArray[15] = (TitilliumRegular) fView.findViewById(R.id.txt22);
        textViewArray[16] = (TitilliumRegular) fView.findViewById(R.id.txt23);
        textViewArray[17] = (TitilliumRegular) fView.findViewById(R.id.txt24);
        textViewArray[18] = (TitilliumRegular) fView.findViewById(R.id.txt25);
        textViewArray[19] = (TitilliumRegular) fView.findViewById(R.id.txt26);
        textViewArray[20] = (TitilliumRegular) fView.findViewById(R.id.txt27);
        textViewArray[21] = (TitilliumRegular) fView.findViewById(R.id.txt28);
        textViewArray[22] = (TitilliumRegular) fView.findViewById(R.id.txt29);
        textViewArray[23] = (TitilliumRegular) fView.findViewById(R.id.txt30);
        textViewArray[24] = (TitilliumRegular) fView.findViewById(R.id.txt31);
        textViewArray[25] = (TitilliumRegular) fView.findViewById(R.id.txt32);
        textViewArray[26] = (TitilliumRegular) fView.findViewById(R.id.txt33);
        textViewArray[27] = (TitilliumRegular) fView.findViewById(R.id.txt34);
        textViewArray[28] = (TitilliumRegular) fView.findViewById(R.id.txt35);
        textViewArray[29] = (TitilliumRegular) fView.findViewById(R.id.txt36);
        textViewArray[30] = (TitilliumRegular) fView.findViewById(R.id.txt37);
        textViewArray[31] = (TitilliumRegular) fView.findViewById(R.id.txt38);
        textViewArray[32] = (TitilliumRegular) fView.findViewById(R.id.txt39);
        textViewArray[33] = (TitilliumRegular) fView.findViewById(R.id.txt40);
        textViewArray[34] = (TitilliumRegular) fView.findViewById(R.id.txt41);
        textViewArray[35] = (TitilliumRegular) fView.findViewById(R.id.txt42);
        textViewArray[36] = (TitilliumRegular) fView.findViewById(R.id.txt43);
        textViewArray[37] = (TitilliumRegular) fView.findViewById(R.id.txt44);
        textViewArray[38] = (TitilliumRegular) fView.findViewById(R.id.txt45);
        textViewArray[39] = (TitilliumRegular) fView.findViewById(R.id.txt46);
        textViewArray[40] = (TitilliumRegular) fView.findViewById(R.id.txt47);
        textViewArray[41] = (TitilliumRegular) fView.findViewById(R.id.txt48);

		/*
         * for (int i = 0; i < textViewArray.length; i++) {
		 * textViewArray[i].setOnClickListener(getActivity()); }
		 */

        eventViewArr = new LinearLayout[42];
        eventViewArr[0] = (LinearLayout) fView.findViewById(R.id.event_style1);
        eventViewArr[1] = (LinearLayout) fView.findViewById(R.id.event_style2);
        eventViewArr[2] = (LinearLayout) fView.findViewById(R.id.event_style3);
        eventViewArr[3] = (LinearLayout) fView.findViewById(R.id.event_style4);
        eventViewArr[4] = (LinearLayout) fView.findViewById(R.id.event_style5);
        eventViewArr[5] = (LinearLayout) fView.findViewById(R.id.event_style6);
        eventViewArr[6] = (LinearLayout) fView.findViewById(R.id.event_style7);
        eventViewArr[7] = (LinearLayout) fView.findViewById(R.id.event_style8);
        eventViewArr[8] = (LinearLayout) fView.findViewById(R.id.event_style9);
        eventViewArr[9] = (LinearLayout) fView.findViewById(R.id.event_style10);
        eventViewArr[10] = (LinearLayout) fView
                .findViewById(R.id.event_style11);
        eventViewArr[11] = (LinearLayout) fView
                .findViewById(R.id.event_style12);
        eventViewArr[12] = (LinearLayout) fView
                .findViewById(R.id.event_style13);
        eventViewArr[13] = (LinearLayout) fView
                .findViewById(R.id.event_style14);
        eventViewArr[14] = (LinearLayout) fView
                .findViewById(R.id.event_style15);
        eventViewArr[15] = (LinearLayout) fView
                .findViewById(R.id.event_style16);
        eventViewArr[16] = (LinearLayout) fView
                .findViewById(R.id.event_style17);
        eventViewArr[17] = (LinearLayout) fView
                .findViewById(R.id.event_style18);
        eventViewArr[18] = (LinearLayout) fView
                .findViewById(R.id.event_style19);
        eventViewArr[19] = (LinearLayout) fView
                .findViewById(R.id.event_style20);
        eventViewArr[20] = (LinearLayout) fView
                .findViewById(R.id.event_style21);
        eventViewArr[21] = (LinearLayout) fView
                .findViewById(R.id.event_style22);
        eventViewArr[22] = (LinearLayout) fView
                .findViewById(R.id.event_style23);
        eventViewArr[23] = (LinearLayout) fView
                .findViewById(R.id.event_style24);
        eventViewArr[24] = (LinearLayout) fView
                .findViewById(R.id.event_style25);
        eventViewArr[25] = (LinearLayout) fView
                .findViewById(R.id.event_style26);
        eventViewArr[26] = (LinearLayout) fView
                .findViewById(R.id.event_style27);
        eventViewArr[27] = (LinearLayout) fView
                .findViewById(R.id.event_style28);
        eventViewArr[28] = (LinearLayout) fView
                .findViewById(R.id.event_style29);
        eventViewArr[29] = (LinearLayout) fView
                .findViewById(R.id.event_style30);
        eventViewArr[30] = (LinearLayout) fView
                .findViewById(R.id.event_style31);
        eventViewArr[31] = (LinearLayout) fView
                .findViewById(R.id.event_style32);
        eventViewArr[32] = (LinearLayout) fView
                .findViewById(R.id.event_style33);
        eventViewArr[33] = (LinearLayout) fView
                .findViewById(R.id.event_style34);
        eventViewArr[34] = (LinearLayout) fView
                .findViewById(R.id.event_style35);
        eventViewArr[35] = (LinearLayout) fView
                .findViewById(R.id.event_style36);
        eventViewArr[36] = (LinearLayout) fView
                .findViewById(R.id.event_style37);
        eventViewArr[37] = (LinearLayout) fView
                .findViewById(R.id.event_style38);
        eventViewArr[38] = (LinearLayout) fView
                .findViewById(R.id.event_style39);
        eventViewArr[39] = (LinearLayout) fView
                .findViewById(R.id.event_style40);
        eventViewArr[40] = (LinearLayout) fView
                .findViewById(R.id.event_style41);
        eventViewArr[41] = (LinearLayout) fView
                .findViewById(R.id.event_style42);

        for (int i = 0; i < eventViewArr.length; i++) {
            eventViewArr[i].setVisibility(View.GONE);
        }

        txtApp = new TitilliumRegular[42];
        txtApp[0] = (TitilliumRegular) fView.findViewById(R.id.app1);
        txtApp[1] = (TitilliumRegular) fView.findViewById(R.id.app2);
        txtApp[2] = (TitilliumRegular) fView.findViewById(R.id.app3);
        txtApp[3] = (TitilliumRegular) fView.findViewById(R.id.app4);
        txtApp[4] = (TitilliumRegular) fView.findViewById(R.id.app5);
        txtApp[5] = (TitilliumRegular) fView.findViewById(R.id.app6);
        txtApp[6] = (TitilliumRegular) fView.findViewById(R.id.app7);
        txtApp[7] = (TitilliumRegular) fView.findViewById(R.id.app8);
        txtApp[8] = (TitilliumRegular) fView.findViewById(R.id.app9);
        txtApp[9] = (TitilliumRegular) fView.findViewById(R.id.app10);
        txtApp[10] = (TitilliumRegular) fView.findViewById(R.id.app11);
        txtApp[11] = (TitilliumRegular) fView.findViewById(R.id.app12);
        txtApp[12] = (TitilliumRegular) fView.findViewById(R.id.app13);
        txtApp[13] = (TitilliumRegular) fView.findViewById(R.id.app14);
        txtApp[14] = (TitilliumRegular) fView.findViewById(R.id.app15);
        txtApp[15] = (TitilliumRegular) fView.findViewById(R.id.app16);
        txtApp[16] = (TitilliumRegular) fView.findViewById(R.id.app17);
        txtApp[17] = (TitilliumRegular) fView.findViewById(R.id.app18);
        txtApp[18] = (TitilliumRegular) fView.findViewById(R.id.app19);
        txtApp[19] = (TitilliumRegular) fView.findViewById(R.id.app20);
        txtApp[20] = (TitilliumRegular) fView.findViewById(R.id.app21);
        txtApp[21] = (TitilliumRegular) fView.findViewById(R.id.app22);
        txtApp[22] = (TitilliumRegular) fView.findViewById(R.id.app23);
        txtApp[23] = (TitilliumRegular) fView.findViewById(R.id.app24);
        txtApp[24] = (TitilliumRegular) fView.findViewById(R.id.app25);
        txtApp[25] = (TitilliumRegular) fView.findViewById(R.id.app26);
        txtApp[26] = (TitilliumRegular) fView.findViewById(R.id.app27);
        txtApp[27] = (TitilliumRegular) fView.findViewById(R.id.app28);
        txtApp[28] = (TitilliumRegular) fView.findViewById(R.id.app29);
        txtApp[29] = (TitilliumRegular) fView.findViewById(R.id.app30);
        txtApp[30] = (TitilliumRegular) fView.findViewById(R.id.app31);
        txtApp[31] = (TitilliumRegular) fView.findViewById(R.id.app32);
        txtApp[32] = (TitilliumRegular) fView.findViewById(R.id.app33);
        txtApp[33] = (TitilliumRegular) fView.findViewById(R.id.app34);
        txtApp[34] = (TitilliumRegular) fView.findViewById(R.id.app35);
        txtApp[35] = (TitilliumRegular) fView.findViewById(R.id.app36);
        txtApp[36] = (TitilliumRegular) fView.findViewById(R.id.app37);
        txtApp[37] = (TitilliumRegular) fView.findViewById(R.id.app38);
        txtApp[38] = (TitilliumRegular) fView.findViewById(R.id.app39);
        txtApp[39] = (TitilliumRegular) fView.findViewById(R.id.app40);
        txtApp[40] = (TitilliumRegular) fView.findViewById(R.id.app41);
        txtApp[41] = (TitilliumRegular) fView.findViewById(R.id.app42);

        for (int i = 0; i < txtApp.length; i++) {
            txtApp[i].setVisibility(View.GONE);
        }

    }

    public void addDiary(final String diaryValue, final String dateVAl) {
        AsyncTask<Void, Void, Void> diaryAdd = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
                llDone.setVisibility(View.GONE);
                etDetails.setEnabled(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {

                    exception = "";
                    urlResponse = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/add_date_respective_diary?logged_in_user=" + saveString + "&date_val=" + dateVAl + "&diary_heading=&diary_text=" + diaryValue)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ",""+jOBJ);

                    Log.i("add_date_resc_diary_Url",AppConfig.HOST + "app_control/add_date_respective_diary?logged_in_user=" + saveString + "&date_val=" + dateVAl + "&diary_heading=&diary_text=" + diaryValue);

                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                llEditDiary.setVisibility(View.VISIBLE);
                llAddDiary.setVisibility(View.GONE);
                pBar.setVisibility(View.GONE);
                getAllEvent(INDEX_OFTHEDAY);
                scrView.setVisibility(View.VISIBLE);
                if (exception.equals("")) {

                } else {
                    // Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        diaryAdd.execute();
    }

    public void getAllEvent(final int indexOfDayOne) {

        AsyncTask<Void, Void, Void> event = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
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
                                    + saveString)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ",""+jOBJ);

                    Log.i("mark_calender_Url",""+AppConfig.HOST
                            + "app_control/mark_calender?client_id="
                            + saveString);
                    JSONArray jArrDiary = jOBJ.getJSONArray("diary_date");
                    calEventData = new CalendarEventDataType();
                    for (int k = 0; k < jArrDiary.length(); k++) {
                        calEventData.setDiary_date(jArrDiary.getString(k));

                        DiaryData = jArrDiary.getString(k).split("-");
                        diaryDataType = new DiaryDataType(DiaryData[2], DiaryData[1], DiaryData[0]);

                        diaryArrayList.add(diaryDataType);
                    }

                } catch (Exception e) {
                    exception = e.toString();
                }

//                Log.i("@@MArk Cal Url : ", AppConfig.HOST + "app_control/mark_calender?client_id=" + saveString);

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (exception.equals("")) {
                    for (int i = 0; i < diaryArrayList.size(); i++) {
                        if (diaryArrayList.get(i).getYear().equals(txt_currentyear.getText().toString())) {
                            DateFormat originalFormat = new SimpleDateFormat("MM");
                            DateFormat targetFormat = new SimpleDateFormat("MMMM");
                            try {
                                date = originalFormat.parse(diaryArrayList.get(i).getMonth());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String formattedMonth = targetFormat.format(date);
                            if (formattedMonth.equalsIgnoreCase(txt_currentdatemonth.getText().toString())) {
                                eventDataType = new EventDataType(diaryArrayList.get(i).getDay(), "diary", false);
                                arrDay.add(eventDataType);
                            }
                        }
                    }

                    for (int i = 0; i < arrDay.size(); i++) {
                        for (int j = 0; j < textViewArray.length; j++) {

                            String textValue = "";

                            if (textViewArray[j].getText().toString().length() < 2) {
                                textValue = "0" + textViewArray[j].getText().toString();
                            } else {
                                textValue = textViewArray[j].getText().toString();
                            }
                            if (textValue.equals(arrDay.get(i).getMarkedDay())) {
                                llArray[(indexOfDayOne - 1)
                                        + Integer.parseInt(arrDay.get(i).getMarkedDay())]
                                        .setBackgroundResource(R.drawable.pen);
                            }
                        }

                    }
                } else {
                    System.out.println("@@ Exception: " + exception);
                    // Toast.makeText(context, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        event.execute();
    }

}

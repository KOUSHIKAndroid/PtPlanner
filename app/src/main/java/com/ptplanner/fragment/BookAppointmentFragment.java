package com.ptplanner.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ptplanner.R;
import com.ptplanner.adapter.AllTrainerAdapter;
import com.ptplanner.adapter.BookAppointAdapter;
import com.ptplanner.customviews.HelveticaHeavy;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.AltrainerDataType;
import com.ptplanner.datatype.AppointDataType;
import com.ptplanner.datatype.AvailableDateDataType;
import com.ptplanner.datatype.CalendarEventDataType;
import com.ptplanner.datatype.LoginDataType;
import com.ptplanner.datatype.MealDateDataType;
import com.ptplanner.datatype.ProgramDateDataType;
import com.ptplanner.datatype.TimeSlotsDataType;
import com.ptplanner.datatype.TrainerBookingDetailsDataType;
import com.ptplanner.dialog.ShowCalendarPopUpBookApp;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.AppController;
import com.ptplanner.helper.ConnectionDetector;
import com.ptplanner.helper.ReturnCalendarDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;

@SuppressLint("SimpleDateFormat")
public class BookAppointmentFragment extends Fragment {

    JSONArray jArrAppointment, jArrProgram, jArrMeal, jArrDiary, jarrayAvailableDate;
    CalendarEventDataType calEventData;
    String[] MealData, ProgramData, AvailableAppointmentDate, AppointmentData;
    ProgramDateDataType programDateDataType;
    MealDateDataType mealDateDataType;
    AppointDataType appointDataType;
    AvailableDateDataType availableDate;
    ProgressDialog progressDialog;
    View calenderView;


    int exCount = 0;
    int check = 0;
    int SelectedItemPosition = 0;
    boolean firstTimeFlag = false;
    TitilliumRegular tv_spinner_no_value;
    boolean flagCheckTimeSlot = false;
    ArrayList<String> TimeSlotList;
    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton, back, showCalender;
    RelativeLayout llMessagebutton;
    ListView bookApptList;
    View fView;
    String items = "";
    Spinner spinner;
    ProgressBar pbr_spinner;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    public ShowCalendarPopUpBookApp showCalPopup;
    // -- Calendar Instance
    //Calendar calendar;
    //    int currentYear, currentMonth, currentDay, currentDate, firstDayPosition,
//            currentMonthLength, previousDayPosition, nextDayPosition;
    String[] positionPre = {};
    public SimpleDateFormat sdfDate, sdfDay, sdfNo, sdfMonth;
    String dateCurrent;

    // -- UI Element
    RelativeLayout nextDate, prevDate;
    HelveticaHeavy tvNumber;
    TitilliumSemiBold tvMonth, tvDay, no_slot_found;

    ArrayList<AltrainerDataType> altrainerDataTypeArrayList;
    AltrainerDataType altrainerDataType;
    ViewPager trinerPageviewer;
    LinearLayout vp_next;
    LinearLayout vp_prev;
    Date date;
    public int viewPagerItemNo=0;
    String exception = "", exceptionJSON = "", urlResponse = "", exceptionTrainer = "", urlResponseTrainer = "";
    ProgressBar viewpagerPbar, pbarList;

    TimeSlotsDataType timeSlotsDataType;
    TrainerBookingDetailsDataType trainerBookingDetailsDataType;
    ArrayList<TrainerBookingDetailsDataType> trainerBookingDetailsDataTypeArrayList;
    ArrayList<TimeSlotsDataType> timeSlotsDataTypeArrayList;

    LinearLayout ptError;
    String trainerName = "";
    String trainerEmail = "";
    LinearLayout calendarButton, progressButton;
    RelativeLayout messageButton;

    TextView txtMSGCount, txtCal, txtApnt, txtPrg, txtMsg;
    ImageView imgCal, imgApnt, imgPrg, imgMsg;

    String saveString;
    SharedPreferences userId;
    ImageView imgBack, imgNext;
    SharedPreferences dateSavePreference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        //////////////////////////////////////////////////////////////////////////

        calendarButton = (LinearLayout) getActivity().findViewById(R.id.calenderbutton);
        progressButton = (LinearLayout) getActivity().findViewById(R.id.progressbutton);
        messageButton = (RelativeLayout) getActivity().findViewById(R.id.messagebutton);
        ///////////////////////////////////////////////////////////////////////////


        fView = inflater.inflate(R.layout.frag_book_appointment, container, false);

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        this.fView = inflater.inflate(R.layout.frag_book_appointment, container, false);
        /////////////////////////////////////////////////////

        fragmentManager = getActivity().getSupportFragmentManager();
        // calendar = Calendar.getInstance(Locale.getDefault());

        back = (LinearLayout) fView.findViewById(R.id.back);
        showCalender = (LinearLayout) fView.findViewById(R.id.show_cal);
        spinner = (Spinner) fView.findViewById(R.id.spinner);
        tv_spinner_no_value = (TitilliumRegular) fView.findViewById(R.id.tv_spinner_no_value);
        pbr_spinner = (ProgressBar) fView.findViewById(R.id.pbr_spinner);


        // ---------- Start
        prevDate = (RelativeLayout) fView.findViewById(R.id.prev_date);
        nextDate = (RelativeLayout) fView.findViewById(R.id.next_date);
        tvNumber = (HelveticaHeavy) fView.findViewById(R.id.tv_number);

        tvMonth = (TitilliumSemiBold) fView.findViewById(R.id.tv_month);
        tvDay = (TitilliumSemiBold) fView.findViewById(R.id.tv_day);
        no_slot_found = (TitilliumSemiBold) fView.findViewById(R.id.no_slot_found);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        trinerPageviewer = (ViewPager) fView.findViewById(R.id.trainer_viewpager);
        vp_next = (LinearLayout) fView.findViewById(R.id.vp_next);
        vp_prev = (LinearLayout) fView.findViewById(R.id.vp_back);
        vp_next.setVisibility(View.VISIBLE);
        vp_prev.setVisibility(View.VISIBLE);
        viewpagerPbar = (ProgressBar) fView.findViewById(R.id.viewpager_pbar);
        viewpagerPbar.setVisibility(View.GONE);

        ptError = (LinearLayout) fView.findViewById(R.id.pterror);
        ptError.setVisibility(View.GONE);

        bookApptList = (ListView) fView.findViewById(R.id.book_app_list);
        bookApptList.setDivider(null);
        pbarList = (ProgressBar) fView.findViewById(R.id.pbar);
        pbarList.setVisibility(View.GONE);// -- Shared Preference


//        SharedPreferences.Editor editorslot = AppController.slot.edit();
//        editorslot.clear();
//        editorslot.commit();


        dateSavePreference = getActivity().getSharedPreferences("DateTime", Context.MODE_PRIVATE);
        // ---------- End
        // -- Calendar
//        currentDate = calendar.get(Calendar.DATE);
//        currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
//        currentMonth = (calendar.get(Calendar.MONTH));
//        currentYear = calendar.get(Calendar.YEAR);
//        firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);
        // -- End

        imgBack = (ImageView) fView.findViewById(R.id.img_back);
        imgNext = (ImageView) fView.findViewById(R.id.img_next);
        imgBack.setBackgroundResource(R.drawable.arrow4);
        imgNext.setBackgroundResource(R.drawable.arrow3);
        vp_next.setClickable(false);
        vp_next.setEnabled(false);
        vp_prev.setClickable(false);
        vp_prev.setEnabled(false);

        userId = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        saveString = userId.getString("UserId", "");


        // -- Show Calendar
        showCalPopup = new ShowCalendarPopUpBookApp(getActivity(), "appointment");

        showCalender.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub


                if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                    AppConfig.appointmentArrayList.clear();
                    AppConfig.programArrayList.clear();
                    AppConfig.mealArrayList.clear();
                    AppConfig.availableDateArrayList.clear();

                    calenderView = view;

                    ///////////////////edit by suraj//////////////////
                    // -- Show Calendar

                    getAllEvent();
                    ///////////////////by suraj//////////////////
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }
            }
        });

        // -- Set Current Date --
        sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        sdfDay = new SimpleDateFormat("EEEE");
        sdfNo = new SimpleDateFormat("d");
        sdfMonth = new SimpleDateFormat("MMMM");

        try {
            if (AppConfig.bookAppDateChange.equalsIgnoreCase("")) {
                try {

                    Log.i("SoutrikToday : ", "inside try");

                    date = sdfDate.parse(getArguments().getString("DateChange"));
                    AppConfig.bookAppDateChange = getArguments().getString("DateChange");

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdfDate.parse(getArguments().getString("DateChange")));
                    AppConfig.calendarBookApp = cal;

                    getAllTrainer(getArguments().getString("DateChange"));

                    tvDay.setText("" + sdfDay.format(date));
                    tvNumber.setText("" + sdfNo.format(date));
                    tvMonth.setText("" + sdfMonth.format(date));
                } catch (Exception e) {

                    if (AppConfig.bookAppDateChange.equals("")) {

                        Log.i("SoutrikToday : ", "inside catch if");


                        AppConfig.bookAppDateChange = "";

                        final Calendar c = Calendar.getInstance();
                        AppConfig.calendarBookApp = c;

                        AppConfig.currentDateBookApp = c.get(Calendar.DATE);
                        AppConfig.currentDayBookApp = c.getActualMinimum(Calendar.DAY_OF_MONTH);
                        AppConfig.currentMonthBookApp = (c.get(Calendar.MONTH));
                        AppConfig.currentYearBookApp = c.get(Calendar.YEAR);
                        AppConfig.firstDayPositionBookApp = c.get(Calendar.DAY_OF_WEEK);

                        dateCurrent = sdfDate.format(AppConfig.calendarBookApp.getTime());

                        getAllTrainer(dateCurrent);

                        tvDay.setText("" + sdfDay.format(c.getTime()));
                        tvNumber.setText("" + sdfNo.format(c.getTime()));
                        tvMonth.setText("" + sdfMonth.format(c.getTime()));
                    } else {
                        try {

                            Log.i("SoutrikToday : ", "inside catch else try");


                            date = sdfDate.parse(AppConfig.bookAppDateChange);

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(sdfDate.parse(AppConfig.bookAppDateChange));
                            AppConfig.calendarBookApp = cal;

                            getAllTrainer(AppConfig.bookAppDateChange);

                            tvDay.setText("" + sdfDay.format(date));
                            tvNumber.setText("" + sdfNo.format(date));
                            tvMonth.setText("" + sdfMonth.format(date));
                        } catch (Exception ex) {
                            Log.i(" Exception : ", ex.toString());

                            Log.i("SoutrikToday : ", "inside catch else try");

                        }
                    }
                }
            } else {
                try {
                    date = sdfDate.parse(AppConfig.bookAppDateChange);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdfDate.parse(AppConfig.bookAppDateChange));
                    AppConfig.calendarBookApp = cal;

                    getAllTrainer(AppConfig.bookAppDateChange);

                    tvDay.setText("" + sdfDay.format(date));
                    tvNumber.setText("" + sdfNo.format(date));
                    tvMonth.setText("" + sdfMonth.format(date));
                } catch (Exception ibx) {
                    ibx.printStackTrace();
                }
            }
        } catch (Exception eww) {
            eww.printStackTrace();
        }

        // -- End

        prevDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                SharedPreferences.Editor editorslot = AppController.slot.edit();
                editorslot.clear();
                editorslot.commit();

                AppConfig.calendarBookApp.add(Calendar.DATE, -1);

                Calendar pre = (Calendar) AppConfig.calendarBookApp.clone();

                //   Log.i("TilBaka : ", "" + pre.toString());
                AppConfig.currentDateBookApp = pre.get(Calendar.DATE);
                AppConfig.currentDayBookApp = pre.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonthBookApp = (pre.get(Calendar.MONTH));
                AppConfig.currentYearBookApp = pre.get(Calendar.YEAR);
                AppConfig.firstDayPositionBookApp = pre.get(Calendar.DAY_OF_WEEK);

                tvDay.setText("" + sdfDay.format(pre.getTime()));
                tvNumber.setText("" + sdfNo.format(pre.getTime()));
                tvMonth.setText("" + sdfMonth.format(pre.getTime()));

                dateCurrent = sdfDate.format(pre.getTime());
                getAllTrainer(dateCurrent);

                AppConfig.bookAppDateChange = dateCurrent;
                AppConfig.changeDate = dateCurrent;


                DateFormat targetFormat = new SimpleDateFormat("yyyy-MMM-dd");
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date tilDate = null;
                try {
                    tilDate = originalFormat.parse(dateCurrent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = targetFormat.format(tilDate);


                SharedPreferences.Editor editor = dateSavePreference.edit();
                editor.clear();
                editor.putString("ClickDatePreferenceType", "" + "appointment");
                editor.putString("ClickDatePreference", "" + formattedDate);
                editor.commit();

                bookApptList.setVisibility(View.GONE);
                trinerPageviewer.setVisibility(View.GONE);
                flagCheckTimeSlot = true;
            }
        });
        nextDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                SharedPreferences.Editor editorslot = AppController.slot.edit();
                editorslot.clear();
                editorslot.commit();

                AppConfig.calendarBookApp.add(Calendar.DATE, +1);

                Calendar next = (Calendar) AppConfig.calendarBookApp.clone();
                //  Log.i("TilBaka : ", "" + next.toString());
                AppConfig.currentDateBookApp = next.get(Calendar.DATE);
                AppConfig.currentDayBookApp = next.getActualMinimum(Calendar.DAY_OF_MONTH);
                AppConfig.currentMonthBookApp = (next.get(Calendar.MONTH));
                AppConfig.currentYearBookApp = next.get(Calendar.YEAR);
                AppConfig.firstDayPositionBookApp = next.get(Calendar.DAY_OF_WEEK);

                tvDay.setText("" + sdfDay.format(next.getTime()));
                tvNumber.setText("" + sdfNo.format(next.getTime()));
                tvMonth.setText("" + sdfMonth.format(next.getTime()));

                dateCurrent = sdfDate.format(AppConfig.calendarBookApp.getTime());
                getAllTrainer(dateCurrent);

                AppConfig.bookAppDateChange = dateCurrent;
                AppConfig.changeDate = dateCurrent;

                DateFormat targetFormat = new SimpleDateFormat("yyyy-MMM-dd");
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date tilDate = null;
                try {
                    tilDate = originalFormat.parse(dateCurrent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = targetFormat.format(tilDate);


                SharedPreferences.Editor editor = dateSavePreference.edit();
                editor.clear();
                editor.putString("ClickDatePreferenceType", "" + "appointment");
                editor.putString("ClickDatePreference", "" + formattedDate);
                editor.commit();

                bookApptList.setVisibility(View.GONE);
                trinerPageviewer.setVisibility(View.GONE);
                flagCheckTimeSlot = true;
            }
        });

        vp_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trinerPageviewer.getCurrentItem() == (altrainerDataTypeArrayList.size() - 1)) {
                    trinerPageviewer.setCurrentItem(0, false);

                } else {
                    trinerPageviewer.setCurrentItem(trinerPageviewer.getCurrentItem() + 1, false);
                }
            }
        });

        vp_prev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trinerPageviewer.getCurrentItem() == 0) {
                    trinerPageviewer.setCurrentItem(altrainerDataTypeArrayList.size(), false);
                } else {
                    trinerPageviewer.setCurrentItem((trinerPageviewer.getCurrentItem() - 1), false);
                }
            }
        });


        trinerPageviewer.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try{
                    Log.i("slot_value_preference1", AppController.slot.getString("value", ""));
                    Log.e("SIZE-",""+altrainerDataTypeArrayList.size()+" POSI"+position);
//                    getTrainerBookingDetails(dateCurrent, altrainerDataTypeArrayList.get(position).getPt_id(), AppController.slot.getString("value", "").substring(0, 1));
                    getTrainerBookingDetails(dateCurrent, altrainerDataTypeArrayList.get(position).getPt_id(), AppController.slot.getString("value", ""));

                    trainerName = altrainerDataTypeArrayList.get(position).getPt_name();
                    trainerEmail = altrainerDataTypeArrayList.get(position).getPt_email();
                }catch (IndexOutOfBoundsException e){
                    Log.e("Error",""+e.getMessage());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
        txtCal.setTextColor(Color.parseColor("#A4A4A5"));
        txtApnt.setTextColor(Color.parseColor("#22A7F0"));
        txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
        txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
        imgCal.setBackgroundResource(R.drawable.cal);
        imgApnt.setBackgroundResource(R.drawable.apntclick2);
        imgPrg.setBackgroundResource(R.drawable.prg);
        imgMsg.setBackgroundResource(R.drawable.msg);
        llCalenderButton.setClickable(true);
        llBlockAppoinmentButton.setClickable(false);
        llProgressButton.setClickable(true);
        llMessagebutton.setClickable(true);

        ///////////////////////////////below the spinner show items value///////////////////////////////////
        spinner.setDropDownVerticalOffset((int) dipToPixels(getActivity(), 50));
        spinner.setSelection(Adapter.NO_SELECTION, false);
        return fView;
    }

    public void getAllTrainer(final String date) {

        AsyncTask<Void, Void, Void> allTrainer = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                ///////////////////////////////////////////////
                messageButton.setClickable(false);
                messageButton.setEnabled(false);
                progressButton.setClickable(false);
                progressButton.setEnabled(false);
                calendarButton.setClickable(false);
                calendarButton.setEnabled(false);

                ///////////////////////////////////////////////////
                prevDate.setClickable(false);
                nextDate.setClickable(false);

                viewpagerPbar.setVisibility(View.VISIBLE);
                trinerPageviewer.setVisibility(View.GONE);

                pbr_spinner.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                tv_spinner_no_value.setVisibility(View.GONE);
                ptError.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    exceptionJSON = "";


                    OkHttpClient client = new OkHttpClient();
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(AppConfig.HOST + "app_control/trainer_by_date?client_id="
                                    + saveString
                                    + "&date_val=" + date)
                            .build();
                    Log.i("getAllTrainer", "" + AppConfig.HOST + "app_control/trainer_by_date?client_id=" + saveString + "&date_val=" + date);
                    okhttp3.Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();


                    try {
                        JSONObject jOBJ = new JSONObject(urlResponse);
                        Log.i("jOBJ", "" + jOBJ);
                        JSONArray jsonArray = jOBJ.getJSONArray("trainer");
                        viewPagerItemNo = jsonArray.length();
                        altrainerDataTypeArrayList = new ArrayList<AltrainerDataType>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            altrainerDataType = new AltrainerDataType(jsonObject.getString("pt_id"),
                                    jsonObject.getString("pt_name"),
                                    jsonObject.getString("pt_image"),
                                    jsonObject.getString("working_address")
                                    , jsonObject.getString("pt_email"));
                            altrainerDataTypeArrayList.add(altrainerDataType);
                        }

                        //  Log.d("RESPONSE############", jOBJ.toString());
                    } catch (Exception e) {
                        exceptionJSON = e.toString();
                        //JSONArray jArr = new JSONArray("");
                    }

                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                ///////////////////////////////////////////////
                messageButton.setClickable(true);
                messageButton.setEnabled(true);
                progressButton.setClickable(true);
                progressButton.setEnabled(true);
                calendarButton.setClickable(true);
                calendarButton.setEnabled(true);
                no_slot_found.setVisibility(View.GONE);

                ///////////////////////////////////////////////////
                prevDate.setClickable(true);
                nextDate.setClickable(true);
                viewpagerPbar.setVisibility(View.GONE);
                if (exception.equals("") & isAdded()) {
                    if (exceptionJSON.equals("")) {

                        trinerPageviewer.setVisibility(View.VISIBLE);
                        ptError.setVisibility(View.GONE);
                        AllTrainerAdapter trainerAdapter = new AllTrainerAdapter(getActivity(), 0, altrainerDataTypeArrayList);
                        trinerPageviewer.setAdapter(trainerAdapter);

                        trainerName = altrainerDataTypeArrayList.get(0).getPt_name();
                        trainerEmail = altrainerDataTypeArrayList.get(0).getPt_email();

                        if (altrainerDataTypeArrayList.size() > 1) {
//                            vp_next.setVisibility(View.VISIBLE);
//                            vp_prev.setVisibility(View.VISIBLE);
                            imgBack.setBackgroundResource(R.drawable.arrow21);
                            imgNext.setBackgroundResource(R.drawable.arrow12);
                            vp_next.setClickable(true);
                            vp_next.setEnabled(true);
                            vp_prev.setClickable(true);
                            vp_prev.setEnabled(true);
                        } else {
//                            vp_next.setVisibility(View.GONE);
//                            vp_prev.setVisibility(View.GONE);


                            imgBack.setBackgroundResource(R.drawable.arrow4);
                            imgNext.setBackgroundResource(R.drawable.arrow3);
                            vp_next.setClickable(false);
                            vp_next.setEnabled(false);
                            vp_prev.setClickable(false);
                            vp_prev.setEnabled(false);
                        }

                        Log.i("slot_value_preference2", AppController.slot.getString("value", ""));
                        getTrainerBookingDetails(date, altrainerDataTypeArrayList.get(0).getPt_id(), AppController.slot.getString("value", ""));
                    } else {
                        //Toast.makeText(getActivity(), "No record found....", Toast.LENGTH_LONG).show();
                        trinerPageviewer.setVisibility(View.GONE);
                        ptError.setVisibility(View.VISIBLE);
                        pbr_spinner.setVisibility(View.GONE);
                        imgBack.setBackgroundResource(R.drawable.arrow4);
                        imgNext.setBackgroundResource(R.drawable.arrow3);
                        vp_next.setClickable(false);
                        vp_next.setEnabled(false);
                        vp_prev.setClickable(false);
                        vp_prev.setEnabled(false);
                    }

                } else {
                    // Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }
        };
        allTrainer.execute();

    }

    public void getTrainerBookingDetails(final String date, final String trainerId, final String slot_len) {

        AsyncTask<Void, Void, Void> trainerBookingDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                ///////////////////////////////////////////////
                messageButton.setClickable(false);
                messageButton.setEnabled(false);
                progressButton.setClickable(false);
                progressButton.setEnabled(false);
                calendarButton.setClickable(false);
                calendarButton.setEnabled(false);

                ///////////////////////////////////////////////////
                pbarList.setVisibility(View.VISIBLE);
                bookApptList.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionTrainer = "";
                    urlResponseTrainer = "";


                    OkHttpClient client = new OkHttpClient();
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(AppConfig.HOST + "app_control/trainer_booking_details?trainer_id="
                                    + trainerId + "&date_val=" + date + "&client_id=" + saveString + "&slot_len=" + slot_len)
                            .build();
                    Log.i("TrainerBookingDetails", "" + AppConfig.HOST + "app_control/trainer_booking_details?trainer_id=" + trainerId + "&date_val=" + date + "&client_id=" + saveString + "&slot_len=" + slot_len);
                    okhttp3.Response response = client.newCall(request).execute();
                    urlResponseTrainer = response.body().string();
                    JSONObject jsonObject = new JSONObject(urlResponseTrainer);

                    Log.i("jsonObject", "" + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONArray("time_slots");


                    timeSlotsDataTypeArrayList = new ArrayList<TimeSlotsDataType>();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jOBJ = jsonArray.getJSONObject(i);
                            if (jOBJ.getString("status").equals("NB")) {
                                timeSlotsDataType = new TimeSlotsDataType(
                                        jOBJ.getString("slot_start"), jOBJ.getString("slot_end"),
                                        jOBJ.getString("counter"), jOBJ.getString("booking_id"),
                                        jOBJ.getString("status"), "NB", jsonObject.getString("trainer_id"),
                                        jsonObject.getString("booking_date"));
                            } else if (jOBJ.getString("status").equals("Ex")) {
                                exCount = exCount + 1;
                                timeSlotsDataType = new TimeSlotsDataType(
                                        jOBJ.getString("slot_start"), jOBJ.getString("slot_end"),
                                        jOBJ.getString("counter"), jOBJ.getString("booking_id"),
                                        jOBJ.getString("status"), "Ex", jsonObject.getString("trainer_id"),
                                        jsonObject.getString("booking_date"));
                            } else {
                                timeSlotsDataType = new TimeSlotsDataType(
                                        jOBJ.getString("slot_start"), jOBJ.getString("slot_end"),
                                        jOBJ.getString("counter"), jOBJ.getString("booking_id"),
                                        jOBJ.getString("status"), "B", jsonObject.getString("trainer_id"),
                                        jsonObject.getString("booking_date"));
                            }
                            timeSlotsDataTypeArrayList.add(timeSlotsDataType);
                        }
                    }

                    Log.d("RESPONSE***********", urlResponseTrainer);
                    Log.d("URL", AppConfig.HOST + "app_control/trainer_booking_details?trainer_id="
                            + trainerId + "&date_val=" + date + "&client_id=" + saveString + "&slot_len=" + slot_len);
                } catch (Exception e) {
                    exceptionTrainer = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                check = 0;
                ///////////////////////////////////////////////
                messageButton.setClickable(true);
                messageButton.setEnabled(true);
                progressButton.setClickable(true);
                progressButton.setEnabled(true);
                calendarButton.setClickable(true);
                calendarButton.setEnabled(true);

                ///////////////////////////////////////////////////
                pbarList.setVisibility(View.GONE);
                if (exceptionTrainer.equals("") & isAdded()) {
                    Log.i("set_size", "" + timeSlotsDataTypeArrayList.size());

                    if (timeSlotsDataTypeArrayList.size() > 0 && exCount != timeSlotsDataTypeArrayList.size()) {
                        no_slot_found.setVisibility(View.GONE);
                        bookApptList.setVisibility(View.VISIBLE);
                        BookAppointAdapter bookAppointAdapter = new BookAppointAdapter(getActivity(), 0, timeSlotsDataTypeArrayList, trainerName, trainerEmail, BookAppointmentFragment.this);
                        bookApptList.setAdapter(bookAppointAdapter);

                    } else {
                        bookApptList.setVisibility(View.GONE);
                        if( viewPagerItemNo>0)
                        no_slot_found.setVisibility(View.VISIBLE);
                    }
                    exCount = 0;
                } else {
                    //Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }

                ///////////////spinner data set from json/////////////////////////

                String url = AppConfig.HOST + "app_control/slot_lengths_func?trainer_id=" + trainerId + "&date_val=" + date + "&client_id=" + saveString;
                Log.i("Slot_url", url);
                getTimeSlot(url, date);
            }
        };
        trainerBookingDetails.execute();
    }


    public void getTimeSlot(String url, final String date) {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                try {
                    TimeSlotList = new ArrayList<>();
                    Log.i("response", "" + response);
                    if (response.getString("response").equalsIgnoreCase("Success")) {
                        JSONArray slotArray = response.getJSONArray("slot");

                        if (slotArray.length() > 0) {
                            tv_spinner_no_value.setVisibility(View.GONE);
//                            TimeSlotList.add("Bokningslängd");
                            for (int i = 0; i < slotArray.length(); i++) {
                                TimeSlotList.add(slotArray.getString(i));
                            }
                            Log.i("Size", "" + TimeSlotList.size());

                            for (int j = 0; j < TimeSlotList.size(); j++) {
                                Log.i("time_slots", "" + TimeSlotList.get(j));
                            }

                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, TimeSlotList);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_textview);

                            if (spinner != null) {
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable(){
//                            public void run(){
                                // do your work
                                pbr_spinner.setVisibility(View.GONE);
                                spinner.setVisibility(View.VISIBLE);
                                spinner.setAdapter(spinnerArrayAdapter);

                                if (flagCheckTimeSlot == true || firstTimeFlag == false) {


                                    if (!AppController.slot.getString("slot", "").equals("")) {
                                        spinner.setSelection(Integer.parseInt(AppController.slot.getString("slot", "")));
                                    } else {
                                        spinner.setSelection(0);
                                    }

                                    flagCheckTimeSlot = false;
                                    firstTimeFlag = true;
                                } else {
                                    spinner.setSelection(SelectedItemPosition);
                                }


                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        check = check + 1;
                                        if (check > 1) {
                                            //  if (position > 0) {

//                                                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Bokningslängd"))
//                                                {
//
//                                                }
//                                                else
//                                                {

                                            SelectedItemPosition = position;

                                            SharedPreferences.Editor editorslot = AppController.slot.edit();
                                            editorslot.clear();
                                            editorslot.putString("slot", "" + SelectedItemPosition);
                                            editorslot.putString("value", spinner.getSelectedItem().toString().substring(0, 2));
                                            editorslot.commit();

                                            ///////////set postition next time//////////////////////
                                            items = spinner.getSelectedItem().toString();
                                            Log.i("Selected item : ", items);
                                            int i = items.indexOf(" ");
                                            items = items.substring(0, i);
                                            Log.i("items", items);
                                            Log.i("date", "" + date);
                                            Log.i("getPt_id", "" + altrainerDataTypeArrayList.get(0).getPt_id());
                                            getTrainerBookingDetails(date, altrainerDataTypeArrayList.get(0).getPt_id(), items);
                                            //    }
                                            //      }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
//                            }
//                        }, 500);
                            } else {
                                Log.i("spinner :", "NULL");
                            }
                        } else {
                            pbr_spinner.setVisibility(View.GONE);
                            spinner.setVisibility(View.GONE);
                            tv_spinner_no_value.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub

                String body;
                //get status code here
                String statusCode = String.valueOf(arg0.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                if (arg0.networkResponse.data != null) {
                    try {
                        body = new String(arg0.networkResponse.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                //do stuff with the body...
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
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
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(AppConfig.HOST
                                    + "app_control/mark_calender?client_id="
                                    + AppConfig.loginDatatype.getSiteUserId())
                            .build();
                    Log.i("mark_calender_Url", "" + AppConfig.HOST + "app_control/mark_calender?client_id=" + AppConfig.loginDatatype.getSiteUserId());
                    okhttp3.Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
//                    Log.i("jOBJ", "" + jOBJ);

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

                    SharedPreferences.Editor editorslot = AppController.slot.edit();
                    editorslot.clear();
                    editorslot.commit();
                    showCalPopup.getLayouts();
                    Calendar pre = (Calendar) AppConfig.calendarBookApp.clone();
                    pre.set(Calendar.MONTH, AppConfig.currentMonthBookApp);
                    pre.set(Calendar.YEAR, AppConfig.currentYearBookApp);
                    pre.set(Calendar.DATE, 1);

                    positionPre = pre.getTime().toString().split(" ");
                    int previousDayPosition = ReturnCalendarDetails.getPosition(positionPre[0]);

                    showCalPopup.getCalendar(
                            ReturnCalendarDetails.getCurrentMonth(positionPre[1]),
                            ReturnCalendarDetails.getPosition(positionPre[0]),
                            Integer.parseInt(positionPre[5])
                    );

                    showCalPopup.showAtLocation(calenderView, Gravity.CENTER_HORIZONTAL, 0, -20);
                    flagCheckTimeSlot = true;


                } else {
                    System.out.println("@@ Exception: " + exception);
                }
                progressDialog.dismiss();
            }
        };
        event.execute();
    }
}
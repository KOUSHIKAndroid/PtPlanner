package com.ptplanner.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ptplanner.K_DataBase.Database;
import com.ptplanner.K_DataBase.LocalDataResponse;
import com.ptplanner.R;
import com.ptplanner.adapter.DietAdapter;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.AppointDataType;
import com.ptplanner.datatype.AvailableDateDataType;
import com.ptplanner.datatype.CalendarEventDataType;
import com.ptplanner.datatype.DietDataType;
import com.ptplanner.datatype.MealDateDataType;
import com.ptplanner.datatype.ProgramDateDataType;
import com.ptplanner.dialog.ShowCalendarPopUp;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import com.ptplanner.helper.ReturnCalendarDetails;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DietFragment extends Fragment {

    JSONArray jArrAppointment, jArrProgram, jArrMeal, jArrDiary, jarrayAvailableDate;
    CalendarEventDataType calEventData;
    String[] MealData,ProgramData,AvailableAppointmentDate,AppointmentData;
    ProgramDateDataType programDateDataType;
    MealDateDataType mealDateDataType;
    AppointDataType appointDataType;
    AvailableDateDataType availableDate;

    ProgressDialog progressDialog;
    View calenderView;




    public ListView dietList;
    LinearLayout back, showCalendar;
    TitilliumSemiBold txtError;
    ConnectionDetector cd;
    LinkedList<DietDataType> dietDataTypeLinkedList;
    DietAdapter dietAdapter;
    View fView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    ConnectionDetector connectionDetector;
    ProgressBar pBar;
    String exception = "", exceptionError = "", urlResponse = "";

    // -- Calendar Instance
//    Calendar calendar;
//    int currentYear, currentMonth, currentDay, currentDate, firstDayPosition;
    SimpleDateFormat dayFormat, monthFormat, dateFormat;
    Date dateChange;
    String date = "";
    String[] positionPre = {};
    int previousDayPosition;
    String dietDialog;
    ShowCalendarPopUp showCalPopup;
    LinearLayout appointmentButton, progressButton;
    RelativeLayout messageButton;

    String saveString;
    SharedPreferences userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        dietDialog = getResources().getString(R.string.fra_diet_calorieDialog);
        fView = inflater.inflate(R.layout.frag_diet, container, false);

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
        this.fView = inflater.inflate(R.layout.frag_diet, container, false);
        /////////////////////////////////////////////////////

        fragmentManager = getActivity().getSupportFragmentManager();
        connectionDetector = new ConnectionDetector(getActivity());

        pBar = (ProgressBar) fView.findViewById(R.id.progbar);
        pBar.setVisibility(View.GONE);
        back = (LinearLayout) fView.findViewById(R.id.back);
        showCalendar = (LinearLayout) fView.findViewById(R.id.show_cal);
        dietList = (ListView) fView.findViewById(R.id.diet_list);
        dietList.setDivider(null);
        txtError = (TitilliumSemiBold) fView.findViewById(R.id.txt_error);


        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

//        calendar = Calendar.getInstance(Locale.getDefault());
//        currentDate = calendar.get(Calendar.DATE);
//        currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
//        currentMonth = (calendar.get(Calendar.MONTH));
//        currentYear = calendar.get(Calendar.YEAR);
//        firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        dayFormat = new SimpleDateFormat("dd");
        monthFormat = new SimpleDateFormat("EEEE");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // -- Show Calendar
        showCalPopup = new ShowCalendarPopUp(getActivity(), "diet");

        showCalendar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub


                if(new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                    ///////////////////edit by suraj//////////////////
                    // -- Show Calendar
                    calenderView = view;
                    AppConfig.appointmentArrayList.clear();
                    AppConfig.programArrayList.clear();
                    AppConfig.mealArrayList.clear();
                    AppConfig.availableDateArrayList.clear();
                    getAllEvent();
                    ///////////////////by suraj//////////////////
                }

                else{
                    Toast.makeText(getActivity(),getResources().getString(R.string.no_internet),Toast.LENGTH_LONG).show();
                }
            }
            //------getting date
        });

        if (connectionDetector.isConnectingToInternet()) {
            try {
                dateChange = dateFormat.parse(getArguments().getString("DateChange"));

                Log.d("DAY==", getArguments().getString("DateChange"));

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(getArguments().getString("DateChange")));
                AppConfig.calendar = cal;

                getDietList(getArguments().getString("DateChange"));

            } catch (Exception e) {
                Log.d("Date Exception : ", e.toString());
                date = "" + dateFormat.format(AppConfig.calendar.getTime());
                getDietList(date);
            }
            //getDietList("2015-07-03");
        } else {
            OffLineData(AppConfig.OfflineDate);

//            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
getActivity().onBackPressed();
//                Bundle bundle = new Bundle();
//                bundle.putString("DateChange", getArguments().getString("DateChange"));
//
//                fragmentTransaction = fragmentManager.beginTransaction();
//                CalenderFragment cal_fragment = new CalenderFragment();
//                cal_fragment.setArguments(bundle);
//                fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
////                int count = fragmentManager.getBackStackEntryCount();
////                fragmentTransaction.addToBackStack(String.valueOf(count));
//                fragmentTransaction.commit();
            }
        });

        return fView;
    }

    private void OffLineData(String dateChange) {
        Log.d("@@ DATE--",dateChange);
        new Database(getActivity()).GET_DietData(dateChange, new LocalDataResponse() {
            @Override
            public void OnSuccess(String Response) {
                try {
                    JSONObject jOBJ = new JSONObject(Response);
                    Log.i("@@ Diate-OBJ",""+jOBJ);
                    JSONArray jsonArray = jOBJ.getJSONArray("meal");
                    dietDataTypeLinkedList = new LinkedList<DietDataType>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DietDataType dietDataType = new DietDataType(
                                jsonObject.getString("meal_id"),
                                jsonObject.getString("meal_image"),
                                jsonObject.getString("meal_description"),
                                jsonObject.getString("meal_title"),
                                jsonObject.getString("custom_meal_id"));
                        dietDataType.setJSONBOJECT(jsonObject);
                        dietDataTypeLinkedList.add(dietDataType);
                    }
                } catch (Exception ex) {
                    exceptionError = ex.toString();
                }

                pBar.setVisibility(View.GONE);
                ///////////////////////////////////////////////////
                progressButton.setClickable(true);
                progressButton.setEnabled(true);
                appointmentButton.setClickable(true);
                appointmentButton.setEnabled(true);
                messageButton.setClickable(true);
                messageButton.setEnabled(true);

                //////////////////////////////////////////////////
                if (exception.equals("")) {
                    if (exceptionError.equals("")) {
                        dietList.setVisibility(View.VISIBLE);
                        dietAdapter = new DietAdapter(getActivity(), 0, dietDataTypeLinkedList, getArguments().getString("DateChange"));
                        dietList.setAdapter(dietAdapter);
                        txtError.setVisibility(View.GONE);
                    } else {
                        txtError.setVisibility(View.GONE);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder
                                .setMessage(dietDialog)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void OnNotfound(String NotFound) {
                pBar.setVisibility(View.VISIBLE);

                dietList.setVisibility(View.GONE);
                txtError.setVisibility(View.VISIBLE);

                ////////////////////////////////////////////
                progressButton.setClickable(false);
                progressButton.setEnabled(false);
                appointmentButton.setClickable(false);
                appointmentButton.setEnabled(false);
                messageButton.setClickable(false);
                messageButton.setEnabled(false);
            }
        });
    }

    public void getDietList(final String date) {

        AsyncTask<Void, Void, Void> dietListDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);

                dietList.setVisibility(View.GONE);
                txtError.setVisibility(View.GONE);

                ////////////////////////////////////////////
                progressButton.setClickable(false);
                progressButton.setEnabled(false);
                appointmentButton.setClickable(false);
                appointmentButton.setEnabled(false);
                messageButton.setClickable(false);
                messageButton.setEnabled(false);
                ///////////////////////////////////////////
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    exceptionError = "";
                    urlResponse = "";



                    OkHttpClient client = new OkHttpClient();
                    Request request;


                    if (AppConfig.changeDate.equalsIgnoreCase(""))
                    {
                        request= new Request.Builder()
                                .url(AppConfig.HOST + "app_control/date_respective_client_meal?logged_in_user=" +
                                        AppConfig.loginDatatype.getSiteUserId() + "&date_val=" + date)
                                .build();

                        Log.d("Diet URL1 : ", AppConfig.HOST + "app_control/date_respective_client_meal?logged_in_user=" +
                                saveString + "&date_val=" + date);
                    }else {

                        request= new Request.Builder()
                                .url(AppConfig.HOST + "app_control/date_respective_client_meal?logged_in_user=" +
                                        AppConfig.loginDatatype.getSiteUserId() + "&date_val=" + AppConfig.changeDate)
                                .build();

                        Log.d("Diet URL2 : ", AppConfig.HOST + "app_control/date_respective_client_meal?logged_in_user=" +
                                saveString + "&date_val=" + AppConfig.changeDate);
                    }


                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();

                    try {
                        JSONObject jOBJ = new JSONObject(urlResponse);
                        Log.i("jOBJ",""+jOBJ);
                        JSONArray jsonArray = jOBJ.getJSONArray("meal");
                        dietDataTypeLinkedList = new LinkedList<DietDataType>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            DietDataType dietDataType = new DietDataType(
                                    jsonObject.getString("meal_id"),
                                    jsonObject.getString("meal_image"),
                                    jsonObject.getString("meal_description"),
                                    jsonObject.getString("meal_title"),
                                    jsonObject.getString("custom_meal_id"));
                            dietDataType.setJSONBOJECT(jsonObject);
                            dietDataTypeLinkedList.add(dietDataType);
                        }
                    } catch (Exception ex) {
                        exceptionError = ex.toString();
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
                pBar.setVisibility(View.GONE);
                ///////////////////////////////////////////////////
                progressButton.setClickable(true);
                progressButton.setEnabled(true);
                appointmentButton.setClickable(true);
                appointmentButton.setEnabled(true);
                messageButton.setClickable(true);
                messageButton.setEnabled(true);

                //////////////////////////////////////////////////
                if (exception.equals("")) {
                    if (exceptionError.equals("")) {
                        dietList.setVisibility(View.VISIBLE);
                        dietAdapter = new DietAdapter(getActivity(), 0, dietDataTypeLinkedList, getArguments().getString("DateChange"));
                        dietList.setAdapter(dietAdapter);
                    } else {
                        txtError.setVisibility(View.GONE);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder
                                .setMessage(dietDialog)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                } else {
                    // Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        dietListDetails.execute();
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
                    Log.i("jOBJ",""+jOBJ);


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

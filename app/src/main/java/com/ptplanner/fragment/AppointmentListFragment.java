package com.ptplanner.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ptplanner.R;
import com.ptplanner.adapter.AppointmentListAdapter;
import com.ptplanner.datatype.AppointmentListDataType;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ltp on 06/07/15.
 */
public class AppointmentListFragment extends Fragment {
    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton, back;
    RelativeLayout llMessagebutton;

    View fView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    ListView listApp;
    ProgressBar progBar;

    String exception = "", urlResponse = "";
    AppointmentListDataType appointmentListDataType;
    ArrayList<AppointmentListDataType> appointmentListDataTypeArrayList;
    ConnectionDetector cd;
    Bundle bundle;
    Date date;

    String saveString;
    SharedPreferences userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.frag_applist, container, false);

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        this.fView = inflater.inflate(R.layout.frag_applist, container, false);
        /////////////////////////////////////////////////////

        back = (LinearLayout) fView.findViewById(R.id.back);
        fragmentManager = getActivity().getSupportFragmentManager();

        listApp = (ListView) fView.findViewById(R.id.list_app);
        progBar = (ProgressBar) fView.findViewById(R.id.prog_bar);
        progBar.setVisibility(View.GONE);

        userId = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        saveString = userId.getString("UserId", "");

        cd = new ConnectionDetector(getActivity());
        if (cd.isConnectingToInternet()) {

            getAppointmentList(getArguments().getString("DateChange"));
            if (getArguments().getString("DateChange")==null || getArguments().getString("DateChange").equalsIgnoreCase("")){

            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                getActivity().onBackPressed();


//                ******** @@ KOUSHIK

//                bundle = new Bundle();
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

        llCalenderButton = (LinearLayout) getActivity().findViewById(R.id.calenderbutton);
        llBlockAppoinmentButton = (LinearLayout) getActivity().findViewById(R.id.blockappoinmentbutton);
        llProgressButton = (LinearLayout) getActivity().findViewById(R.id.progressbutton);
        llMessagebutton = (RelativeLayout) getActivity().findViewById(R.id.messagebutton);
        llCalenderButton.setClickable(false);
        llBlockAppoinmentButton.setClickable(true);
        llProgressButton.setClickable(true);
        llMessagebutton.setClickable(true);

        return fView;
    }

    public void getAppointmentList(final String date) {

        AsyncTask<Void, Void, Void> appointList = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                listApp.setVisibility(View.GONE);
                progBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    Log.i("jOBJ_j",""+AppConfig.HOST + "app_control/get_all_booking?client_id=" +
                            saveString + "&date_val=" + date);


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/get_all_booking?client_id=" +
                                    saveString + "&date_val=" + date)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    Log.i("jOBJ_j",""+urlResponse);
                    JSONObject jOBJ = new JSONObject(urlResponse);


                    JSONArray jsonArray = jOBJ.getJSONArray("bookings");
                    appointmentListDataTypeArrayList = new ArrayList<AppointmentListDataType>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        appointmentListDataType = new AppointmentListDataType(jsonObject.getString("id"),
                                jsonObject.getString("trainer_name"),
                                jsonObject.getString("booked_by"),
                                jsonObject.getString("booked_date"),
                                jsonObject.getString("booking_time_start"),
                                jsonObject.getString("booking_time_end"),
                                jsonObject.getString("status"),
                                jsonObject.getString("booking_date"),
                                jsonObject.getString("program_name"));
                        appointmentListDataTypeArrayList.add(appointmentListDataType);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    exception = e.toString();
                }
//                Log.i("Dek : ", AppConfig.HOST + "app_control/get_all_booking?client_id=" +
//                        saveString + "&date_val=" + date);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progBar.setVisibility(View.GONE);

                if (exception.equals("")) {
                    listApp.setVisibility(View.VISIBLE);

                    AppointmentListAdapter appointmentListAdapter = new AppointmentListAdapter(getActivity(), 0,
                            appointmentListDataTypeArrayList, getArguments().getString("DateChange"));
                    listApp.setAdapter(appointmentListAdapter);

                } else {
                    Log.i("Dek : ", exception);
                }
            }

        };
        appointList.execute();

    }

}

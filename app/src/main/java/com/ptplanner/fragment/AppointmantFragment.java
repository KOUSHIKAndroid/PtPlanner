package com.ptplanner.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ptplanner.R;
import com.ptplanner.customviews.TitilliumBold;
import com.ptplanner.customviews.TitilliumLight;
import com.ptplanner.datatype.AppointmentDataType;
import com.ptplanner.datatype.AppointmentListDataType;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import com.ptplanner.helper.Trns;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AppointmantFragment extends Fragment {

    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton, back;
    RelativeLayout llMessagebutton, llCancel;

    View fView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Bundle bundle;
    String exception = "", urlResponse = "";

    ProgressBar progressBar, cancelPBar;
    LinearLayout mainContainer;
    ScrollView scrollView;
    AppointmentDataType appointDataType;
    ConnectionDetector cd;
    AppointmentListDataType appointmentListDataType;
    ImageView imgPic;
    TitilliumBold txtName;
    TitilliumLight txtNameSub, txtDesc, txtDatetime, txtTo, txtFrom, txtLocation, cancel_appointment;

    String saveString;
    SharedPreferences userId;

    TitilliumLight clientEmail;
    String bookingId = "";
    LinearLayout ll_msg_new_click;

    String msgUserName = "", msgUserId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.frag_appointment, container, false);

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        this.fView = inflater.inflate(R.layout.frag_appointment, container, false);
        /////////////////////////////////////////////////////

        back = (LinearLayout) fView.findViewById(R.id.back);
        fragmentManager = getActivity().getSupportFragmentManager();

        cd = new ConnectionDetector(getActivity());

        progressBar = (ProgressBar) fView.findViewById(R.id.prog_bar);
        mainContainer = (LinearLayout) fView.findViewById(R.id.main_container);
        scrollView = (ScrollView) fView.findViewById(R.id.scrl_body);
        imgPic = (ImageView) fView.findViewById(R.id.img_pic);
        txtName = (TitilliumBold) fView.findViewById(R.id.txt_name);
        txtNameSub = (TitilliumLight) fView.findViewById(R.id.txt_name_sub);
        txtDesc = (TitilliumLight) fView.findViewById(R.id.txt_desc);
        txtDatetime = (TitilliumLight) fView.findViewById(R.id.txt_datetime);
        txtTo = (TitilliumLight) fView.findViewById(R.id.txt_to);
        txtFrom = (TitilliumLight) fView.findViewById(R.id.txt_from);
        txtLocation = (TitilliumLight) fView.findViewById(R.id.txt_location);
        llCancel = (RelativeLayout) fView.findViewById(R.id.ll_cancel);
        cancelPBar = (ProgressBar) fView.findViewById(R.id.cancel_pbar);
        clientEmail = (TitilliumLight) fView.findViewById(R.id.txt_clientEmail);

        ll_msg_new_click = (LinearLayout) fView.findViewById(R.id.ll_msg_new);
//        txtLocation.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        cancelPBar.setVisibility(View.GONE);

        userId = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        saveString = userId.getString("UserId", "");

        if (cd.isConnectingToInternet()) {
            try {
                String id = getArguments().getString("BOOKID");
                if (id.equals("")) {
//                    Log.i("CCC : ", "IF");
                    getAppointmentList(getArguments().getString("DateChange"));
                } else {
//                    Log.i("CCC : ", "ELSE");
                    getAppointmentDetails(getArguments().getString("BOOKID"));
                }
            } catch (Exception e) {
                getAppointmentList(getArguments().getString("DateChange"));
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        llCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(appointDataType.getCancel_status().equals("CAN_NOT"))
                {
                   new AlertDialog.Builder(getActivity()).setMessage("Vänligen avboka pass senast 24 timmar innan utsatt tid").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                       }
                   }).create().show();
                }else
                cancelAppointmentDetails(getArguments().getString("BOOKID"));
            }
        });

        ll_msg_new_click.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (
//                        !msgUserName.equalsIgnoreCase("") &&
                                !msgUserId.equalsIgnoreCase("")) {
                    Intent i = new Intent(getActivity(), ChatDetailsFragment.class);
                    i.putExtra("newBack", "AppointmentFragment");
                    i.putExtra("msgUserName", msgUserName);
                    i.putExtra("msgUserId", msgUserId);
                    startActivity(i);
                }
            }
        });

        clientEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!appointDataType.getTrainer_email().equalsIgnoreCase("")) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        final PackageManager pm = getActivity().getPackageManager();
                        final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
                        ResolveInfo best = null;
                        for (final ResolveInfo info : matches) {
                            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) {
                                best = info;
                                break;
                            }
                        }
                        if (best != null) {
                            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                        }
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{appointDataType.getTrainer_email()});
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        llCalenderButton = (LinearLayout) getActivity().findViewById(R.id.calenderbutton);
        llBlockAppoinmentButton = (LinearLayout) getActivity().findViewById(R.id.blockappoinmentbutton);
        llProgressButton = (LinearLayout) getActivity().findViewById(R.id.progressbutton);
        llMessagebutton = (RelativeLayout) getActivity().findViewById(R.id.messagebutton);
        llCalenderButton.setClickable(true);
        llBlockAppoinmentButton.setClickable(true);
        llProgressButton.setClickable(true);
        llMessagebutton.setClickable(true);


        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                bundle = new Bundle();
                try {
                    Log.i("DO", "HERE");
                    if (getArguments().getString("PAGE").equals("BOOKINGADAPTER")) {
                        bundle.putString("DateChange", getArguments().getString("DateChange"));
                        fragmentTransaction = fragmentManager.beginTransaction();
                        BookAppointmentFragment cal_fragment = new BookAppointmentFragment();
                        cal_fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
//                        int count = fragmentManager.getBackStackEntryCount();
//                        fragmentTransaction.addToBackStack(String.valueOf(count));
                        fragmentTransaction.commit();
                    } else {
                        bundle.putString("DateChange", getArguments().getString("DateChange"));
                        fragmentTransaction = fragmentManager.beginTransaction();
                        AppointmentListFragment cal_fragment = new AppointmentListFragment();
                        cal_fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
//                        int count = fragmentManager.getBackStackEntryCount();
//                        fragmentTransaction.addToBackStack(String.valueOf(count));
                        fragmentTransaction.commit();
                    }
                } catch (Exception e) {
                    try {
                        if (getArguments().getString("PAGE").equals("APPOINTMENTLISTFRAGMENT")) {

                            bundle.putString("DateChange", getArguments().getString("DateChange"));
                            fragmentTransaction = fragmentManager.beginTransaction();
                            AppointmentListFragment cal_fragment = new AppointmentListFragment();
                            cal_fragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
//                            int count = fragmentManager.getBackStackEntryCount();
//                            fragmentTransaction.addToBackStack(String.valueOf(count));
                            fragmentTransaction.commit();
                        }
                    } catch (Exception ex) {
                        bundle.putString("DateChange", getArguments().getString("DateChange"));
                        fragmentTransaction = fragmentManager.beginTransaction();
                        CalenderFragment cal_fragment = new CalenderFragment();
                        cal_fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
//                        int count = fragmentManager.getBackStackEntryCount();
//                        fragmentTransaction.addToBackStack(String.valueOf(count));
                        fragmentTransaction.commit();
                    }

                }
                //}

            }
        });

        return fView;
    }

    public void getAppointmentList(final String date) {

        AsyncTask<Void, Void, Void> appointList = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                scrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                llCancel.setClickable(true);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/get_all_booking?client_id=" +
                                    saveString + "&date_val=" + date)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ",""+jOBJ);

                    JSONArray jsonArray = jOBJ.getJSONArray("bookings");
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
                    }

                    // Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }

//                Log.d("URL", AppConfig.HOST + "app_control/get_all_booking?client_id=" +
//                        saveString + "&date_val=" + date);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                if (exception.equals("")) {
                    getAppointmentDetails(appointmentListDataType.getId());
                } else {
                    // Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        appointList.execute();

    }

    public void getAppointmentDetails(final String booking_id) {

        AsyncTask<Void, Void, Void> appointDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                scrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/get_each_booking_details?booking_id=" + booking_id)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ",""+jOBJ);

                    appointDataType = new AppointmentDataType(
                            jOBJ.getString("trainer_name"),
                            jOBJ.getString("trainer_image"),
                            jOBJ.getString("trainer_about"),
                            jOBJ.getString("trainer_address"),
                            jOBJ.getString("booked_by"),
                            jOBJ.getString("booked_date"),
                            jOBJ.getString("booking_time_start"),
                            jOBJ.getString("booking_time_end"),
                            jOBJ.getString("cancel_status"),
                            jOBJ.getString("status"),
                            jOBJ.getString("booking_date"),
                            jOBJ.getString("program_name"),
                            jOBJ.getString("trainer_company"),
                            jOBJ.getString("trainer_email"),
                            jOBJ.getString("trainer_id")
                    );

                    // Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }

                Log.d("URL App Frag : ", AppConfig.HOST + "app_control/get_each_booking_details?booking_id=" + booking_id);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progressBar.setVisibility(View.GONE);

                if (exception.equals("")) {
                    scrollView.setVisibility(View.VISIBLE);

                    Picasso.with(getActivity()).load(appointDataType.getTrainer_image()).transform(new Trns()).placeholder(R.drawable.no_image_available).error(R.drawable.no_image_available_placeholdder).centerCrop().fit().into(imgPic);

//                    Glide.with(getActivity())
//                            .load(appointDataType.getTrainer_image())
//                            .bitmapTransform(new BitmapTransform(getActivity()))
//                            .fitCenter()
//                            .error(R.drawable.placeholdericon)
//                            .into(imgPic);

                    txtName.setText(appointDataType.getTrainer_name());
                    txtNameSub.setText(appointDataType.getTrainer_company());
                    clientEmail.setText(appointDataType.getTrainer_email());
                    txtDesc.setText(appointDataType.getTrainer_about());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date myDate = null;
                    try {
                        myDate = dateFormat.parse(appointDataType.getBooked_date());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat timeFormat = new SimpleDateFormat("EEEE, d MMM yyyy");
                    String finalDate = timeFormat.format(myDate);
                    txtDatetime.setText(finalDate);

                    txtTo.setText(appointDataType.getBooking_time_start());
                    txtFrom.setText(appointDataType.getBooking_time_end());
                    txtLocation.setText(Html.fromHtml("<u>" + appointDataType.getTrainer_address() + "</u"));

                    msgUserName = "" + appointDataType.getTrainer_name();
                    msgUserId = "" + appointDataType.getId();// Change here


                    txtLocation.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            VIEWMAPLOCATION(appointDataType.getTrainer_address());
//
                        }

                    });


                    if (appointDataType.getCancel_status().equals("CAN_NOT")) {
                        llCancel.setVisibility(View.VISIBLE);

                        //  progressBar.setVisibility(View.GONE);
                    } else {
                        llCancel.setVisibility(View.VISIBLE);

                        getAppointmentListCancel(getArguments().getString("DateChange"));
                    }

                } else {
                    Log.d("Exception : ", exception);
                }
            }

        };
        appointDetails.execute();

    }

    private void VIEWMAPLOCATION(String trainer_address) {
        final Double[] Lat = new Double[1];
        final double[] Lng = new double[1];
        final String PALCE = trainer_address.replaceAll(" ", "%20");
        final String URL = "https://maps.googleapis.com/maps/api/geocode/json?address=" + PALCE + "&key=AIzaSyAsdkm0gt7PAsMO7uUFH-BnYOwclf0KsZI";

        AsyncTask<Void, Void, Void> MAPLIST = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(URL)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ",""+jOBJ);

                    JSONArray jsonArray = jOBJ.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject GeoM = jsonObject.getJSONObject("geometry");
                        JSONObject LATLONG = GeoM.getJSONObject("location");
                        Lat[0] = LATLONG.getDouble("lat");
                        Lng[0] = LATLONG.getDouble("lng");

                    }

                } catch (Exception e) {
                    exception = e.toString();
                }
                ;
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);


                String urlAddress = "http://maps.google.com/maps?q=" + Lat[0] + "," + Lng[0] + "(" + PALCE + ")&iwloc=A&hl=es";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
                startActivity(intent);

            }

        };
        MAPLIST.execute();

    }

    public void getAppointmentListCancel(final String date) {

        AsyncTask<Void, Void, Void> appointListCancel = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                // progressBar.setVisibility(View.VISIBLE);
                llCancel.setClickable(true);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/get_all_booking?client_id=" +
                                    saveString + "&date_val=" + date)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ",""+jOBJ);

                    JSONArray jsonArray = jOBJ.getJSONArray("bookings");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        appointmentListDataType = new AppointmentListDataType(
                                jsonObject.getString("id"),
                                jsonObject.getString("trainer_name"),
                                jsonObject.getString("booked_by"),
                                jsonObject.getString("booked_date"),
                                jsonObject.getString("booking_time_start"),
                                jsonObject.getString("booking_time_end"),
                                jsonObject.getString("status"),
                                jsonObject.getString("booking_date"),
                                jsonObject.getString("program_name"));
                    }

                    //  Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }

//                Log.d("URL", AppConfig.HOST + "app_control/get_all_booking?client_id=" +
//                        saveString + "&date_val=" + date);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progressBar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    bookingId = appointmentListDataType.getId();
                    //cancelAppointmentDetails(appointmentListDataType.getId());
                } else {
                    //  Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        appointListCancel.execute();

    }

    public void cancelAppointmentDetails(final String booking_id) {

        AsyncTask<Void, Void, Void> appointDetailsCancel = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                cancelPBar.setVisibility(View.VISIBLE);
                llCancel.setClickable(true);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";



                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/cancel_booking?booking_id=" + bookingId)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);

                    Log.d("Cancel RESPONSE", jOBJ.toString());
                    Log.d("Cancel URL", AppConfig.HOST + "app_control/cancel_booking?booking_id=" + booking_id);
                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                cancelPBar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    llCancel.setClickable(true);
                    bundle = new Bundle();

                    try {
                        if (getArguments().getString("PAGE").equals("BOOKINGADAPTER")) {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            bundle.putString("DateChange", getArguments().getString("DateChange"));
                            BookAppointmentFragment cal_fragment = new BookAppointmentFragment();
                            cal_fragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
//                            int count = fragmentManager.getBackStackEntryCount();
//                            fragmentTransaction.addToBackStack(String.valueOf(count));
                            fragmentTransaction.commit();
                        }
                    } catch (Exception e) {
                        try {
                            if (getArguments().getString("PAGE").equals("APPOINTMENTLISTFRAGMENT")) {
                                bundle.putString("DateChange", getArguments().getString("DateChange"));
                                fragmentTransaction = fragmentManager.beginTransaction();
                                AppointmentListFragment cal_fragment = new AppointmentListFragment();
                                cal_fragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
//                                int count = fragmentManager.getBackStackEntryCount();
//                                fragmentTransaction.addToBackStack(String.valueOf(count));
                                fragmentTransaction.commit();
                            }
                        } catch (Exception ex) {
                            bundle.putString("DateChange", getArguments().getString("DateChange"));
                            fragmentTransaction = fragmentManager.beginTransaction();
                            CalenderFragment cal_fragment = new CalenderFragment();
                            cal_fragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
//                            int count = fragmentManager.getBackStackEntryCount();
//                            fragmentTransaction.addToBackStack(String.valueOf(count));
                            fragmentTransaction.commit();
                        }
                    }
                } else {
                    Log.d("Exception : ", exception);
                }
            }

        };
        appointDetailsCancel.execute();

    }

}

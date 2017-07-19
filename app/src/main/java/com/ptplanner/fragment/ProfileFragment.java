package com.ptplanner.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ptplanner.R;
import com.ptplanner.customviews.TitilliumBold;
import com.ptplanner.customviews.TitilliumLight;
import com.ptplanner.datatype.ProfileViewDataType;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import com.ptplanner.helper.Trns;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton,
            back;
    RelativeLayout llMessagebutton;

    ProfileViewDataType profileViewDataType;
    String url;
    View fView;

    ImageView imgPic;
    TitilliumBold txtName;
    TitilliumLight txtNameSub, txtDesc, txtEmail, txtLocationWork, txtLocationBilling;
    ScrollView scrlBody;
    ProgressBar progBar;

    ConnectionDetector cd;
    String exception = "", urlResponse = "";

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    TextView txtMSGCount, txtCal, txtApnt, txtPrg, txtMsg;
    ImageView imgCal, imgApnt, imgPrg, imgMsg;

    String saveString;
    SharedPreferences userId;
    TitilliumLight clientEmail;
    TitilliumBold locationBold;
    LinearLayout ll_msg_new_click;

    String msgUserName = "", msgUserId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.profile_details, container, false);

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        this.fView = inflater.inflate(R.layout.profile_details, container, false);
        /////////////////////////////////////////////////////

        cd = new ConnectionDetector(getActivity());

        back = (LinearLayout) fView.findViewById(R.id.back);
        imgPic = (ImageView) fView.findViewById(R.id.img_pic);
        txtName = (TitilliumBold) fView.findViewById(R.id.txt_name);
        txtNameSub = (TitilliumLight) fView.findViewById(R.id.txt_name_sub);
        scrlBody = (ScrollView) fView.findViewById(R.id.scrl_body);
        txtDesc = (TitilliumLight) fView.findViewById(R.id.txt_desc);
        txtEmail = (TitilliumLight) fView.findViewById(R.id.txt_email);
//        txtLocationWork = (TitilliumLight) fView.findViewById(R.id.txt_location_work);
        txtLocationBilling = (TitilliumLight) fView.findViewById(R.id.txt_location_billing);
        progBar = (ProgressBar) fView.findViewById(R.id.prog_bar);
        clientEmail = (TitilliumLight) fView.findViewById(R.id.txt_clientEmail);
        locationBold = (TitilliumBold) fView.findViewById(R.id.txt_locationBold);

        ll_msg_new_click = (LinearLayout) fView.findViewById(R.id.ll_msg_new);

        if (cd.isConnectingToInternet()) {
            getProfileDetails(AppConfig.PT_ID);
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

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
        txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
        txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
        txtMsg.setTextColor(Color.parseColor("#22A7F0"));
        imgCal.setBackgroundResource(R.drawable.cal);
        imgApnt.setBackgroundResource(R.drawable.apntclicl);
        imgPrg.setBackgroundResource(R.drawable.prg);
        imgMsg.setBackgroundResource(R.drawable.msg);

        llCalenderButton.setClickable(true);
        llBlockAppoinmentButton.setClickable(false);
        llProgressButton.setClickable(true);
        llMessagebutton.setClickable(true);

        try {
            String val = getArguments().getString("AllTrainer");
            txtCal.setTextColor(Color.parseColor("#A4A4A5"));
            txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
            txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
            txtApnt.setTextColor(Color.parseColor("#22A7F0"));
            imgCal.setBackgroundResource(R.drawable.cal);
            imgApnt.setBackgroundResource(R.drawable.apntclick2);
            imgPrg.setBackgroundResource(R.drawable.prg);
            imgMsg.setBackgroundResource(R.drawable.msg);

            llCalenderButton.setClickable(true);
            llBlockAppoinmentButton.setClickable(false);
            llProgressButton.setClickable(true);
            llMessagebutton.setClickable(true);
        } catch (Exception e) {

            txtCal.setTextColor(Color.parseColor("#A4A4A5"));
            txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
            txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
            txtMsg.setTextColor(Color.parseColor("#22A7F0"));
            imgCal.setBackgroundResource(R.drawable.cal);
            imgApnt.setBackgroundResource(R.drawable.apnt);
            imgPrg.setBackgroundResource(R.drawable.prg);
            imgMsg.setBackgroundResource(R.drawable.msgfill);

            llCalenderButton.setClickable(true);
            llBlockAppoinmentButton.setClickable(true);
            llProgressButton.setClickable(true);
            llMessagebutton.setClickable(false);

        }

        try {
            Log.i("MSG_FROM", "MSG_FROM");
            String val = getArguments().getString("AllTrainer");
            ll_msg_new_click.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.i("MSG_FROM", "Error");
            ll_msg_new_click.setVisibility(View.GONE);
        }

        ll_msg_new_click.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (
//                        !msgUserName.equalsIgnoreCase("") &&
                        !msgUserId.equalsIgnoreCase("")) {
                    Intent i = new Intent(getActivity(), ChatDetailsFragment.class);
                    i.putExtra("newBack", "ProfileFragment");
                    i.putExtra("msgUserName", msgUserName);
                    i.putExtra("msgUserId", msgUserId);
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });

        clientEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!profileViewDataType.getEmail().equalsIgnoreCase("")) {
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
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{profileViewDataType.getEmail()});
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    String val = getArguments().getString("AllTrainer");
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    BookAppointmentFragment bookapnt_fragment = new BookAppointmentFragment();
                    fragmentTransaction.replace(R.id.fragment_container, bookapnt_fragment);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    Intent intent = new Intent(getActivity(), ChatDetailsFragment.class);
                    intent.putExtra("msgUserName", AppConfig.PT_NAME);
                    intent.putExtra("msgUserId", AppConfig.PT_ID);
                    getActivity().startActivity(intent);
                    getActivity().finish();

                }
            }
        });

        return fView;
    }

    public void getProfileDetails(final String pt_id) {

        AsyncTask<Void, Void, Void> profileDetais = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                progBar.setVisibility(View.VISIBLE);
                scrlBody.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/user_details?pt_id=" + pt_id)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ", "" + jOBJ);


                    profileViewDataType = new ProfileViewDataType(
                            jOBJ.getString("id"),
                            jOBJ.getString("user_type"),
                            jOBJ.getString("name"),
                            jOBJ.getString("image"),
                            jOBJ.getString("email"),
                            jOBJ.getString("address"),
                            jOBJ.getString("company"),
                            jOBJ.getString("work_address"),
                            jOBJ.getString("billing_address"),
                            jOBJ.getString("phone"),
                            jOBJ.getString("about"));

                    // Log.d("RESPONSE", jOBJ.toString());
                } catch (Exception e) {
                    exception = e.toString();
                }
                Log.d("URL Pro Frag : ", AppConfig.HOST + "app_control/user_details?pt_id=" + pt_id);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progBar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    scrlBody.setVisibility(View.VISIBLE);

                    msgUserName = "" + profileViewDataType.getName();
                    msgUserId = "" + profileViewDataType.getId();

                    Log.i("msgUserName", "" + msgUserName);
                    Log.i("msgUserId", "" + msgUserId);

                    txtName.setText(profileViewDataType.getName());
                    txtNameSub.setText(profileViewDataType.getCompany());
                    txtDesc.setText(profileViewDataType.getAbout());
                    // txtEmail.setText(profileViewDataType.getEmail());
                    clientEmail.setText(profileViewDataType.getEmail());
//                    txtLocationWork.setText(profileViewDataType.getWork_address());
//                    locationBold.setText(profileViewDataType.getWork_address());
//                    txtLocationBilling.setText(profileViewDataType.getWork_address());


                    txtLocationBilling.setText(Html.fromHtml("<u>" + profileViewDataType.getWork_address() + "</u"));

                    txtLocationBilling.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VIEWMAPLOCATION(profileViewDataType.getWork_address());
                        }
                    });

                    Picasso.with(getActivity()).load(profileViewDataType.getImage()).placeholder(R.drawable.no_image_available_placeholdder).error(R.drawable.no_image_available).fit().transform(new Trns()).into(imgPic);
                } else {
//                    Log.d("Exception : ", exception);
                    // Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        profileDetais.execute();

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
                    Log.i("jOBJ", "" + jOBJ);

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
}









package com.ptplanner.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.ptplanner.LandScreenActivity;
import com.ptplanner.R;
import com.ptplanner.adapter.MessageRoomAdapter;
import com.ptplanner.datatype.MsgDataType;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.AppController;
import com.ptplanner.helper.BadgeUtils;
import com.ptplanner.helper.ConnectionDetector;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedList;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Messagefragment extends Fragment {

    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton;
    RelativeLayout llMessagebutton;
    ProgressBar pbar;
    ListView messageRoomList;
    MessageRoomAdapter msgAdapter;
    String url;
    LinkedList<MsgDataType> msgDataTypeLinkedList;
    String exception = "", urlResponse = "";
    ConnectionDetector cd;
    String saveString;
    SharedPreferences userId;

    View fView;
    LinearLayout calendarButton, progressButton, appointmentButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //////////////////////////////////////////////////////////////////////////

        calendarButton = (LinearLayout) getActivity().findViewById(R.id.calenderbutton);
        progressButton = (LinearLayout) getActivity().findViewById(R.id.progressbutton);
        appointmentButton = (LinearLayout) getActivity().findViewById(R.id.blockappoinmentbutton);
        ///////////////////////////////////////////////////////////////////////////
        fView = inflater.inflate(R.layout.frag_message, container, false);

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        this.fView = inflater.inflate(R.layout.frag_message, container, false);
        /////////////////////////////////////////////////////

        cd = new ConnectionDetector(getActivity());

        pbar = (ProgressBar) fView.findViewById(R.id.progbar);
        pbar.setVisibility(View.GONE);
        messageRoomList = (ListView) fView.findViewById(R.id.listviewmessageroom);
        messageRoomList.setDivider(null);
        //////////////////////////////////////////////////////////////////////////
        userId = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        saveString = userId.getString("UserId", "");
        ///////////////////////////////////////////////////////////////////////////

        try {
            if (getArguments().getString("isRefreshPusher").equalsIgnoreCase("YES")) {
                ((LandScreenActivity) getActivity()).pusherFunctionally();
            }
        } catch (Exception e) {
            Log.i("RECONNECT : ", "Pusher Exception : ");
        }

        if (cd.isConnectingToInternet()) {
            getMSGLis();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


        llCalenderButton = (LinearLayout) getActivity().findViewById(R.id.calenderbutton);
        llBlockAppoinmentButton = (LinearLayout) getActivity().findViewById(R.id.blockappoinmentbutton);
        llProgressButton = (LinearLayout) getActivity().findViewById(R.id.progressbutton);
        llMessagebutton = (RelativeLayout) getActivity().findViewById(R.id.messagebutton);
        llCalenderButton.setClickable(true);
        llBlockAppoinmentButton.setClickable(true);
        llProgressButton.setClickable(true);
        llMessagebutton.setClickable(false);

        return fView;

    }

    public void refreshMSGFragment(final String sendBy, final String msg, final String dateTime) {
        try {
            for (int i = 0; i < msgDataTypeLinkedList.size(); i++) {
                if (msgDataTypeLinkedList.get(i).getUser_id().toString().equals(sendBy)) {
                    msgDataTypeLinkedList.get(i).setLast_message("" + msg);
                    msgDataTypeLinkedList.get(i).setLast_send_time("" + dateTime);
                    msgAdapter.notifyDataSetChanged();
                } else {
                }
            }
        } catch (Exception e) {
            Log.i("REFRESH EXC : ", e.toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.setIsAppRunning("NO");
        AppController.setIsNotificationState("NO");
        Log.i("State : ", "MSG onResume " + AppController.isNotificationState());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppController.setIsAppRunning("NO");
    }

    @Override
    public void onResume() {
        super.onResume();
        AppController.setIsAppRunning("NO");
        AppController.setIsNotificationState("NO");

        /*
        @Koushik Final BAtch Clear From here
         */

        AppController.K_SetLuncherAndNotificationCount(0);
        BadgeUtils.clearBadge(getActivity());



        Log.i("State : ", "MSG onResume " + AppController.isNotificationState());
    }

    @Override
    public void onStop() {
        super.onStop();
//        AppController.setIsNotificationState("YES");
        AppController.setIsAppRunning("YES");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        AppController.setIsNotificationState("YES");
        AppController.setIsAppRunning("YES");
    }

    public void getMSGLis() {

        AsyncTask<Void, Void, Void> msgList = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                ///////////////////////////////////////////////
                appointmentButton.setClickable(false);
                appointmentButton.setEnabled(false);
                progressButton.setClickable(false);
                progressButton.setEnabled(false);
                calendarButton.setClickable(false);
                calendarButton.setEnabled(false);

                ///////////////////////////////////////////////////
                pbar.setVisibility(View.VISIBLE);
                messageRoomList.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "dashboard/get_sender_list_app?logged_in_user=" +
                                    saveString)
                            .build();

                    Log.d("URL-"," "+AppConfig.HOST + "dashboard/get_sender_list_app?logged_in_user=" +
                            saveString);
                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);

                    msgDataTypeLinkedList = new LinkedList<MsgDataType>();
                    JSONArray jsonArray = jOBJ.getJSONArray("all_user");
                    if (jsonArray.length() != 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            MsgDataType msgDataType = new MsgDataType(
                                    jsonObject.getString("user_id"),
                                    jsonObject.getString("last_send_time"),
                                    jsonObject.getString("user_name"),
                                    jsonObject.getString("user_image"),
                                    jsonObject.getString("last_message"));
                            msgDataTypeLinkedList.add(msgDataType);
                        }
                    }

                    //  Log.d("RESPONSE", jOBJ.toString());

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
                appointmentButton.setClickable(true);
                appointmentButton.setEnabled(true);
                progressButton.setClickable(true);
                progressButton.setEnabled(true);
                calendarButton.setClickable(true);
                calendarButton.setEnabled(true);

                ///////////////////////////////////////////////////
                pbar.setVisibility(View.GONE);
                if (exception.equals("") & isAdded()) {
                    messageRoomList.setVisibility(View.VISIBLE);
                    msgAdapter = new MessageRoomAdapter(getActivity(), 0, msgDataTypeLinkedList);
                    messageRoomList.setAdapter(msgAdapter);
                } else {
                }
            }
        };
        msgList.execute();

    }
}



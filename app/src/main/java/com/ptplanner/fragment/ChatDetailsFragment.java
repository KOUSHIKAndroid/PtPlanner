package com.ptplanner.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ptplanner.LandScreenActivity;
import com.ptplanner.R;
import com.ptplanner.adapter.MessageChatAdapter;
import com.ptplanner.customviews.TitilliumRegularEdit;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.UserRespectiveMSGDatatype;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.AppController;
import com.ptplanner.helper.ConnectionDetector;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.OkHttpClient;

public class ChatDetailsFragment extends FragmentActivity {

    LinearLayout llViewDetails, back;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    ListView chat_history;
    MessageChatAdapter messageChatAdapter;
    ProgressBar pbarChat;

    LinkedList<UserRespectiveMSGDatatype> userRespectiveMSGDatatypeLinkedList;
    UserRespectiveMSGDatatype userRespectiveMSGDatatype;

    ImageView ed;
    TitilliumSemiBold titleChatDetails;
    EditText etSendMsg;

    String msgUserName = "", msgUserId = "", exception = "", urlResponse = "", exceptionError = "";
    ConnectionDetector cd;
    TitilliumSemiBold txtError;

    int nextChatStart = 0;

    RelativeLayout rlSendMsg, rlLoader, rlBlank;

    public String PUSHER_API_KEY = "676b2632a600d73a2182";
    public String PUSHER_CHANNEL = "test_channel";
    public String PUSHER_EVENT = "my_event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.msg_history);

        cd = new ConnectionDetector(ChatDetailsFragment.this);

        titleChatDetails = (TitilliumSemiBold) findViewById(R.id.title_chat_details);

        pbarChat = (ProgressBar) findViewById(R.id.pbar_chat);
        chat_history = (ListView) findViewById(R.id.listviewchat);
        txtError = (TitilliumSemiBold) findViewById(R.id.txt_error);
        txtError.setVisibility(View.GONE);
        pbarChat.setVisibility(View.GONE);
        chat_history.setVisibility(View.GONE);

        llViewDetails = (LinearLayout) findViewById(R.id.ll_viewdetails);
        back = (LinearLayout) findViewById(R.id.back);

        etSendMsg = (TitilliumRegularEdit) findViewById(R.id.et_send_msg);

        ed = (ImageView) findViewById(R.id.send_msg);

        rlSendMsg = (RelativeLayout) findViewById(R.id.rl_send_msg);
        rlLoader = (RelativeLayout) findViewById(R.id.rl_loader);
        rlBlank = (RelativeLayout) findViewById(R.id.rl_blank);
        rlBlank.setVisibility(View.GONE);

        fragmentManager = getSupportFragmentManager();

        try {
            if (getIntent().getStringExtra("notiSentBy") == null) {
                msgUserName = getIntent().getExtras().getString("msgUserName");
                msgUserId = getIntent().getExtras().getString("msgUserId");
                AppConfig.PT_ID = msgUserId;
                AppConfig.PT_NAME = msgUserName;
                titleChatDetails.setText(msgUserName);
            } else {
                msgUserName = getIntent().getExtras().getString("notiSenderName");
                msgUserId = getIntent().getExtras().getString("notiSentBy");
                AppConfig.PT_ID = msgUserId;
                AppConfig.PT_NAME = msgUserName;
                titleChatDetails.setText(msgUserName);
            }
        } catch (Exception e) {
            e.printStackTrace();

            msgUserName = getIntent().getExtras().getString("msgUserName");
            msgUserId = getIntent().getExtras().getString("msgUserName");
            AppConfig.PT_ID = msgUserId;
            AppConfig.PT_NAME = msgUserName;
            titleChatDetails.setText(msgUserName);
        }

        if (cd.isConnectingToInternet()) {
            getAllMessage();
        } else {
            Toast.makeText(ChatDetailsFragment.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        rlSendMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if (etSendMsg.getText().toString().startsWith(" ") || etSendMsg.getText().toString().matches("")) {
                        rlBlank.setVisibility(View.GONE);
                    } else {
                        try {
                            String msgText = URLEncoder.encode(etSendMsg.getText().toString(), "UTF-8");
                            sendMessage(
                                    msgUserId,
                                    AppConfig.loginDatatype.getSiteUserId(),
                                    msgText.replace("+", "%20")
                            );
                        } catch (Exception e) {
//                            Log.i("Send ex : ", e.toString());
                        }

                        etSendMsg.setText("");
                    }
                } else {
                    Toast.makeText(ChatDetailsFragment.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        llViewDetails.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub


                Intent intent = new Intent(ChatDetailsFragment.this, LandScreenActivity.class);
                intent.putExtra("MSG", "ChatDetailsFragment");
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    if (getIntent().getStringExtra("newBack").equalsIgnoreCase("AppointmentFragment")) {
                        onBackPressed();
                    } else if (getIntent().getStringExtra("newBack").equalsIgnoreCase("ProfileFragment")) {
                        onBackPressed();
                    } else {
                        Intent intent = new Intent(ChatDetailsFragment.this, LandScreenActivity.class);
                        intent.putExtra("MSG", "MSGFragment");
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    try {
                        Intent intent = new Intent(ChatDetailsFragment.this, LandScreenActivity.class);
                        intent.putExtra("MSG", "MSGFragment");
                        startActivity(intent);
                        finish();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        Pusher mPusher = new Pusher(PUSHER_API_KEY);
        mPusher.connect(new ConnectionEventListener() {

            //@Override
            public void onError(String message, String code, Exception e) {
                //  Log.i("---->>On Error : : ", "message :" + message + "\n code :" + code + "\n Exception :" + e.toString());
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
                    final String sendTo = jObject.getString("sent_to");
                    final String sendBy = jObject.getString("sent_by");
                    final String msg = jObject.getString("message");
                    final String send_time = jObject.getString("send_time");
                    final String sender_image = jObject.getString("sender_image");
                    final String receiver_image = jObject.getString("receiver_image");
                    final String receiver_name = jObject.getString("receiver_name");
                    final String sender_name = jObject.getString("sender_name");
                    final String status = jObject.getString("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (AppConfig.loginDatatype.getSiteUserId().equalsIgnoreCase(sendTo)) {
                                    userRespectiveMSGDatatype = new UserRespectiveMSGDatatype(
                                            true,
                                            "",
                                            sendTo,
                                            sendBy,
                                            msg,
                                            "",
                                            "",
                                            "",
                                            "",
                                            sender_image,
                                            receiver_image,
                                            status,
                                            0
                                    );
                                    messageChatAdapter.addFromReceiver(userRespectiveMSGDatatype);
                                }
                            } catch (Exception e) {
//                                Log.i("CHAT exception: ", e.toString());
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

    public void sendMessage(final String sendTo, final String sendFrom, final String MSG) {

        String postURL = AppConfig.HOST + "app_control/send_message_through_app?sent_to=" + sendTo + "&sent_by=" + sendFrom;

        rlLoader.setVisibility(View.GONE);
        rlBlank.setVisibility(View.GONE);
        rlSendMsg.setClickable(false);

        final StringRequest sr = new StringRequest(Request.Method.POST, postURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {
//                Log.i("CHAT RES @@@", stringResponse);
                rlLoader.setVisibility(View.GONE);
                rlSendMsg.setClickable(true);
                try {
                    JSONObject response = new JSONObject(stringResponse);
                    userRespectiveMSGDatatype = new UserRespectiveMSGDatatype(
                            true,
                            response.getString("id"),
                            sendTo,
                            sendFrom,
                            response.getString("message"),
                            "",
                            response.getString("send_time"),
                            response.getString("sender"),
                            response.getString("receiver"),
                            response.getString("sender_image"),
                            response.getString("receiver_image"),
                            response.getString("status"),
                            0
                    );
                    messageChatAdapter.addFromReceiver(userRespectiveMSGDatatype);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Log.i("CHAT RES SAKU", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                // Toast.makeText(ChatDetailsFragment.this, "Server not responding...!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("message", MSG);

//                Log.d("MSG", MSG);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);

    }

    public void getAllMessage() {

        AsyncTask<Void, Void, Void> allMSG = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pbarChat.setVisibility(View.VISIBLE);
                chat_history.setVisibility(View.GONE);
                txtError.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    exceptionError = "";
                    urlResponse = "";

                    OkHttpClient client = new OkHttpClient();
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(AppConfig.HOST + "dashboard/get_user_respective_messages?user_id=" +
                                    msgUserId + "&logged_in_user=" +
                                    AppConfig.loginDatatype.getSiteUserId() +
                                    "&start=0")
                            .build();

                    okhttp3.Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    final JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ",""+jOBJ);

                    try {
                        nextChatStart = jOBJ.getInt("next_start");
                    } catch (Exception backend) {
//                        Log.i("backend : ", backend.toString());
                        nextChatStart = 0;
                    }
                    userRespectiveMSGDatatypeLinkedList = new LinkedList<UserRespectiveMSGDatatype>();

                    try {

                        if(!jOBJ.getString("member_avilable").equalsIgnoreCase("FALSE")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findViewById(R.id.ll_footer).setVisibility(View.VISIBLE);
                                    findViewById(R.id.rl_blank).setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        findViewById(R.id.ll_footer).setVisibility(View.GONE);
                                        findViewById(R.id.rl_blank).setVisibility(View.VISIBLE);
                                        ((TitilliumSemiBold) findViewById(R.id.rl_blank_txt)).setText(jOBJ.getString("message"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        JSONArray jsonArray = jOBJ.getJSONArray("all_message");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            userRespectiveMSGDatatype = new UserRespectiveMSGDatatype(
                                    true,
                                    jsonObject.getString("id"),
                                    jsonObject.getString("sent_to"),
                                    jsonObject.getString("sent_by"),
                                    jsonObject.getString("message"),
                                    jsonObject.getString("read_status"),
                                    jsonObject.getString("send_time"),
                                    jsonObject.getString("sender"),
                                    jsonObject.getString("receiver"),
                                    jsonObject.getString("sender_image"),
                                    jsonObject.getString("receiver_image"),
                                    jsonObject.getString("status"),
                                    nextChatStart
                            );
                            userRespectiveMSGDatatypeLinkedList.add(userRespectiveMSGDatatype);
                        }
                    } catch (Exception ex) {
                        exceptionError = ex.toString();
                    }
                    //  Log.i("RESPONSE", jOBJ.toString());
                } catch (Exception e) {
                    exception = e.toString();
                }
                Log.i("URL : ", AppConfig.HOST + "dashboard/get_user_respective_messages?user_id=" +
                        msgUserId + "&logged_in_user=" +
                        AppConfig.loginDatatype.getSiteUserId() +
                        "&start=0");
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pbarChat.setVisibility(View.GONE);
                if (exception.equals("")) {
                    chat_history.setVisibility(View.VISIBLE);
                    if (exceptionError.equals("")) {
                        Collections.reverse(userRespectiveMSGDatatypeLinkedList);
                        messageChatAdapter = new MessageChatAdapter(ChatDetailsFragment.this, 0,
                                userRespectiveMSGDatatypeLinkedList,
                                pbarChat,
                                nextChatStart,
                                msgUserId,
                                chat_history);
                        chat_history.setAdapter(messageChatAdapter);
                    } else {
                        txtError.setVisibility(View.VISIBLE);
                    }
                } else {
//                    Log.d("Exception : ", exception);
                }
            }
        };
        allMSG.execute();
    }

    @Override
    public void onBackPressed() {
        try {
            if (getIntent().getStringExtra("newBack").equalsIgnoreCase("AppointmentFragment")) {
                super.onBackPressed();
            } else if (getIntent().getStringExtra("newBack").equalsIgnoreCase("ProfileFragment")) {
                super.onBackPressed();
            } else {
                Intent intent = new Intent(ChatDetailsFragment.this, LandScreenActivity.class);
                intent.putExtra("MSG", "MSGFragment");
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            try {
                Intent intent = new Intent(ChatDetailsFragment.this, LandScreenActivity.class);
                intent.putExtra("MSG", "MSGFragment");
                startActivity(intent);
                finish();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.setIsNotificationStateChat("NO");
        AppController.setIsNotificationState("NO");
//        Log.i("State : ", "chat onStart " + AppController.isNotificationStateChat());
    }



    @Override
    public void onResume() {
        super.onResume();
        AppController.setIsNotificationStateChat("NO");
        AppController.setIsNotificationState("NO");
//        Log.i("State : ", "chat onResume " + AppController.isNotificationStateChat());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppController.setIsNotificationStateChat("YES");
//        Log.i("State : ", "chat onPause " + AppController.isNotificationStateChat());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppController.setIsNotificationStateChat("YES");
//        Log.i("State : ", "chat onStop " + AppController.isNotificationStateChat());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppController.setIsNotificationStateChat("YES");
//        Log.i("State : ", "chat onDestroy " + AppController.isNotificationStateChat());
    }
}








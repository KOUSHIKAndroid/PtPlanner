package com.ptplanner;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.ptplanner.customviews.TitilliumLight;
import com.ptplanner.customviews.TitilliumLightEdit;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import org.json.JSONObject;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ltp on 06/08/15.
 */
public class ForgotPasswordActivity extends Activity {

    LinearLayout llBack;
    TitilliumLightEdit etEmail;
    RelativeLayout rlDone;
    ProgressBar pBar;
    TitilliumLight txtError;

    ConnectionDetector cd;

    String exception, responseMSG, urlResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //////////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_forgotpassword);
        /////////////////////////////////////////////////////

        setContentView(R.layout.activity_forgotpassword);

        cd = new ConnectionDetector(ForgotPasswordActivity.this);

        llBack = (LinearLayout) findViewById(R.id.back);
        etEmail = (TitilliumLightEdit) findViewById(R.id.et_email);
        rlDone = (RelativeLayout) findViewById(R.id.rl_done);
        txtError = (TitilliumLight) findViewById(R.id.txt_error);
        pBar = (ProgressBar) findViewById(R.id.prgbar);
        pBar.setVisibility(View.GONE);
        txtError.setVisibility(View.GONE);

        rlDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if (!etEmail.getText().toString().trim().equals("")) {
                        if (isEmailValid(etEmail.getText().toString().trim())) {
                            passwordForgot(etEmail.getText().toString().trim());
                        } else {
                            etEmail.requestFocus();
                            etEmail.setError("Inte en giltig e-post");
                        }
                    } else {
                        etEmail.setError("E-post kan inte lämnas tomt");
                        etEmail.requestFocus();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

    }


    public void passwordForgot(final String email) {

        AsyncTask<Void, Void, Void> forgot = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
                txtError.setVisibility(View.GONE);
                rlDone.setClickable(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    responseMSG = "";
                    urlResponse = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/forgot_password_client?email=" + email)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ",""+jOBJ);


                    try {
                        responseMSG = jOBJ.getString("response");
                    } catch (Exception ex) {
                        Log.i("Site : ", ex.toString());
                    }

                } catch (Exception e) {
                    exception = e.toString();
                }
                Log.d("LOGIN", AppConfig.HOST + "app_control/forgot_password_client?email=" + email);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pBar.setVisibility(View.GONE);
                rlDone.setClickable(true);
                if (exception.equals("")) {
                    if (responseMSG.equals("success")) {
//                        Toast.makeText(ForgotPasswordActivity.this, "Regenererad lösenord har skickats till din e-id", Toast.LENGTH_LONG).show();
                        Toast.makeText(ForgotPasswordActivity.this, "Ett nytt lösenord har skickats till din registrerade e-postadress", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (responseMSG.equals("failed")) {
                        txtError.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Error....", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.i(" SerVer Error : ", exception);
                    //  Toast.makeText(ForgotPasswordActivity.this, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        forgot.execute();

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }
}

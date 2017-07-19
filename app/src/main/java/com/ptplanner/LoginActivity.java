package com.ptplanner;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ptplanner.customviews.TitilliumLight;
import com.ptplanner.datatype.LoginDataType;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.AppController;
import com.ptplanner.helper.ConnectionDetector;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class LoginActivity extends Activity {

    ConnectionDetector cd;

    EditText etEmail, etPass;
    LinearLayout btnLogin;
    LinearLayout llRememberMe, llForgotPass;
    ImageView imgChkbox;
    ProgressBar pBar;
    TitilliumLight txtErrorMSG;
    String responseMSG = "", exception = "", urlResponse = "", message = "", siteUserId = "";
    SharedPreferences loginPreferences;
    String blankPassword = "", invalidEmail = "", blankEmail = "", errorMSG = "";
    String android_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

 /*
        @Koushik
        Device Unique Id forever
         */
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("@@@ DEVICE ID-", "" + android_id);
        Log.d("@@@ SANTU DEVICE TOKEN-", "" + AppConfig.appRegId);

        String languageToLoad = AppConfig.LANGUAGE; // your language "sv --- > swedish :: en ---- > english"
        Locale mlocale = new Locale(languageToLoad);
        Locale.setDefault(mlocale);
        Configuration config = new Configuration();
        config.locale = mlocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_login);

        setContentView(R.layout.activity_login);

        if (AppController.Acdeactivation)
            DEACTIVATIONNOTIFICATION();

        etEmail = (EditText) findViewById(R.id.et_email);
        etPass = (EditText) findViewById(R.id.et_pass);

        blankPassword = getResources().getString(R.string.BlankPassword);
        invalidEmail = getResources().getString(R.string.InvalidEmail);
        blankEmail = getResources().getString(R.string.BlankEmail);
        errorMSG = getResources().getString(R.string.ErrorMSG);

        btnLogin = (LinearLayout) findViewById(R.id.btn_login);

        llRememberMe = (LinearLayout) findViewById(R.id.ll_remember);
        llForgotPass = (LinearLayout) findViewById(R.id.ll_forgot);

        imgChkbox = (ImageView) findViewById(R.id.img_chkbox);

        pBar = (ProgressBar) findViewById(R.id.prgbar);
        pBar.setVisibility(View.GONE);
        txtErrorMSG = (TitilliumLight) findViewById(R.id.txt_error);
        txtErrorMSG.setVisibility(View.GONE);

        cd = new ConnectionDetector(LoginActivity.this);
//
//        etEmail.setText("makes.good.pie@gmail.com");
//        etPass.setText("1455574825");

//        etEmail.setText("soutrik@esolzmail.com");
//        etPass.setText("123456");

        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        AppConfig.isRemember = true;
        AppConfig.strRemember = "Y";
        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (cd.isConnectingToInternet()) {
                    if (!etEmail.getText().toString().trim().equals("")) {
                        if (isEmailValid(etEmail.getText().toString().trim())) {
                            if (!etPass.getText().toString().equals("")) {
                                userLogin(
                                        etEmail.getText().toString().trim(),
                                        etPass.getText().toString().trim(),
                                        AppConfig.strRemember
                                );
                            } else {
                                etPass.setError(blankPassword);
                                etPass.requestFocus();
                            }
                        } else {
                            etEmail.requestFocus();
                            etEmail.setError(invalidEmail);
                        }
                    } else {
                        etEmail.setError(blankEmail);
                        etEmail.requestFocus();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }
            }
        });

//        llRememberMe.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                if (AppConfig.isRemember) {
//                    AppConfig.isRemember = false;
//                    imgChkbox.setBackgroundResource(R.drawable.check);
//                    AppConfig.strRemember = "Y";
//                } else {
//                    AppConfig.isRemember = true;
//                    imgChkbox.setBackgroundResource(R.drawable.uncheck);
//                    AppConfig.strRemember = "N";
//                }
//            }
//        });

        llForgotPass.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                // finish();
            }
        });
    }

    private void DEACTIVATIONNOTIFICATION() {

        AlertDialog.Builder DeactiveDilog = new AlertDialog.Builder(LoginActivity.this);
        DeactiveDilog.setIcon(R.drawable.notifitnesslogo);
        DeactiveDilog.setTitle(R.string.app_name);
        DeactiveDilog.setMessage(AppController.DeactivationAlert);
        DeactiveDilog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppController.Acdeactivation = false;
            }
        }).show();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void userLogin(final String email, final String password, final String remember) {

        AsyncTask<Void, Void, Void> login = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    responseMSG = "";
                    urlResponse = "";
                    message = "";
                    URL url;
                    HttpURLConnection urlConnection = null;


                    try {
                        url = new URL(AppConfig.HOST + "login/verify_app_login?email="
                                + email + "&password=" + password
                                + "&remember_me=" + remember
                                + "&device_token=" + AppConfig.appRegId + "&device_token_ios=&device_id=" + android_id);


                        urlConnection = (HttpURLConnection) url.openConnection();
                        Log.d("URL=", url.toString());
                        int responseCode = urlConnection.getResponseCode();

                        if (responseCode == HttpStatus.SC_OK) {
                            urlResponse = readStream(urlConnection.getInputStream());
                            Log.v("CatalogClient", "Response code:" + urlResponse);

                        } else {
                            Log.v("CatalogClient", "Response code:" + responseCode);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null)
                            urlConnection.disconnect();
                    }

                    JSONObject jOBJ = new JSONObject(urlResponse);


                    if (jOBJ.getString("response").equalsIgnoreCase("error")) {
                        exception = "" + jOBJ.getString("message");
                        responseMSG = jOBJ.getString("response");
                        message = jOBJ.getString("message");
                    } else if (jOBJ.getString("response").equalsIgnoreCase("error_disable")) {
                        exception = "" + jOBJ.getString("message");
                        responseMSG = jOBJ.getString("response");
                        message = jOBJ.getString("message");
                    } else
                    {
                        try {

                            siteUserId = jOBJ.getString("site_user_id");
                            responseMSG = jOBJ.getString("response");
                            message = jOBJ.getString("message");

                            AppConfig.loginDatatype = new LoginDataType(
                                    siteUserId,
                                    email,
                                    password
                            );
                        } catch (Exception ex) {
                            // Log.i("Site : ", ex.toString());

                            responseMSG = jOBJ.getString("response");
                            message = jOBJ.getString("message");
                        }
                    }


                } catch (Exception e) {
                    exception = e.toString();
                }
//
                Log.i("Login : ", AppConfig.HOST + "login/verify_app_login?email="
                        + email + "&password=" + password
                        + "&remember_me=" + remember
                        + "&device_token=" + AppConfig.appRegId + "&device_token_ios=");

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pBar.setVisibility(View.GONE);

                if (exception.equals("")) {
                    if (responseMSG.equals("success")) {
                        if (AppConfig.strRemember.equals("Y")) {

                            Editor editor = loginPreferences.edit();
                            editor.putString("Remember", "remember");
                            editor.putString("UserId", siteUserId);

//                            @@KOUSHIK
                            editor.putBoolean("AC_Activation", true);

                            editor.putString("Username", etEmail.getText().toString().trim());
                            editor.putString("Password", etPass.getText().toString().trim());
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, LandScreenActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

//                            Editor editor = loginPreferences.edit();
//                            editor.clear();
//                            editor.commit();

                            Editor editor = loginPreferences.edit();
                            editor.putString("Remember", "not_remember");
                            editor.putString("UserId", siteUserId);

//                            @@KOUSHIK
                            editor.putBoolean("AC_Activation", true);

                            editor.putString("Username", etEmail.getText().toString().trim());
                            editor.putString("Password", etPass.getText().toString().trim());
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, LandScreenActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    } else if (responseMSG.equals("Error")) {
                        txtErrorMSG.setVisibility(View.VISIBLE);
                        if (message.equals("Wrong/non-existing account")) {
//                            txtErrorMSG.setText("Användaren existerar inte");
                            txtErrorMSG.setText("Vänligen kontakta din tränare för ett giltigt konto");
                        } else if (message.equals("Wrong Password")) {
                            txtErrorMSG.setText("Ditt konto är inaktiverat, kontakta din tränare för mer information");
                        } else {
                            txtErrorMSG.setText(message);
                        }

                    } else {
                        // Toast.makeText(LoginActivity.this, "Error....", Toast.LENGTH_LONG).show();
                    }
                } else if (exception.equalsIgnoreCase("Your account is blocked")) {
                    Log.i(" SerVer Error : ", exception);
                    // Toast.makeText(LoginActivity.this, "Server not responding....", Toast.LENGTH_LONG).show();
                    new AlertDialog.Builder(LoginActivity.this).
                            setMessage("Ditt konto är inaktiverat, kontakta din tränare för mer information")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                } else {
                    txtErrorMSG.setVisibility(View.VISIBLE);
                    if (exception.equals("Wrong/non-existing account")) {
//                        txtErrorMSG.setText("Användaren existerar inte");
                        txtErrorMSG.setText("Vänligen kontakta din tränare för ett giltigt konto");
                    } else if (exception.equals("Wrong Password")) {
                        txtErrorMSG.setText("Felaktigt lösenord, vänligen försök igen");
                    } else {
                        txtErrorMSG.setText(exception);
                    }
                }
            }

        };
        login.execute();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();

//        Log.i("State : ", "Land onResume " + AppController.isNotificationState());
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}

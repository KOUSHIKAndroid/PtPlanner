package com.happywannyan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.Validation;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView IMG_Background;
    EditText EDX_email, EDX_Password;
    CardView Card_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        IMG_Background = (ImageView) findViewById(R.id.IMG_background);
        Card_Login = (CardView) findViewById(R.id.Card_Login);
        Card_Login.setOnClickListener(this);
        EDX_email = (EditText) findViewById(R.id.EDX_email);
        EDX_Password = (EditText) findViewById(R.id.EDX_Password);
        findViewById(R.id.LL_LoginNow).setOnClickListener(this);


        Glide.with(this).load(R.drawable.temp_03).into(IMG_Background);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Card_Login:


                Loger.MSG("###", "- " + Validation.isPassword(EDX_Password.getText().toString()));
                if (!EDX_email.getText().toString().trim().equals("") &&
                        !EDX_Password.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!Validation.isValidEmail(EDX_email.getText())) {
                        new MYAlert(SignUpActivity.this).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.Please_enter_valid_email), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {
                                if (res) {
                                    EDX_email.requestFocus();
                                }
                            }
                        });

                    } else if (!Validation.isPassword(EDX_Password.getText().toString()) || EDX_Password.getText().toString().trim().length() <= 6) {
                        new MYAlert(SignUpActivity.this).AlertOnly(getResources().getString(R.string.SignUp), getResources().getString(R.string.signup_password_checkingtext), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {
                                if (res) {
                                    EDX_Password.requestFocus();
                                }
                            }
                        });

                    } else {
                        ArrayList<APIPOSTDATA> apipostdataArrayList = new ArrayList<>();
                        APIPOSTDATA apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("user_email");
                        apipostdata.setValues(EDX_email.getText().toString());
                        apipostdataArrayList.add(apipostdata);
                        apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("password");
                        apipostdata.setValues(EDX_Password.getText().toString());
                        apipostdataArrayList.add(apipostdata);
                        apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("langid");
                        apipostdata.setValues(AppContsnat.Language);
                        Loger.MSG("@@ LANG-", " " + AppContsnat.Language);
                        apipostdataArrayList.add(apipostdata);
                        new JSONPerser().API_FOR_POST(AppContsnat.BASEURL + "app_regirsation\n", apipostdataArrayList, new JSONPerser.JSONRESPONSE() {
                            @Override
                            public void OnSuccess(String Result) {
                                Loger.MSG("@@ REG", Result);
                            }

                            @Override
                            public void OnError(String Error) {
                                Loger.Error("@@ REG", Error);
                            }
                        });
                    }

                } else {
                    new MYAlert(SignUpActivity.this).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.Please_enter_both), new MYAlert.OnlyMessage() {
                        @Override
                        public void OnOk(boolean res) {
                            if (res && EDX_email.getText().toString().trim().equals("")) {
                                EDX_email.requestFocus();
                            } else {
                                EDX_Password.requestFocus();
                            }


                        }
                    });
                }
                break;

            case R.id.LL_LoginNow:
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();
                break;
            case R.id.LL_fb:
//                lklllll ttgrtgrt
                //////suraj//////
                startActivity(new Intent(SignUpActivity.this,FacebookActivity.class));
                break;

        }
    }


}

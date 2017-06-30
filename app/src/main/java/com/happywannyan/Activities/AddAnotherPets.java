package com.happywannyan.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.happywannyan.Adapter.Adapter_petlist;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFEditText;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.Search_Basic;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.POJO.PetService;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddAnotherPets extends AppCompatActivity implements View.OnClickListener {
    JSONArray other_info;
    AppLoader appLoader;
    JSONArray PetService;
    String PetTypeId = "";
    EditText TXTName;
    JSONArray SelectObject;
    RadioGroup Radio_Catspayed, Rad_catf;
    JSONArray Text, InputArea, Select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_another_pets);
        appLoader = new AppLoader(this);
        appLoader.Show();
        TXTName = (EditText) findViewById(R.id.TXTName);
        Radio_Catspayed = (RadioGroup) findViewById(R.id.Radio_Catspayed);
        Rad_catf = (RadioGroup) findViewById(R.id.Rad_catf);
        new AppContsnat(this);
        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "parent_service", new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject object = new JSONObject(Result);
                    Loger.MSG("@@" + getClass().getName(), object.toString());
                    appLoader.Dismiss();
                    PetService = object.getJSONArray("allPetDetails");

                } catch (JSONException e) {
                    appLoader.Dismiss();
                }
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AppContsnat(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Hea:

                new MYAlert(AddAnotherPets.this).AlertTextLsit("" + getResources().getString(R.string.Chosepettype),
                        PetService, "name", new MYAlert.OnSignleListTextSelected() {
                            @Override
                            public void OnSelectedTEXT(JSONObject jsonObject) {
                                Loger.MSG("@@ Data", "" + jsonObject);
                                try {
                                    PetTypeId = jsonObject.getString("id");
                                    ((SFNFTextView) findViewById(R.id.Txt_type)).setText(jsonObject.getString("name"));
                                    ArrayList<APIPOSTDATA> params = new ArrayList<>();
                                    APIPOSTDATA apipostdata = new APIPOSTDATA();
                                    apipostdata.setPARAMS("user_id");
                                    apipostdata.setValues(AppContsnat.UserId);
                                    params.add(apipostdata);
                                    apipostdata = new APIPOSTDATA();
                                    apipostdata.setPARAMS("lang_id");
                                    apipostdata.setValues(AppContsnat.Language);
                                    params.add(apipostdata);
                                    apipostdata = new APIPOSTDATA();
                                    apipostdata.setPARAMS("pet_type_id");
                                    apipostdata.setValues(PetTypeId);
                                    params.add(apipostdata);
                                    appLoader.Show();
                                    new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "app_users_petinfo?", params, new JSONPerser.JSONRESPONSE() {
                                        @Override
                                        public void OnSuccess(String Result) {
                                            findViewById(R.id.Body).setVisibility(View.VISIBLE);
                                            SETDatField(Result);
                                            appLoader.Dismiss();
                                        }

                                        @Override
                                        public void OnError(String Error, String Response) {
                                            appLoader.Dismiss();
                                        }

                                        @Override
                                        public void OnError(String Error) {
                                            appLoader.Dismiss();
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                break;

            case R.id.IMG_icon_back:
                finish();
                break;
            case R.id.SE_Year:
                try {
                    new MYAlert(AddAnotherPets.this).AlertTextLsit(SelectObject.getJSONObject(0).getString("input_name"), SelectObject.getJSONObject(0).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_Year)).setText(jsonObject.getString("option_name"));
                                ((SFNFTextView) findViewById(R.id.TXT_Year)).setTag(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.SE_Month:
                try {
                    new MYAlert(AddAnotherPets.this).AlertTextLsit(SelectObject.getJSONObject(1).getString("input_name"), SelectObject.getJSONObject(1).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_Month)).setText(jsonObject.getString("option_name"));
                                ((SFNFTextView) findViewById(R.id.TXT_Month)).setTag(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.RL_Gender:
                try {
                    new MYAlert(AddAnotherPets.this).AlertTextLsit(SelectObject.getJSONObject(2).getString("input_name"), SelectObject.getJSONObject(2).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_gender)).setText(jsonObject.getString("option_name"));
                                ((SFNFTextView) findViewById(R.id.TXT_gender)).setTag(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.RL_petSize:
                try {
                    new MYAlert(AddAnotherPets.this).AlertTextLsit(SelectObject.getJSONObject(3).getString("input_name"), SelectObject.getJSONObject(3).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_petsize)).setText(jsonObject.getString("option_name"));
                                ((SFNFTextView) findViewById(R.id.TXT_petsize)).setTag(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.RL_breed:
                try {
                    new MYAlert(AddAnotherPets.this).AlertTextLsit(SelectObject.getJSONObject(4).getString("input_name"), SelectObject.getJSONObject(4).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_breed)).setText(jsonObject.getString("option_name"));
                                ((SFNFTextView) findViewById(R.id.TXT_breed)).setTag(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private void SETDatField(String result) {
        Loger.MSG("@@" + getClass().getName(), result);
        try {
            JSONObject MainObject = new JSONObject(result);
            JSONObject info_array = MainObject.getJSONArray("info_array").getJSONObject(0);
            other_info = info_array.getJSONArray("other_info");
            SelectObject=new JSONArray();

            ((SFNFTextView) findViewById(R.id.TXT_breed)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_breed)).setTag(null);

            ((SFNFTextView) findViewById(R.id.TXT_petsize)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_petsize)).setTag(null);

            ((SFNFTextView) findViewById(R.id.TXT_gender)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_gender)).setTag(null);

            ((SFNFTextView) findViewById(R.id.TXT_Month)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_Month)).setTag(null);

            ((SFNFTextView) findViewById(R.id.TXT_Year)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_Year)).setTag(null);


            findViewById(R.id.RADIO1).setVisibility(View.GONE);
            findViewById(R.id.Radio_div1).setVisibility(View.GONE);
            findViewById(R.id.Radio2).setVisibility(View.GONE);
            findViewById(R.id.Raddio_deiver2).setVisibility(View.GONE);
            ((RelativeLayout)findViewById(R.id.RL_breed)).setVisibility(View.GONE);
            for (int j = 0; j < other_info.length(); j++) {
                JSONObject jsonObject=other_info.getJSONObject(j);

                if(jsonObject.getString("input_field_type").equals("3"))
                SelectObject.put(jsonObject);


                if (jsonObject.getString("input_field_type").equals("1")) {
                    TXTName.setHint(jsonObject.getString("show_name"));
                    TXTName.setTag(jsonObject);
                }
                if (jsonObject.getString("input_field_type").equals("2")) {
                    ((EditText) findViewById(R.id.EditDescribe)).setTag(jsonObject);
                }
                if (j>5 && jsonObject.getString("input_field_type").equals("5")) {
                    findViewById(R.id.RADIO1).setVisibility(View.VISIBLE);
                    findViewById(R.id.Radio_div1).setVisibility(View.VISIBLE);
                    ((SFNFTextView) findViewById(R.id.B1)).setText(jsonObject.getString("input_name"));
                    JSONArray array = jsonObject.getJSONArray("input_option");
                    Radio_Catspayed.removeAllViewsInLayout();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        RadioButton btn = new RadioButton(this);
                        btn.setText(object.getString("option_name"));
                        btn.setPadding(0, 25, 25, 25);
                        btn.setTag(object);
                        Radio_Catspayed.addView(btn);
                    }

                }
                if (j> 7 && jsonObject.getString("input_field_type").equals("5")) {
                    findViewById(R.id.Radio2).setVisibility(View.VISIBLE);
                    findViewById(R.id.Raddio_deiver2).setVisibility(View.VISIBLE);
                    ((SFNFTextView) findViewById(R.id.C1)).setText(jsonObject.getString("input_name"));
                    JSONArray array = jsonObject.getJSONArray("input_option");
                    Rad_catf.removeAllViewsInLayout();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        RadioButton btn = new RadioButton(this);
                        btn.setPadding(0, 25, 25, 25);
                        btn.setTag(object);
                        btn.setText(object.getString("option_name"));
                        Rad_catf.addView(btn);
                    }

                }

            }


            ((SFNFTextView)findViewById(R.id.T4)).setText(SelectObject.getJSONObject(0).getString("input_name"));
            ((SFNFTextView)findViewById(R.id.T3)).setText(SelectObject.getJSONObject(1).getString("input_name"));
            ((SFNFTextView)findViewById(R.id.T5)).setText(SelectObject.getJSONObject(2).getString("input_name"));
            ((SFNFTextView)findViewById(R.id.T6)).setText(SelectObject.getJSONObject(3).getString("input_name"));
            if(SelectObject.length()>3)
            {
            ((SFNFTextView)findViewById(R.id.TXT_breed)).setText(SelectObject.getJSONObject(4).getString("input_name"));
                ((RelativeLayout)findViewById(R.id.RL_breed)).setVisibility(View.VISIBLE);
            }




//
//            if (other_info.length() >= 6 && other_info.getJSONObject(7).getString("input_field_type").equals("5")) {
//                findViewById(R.id.RADIO1).setVisibility(View.VISIBLE);
//                findViewById(R.id.Radio_div1).setVisibility(View.VISIBLE);
//                ((SFNFTextView) findViewById(R.id.B1)).setText(other_info.getJSONObject(7).getString("input_name"));
//                JSONArray array = other_info.getJSONObject(7).getJSONArray("input_option");
//                Radio_Catspayed.removeAllViewsInLayout();
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject object = array.getJSONObject(i);
//                    RadioButton btn = new RadioButton(this);
//                    btn.setText(object.getString("option_name"));
//                    btn.setPadding(0, 25, 25, 25);
//                    btn.setTag(object);
//                    Radio_Catspayed.addView(btn);
//                }
//
//            } else {
//                findViewById(R.id.RADIO1).setVisibility(View.GONE);
//                findViewById(R.id.Radio_div1).setVisibility(View.GONE);
//            }
//            if (other_info.length() >= 9 && other_info.getJSONObject(8).getString("input_field_type").equals("5")) {
//                findViewById(R.id.Radio2).setVisibility(View.VISIBLE);
//                findViewById(R.id.Raddio_deiver2).setVisibility(View.VISIBLE);
//                ((SFNFTextView) findViewById(R.id.C1)).setText(other_info.getJSONObject(8).getString("input_name"));
//                JSONArray array = other_info.getJSONObject(8).getJSONArray("input_option");
//                Rad_catf.removeAllViewsInLayout();
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject object = array.getJSONObject(i);
//                    RadioButton btn = new RadioButton(this);
//                    btn.setPadding(0, 25, 25, 25);
//                    btn.setTag(object);
//                    btn.setText(object.getString("option_name"));
//                    Rad_catf.addView(btn);
//                }
//
//            } else {
//                findViewById(R.id.Radio2).setVisibility(View.GONE);
//                findViewById(R.id.Raddio_deiver2).setVisibility(View.GONE);
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

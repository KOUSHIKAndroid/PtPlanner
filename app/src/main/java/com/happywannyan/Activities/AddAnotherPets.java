package com.happywannyan.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

public class AddAnotherPets extends AppCompatActivity implements View.OnClickListener{
    JSONArray other_info;
    AppLoader appLoader;
    JSONArray PetService;
    String PetTypeId="";
   EditText TXTName;
    JSONArray Text,InputArea,Select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_another_pets);
        appLoader=new AppLoader(this);
        appLoader.Show();
        TXTName=(EditText)findViewById(R.id.TXTName);
        new AppContsnat(this);
        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "parent_service", new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject object=new JSONObject(Result);
                    Loger.MSG("@@"+getClass().getName(),object.toString());
                    appLoader.Dismiss();
                     PetService=object.getJSONArray("allPetDetails");

                }catch (JSONException e)
                {
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
        switch (view.getId()){
            case R.id.Hea:

                new MYAlert(AddAnotherPets.this).AlertTextLsit("" + getResources().getString(R.string.Chosepettype),
                        PetService, "name", new MYAlert.OnSignleListTextSelected() {
                            @Override
                            public void OnSelectedTEXT(JSONObject jsonObject) {
                                Loger.MSG("@@ Data",""+jsonObject);
                                try {
                                    PetTypeId=jsonObject.getString("id");
                                    ((SFNFTextView)findViewById(R.id.Txt_type)).setText(jsonObject.getString("name"));
                                    ArrayList<APIPOSTDATA>params=new ArrayList<>();
                                    APIPOSTDATA apipostdata=new APIPOSTDATA();
                                    apipostdata.setPARAMS("user_id");
                                    apipostdata.setValues(AppContsnat.UserId);
                                    params.add(apipostdata);
                                    apipostdata=new APIPOSTDATA();
                                    apipostdata.setPARAMS("lang_id");
                                    apipostdata.setValues(AppContsnat.Language);
                                    params.add(apipostdata);
                                    apipostdata=new APIPOSTDATA();
                                    apipostdata.setPARAMS("pet_type_id");
                                    apipostdata.setValues(PetTypeId);
                                    params.add(apipostdata);
                                    appLoader.Show();
                                    new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "app_users_petinfo?", params, new JSONPerser.JSONRESPONSE() {
                                        @Override
                                        public void OnSuccess(String Result) {
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
                    new MYAlert(AddAnotherPets.this).AlertTextLsit(getResources().getString(R.string.Year), other_info.getJSONObject(1).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView)findViewById(R.id.TXT_Year)).setText(jsonObject.getString("option_name"));
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
                    new MYAlert(AddAnotherPets.this).AlertTextLsit(getResources().getString(R.string.Month), other_info.getJSONObject(2).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView)findViewById(R.id.TXT_Month)).setText(jsonObject.getString("option_name"));
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
                    new MYAlert(AddAnotherPets.this).AlertTextLsit(getResources().getString(R.string.Gender), other_info.getJSONObject(3).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView)findViewById(R.id.TXT_gender)).setText(jsonObject.getString("option_name"));
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
                    new MYAlert(AddAnotherPets.this).AlertTextLsit(getResources().getString(R.string.pet_size), other_info.getJSONObject(5).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView)findViewById(R.id.TXT_petsize)).setText(jsonObject.getString("option_name"));
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
                    new MYAlert(AddAnotherPets.this).AlertTextLsit(getResources().getString(R.string.pet_size), other_info.getJSONObject(4).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView)findViewById(R.id.TXT_breed)).setText(jsonObject.getString("option_name"));
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
        Loger.MSG("@@"+getClass().getName(),result);
        try {
            JSONObject MainObject=new JSONObject(result);
            JSONObject info_array=MainObject.getJSONArray("info_array").getJSONObject(0);
             other_info=info_array.getJSONArray("other_info");

            if(other_info.getJSONObject(0).getString("input_field_type").equals("1"))
            {
                TXTName.setHint(other_info.getJSONObject(0).getString("show_name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.ptplanner.K_DataBase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ptplanner.R;
import com.ptplanner.datatype.DateRespectiveDiaryDataType;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.Trns;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by su on 7/11/17.
 */

public abstract class OFFLineDataSave {
    Context mContext;

    public OFFLineDataSave(Context mContext) {
        this.mContext = mContext;
    }

   public abstract void OnsaveSucess(String Response);
    public abstract void OnsaveError(String Error);
    public void SaveDiary(final String Date){
        final String[] exception = {""};
       new AsyncTask<Void, String, String>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();



            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/get_date_respective_diary?logged_in_user=" + AppConfig.loginDatatype.getSiteUserId()
                                    + "&date_val=" + Date)
                            .build();



                    Response response = client.newCall(request).execute();

                    Log.d("Diary @@ URL : ",AppConfig.HOST + "app_control/get_date_respective_diary?logged_in_user=" + AppConfig.loginDatatype.getSiteUserId()
                            + "&date_val=" + Date);
                   return response.body().string();



                } catch (Exception e) {
                    exception[0] = e.toString();
                }
                return exception[0];
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
               if( exception[0].equals(result))
               {
                   OnsaveError(result);
               }else {
                   new Database(mContext).SetDirayPageData(Date, result, new LocalDataResponse() {
                       @Override
                       public void OnSuccess(String Response) {
                           Log.d("Diary-",Response);
                           SaveDiate(Date);
                       }

                       @Override
                       public void OnNotfound(String NotFound) {
                            OnsaveError(NotFound);
                       }
                   });
               }
            }
        }.execute();
    }


    public void SaveDiate(final String Date){
        final String[] exception = {""};
        new AsyncTask<Void, String, String>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();



            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/date_respective_client_meal?logged_in_user=" + AppConfig.loginDatatype.getSiteUserId()
                                    + "&date_val=" + Date)
                            .build();



                    Response response = client.newCall(request).execute();
                    Log.d("Diate @@ URL : ",AppConfig.HOST + "app_control/date_respective_client_meal?logged_in_user=" + AppConfig.loginDatatype.getSiteUserId()
                            + "&date_val=" + Date);

//
//                    JSONObject obj=new JSONObject(response.body().string());
//                    JSONArray meal=obj.getJSONArray("meal");
//
//                    for(int i=0;i<meal.length();i++)
//                    {
//                        Log.d("@@@888",meal.getJSONObject(i).getString("meal_image"));
////                        Glide.with(mContext)
////                                .load(meal.getJSONObject(i).getString("meal_image"))
////                                .diskCacheStrategy(DiskCacheStrategy.ALL);
//
//
//                    }







                    return response.body().string();



                } catch (Exception e) {
                    exception[0] = e.toString();
                }
                return exception[0];
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if( exception[0].equals(result))
                {
                    OnsaveError(result);
                }else {
                    Log.d("@@@888",result);
                    new Database(mContext).SetDiate_PageData(Date, result, new LocalDataResponse() {
                        @Override
                        public void OnSuccess(String Response) {
                            Log.d("Diate-",Response);
                            OnsaveSucess(Response);
                        }

                        @Override
                        public void OnNotfound(String NotFound) {
                            OnsaveError(NotFound);
                        }
                    });
                }
            }
        }.execute();
    }
}

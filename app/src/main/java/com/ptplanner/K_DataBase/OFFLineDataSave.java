package com.ptplanner.K_DataBase;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ptplanner.R;
import com.ptplanner.datatype.DateRespectiveDiaryDataType;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.Trns;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by su on 7/11/17.
 */

public abstract class OFFLineDataSave {
    Activity mContext;

    public OFFLineDataSave(Activity mContext) {
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


                    try {
                        JSONObject obj = new JSONObject(result);
                        JSONArray meal=obj.getJSONArray("meal");

                        for(int i=0;i<meal.length();i++)
                        {
                            Log.d("@@@888",meal.getJSONObject(i).getString("meal_image"));

                            View view=mContext.getLayoutInflater().inflate(R.layout.diet_item,null);
//                            View view2=mContext.getLayoutInflater().inflate(R.layout.frag_dietlist_details,null);

                            ImageView imageView=(ImageView)view.findViewById(R.id.img_diet);
//                            ImageView imageView2=(ImageView)view2.findViewById(R.id.meal_image);
                             Glide.with(mContext)
                                .load(meal.getJSONObject(i).getString("meal_image"))
                                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);




                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

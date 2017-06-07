package com.happywannyan.Utils;

import android.os.AsyncTask;

import com.happywannyan.POJO.APIPOSTDATA;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by apple on 19/05/17.
 */

public class JSONPerser {

        public interface  JSONRESPONSE{
            void OnSuccess( String Result);
            void OnError(String Error);
        }


    public void API_FOR_GET(final String URL, final ArrayList<APIPOSTDATA> apipostdata, final JSONRESPONSE jsonresponse){


        new AsyncTask<Void, Void, Void>() {

            private String respose = null;
            private Exception exception=null;
            String PARAMS="";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            if(apipostdata!=null && apipostdata.size()>0) {
                PARAMS="&";
                for (APIPOSTDATA data : apipostdata) {
                    PARAMS = PARAMS + data.getPARAMS() + "=" + data.getValues()+"&";
                }

                Loger.MSG("url", "" + URL + PARAMS);
            }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {

                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(5000, TimeUnit.MILLISECONDS).build();
                        Request request = new Request.Builder().url(URL+PARAMS).build();
                        Response response = client.newCall(request).execute();

                        respose = response.body().string();

                       Loger.MSG("response", "respose_::" + respose);
                       Loger.MSG("response", "respose_ww_message::" + response.message());
                       Loger.MSG("response", "respose_ww_headers::" + response.headers());
                       Loger.MSG("response", "respose_ww_isRedirect::" + response.isRedirect());
//                       Loger.MSG("response", "respose_ww_body::" + response.body().string());
                    }
                } catch (Exception e) {
                    this.exception=e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception==null) {
                    jsonresponse.OnSuccess(respose);

                }else {
                    jsonresponse.OnSuccess(exception.getMessage()+"");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public void API_FOR_POST(final String URL, final ArrayList<APIPOSTDATA> apipostdata, final JSONRESPONSE jsonresponse){


        new AsyncTask<Void, Void, Void>() {

            private String respose = null;
            private Exception exception=null;
            MultipartBody.Builder buildernew;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM);
                for(APIPOSTDATA data : apipostdata){
                    buildernew.addFormDataPart(""+data.getPARAMS(), data.getValues());
                }

            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {
                        MultipartBody requestBody = buildernew.build();
                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(5000, TimeUnit.MILLISECONDS).build();
                        Request request = new Request.Builder().url(URL) .method("POST", RequestBody.create(null, new byte[0]))
                                .post(requestBody).build();
                        Response response = client.newCall(request).execute();

                        respose = response.body().string();

                        Loger.MSG("response", "respose_::" + respose);
                        Loger.MSG("response", "respose_ww_message::" + response.message());
                        Loger.MSG("response", "respose_ww_headers::" + response.headers());
                        Loger.MSG("response", "respose_ww_isRedirect::" + response.isRedirect());
//                       Loger.MSG("response", "respose_ww_body::" + response.body().string());
                    }
                } catch (Exception e) {
                    this.exception=e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception==null) {

                    try{
                        if(new JSONObject(respose).getBoolean("response"))
                        {
                            jsonresponse.OnSuccess(respose);
                        }else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message")+"");
                        }
                    }catch (Exception e){}



                }else {
                    jsonresponse.OnError(exception.getMessage()+"");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



}

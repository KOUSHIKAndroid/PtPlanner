package com.ptplanner.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ptplanner.K_DataBase.Database;
import com.ptplanner.K_DataBase.LocalDataResponse;
import com.ptplanner.R;
import com.ptplanner.adapter.AdapterTrainingListRecyclerView;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.AllExercisesDataType;
import com.ptplanner.datatype.ExerciseSetsDataype;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import com.ptplanner.helper.ItemOffsetDecoration;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by su on 12/22/17.
 */

public class TrainingFragmentList extends Fragment {

//    ProgressDialog progressDialog;
    RecyclerView rcv_list;
    TitilliumSemiBold tv_description;
    AdapterTrainingListRecyclerView adapterTrainingListRecyclerView;

    static String urlResponse,dateChange;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    TitilliumSemiBold txt_title_exercise;
    String dialogString,exception;
    Database LocalDatabase;



    public static TrainingFragmentList getInstance(String urlResponse1,String dateChange1) {
        urlResponse=urlResponse1;
        dateChange=dateChange1;
        Log.i("saveString-->",urlResponse);
        TrainingFragmentList trainingFragmentList = new TrainingFragmentList();
        return trainingFragmentList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training_list, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogString = getResources().getString(R.string.frag_training_alertDialog);
        rcv_list = (RecyclerView) view.findViewById(R.id.rcv_list);
        tv_description = (TitilliumSemiBold) view.findViewById(R.id.tv_description);
        txt_title_exercise= (TitilliumSemiBold) view.findViewById(R.id.txt_title_exercise);

        LocalDatabase = new Database(getActivity());

        tv_description.setVisibility(View.GONE);
        tv_description.setMovementMethod(new ScrollingMovementMethod());

        rcv_list.setVisibility(View.GONE);

//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage(getResources().getString(R.string.please_wait));
//        progressDialog.setCancelable(false);


//        tv_description.setText("");

        fragmentManager = getActivity().getSupportFragmentManager();

        //rcv_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (AppConfig.allExercisesDataTypeArrayList.size() > 0) {
            LocalDatabase.GET_ProgramDetails(AppConfig.allExercisesDataTypeArrayList.get(0).getUser_program_id(), new LocalDataResponse() {
                @Override
                public void OnSuccess(String Response) {
                    try {
                        Log.d("@@ Progra,-", Response);
                        txt_title_exercise.setText(new JSONObject(Response).getString("program_name"));
                        if (new JSONObject(Response).getString("program_name").equals("")) {
                            txt_title_exercise.setText("Vilodag");
                        } else {
                            txt_title_exercise.setText("" + new JSONObject(Response).getString("program_name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnNotfound(String NotFound) {
                    txt_title_exercise.setText("Vilodag");
                }
            });
        }else {
            txt_title_exercise.setText("Vilodag");
        }

        rcv_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        rcv_list.addItemDecoration(itemDecoration);


        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        try {
            JSONObject urlResponseJson = new JSONObject(urlResponse);
            JSONArray jsonArrayAllExercises = urlResponseJson.getJSONArray("all_exercises");


            if (urlResponseJson.getString("program_description").trim().equals("")){
                tv_description.setVisibility(View.GONE);
            }else {
                tv_description.setVisibility(View.VISIBLE);
                tv_description.setText(urlResponseJson.getString("program_description"));
            }


            if (jsonArrayAllExercises.length()>0) {

                rcv_list.setVisibility(View.VISIBLE);

                adapterTrainingListRecyclerView = new AdapterTrainingListRecyclerView(getActivity(), jsonArrayAllExercises, new OnItemClickByMe() {
                    @Override
                    public void callBackMe(int position,JSONObject jsonObject) {

                        Bundle bundleTraining = new Bundle();
                        bundleTraining.putString("DateChange", dateChange);
                        bundleTraining.putString("position", String.valueOf(position));

                        fragmentTransaction = fragmentManager.beginTransaction();
                        TrainingFragment trn_fragment = new TrainingFragment();
                        trn_fragment.setArguments(bundleTraining);
                        if (new ConnectionDetector(getActivity()).isConnectingToInternet())
                            fragmentTransaction.replace(R.id.fragment_container, trn_fragment);
                        else
                            fragmentTransaction.add(R.id.fragment_container, trn_fragment);
                        int count = fragmentManager.getBackStackEntryCount();
                        fragmentTransaction.addToBackStack(String.valueOf(count));
                        fragmentTransaction.commit();
                    }
                });

                rcv_list.setAdapter(adapterTrainingListRecyclerView);
            }else {
                rcv_list.setVisibility(View.GONE);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder
                        .setMessage(dialogString)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface OnItemClickByMe {
        void callBackMe(int position,JSONObject jsonObject);
    }



//    public void getAllEvents() {
//
//        AsyncTask<Void, Void, Void> allEvents = new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected void onPreExecute() {
//                // TODO Auto-generated method stub
//                super.onPreExecute();
//                progressDialog.show();
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                // TODO Auto-generated method stub
//                try {
//                    exception = "";
//                    urlResponse = "";
//
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder()
//                            .url(AppConfig.HOST + "app_control/get_all_events_for_date/?" +
//                                    "client_id=" + saveString +
//                                    "&date_val=" + DateChange)
//                            .build();
//                    Log.i("ALL EVENTS- URL", AppConfig.HOST + "app_control/get_all_events_for_date/?" +
//                            "client_id=" + saveString +
//                            "&date_val=" + DateChange);
//
//                    Response response = client.newCall(request).execute();
//
//
//                    urlResponse = response.body().string();
//                    JSONObject jOBJ = new JSONObject(urlResponse);
//                    Log.i("jOBJ", "" + jOBJ);
//
//                    JSONArray jsonArray = jOBJ.getJSONArray("all_exercises");
////
//                    Log.d("getallEventsForDate", jOBJ.toString());
//
//                } catch (Exception e) {
//                    exception = e.toString();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//                // TODO Auto-generated method stub
//                super.onPostExecute(result);
//
//                if (exception.equals("") & isAdded()) {
//                    try {
//                        JSONObject urlResponseJson = new JSONObject(urlResponse);
//
//                        JSONArray jsonArrayAllExercises = urlResponseJson.getJSONArray("all_exercises");
//
//                        // if(jsonArray.length() > 0) {
//                        AppConfig.allExercisesDataTypeArrayList = new ArrayList<AllExercisesDataType>();
//                        AppConfig.exerciseSetsDataypeArrayList = new ArrayList<ExerciseSetsDataype>();
//
//                        for (int j = 0; j < jsonArrayAllExercises.length(); j++) {
//                            JSONObject jsonObject = jsonArrayAllExercises.getJSONObject(j);
//                            try {
//                                JSONArray jArr = jsonObject.getJSONArray("exercise_sets");
//                                if (jArr.length() > 0) {
//                                    for (int k = 0; k < jArr.length(); k++) {
//                                        JSONObject jObject = jArr.getJSONObject(k);
//                                        ExerciseSetsDataype exerciseSetsDataype = new ExerciseSetsDataype(
//                                                jObject.getString("reps"),
//                                                jObject.getString("kg")
//                                        );
//                                        AppConfig.exerciseSetsDataypeArrayList.add(exerciseSetsDataype);
//                                    }
//                                }
//                            } catch (Exception ex) {
//                                Log.i("exercise_sets", ex.toString());
//                            }
//                            AllExercisesDataType allExercisesDataType = new AllExercisesDataType(
//                                    jsonObject.getString("user_program_id"),
//                                    jsonObject.getString("exercise_id"),
//                                    jsonObject.getString("exercise_title"),
//                                    jsonObject.getString("instruction"),
//                                    AppConfig.exerciseSetsDataypeArrayList
//                            );
//                            allExercisesDataType.setTraingingPageData(jsonObject.getJSONObject("exercise_inDetails").toString());
//                            AppConfig.allExercisesDataTypeArrayList.add(allExercisesDataType);
//                        }
//
//
//
//                        if (urlResponseJson.getString("program_description").trim().equals("")){
//                            tv_description.setVisibility(View.GONE);
//                        }else {
//                            tv_description.setVisibility(View.VISIBLE);
//                            tv_description.setText(urlResponseJson.getString("program_description"));
//                        }
//
//
//
//                        if (jsonArrayAllExercises.length()>0) {
//
//
//                            rcv_list.setVisibility(View.VISIBLE);
//
//                            adapterTrainingListRecyclerView = new AdapterTrainingListRecyclerView(getActivity(), jsonArrayAllExercises, new OnItemClickByMe() {
//                                @Override
//                                public void callBackMe(int position,JSONObject jsonObject) {
//
//                                    Bundle bundleTraining = new Bundle();
//                                    bundleTraining.putString("DateChange", DateChange);
//                                    bundleTraining.putString("position", String.valueOf(position));
//
//                                    fragmentTransaction = fragmentManager.beginTransaction();
//                                    TrainingFragment trn_fragment = new TrainingFragment();
//                                    trn_fragment.setArguments(bundleTraining);
//                                    if (new ConnectionDetector(getActivity()).isConnectingToInternet())
//                                        fragmentTransaction.replace(R.id.fragment_container, trn_fragment);
//                                    else
//                                        fragmentTransaction.add(R.id.fragment_container, trn_fragment);
//                                    int count = fragmentManager.getBackStackEntryCount();
//                                    fragmentTransaction.addToBackStack(String.valueOf(count));
//                                    fragmentTransaction.commit();
//                                }
//                            });
//
//                            rcv_list.setAdapter(adapterTrainingListRecyclerView);
//                        }else {
//                            rcv_list.setVisibility(View.GONE);
//
//                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//                            alertDialogBuilder
//                                    .setMessage(dialogString)
//                                    .setCancelable(false)
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                            AlertDialog alertDialog = alertDialogBuilder.create();
//                            alertDialog.show();
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    progressDialog.dismiss();
//                } else {
//                    Log.i("*--Excep : ", exception);
//                    progressDialog.dismiss();
//                }
//            }
//        };
//        allEvents.execute();
//    }
}

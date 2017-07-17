package com.ptplanner.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ptplanner.Khelper.Internet;
import com.ptplanner.Khelper.Internet_Informer;
import com.ptplanner.LandScreenActivity;
import com.ptplanner.R;
import com.ptplanner.adapter.TrainingAdapter;
import com.ptplanner.adapter.TrainingViewPagerAdapter;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.LoginDataType;
import com.ptplanner.datatype.ParticularExerciseDetailsDataType;
import com.ptplanner.datatype.TrainingPerticularExerciseSetsDatatype;
import com.ptplanner.dialog.ShowMorePopUp;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrainingFragment extends Fragment {

    ShowMorePopUp showmorePopup;
    ListView traingList;
    LinearLayout back, more;
    TrainingAdapter trainingAdapter;
    String url;
    View fView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    ProgressBar progTraining, viewpagerPBAR, progFinish;
    ConnectionDetector cd;
    int lentg = 1;
    RelativeLayout rlLeftClick, rlRightClick;
    TitilliumRegular txtLeft, txtRight, txtFinish;
    LinearLayout llFinish;
    TitilliumSemiBold txtExerciseTitle;
    ViewPager viewpagerExcercise;
    ProgressDialog prgDialog;
    ArrayList<ParticularExerciseDetailsDataType> particularExerciseDetailsDataTypeArrayList;
    ParticularExerciseDetailsDataType perParticularExerciseDetailsDataType;
    ArrayList<TrainingPerticularExerciseSetsDatatype> trainingPerticularExerciseSetsDatatypeArrayList;
    TrainingPerticularExerciseSetsDatatype trainingPerticularExerciseSetsDatatype;

    String exception = "", urlResponse = "", exceptionFinish = "", urlResponseFinish = "", statusFinish = "";
    ArrayList<String> exerciseTitleArr;
    ArrayList<String> exerciseIDArr;
    ArrayList<String> userProgramIdArr;

    ArrayList<String> setREPS, setKG;
    TrainingViewPagerAdapter trainingViewPagerAdapter;
    String dialogString;

    int t = 0;

    private LinearLayout dotsLayout;
    private int dotsCount = 0;
    private TextView[] dots;
    int count = 0;

    String user_program_id = "";

    LinearLayout appointmentButton, progressButton;
    RelativeLayout messageButton;

    String saveString;
    SharedPreferences loginPreferences;

    ImageView img_leftarrow;
    String checkValue = "";








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        dialogString = getResources().getString(R.string.frag_training_alertDialog);
//        fView = inflater.inflate(R.layout.frag_training, container, false);





        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        this.fView = inflater.inflate(R.layout.frag_training, container, false);
        /////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////
        appointmentButton = (LinearLayout) getActivity().findViewById(R.id.blockappoinmentbutton);
        progressButton = (LinearLayout) getActivity().findViewById(R.id.progressbutton);
        messageButton = (RelativeLayout) getActivity().findViewById(R.id.messagebutton);
        /////////////////////////////////////////////////////////////////////////////

        fragmentManager = getActivity().getSupportFragmentManager();
        cd = new ConnectionDetector(getActivity());

        traingList = (ListView) fView.findViewById(R.id.list_trn);
        traingList.setDivider(null);
        //traingList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        progTraining = (ProgressBar) fView.findViewById(R.id.prog_training);
        progTraining.setVisibility(View.GONE);
        back = (LinearLayout) fView.findViewById(R.id.back);
        more = (LinearLayout) fView.findViewById(R.id.more);

        rlLeftClick = (RelativeLayout) fView.findViewById(R.id.rl_left_click);
        rlRightClick = (RelativeLayout) fView.findViewById(R.id.rl_right_click);
        txtLeft = (TitilliumRegular) fView.findViewById(R.id.txt_left);
        txtRight = (TitilliumRegular) fView.findViewById(R.id.txt_right);
        txtFinish = (TitilliumRegular) fView.findViewById(R.id.txt_finish);
        llFinish = (LinearLayout) fView.findViewById(R.id.ll_finish);

        txtExerciseTitle = (TitilliumSemiBold) fView.findViewById(R.id.txt_title_exercise);
        viewpagerExcercise = (ViewPager) fView.findViewById(R.id.view_pager_exercise);
        viewpagerPBAR = (ProgressBar) fView.findViewById(R.id.viewpager_pbar);
        viewpagerPBAR.setVisibility(View.GONE);
        progFinish = (ProgressBar) fView.findViewById(R.id.prog_finish);
        progFinish.setVisibility(View.GONE);

        img_leftarrow = (ImageView) fView.findViewById(R.id.img_leftarrow);

        exerciseTitleArr = new ArrayList<String>();
        exerciseIDArr = new ArrayList<String>();
        userProgramIdArr = new ArrayList<String>();





        loginPreferences = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        saveString = loginPreferences.getString("UserId", "");

        AppConfig.loginDatatype = new LoginDataType(
                loginPreferences.getString("UserId", ""),
                loginPreferences.getString("Username", ""),
                loginPreferences.getString("Password", ""));


        try {
            for (int i = 0; i < AppConfig.allExercisesDataTypeArrayList.size(); i++) {
                exerciseTitleArr.add(AppConfig.allExercisesDataTypeArrayList.get(i).getExercise_title());
            }

            for (int j = 0; j < AppConfig.allExercisesDataTypeArrayList.size(); j++) {
                exerciseIDArr.add(AppConfig.allExercisesDataTypeArrayList.get(j).getExercise_id());
            }

            for (int k = 0; k < AppConfig.allExercisesDataTypeArrayList.size(); k++) {
                userProgramIdArr.add(AppConfig.allExercisesDataTypeArrayList.get(k).getUser_program_id());
            }
        }catch (Exception e)
        {
            Toast.makeText(getActivity(),"Error- "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }



        if (cd.isConnectingToInternet()) {
            try {
                try {
                    txtRight.setText(exerciseTitleArr.get(1));
//                    txtExerciseTitle.setText(exerciseTitleArr.get(0));
                    rlRightClick.setVisibility(View.VISIBLE);
                    rlLeftClick.setVisibility(View.GONE);
                    checkValue = "";
                    rlLeftClick.setClickable(true);
                    rlLeftClick.setEnabled(true);
                    img_leftarrow.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    rlRightClick.setVisibility(View.GONE);
                    rlLeftClick.setVisibility(View.VISIBLE);
                    rlLeftClick.setClickable(false);
                    rlLeftClick.setEnabled(false);
                    checkValue = "CHECK";
                    img_leftarrow.setVisibility(View.INVISIBLE);
                }
                getExerCiseDetails(userProgramIdArr.get(0), exerciseIDArr.get(0));

            } catch (Exception e) {

                more.setClickable(false);
                llFinish.setVisibility(View.GONE);

                rlRightClick.setVisibility(View.GONE);
                rlLeftClick.setVisibility(View.GONE);

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
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
        try {

        } catch (Exception e) {
            Log.i("Excep Training : ", "No Data........");
        }

        rlLeftClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if no view has focus:
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if (exerciseTitleArr.size() > 0) {
                    if (t > 0 && t < exerciseTitleArr.size()) {
                        t--;
                        if (t == 0) {
                            rlLeftClick.setVisibility(View.GONE);
                            txtRight.setText(exerciseTitleArr.get(t + 1));
                            rlRightClick.setVisibility(View.VISIBLE);
                        } else if (t == (exerciseTitleArr.size() - 1)) {
                            rlLeftClick.setVisibility(View.VISIBLE);
                            txtLeft.setText(exerciseTitleArr.get(t - 1));
                            rlRightClick.setVisibility(View.GONE);
                        } else {
                            rlLeftClick.setVisibility(View.VISIBLE);
                            txtRight.setText(exerciseTitleArr.get(t + 1));
                            txtLeft.setText(exerciseTitleArr.get(t - 1));
                            rlRightClick.setVisibility(View.VISIBLE);
                        }
                        if (cd.isConnectingToInternet()) {
                            try {
                                getExerCiseDetails(userProgramIdArr.get(t), exerciseIDArr.get(t));
                            } catch (Exception e) {
                                Log.i("Excep Training : ", "No Data........");
                            }
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getActivity(), exerciseIDArr.get(t), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        rlRightClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t < exerciseTitleArr.size()) {
                    t++;
                    if (t == 0) {
                        rlLeftClick.setVisibility(View.GONE);
                        txtRight.setText(exerciseTitleArr.get(t + 1));
                        rlRightClick.setVisibility(View.VISIBLE);
                    } else if (t == (exerciseTitleArr.size() - 1)) {
                        rlLeftClick.setVisibility(View.VISIBLE);
                        txtLeft.setText(exerciseTitleArr.get(t - 1));
                        rlRightClick.setVisibility(View.GONE);
                    } else {
                        rlLeftClick.setVisibility(View.VISIBLE);
                        txtRight.setText(exerciseTitleArr.get(t + 1));
                        txtLeft.setText(exerciseTitleArr.get(t - 1));
                        rlRightClick.setVisibility(View.VISIBLE);
                    }
                    if (cd.isConnectingToInternet()) {
                        try {
                            getExerCiseDetails(userProgramIdArr.get(t), exerciseIDArr.get(t));
                        } catch (Exception e) {
                            Log.i("Excep Training : ", "No Data........");
                        }
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getActivity(), exerciseIDArr.get(t), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewpagerExcercise.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

        more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showmorePopup.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, -20);
                } catch (Exception e) {
                    Log.i("More exception : ", "" + e.toString());
                }
            }
        });

        llFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    finishExercise(user_program_id, perParticularExerciseDetailsDataType.getExercise_id());
                    Log.d("Exercises", user_program_id);

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // Check if no view has focus:
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                Bundle bundle = new Bundle();
                bundle.putString("DateChange", getArguments().getString("DateChange"));

                fragmentTransaction = fragmentManager.beginTransaction();
                CalenderFragment cal_fragment = new CalenderFragment();
                cal_fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
//                int count = fragmentManager.getBackStackEntryCount();
//                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        return fView;
    }



    public void editExcercise(final String userProgramId, final String excerciseId,
                              final int position) {



        AsyncTask<Void, Void, Void> excerciseEdit=  new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();

                setREPS = new ArrayList<String>();
                setKG = new ArrayList<String>();
                for (int i = 0; i < trainingPerticularExerciseSetsDatatypeArrayList.size(); i++) {
                                setKG.add(trainingPerticularExerciseSetsDatatypeArrayList.get(i).getKg());
                            }

                for (int i = 0; i < trainingPerticularExerciseSetsDatatypeArrayList.size(); i++) {
                    setREPS.add(trainingPerticularExerciseSetsDatatypeArrayList.get(i).getREPS());
                }


                prgDialog = new ProgressDialog(getActivity());
                prgDialog.setMessage("uppdatering...");
                prgDialog.setCancelable(false);
                prgDialog.show();


            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionFinish = "";
                    urlResponseFinish = "";
                    statusFinish = "";


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/update_sets_value?user_program_id=" + userProgramId +
                                    "&client_id=" + AppConfig.loginDatatype.getSiteUserId() +
                                    "&exercise_id=" + excerciseId +
                                    "&updated_sets_reps=" + TextUtils.join(",", setREPS) +
                                    "&updated_sets_kg=" + TextUtils.join(",", setKG))
                            .build();


                    Log.d("@@ Koushik URL-",AppConfig.HOST + "app_control/update_sets_value?user_program_id=" + userProgramId +
                            "&client_id=" + AppConfig.loginDatatype.getSiteUserId() +
                            "&exercise_id=" + excerciseId +
                            "&updated_sets_reps=" + TextUtils.join(",", setREPS) +
                            "&updated_sets_kg=" + TextUtils.join(",", setKG));

                    Response response = client.newCall(request).execute();
                    urlResponseFinish = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponseFinish);
                    Log.i("jOBJ",""+jOBJ);

                    statusFinish = jOBJ.getString("response");

//                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exceptionFinish = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                prgDialog.dismiss();
                if (exceptionFinish.equals("")) {
                    if (statusFinish.equals("success")) {
                        trainingPerticularExerciseSetsDatatypeArrayList.get(position).setIsEditable(false);
                        trainingAdapter.notifyDataSetChanged();

//                        Toast.makeText(getActivity(), "Update successfull....", Toast.LENGTH_LONG).show();
                    } else {
//                        Toast.makeText(getActivity(), "Not updated....", Toast.LENGTH_LONG).show();
                    }
                } else {
//                    Toast.makeText(getActivity(), "Exception.."+exceptionFinish, Toast.LENGTH_LONG).show();
                    Log.d("@  Exception Finish ", exceptionFinish);
                }
            }

        };

            excerciseEdit.execute();


    }


    public void finishExercise(final String userProgramId, final String excerciseId) {

        AsyncTask<Void, Void, Void> finishExcercise = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                progFinish.setVisibility(View.VISIBLE);
                llFinish.setClickable(false);
                ////////////////////////////////////////////////////////////////////
                progressButton.setClickable(false);
                progressButton.setEnabled(false);
                appointmentButton.setClickable(false);
                appointmentButton.setEnabled(false);
                messageButton.setClickable(false);
                messageButton.setEnabled(false);
                back.setClickable(false);
                back.setEnabled(false);

                ////////////////////////////////////////////////////////////////////
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionFinish = "";
                    urlResponseFinish = "";
                    statusFinish = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/update_finish_status?user_program_id=" +
                                    userProgramId + "&client_id=" + saveString + "&exercise_id=" + excerciseId)
                            .build();

                    Log.i("@@KOUSHIK","API URL- "+AppConfig.HOST + "app_control/update_finish_status?user_program_id=" +
                            userProgramId + "&client_id=" + saveString + "&exercise_id=" + excerciseId);

                    Response response = client.newCall(request).execute();
                    urlResponseFinish = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponseFinish);
                    Log.i("jOBJ",""+jOBJ);

                    statusFinish = jOBJ.getString("finished");

                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exceptionFinish = e.toString();
                }

                Log.d("URL", AppConfig.HOST + "app_control/update_finish_status?user_program_id=" +
                        userProgramId + "&client_id=" + saveString + "&exercise_id=" + excerciseId);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progFinish.setVisibility(View.GONE);
                llFinish.setClickable(false);
                ////////////////////////////////////////////////////////////////////////////
                progressButton.setClickable(true);
                progressButton.setEnabled(true);
                appointmentButton.setClickable(true);
                appointmentButton.setEnabled(true);
                messageButton.setClickable(true);
                messageButton.setEnabled(true);
                back.setClickable(true);
                back.setEnabled(true);

                ////////////////////////////////////////////////////////////////////////////
                if (exceptionFinish.equals("")) {
                    if (statusFinish.equals("TRUE")) {
                        //if(t==(exerciseTitleArr.size())){

                        if (checkValue.equalsIgnoreCase("CHECK")) {
                            back.performClick();
                        } else {
                            if (t == (exerciseTitleArr.size() - 1)) {
                                // rlLeftClick.performClick();
//                            Intent intent = new Intent(getActivity(), LandScreenActivity.class);
//                            startActivity(intent);
                                back.performClick();
                            } else {
                                rlRightClick.performClick();
                            }
                        }
                        perParticularExerciseDetailsDataType.setFinished("TRUE");
                        count++;

                    }
                } else {
                    Log.d("@  Exception Finish ", exceptionFinish);
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        finishExcercise.execute();

    }

    public void getExerCiseDetails(final String userProgramId, final String excerciseId) {

        AsyncTask<Void, Void, Void> excerciseDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();

                txtExerciseTitle.setText("");

                progTraining.setVisibility(View.GONE);
                traingList.setVisibility(View.GONE);
                rlLeftClick.setClickable(false);
                rlRightClick.setClickable(false);
                viewpagerPBAR.setVisibility(View.VISIBLE);
                viewpagerExcercise.setVisibility(View.GONE);
                llFinish.setVisibility(View.GONE);
                ////////////////////////////////////////////////////////////////////
                progressButton.setClickable(false);
                progressButton.setEnabled(false);
                appointmentButton.setClickable(false);
                appointmentButton.setEnabled(false);
                messageButton.setClickable(false);
                messageButton.setEnabled(false);

                ////////////////////////////////////////////////////////////////////

                user_program_id = userProgramId;
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/get_particular_exercise_details?user_program_id=" +
                                    userProgramId + "&client_id=" + saveString + "&exercise_id=" + excerciseId)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    Log.d("@@ GET TRAINING--",AppConfig.HOST + "app_control/get_particular_exercise_details?user_program_id=" +
                            userProgramId + "&client_id=" + saveString + "&exercise_id=" + excerciseId);
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ",""+jOBJ);

                    particularExerciseDetailsDataTypeArrayList = new ArrayList<ParticularExerciseDetailsDataType>();
                    trainingPerticularExerciseSetsDatatypeArrayList = new ArrayList<TrainingPerticularExerciseSetsDatatype>();
                    try {
                        JSONArray jsonArray = jOBJ.getJSONArray("exercise_sets");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                trainingPerticularExerciseSetsDatatype = new TrainingPerticularExerciseSetsDatatype(
                                        jsonObject.getString("reps"),
                                        jsonObject.getString("kg"),
                                        false
                                );
                                trainingPerticularExerciseSetsDatatypeArrayList.add(trainingPerticularExerciseSetsDatatype);
                            } catch (Exception e) {
                                Log.d("Inner Exception : ", e.toString());
                            }
                        }
                    } catch (Exception exce) {
                        Log.d("exercise_sets tra : ", exce.toString());
                    }
                    perParticularExerciseDetailsDataType = new ParticularExerciseDetailsDataType(
                            jOBJ.getString("exercise_id"),
                            jOBJ.getString("exercise_title"),
                            jOBJ.getString("exercise_description"),
                            jOBJ.getString("exercise_image"),
                            jOBJ.getString("exercise_video"),
                            jOBJ.getString("instruction"),
                            jOBJ.getString("finished"),
                            trainingPerticularExerciseSetsDatatypeArrayList
                    );
                    particularExerciseDetailsDataTypeArrayList.add(perParticularExerciseDetailsDataType);


                } catch (Exception e) {
                    exception = e.toString();
                }
                Log.d("Get Ex RESPONSE", urlResponse);
                Log.d("URL", AppConfig.HOST + "app_control/get_particular_exercise_details?user_program_id=" +
                        userProgramId + "&client_id=" + saveString + "&exercise_id=" + excerciseId);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progTraining.setVisibility(View.GONE);
                viewpagerPBAR.setVisibility(View.GONE);
                ////////////////////////////////////////////////////////////////////////////
                progressButton.setClickable(true);
                progressButton.setEnabled(true);
                appointmentButton.setClickable(true);
                appointmentButton.setEnabled(true);
                messageButton.setClickable(true);
                messageButton.setEnabled(true);

                ////////////////////////////////////////////////////////////////////////////

                if (exception.equals("")) {
                    traingList.setVisibility(View.VISIBLE);
                    viewpagerExcercise.setVisibility(View.VISIBLE);
                    rlRightClick.setClickable(true);
                    llFinish.setVisibility(View.VISIBLE);


                    if (checkValue.equalsIgnoreCase("CHECK")) {
                        txtLeft.setText(perParticularExerciseDetailsDataType.getExercise_title());
                        rlLeftClick.setClickable(false);
                        img_leftarrow.setVisibility(View.INVISIBLE);
                    } else {
                        rlLeftClick.setClickable(true);
                        img_leftarrow.setVisibility(View.VISIBLE);
                    }

                    if (perParticularExerciseDetailsDataType.getFinished().equals("FALSE")) {
                        llFinish.setClickable(true);
                        llFinish.setBackgroundResource(R.drawable.finish_button);
                        txtFinish.setText("Klar med övning");
                        txtFinish.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        llFinish.setClickable(false);
                        llFinish.setBackgroundColor(Color.parseColor("#BDBDBD"));
                        txtFinish.setText("Slutförd övning");
                        txtFinish.setTextColor(Color.parseColor("#000000"));
                    }

                    txtExerciseTitle.setText(perParticularExerciseDetailsDataType.getExercise_title());

                    trainingAdapter = new TrainingAdapter(TrainingFragment.this,getActivity(), 0,
                            particularExerciseDetailsDataTypeArrayList.get(0).getTrainingPerticularExerciseSetsDatatypeArrayList(),
                            perParticularExerciseDetailsDataType.getExercise_id(), userProgramId);
                    traingList.setAdapter(trainingAdapter);

                    trainingViewPagerAdapter = new TrainingViewPagerAdapter(getActivity(), 0,
                            perParticularExerciseDetailsDataType.getExercise_image(),
                            perParticularExerciseDetailsDataType.getExercise_video());
                    viewpagerExcercise.setAdapter(trainingViewPagerAdapter);

                    if (lentg == 1) {
                        setViewPagerItemsWithAdapter();
                        setUiPageViewController(2);
                    } else {
                        for (int i = 0; i < dotsCount; i++) {
                            dots[i].setTextColor(Color.parseColor("#BDBDBD"));
                        }
                        dots[0].setTextColor(Color.parseColor("#22A7F0"));

                    }
                    lentg++;
                    showmorePopup = new ShowMorePopUp(getActivity(),
                            perParticularExerciseDetailsDataType.getExercise_title(),
                            perParticularExerciseDetailsDataType.getExercise_description(),
                            perParticularExerciseDetailsDataType.getInstruction());

                    try {
                        txtExerciseTitle.setText(perParticularExerciseDetailsDataType.getExercise_title());
                    } catch (Exception ee) {
                        txtExerciseTitle.setText("");
                    }

                } else {
                    Log.d("@  Exception ", exception);
                    // Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        excerciseDetails.execute();

    }

    private void setUiPageViewController(int ii) {
        try{
            dotsLayout = (LinearLayout) getActivity().findViewById(R.id.viewPagerCountDots);
            dotsCount = ii;
            dots = new TextView[ii];


            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new TextView(getActivity());
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(30);
                dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
                dotsLayout.addView(dots[i]);
            }
            dots[0].setTextColor(Color.parseColor("#22A7F0"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void setViewPagerItemsWithAdapter() {

        viewpagerExcercise.setCurrentItem(0);
        viewpagerExcercise.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setTextColor(Color.parseColor("#BDBDBD"));
            }
            dots[position].setTextColor(Color.parseColor("#22A7F0"));
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

}

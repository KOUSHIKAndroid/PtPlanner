package com.ptplanner;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.ptplanner.customgraphview.LineView;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.GraphDetailsDataType;
import com.ptplanner.datatype.GraphDetailsPointDataType;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProgressGraphView extends FragmentActivity {

    //WebView webView;

    LineView lineView;

    LinearLayout back;
    LinearLayout add_measurement;
    TitilliumSemiBold parentWeight, parentDeadline;
    TitilliumRegular txtWeightMesure;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    String exceptionGraphDetails = "", getUrlResponseGraphDetails = "", exception = "", responseUpdate = "";
    ConnectionDetector cd;
    ProgressBar graphBar;

    ArrayList<GraphDetailsDataType> graphDetailsDataTypeArrayList;
    GraphDetailsDataType graphDetailsDataType;

    ArrayList<GraphDetailsPointDataType> graphDetailsPointDataTypeArrayList;
    GraphDetailsPointDataType graphDetailsPointDataType;

    ArrayList<String> arrayListXAxispoint, arrayListYAxisPoint;

    Dialog dialogMeasurement;
    RelativeLayout rlBack;
    LinearLayout llDone, llMinus, llPlus;
    TitilliumSemiBold txtMeasurement;
    DatePicker pickerDate;
    int mYear, mMonth, mDay;

    HorizontalScrollView horizontalScrollView;

    int measurementCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_graph_view);
        /////////////////////////////////////////////////////

        setContentView(R.layout.activity_graph_view);

        back = (LinearLayout) findViewById(R.id.back);
        add_measurement = (LinearLayout) findViewById(R.id.add_measurement);
        parentDeadline = (TitilliumSemiBold) findViewById(R.id.deadline);
        parentWeight = (TitilliumSemiBold) findViewById(R.id.weight);
        txtWeightMesure = (TitilliumRegular) findViewById(R.id.txt_wt_mesure);

        lineView = (LineView) findViewById(R.id.line_view);
        graphBar = (ProgressBar) findViewById(R.id.graph_pbar);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);


        horizontalScrollView.setVisibility(View.GONE);
        graphBar.setVisibility(View.VISIBLE);

        fragmentManager = getSupportFragmentManager();

        cd = new ConnectionDetector(ProgressGraphView.this);

        dialogMeasurement = new Dialog(ProgressGraphView.this);
        dialogMeasurement.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMeasurement.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogMeasurement.getWindow().setGravity(Gravity.CENTER);
        dialogMeasurement.setContentView(R.layout.measurement_dialog);
        dialogMeasurement.setCanceledOnTouchOutside(true);

        rlBack = (RelativeLayout) dialogMeasurement.findViewById(R.id.ll_cancel);
        llDone = (LinearLayout) dialogMeasurement.findViewById(R.id.ll_done);
        llMinus = (LinearLayout) dialogMeasurement.findViewById(R.id.ll_minus);
        llPlus = (LinearLayout) dialogMeasurement.findViewById(R.id.ll_plus);
        txtMeasurement = (TitilliumSemiBold) dialogMeasurement.findViewById(R.id.txt_measurement);
        pickerDate = (DatePicker) dialogMeasurement.findViewById(R.id.picker_date);

        ViewTreeObserver viewTreeObserver = horizontalScrollView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            horizontalScrollView.setVisibility(View.VISIBLE);
                            graphBar.setVisibility(View.GONE);
                            horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    });
        }

        llPlus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                measurementCount = Integer.parseInt(txtMeasurement.getText().toString());
                ++measurementCount;
                txtMeasurement.setText("" + measurementCount);
//                txtMeasurement.setText(""
//                                + (Integer.parseInt(txtMeasurement.getText().toString()) + 1)
//                );
            }
        });

        llMinus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if ((Integer.parseInt(txtMeasurement.getText().toString()) - 1) > 0) {
                    measurementCount = Integer.parseInt(txtMeasurement.getText().toString());
                    --measurementCount;
                    txtMeasurement.setText("" + measurementCount);
//                    txtMeasurement.setText(""
//                                    + (Integer.parseInt(txtMeasurement.getText().toString()) - 1)
//                    );
                }

            }
        });

        rlBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMeasurement.cancel();
            }
        });

        llDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMeasurement.cancel();

                mDay = pickerDate.getDayOfMonth();
                mMonth = pickerDate.getMonth() + 1;
                mYear = pickerDate.getYear();

                String stringDate = mYear + "-" + mMonth + "-" + mDay;

                if (cd.isConnectingToInternet()) {
//                    updateMeasurement(
//                            getIntent().getExtras().getString("GRAPH_ID"),
//                            stringDate,
//                            txtMeasurement.getText().toString()
//                    );
                    updateMeasurement(
                            getIntent().getExtras().getString("GRAPH_ID"),
                            stringDate,
                            "" + measurementCount
                    );
                } else {
                    Toast.makeText(ProgressGraphView.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }

            }
        });

        if (cd.isConnectingToInternet()) {
            getGraphDetailsById(getIntent().getExtras().getString("GRAPH_ID"));
        } else {
            Toast.makeText(ProgressGraphView.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                finish();
            }
        });

        add_measurement.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialogMeasurement.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    public void updateMeasurement(final String graphID, final String date, final String measurement) {

        AsyncTask<Void, Void, Void> measurementUpdate = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    responseUpdate = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/add_measurement?graph_id=" + graphID + "&date=" + date + "&measurement=" + measurement)
                            .build();

                    Response response = client.newCall(request).execute();
                    responseUpdate = response.body().string();
                    JSONObject jOBJ = new JSONObject(responseUpdate);

                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }
                Log.d("add_measurement_URL", AppConfig.HOST + "app_control/add_measurement?graph_id=" + graphID + "&date=" + date + "&measurement=" + measurement);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (exception.equals("")) {
                    getGraphDetailsById(graphID);
                } else {
                    Log.d("Exception : ", exception);
                    //  Toast.makeText(ProgressGraphView.this, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        measurementUpdate.execute();
    }


    public void getGraphDetailsById(final String graphID) {

        AsyncTask<Void, Void, Void> graphDetailsById = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionGraphDetails = "";
                    getUrlResponseGraphDetails = "";


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/graph_details?graph_id=" + graphID)
                            .build();

                    Response response = client.newCall(request).execute();
                    getUrlResponseGraphDetails = response.body().string();
                    JSONObject jOBJ = new JSONObject(getUrlResponseGraphDetails);
                    Log.i("jOBJ",""+jOBJ);

                    Log.i("graph_details_Url",""+AppConfig.HOST + "app_control/graph_details?graph_id=" + graphID);


                    graphDetailsDataTypeArrayList = new ArrayList<GraphDetailsDataType>();
                    graphDetailsPointDataTypeArrayList = new ArrayList<GraphDetailsPointDataType>();

                    JSONArray jArr = jOBJ.getJSONArray("points");
                    for (int i = 0; i < jArr.length(); i++) {
                        JSONObject jsonObject = jArr.getJSONObject(i);

                        graphDetailsPointDataType = new GraphDetailsPointDataType(
                                jsonObject.getString("x_axis_point"),
                                jsonObject.getString("y_axis_point")
                        );
                        graphDetailsPointDataTypeArrayList.add(graphDetailsPointDataType);
                    }

                    graphDetailsDataType = new GraphDetailsDataType(
                            jOBJ.getString("client_name"),
                            jOBJ.getString("graph_type"),
                            jOBJ.getString("graph_for"),
                            jOBJ.getString("measure_unit"),
                            jOBJ.getString("goal"),
                            jOBJ.getString("deadline"),
                            graphDetailsPointDataTypeArrayList
                    );
                    graphDetailsDataTypeArrayList.add(graphDetailsDataType);

                } catch (Exception e) {
                    exceptionGraphDetails = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (exceptionGraphDetails.equals("")) {

                    ArrayList<String> test = new ArrayList<String>();
                    for (int i = 0; i < graphDetailsPointDataTypeArrayList.size(); i++) {
                        test.add(
                                changeDateFormat(
                                        graphDetailsPointDataTypeArrayList.get(i).getX_axis_point()
                                )
                        );
                    }

                    lineView.setBottomTextList(test);
                    lineView.setDrawDotLine(true);
                    lineView.setScrollbarFadingEnabled(true);
                    lineView.setShowPopup(LineView.SHOW_POPUPS_All);

                    randomSet(lineView, graphDetailsPointDataTypeArrayList);

                    try {
                        DateFormat originalFormatToday = new SimpleDateFormat("yyyy/dd/MM");
                        DateFormat targetFormatToday = new SimpleDateFormat("yyyy-MM-dd");
                        Date todayDate = null;
                        try {
                            todayDate = originalFormatToday.parse(
                                    graphDetailsDataTypeArrayList.get(0).getGraphDetailsPointDataTypes().get(0).getX_axis_point()
                            );
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        parentDeadline.setText(targetFormatToday.format(todayDate));
                    } catch (Exception e) {
                        Log.i("Tilbaka : ", " Dead : " + e.toString());
                    }

                    try {
                        String[] weightParent = graphDetailsDataType.getGoal().split("\\.");
                        parentWeight.setText(
                                weightParent[0]
                        );
                    } catch (Exception e) {
                        try {
                            parentWeight.setText(
                                    graphDetailsDataType.getGoal()
                            );
                        } catch (Exception ex) {

                        }
                    }

                    try {
                        String[] weightParent = graphDetailsDataTypeArrayList
                                .get(0)
                                .getGraphDetailsPointDataTypes()
                                .get(graphDetailsPointDataTypeArrayList.size() - 1).getY_axis_point()
                                .split("\\.");
                        Log.i("CHECK : ", weightParent[0]);
                        txtMeasurement.setText("" + weightParent[0]);
                    } catch (Exception e) {
                        try {
                            txtMeasurement.setText(""
                                    + graphDetailsDataTypeArrayList
                                    .get(0)
                                    .getGraphDetailsPointDataTypes()
                                    .get(graphDetailsPointDataTypeArrayList.size() - 1).getY_axis_point());
                            Log.i("CHECK : ", ""
                                    + graphDetailsDataTypeArrayList
                                    .get(0)
                                    .getGraphDetailsPointDataTypes()
                                    .get(graphDetailsPointDataTypeArrayList.size() - 1).getY_axis_point());
                        } catch (Exception ex) {
                            Log.i("CHECK : ", "HOYNI");
                        }
                    }

                    txtWeightMesure.setText("" + graphDetailsDataType.getMeasure_unit().toLowerCase());

                } else {
                    Log.d("Exception : ", exceptionGraphDetails);
                    //  Toast.makeText(ProgressGraphView.this, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        graphDetailsById.execute();

    }

    private void randomSet(LineView lineView, ArrayList<GraphDetailsPointDataType> clientAllGraphPointDataTypeArrayList) {
        ArrayList<Integer> dataList = new ArrayList<Integer>();
        for (int i = 0; i < clientAllGraphPointDataTypeArrayList.size(); i++) {
            dataList.add(
                    (int) Float.parseFloat(clientAllGraphPointDataTypeArrayList.get(i).getY_axis_point())
            );
        }

        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
        dataLists.add(dataList);

        lineView.setDataList(dataLists);
    }

    public String changeDateFormat(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/dd/MM");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM");
        String finalDate = timeFormat.format(myDate);

        return finalDate;
    }

}
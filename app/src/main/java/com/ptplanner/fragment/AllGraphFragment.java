package com.ptplanner.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.ptplanner.R;
import com.ptplanner.adapter.AllGraphAdapter;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.ClientAllGraphDataType;
import com.ptplanner.datatype.ClientAllGraphPointDataType;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ltp on 05/08/15.
 */
public class AllGraphFragment extends Fragment {

    View fView;
    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton, llGrphdetailsList;
    RelativeLayout llMessagebutton;

    ConnectionDetector connectionDetector;

    LinearLayout back;
    ListView listAllGraph;
    ProgressBar pBar, pBarGraph;
    TitilliumSemiBold txtError;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    ClientAllGraphDataType clientAllGraphDataType;
    ArrayList<ClientAllGraphDataType> clientAllGraphDataTypeArrayList;

    ClientAllGraphPointDataType clientAllGraphPointDataType;
    ArrayList<ClientAllGraphPointDataType> clientAllGraphPointDataTypeArrayList;

    String exceptionGraphDetails = "", getUrlResponseGraphDetails = "";

    LayoutInflater inflatorLayout;

    String saveString;
    SharedPreferences userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fView = inflater.inflate(R.layout.frag_allgraph, container, false);

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        this.fView = inflater.inflate(R.layout.frag_allgraph, container, false);
        /////////////////////////////////////////////////////

        connectionDetector = new ConnectionDetector(getActivity());
        fragmentManager = getActivity().getSupportFragmentManager();

        inflatorLayout = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        back = (LinearLayout) fView.findViewById(R.id.back);
        listAllGraph = (ListView) fView.findViewById(R.id.list_allgraph);
        txtError = (TitilliumSemiBold) fView.findViewById(R.id.txt_error);
        txtError.setVisibility(View.GONE);
        pBarGraph = (ProgressBar) fView.findViewById(R.id.progbar);
        pBarGraph.setVisibility(View.GONE);

        userId = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        saveString = userId.getString("UserId", "");


        if (connectionDetector.isConnectingToInternet()) {
            // getGraphDetailsById(saveString);
            getGraphDetailsById(saveString);
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                fragmentTransaction = fragmentManager.beginTransaction();
                ProgressFragment progressFragment = new ProgressFragment();
                fragmentTransaction.replace(R.id.fragment_container, progressFragment);
                fragmentTransaction.commit();
            }
        });

        llCalenderButton = (LinearLayout) getActivity().findViewById(R.id.calenderbutton);
        llBlockAppoinmentButton = (LinearLayout) getActivity().findViewById(R.id.blockappoinmentbutton);
        llProgressButton = (LinearLayout) getActivity().findViewById(R.id.progressbutton);
        llMessagebutton = (RelativeLayout) getActivity().findViewById(R.id.messagebutton);
        llCalenderButton.setClickable(true);
        llBlockAppoinmentButton.setClickable(true);
        llProgressButton.setClickable(false);
        llMessagebutton.setClickable(true);

        return fView;
    }

    public void getGraphDetailsById(final String clientId) {

        AsyncTask<Void, Void, Void> graphDetailsById = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                listAllGraph.setVisibility(View.GONE);
                txtError.setVisibility(View.GONE);
                pBarGraph.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionGraphDetails = "";
                    getUrlResponseGraphDetails = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/all_graphs?client_id=" + clientId)
                            .build();

                    Response response = client.newCall(request).execute();
                    getUrlResponseGraphDetails = response.body().string();
                    JSONObject jOBJ = new JSONObject(getUrlResponseGraphDetails);
                    Log.i("jOBJ",""+jOBJ);

                    clientAllGraphDataTypeArrayList = new ArrayList<ClientAllGraphDataType>();

                    JSONArray jArr = jOBJ.getJSONArray("all_graphs");
                    for (int i = 0; i < jArr.length(); i++) {

                        clientAllGraphPointDataTypeArrayList = new ArrayList<ClientAllGraphPointDataType>();
                        JSONObject jsonObject = jArr.getJSONObject(i);
                        JSONArray jsonArray = jsonObject.getJSONArray("points");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonOBJ = jsonArray.getJSONObject(j);
                            clientAllGraphPointDataType = new ClientAllGraphPointDataType(
                                    jsonOBJ.getString("x_axis_point"),
                                    jsonOBJ.getString("y_axis_point")
                            );
                            clientAllGraphPointDataTypeArrayList.add(clientAllGraphPointDataType);
                        }
                        clientAllGraphDataType = new ClientAllGraphDataType(
                                jsonObject.getString("id"),
                                jsonObject.getString("graph_type"),
                                jsonObject.getString("graph_for"),
                                jsonObject.getString("measure_unit"),
                                jsonObject.getString("goal"),
                                jsonObject.getString("deadline"),
                                clientAllGraphPointDataTypeArrayList
                        );
                        clientAllGraphDataTypeArrayList.add(clientAllGraphDataType);
                    }

                } catch (Exception e) {
                    exceptionGraphDetails = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pBarGraph.setVisibility(View.GONE);
                if (exceptionGraphDetails.equals("")) {

                    listAllGraph.setVisibility(View.VISIBLE);
                    AllGraphAdapter allGraphAdapter = new AllGraphAdapter(
                            getActivity(),
                            0,
                            clientAllGraphDataTypeArrayList
                    );
                    listAllGraph.setAdapter(allGraphAdapter);
                } else {
                    txtError.setVisibility(View.VISIBLE);
                    Log.d("Exception : ", exceptionGraphDetails);
                    // Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        graphDetailsById.execute();

    }
}

package com.ptplanner.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ptplanner.R;
import com.ptplanner.customviews.TitilliumBold;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.DietDetailDataType;
import com.ptplanner.datatype.SetDataType;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import com.ptplanner.helper.ObservableScrollView;
import com.ptplanner.helper.ScrollViewListener;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DietListDetailsFragment extends Fragment implements ScrollViewListener {

    LinearLayout backDiet;
    ObservableScrollView mainS;
    ScrollView child;
    DietDetailDataType dietDetailDataType;
    ArrayList<SetDataType> setDataTypeArrayList;
    View fView;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    TitilliumBold title;
    TitilliumSemiBold txtTitle;
    TitilliumRegular description, details_sets, instruction;
    ImageView mealimage;
    LinearLayout llContainer;
    ProgressBar pBar;

    ConnectionDetector cd;
    String exception = "", urlResponse = "";

    String saveString;
    SharedPreferences userId;
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static DietListDetailsFragment newInstance(String param1, String param2) {
        DietListDetailsFragment fragment = new DietListDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Log.d("@@@@ AKA ",mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.frag_dietlist_details, container, false);


        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        this.fView = inflater.inflate(R.layout.frag_dietlist_details, container, false);
        /////////////////////////////////////////////////////

        title = (TitilliumBold) fView.findViewById(R.id.details_title);
        description = (TitilliumRegular) fView.findViewById(R.id.details_desc);
        mealimage = (ImageView) fView.findViewById(R.id.meal_image);
        llContainer = (LinearLayout) fView.findViewById(R.id.container);
        pBar = (ProgressBar) fView.findViewById(R.id.pbar);
        backDiet = (LinearLayout) fView.findViewById(R.id.back_diet);
        txtTitle = (TitilliumSemiBold) fView.findViewById(R.id.txt_title);
        details_sets = (TitilliumRegular) fView.findViewById(R.id.details_sets);
        instruction = (TitilliumRegular) fView.findViewById(R.id.instruction);

        fragmentManager = getActivity().getSupportFragmentManager();
        cd = new ConnectionDetector(getActivity());
        userId = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        saveString = userId.getString("UserId", "");

        if (cd.isConnectingToInternet()) {
            getDietDetails(mParam1);
        } else {
            getDietDetails(mParam1);
//            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }

        backDiet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Bundle bundle = new Bundle();
                bundle.putString("DateChange", getArguments().getString("DateChange"));

                fragmentTransaction = fragmentManager.beginTransaction();
                DietFragment diet_fragment = new DietFragment();
                diet_fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, diet_fragment);
//                int count = fragmentManager.getBackStackEntryCount();
//                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        return fView;
    }

    public void getDietDetails(final String mParam1) {

        AsyncTask<Void, Void, Void> dietListDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
                urlResponse=mParam1;
                llContainer.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
//
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder()
//                            .url(AppConfig.HOST + "app_control/get_custom_meal_details?custom_meal_id=" + getArguments().getString("CustomMealID")
//                                    + "&client_id=" + saveString
//                                    + "&meal_id=" + getArguments().getString("MealID"))
//                            .build();
//
//                    Response response = client.newCall(request).execute();
//                    urlResponse = response.body().string();



                    setDataTypeArrayList = new ArrayList<SetDataType>();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ",""+jOBJ);
                    JSONArray jArr = jOBJ.getJSONArray("set");
                    for (int s = 0; s < jArr.length(); s++) {
                        JSONObject obj = jArr.getJSONObject(s);
                        SetDataType setDataType = new SetDataType(
                                obj.getString("specifically"),
                                obj.getString("amount")
                        );
                        setDataTypeArrayList.add(setDataType);
                    }
                    dietDetailDataType = new DietDetailDataType(
                            jOBJ.getString("meal_title"),
                            jOBJ.getString("meal_image"),
                            jOBJ.getString("meal_description"),
                            setDataTypeArrayList,
                            jOBJ.getString("instruction")
                    );


                    Log.d("Diet URL : ", AppConfig.HOST + "app_control/get_custom_meal_details?custom_meal_id=" + getArguments().getString("CustomMealID")
                            + "&client_id=" + saveString
                            + "&meal_id=" + getArguments().getString("MealID"));

                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pBar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    llContainer.setVisibility(View.VISIBLE);

                    txtTitle.setText(dietDetailDataType.getMeal_title());
                    title.setText(dietDetailDataType.getMeal_title());
                    description.setText(dietDetailDataType.getMeal_description());
                    Glide.with(getActivity())
                            .load(dietDetailDataType.getMeal_image())
                            .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                            .fitCenter()
                            .into(mealimage);

                    try {
                        String mealSets = "";
                        for (int c = 0; c < dietDetailDataType.getSetDataTypeArrayList().size(); c++) {
                            mealSets = mealSets
                                    + dietDetailDataType.getSetDataTypeArrayList().get(c).getSpecifically()
                                    + " - "
                                    + dietDetailDataType.getSetDataTypeArrayList().get(c).getAmount()
                                    + "\n";
                            details_sets.setText("" + mealSets);
                        }
                    } catch (Exception e) {
                        details_sets.setText(""
                                        + dietDetailDataType.getSetDataTypeArrayList().get(0).getSpecifically()
                                        + " - "
                                        + dietDetailDataType.getSetDataTypeArrayList().get(0).getAmount()
                        );
                    }
                    instruction.setText("" + dietDetailDataType.getInstruction());

                } else {
                    // Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        dietListDetails.execute();

    }


    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (oldx > x) {

        } else {
            // System.out.println("Scroll value : " + y);
            if (y > 448) {
            } else {
            }
            child.scrollTo(x, y / 2);
            // header.setAlpha((float) y / 1200);
        }
    }


}

package com.happywannyan.Activities.profile.profilepagerFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.happywannyan.Activities.profile.ProfileDetails;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileFragAbout extends Fragment {
    JSONObject jsonObject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            jsonObject=new JSONObject(((ProfileDetails)getActivity()).JSONRESPONSE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        try {
            LinearLayout LLSkils=(LinearLayout)view.findViewById(R.id.LLSkils);
            LinearLayout LLSpclAcco=(LinearLayout)view.findViewById(R.id.LLSpclAcco);
            LinearLayout LLHightLisgt=(LinearLayout)view.findViewById(R.id.LLHightLisgt);
            JSONArray Skills=jsonObject.getJSONObject("info_array").getJSONArray("special_skills");
            JSONArray SPCLAcco=jsonObject.getJSONObject("info_array").getJSONArray("spe_accommodation");
            JSONArray hightlight_feature=jsonObject.getJSONObject("info_array").getJSONArray("hightlight_feature");

            for(int i=0;i<Skills.length();i++){
                SFNFTextView view1=new SFNFTextView(getActivity());
                view1.setPadding(0,30,0,30);
                view1.setText(Skills.getString(i));
                LLSkils.addView(view1);
            }

            for(int i=0;i<SPCLAcco.length();i++){
                SFNFTextView view1=new SFNFTextView(getActivity());
                view1.setPadding(0,30,0,30);
                view1.setText(SPCLAcco.getString(i));
                LLSpclAcco.addView(view1);
            }

            for(int i=0;i<hightlight_feature.length();i++){
                SFNFTextView view1=new SFNFTextView(getActivity());
                view1.setPadding(0,30,0,30);
                view1.setText(hightlight_feature.getString(i));
                LLHightLisgt.addView(view1);
            }
            ((SFNFTextView)view.findViewById(R.id.Description)).setText(jsonObject.getJSONObject("info_array").getJSONObject("about_info").getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

package com.happywannyan.SitterBooking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.happywannyan.Activities.CalenderActivity;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookingFragmnetOne extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SFNFTextView TXT_ServiceName,TXT_StartDate,TXT_EndDte,TXT_ExtarItem;
    SFNFBoldTextView TXT_Price;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    JSONArray ExtraPopup;

    LinearLayout LL_S_F;
    RelativeLayout RL_SingleDate,RL_ExtraDropDown;
    private OnFragmentInteractionListener mListener;
    private static final int CALL_CALENDER = 12;
    public BookingFragmnetOne() {
    }

    public static BookingFragmnetOne newInstance(String param1, String param2) {
        BookingFragmnetOne fragment = new BookingFragmnetOne();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_fragmnet_one, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.Card_next).setOnClickListener(this);
        view.findViewById(R.id.RL_ChoseCat).setOnClickListener(this);
        view.findViewById(R.id.LL_S_F).setOnClickListener(this);
        view.findViewById(R.id.RL_ExtraDropDown).setOnClickListener(this);
        TXT_ServiceName=(SFNFTextView)view.findViewById(R.id.TXT_ServiceName);
        TXT_StartDate=(SFNFTextView)view.findViewById(R.id.TXT_StartDate);
        TXT_EndDte=(SFNFTextView)view.findViewById(R.id.TXT_EndDte);
        TXT_ExtarItem=(SFNFTextView)view.findViewById(R.id.TXT_ExtarItem);
        TXT_Price=(SFNFBoldTextView)view.findViewById(R.id.TXT_Price);
        LL_S_F=(LinearLayout)view.findViewById(R.id.LL_S_F);
        RL_SingleDate=(RelativeLayout)view.findViewById(R.id.RL_SingleDate);
        RL_ExtraDropDown=(RelativeLayout)view.findViewById(R.id.RL_ExtraDropDown);
        RL_SingleDate.setVisibility(View.GONE);
        RL_ExtraDropDown.setVisibility(View.GONE);
        LL_S_F.setVisibility(View.GONE);

        if(!mParam2.equals("NA"))
        {
            try {
                JSONObject jsonObject=new JSONObject(mParam2);
                TXT_ServiceName.setText(jsonObject.getString("service_name"));
                RL_SingleDate.setVisibility(View.GONE);
                LL_S_F.setVisibility(View.GONE);
                TXT_Price.setText(jsonObject.getString("service_price")+" / "+jsonObject.getString("unit_name"));
                if(jsonObject.getString("date_field").equals("double"))
                {
                    LL_S_F.setVisibility(View.VISIBLE);
                }else {
                    RL_SingleDate.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri.getFragment());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CALL_CALENDER:
               String StartDate = data.getStringExtra("startdate");
              String  EndDate = data.getStringExtra("enddate");
                TXT_StartDate.setText(StartDate);
                TXT_EndDte.setText(EndDate);


                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick( View view) {
        switch (view.getId()){
            case R.id.Card_next:
                mListener.onFragmentInteraction("Two");
                break;
            case R.id.RL_ChoseCat:
                if(mParam2.equals("NA"))
                try {
                    new MYAlert(getActivity()).AlertTextLsit("" + getString(R.string.ChooseService), new JSONArray(mParam1), "service_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                TXT_ServiceName.setText(jsonObject.getString("service_name"));
                                RL_SingleDate.setVisibility(View.GONE);
                                RL_ExtraDropDown.setVisibility(View.GONE);
                                LL_S_F.setVisibility(View.GONE);
                                TXT_Price.setText(jsonObject.getString("service_price")+" / "+jsonObject.getString("unit_name"));
                                if(jsonObject.getString("date_field").equals("double"))
                                {
                                    LL_S_F.setVisibility(View.VISIBLE);
                                }else {
                                    RL_SingleDate.setVisibility(View.VISIBLE);
                                }

                                if(jsonObject.getString("no_of_times").equals("1"))
                                {
                                    RL_ExtraDropDown.setVisibility(View.VISIBLE);
                                    ExtraPopup=jsonObject.getJSONArray("no_of_times_dropdown");
                                    TXT_ExtarItem.setText(ExtraPopup.getJSONObject(0).getString("name"));
                                    TXT_ExtarItem.setTag(ExtraPopup.getJSONObject(0).getString("value"));
                                }
                                else {
                                    RL_ExtraDropDown.setVisibility(View.GONE);
                                    ExtraPopup=null;
                                }

                                if(jsonObject.getString("no_of_visit").equals("1"))
                                {
                                    RL_ExtraDropDown.setVisibility(View.VISIBLE);
                                    ExtraPopup=jsonObject.getJSONArray("no_of_visit_dropdown");
                                    TXT_ExtarItem.setText(ExtraPopup.getJSONObject(0).getString("name"));
                                    TXT_ExtarItem.setTag(ExtraPopup.getJSONObject(0).getString("value"));
                                }
                                else {
                                    RL_ExtraDropDown.setVisibility(View.GONE);
                                    ExtraPopup=null;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.LL_S_F:
                    startActivityForResult(new Intent(getActivity(), CalenderActivity.class), CALL_CALENDER);
                break;

            case R.id.RL_ExtraDropDown:
                new MYAlert(getActivity()).AlertTextLsit("", ExtraPopup, "name", new MYAlert.OnSignleListTextSelected() {
                    @Override
                    public void OnSelectedTEXT(JSONObject jsonObject) {
                        try {
                            TXT_ExtarItem.setText(jsonObject.getString("name"));
                            TXT_ExtarItem.setTag(jsonObject.getString("value"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

        }

    }

}

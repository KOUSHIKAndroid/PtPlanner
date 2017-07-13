package com.happywannyan.SitterBooking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.R;
import com.happywannyan.Utils.App_data_holder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BookingFragmentTwo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout LL_MYPETS;

    private OnFragmentInteractionListener mListener;

    public BookingFragmentTwo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookingFragmentTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static BookingFragmentTwo newInstance(String param1, String param2) {
        BookingFragmentTwo fragment = new BookingFragmentTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppContsnat(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.d("@@ PARA<MS",mParam1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.Card_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction("Three");
            }
        });

        new AppContsnat(getActivity()).GET_SHAREDATA(App_data_holder.UserData, new App_data_holder.App_sharePrefData() {
            @Override
            public void Avialable(boolean avilavle, JSONObject data) {
                try {
                    ((EditText)view.findViewById(R.id.EDX_Fname)).setText(data.getJSONObject("info_array").getString("firstname"));
                    ((EditText)view.findViewById(R.id.EDX_Lname)).setText(data.getJSONObject("info_array").getString("lastname"));

                } catch (JSONException e) {

                }
            }

            @Override
            public void NotAvilable(String Error) {



            }
        });

        LL_MYPETS=(LinearLayout)view.findViewById(R.id.LL_MYPETS);

        
        SetPetList();

    }

    private void SetPetList() {
        try {
            JSONObject MainJ=new JSONObject(mParam1).getJSONObject("info_array");
            JSONArray Array=MainJ.getJSONArray("pet_section");
            for(int i=0;i<Array.length();i++)
            {
                CheckBox chk=new CheckBox(getActivity());
                chk.setText(Array.getJSONObject(i).getString("name"));
                chk.setTag(Array.getJSONObject(i).getString("id"));
                LL_MYPETS.addView(chk);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_fragment_two, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri.getFragment());
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


}

package com.happywannyan.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Adapter.YourPets_Adapter;
import com.happywannyan.POJO.YourPets;
import com.happywannyan.R;

import java.util.ArrayList;

public class MyPets_Fragments extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;

    YourPets_Adapter yourPets_adapter;
    ArrayList<YourPets> ListPets;

    private OnFragmentInteractionListener mListener;

    public MyPets_Fragments() {
        // Required empty public constructor
    }

    public static MyPets_Fragments newInstance(String param1, String param2) {
        MyPets_Fragments fragment = new MyPets_Fragments();
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
        return inflater.inflate(R.layout.fragment_my_pets, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ListPets=new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            YourPets yourPets=new YourPets();
            yourPets.setName("Name"+i);
            yourPets.setFriendly(true);
            yourPets.setMonth("june");
            yourPets.setImg("http://s7d2.scene7.com/is/image/PetSmart/PB0101_HERO-Dog-GroomingSupplies-20160818?$sclp-banner-main_small$");
            ListPets.add(yourPets);
        }
        yourPets_adapter=new YourPets_Adapter(getActivity(),ListPets);
        recyclerView.setAdapter(yourPets_adapter);




        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).Menu_Drawer();
            }
        });

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

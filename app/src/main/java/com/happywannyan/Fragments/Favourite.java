package com.happywannyan.Fragments;

import android.content.Context;
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
import com.happywannyan.Adapter.FavouriteRecyclerAdapter;
import com.happywannyan.POJO.SetGetFavourite;
import com.happywannyan.R;

import java.util.ArrayList;

public class Favourite extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView rcv_favourite;
    ArrayList<SetGetFavourite> favouriteArrayList;
    FavouriteRecyclerAdapter favouriteRecyclerAdapter;
    public Favourite() {
        // Required empty public constructor
    }

    public static Favourite newInstance(String param1, String param2) {
        Favourite fragment = new Favourite();
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
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity)getActivity()).Menu_Drawer();
            }
        });

        rcv_favourite= (RecyclerView) view.findViewById(R.id.recycler_view);
        rcv_favourite.setLayoutManager(new LinearLayoutManager(getActivity()));

        favouriteArrayList=new ArrayList<>();

        for(int i=0;i<20;i++){

            SetGetFavourite setGetFavourite=new SetGetFavourite();
            setGetFavourite.setCheckRightValue(false);
            setGetFavourite.setName("John Doe");
            setGetFavourite.setImg("http://www.wa11papers.com/assets/thumbnails/nature-landscape-trees-clouds-grass-forest-fog-hd%20wallpaper-meadow-foggy-wallpaper-8038-thumbnail-5d61d18a.jpg");
            setGetFavourite.setAddress("Tuoh,Ciba,Japan");
            setGetFavourite.setReservation("Reservation");
            setGetFavourite.setContact("Contact");
            setGetFavourite.setMeet_up("Wan-Nyan meet up");

            favouriteArrayList.add(setGetFavourite);

        }


        favouriteRecyclerAdapter=new FavouriteRecyclerAdapter(getActivity(),favouriteArrayList);
        rcv_favourite.setAdapter(favouriteRecyclerAdapter);
    }
}

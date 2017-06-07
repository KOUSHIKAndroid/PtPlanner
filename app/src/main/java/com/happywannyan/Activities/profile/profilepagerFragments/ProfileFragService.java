package com.happywannyan.Activities.profile.profilepagerFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Activities.profile.fragmentPagerAdapter.ProfileServiceListingAdapter;
import com.happywannyan.R;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileFragService extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment_listing_only, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.service_recycler);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(new ProfileServiceListingAdapter(getActivity()));
    }
}

package com.happywannyan.Activities.profile.profilepagerFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.R;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileFragAbout extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment_about, container, false);
    }
}

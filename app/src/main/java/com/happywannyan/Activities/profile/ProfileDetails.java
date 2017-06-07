package com.happywannyan.Activities.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.happywannyan.Activities.profile.fragmentPagerAdapter.ProfileFragPagerAdapter;
import com.happywannyan.R;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileDetails extends AppCompatActivity {
    private ViewPager viewpager;
    private ProfileFragPagerAdapter pagerAdapter = null;
    private LinearLayout reservation = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        reservation = (LinearLayout) findViewById(R.id.reservation);
        pagerAdapter = new ProfileFragPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    reservation.setVisibility(View.GONE);
                } else {
                    reservation.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


}

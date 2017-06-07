package com.happywannyan.Activities.Booking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.happywannyan.R;

public class BookingOne extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_one);
        findViewById(R.id.IMG_icon_back).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IMG_icon_back:
                finish();
                break;



        }
    }
}

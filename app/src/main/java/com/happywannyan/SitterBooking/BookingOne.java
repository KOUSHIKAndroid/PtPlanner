package com.happywannyan.SitterBooking;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.R;

public class BookingOne extends AppCompatActivity implements View.OnClickListener,OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_one);
        findViewById(R.id.IMG_icon_back).setOnClickListener(this);

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.Body,BookingFragmnetOne.newInstance(null,null));
        fragmentTransaction.disallowAddToBackStack().commit();





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IMG_icon_back:
                finish();
                break;



        }
    }



    @Override
    public void onFragmentInteraction(String uri) {
        switch (uri)
        {
            case "Two":
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Body,BookingFragmentTwo.newInstance(null,null));
                fragmentTransaction.addToBackStack(null).commit();
                break;
             case "Three":
                 fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Body,BookingFrgamnetThree.newInstance(null,null));
                fragmentTransaction.addToBackStack(null).commit();
                break;

        }
    }
}

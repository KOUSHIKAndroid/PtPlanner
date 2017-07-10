package com.happywannyan.SitterBooking;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookingOne extends AppCompatActivity implements View.OnClickListener,OnFragmentInteractionListener {

    public String ServiceList;
    public String PRESelectService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_one);
        findViewById(R.id.IMG_icon_back).setOnClickListener(this);


        try {
            ServiceList=getIntent().getStringExtra("LIST");
            PRESelectService=getIntent().getStringExtra("SELECT");
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.Body,BookingFragmnetOne.newInstance(ServiceList,PRESelectService));
        fragmentTransaction.disallowAddToBackStack().commit();





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IMG_icon_back:
                onBackPressed();
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

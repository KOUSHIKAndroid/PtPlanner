package com.happywannyan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.firebase.iid.FirebaseInstanceId;
import com.happywannyan.Activities.Booking.BookingOne;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.BookingFragment;
import com.happywannyan.Fragments.MyPets_Fragments;
import com.happywannyan.Fragments.Search_Basic;
import com.happywannyan.Fragments.Message_Fragment;
import com.happywannyan.R;
import com.happywannyan.Utils.App_data_holder;
import com.happywannyan.Utils.CircleTransform;
import com.happywannyan.Utils.Loger;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("@@@", "Refreshed token: " + refreshedToken);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        Search_Basic search_basic=new Search_Basic();
        fragmentTransaction.add(R.id.Base_fargment_layout,search_basic);
        fragmentTransaction.commit();







        /*
        @Koushik
        Navigation Control respect of UserCredintial
         */
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        final ImageView UserImage=(ImageView)navigationView.getHeaderView(0).findViewById(R.id.imageView);
        final SFNFTextView UserName=(SFNFTextView)navigationView.getHeaderView(0).findViewById(R.id.TXT_UserName);
        final SFNFTextView txt_login_label=(SFNFTextView)navigationView.getHeaderView(0).findViewById(R.id.TXT_loginlabel);


        navigationView.findViewById(R.id.LL_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                Message_Fragment search_basic=new Message_Fragment();
                fragmentTransaction.add(R.id.Base_fargment_layout,search_basic);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        new App_data_holder(BaseActivity.this).GET_SHAREDATA(App_data_holder.UserData, new App_data_holder.App_sharePrefData() {
            @Override
            public void Avialable(boolean avilavle, JSONObject data) {
                try{

                    Loger.MSG("@@ DADAD",""+data);
                    Glide.with(BaseActivity.this).load(data.getJSONObject("info_array").getString("image_path")).transform(new CircleTransform(BaseActivity.this)).into(UserImage);
                    UserName.setText(data.getJSONObject("info_array").getString("firstname"));
                    txt_login_label.setVisibility(View.GONE);
                }catch (JSONException e)
                {

                }
            }

            @Override
            public void NotAvilable(String Error) {
                navigationView.findViewById(R.id.LL_message).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_Booking).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_yourPets).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_Payment).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_Profile).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_Logout).setVisibility(View.GONE);
                navigationView.getHeaderView(0).findViewById(R.id.RL_HEADER).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(BaseActivity.this,LoginActivity.class));
                    }
                });



            }
        });

        navigationView.findViewById(R.id.LL_Logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new App_data_holder(BaseActivity.this).LogOut_ClearAllData();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(BaseActivity.this,LoginChoser.class));
                finish();

            }
        });

        navigationView.findViewById(R.id.LL_Booking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
//                startActivity(new Intent(BaseActivity.this,BookingOne.class));
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.Base_fargment_layout, new BookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        navigationView.findViewById(R.id.LL_Profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);


            }
        });

        navigationView.findViewById(R.id.LL_yourPets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.Base_fargment_layout, MyPets_Fragments.newInstance(null,null));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Menu_Drawer()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer.openDrawer(navigationView);

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}

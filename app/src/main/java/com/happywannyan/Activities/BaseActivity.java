package com.happywannyan.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Events;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.BookingFragment;
import com.happywannyan.Fragments.Favourite;
import com.happywannyan.Fragments.MyPets_Fragments;
import com.happywannyan.Fragments.Search_Basic;
import com.happywannyan.Fragments.Message_Fragment;
import com.happywannyan.R;
import com.happywannyan.Utils.App_data_holder;
import com.happywannyan.Utils.CircleTransform;
import com.happywannyan.Utils.LocationListener.LocationBaseActivity;
import com.happywannyan.Utils.LocationListener.LocationConfiguration;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.constants.ProviderType;

import org.json.JSONException;
import org.json.JSONObject;


public class BaseActivity extends LocationBaseActivity
        implements  NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
Events events;
    NavigationView navigationView;
    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(getClass().getName(), "Refreshed token: " + refreshedToken);

//       LocationRequest();


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Search_Basic search_basic = new Search_Basic();
        fragmentTransaction.add(R.id.Base_fargment_layout, search_basic);
        fragmentTransaction.commit();


        /*
        @Koushik
        Navigation Control respect of UserCredintial
         */
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        final ImageView UserImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        final SFNFTextView UserName = (SFNFTextView) navigationView.getHeaderView(0).findViewById(R.id.TXT_UserName);
        final SFNFTextView txt_login_label = (SFNFTextView) navigationView.getHeaderView(0).findViewById(R.id.TXT_loginlabel);


        navigationView.findViewById(R.id.LL_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Message_Fragment search_basic = new Message_Fragment();
                fragmentTransaction.add(R.id.Base_fargment_layout, search_basic);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        new AppContsnat(BaseActivity.this).GET_SHAREDATA(App_data_holder.UserData, new App_data_holder.App_sharePrefData() {
            @Override
            public void Avialable(boolean avilavle, JSONObject data) {
                try {

                    Loger.MSG("@@ DADAD", "" + data);
                    Glide.with(BaseActivity.this).load(data.getJSONObject("info_array").getString("image_path")).transform(new CircleTransform(BaseActivity.this)).into(UserImage);
                    UserName.setText(data.getJSONObject("info_array").getString("firstname"));
                    txt_login_label.setVisibility(View.GONE);
                } catch (JSONException e) {

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
                        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                    }
                });


            }
        });

        navigationView.findViewById(R.id.LL_Logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AppContsnat(BaseActivity.this).LogOut_ClearAllData();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(BaseActivity.this, LoginChoser.class));
                finish();

            }
        });

        navigationView.findViewById(R.id.LL_Booking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
//                startActivity(new Intent(BaseActivity.this,BookingOne.class));
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
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
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.Base_fargment_layout, MyPets_Fragments.newInstance(null, null));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }); navigationView.findViewById(R.id.LL_Favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.Base_fargment_layout, Favourite.newInstance(null, null));
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

    public void Menu_Drawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer.openDrawer(navigationView);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                LocationRequest();
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        } else {
//            locationManager.requestLocationUpdates(provider, INTERVAL, 1, this);

        }

    }

    @Override
    public LocationConfiguration getLocationConfiguration() {

        return new LocationConfiguration()
                .keepTracking(true)
                .askForGooglePlayServices(true)
                .setMinAccuracy(200.0f)
                .setWaitPeriod(ProviderType.GOOGLE_PLAY_SERVICES, 5 * 1000)
                .setWaitPeriod(ProviderType.GPS, 10 * 1000)
                .setWaitPeriod(ProviderType.NETWORK, 5 * 1000)
                .setGPSMessage(getResources().getString(R.string.would_you_mind_to_turn_gps_on))
                .setRationalMessage(getResources().getString(R.string.give_the_permission));
//        return null;
    }

    @Override
    public void onLocationFailed(int failType) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Loger.MSG("@@@ LAT", "" + location.getLatitude() + location.getLongitude());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Loger.MSG("@@ Provider enable",s);

    }

    @Override
    public void onProviderDisabled(String s) {
        Loger.MSG("@@ Provider Desable",s);

    }


}

package com.happywannyan.Activities.profile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.happywannyan.SitterBooking.BookingOne;
import com.happywannyan.Activities.profile.fragmentPagerAdapter.ProfileFragPagerAdapter;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.App_data_holder;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.provider.RatingColor;
import com.happywannyan.Utils.provider.AppTimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileDetails extends AppCompatActivity {
    private ViewPager viewpager;
    private ProfileFragPagerAdapter pagerAdapter = null;
    private LinearLayout reservation = null;
    JSONObject PrevJSON;
    RatingBar Rating;
    String SitterId;
    String UserData;
    AppLoader appLoader;
   public String JSONRESPONSE;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
        appLoader = new AppLoader(this);
        appLoader.Show();
        Rating = (RatingBar) findViewById(R.id.Rating);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        reservation = (LinearLayout) findViewById(R.id.reservation);

        try {
            new App_data_holder(this).GET_SHAREDATA(App_data_holder.UserData, new App_data_holder.App_sharePrefData() {
                @Override
                public void Avialable(boolean avilavle, JSONObject data) {
                    try {
                        UserData = data.getJSONObject("info_array").getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void NotAvilable(String Error) {

                }
            });
            this.PrevJSON = new JSONObject(getIntent().getStringExtra("data"));
            SitterId = PrevJSON.getString("sitter_user_id");
            Glide.with(this).load(PrevJSON.getString("photo_url")).into((ImageView) findViewById(R.id.IMG_Profile));
            Rating.setRating(Float.parseFloat(PrevJSON.getString("ave_rating")));
            Rating.setIsIndicator(true);
            LayerDrawable stars = (LayerDrawable) Rating.getProgressDrawable();
            RatingColor.SETRatingColor(stars);
            ((SFNFTextView) findViewById(R.id.UserName)).setText(PrevJSON.getString("nickname"));
            ((SFNFTextView) findViewById(R.id.Bussinessname)).setText(PrevJSON.getString("business_name"));
            ((SFNFTextView) findViewById(R.id.Location)).setText(PrevJSON.getString("whole_address"));
            ((SFNFTextView) findViewById(R.id.ReviewNo)).setText(PrevJSON.getString("num_rvw") + " " + getResources().getString(R.string.review));
            findViewById(R.id.map_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    try {
                        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d&q=%f,%f", Double.parseDouble(PrevJSON.getString("lat")), Double.parseDouble(PrevJSON.getString("long")), 17, Double.parseDouble(PrevJSON.getString("lat")), Double.parseDouble(PrevJSON.getString("long")));
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String Data = getIntent().getStringExtra("data");
        Loger.MSG("@@ Profile", " Intent Data- " + Data);

        ArrayList<APIPOSTDATA> Paramas = new ArrayList<>();
        APIPOSTDATA apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("sitter_user_id");
//        apipostdata.setValues(SitterId);
        apipostdata.setValues(""+3);
        Paramas.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("user_id");
        apipostdata.setValues(UserData);
        Paramas.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("langid");
        apipostdata.setValues(AppContsnat.Language);
        Paramas.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("user_timezone");
        apipostdata.setValues(AppTimeZone.GetTimeZone());
        Paramas.add(apipostdata);


        new JSONPerser().API_FOR_POST(AppContsnat.BASEURL+"app_users_sitterinfo", Paramas, new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                JSONRESPONSE = Result;
                pagerAdapter = new ProfileFragPagerAdapter(getSupportFragmentManager());
                viewpager.setAdapter(pagerAdapter);
                appLoader.Dismiss();
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
            }
        });

        findViewById(R.id.Menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = getLayoutInflater();
                View popupView = layoutInflater.inflate(R.layout.profile_menu, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);

                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(findViewById(R.id.ancher));
            }
        });


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

                ((View)findViewById(R.id.div1)).setBackgroundColor(Color.TRANSPARENT);
                ((View)findViewById(R.id.div2)).setBackgroundColor(Color.TRANSPARENT);
                ((View)findViewById(R.id.div3)).setBackgroundColor(Color.TRANSPARENT);
                ((View)findViewById(R.id.div4)).setBackgroundColor(Color.TRANSPARENT);
                reservation.setVisibility(View.VISIBLE);
                ((SFNFTextView)findViewById(R.id.TXTab1)).setTextColor(Color.parseColor("#666565"));
                ((SFNFTextView)findViewById(R.id.TXTab2)).setTextColor(Color.parseColor("#666565"));
                ((SFNFTextView)findViewById(R.id.TXTab3)).setTextColor(Color.parseColor("#666565"));
                ((SFNFTextView)findViewById(R.id.TXTab4)).setTextColor(Color.parseColor("#666565"));

              switch (position){
                  case 0:
                      ((View)findViewById(R.id.div1)).setBackgroundColor(Color.parseColor("#bf3e49"));
                      ((SFNFTextView)findViewById(R.id.TXTab1)).setTextColor(Color.BLACK);

                      break;
                  case 1:
                      ((View)findViewById(R.id.div2)).setBackgroundColor(Color.parseColor("#bf3e49"));
                      ((SFNFTextView)findViewById(R.id.TXTab2)).setTextColor(Color.BLACK);
                      reservation.setVisibility(View.GONE);
                      break;
                  case 2:
                      ((View)findViewById(R.id.div3)).setBackgroundColor(Color.parseColor("#bf3e49"));
                      ((SFNFTextView)findViewById(R.id.TXTab3)).setTextColor(Color.BLACK);
                      break;
                  case 3:
                      ((View)findViewById(R.id.div4)).setBackgroundColor(Color.parseColor("#bf3e49"));
                      ((SFNFTextView)findViewById(R.id.TXTab4)).setTextColor(Color.BLACK);
                      break;
              }




            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileDetails.this, BookingOne.class));
            }
        });

        ((SFNFTextView)findViewById(R.id.TXTab1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(0);
            }
        }); ((SFNFTextView)findViewById(R.id.TXTab2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(1);
            }
        }); ((SFNFTextView)findViewById(R.id.TXTab3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(2);
            }
        }); ((SFNFTextView)findViewById(R.id.TXTab4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(3);
            }
        });
    }


}

package com.happywannyan.Activities.Booking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookingDetails extends AppCompatActivity {

    JSONObject jsonObject;
    JSONArray PetInfo;
    LinearLayout LLPetInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("data"));
            PetInfo = jsonObject.getJSONArray("pet_details");
            Loger.MSG("@@ Booking Details", jsonObject.toString());
            ImageView profimage = (ImageView) findViewById(R.id.img_view);

            ImageView backimage = (ImageView) findViewById(R.id.IMG_icon_back);
            LLPetInfo = (LinearLayout) findViewById(R.id.LLPetInfo);
            Glide.with(this).load(jsonObject.getJSONObject("users_profile").getString("booked_user_image")).into(profimage);
            ((SFNFTextView) findViewById(R.id.tv_name)).setText(jsonObject.getJSONObject("users_profile").getString("custom_quotes"));
            ((SFNFTextView) findViewById(R.id.tv_type)).setText(jsonObject.getJSONObject("users_profile").getString("booked_user_name"));

            ((SFNFTextView) findViewById(R.id.TXT_total_no_pet)).setText(jsonObject.getJSONObject("booking_info").getString("booked_total_pet"));

            ((SFNFBoldTextView) findViewById(R.id.StartDate)).setText(jsonObject.getJSONObject("booking_info").getString("booking_start_date"));
            ((SFNFBoldTextView) findViewById(R.id.Enddate)).setText(jsonObject.getJSONObject("booking_info").getString("booking_end_date"));
            ((SFNFBoldTextView) findViewById(R.id.BookingId)).setText(jsonObject.getJSONObject("booking_info").getString("booking_id"));
            ((SFNFBoldTextView) findViewById(R.id.tv_service_value)).setText(jsonObject.getJSONObject("booking_info").getString("booking_service"));


            findViewById(R.id.IMG_icon_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });


            if (jsonObject.getJSONObject("booking_info").has("accept_button") && !jsonObject.getJSONObject("booking_info").getString("accept_button").trim().equals("")) {
                findViewById(R.id.card_view_message).setVisibility(View.VISIBLE);
                ((SFNFBoldTextView) findViewById(R.id.TXT_positive)).setText(jsonObject.getJSONObject("booking_info").getString("accept_button"));
            } else {
                findViewById(R.id.card_view_message).setVisibility(View.GONE);
            }

            if (jsonObject.getJSONObject("booking_info").has("deny_button") && !jsonObject.getJSONObject("booking_info").getString("deny_button").trim().equals("")) {
                findViewById(R.id.card_view_cancel).setVisibility(View.VISIBLE);
                ((SFNFBoldTextView) findViewById(R.id.TXT_Cancel)).setText(jsonObject.getJSONObject("booking_info").getString("deny_button"));
            } else {
                findViewById(R.id.card_view_cancel).setVisibility(View.GONE);
            }





            /*
            Pet List Handle With Scrrolig
             */


            if (PetInfo.length() <= 0) {
                View nodata = getLayoutInflater().inflate(R.layout.no_data_layout, null);
                SFNFTextView nodatText = (SFNFTextView) nodata.findViewById(R.id.TXT_Nodata);
                nodatText.setText(getString(R.string.no_pet_data_found));
                findViewById(R.id.TXTeading).setVisibility(View.GONE);
                LLPetInfo.addView(nodata);
            } else {
                for (int i = 0; i < PetInfo.length(); i++) {
                    View PetView = getLayoutInflater().inflate(R.layout.booking_aditional_pet, null);
                    JSONObject PetObj = PetInfo.getJSONObject(i);

                    ImageView petimage = (ImageView) PetView.findViewById(R.id.img_view_pets);
                    Glide.with(this).load(PetObj.getString("pet_image")).into(petimage);
                    ((SFNFTextView) PetView.findViewById(R.id.tv_pet_name_value)).setText(PetObj.getJSONArray("pet_info").getJSONObject(0).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_name_pet)).setText(PetObj.getJSONArray("pet_info").getJSONObject(0).getString("name"));

                    ((SFNFBoldTextView) PetView.findViewById(R.id.tv_year)).setText(PetObj.getJSONArray("pet_info").getJSONObject(1).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.year_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(1).getString("name"));

                    ((SFNFBoldTextView) PetView.findViewById(R.id.tv_month)).setText(PetObj.getJSONArray("pet_info").getJSONObject(2).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_month_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(2).getString("name"));

                    ((SFNFBoldTextView) PetView.findViewById(R.id.tv_gender)).setText(PetObj.getJSONArray("pet_info").getJSONObject(3).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_gender_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(3).getString("name"));

                    ((SFNFBoldTextView) PetView.findViewById(R.id.tv_breed)).setText(PetObj.getJSONArray("pet_info").getJSONObject(4).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_breed_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(4).getString("name"));

                    ((SFNFBoldTextView) PetView.findViewById(R.id.tv_size_value)).setText(PetObj.getJSONArray("pet_info").getJSONObject(5).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_size_name)).setText(PetObj.getJSONArray("pet_info").getJSONObject(5).getString("name"));

                    ((SFNFTextView) PetView.findViewById(R.id.tv_describe_value)).setText(PetObj.getJSONArray("pet_info").getJSONObject(6).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_describe_name)).setText(PetObj.getJSONArray("pet_info").getJSONObject(6).getString("name"));

                    ((SFNFTextView) PetView.findViewById(R.id.tv_answer_one)).setText(PetObj.getJSONArray("pet_info").getJSONObject(7).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_question_one)).setText(PetObj.getJSONArray("pet_info").getJSONObject(7).getString("name"));


                    ((SFNFTextView) PetView.findViewById(R.id.tv_question_two)).setText(PetObj.getJSONArray("pet_info").getJSONObject(3).getString("name"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_answer_two)).setText(PetObj.getJSONArray("pet_info").getJSONObject(3).getString("value"));




                    LLPetInfo.addView(PetView);
                }
            }


            backimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

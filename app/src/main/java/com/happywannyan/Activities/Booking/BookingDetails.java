package com.happywannyan.Activities.Booking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;

import org.json.JSONException;
import org.json.JSONObject;

public class BookingDetails extends AppCompatActivity {

    JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        try {
            jsonObject=new JSONObject(getIntent().getStringExtra("data"));
            Loger.MSG("@@ Booking Details", jsonObject.toString());
            ImageView profimage=(ImageView)findViewById(R.id.img_view);
            ImageView petimage=(ImageView)findViewById(R.id.img_view_pets);
            ImageView backimage=(ImageView)findViewById(R.id.IMG_icon_back);
            Glide.with(this).load(jsonObject.getJSONObject("users_profile").getString("booked_user_image")).into(profimage);
            Glide.with(this).load(jsonObject.getJSONObject("pet_details").getString("pet_image")).into(petimage);
            ((SFNFTextView)findViewById(R.id.tv_name)).setText(jsonObject.getJSONObject("users_profile").getString("custom_quotes"));
            ((SFNFTextView)findViewById(R.id.tv_type)).setText(jsonObject.getJSONObject("users_profile").getString("booked_user_name"));
            ((SFNFTextView)findViewById(R.id.tv_pet_name_value)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(1).getString("value"));
            ((SFNFTextView)findViewById(R.id.tv_describe_value)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(0).getString("value"));
            ((SFNFTextView)findViewById(R.id.tv_describe_name)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(0).getString("name"));
            ((SFNFTextView)findViewById(R.id.tv_question_one)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(2).getString("name"));
            ((SFNFTextView)findViewById(R.id.tv_answer_one)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(2).getString("value"));
            ((SFNFTextView)findViewById(R.id.tv_question_two)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(3).getString("name"));
            ((SFNFTextView)findViewById(R.id.tv_answer_two)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(3).getString("value"));

            ((SFNFBoldTextView)findViewById(R.id.StartDate)).setText(jsonObject.getJSONObject("booking_info").getString("booking_start_date"));
            ((SFNFBoldTextView)findViewById(R.id.Enddate)).setText(jsonObject.getJSONObject("booking_info").getString("booking_end_date"));
            ((SFNFBoldTextView)findViewById(R.id.BookingId)).setText(jsonObject.getJSONObject("booking_info").getString("booking_id"));
            ((SFNFBoldTextView)findViewById(R.id.tv_service_value)).setText(jsonObject.getJSONObject("booking_info").getString("booking_service"));
            ((SFNFBoldTextView)findViewById(R.id.tv_year)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(4).getString("value"));
            ((SFNFBoldTextView)findViewById(R.id.tv_month)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(5).getString("value"));
            ((SFNFBoldTextView)findViewById(R.id.tv_gender)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(6).getString("value"));
            ((SFNFBoldTextView)findViewById(R.id.tv_breed)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(7).getString("value"));
            ((SFNFBoldTextView)findViewById(R.id.tv_size_value)).setText(jsonObject.getJSONObject("pet_details").getJSONArray("pet_info").getJSONObject(8).getString("value"));



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

package com.happywannyan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Activities.profile.ProfileDetails;
import com.happywannyan.POJO.SearchData;
import com.happywannyan.R;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by su on 5/27/17.
 */

public class SearchPets_Adapter extends RecyclerView.Adapter<SearchPets_Adapter.MyViewHolder> {

    Context context;
    ArrayList<SearchData> searchPetArrayList;

    public SearchPets_Adapter(Context context, ArrayList<SearchData> searchPetArrayList){
        this.context=context;
        this.searchPetArrayList=searchPetArrayList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_search_pet,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        SearchData searchData=searchPetArrayList.get(position);


        try {
            Glide.with(context).load(searchData.getSearcItem().getString("photo_url")).into(holder.img_view);
            holder.tv_title.setText(searchData.getSearcItem().getString("business_name"));



//        holder.tv_days.setText(searchPetArrayList.get(position).getDays());
        holder.tv_name.setText(searchData.getSearcItem().getString("nickname"));
        holder.tv_details.setText(searchData.getSearcItem().getString("Profile_summary"));
        holder.tv_address.setText(searchData.getSearcItem().getString("whole_address"));
//        holder.tv_time.setText(searchPetArrayList.get(position).getTime());
        holder.tv_review.setText(searchData.getSearcItem().getString("num_rvw")+" "+context.getResources().getString(R.string.review));

        holder.rating_bar.setRating(Float.parseFloat(searchData.getSearcItem().getString("ave_rating")));



        }catch (JSONException e)
        {

        }catch (Exception e)
        {

        }





    }

    @Override
    public int getItemCount() {
        return searchPetArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView img_view;
        SFNFTextView tv_title,tv_days,tv_name,tv_details,tv_address,tv_time,tv_review;
        AppCompatRatingBar rating_bar;
        public MyViewHolder(View itemView) {
            super(itemView);
            img_view= (AppCompatImageView) itemView.findViewById(R.id.img_view);
            tv_title= (SFNFTextView) itemView.findViewById(R.id.tv_title);
            tv_days= (SFNFTextView) itemView.findViewById(R.id.tv_days);
            tv_name= (SFNFTextView) itemView.findViewById(R.id.tv_name);
            tv_details= (SFNFTextView) itemView.findViewById(R.id.tv_details);
            tv_address= (SFNFTextView) itemView.findViewById(R.id.tv_address);
            tv_time= (SFNFTextView) itemView.findViewById(R.id.tv_time);
            tv_review= (SFNFTextView) itemView.findViewById(R.id.tv_review);
            rating_bar= (AppCompatRatingBar) itemView.findViewById(R.id.rating_bar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 context.startActivity(new Intent(context, ProfileDetails.class));
                }
            });
        }
    }
}

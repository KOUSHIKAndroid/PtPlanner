package com.happywannyan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.YourPets;
import com.happywannyan.R;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by su on 5/26/17.
 */

public class YourPets_Adapter extends RecyclerView.Adapter<YourPets_Adapter.MyViewHolder> {
    ArrayList<YourPets> yourPetsArrayList;
    Context context;

    public YourPets_Adapter(Context context, ArrayList<YourPets> yourPetsArrayList){
        this.context=context;
        this.yourPetsArrayList=yourPetsArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_your_pets,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        YourPets data=yourPetsArrayList.get(position);


        holder.tv_name.setText(data.getPet_name());
        try {
            holder.tv_type.setText(data.getOtherinfo().getJSONObject(0).getString("show_name"));
            holder.tv_year.setText(data.getOtherinfo().getJSONObject(1).getString("show_name"));
            holder.tv_month.setText(data.getOtherinfo().getJSONObject(2).getString("show_name"));
            holder.tv_gender.setText(data.getOtherinfo().getJSONObject(3).getString("show_name"));
            holder.tv_size.setText(data.getOtherinfo().getJSONObject(4).getString("show_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Glide.with(context).load(data.getPet_image()).into(holder.img_view);
    }

    @Override
    public int getItemCount() {
        return yourPetsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_view,img_edit,img_delete;
        SFNFTextView tv_name,tv_type;
        SFNFBoldTextView tv_year,tv_month,tv_gender,tv_size;
        public MyViewHolder(View itemView) {
            super(itemView);
            img_view= (ImageView) itemView.findViewById(R.id.img_view);
            img_edit= (ImageView) itemView.findViewById(R.id.img_edit);
            img_delete= (ImageView) itemView.findViewById(R.id.img_delete);

            tv_name= (SFNFTextView) itemView.findViewById(R.id.tv_name);
            tv_type= (SFNFTextView) itemView.findViewById(R.id.tv_type);
            tv_year= (SFNFBoldTextView) itemView.findViewById(R.id.tv_year);
            tv_month= (SFNFBoldTextView) itemView.findViewById(R.id.tv_month);
            tv_gender= (SFNFBoldTextView) itemView.findViewById(R.id.tv_gender);
            tv_size= (SFNFBoldTextView) itemView.findViewById(R.id.tv_size);
        }
    }
}

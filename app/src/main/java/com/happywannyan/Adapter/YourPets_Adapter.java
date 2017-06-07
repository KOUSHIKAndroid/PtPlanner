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

        holder.tv_name.setText(yourPetsArrayList.get(position).getName());
        holder.tv_type.setText(yourPetsArrayList.get(position).getType());
        holder.tv_year.setText(yourPetsArrayList.get(position).getYear());
        holder.tv_month.setText(yourPetsArrayList.get(position).getMonth());
        holder.tv_gender.setText(yourPetsArrayList.get(position).getGender());
        holder.tv_size.setText(yourPetsArrayList.get(position).getSize());

        Glide.with(context).load(yourPetsArrayList.get(position).getImg()).into(holder.img_view);
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

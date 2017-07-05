package com.happywannyan.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.Advanced_search;
import com.happywannyan.Fragments.Search_Basic;
import com.happywannyan.POJO.PetService;
import com.happywannyan.R;

import java.util.ArrayList;

/**
 * Created by su on 5/22/17.
 */

public class Adapter_petlist extends RecyclerView.Adapter<Adapter_petlist.MyViewHolder> {
    ArrayList<PetService> serviceCatListArrayList;
    Context context;
    Search_Basic search_basic;
    public Adapter_petlist(Search_Basic search_basic,Context context, ArrayList<PetService> serviceCatListArrayList){
        this.context=context;
        this.serviceCatListArrayList=serviceCatListArrayList;
        this.search_basic=search_basic;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_details_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_walking.setText(serviceCatListArrayList.get(position).getName());

        Log.i("default img",""+serviceCatListArrayList.get(position).getDefault_image());
        Log.i("selected img",""+serviceCatListArrayList.get(position).getSelected_image());

        if (serviceCatListArrayList.get(position).isTick_value()){

            holder.img_tick.setImageResource(R.drawable.ic_checked_brown);

            //holder.tv_walking.setTextColor(Color.parseColor("#F78181"));
            holder.tv_walking.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.btn_red, null));

            Glide.with(context)
                    .load(serviceCatListArrayList.get(position).getSelected_image())
//                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .into(holder.img_walking);

        }
        else {
            holder.img_tick.setImageResource(R.drawable.ic_checked_black);

            holder.tv_walking.setTextColor(Color.parseColor("#000000"));

            Glide.with(context)
                    .load(serviceCatListArrayList.get(position).getDefault_image())
//                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .into(holder.img_walking);
        }

        holder.Rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Rel_main","Rel_main");
                for(int i=0;i<serviceCatListArrayList.size();i++){
                    if (i==position){
                        serviceCatListArrayList.get(i).setTick_value(true);
                    }
                    else {
                        serviceCatListArrayList.get(i).setTick_value(false);
                    }
                }

                for (int print=0;print<serviceCatListArrayList.size();print++){
                    Log.i("value["+print+"]",""+serviceCatListArrayList.get(print).isTick_value());
                }
                notifyDataSetChanged();

              search_basic.GotoAdvancedSearched(serviceCatListArrayList.get(position).getJsondata());

            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceCatListArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_tick,img_walking;
        RelativeLayout Rel_main;
        SFNFTextView tv_walking;
        public MyViewHolder(View itemView) {
            super(itemView);
            Rel_main= (RelativeLayout) itemView.findViewById(R.id.Rel_main);
            img_tick= (ImageView) itemView.findViewById(R.id.img_tick);
            img_walking= (ImageView) itemView.findViewById(R.id.img_walking);
            tv_walking= (SFNFTextView) itemView.findViewById(R.id.tv_walking);
        }
    }
}

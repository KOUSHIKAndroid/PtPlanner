package com.ptplanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ptplanner.R;
import com.ptplanner.customviews.TitilliumLight;
import com.ptplanner.fragment.TrainingFragmentList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 12/22/17.
 */

public class AdapterTrainingListRecyclerView extends RecyclerView.Adapter<AdapterTrainingListRecyclerView.MyViewHolder> {

    Context mContext;
    JSONArray jsonArrayAllExercises;
    TrainingFragmentList.OnItemClickByMe onItemClickByMe;
    public AdapterTrainingListRecyclerView(Context mContext, JSONArray jsonArrayAllExercises,TrainingFragmentList.OnItemClickByMe onItemClickByMe){
        this.mContext=mContext;
        this.jsonArrayAllExercises=jsonArrayAllExercises;
        this.onItemClickByMe=onItemClickByMe;
    }
    @Override
    public AdapterTrainingListRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_training_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(AdapterTrainingListRecyclerView.MyViewHolder holder, final int position) {

        try {
            final JSONObject jsonObject=jsonArrayAllExercises.getJSONObject(position);

            holder.tv_name.setText(jsonArrayAllExercises.getJSONObject(position).getJSONObject("exercise_inDetails").getString("exercise_title"));

            Glide.with(mContext)
                    .load(jsonArrayAllExercises.getJSONObject(position).getJSONObject("exercise_inDetails").getString("exercise_image"))
                    .centerCrop()
                    .into(holder.img_view);

            holder.RL_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickByMe.callBackMe(position,jsonObject);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArrayAllExercises.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout RL_main;
        TitilliumLight tv_name;
        ImageView img_view;
        public MyViewHolder(View itemView) {
            super(itemView);

            RL_main= (RelativeLayout) itemView.findViewById(R.id.RL_main);
            img_view= (ImageView) itemView.findViewById(R.id.img_view);
            tv_name= (TitilliumLight) itemView.findViewById(R.id.tv_name);

        }
    }
}

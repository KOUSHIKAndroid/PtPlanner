package com.ptplanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ptplanner.LandScreenActivity;
import com.ptplanner.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by su on 1/25/18.
 */

public class DairyImageAdapter extends RecyclerView.Adapter<DairyImageAdapter.MyViewHolder> {

    Context context;
    JSONArray jsonArray;
    int height,width;

    public DairyImageAdapter(Context context,JSONArray jsonArray){
        this.context=context;
        this.jsonArray=jsonArray;
        Log.i("jsonArray-->",""+jsonArray);

//        Display display = context.getWindowManager().getDefaultDisplay();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((LandScreenActivity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

    @Override
    public DairyImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dairy_image_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {


            holder.img.getLayoutParams().width = (width - 20);
            holder.img.getLayoutParams().height = (int)3*(width - 20)/4;

            Glide.with(context)
                    .load(jsonArray.getString(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.img);
        }
    }
}

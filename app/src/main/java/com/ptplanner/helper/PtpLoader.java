package com.ptplanner.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ptplanner.R;

import io.fabric.sdk.android.services.common.Crash;

/**
 * Created by su on 7/11/17.
 */

public class PtpLoader {
    Context mContext;
    AlertDialog Dailog;

    public PtpLoader(Context mContext) {
        this.mContext = mContext;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        View view=inflater.inflate(R.layout.loader_view,null);
//        ImageView Loader=(ImageView)view.findViewById(R.id.IMGLoader);
//        Glide.with(mContext).load(R.drawable.cat_loader).into(Loader);
        builder.setView(view);
        Dailog=builder.create();
        Dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Dailog.setCanceledOnTouchOutside(false);
    }


    public void Show(){
        try {
            if(!AppConfig.APPBackGround)
            Dailog.show();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

    }
    public void Dismiss(){
        try {
            if(!AppConfig.APPBackGround)
            Dailog.dismiss();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
}

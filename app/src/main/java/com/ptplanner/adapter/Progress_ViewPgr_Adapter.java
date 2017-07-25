package com.ptplanner.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.ptplanner.R;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.datatype.ZoomCurrentImageDataType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Progress_ViewPgr_Adapter extends PagerAdapter {


    ProgressBar progressBar2;
    Context mContext;
    View itemView;
    ImageView profile_pic1;
    LayoutInflater mLayoutInflater;
    String obj;
    TitilliumRegular uploadDate;

    ArrayList<ZoomCurrentImageDataType> client_images;

    public Progress_ViewPgr_Adapter(Context mContext, ArrayList<ZoomCurrentImageDataType> people_images) {
        this.mContext = mContext;
        this.client_images = people_images;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {

        return client_images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        itemView = mLayoutInflater.inflate(R.layout.progress_client_image_slider, container, false);
        profile_pic1 = (ImageView) itemView.findViewById(R.id.slider_image);
        progressBar2 = (ProgressBar) itemView.findViewById(R.id.progressBar2);
        uploadDate = (TitilliumRegular) itemView.findViewById(R.id.upload_date);

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = null;
            try {
                myDate = dateFormat.parse(client_images.get(position).getUploadDate());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat timeFormat = new SimpleDateFormat("MMMM d, yyyy");
            String finalDate = timeFormat.format(myDate);
            uploadDate.setText(finalDate);

            Log.i("client_images",""+client_images.get(position).getImgLink());
            Glide.with(mContext)
                    .load(client_images.get(position).getImgLink())
                    .into(new GlideDrawableImageViewTarget(profile_pic1) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            progressBar2.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            progressBar2.setVisibility(View.GONE);

                            Glide.with(mContext)
                                    .load(R.drawable.no_progress_images)
                                    .into(profile_pic1);
                        }
                    });
        } catch (Exception e) {
            try {
                Glide.with(mContext)
                        .load(client_images.get(position).getImgLink())
                        .into(new GlideDrawableImageViewTarget(profile_pic1) {
                            @Override
                            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                progressBar2.setVisibility(View.GONE);
                            }
                        });
            } catch (Exception exx) {
                exx.printStackTrace();
            }
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}

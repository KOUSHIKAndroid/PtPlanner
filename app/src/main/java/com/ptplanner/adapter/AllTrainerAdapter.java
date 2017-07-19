package com.ptplanner.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.ptplanner.R;
import com.ptplanner.customviews.TitilliumBold;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.datatype.AltrainerDataType;
import com.ptplanner.fragment.ProfileFragment;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.Trns;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by su on 18/6/15.
 */
public class AllTrainerAdapter extends PagerAdapter {


    Context context;
    ArrayList<AltrainerDataType> altrainerDataTypeArrayList;
    LayoutInflater inflater;
    int Size;
    AltrainerDataType cust_data;
    View itemview;
    LinearLayout main_container;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;


    public AllTrainerAdapter(Context context, int i, ArrayList<AltrainerDataType> altrainerDataTypeArrayList) {

        this.context = context;
        this.altrainerDataTypeArrayList = altrainerDataTypeArrayList;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
    }

    @Override
    public int getCount() {
        return altrainerDataTypeArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        //super.instantiateItem(container, position);

        TitilliumBold t_name;
        TitilliumRegular t_address;


        itemview = inflater.inflate(R.layout.k_triner_viewpager, container, false);

        main_container = (LinearLayout) itemview.findViewById(R.id.main_container);

        t_name = (TitilliumBold) itemview.findViewById(R.id.tv_trainer_name);
        t_address = (TitilliumRegular) itemview.findViewById(R.id.tv_trainer_address);
        ImageView pic = (ImageView) itemview.findViewById(R.id.imgv_trainer);
        cust_data = altrainerDataTypeArrayList.get(position);

        if(cust_data.getPt_name().equals("")){
            t_name.setText(cust_data.getPt_email());
        }else {
            t_name.setText(cust_data.getPt_name());
        }

        if(!cust_data.getWorking_address().equalsIgnoreCase("")) {
            t_address.setText(cust_data.getWorking_address());
        }else {
            t_address.setVisibility(View.GONE);
        }

         Picasso.with(context).load(cust_data.getPt_image()).transform(new Trns()).fit().error(R.drawable.placeholdericon).into(pic);

//        Glide.with(context)
//                .load(cust_data.getPt_image())
//                .bitmapTransform(new BitmapTransform(context))
//                .fitCenter()
//                .error(R.drawable.placeholdericon)
//                .into(pic);

        main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.PT_ID = altrainerDataTypeArrayList.get(position).getPt_id();
                //Toast.makeText(context, "" + AppConfig.PT_ID, Toast.LENGTH_LONG).show();

                Bundle bundle = new Bundle();
                bundle.putString("AllTrainer", "AllTrainer");

                fragmentTransaction = fragmentManager.beginTransaction();
                ProfileFragment prl_fragment = new ProfileFragment();
                prl_fragment.setArguments(bundle);
//                fragmentTransaction.replace(R.id.fragment_container, prl_fragment);
                fragmentTransaction.replace(R.id.fragment_container, prl_fragment,"FROM_BookingPage");
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        // Add viewpager_item.xml to ViewPager

        ((ViewPager) container).addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView((View) object);

    }


}

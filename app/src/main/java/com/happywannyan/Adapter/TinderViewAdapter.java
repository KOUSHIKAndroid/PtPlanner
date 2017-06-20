package com.happywannyan.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.SearchResult;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SearchData;
import com.happywannyan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by su on 6/15/17.
 */
public class TinderViewAdapter extends BaseAdapter {

    private List<SearchData> data;
    private Context context;

    public TinderViewAdapter(List<SearchData> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null){
            LayoutInflater inflater =((SearchResult)context).getLayoutInflater();
            // normally use a viewholder
            v = inflater.inflate(R.layout.tinder_card_view, parent, false);
        }

//        ((SFNFTextView) v.findViewById(R.id.tv_address)) .setText(data.get(position));
        try {
            JSONObject object=data.get(position).getSearcItem();
//            JSONObject object=new JSONObject(data.get(position));
            Glide.with(context).load(object.getString("photo_url")).into((ImageView)v.findViewById(R.id.profileImageView));
            ((SFNFTextView) v.findViewById(R.id.tv_title)).setText(object.getString("business_name"));
            ((SFNFTextView) v.findViewById(R.id.tv_address)) .setText(object.getString("whole_address"));
            ((SFNFBoldTextView) v.findViewById(R.id.tv_Price)) .setText(object.getString("currency")+" "+object.getString("price_one"));
            ((SFNFTextView) v.findViewById(R.id.Time)) .setText(object.getString("unit"));
            ((RatingBar) v.findViewById(R.id.rating_bar)) .setNumStars(Integer.parseInt(object.getString("ave_rating")));

        } catch (Exception e) {
            e.printStackTrace();
        }


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String item = (String)getItem(position);
//                Log.i("MainActivity", item);
            }
        });

        return v;
    }
}
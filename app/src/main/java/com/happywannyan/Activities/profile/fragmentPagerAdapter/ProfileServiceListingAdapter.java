package com.happywannyan.Activities.profile.fragmentPagerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileServiceListingAdapter extends RecyclerView.Adapter<ProfileServiceListingAdapter.ViewHolder> {

    private Context mContext = null;
    JSONArray ServiceArry;

    public ProfileServiceListingAdapter(Context mContext,JSONArray serviceArry) {
        this.mContext = mContext;
        this.ServiceArry=serviceArry;
    }

    @Override
    public int getItemCount() {
        return ServiceArry.length();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.profile_service_listing_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject object=ServiceArry.getJSONObject(position);
            holder.Title.setText(object.getString("service_name"));
            holder.Description.setText(object.getString("description"));
            holder.PricePer.setText(object.getString("service_price")+"/"+object.getString("unit_name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SFNFBoldTextView Title;
        SFNFTextView Description;
        SFNFTextView PricePer;
        public ViewHolder(View itemView) {
            super(itemView);
            Title=(SFNFBoldTextView)itemView.findViewById(R.id.Title);
            Description=(SFNFTextView) itemView.findViewById(R.id.Description);
            PricePer=(SFNFTextView) itemView.findViewById(R.id.PricePer);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}

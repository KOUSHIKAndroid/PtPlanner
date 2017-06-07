package com.happywannyan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.Message_Fragment;
import com.happywannyan.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by su on 5/22/17.
 */

public class Adapter_message extends RecyclerView.Adapter<Adapter_message.MyViewHolder> {
    ArrayList<JSONObject> MessageList;
    Context context;
    int from=0;
    public int nextData=1;
    Message_Fragment message_fragment;

    public Adapter_message(Context context, Message_Fragment message_fragment,ArrayList<JSONObject> MessageList){
        this.context=context;
        this.message_fragment=message_fragment;
        this.MessageList=MessageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            JSONObject object=MessageList.get(position);
            holder.Title.setText(MessageList.get(position).getString("message_type"));
            holder.tv_name.setText(MessageList.get(position).getString("usersname"));
            holder.tv_details.setText(MessageList.get(position).getString("message_info"));
            holder.left_red_view.setText(MessageList.get(position).getString("time_difference")+" ago");
            Glide.with(context).load(object.getString("usersimage")).into(holder.img_view);

            if(position==MessageList.size()-1 &&
                    MessageList.size()%10==0
                    && MessageList.size()>=10
                    && nextData==1){

                ///////////lazy load here called///////
                from=from+10;
                //message_fragment.loadList(""+from);
                message_fragment.loadList(String.valueOf(from));
            }

        }catch (JSONException e)
        {

        }
    }


    

    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_view, img_walking;

        SFNFTextView Title,tv_name,tv_details,left_red_view;

        public MyViewHolder(View itemView) {
            super(itemView);

            Title = (SFNFTextView) itemView.findViewById(R.id.tv_title);
            tv_name = (SFNFTextView) itemView.findViewById(R.id.tv_name);
            tv_details = (SFNFTextView) itemView.findViewById(R.id.tv_details);
            left_red_view = (SFNFTextView) itemView.findViewById(R.id.tv_days);
            img_view=(ImageView)itemView.findViewById(R.id.img_view);

        }
    }
}

package com.ptplanner.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.ptplanner.LandScreenActivity;
import com.ptplanner.R;
import com.ptplanner.customviews.HelveticaSemiBoldLight;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.MsgDataType;
import com.ptplanner.fragment.ChatDetailsFragment;
import com.ptplanner.helper.Trns;
import com.squareup.picasso.Picasso;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;


public class MessageRoomAdapter extends ArrayAdapter<MsgDataType> {

    Context context;
    LinkedList<MsgDataType> msgDataTypeLinkedList;
    LayoutInflater inflator;
    int posi;
    FragmentManager fragmentManager;
    Date date;

    SimpleDateFormat dateFormatCurrentDate, dateFormat, targetFormat, jsonDateFormat;


    protected class ViewHolder {
        TitilliumSemiBold currenttime, username;
        TitilliumRegular usermessage;
        ImageView image;
        LinearLayout listItemContainer;
        HelveticaSemiBoldLight chatCounter;
    }

    public MessageRoomAdapter(Context context, int resource, LinkedList<MsgDataType> msgDataTypeLinkedList) {
        super(context, resource, msgDataTypeLinkedList);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.msgDataTypeLinkedList = msgDataTypeLinkedList;

        fragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();
        inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder holder;
        posi = position;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.messages_list_items, parent, false);
            holder.listItemContainer = (LinearLayout) convertView.findViewById(R.id.container);
            holder.username = (TitilliumSemiBold) convertView.findViewById(R.id.user_name);
            holder.currenttime = (TitilliumSemiBold) convertView.findViewById(R.id.current_time);
            holder.usermessage = (TitilliumRegular) convertView.findViewById(R.id.user_message);
            holder.image = (ImageView) convertView.findViewById(R.id.imageview);

            holder.chatCounter = (HelveticaSemiBoldLight) convertView.findViewById(R.id.imageviewreddotnumberofchat);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



//        if(!msgDataTypeLinkedList.get(position).getUser_name().equalsIgnoreCase(""))
        holder.username.setText(msgDataTypeLinkedList.get(position).getUser_name());
//        else
//            holder.username.setVisibility(View.GONE);


        try {
            holder.usermessage.setText((URLDecoder.decode(msgDataTypeLinkedList.get(position).getLast_message(), "UTF-8")));
        } catch (Exception e) {
            Log.i("Receiver MSG : ", e.toString());
        }

        Picasso.with(context).load(msgDataTypeLinkedList.get(position).getUser_image()).placeholder(R.drawable.no_image_available_placeholdder).transform(new Trns())
                .resize(400, 400).centerInside().into(holder.image);

//        Glide.with(context)
//                .load(msgDataTypeLinkedList.get(position).getUser_image())
//                .bitmapTransform(new BitmapTransform(context))
//                .fitCenter()
//                .error(R.drawable.placeholdericon)
//                .into(holder.image);

        dateFormatCurrentDate = new SimpleDateFormat("dd-MM-yyyy");
//        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        targetFormat = new SimpleDateFormat("HH:mm");
        jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date dateCheck = null;

        try {
            dateCheck = new Date();
            Log.d("@@ DATE",""+msgDataTypeLinkedList.get(position).getLast_send_time());
            date = jsonDateFormat.parse(msgDataTypeLinkedList.get(position).getLast_send_time());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (dateFormat.format(date).equalsIgnoreCase(dateFormat.format(dateCheck))) {
                holder.currenttime.setText(targetFormat.format(date));
            } else {
                holder.currenttime.setText(dateFormat.format(date));
            }
        } catch (Exception e) {
            Log.i("Date exception : ", e.toString());
        }

        holder.listItemContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(context, ChatDetailsFragment.class);
                i.putExtra("msgUserName", msgDataTypeLinkedList.get(position).getUser_name());
                i.putExtra("msgUserId", msgDataTypeLinkedList.get(position).getUser_id());
                context.startActivity(i);
                ((LandScreenActivity) context).finish();
            }
        });

        holder.chatCounter.setVisibility(View.GONE);

        return convertView;
    }

}
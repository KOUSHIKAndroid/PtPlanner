package com.happywannyan.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apple on 22/05/17.
 */

public class MYAlert {
    Context mContext;
    AlertDialog Dialog;
    public interface OnlyMessage{
        public void OnOk(boolean res);
    }

    public interface  OnSignleListTextSelected{
        public void  OnSelectedTEXT(JSONObject jsonObject);
    }

    public MYAlert(Context mContext) {
        this.mContext = mContext;
    }

    public void AlertOnly(String Title, String Message, final OnlyMessage onlyMessage){
        AlertDialog.Builder alertbuilder=new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View LayView=inflater.inflate(R.layout.alert_dialog_msg,null);
        SFNFTextView TXTMessage=(SFNFTextView)LayView.findViewById(R.id.Message);
        TXTMessage.setText(Message);
        SFNFTextView TXTTitle=(SFNFTextView)LayView.findViewById(R.id.Title);
        TXTTitle.setText(Title);
        Button BTN_OK=(Button)LayView.findViewById(R.id.BTN_OK);
        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlyMessage.OnOk(true);
                Dialog.dismiss();
            }
        });
        alertbuilder.setView(LayView);
        Dialog=alertbuilder.create();
        Dialog.show();

    }



    public void AlertTextLsit(String Title, JSONArray ListArray, String GetPramsName,final OnSignleListTextSelected onSignleListTextSelected)
    {
        AlertDialog.Builder alertbuilder=new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View LayView=inflater.inflate(R.layout.alert_text_list,null);

        SFNFTextView TXTTitle=(SFNFTextView)LayView.findViewById(R.id.Title);
        TXTTitle.setText(Title);
        LinearLayout ListLay=(LinearLayout)LayView.findViewById(R.id.LL_ALERTList);

        try {
            ListLay.removeAllViews();
            for( int j=0;j<ListArray.length();j++)
            {
                View ListItem=inflater.inflate(R.layout.single_list_text,null);
                SFNFTextView Text=(SFNFTextView)ListItem.findViewById(R.id.TXT_item);

                JSONObject data2=ListArray.getJSONObject(j);
                ListItem.setTag(data2);
                Text.setText(data2.getString(GetPramsName));
                ListLay.addView(ListItem);
                ListItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Dialog.dismiss();
                            onSignleListTextSelected.OnSelectedTEXT(new JSONObject(v.getTag().toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }




        }catch (JSONException e)
        {

        }



        LayView.findViewById(R.id.IMG_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onlyMessage.OnOk(true);
                Dialog.dismiss();
            }
        });
        alertbuilder.setView(LayView);
        Dialog=alertbuilder.create();
        Dialog.show();
    }

}

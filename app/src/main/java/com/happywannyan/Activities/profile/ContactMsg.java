package com.happywannyan.Activities.profile;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.AppCalender;
import com.happywannyan.Utils.Loger;

import java.util.Calendar;

public class ContactMsg extends AppCompatActivity implements View.OnClickListener, AppCalender.OnDateSelect {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_msg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.IMG_icon_drwaer:
                onBackPressed();
                break;
            case R.id.startdate:
                DialogFragment newFragment = AppCalender.newInstance(R.id.startdate);
                newFragment.show(getFragmentManager(), "datePicker");
                break;
            case R.id.AlterDate:
                 newFragment = AppCalender.newInstance(R.id.AlterDate);
                newFragment.show(getFragmentManager(), "datePicker");
                break;
        }
    }

    @Override
    public void Ondate(Calendar date, int viewid) {
        switch (viewid) {
            case R.id.startdate:
                Loger.MSG("@@ Date", date.getTime().toString());
                ((SFNFTextView) findViewById(R.id.startdate)).setText(date.get(Calendar.YEAR) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.DAY_OF_MONTH));
                break;
            case R.id.AlterDate:
                Loger.MSG("@@ Date", date.getTime().toString());
                ((SFNFTextView) findViewById(R.id.AlterDate)).setText(date.get(Calendar.YEAR) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }
}

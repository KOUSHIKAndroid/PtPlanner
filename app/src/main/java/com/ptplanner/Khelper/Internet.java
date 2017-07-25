package com.ptplanner.Khelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ptplanner.helper.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 17/07/17.
 */

public   class Internet extends BroadcastReceiver {
    Context Mcontext;
    Internet_Informer internet_informer;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.Mcontext = context;

        Log.d("@@ BROADCAST"," HIT");
        if (new ConnectionDetector(Mcontext).isConnectingToInternet()) {
            Intent broadcastIntent = new Intent("INTERNET");
            broadcastIntent.putExtra("NET", true);
            LocalBroadcastManager.getInstance(Mcontext).sendBroadcast(broadcastIntent);
            Log.d("@@ BROADCAST"," Connected");
            if(internet_informer!=null)
            {
                internet_informer.OnAvilable(true);
            }
        }else {
            Intent broadcastIntent = new Intent("INTERNET");
            broadcastIntent.putExtra("NET", false);
            LocalBroadcastManager.getInstance(Mcontext).sendBroadcast(broadcastIntent);
            Log.d("@@ BROADCAST","DisConnected");
            if(internet_informer!=null)
            {
                internet_informer.OnUnAvilable(false);
            }
        }

    }

public  void Register(Internet_Informer internet_informer){
    this.internet_informer=internet_informer;
}

public void UnRegister()
{
    this.internet_informer=null;
}


}

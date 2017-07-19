package com.ptplanner;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by ltp on 26/08/15.
 */
public class TimeAlarm extends WakefulBroadcastReceiver {

    NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(), ReminderService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

//        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        CharSequence from = "Soutrik";
//        CharSequence message = "Crazy About Android...";
//        Intent in = new Intent(context, LandScreenActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, in, 0);
//        Notification notif = new Notification(R.mipmap.ic_launcher, "Crazy About Android...", System.currentTimeMillis());
//        notif.setLatestEventInfo(context, from, message, contentIntent);
//        nm.notify(1, notif);
    }
}

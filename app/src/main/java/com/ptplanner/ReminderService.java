package com.ptplanner;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ptplanner.helper.AppController;

/**
 * Created by ltp on 26/08/15.
 */
public class ReminderService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    String TAG = "ReminderIntentService";

    public static String MY_EVENT_ACTION = "My_Events";


    public ReminderService() {
        super("ReminderIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (AppController.isAlermSet().equals("YES")) {
            if (AppController.isAppRunning().equals("YES")) {
                Intent brodIntent = new Intent();
                brodIntent.setAction(MY_EVENT_ACTION);
                brodIntent.putExtra("MODE", "OPEN");
                sendBroadcast(brodIntent);
            } else {
                Intent brodIntent = new Intent();
                brodIntent.setAction(MY_EVENT_ACTION);
                brodIntent.putExtra("MODE", "CLOSE");
                sendBroadcast(brodIntent);
                sendNotificationGroup();
            }
        } else {
            Log.i("TAG", AppController.isAlermSet());
        }

        Log.i("TAG", "NOTI");

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        TimeAlarm.completeWakefulIntent(intent);
    }

    private final void sendNotificationGroup() {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        final PendingIntent contentIntentGroup;
        final Intent intentLandGroup;
        String notificationTitle = "";

        intentLandGroup = new Intent(this, LandScreenActivity.class);
        intentLandGroup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        contentIntentGroup = PendingIntent.getActivity(this, 0, intentLandGroup, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationTitle = "PT-Planner";

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(notificationTitle)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Du har en påminnelse från PT-Planner"))
                        .setContentText("Du har en påminnelse från PT-Planner")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000, 1000})
                        .setLights(Color.RED, 3000, 3000);

        mBuilder.setAutoCancel(true);
        mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        mBuilder.setContentIntent(contentIntentGroup);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}


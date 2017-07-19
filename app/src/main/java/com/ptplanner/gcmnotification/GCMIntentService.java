package com.ptplanner.gcmnotification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ptplanner.LandScreenActivity;
import com.ptplanner.LoginActivity;
import com.ptplanner.R;
import com.ptplanner.SplashActivity;
import com.ptplanner.helper.AppController;
import com.ptplanner.helper.BadgeUtils;

import static android.R.attr.data;

/**
 * Created by ltp on 12/02/16.
 */
public class GCMIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    SharedPreferences loginPreferences;
    String TAG = "GcmIntentService";
    String Message="";

    public static String MY_EVENT_ACTION = "My_Events";

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        Message=extras.getString("message");
        Log.i("@@koushik","@@Gcm Title"+extras.getString("title"));
        Log.i("@@koushik","@@Gcm Details"+extras.toString());



        if (!extras.isEmpty()) {




            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i("NotState : ", "Land  " + AppController.isNotificationState());
                Log.i("NotState : ", "Chat  " + AppController.isNotificationStateChat());

                    if (AppController.isNotificationStateChat().equals("YES") &&
                            AppController.isNotificationState().equals("YES")
                            && !extras.getString("message").equalsIgnoreCase("Account logged out")) {
                        try {
                            try {

                                /*
                                @@KOUSHIK
                                Implement Batch Count Here...
                                 */
                                AppController.K_SetLuncherAndNotificationCount(AppController.K_getNotificationCount()+1);


                                Log.d("## Count","  before-"+AppController.K_getNotificationCount());

                                Log.d("## Count","  after-"+AppController.K_getNotificationCount());
                                BadgeUtils.setBadge(this,AppController.K_getNotificationCount());


                                sendNotificationGroup(
                                        extras.getString("message"),
                                        extras.getString("sent_by"),
                                        extras.getString("sent_to"),
                                        extras.getString("sender_name")
                                );
                            } catch (Exception ee) {

                            }
                        } catch (Exception e) {
                            Log.i("Notification EX : ", e.toString());
                        }
                    }else{

                    }

            }


            if(extras.getString("message").equalsIgnoreCase("Account Deactivated"))
            {
                Intent broadcastIntent = new Intent("message");
                broadcastIntent.putExtra("LOGOUT", "Account Deactivated");
                if (AppController.isNotificationState().equals("YES"))
                    NotificationForAccount(extras.getString("title"),AppController.DeactivationAlert);

                AppController.DeactivationAlert="kontot har inaktiverats , kontakta din tränare .";
                Log.d("@@KOUSHIK CHECk", "Deactivated");
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                SharedPreferences.Editor editor = loginPreferences.edit();
                editor.putBoolean("AC_Activation", false);
                editor.commit();
            }else if(extras.getString("message").equalsIgnoreCase("Account logged out"))
            {
                Intent broadcastIntent = new Intent("message");
                broadcastIntent.putExtra("LOGOUT", "Account logged out");
                Log.d("@@KOUSHIK CHECk", "Logout");
                AppController.DeactivationAlert="Någon annan har loggat in och du har därmed loggats ut";
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                SharedPreferences.Editor editor = loginPreferences.edit();
                editor.putBoolean("AC_Activation", false);
                editor.commit();
                if (AppController.isNotificationState().equals("YES"))
                NotificationForAccount(extras.getString("title"),AppController.DeactivationAlert);
            }

        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadCastReceiver.completeWakefulIntent(intent);
    }

    private void NotificationForAccount(String Title,String message) {




        PendingIntent pendingIntent = null;
//        if(!AppController.getInstance().App_is_forground) {
            Intent intent = new Intent(this, LandScreenActivity.class);
            intent.putExtra("TYPE","SINGLE");
//            intent.putExtras(extras);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.pushicon1)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(message)))
                .setColor(Color.parseColor("#22A6EE"))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private final void sendNotificationGroup(final String msg, final String sentBy, final String sentTo, final String senderName) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        final PendingIntent contentIntentGroup;
        final Intent intentLandGroup;

        intentLandGroup = new Intent(this, SplashActivity.class);
        intentLandGroup.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentLandGroup.putExtra("notiSentBy", "" + sentBy);
        intentLandGroup.putExtra("notiSentTo", "" + sentTo);
        intentLandGroup.putExtra("notiSenderName", "" + senderName);


        contentIntentGroup = PendingIntent.getActivity(this, 0, intentLandGroup, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
//                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.appicon))
                        .setSmallIcon(R.drawable.pushicon1)
                        .setContentTitle(senderName)
                        .setStyle(new NotificationCompat.InboxStyle().setBigContentTitle(""+msg))
                        .setContentText(""+msg)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000, 1000})
                        .setLights(Color.RED, 3000, 3000);
        mBuilder.setColor(Color.parseColor("#22A6EE"));
        mBuilder.setAutoCancel(true);
        mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        mBuilder.setContentIntent(contentIntentGroup);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}

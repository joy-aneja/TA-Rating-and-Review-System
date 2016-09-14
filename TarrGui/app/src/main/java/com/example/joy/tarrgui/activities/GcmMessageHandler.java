package com.example.joy.tarrgui.activities;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.joy.tarrgui.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by joy on 22-04-2015.
 */
public class GcmMessageHandler extends IntentService {
    String title, data;
    private Handler handler;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyMgr;
    int mNotificationId = 001;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        super.onCreate();

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        title = extras.getString("title");
        data = extras.getString("message");
        showNotification();
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    public void showNotification(){
        handler.post(new Runnable() {
            public void run() {
                //Toast.makeText(getApplicationContext(),data , Toast.LENGTH_LONG).show();

                mBuilder.setContentTitle(title);
                mBuilder.setContentText(data);
                mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }
        });

    }
}

package com.example.happybank;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class DepositNotificationService extends Service {

    public static final int DEPOSIT_NOT_CODE = 1;

    public DepositNotificationService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Create a channel if required
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_name),
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getString(R.string.channel_description));
            notificationManager.createNotificationChannel(channel);
        }

        //Set the content of the notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, getString(R.string.channel_name))
                        .setSmallIcon(R.drawable.pig_icon)
                        .setContentTitle(getString(R.string.deposit_not_title))
                        .setContentText(getString(R.string.deposit_not_message))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        // Set what happens when the notification is tapped
        Intent resultIntent = new Intent(this, DepositActivity.class);
        Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivities(this, 0,
                        new Intent[]{backIntent, resultIntent}, PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(resultPendingIntent);

        //Build the notification
        notificationManager.notify(DEPOSIT_NOT_CODE, notificationBuilder.build());

        stopSelf();

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}

package com.mobileapllication.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mobileapllication.R;


// Sets an alarm for notifcation to read

public class AlarmReceiver extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Reading Alarm", "Reading Alarm", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService (NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Reading Alarm" );
        builder.setContentTitle("Reading Time!");
        builder.setContentText("Enjoy reading books");
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());
    }
}

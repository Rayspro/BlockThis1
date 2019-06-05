package com.example.admin.blockthis1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyReciever extends BroadcastReceiver {
    static  final int NOTIFICATION_ID = 1;
    static boolean checked;
    @Override
    public void onReceive(Context context, Intent intent) {

        checked =(Boolean) intent.getExtras().get("checked_status");
        System.out.println("showNotification ********-->" + checked);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(false)
                .setContentTitle("Busy Mode")
                .setContentText("Busy Mode is On, No one will disturb you")
                .setSmallIcon(R.drawable.block_notify)
                .setOngoing(true);       // make it removable


        Notification notification = builder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);

        if(checked) {
            manager.notify(NOTIFICATION_ID, notification);
        }
    }
}

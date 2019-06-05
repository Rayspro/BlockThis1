package com.example.admin.blockthis1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by admin on 12/03/18.
 */

public class MyService extends IntentService {
    static final int NOTIFICATION_ID = 1;
     static Boolean checked;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyService(String name) {
        super(name);
    }
    public MyService(){
        super("MyService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        run((Boolean)intent.getExtras().get("checked_status"));
    }

    public void run(Boolean b)
    {
        showNotification(b);
        Intent intent = new Intent();
                intent.putExtra("checked_status",b);
                intent.setAction("com.example.admin.blockthis1.BUSY_MODE");
                sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("onCreate() :");
    }

    @Override
    public void onDestroy() {
        Intent newIntent = new Intent();
        System.out.println("checked -> in destroy"+checked);
        newIntent.putExtra("checked_status",checked);
        newIntent.setAction("com.example.admin.blockthis1.BUSY_MODE");
        sendBroadcast(newIntent);
        System.out.println("onDestroy() :");
        super.onDestroy();
    }

    private void showNotification(boolean b) {


        System.out.println("showNotification ********-->" + b);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(false)
                .setContentTitle("Busy Mode")
                .setContentText("Busy Mode is On, No one will disturb you")
                .setSmallIcon(R.drawable.block_notify)
                .setOngoing(b);       // make it removable

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);


        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        if(b) {
            manager.notify(NOTIFICATION_ID, notification);
        }
        if(!b){
            stopSelf();
            manager.cancel(NOTIFICATION_ID);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startid)
    {
        System.out.println("onStartCommand() :");
        checked =(Boolean) intent.getExtras().get("checked_status");
        Log.d("Service Called","------->");
        showNotification(checked);
        return START_STICKY;
    }
}

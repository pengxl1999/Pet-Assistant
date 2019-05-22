package com.pengxl.petassistant;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;

import static com.pengxl.petassistant.StaticMember.*;

public class AlertService extends Service {

    private final IBinder binder = new AlertService.LocalBinder();
    private boolean flag = false;
    private Thread thread;

    public class LocalBinder extends Binder {
        AlertService getService() {
            return AlertService.this;
        }
    }

    public void work() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int id = 1;
                final String CHANNEL_ID = "pengxl_id_1233";
                final String CHANNEL_NAME = "pengxl_name_1233";
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setBypassDnd(true);
                channel.setShowBadge(true);
                channel.enableLights(true);
                channel.enableVibration(true);
                manager.createNotificationChannel(channel);

                //设置通知点击事件
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName(AlertService.this, MainActivity.class));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                PendingIntent pendingIntent = PendingIntent.getActivity(AlertService.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(AlertService.this, CHANNEL_ID);
                builder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle("宠物助手").setContentText("宠物离开围栏区域！").setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setContentIntent(pendingIntent);
                while(!thread.isInterrupted()) {
                     try {
                         if(geoFence != null && petPosition != null && !geoFence.contains(petPosition)) {
                             manager.notify(id, builder.build());
                             Log.i("pengxl1999", "alert!");
                             Thread.sleep(5000);
                         }
                     } catch (Exception e) {
                         e.printStackTrace();
                         break;
                     }
                }
            }
        });
        thread.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

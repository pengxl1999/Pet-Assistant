package com.pengxl.petassistant;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.net.DatagramSocket;

public class ReceiveService extends Service {
    private final IBinder binder = new LocalBinder();
    private ReceiveThread thread;
    private boolean flag = false;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public class LocalBinder extends Binder {
        ReceiveService getService() {
            return ReceiveService.this;
        }
    }

    public void work() {
        flag = false;
            try {
                //Log.i("START","start");     //启动服务
                context = getApplicationContext();
                thread = new ReceiveThread(context);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void setFlag() {     //停止服务时使用，暂不需要
        flag = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

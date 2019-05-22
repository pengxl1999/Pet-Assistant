package com.pengxl.petassistant;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageButton homeButton, userButton;
    private ReceiveService receiveService;
    private AlertService alertService;
    private boolean isHomeBtnActivated = true;
    private boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(receiveServiceConnection);
        unbindService(alertServiceConnection);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit()
    {
        if(!isExit) {
            isExit = true;
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
        }
    }

    private void init()
    {
        homeButton = (ImageButton) findViewById(R.id.home_btn);
        userButton = (ImageButton) findViewById(R.id.user_btn);
        homeButton.setActivated(true);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isHomeBtnActivated) {
                    homeButton.setActivated(true);
                    userButton.setActivated(false);
                    isHomeBtnActivated = true;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.user_main_content, new MainFragment());
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isHomeBtnActivated) {
                    homeButton.setActivated(false);
                    userButton.setActivated(true);
                    isHomeBtnActivated = false;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.user_main_content, new UserFragment());
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.user_main_content, new MainFragment());
        ft.addToBackStack(null);
        ft.commit();

        /*废弃代码*/
//        Intent ii = getIntent();
//        if(ii.getStringExtra("NOTIFICATION") != null) {
//            Log.i("haha", "OnCreate By Notification");
//            return;
//        }

        Intent intent = new Intent(MainActivity.this, ReceiveService.class);
        bindService(intent, receiveServiceConnection, Context.BIND_AUTO_CREATE);
        Intent intent1 = new Intent(MainActivity.this, AlertService.class);
        bindService(intent1, alertServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /*绑定接收数据服务*/
    final private ServiceConnection receiveServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ReceiveService.LocalBinder binder = (ReceiveService.LocalBinder) iBinder;
            receiveService = binder.getService();
            receiveService.work();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    /*绑定报警信息服务*/
    final private ServiceConnection alertServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AlertService.LocalBinder binder = (AlertService.LocalBinder) iBinder;
            alertService = binder.getService();
            alertService.work();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

}

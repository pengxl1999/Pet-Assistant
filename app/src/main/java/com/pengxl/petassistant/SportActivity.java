package com.pengxl.petassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.pengxl.petassistant.StaticMember.step;

public class SportActivity extends AppCompatActivity {

    private TextView petStep, petCal;
    private ImageButton back;
    private boolean stop = false;
    private Thread thread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        petStep = (TextView) findViewById(R.id.sport_step);
        petCal = (TextView) findViewById(R.id.sport_cal);
        back = (ImageButton) findViewById(R.id.sport_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        SportActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showInfo();
                            }
                        });
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }

    synchronized private void showInfo() {
        petStep.setText(String.valueOf(step));
        petCal.setText(String.valueOf(step * 22));
    }
}

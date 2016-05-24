package com.sally.customprogressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sally.customprogressbar.views.HorizontalProgressBarWithProgress;
import com.sally.customprogressbar.views.RoundProgressBarWithProgress;


public class MainActivity extends AppCompatActivity {

    public static final int UPDATE = 0x110;

    private HorizontalProgressBarWithProgress pr1;
    private int progress = 0;

    private RoundProgressBarWithProgress rp2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    pr1.setProgress(++progress);
                    rp2.setProgress(++progress);
                    sendEmptyMessageDelayed(UPDATE, 100);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pr1 = (HorizontalProgressBarWithProgress) findViewById(R.id.id_pr1);
        rp2 = (RoundProgressBarWithProgress) findViewById(R.id.id_rp1);


        handler.sendEmptyMessage(UPDATE);
    }
}

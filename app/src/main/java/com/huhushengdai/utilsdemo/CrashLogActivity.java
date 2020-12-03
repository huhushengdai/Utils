package com.huhushengdai.utilsdemo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CrashLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_log);
    }

    public void clickChild(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = null;
                s.length();
            }
        }).start();
    }

    public void clickMain(View v) {
        String s = null;
        s.length();
    }


}
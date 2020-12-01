package com.huhushengdai.utilsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.huhushengdai.log.LogTool;
import com.huhushengdai.utilsdemo.log.LogWriteFileHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void print(View v){
        LogTool.setLogHandler(new LogWriteFileHandler());
        LogTest.print("msg d");
    }
}
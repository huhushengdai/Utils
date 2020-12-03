package com.huhushengdai.utilsdemo;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.huhushengdai.log.LogTool;
import com.huhushengdai.log.LogWriteFileHandler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String path = Environment.getExternalStorageDirectory()+"/Download/log";
        LogTool.setLogHandler(new LogWriteFileHandler.Builder()
                .setSavePath(path).setDirMaxSize(50*1024*1024)
                .setSingleFileMaxSize(5*1024*1024).build());
        LogTool.d("path = " + path);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},102);
    }

    public void print(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    LogTest.print("msg d");
                }
            }
        }).start();

    }
}
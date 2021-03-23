package com.huhushengdai.utilsdemo;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;

import com.huhushengdai.tool.log.LogTool;
import com.huhushengdai.utilsdemo.bean.UserBean;
import com.huhushengdai.utilsdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DataBindingUtil.setContentView();
//        DataBindingUtil.bind();
//        DataBindingUtil.convertBrIdToString();
//        DataBindingUtil.findBinding();
//        DataBindingUtil.getBinding();
//        DataBindingUtil.getDefaultComponent();
//        DataBindingUtil.inflate();
//        DataBindingUtil.setDefaultComponent();
//        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
//        setContentView(R.layout.activity_main);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null, false);
//            }
//        }).start();
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null, false);

        Object tag = view.getTag();
        if (tag != null) {
            Log.d("main", "--------------------:" + tag.toString());
        } else {
            Log.d("main", "--------------------: tag is null");
        }

        ActivityMainBinding binding = DataBindingUtil.bind(view, new MyBindingComponent());

//        setContentView(view);

        //        ActivityMainBinding binding = DataBindingUtil.inflate(
//                LayoutInflater.from(this),R.layout.activity_main,null,false);
        Log.d("main", "bind = " + (binding == null));
        setContentView(binding.getRoot());
        binding.setAge(new ObservableInt(5));
        String path = Environment.getExternalStorageDirectory() + "/Download/log";
//        LogTool.setLogHandler(new LogWriteFileHandler.Builder()
//                .setSavePath(path).setDirMaxSize(50 * 1024 * 1024)
//                .setSingleFileMaxSize(5 * 1024 * 1024).build());
        Log.d("main", path + path);
        bean = new UserBean();
        binding.setLifecycleOwner(this);
        binding.setUser(bean);
        binding.executePendingBindings();
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
    }

    UserBean bean;

    private int count = 0;

    public void print(View v) {
        bean.name.postValue("test" + count++);
//        LogTool.d("count = "+count);
    }
}
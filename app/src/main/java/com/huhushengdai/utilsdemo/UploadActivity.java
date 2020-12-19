package com.huhushengdai.utilsdemo;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.huhushengdai.tool.log.LogTool;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.util.Auth;

import org.json.JSONObject;

import java.io.File;


public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
    }

    String ak = "3bmkJLD-inSGpQnLr_9UlommFT81B5L0ryesJLhS";
    String sk = "X22vza-l53jcZyi_fmaex88R065_Ip2_3j5Im0Se";

    public void upload(View v) {
        //http://apad.etor.vip/houzi
        Auth auth = Auth.create(ak, sk);
        String upToken = auth.uploadToken("apadlog");
        UploadManager uploadManager = new UploadManager();
        String path = Environment.getExternalStorageDirectory() + "/Download/app-debug.apk";
        File file = new File(path);
        uploadManager.put(file, "app.apk", upToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {

                if (info.isOK()){
                    LogTool.d("up ok info :"+info.message);
                }else {
                    LogTool.d("up error info :"+info.message);
                }
                LogTool.d("qiniu "+ key + ",\r\n " + info + ",\r\n " + response);
            }
        }, null);
    }
}
package com.huhushengdai.utilsdemo;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;


public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
        }
    }

    String ak = "3bmkJLD-inSGpQnLr_9UlommFT81B5L0ryesJLhS";
    String sk = "X22vza-l53jcZyi_fmaex88R065_Ip2_3j5Im0Se";

    public void upload(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int code = deleteFileFromQiniu("daixiaolu.apk");
                Log.i("delete", "code = " + code);
            }
        }).start();
    }
//    public void upload(View v) {
//        //http://apad.etor.vip/houzi
//        Auth auth = Auth.create(ak, sk);
//        String upToken = auth.uploadToken("apadlog");
//        UploadManager uploadManager = new UploadManager();
////        String path = Environment.getExternalStorageDirectory() + "/Download/firework-release.apk";
//        String path = Environment.getExternalStorageDirectory() + "/Download/123456.jpg";
//        File file = new File(path);
//        uploadManager.put(file, "daixiaolu.apk", upToken, new UpCompletionHandler() {
////        uploadManager.put("only once,没了".getBytes(), "daixiaolu.apk", upToken, new UpCompletionHandler() {
////        uploadManager.put(file, "666.jpg", upToken, new UpCompletionHandler() {
//            @Override
//            public void complete(String key, ResponseInfo info, JSONObject response) {
//
//                if (info.isOK()){
//                    LogTool.d("up ok info :"+info.message);
//                }else {
//                    LogTool.d("up error info :"+info.message);
//                }
//                LogTool.d("qiniu "+ key + ",\r\n " + info + ",\r\n " + response);
//            }
//        }, null);
//    }

    //删除文件
    public int deleteFileFromQiniu(String fileName) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        String key = fileName;
        Auth auth = Auth.create(ak, sk);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            Response delete = bucketManager.delete("apadlog", key);
            return delete.statusCode;
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            ex.printStackTrace();
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
        return -1;
    }
}
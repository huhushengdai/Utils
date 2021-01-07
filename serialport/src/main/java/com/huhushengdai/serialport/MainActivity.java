package com.huhushengdai.serialport;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.huhushengdai.serialport.databinding.ActivityMainBinding;
import com.huhushengdai.tool.log.LogTool;

import top.maybesix.xhlibrary.serialport.SerialPortHelper;

public class MainActivity extends AppCompatActivity {

    private SerialPortHelper serialPortHelper;
    private final MainData mainData = new MainData();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.DELETE_PACKAGES, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setData(mainData);
        //卸载app
        binding.deleteApp.setOnClickListener(v -> {

            Uri packageUri = Uri.parse("package:" + MainActivity.this.getPackageName());

            Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
            startActivity(intent);
        });
        //打开串口
        binding.openSerialPort.setOnClickListener(v -> openSerialPort());
        binding.send.setOnClickListener(v -> send());
        binding.clearReceive.setOnClickListener(v -> mainData.setReceive(""));
    }

    private void send() {
        if (serialPortHelper == null || !serialPortHelper.isOpen()) {
            toast("串口没有打开");
            return;
        }
        if (TextUtils.isEmpty(mainData.getSend())) {
            toast("请输入发送数据");
            return;
        }
        try {
            serialPortHelper.send(mainData.getSend().getBytes());
            toast("发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            toast("发送串口数据失败");
        }
    }

    /**
     * 打开串口
     */
    private void openSerialPort() {
        if (serialPortHelper != null && serialPortHelper.isOpen()) {
            toast("串口已经打开");
            return;
        }
        if (TextUtils.isEmpty(mainData.getSerialPort())) {
            showError("请设置串口");
            return;
        }

        int baudRate = -1;
        try {
            baudRate = Integer.parseInt(mainData.getBaudRate());
        } catch (Exception e) {
            //do nothing
        }
        if (baudRate <= 0) {
            showError("波特率设置有误，请注意检查");
            return;
        }
        serialPortHelper = new SerialPortHelper("/dev/" + mainData.getSerialPort(), baudRate, comPortData -> {
            if (comPortData != null) {
                LogTool.d("收到串口数据：" + new String(comPortData.getRecData()) + ",RecTime = " + comPortData.getRecTime() + ",ComPort" + comPortData.getComPort());
                onReceiveData(comPortData.getRecData());
            }
        });
        try {
            serialPortHelper.open();
            binding.errorInfo.setVisibility(View.GONE);
            binding.dataContainer.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            showError("打开串口失败，请注意查看串口号是否正确（包括大小写），波特率是否正确，设备是否有串口通讯功能。若依旧无法打开串口，找android开发");
            binding.dataContainer.setVisibility(View.GONE);
        }
    }

    private void showError(String errorInfo) {
        binding.errorInfo.setText(errorInfo);
        binding.errorInfo.setVisibility(View.VISIBLE);
    }

    private void onReceiveData(byte[] data) {
        if (data == null) {
            return;
        }
        mainData.setReceive(new String(data));
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
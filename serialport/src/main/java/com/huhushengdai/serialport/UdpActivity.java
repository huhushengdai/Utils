package com.huhushengdai.serialport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpActivity extends AppCompatActivity {

    private UdpManager mUdpManager;
    private TextView ipView;
    private TextView msgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp);
        ipView = findViewById(R.id.ip);
        msgView = findViewById(R.id.msg);
    }

    public void open(View v) {
        try {
            mUdpManager = new UdpManager("255.255.255.255", 50500);
            mUdpManager.open();
            mUdpManager.setReceiveListener((recMsg, ip) -> runOnUiThread(() -> {
                ipView.setText(ip);
                msgView.setText(recMsg);
            }));
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendUdp(View v) {
        mUdpManager.sendMsg("seekdevice\r\n");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUdpManager.close();
    }
}
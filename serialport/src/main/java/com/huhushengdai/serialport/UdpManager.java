package com.huhushengdai.serialport;

import android.text.TextUtils;

import com.huhushengdai.tool.log.LogTool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Date： 2021/1/13
 * Description:
 */
public class UdpManager {
    private final InetAddress mAddress;//发送地址
    private final int mPort;//发送端口
    private final int mReceiveMaxSize;

    private boolean mOpen;//是否开启udp通讯
    private boolean mClose;//是否已经关闭udp

    private DatagramSocket mUdpSocket;
    private Thread mSendThread;
    private Thread mRecThread;

    private BlockingQueue<String> mMessageQueue;

    private ReceiveListener mReceiveListener;

    public UdpManager(String address, int port) throws UnknownHostException {
        this(address, port, 1024);
    }

    public UdpManager(String address, int port, int recMaxSize) throws UnknownHostException {
        mPort = port;
        mReceiveMaxSize = recMaxSize;
        mAddress = InetAddress.getByName(address);
    }

    public void setReceiveListener(ReceiveListener listener) {
        mReceiveListener = listener;
    }

    public void open() throws SocketException {
        mUdpSocket = new DatagramSocket(mPort);
        mMessageQueue = new ArrayBlockingQueue<>(10);
        mSendThread = new SendThread();
        mSendThread.setName("UdpSendThread");
        mSendThread.start();
        mRecThread = new ReceiveThread();
        mRecThread.setName("UdpRecThread");
        mRecThread.start();
        mOpen = true;
    }

    public void close() {
        mClose = true;
        mMessageQueue.clear();
        mRecThread.interrupt();
        mRecThread = null;
        mSendThread.interrupt();
        mSendThread = null;
        mUdpSocket.close();
        mUdpSocket = null;
    }

    public void sendMsg(String msg) {
        if (!mOpen) {
            throw new IllegalStateException("not open");
        }
        if (mClose) {
            throw new IllegalStateException("is closed");
        }
        if (TextUtils.isEmpty(msg)) {
            LogTool.w("send msg is null");
            return;
        }
        mMessageQueue.add(msg);
    }

    private class SendThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!mClose) {
                try {
                    sendMsg();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendMsg() throws InterruptedException, IOException {
            String msg = mMessageQueue.take();
            if (TextUtils.isEmpty(msg)) {
                LogTool.d("mMessageQueue msg is null");
                return;
            }
            LogTool.d("send msg :" + msg);
            DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.length()
                    , mAddress, mPort);
            mUdpSocket.send(sendPacket);
        }
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            super.run();
            byte[] receiveBuf = new byte[mReceiveMaxSize];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);
            String recMsg;
            while (!mClose) {
                try {
                    mUdpSocket.receive(receivePacket);
                    recMsg = new String(receiveBuf, 0, receivePacket.getLength());
                    LogTool.d("rec msg : " + recMsg+",address :"+receivePacket.getAddress().getHostAddress());
                    if (mReceiveListener != null) {
                        mReceiveListener.onReceive(recMsg,receivePacket.getAddress().getHostAddress());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface ReceiveListener {
        void onReceive(String recMsg,String ip);
    }
}

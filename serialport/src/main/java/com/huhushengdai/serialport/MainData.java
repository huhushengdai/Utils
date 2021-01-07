package com.huhushengdai.serialport;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Date： 2021/1/7
 * Description:
 *
 * @version 1.0
 */
public class MainData extends BaseObservable {
    private String serialPort = "ttyS3";
    private String baudRate = "115200";
    private String send = "123";
    private String receive;

    @Bindable
    public String getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(String serialPort) {
        this.serialPort = serialPort;
        notifyPropertyChanged(BR.serialPort);
    }
    @Bindable
    public String getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(String baudRate) {
        this.baudRate = baudRate;
        notifyPropertyChanged(BR.baudRate);
    }
    @Bindable
    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
        notifyPropertyChanged(BR.send);
    }
    @Bindable
    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
        notifyPropertyChanged(BR.receive);
    }
}

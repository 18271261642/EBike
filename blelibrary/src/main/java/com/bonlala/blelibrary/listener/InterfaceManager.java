package com.bonlala.blelibrary.listener;

/**
 * Created by Admin
 * Date 2021/7/6
 */
public class InterfaceManager {

    //连接状态
    public BleConnStatusListener bleConnStatusListener;

    /**
     * 写入数据返回
     */
    public WriteBackDataListener writeBackDataListener;

    //写入数据返回，带tag
    public WriteBack24HourDataListener writeBack24HourDataListener;



    /**
     * 监听各种设备状态接口，查找手机，音乐控制等
     */
    public OnBleStatusBackListener onBleBackListener;


    //app给蓝牙设备发送指令的回调
    public OnSendWriteDataListener onSendWriteDataListener;

    public void setOnSendWriteDataListener(OnSendWriteDataListener onSendWriteDataListener) {
        this.onSendWriteDataListener = onSendWriteDataListener;
    }

    public OnBleStatusBackListener getOnBleBackListener() {
        return onBleBackListener;
    }

    public void setOnBleBackListener(OnBleStatusBackListener onBleBackListener) {
        this.onBleBackListener = onBleBackListener;
    }

    public void setBleConnStatusListener(BleConnStatusListener bleConnStatusListener) {
        this.bleConnStatusListener = bleConnStatusListener;
    }

    public void setWriteBackDataListener(WriteBackDataListener writeBackDataListener) {
        this.writeBackDataListener = writeBackDataListener;
    }

    public void setWriteBack24HourDataListener(WriteBack24HourDataListener writeBack24HourDataListener) {
        this.writeBack24HourDataListener = writeBack24HourDataListener;
    }
}

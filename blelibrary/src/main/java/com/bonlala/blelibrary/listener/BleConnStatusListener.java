package com.bonlala.blelibrary.listener;

/**
 * 监听连接状态
 * Created by Admin
 * Date 2019/7/3
 */
public interface BleConnStatusListener {

    void onConnectStatusChanged(String mac, int status);
}

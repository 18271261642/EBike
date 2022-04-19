package com.bonlala.ebike.ui.bean;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Admin
 * Date 2022/4/9
 */
public class SearchDeviceBean {


    private BluetoothDevice bluetoothDevice;

    private int rssi;

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public SearchDeviceBean() {
    }

    public SearchDeviceBean(BluetoothDevice bluetoothDevice, int rssi) {
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
    }
}

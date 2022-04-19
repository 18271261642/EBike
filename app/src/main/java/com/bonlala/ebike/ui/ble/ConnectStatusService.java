package com.bonlala.ebike.ui.ble;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import timber.log.Timber;

import com.bonlala.blelibrary.BleConstant;
import com.bonlala.blelibrary.BleSpUtils;
import com.bonlala.blelibrary.listener.BleConnStatusListener;
import com.bonlala.blelibrary.listener.ConnStatusListener;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.http.api.weather.WeatherApi;
import com.bonlala.ebike.utils.BikeUtils;
import com.bonlala.ebike.utils.MmkvUtils;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;


public class ConnectStatusService extends Service {

    private static final String TAG = "ConnectStatusService";

    private IBinder iBinder = new CBikeServiceBinder();

    private BleConnStatusListener bleConnStatusListener;

    public void setBleConnStatusListener(BleConnStatusListener bleConnStatusListener) {
        this.bleConnStatusListener = bleConnStatusListener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleConstant.CONNECTED_ACTION);
        intentFilter.addAction(BleConstant.DIS_CONNECTED_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
    }


    public class CBikeServiceBinder extends Binder {
        public ConnectStatusService getService(){
            return ConnectStatusService.this;
        }
    }


    public void getWeatherData(){
        //113.854447,22.78397
        EasyHttp.get((LifecycleOwner) this).api(new WeatherApi("深圳市宝安区",22.78397d,113.854447d)).request(new OnHttpListener<String>() {
            @Override
            public void onSucceed(String result) {
                Timber.e("----获取天气="+result);
            }

            @Override
            public void onFail(Exception e) {

            }
        });

    }


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == null)
                return;
            Log.e(TAG,"-------连接状态action="+action);
            if(action.equals(BleConstant.CONNECTED_ACTION)){    //连接成功
                AppApplication.getInstance().setConnectStatus(true);
                String bleName = intent.getStringExtra("ble_params");

                AppApplication.getBleOperateManager().syncDeviceTime();

                sendAction(BleConstant.HOME_CONN_STATUS_ACTION);
            }

            if(action.equals(BleConstant.DIS_CONNECTED_ACTION)){    //断开连接
                AppApplication.getInstance().setConnectStatus(false);
                sendAction(BleConstant.HOME_CONN_STATUS_ACTION);



                String saveMa = MmkvUtils.getConnDeviceMac();
                Timber.e("----连接的mac="+saveMa);
                if(!TextUtils.isEmpty(saveMa) && AppApplication.connDeviceMac != null){  //非手动断开 ，需要重连
                    autoConn(saveMa);
                }
            }
        }
    };


    public void autoConn(String mac){
        if(BikeUtils.isEmpty(mac))
            return;
        if(AppApplication.getInstance().isConnectStatus())
            return;
        sendAction(BleConstant.DEVICE_CONNECTNG_ACTION);
        AppApplication.getBleOperateManager().scanYakBleDevice(new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult searchResult) {
                if(TextUtils.isEmpty(searchResult.getName()) || searchResult.getName().equals("NULL"))
                    return;
//                if(!searchResult.getName().toLowerCase().contains("c0"))
//                    return;
                Log.e(TAG,"----自动连接中="+searchResult.getAddress());
                if(searchResult.getAddress().equals(mac)){
                    AppApplication.getBleOperateManager().stopScanDevice();
                    autoConnDevice(searchResult.device);
                }
            }

            @Override
            public void onSearchStopped() {
                sendAction(BleConstant.HOME_CONN_STATUS_ACTION);
            }

            @Override
            public void onSearchCanceled() {
                sendAction(BleConstant.HOME_CONN_STATUS_ACTION);
            }
        },30 * 1000,1);
    }


    private void autoConnDevice(BluetoothDevice bluetoothDevice){
        AppApplication.getBleOperateManager().setBleConnStatusListener(new BleConnStatusListener() {
            @Override
            public void onConnectStatusChanged(String mac, int status) {

            }
        });
        AppApplication.getBleOperateManager().connYakDevice(bluetoothDevice, new ConnStatusListener() {
            @Override
            public void connStatus(int status) {

            }

            @Override
            public void setNoticeStatus(int code) {
                AppApplication.connDeviceMac = bluetoothDevice.getAddress();
                MmkvUtils.saveConnDeviceMac(bluetoothDevice.getAddress());
                MmkvUtils.saveConnDeviceName(bluetoothDevice.getName());
                sendAction(BleConstant.CONNECTED_ACTION);
            }
        });
    }


    private void sendAction(String action){
        Intent intent = new Intent();
        intent.setAction(action);
        this.sendBroadcast(intent);
    }
}

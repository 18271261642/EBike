package com.bonlala.blelibrary;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.bonlala.blelibrary.listener.BleConnStatusListener;
import com.bonlala.blelibrary.listener.ConnStatusListener;
import com.bonlala.blelibrary.listener.InterfaceManager;
import com.bonlala.blelibrary.listener.OnBleStatusBackListener;
import com.bonlala.blelibrary.listener.OnSendWriteDataListener;
import com.bonlala.blelibrary.listener.WriteBack24HourDataListener;
import com.bonlala.blelibrary.listener.WriteBackDataListener;
import com.google.gson.Gson;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Admin
 * Date 2021/9/3
 */
public class BleManager {

    private static final String TAG = "BleManager";


    private static BleManager bleManager;
    private static BluetoothClient bluetoothClient;

    private static Context mContext;

    //天，0今天；1昨天，2前天
    private int dayTag ;

    //用户获取已经连接的设备Mac和name
    private String connBleName;
    private BluetoothDevice connBleDevice;

    //写入的指令
    private final StringBuilder stringBuilder = new StringBuilder();


    private BleConnStatusListener bleConnStatusListener;

    private final InterfaceManager interfaceManager = new InterfaceManager();

    public void setBleConnStatusListener(BleConnStatusListener bleConnStatusListener) {
        this.bleConnStatusListener = bleConnStatusListener;
    }

    public void setOnBleBackListener (OnBleStatusBackListener onBleBackListener){
        this.interfaceManager.onBleBackListener = onBleBackListener;
    }


    public void setOnSendWriteListener(OnSendWriteDataListener onSendWriteListener){
        this.interfaceManager.onSendWriteDataListener = onSendWriteListener;
    }



    public static BleManager getInstance(Context context){
        mContext = context;
        bluetoothClient = new BluetoothClient(mContext);
        if(bleManager == null){
            synchronized (BleManager.class){
                if(bleManager == null){
                    bleManager = new BleManager();
                }
            }
        }
        return bleManager;
    }



    /**
     * 搜索设备
     * @param searchResponse 回调
     * @param duration 搜索时间
     * @param times 搜索次数
     *   eg:duration=10 * 1000;times=1 表示：10s搜索一次，每次10s
     */
    public void startScanBleDevice(final SearchResponse searchResponse, int duration, int times){
        bluetoothClient.stopSearch();
        final SearchRequest searchRequest = new SearchRequest.Builder()
                .searchBluetoothLeDevice(duration,times)
                .build();
        bluetoothClient.search(searchRequest, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                searchResponse.onSearchStarted();
            }

            @Override
            public void onDeviceFounded(SearchResult searchResult) {
                searchResponse.onDeviceFounded(searchResult);
            }

            @Override
            public void onSearchStopped() {
                searchResponse.onSearchStopped();
            }

            @Override
            public void onSearchCanceled() {
                searchResponse.onSearchCanceled();
            }
        });

    }


    /**
     * 停止搜索
     */
    public void stopScan(){
        if(bluetoothClient != null){
            bluetoothClient.stopSearch();
        }
    }

    //根据Mac地址连接蓝牙设备
    public void connBleDeviceByMac(BluetoothDevice bluetoothDevice, ConnStatusListener connStatusListener){
        connBleDevice(bluetoothDevice,connStatusListener);
    }

    public boolean getConnBle(String mac){
       return bluetoothClient.getConnectStatus(mac) == Constants.STATUS_CONNECTED;
    }


    public BluetoothDevice getConnBleDevice(){
        return connBleDevice;
    }


    //断连连接
    public void disConnDevice(){

        String spMac = (String) BleSpUtils.get(mContext,BleConstant.SAVE_BLE_MAC_KEY,"");
        if(TextUtils.isEmpty(spMac))
            return;
        bluetoothClient.disconnect(spMac);
        BleSpUtils.remove(mContext,BleConstant.SAVE_BLE_MAC_KEY);
    }


    private synchronized void connBleDevice(final BluetoothDevice bluetoothDevice, final ConnStatusListener connectResponse){
        BleSpUtils.put(mContext,BleConstant.SAVE_BLE_MAC_KEY,bluetoothDevice.getAddress());
        bluetoothClient.registerConnectStatusListener(bluetoothDevice.getAddress(),connectStatusListener);
        BleConnectOptions options = (new BleConnectOptions.Builder()).setConnectRetry(3).setConnectTimeout(30000).setServiceDiscoverRetry(3).setServiceDiscoverTimeout(20000).build();
        bluetoothClient.connect(bluetoothDevice.getAddress(), options, new BleConnectResponse() {
            @Override
            public void onResponse(final int code, final BleGattProfile data) {
                Log.e(TAG,"-----onResponse="+code+"\n"+new Gson().toJson(data.getServices()));
                List<BleGattService> serviceList = data.getServices();

                if(code == 0){  //连接成功了，开始设置通知
                    connBleName = bluetoothDevice.getName();
                    connBleDevice = bluetoothDevice;
                    //判断是否是OTA升级状态，是OTA状态不保存地址
                    (new Handler(Looper.getMainLooper())).postDelayed(new Runnable() {
                        public void run() {
                            setNotiData(bluetoothDevice.getAddress(), BleConstant.SERVICE_UUID, BleConstant.NOTIFY_UUID,connectResponse);
                            setNotiData(bluetoothDevice.getAddress(), BleConstant.SERVICE_UUID, BleConstant.NOTIFY_BLE_UUID,connectResponse);

                            setNotiData(bluetoothDevice.getAddress(), BleConstant.SERVICE_UUID, BleConstant.NOTIFY_NAVI,connectResponse);

                        }
                    }, 2000L);
                    connectResponse.connStatus(code);

                    (new Handler(Looper.getMainLooper())).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            writeBleDataToDevice(BleConstant.pairByteArray(),writeBackDataListener);
                        }
                    },3 * 1000);
                }
            }
        });

    }


    private final List<byte[]> byteList = new ArrayList<>();
    //是否是24小时数据
    private boolean is24HourData = false;
    private synchronized void setNotiData(String bleMac, UUID serUUID, UUID notiUUID){
        byteList.clear();
        bluetoothClient.notify(bleMac, serUUID, notiUUID, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                if(uuid1.equals(BleConstant.NOTIFY_UUID)){
                    Log.e(TAG,"----通道数据返回="+uuid1.toString()+" "+Arrays.toString(bytes));
                    StringBuilder stringBuilder = new StringBuilder();
                    for(byte bt : bytes){
                        stringBuilder.append(String.format("%02x",bt)+" ");
                    }

                    Log.e(TAG,"----16进制="+stringBuilder.toString());
//                    if(interfaceManager.writeBackDataListener != null){
//                        interfaceManager.writeBackDataListener.backWriteData(bytes);
//                    }

                }

            }

            @Override
            public void onResponse(int i) {

            }
        });

    }

    private final StringBuilder tmpSb = new StringBuilder();
    //设置通知
    private synchronized void setNotiData(String bleMac, UUID serUUID, UUID notiUUID, final ConnStatusListener connStatusListener){
        bluetoothClient.notify(bleMac, serUUID, notiUUID, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, final byte[] value) {
                // Log.e(TAG,"---111-----设置通知="+ character.toString() + " "+Arrays.toString(value));

                 //设置的返回
//                if(character.toString().equals(BleConstant.NOTIFY_BLE_UUID.toString())){
//                    if(interfaceManager.writeBackDataListener != null){
//                        interfaceManager.writeBackDataListener.backWriteData(value);
//                    }
//                }

                tmpSb.delete(0,tmpSb.length());
                for(byte bt : value){
                    tmpSb.append(String.format("%02x",bt)+"");
                 //   Log.e(TAG,"------十进制="+(bt & 0xff));
                }

              //  Log.e(TAG,"----notify16进制="+tmpSb.toString());
                if(!character.toString().equals(BleConstant.NOTIFY_UUID.toString())){
                    Log.e(TAG,"----notify16进制="+tmpSb.toString());
                }
                stringBuilder.append("back bytes="+tmpSb.toString() +" "+"\n");
                if(interfaceManager.writeBackDataListener != null){
                    interfaceManager.writeBackDataListener.backWriteData(value);
                }

                //电量
                if(value.length>3){
                    if((value[0] & 0xff) == 17 && (value[1] & 0xff) == 17){
                        //battery
                        int batteryValue = value[2] & 0xff;
                        setAction(BleConstant.BLE_BATTERY_ACTION,batteryValue+"");
                    }
                }


                //距离，速度  aa330627000600000a24010000 aa330400000000012115090000
                //aa 33 03 53 00 10 00 00 06 03 0b 00 00
                //aa 33 03 00 00 0c 00 00 02 1d 0a 00 00
                if(BleConstant.NOTIFY_NAVI.toString().toLowerCase(Locale.ROOT).equals(notiUUID.toString())){
                    if(value.length>10){
                        if((value[0] & 0xff) == 170 && (value[1] &0xff) == 51){
                            DeviceAutoDataBean deviceAutoDataBean = new DeviceAutoDataBean();


                            byte[] unitArr = Utils.byteToBitOfArray(value[2]);
                            //档位 bit0~3
                            int gear = Utils.getGear(unitArr);
                            deviceAutoDataBean.setGear(gear);

                            //公英制
                            int unit = unitArr[3];
                            deviceAutoDataBean.setUnit(unit);

                            //当前速度 2个byte，低位在前
                            int currSpeed = Utils.getIntFromBytes(value[4],value[3]);
                            deviceAutoDataBean.setCurrentSpeed(currSpeed);

                            //平均速度
                            int avgSpeed = Utils.getIntFromBytes(value[6],value[5]);
                            deviceAutoDataBean.setAvgSpeed(avgSpeed);

                            //骑行时间 3个byte 时 ，分，秒
                            int cycleTimeHour = value[7] & 0xff;
                            deviceAutoDataBean.setRideHour(cycleTimeHour);

                            int cycleTimeMinute = value[8] &0xff;
                            deviceAutoDataBean.setRideMinute(cycleTimeMinute);

                            int cycleSecond = value[9] & 0xff;
                            deviceAutoDataBean.setRideSecond(cycleSecond);

                            //骑行距离 3个byte
                            int cycleDistance = Utils.getIntFromBytes((byte) 0x00,value[12],value[11],value[10]);
                            deviceAutoDataBean.setRideDistance(cycleDistance);


                            Log.e(TAG,"-----deviceBean="+deviceAutoDataBean.toString());

                            Intent intent = new Intent();
                            intent.setAction(BleConstant.DEVICE_AUTO_DATA_ACTION);
                            intent.putExtra(BleConstant.BROADCAST_PARAMS_KEY,deviceAutoDataBean);
                            mContext.sendBroadcast(intent);

                        }
                    }
                }

            }

            @Override
            public void onResponse(int code) {
                connStatusListener.setNoticeStatus(code);

            }
        });
    }

    private final StringBuilder sb = new StringBuilder();
    //写入设备数据
    public synchronized void writeDataToDevice(byte[] data, WriteBackDataListener writeBackDataListener){
        sb.delete(0,sb.length());
        Log.e(TAG,"-----写入数据="+Arrays.toString(data));
        String bleMac = (String) BleSpUtils.get(mContext,BleConstant.SAVE_BLE_MAC_KEY,"");
        if(TextUtils.isEmpty(bleMac))
            return;
//        if(interfaceManager.onSendWriteDataListener != null)
//            interfaceManager.onSendWriteDataListener.sendWriteData(data);
        for(Byte bt : data){
            sb.append(String.format("%02x",bt));
        }
        stringBuilder.append("write data:"+sb.toString()+"   "+"\n");
        interfaceManager.setWriteBackDataListener(writeBackDataListener);
        bluetoothClient.write(bleMac, BleConstant.SERVICE_UUID,BleConstant.WRITE_UUID,data,bleWriteResponse);

    }



    public synchronized void writeBleDataToDevice(byte[] data, WriteBackDataListener writeBackDataListener){
        sb.delete(0,sb.length());
        Log.e(TAG,"-----写入数据="+Arrays.toString(data));
        String bleMac = (String) BleSpUtils.get(mContext,BleConstant.SAVE_BLE_MAC_KEY,"");
        if(TextUtils.isEmpty(bleMac))
            return;
//        if(interfaceManager.onSendWriteDataListener != null)
//            interfaceManager.onSendWriteDataListener.sendWriteData(data);
        for(Byte bt : data){
            sb.append(String.format("%02x",bt));
        }
        stringBuilder.append("write data:"+sb.toString()+"     ");
        interfaceManager.setWriteBackDataListener(writeBackDataListener);
        bluetoothClient.write(bleMac, BleConstant.SERVICE_UUID,BleConstant.WRITE_BLE_UUID,data,bleWriteResponse);

    }



    //写入设备数据
    public synchronized void writeDataToDevice(int day,byte[] data, WriteBack24HourDataListener writeBack24HourDataListener){
      //  this.dayTag = day;
        Log.e(TAG,"-----写入数据="+Arrays.toString(data));
        String bleMac = (String) BleSpUtils.get(mContext,BleConstant.SAVE_BLE_MAC_KEY,"");
        if(TextUtils.isEmpty(bleMac))
            return;
//        if(interfaceManager.onSendWriteDataListener != null)
//            interfaceManager.onSendWriteDataListener.sendWriteData(data);
        byteList.clear();
        interfaceManager.setWriteBack24HourDataListener(writeBack24HourDataListener);
        bluetoothClient.write(bleMac, BleConstant.SERVICE_UUID,BleConstant.WRITE_UUID,data,bleWriteResponse);
    }




    //写入设备数据
    public synchronized void writeExcDataToDevice(byte[] data, WriteBackDataListener writeBackDataListener){
        Log.e(TAG,"-----写入数据="+Arrays.toString(data));
        String bleMac = (String) BleSpUtils.get(mContext,BleConstant.SAVE_BLE_MAC_KEY,"");
        if(TextUtils.isEmpty(bleMac))
            return;
//        if(interfaceManager.onSendWriteDataListener != null)
//            interfaceManager.onSendWriteDataListener.sendWriteData(data);
        interfaceManager.setWriteBackDataListener(writeBackDataListener);
        bluetoothClient.write(bleMac, BleConstant.SERVICE_UUID,BleConstant.WRITE_UUID,data,bleWriteResponse);

//        bluetoothClient.write(bleMac, bleConstant.W301_SERVICE_UUID,bleConstant.W301_WRITE_UUID,data,bleWriteResponse);
    }



    //监听蓝牙连接状态的监听
    private final BleConnectStatusListener connectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if(status == Constants.STATUS_DISCONNECTED){
                connBleName = null;
              //  connBleDevice = null;
            }
            Log.e(TAG,"-----连接状态="+mac +" "+status);
            setAction(status == Constants.STATUS_CONNECTED ? BleConstant.CONNECTED_ACTION : BleConstant.DIS_CONNECTED_ACTION,connBleName);
            if(bleConnStatusListener != null){
                bleConnStatusListener.onConnectStatusChanged(mac==null?"mac":mac,status);
            }
        }
    };


    //获取所以写入的数据
    public String getWriteByteData(){
        return stringBuilder.toString();
    }

    //清除写入的数据
    public void clearAllWriteByteData(){
        stringBuilder.delete(0,stringBuilder.length());
    }


    private final WriteBackDataListener writeBackDataListener = new WriteBackDataListener() {
        @Override
        public void backWriteData(byte[] data) {

        }
    };

    private final BleWriteResponse bleWriteResponse = new BleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };

    private void setAction(String action,String value){
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(BleConstant.BROADCAST_PARAMS_KEY,value);
        mContext.sendBroadcast(intent);
    }

}

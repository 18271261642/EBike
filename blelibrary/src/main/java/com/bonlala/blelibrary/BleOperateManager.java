package com.bonlala.blelibrary;

import android.bluetooth.BluetoothDevice;

import com.bonlala.blelibrary.listener.BleConnStatusListener;
import com.bonlala.blelibrary.listener.ConnStatusListener;
import com.bonlala.blelibrary.listener.WriteBackDataListener;
import com.bonlala.blelibrary.listener.WriteCommBackListener;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.Calendar;

public class BleOperateManager {

    private static final String TAG = "BleOperateManager";

    private static BleOperateManager bleOperateManager;
    private final BleManager bleManager = BleApplication.getBleApplication().getBleManager();


    public static BleOperateManager getInstance(){
        if(bleOperateManager == null){
            synchronized (BleOperateManager.class){
                if(bleOperateManager == null)
                    bleOperateManager = new BleOperateManager();
            }
        }
        return bleOperateManager;
    }


    private BleOperateManager() {
    }


    /**
     * 搜索方法
     * @param searchResponse 回调
     * @param duration 间隔
     * @param times 持续时间
     */
    public void scanYakBleDevice(SearchResponse searchResponse, int duration, int times){
        bleManager.startScanBleDevice(searchResponse,duration,times);
    }

    //停止搜索
    public void stopScanDevice(){
        bleManager.stopScan();
    }

    //设置连接状态监听，在连接之前设置
    public void setBleConnStatusListener(BleConnStatusListener bleConnStatusListener){
        bleManager.setBleConnStatusListener(bleConnStatusListener);
    }


    //连接
    public void connYakDevice(BluetoothDevice bluetoothDevice, ConnStatusListener connStatusListener){
        bleManager.connBleDeviceByMac(bluetoothDevice,connStatusListener);
    }


    public BluetoothDevice getConnDevice(){
        return bleManager.getConnBleDevice();
    }

    //断连连接
    public void disConnYakDevice(){
        bleManager.stopScan();
        bleManager.disConnDevice();
    }

    //写通用的设置，直接写数据
    public void writeCommonByte(byte[] bytes, WriteBackDataListener writeBackDataListener){
        bleManager.writeDataToDevice(bytes,writeBackDataListener);
    }


    //写通用的设置，直接写数据
    public void writeBleCommonByte(byte[] bytes, WriteBackDataListener writeBackDataListener){
        bleManager.writeBleDataToDevice(bytes,writeBackDataListener);
    }


    //获取所以已经写入的数据
    public String getAllWriteDeviceData(){
        return bleManager.getWriteByteData();
    }

    //清除写入的日志
    public void clearAllWriteData(){
        bleManager.clearAllWriteByteData();
    }





    //获取码表当前表盘序号
    public void getDeviceCurrentDial(WriteCommBackListener writeCommBackListener){
        bleManager.writeBleDataToDevice(BleConstant.getDeviceDial(), new WriteBackDataListener() {
            @Override
            public void backWriteData(byte[] data) {
                writeCommBackListener.backCommStrDate(Utils.formatBtArrayToString(data));
            }
        });
    }



    //同步时间
    public void syncDeviceTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        bleManager.writeBleDataToDevice(BleConstant.setDeviceTime(year,month,day,hour,minute,second),writeBackDataListener);
    }


    //进入退出导航
    public void intoAndOutNavi(boolean isInto){
        bleManager.writeBleDataToDevice(BleConstant.intoOrOutNavi(isInto ? 1 :2),writeBackDataListener);
    }


    //下条路名
    public void seNextRouteName(String routeName,WriteBackDataListener writeBackDataListener){
        bleManager.writeBleDataToDevice(BleConstant.nextRoute(routeName),writeBackDataListener);
    }


    //方向，距离，时间
    public void setDirectAndDistance(int direct,int directDistance,int goalDistance,int goalTimeHour,int goalTimeMin,WriteBackDataListener writeBackDataListener){
        bleManager.writeBleDataToDevice(BleConstant.setRouteDirection(direct,directDistance,goalDistance,goalTimeHour,goalTimeMin),writeBackDataListener);

    }


    private final WriteBackDataListener writeBackDataListener = new WriteBackDataListener() {
        @Override
        public void backWriteData(byte[] data) {

        }
    };
}

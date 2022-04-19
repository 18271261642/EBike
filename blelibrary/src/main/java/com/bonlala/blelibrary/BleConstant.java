package com.bonlala.blelibrary;


import java.nio.charset.StandardCharsets;
import java.util.UUID;

//常量类
public class BleConstant {

    public static final String SAVE_BLE_MAC_KEY = "cbike_ble_mac";

    //service UUID
    public static final UUID SERVICE_UUID = UUID.fromString("7658fd00-878a-4350-a93e-da553e719ed0");

    //notify uuid
    public static final UUID NOTIFY_UUID = UUID.fromString("7658fd03-878a-4350-a93e-da553e719ed0");
    //write uuid
    public static final UUID WRITE_UUID =UUID.fromString("7658fd05-878a-4350-a93e-da553e719ed0");



    //write ble
    public static final UUID WRITE_BLE_UUID =UUID.fromString("7658fd01-878a-4350-a93e-da553e719ed0");

    //notify ble
    public static final UUID NOTIFY_BLE_UUID =UUID.fromString("7658fd02-878a-4350-a93e-da553e719ed0");


    public static final UUID NOTIFY_NAVI =UUID.fromString("7658fd04-878a-4350-a93e-da553e719ed0");


    public static final String CONNECTED_ACTION = "com.bonlala.blelibrary.connected";

    public static final String DIS_CONNECTED_ACTION = "com.bonlala.blelibrary.dis_connected";

    public static final String HOME_CONN_STATUS_ACTION = "com.bonlala.blelibrary.status";


    //电量广播，注册广播后可以接受电量信息
    public static final String BLE_BATTERY_ACTION = "com.bonlala.blelibrary.battery";
    //设备主动返回的距离，速度等信息action
    public static final String DEVICE_AUTO_DATA_ACTION = "com.bonlala.blelibrary.auto_data";
    //主动返回的信息

    //连接中的广播
    public static final String DEVICE_CONNECTNG_ACTION = "com.bonlala.blelibrary.conning";

    //广播的通用参数
    public static final String BROADCAST_PARAMS_KEY = "ble_params";

    //配对
    public static byte[] pairByteArray(){
        return new byte[]{0x55,0x01, (byte) 0xA1, (byte) 0xFE, 0x74, 0x69};
    }

    //关机
    public static byte[] powerOff(){
        return new byte[]{0x55,0x02,0x01};
    }

    //设置用户信息

    /**
     *
     * @param year 年
     * @param month 月
     * @param day 日
     * @param height 身高 cm
     * @param weight 体重kg
     * @param sex 性别 1男 0 女
     * @return
     */
    public static byte[] setUserInfoByte(int year,int month,int day,int height,int weight,int sex){

        byte[] userInfoB = new byte[10];
        userInfoB[0] = 0x55;
        userInfoB[1] = 0x03;
        userInfoB[2] = (byte) (year &0xff);
        userInfoB[3] = (byte) ((year >> 8) &0xff);
//        userInfoB[4] = (byte) ((year >> 16) & 0xff);
//        userInfoB[5] = (byte) ((year >> 24) & 0xff);
        userInfoB[4] = (byte) (month & 0xff);
        userInfoB[5] = (byte) (day & 0xff);
        userInfoB[6] = (byte) sex;
        userInfoB[7] = (byte) (weight &0xff);
        userInfoB[8] = (byte) ((weight >>8) &0xff);
        userInfoB[9] = (byte) height;
        return userInfoB;
    }


    //获取设置的指令
    public static byte[] getCommSetData(){
        return new byte[]{0x55,0x1f};

    }


    //设置表盘
    public static byte[] setDeviceDial(int dialId){
        return new byte[]{0x55,0x14, (byte) dialId};
    }

    //获取当前表盘
    public static byte[] getDeviceDial(){
        return new byte[]{0x55,0x15};
    }

    //获取当前电压
    public static byte[] getCurrentVoltage(){
        return new byte[]{0x55,0x04};
    }

    //获取电池容量
    public static byte[] getBatteryCapacity(){
        return new byte[]{0x11,0x11};
    }

    //获取剩余距离
    public static byte[] getLastDistance(){
        return new byte[]{0x11,0x22,0x33};
    }


    //获取控制器软件版本
    public static byte[] getControlVersion(){
        return new byte[]{0x11,0x66,0x77};
    }


    //获取控制器硬件版本
    public static byte[] getControlHardVersion(){
        return new byte[]{0x11,0x67,0x78};
    }

    //获取协议版本
    public static byte[] getPactVersion(){
        return new byte[]{0x11, (byte) 0x90};
    }


    //设置总里程km
    public static byte[] setAllDistance(int distance){
        byte[] countDByte = new byte[6];
        countDByte[0] = 0x55;
        countDByte[1] = 0x06;
        countDByte[2] = (byte) (distance & 0xff);
        countDByte[3] = (byte) ((distance >> 8) & 0xff);
        countDByte[4] = (byte) ((distance >> 16) & 0xff);
        countDByte[5] = (byte) ((distance >> 24) & 0xff);
        return countDByte;

    }

    //设置大灯 1 开 0关
    public static byte[] setLightStatus(boolean isOn){
        return new byte[]{0x55,0x05, (byte) (isOn ? 1 : 0)};
    }


    //设置单位公英制
    public static byte[] setUnit(boolean isKm){
        return new byte[]{0x55,0x07, (byte) (isKm ? 0 : 1)};
    }


    //设置背光等级 0 ~ 2
    public static byte[] setBackLight(int level){
        return new byte[]{0x55,0x08, (byte) level};
    }

    //设置灭屏时间 OFF、5、10、20、30 [0,4]
    public static byte[] setLightOff(int level){
        return new byte[]{0x55,0x09, (byte) level};
    }


    //设置开机密码清除
    public static byte[] setClearPwd(){
        return new byte[]{0x55,0x0A, 0x01};
    }

    //设置车轮直径12、14、16、18、20、22、24、26、27、27.5、700C、28、29 [0,12]
    public static byte[] setBikeWheel(int interval){
        return new byte[]{0x55,0x0B, (byte) interval};
    }

    //限速设置
    //限速设置 5~46km/H step 1km 1BYTE
    public static byte[] setLimitSpeed(int interval){
        return new byte[]{0x55,0x0C, (byte) interval};
    }

    //助力档位设置
    //助力档位设置 0~3、1~3、0~5、1~5、0~9、1~9 [0,5]
    public static byte[] setGear(int interval){
        return new byte[]{0x55,0x0D, (byte) interval};
    }

    //限流设置
    //限流设置 0.5~31.5a 0.5step  2BYTE 低位在前
    public static byte[] setLimitAmpere(int interval){
        return new byte[]{0x55,0x0F, (byte) (interval & 0xff),(byte) ((interval >> 8) & 0xff)};
    }


    //系统电压设置
    //系统电压设置  24、36、48v 1bytes [0,2]
    public static byte[] setSystemVoltage(int interval){
        return new byte[]{0x55,0x10, (byte) interval};
    }

    //欠压保护设置,0.5个等级，最终乘10转换
    //欠压保护设置 10~52v 0.5step 2byte 低位在前
    public static byte[] setLowSafe(int interval){
        return new byte[]{0x55,0x11,(byte) (interval & 0xff),(byte) ((interval >> 8) &0xff)};
    }

    //获取软件版本号
    public static byte[] getVersionInfo(){
        return new byte[]{0x12};
    }

    //设置时间
    //设置当前时间 byte2~3 年(低位在前)，byte4 月，byte5 日，byte6 小时，byte7 分，byte8 秒
    public static byte[] setDeviceTime(int year,int month,int day,int hour,int minute,int second){
        byte[] timeByte = new byte[9];
        timeByte[0] = 0x55;
        timeByte[1] = 0x13;
        timeByte[2] = (byte) (year & 0xff);
        timeByte[3] = (byte) ((year >> 8) & 0xff);
        timeByte[4] = (byte) (month & 0xff);
        timeByte[5] = (byte) (day & 0xff);
        timeByte[6] = (byte) (hour & 0xff);
        timeByte[7] = (byte) (minute & 0xff);
        timeByte[8] = (byte) (second & 0xff);
        return timeByte;
    }



    //进入退出导航 1 进入导航，；退出导航
    public static byte[] intoOrOutNavi(int code){
        return new byte[]{0x55,0x30, (byte) code};
    }


    //设置路口方向
    // "路口方向： 1byte [1,8]1掉头、2左后转、3左转、4左前方、5直行、6右前方、7右转、8右后方、9终点
    //路口距离： 2byte 单位米(低位在前)
    //目标距离： 3byte 单位米(低位在前)
    //目标时间： 2byte 时、分"
    public static byte[] setRouteDirection(int direct,int directDistance,int goalDistance,int goalTimeHour,int goalTimeMin){
        byte[] routeByte = new byte[10];
        routeByte[0] = 0x55;
        routeByte[1] = 0x31;
        //方向
        routeByte[2] = (byte) (direct & 0xff);
        //距离
        routeByte[3] = (byte) (directDistance & 0xff);
        routeByte[4] = (byte) ((directDistance >> 8) & 0xff);
        //距离目标
        routeByte[5] = (byte) (goalDistance & 0xff);
        routeByte[6] = (byte) ((goalDistance >> 8) & 0xff);
        routeByte[7] = (byte) ((goalDistance >> 16) & 0xff);
        //目标时间
        routeByte[8] = (byte) (goalTimeHour & 0xff);
        routeByte[9] = (byte) (goalTimeMin & 0xff);

        return routeByte;
    }

    //下条路名 最大30个byte，转拼音
    public static byte[] nextRoute(String nextRouteStr){
        byte[] nextByte = new byte[32];
        nextByte[0] = 0x55;
        nextByte[1] = 0x32;
        byte[] nextSt = nextRouteStr.getBytes(StandardCharsets.UTF_8);

        System.arraycopy(nextSt,0,nextByte,2, Math.min(nextSt.length, 30));

        return nextByte;
    }



}

package com.bonlala.blelibrary;

import java.io.Serializable;

/**
 * Created by Admin
 * Date 2022/4/18
 */
public class DeviceAutoDataBean implements Serializable {

    //公英制 0 公制；1英制
    private int unit;

    //档位
    private int gear;

    //当前速度
    private int currentSpeed;

    //平均速度
    private int avgSpeed;

    //骑行时间，小时
    private int rideHour;

    //骑行时间 分钟
    private int rideMinute;

    //骑行时间 秒
    private int rideSecond;

    //骑行距离 单位0.1km 取值后除以10
    private float rideDistance;


    public String getHhMmSsStr(){
        return String.format("%02d",rideHour)+":"+String.format("%02d",rideMinute)+":"+String.format("%02d",rideSecond);
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public int getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(int avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public int getRideHour() {
        return rideHour;
    }

    public void setRideHour(int rideHour) {
        this.rideHour = rideHour;
    }

    public int getRideMinute() {
        return rideMinute;
    }

    public void setRideMinute(int rideMinute) {
        this.rideMinute = rideMinute;
    }

    public int getRideSecond() {
        return rideSecond;
    }

    public void setRideSecond(int rideSecond) {
        this.rideSecond = rideSecond;
    }

    public float getRideDistance() {
        return rideDistance;
    }

    public void setRideDistance(float rideDistance) {
        this.rideDistance = rideDistance;
    }

    @Override
    public String toString() {
        return "DeviceAutoDataBean{" +
                "unit=" + unit +
                ", gear=" + gear +
                ", currentSpeed=" + currentSpeed +
                ", avgSpeed=" + avgSpeed +
                ", rideHour=" + rideHour +
                ", rideMinute=" + rideMinute +
                ", rideSecond=" + rideSecond +
                ", rideDistance=" + rideDistance +
                '}';
    }
}

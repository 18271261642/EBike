package com.bonlala.blelibrary;

/**
 * Created by Admin
 * Date 2022/4/16
 */
public class MapNaviBean {

    /**
     * "路口方向： 1byte [1,8]1掉头、2左后转、3左转、4左前方、5直行、6右前方、7右转、8右后方、9终点
     * 路口距离： 2byte 单位米(低位在前)
     * 目标距离： 3byte 单位米(低位在前)
     * 目标时间： 2byte 时、分"
     */
    //路口方向
    private int routeDirect;

    //路口距离
    private int routeDistance;

    //终点距离
    private int endDistance;

    //终点时间 时
    private int endTimeHour;

    //终点时间 分钟
    private int endTimeMinute;

    public int getRouteDirect() {
        return routeDirect;
    }

    public void setRouteDirect(int routeDirect) {
        this.routeDirect = routeDirect;
    }

    public int getRouteDistance() {
        return routeDistance;
    }

    public void setRouteDistance(int routeDistance) {
        this.routeDistance = routeDistance;
    }

    public int getEndDistance() {
        return endDistance;
    }

    public void setEndDistance(int endDistance) {
        this.endDistance = endDistance;
    }

    public int getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(int endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public int getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(int endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }


    @Override
    public String toString() {
        return "MapNaviBean{" +
                "routeDirect=" + routeDirect +
                ", routeDistance=" + routeDistance +
                ", endDistance=" + endDistance +
                ", endTimeHour=" + endTimeHour +
                ", endTimeMinute=" + endTimeMinute +
                '}';
    }
}

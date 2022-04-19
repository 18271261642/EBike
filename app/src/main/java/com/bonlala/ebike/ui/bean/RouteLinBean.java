package com.bonlala.ebike.ui.bean;

/**
 * 路线规划bean
 * Created by Admin
 * Date 2022/4/13
 */
public class RouteLinBean {

    //时间，分钟
    private int routeMinute;

    //距离 米
    private int routeDistance;

    //是否选中
    private boolean isSelect;


    public RouteLinBean() {
    }


    public RouteLinBean(int routeMinute, int routeDistance, boolean isSelect) {
        this.routeMinute = routeMinute;
        this.routeDistance = routeDistance;
        this.isSelect = isSelect;
    }

    public int getRouteMinute() {
        return routeMinute;
    }

    public void setRouteMinute(int routeMinute) {
        this.routeMinute = routeMinute;
    }

    public int getRouteDistance() {
        return routeDistance;
    }

    public void setRouteDistance(int routeDistance) {
        this.routeDistance = routeDistance;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

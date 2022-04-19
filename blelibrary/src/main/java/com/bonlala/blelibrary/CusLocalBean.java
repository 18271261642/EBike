package com.bonlala.blelibrary;

/**
 * Created by Admin
 * Date 2022/4/15
 */
public class CusLocalBean {

    private double lat;

    private double lon;

    //精确度
    private float direction;

    //半径
    private float accuracy;



    public CusLocalBean() {
    }

    public CusLocalBean(double lat, double lon, float direction, float accuracy) {
        this.lat = lat;
        this.lon = lon;
        this.direction = direction;
        this.accuracy = accuracy;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "CusLocalBean{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", direction=" + direction +
                ", accuracy=" + accuracy +
                '}';
    }
}

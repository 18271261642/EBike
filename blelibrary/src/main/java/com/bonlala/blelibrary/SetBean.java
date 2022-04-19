package com.bonlala.blelibrary;

/**
 * 设置对应bean
 */
public class SetBean {

    //出生日期 yyyy-MM-dd格式
    private int birthdayYear = 1992;
    private int birthdayMonth = 1;
    private int birthdayDay = 1;

    //性别 1 男；0女
    private int sex = 1;

    //身高cm
    private int height = 175;

    //体重kg
    private int weight = 70;

    //总里程km
    private int countDistance;


    //单位
    private int unit;


    //背光等级 1,2,3
    private String backLightLevel = "2";

    //自动关机时间 off,5min ,
    private String autoPowerOff = "5min";

    //轮径设置
    private String wheelRadius = "26";

    //限速设置
    private int limitSpeed = 26;

    //助力档位设置 0~3、1~3、0~5、1~5、0~9、1~9
    private String gearLevel = "0~3";

    //限流设置 16.5
    private String limitAmpere = "12.0";

    //系统电压设置
    private int sysVoltage = 0;

    //欠压保护设置
    private String lowSafeVoltage = "31.5";

    //设置大灯 1开，0关
    private int lightStatus = 0;

    public int getBirthdayYear() {
        return birthdayYear;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public void setBirthdayYear(int birthdayYear) {
        this.birthdayYear = birthdayYear;
    }

    public int getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(int birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public int getBirthdayDay() {
        return birthdayDay;
    }

    public void setBirthdayDay(int birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCountDistance() {
        return countDistance;
    }

    public void setCountDistance(int countDistance) {
        this.countDistance = countDistance;
    }

    public String getBackLightLevel() {
        return backLightLevel;
    }

    public void setBackLightLevel(String backLightLevel) {
        this.backLightLevel = backLightLevel;
    }

    public String getAutoPowerOff() {
        return autoPowerOff;
    }

    public void setAutoPowerOff(String autoPowerOff) {
        this.autoPowerOff = autoPowerOff;
    }

    public String getWheelRadius() {
        return wheelRadius;
    }

    public void setWheelRadius(String wheelRadius) {
        this.wheelRadius = wheelRadius;
    }

    public int getLimitSpeed() {
        return limitSpeed;
    }

    public void setLimitSpeed(int limitSpeed) {
        this.limitSpeed = limitSpeed;
    }

    public String getGearLevel() {
        return gearLevel;
    }

    public void setGearLevel(String gearLevel) {
        this.gearLevel = gearLevel;
    }

    public String getLimitAmpere() {
        return limitAmpere;
    }

    public void setLimitAmpere(String limitAmpere) {
        this.limitAmpere = limitAmpere;
    }

    public int getSysVoltage() {
        return sysVoltage;
    }

    public void setSysVoltage(int sysVoltage) {
        this.sysVoltage = sysVoltage;
    }

    public String getLowSafeVoltage() {
        return lowSafeVoltage;
    }

    public void setLowSafeVoltage(String lowSafeVoltage) {
        this.lowSafeVoltage = lowSafeVoltage;
    }

    public int getLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(int lightStatus) {
        this.lightStatus = lightStatus;
    }

    @Override
    public String toString() {
        return "SetBean{" +
                "birthdayYear=" + birthdayYear +
                ", birthdayMonth=" + birthdayMonth +
                ", birthdayDay=" + birthdayDay +
                ", sex=" + sex +
                ", height=" + height +
                ", weight=" + weight +
                ", countDistance=" + countDistance +
                ", unit=" + unit +
                ", backLightLevel='" + backLightLevel + '\'' +
                ", autoPowerOff='" + autoPowerOff + '\'' +
                ", wheelRadius='" + wheelRadius + '\'' +
                ", limitSpeed=" + limitSpeed +
                ", gearLevel='" + gearLevel + '\'' +
                ", limitAmpere='" + limitAmpere + '\'' +
                ", sysVoltage=" + sysVoltage +
                ", lowSafeVoltage='" + lowSafeVoltage + '\'' +
                ", lightStatus=" + lightStatus +
                '}';
    }
}

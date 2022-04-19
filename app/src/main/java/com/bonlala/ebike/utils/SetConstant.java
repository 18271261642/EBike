package com.bonlala.ebike.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 设置的常量
 */
public class SetConstant {


    //获取点击故障状态
    public String getStatus(String key){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("01","正常状态");
        hashMap.put("03","已刹车");
        hashMap.put("04","转把没有归位（停在高位处）");
        hashMap.put("05","转把故障");
        hashMap.put("06","低电压保护");
        hashMap.put("07","过电压保护");
        hashMap.put("08","电机霍尔信号线故障");

        hashMap.put("09","电机相线故障");
        hashMap.put("10","电机高已达到保护点");
        hashMap.put("11","电机温度高已达到保护点");
        hashMap.put("12","电流传感器故障");
        hashMap.put("13","电池内温度故障");
        hashMap.put("14","控制器温度达到温度保护点");
        hashMap.put("15","控制器内温度传感器故障");

        hashMap.put("21","速度传感器故障");
        hashMap.put("22","BMS通讯故障");
        hashMap.put("23","大灯故障");
        hashMap.put("24","大灯传感器故障");

        hashMap.put("25","力矩传感器力矩信号故障");
        hashMap.put("26","力矩传感器速度信号故障");
        hashMap.put("30","通讯故障");
        return hashMap.get(key);
    }


    class StatusBean{
        String keyStr;
        String valueStatus;

        public String getKeyStr() {
            return keyStr;
        }

        public void setKeyStr(String keyStr) {
            this.keyStr = keyStr;
        }

        public String getValueStatus() {
            return valueStatus;
        }

        public void setValueStatus(String valueStatus) {
            this.valueStatus = valueStatus;
        }
    }


    //限流设置，有小数
    public ArrayList<String> getLimitAmpereSource(){
        ArrayList<String> leftLt = new ArrayList<>();
        for(int i = 0;i<32;i++){
            leftLt.add(i+"");
        }
        return leftLt;
    }


    //欠压保护，有小数
    public ArrayList<String> getLowSafeVSource(){
        ArrayList<String> leftL = new ArrayList<>();
        for(int i = 0;i<43;i++){
            leftL.add((10+i)+"");
        }
        return leftL;
    }

    //右侧小数点
    public ArrayList<String> getRightPointSource(){
        ArrayList<String> rightL = new ArrayList<>();
        rightL.add("0");
        rightL.add("5");
        return rightL;
    }


    //单位设置，公制，英制
    public ArrayList<String> getUnitSource(){
        ArrayList<String> list = new ArrayList<>();
        list.add("公制");
        list.add("英制");
        return list;
    }


    //性别
    public ArrayList<String> getSexSource(){
        ArrayList<String> sexList = new ArrayList<>();
        sexList.add("女");
        sexList.add("男");
        return sexList;
    }

    //身高
    public ArrayList<String> getHeightSource(){
        ArrayList<String> heightLt = new ArrayList<>();
        for(int i = 0;i<150;i++){
            heightLt.add((80+i)+"cm");
        }
        return heightLt;
    }

    //体重
    public ArrayList<String> getWeightSource(){
        ArrayList<String> weightList = new ArrayList<>();
        for(int i = 0;i<100;i++){
            weightList.add((30+i)+"kg");
        }
        return weightList;
    }

    //背光等级
    public ArrayList<String> getBackLightInterval(){
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        return list;
    }

    //自动关机时间
    public ArrayList<String> getAutoPowerOffSource(){
        ArrayList<String> list = new ArrayList<>();
        list.add("OFF");
        list.add("5min");
        list.add("10min");
        list.add("20min");
        list.add("30min");
        return list;
    }

    //设置轮径
    public ArrayList<String> getWheelSource(){
        ArrayList<String> list = new ArrayList<>();
        list.add("12");
        list.add("14");
        list.add("16");
        list.add("18");
        list.add("20");
        list.add("22");
        list.add("24");
        list.add("26");
        list.add("27");
        list.add("27.5");
        list.add("700C");
        list.add("28");
        list.add("29");
        return list;
    }

    //限速
    public ArrayList<String> getLimitSpeedSource(){
        ArrayList<String> speedList = new ArrayList<>();
        for(int i = 0;i<41;i++){
            speedList.add((5+i)+"");
        }
        return speedList;
    }

    //助力档位
    public ArrayList<String> getGearSource(){
        ArrayList<String> speedList = new ArrayList<>();
        speedList.add("0~3");
        speedList.add("1~3");
        speedList.add("0~5");
        speedList.add("1~5");
        speedList.add("0~9");
        speedList.add("1~9");
        return speedList;
    }

    //系统电压
    public ArrayList<String> getSysVoltageSource(){
        ArrayList<String> vList = new ArrayList<>();
        vList.add("24");
        vList.add("36");
        vList.add("48");
        return vList;
    }

    //设置大灯
    public ArrayList<String> getLightStatusSource(){
        ArrayList<String> litList = new ArrayList<>();
        litList.add("关");
        litList.add("开");
        return litList;
    }
}

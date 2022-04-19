package com.bonlala.ebike.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

import com.airbnb.lottie.L;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin
 * Date 2022/3/25
 */
public class BikeUtils {

    // 字符串的非空
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }


    public static String getFormatDate(long time,String format){
       SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CHINA);
        return sdf.format(new Date(time));
    }


    //将yyyy-MM-dd格式的日期转换成long 豪秒形式
    public static long transToDate(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            return Objects.requireNonNull(simpleDateFormat.parse(dateStr)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String getCurrDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        return sdf.format(new Date());
    }

    public static String getCurrDate2(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        return sdf.format(new Date());
    }

    //邮箱正则
    public static boolean isValidEmail(String email) {
        if ((email != null) && (!email.isEmpty())) {
            return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
        }
        return false;
    }

    //手机号
    public static boolean isValidPhone(String phoneStr){
        String regex = "^1[345678]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneStr);
        return matcher.matches();
    }


    public static String formatSecond(int second){
        try {
            if(second == 0){
                return "00:00:00";
            }
            int hour = second / 3600;
            int minute = second / 60;
            int secondes = second % 60;

            return String.format("02d",hour)+":"+String.format("%02d",minute)+":"+String.format("%02d",secondes);
        }catch (Exception e){
            e.printStackTrace();
            return "00:00:00";

        }
    }


    public static String formatSecond2(int time){
        if(time == 0){
            return "0'0''";
        }

        int minute = time / 60;
        int secondes = time % 60;

        return time+"'"+secondes+"''";
    }


    //判断蓝牙状态
    public static boolean isBleEnable(Context context){
        try {
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null)
                return false;
            BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter == null)
                return false;
            return bluetoothAdapter.isEnabled();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    public static void openBletooth(Activity context) {
        try {
            // 请求打开 Bluetooth
            Intent requestBluetoothOn = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
            requestBluetoothOn
                    .setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            // 设置 Bluetooth 设备可见时间
            requestBluetoothOn.putExtra(
                    BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                    30 * 1000);
            // 请求开启 Bluetooth
            context.startActivityForResult(requestBluetoothOn,
                    1001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //C078 CB780A33DBF3
    public static String formatNameToMac(String name){
        if(name == null)
            return null;
        StringBuilder stringBuilder = new StringBuilder();
        String afterStr = StringUtils.substringAfter(name," ");
        char[] ctArray = afterStr.toCharArray();
        for(int i = 0;i<ctArray.length;i+=2){
            if(i+1<ctArray.length){
                char txt2 = ctArray[i];
                char txt = ctArray[i+1];
                stringBuilder.append(String.valueOf(txt2)+String.valueOf(txt)+":");
            }

        }
        stringBuilder.replace(stringBuilder.length()-1,stringBuilder.length(),"");
        return stringBuilder.toString();

    }

}

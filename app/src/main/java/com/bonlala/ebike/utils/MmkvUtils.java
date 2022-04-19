package com.bonlala.ebike.utils;

import com.bonlala.ebike.http.api.user.GetLoginInfoApi;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

/**
 * Created by Admin
 * Date 2022/3/25
 */
public class MmkvUtils {


    private final static String SAVE_FILE_KEY = "e_bike_m_key";
    public static final String TOKEN_KEY = "token_key";

    //用户信息的bean
    private static final String USER_INFO_KEY = "user_info_key";
    //连接的设备mac
    private static final String CONN_DEVICE_MAC = "conn_device_mac";
    private static final String CONN_DEVICE_NAME = "conn_device_name";
    //是否已经同意用户协议
    private static final String IS_AGREE_PRIVACY = "is_privacy";


    private static MMKV mmkv;

    public static void initMkv(){
        mmkv = MMKV.mmkvWithID(SAVE_FILE_KEY);
    }

    public static void setSaveParams(String k,String v){
        if(mmkv == null){
            throw new IllegalStateException("You should Call MMKV.initialize() first.");
        }
        mmkv.putString(k,v);
    }

    public static void setSaveParams(String k,Boolean v){
        if(mmkv == null){
            throw new IllegalStateException("You should Call MMKV.initialize() first.");
        }
        mmkv.putBoolean(k,v);
    }



    public static void setSaveObjParams(String k,Object v){
        if(mmkv == null){
            throw new IllegalStateException("You should Call MMKV.initialize() first.");
        }
        if(v instanceof String){
            mmkv.putString(k, (String) v);
        }

        if(v instanceof Boolean){
            mmkv.putBoolean(k, (Boolean) v);
        }

        if(v instanceof  Long){
            mmkv.getLong(k, (Long) v);
        }

        if(v instanceof  Float){
            mmkv.putFloat(k, (Float) v);
        }


    }


    public static Object getSaveParams(String k,Object oj){
        if(mmkv == null){
            throw new IllegalStateException("You should Call MMKV.initialize() first.");
        }
        if(oj instanceof String){
            return mmkv.getString(k, (String) oj);
        }

        if(oj instanceof Integer){
            return mmkv.getInt(k,(int)oj);
        }

        if(oj instanceof Long){
            return mmkv.getLong(k, (Long) oj);
        }

        if(oj instanceof Float){
            return mmkv.getFloat(k, (Float) oj);
        }

        if(oj instanceof Boolean){
            return mmkv.getBoolean(k, (Boolean) oj);
        }

        return mmkv.getString(k, (String) oj);

    }


    //保存用户信息
    public static void saveUserInfoData(GetLoginInfoApi.UserInfoData userInfoData){
        if(userInfoData == null)
            return;
        setSaveParams(USER_INFO_KEY,new Gson().toJson(userInfoData));
    }

    //获取用户信息
    public static GetLoginInfoApi.UserInfoData getUserInfoData(){
        String userInfoStr = (String) getSaveParams(USER_INFO_KEY,"");
        if(userInfoStr == null)
            return null;
        return new Gson().fromJson(userInfoStr,GetLoginInfoApi.UserInfoData.class);
    }


    //保存用户连接的Mac
    public static void saveConnDeviceMac(String mac){
        setSaveParams(CONN_DEVICE_MAC,mac);
    }

    //获取已经连接的Mac
    public static String getConnDeviceMac(){
        return (String) getSaveParams(CONN_DEVICE_MAC,"");
    }

    //保存用户连接的设备名称
    public static void saveConnDeviceName(String name){
        setSaveParams(CONN_DEVICE_NAME,name);
    }

    public static String getConnDeviceName(){
        return (String) getSaveParams(CONN_DEVICE_NAME,"");
    }


    public static void setIsAgreePrivacy(boolean isAgreePrivacy){
        setSaveParams(IS_AGREE_PRIVACY,isAgreePrivacy);
    }

    public static boolean getPrivacy(){
        return (boolean) getSaveParams(IS_AGREE_PRIVACY,false);
    }


    //保存的时间戳，秒，用于判断天气每小时获取一次
    public static void saveLocalTime(long time){
        setSaveObjParams("weather_key",time);
    }

    public static Long getWeatherLocalTime(){
        return (Long) getSaveParams("weather_key",System.currentTimeMillis()/1000);
    }

    //天气图片url
    public static void saveWeatherImgUrl(String ulr){
        getSaveParams("weather_url",ulr);
    }

    public static String getWeatherImgUrl(){
        return (String) getSaveParams("weather_url","");
    }
}

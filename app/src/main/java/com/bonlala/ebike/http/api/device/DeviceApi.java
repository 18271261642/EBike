package com.bonlala.ebike.http.api.device;

import com.bonlala.ebike.http.api.CommApi;
import com.bonlala.ebike.http.net.ServerConstance;

import androidx.annotation.NonNull;

/**
 * Created by Admin
 * Date 2022/4/11
 */
public class DeviceApi implements CommApi {


    private String apiUrl;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Override
    public Class<?> getObjBean() {
        return null;
    }

    //绑定设备
    @NonNull
    @Override
    public String getApi() {
        return getApiUrl();
    }


    //设备名称 参数
    private String deviceName;
    //设备类型 参数
    private String deviceType;
    //设备mac
    private String mac;

    //绑定设备
    public DeviceApi bindDevice(String deviceName, String deviceType, String mac) {
        setApiUrl(ServerConstance.BIND_DEVICE);
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.mac = mac;
        return this;

    }




    //解绑设备
    public DeviceApi unBindDevice() {
        setApiUrl(ServerConstance.UNBIND_DEVICE);
        return this;

    }


    //查询已绑定的设备列表
    public DeviceApi findBindList(){
        setApiUrl(ServerConstance.GET_BIND_DEVICE_LIST);
        return this;
    }


    //用户已绑定返回
    public static class UserBindDevice{

        private String deviceName;
        private String deviceType;
        private String deviceTypeImgUrl;
        private String mac;


        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceTypeImgUrl() {
            return deviceTypeImgUrl;
        }

        public void setDeviceTypeImgUrl(String deviceTypeImgUrl) {
            this.deviceTypeImgUrl = deviceTypeImgUrl;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        @Override
        public String toString() {
            return "UserBindDevice{" +
                    "deviceName='" + deviceName + '\'' +
                    ", deviceType='" + deviceType + '\'' +
                    ", deviceTypeImgUrl='" + deviceTypeImgUrl + '\'' +
                    ", mac='" + mac + '\'' +
                    '}';
        }
    }

}

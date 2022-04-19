package com.bonlala.ebike.http.api.device;

import com.bonlala.ebike.http.api.CommApi;
import com.hjq.http.annotation.HttpHeader;

import androidx.annotation.NonNull;

/**
 * 设备版本管理
 * Created by Admin
 * Date 2022/4/11
 */
public class DeviceVersionApi implements CommApi {
    @Override
    public Class<?> getObjBean() {
        return null;
    }

    @NonNull
    @Override
    public String getApi() {
        return "app/version/newest";
    }

    //设备类型 "C078"
    private String deviceType;

    // 1
    private int versionType;



    //获取版本信息
    public DeviceVersionApi setDeviceVersion(String deviceType, int versionType) {
        this.deviceType = deviceType;
        this.versionType = versionType;
        return this;
    }


    public DeviceVersionApi getDeviceVersionApi(){
        return this;
    }



    public static class DeviceVersionInfo{


        private Integer fileSize;
        private String fileUrl;
        private String remark;
        private Integer updateMethod;
        private String versionName;


        public Integer getFileSize() {
            return fileSize;
        }

        public void setFileSize(Integer fileSize) {
            this.fileSize = fileSize;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Integer getUpdateMethod() {
            return updateMethod;
        }

        public void setUpdateMethod(Integer updateMethod) {
            this.updateMethod = updateMethod;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        @Override
        public String toString() {
            return "DeviceVersionInfo{" +
                    "fileSize=" + fileSize +
                    ", fileUrl='" + fileUrl + '\'' +
                    ", remark=" + remark +
                    ", updateMethod=" + updateMethod +
                    ", versionName='" + versionName + '\'' +
                    '}';
        }
    }

}

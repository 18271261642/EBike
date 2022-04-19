package com.bonlala.ebike.http;


import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.other.AppConfig;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;
import com.hjq.http.model.CacheMode;

import androidx.annotation.NonNull;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2020/10/02
 *    desc   : 服务器配置
 */
public class RequestServer implements IRequestServer {


    public RequestServer(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    private BodyType bodyType = BodyType.FORM;

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    @Override
    public String getHost() {
        if(AppApplication.getInstance().isLocalService()){
            return "http://192.168.1.144:8772/api/";
        }
        return AppConfig.getHostUrl();
    }


    @NonNull
    @Override
    public CacheMode getCacheMode() {
        return CacheMode.NO_CACHE;
    }
}
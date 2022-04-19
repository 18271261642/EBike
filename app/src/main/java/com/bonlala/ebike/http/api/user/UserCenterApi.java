package com.bonlala.ebike.http.api.user;

import com.bonlala.ebike.http.api.CommApi;
import com.hjq.http.annotation.HttpHeader;

/**
 * 用户中心的api
 * Created by Admin
 * Date 2022/4/3
 */
public class UserCenterApi implements CommApi {
    @Override
    public Class<?> getObjBean() {
        return null;
    }

    @Override
    public String getApi() {
        return "app/user/center";
    }

    @HttpHeader
    private String language;

    @HttpHeader
    private String appToken;

    public UserCenterApi setHeadParams(String language,String toke){
        this.language = language;
        this.appToken = toke;
        return this;
    }


}

package com.bonlala.ebike.http.api.user;

import com.bonlala.ebike.http.api.CommApi;
import com.bonlala.ebike.http.net.ServerConstance;

/**
 * 登录的接口
 * Created by Admin
 * Date 2022/3/25
 */
public class LoginApi implements CommApi {

    private String apiUrl;

    private String getApiUrl() {
        return apiUrl;
    }

    private void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Override
    public String getApi() {
        return getApiUrl();
    }

    //登录类型 0 手机号 ；1邮箱
    private int loginType;

    //邮箱
    private String email;

    //手机号
    private String mobile;

    //验证码，登录时使用
    private int viaCode;



    public LoginApi getVerifyCode(int loginType,String typeAccount){
        setApiUrl(ServerConstance.GET_VERiFY_CODE);
        this.loginType = loginType;
        if(loginType == 0){
            this.mobile = typeAccount;
        }else{
            this.email = typeAccount;
        }
        return this;
    }


    //登录
    public LoginApi setLoginType(int loginType,String typeAccount,int code) {
        setApiUrl(ServerConstance.LOGIN_BY_TYPE);
        this.loginType = loginType;
        if(loginType == 0){
            this.mobile = typeAccount;
        }else{
            this.email = typeAccount;
        }

        this.viaCode = code;
        return this;
    }


    //获取用户信息
    public LoginApi getLoginInfo(){
        setApiUrl("app/user/info");
        return this;
    }



    @Override
    public Class<?> getObjBean() {
        return LoginTokenBean.class;
    }


    public class LoginTokenBean{

        private String appAuthToken;
        private Long registerTime;
        private Integer status;
        private Integer userId;

        public String getAppAuthToken() {
            return appAuthToken;
        }

        public void setAppAuthToken(String appAuthToken) {
            this.appAuthToken = appAuthToken;
        }

        public Long getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(Long registerTime) {
            this.registerTime = registerTime;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }


        @Override
        public String toString() {
            return "LoginTokenBean{" +
                    "appAuthToken='" + appAuthToken + '\'' +
                    ", registerTime=" + registerTime +
                    ", status=" + status +
                    ", userId=" + userId +
                    '}';
        }
    }
}

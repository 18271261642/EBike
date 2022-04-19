package com.bonlala.ebike.http.api.user;

import com.bonlala.ebike.http.api.CommApi;
import com.bonlala.ebike.http.api.device.DeviceApi;
import com.bonlala.ebike.http.api.device.DeviceVersionApi;

import androidx.lifecycle.LifecycleOwner;

/**
 * Created by Admin
 * Date 2022/3/29
 */
public class LoginPresent {

    private LoginView loginView;
    private LoginModel loginModel;

    public void attachView(LoginView loginView){
        this.loginView = loginView;
        loginModel = new LoginModel();
    }

    //获取验证码
    public void getLoginVerifyCode(LifecycleOwner lifecycleOwner, CommApi commApi,int code){
        loginModel.getVerifyCode(lifecycleOwner, commApi, new LoginView() {
            @Override
            public void not200CodeMsg(String msg) {
                if(loginView != null)
                    loginView.not200CodeMsg(msg);
            }

            @Override
            public void onSuccessData(Object data, int tagCode) {
                if(loginView != null)
                    loginView.onSuccessData(data,tagCode);
            }

            @Override
            public void onFailedData(String error) {

            }
        },code);
    }


    //登录
    public void loginByAccountType(LifecycleOwner lifecycleOwner,CommApi commApi,int code){
        loginModel.loginData(lifecycleOwner, commApi, new LoginView() {
            @Override
            public void not200CodeMsg(String msg) {
                if(loginView != null)
                    loginView.not200CodeMsg(msg);
            }

            @Override
            public void onSuccessData(Object data, int tagCode) {
                if(loginView != null)
                    loginView.onSuccessData(data,tagCode);
            }

            @Override
            public void onFailedData(String error) {

            }
        },code);
    }


    //获取登录信息
    public void getLoginInfoData(LifecycleOwner lifecycleOwner, GetLoginInfoApi commApi, int code){
        loginModel.getLoginData(lifecycleOwner, commApi, new LoginView() {
            @Override
            public void not200CodeMsg(String msg) {
                if(loginView != null)
                    loginView.not200CodeMsg(msg);
            }

            @Override
            public void onSuccessData(Object data, int tagCode) {
                if(loginView != null){
                    loginView.onSuccessData(data,tagCode);
                }
            }

            @Override
            public void onFailedData(String error) {
                if(loginView != null)
                    loginView.onFailedData(error);
            }
        },code);
    }


    //初始化用户信息，可以更改
    public void initPresentDefaultUserInfo(LifecycleOwner lifecycleOwner,JumpApi jumpApi,String str,int code){
        loginModel.initDefaultUseInfo(lifecycleOwner, jumpApi, str, new LoginView() {
            @Override
            public void not200CodeMsg(String msg) {
                loginView.not200CodeMsg(msg);
            }

            @Override
            public void onSuccessData(Object data, int tagCode) {
                loginView.onSuccessData(data,tagCode);
            }

            @Override
            public void onFailedData(String error) {
                loginView.onFailedData(error);
            }
        },code);
    }





    //更新用户个人信息
    public void updatePresentUserInfo(LifecycleOwner lifecycleOwner,GetLoginInfoApi getLoginInfoApi,String jsonStr,int code){
        loginModel.setUserInfoData(lifecycleOwner, getLoginInfoApi, jsonStr, new LoginView() {
            @Override
            public void not200CodeMsg(String msg) {
                if(loginView != null)
                    loginView.not200CodeMsg(msg);
            }

            @Override
            public void onSuccessData(Object data, int tagCode) {
                loginView.onSuccessData(data,tagCode);
            }

            @Override
            public void onFailedData(String error) {
                loginView.onFailedData(error);
            }
        },code);
    }





    //获取默认的用户登录信息
    public void getPresentDefaultUserInfo(LifecycleOwner lifecycleOwner,InitUserInfoApi initUserInfoApi,int code){
        loginModel.getDefaultUserData(lifecycleOwner, initUserInfoApi, new LoginView() {
            @Override
            public void not200CodeMsg(String msg) {
                if(loginView != null)
                    loginView.not200CodeMsg(msg);
            }

            @Override
            public void onSuccessData(Object data, int tagCode) {
                if(loginView != null)
                    loginView.onSuccessData(data,tagCode);
            }

            @Override
            public void onFailedData(String error) {

            }
        },code);
    }


    /**
     * 获取用户信息，用户中心
     * @param lifecycleOwner
     * @param userCenterApi
     * @param code
     */
    public void getPresentUserCenterData(LifecycleOwner lifecycleOwner,UserCenterApi userCenterApi,int code){
        loginModel.getUserCenterInfo(lifecycleOwner, userCenterApi, new LoginView() {
            @Override
            public void not200CodeMsg(String msg) {

            }

            @Override
            public void onSuccessData(Object data, int tagCode) {

            }

            @Override
            public void onFailedData(String error) {

            }
        },code);
    }


    //获取图片上传oss token
    public void getPresentAliOssToken(LifecycleOwner lifecycleOwner,GetOssApi getOssApi,int code){
        loginModel.getOssToken(lifecycleOwner, getOssApi, new LoginView() {
            @Override
            public void not200CodeMsg(String msg) {
                if(loginView != null){
                    loginView.not200CodeMsg(msg);
                }
            }

            @Override
            public void onSuccessData(Object data, int tagCode) {
                if(loginView != null){
                    loginView.onSuccessData(data,tagCode);
                }
            }

            @Override
            public void onFailedData(String error) {
                if(loginView != null){
                    loginView.onFailedData(error);
                }
            }
        },code);
    }


    //获取固件版本
    public void getPresentDeviceVersion(LifecycleOwner lifecycleOwner, DeviceVersionApi deviceVersionApi,int code){
        loginModel.getDeviceOtaVersion(lifecycleOwner, deviceVersionApi, new LoginView() {
            @Override
            public void not200CodeMsg(String msg) {
                if(loginView != null){
                    loginView.not200CodeMsg(msg);
                }
            }

            @Override
            public void onSuccessData(Object data, int tagCode) {
                if(loginView != null){
                    loginView.onSuccessData(data,tagCode);
                }
            }

            @Override
            public void onFailedData(String error) {
                if(loginView != null){
                    loginView.onFailedData(error);
                }
            }
        },code);
    }

    //绑定或解绑设备
    public void bindAndUnBindDevice(LifecycleOwner lifecycleOwner, DeviceApi deviceApi,String str,int code){
        loginModel.bindAndUnBindDevice(lifecycleOwner, deviceApi,str,new LoginView() {
            @Override
            public void not200CodeMsg(String msg) {
                if(loginView != null){
                    loginView.not200CodeMsg(msg);
                }
            }

            @Override
            public void onSuccessData(Object data, int tagCode) {
                if(loginView != null){
                    loginView.onSuccessData(data,tagCode);
                }
            }

            @Override
            public void onFailedData(String error) {
                if(loginView != null){
                    loginView.onFailedData(error);
                }
            }
        },code);
    }

    //获取用户绑定的设备列表
    public void getPresentUserBindDevice(LifecycleOwner lifecycleOwner,DeviceApi deviceApi,int code){
        loginModel.getUserBindList(lifecycleOwner,deviceApi, code,new LoginView() {
            @Override
            public void onSuccessData(Object data, int tagCode) {
                loginView.onSuccessData(data,tagCode);
            }

            @Override
            public void onFailedData(String error) {
                loginView.onFailedData(error);
            }

            @Override
            public void not200CodeMsg(String msg) {
                loginView.not200CodeMsg(msg);
                }
        });
    }

    //获取累加信息
    public void getPresentHomeTotal(LifecycleOwner lifecycleOwner,UserHomeApi userHomeApi,int code){
        loginModel.getHomeViewTotalData(lifecycleOwner, userHomeApi, code, new LoginView() {
            @Override
            public void not200CodeMsg(String msg) {
                loginView.not200CodeMsg(msg);
            }

            @Override
            public void onSuccessData(Object data, int tagCode) {
                loginView.onSuccessData(data,tagCode);
            }

            @Override
            public void onFailedData(String error) {
                loginView.onFailedData(error);
            }
        });
    }
}

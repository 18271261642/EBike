package com.bonlala.ebike.http.api.user;

import android.util.Log;

import com.bonlala.ebike.http.HttpData;
import com.bonlala.ebike.http.RequestServer;
import com.bonlala.ebike.http.api.CommApi;
import com.bonlala.ebike.http.api.device.DeviceApi;
import com.bonlala.ebike.http.api.device.DeviceVersionApi;
import com.bonlala.ebike.utils.MmkvUtils;
import com.google.gson.Gson;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.http.listener.OnUpdateListener;
import com.hjq.http.model.BodyType;

import org.json.JSONObject;

import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import timber.log.Timber;

/**
 * Created by Admin
 * Date 2022/3/29
 */
public class LoginModel<T> {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //获取验证码
    public void getVerifyCode(LifecycleOwner lifecycleOwner, CommApi commApi, LoginView loginView, int code) {
        EasyHttp.post(lifecycleOwner).api(commApi).request(new OnHttpListener<HttpData<String>>() {
            @Override
            public void onSucceed(HttpData<String> result) {
                if (loginView != null){
                    if(result.isNo200Code()){
                        loginView.not200CodeMsg(result.getMessage());
                    }
                    if(result.isRequestSucceed()){
                        loginView.onSuccessData(result.getMessage(), code);
                    }

                }
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }


    //账号和验证码登录
    public void loginData(LifecycleOwner lifecycleOwner, CommApi commApi, LoginView loginView, int code) {
        EasyHttp.post(lifecycleOwner).api(commApi).request(new OnHttpListener<HttpData<LoginApi.LoginTokenBean>>() {

            @Override
            public void onSucceed(HttpData<LoginApi.LoginTokenBean> result) {
                if (loginView != null)
                    loginView.onSuccessData(result.isSuccess() && result.isRequestSucceed() ? result.getData() : null, code);
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }


    //获取登录的用户信息
    public void getLoginData(LifecycleOwner lifecycleOwner, GetLoginInfoApi commApi, LoginView loginView, int tag) {
//        EasyConfig.getInstance().addHeader("Accept-Language","CN").setServer(new RequestServer(BodyType.FORM))
//                .addHeader("Bearer","6dd86396-9501-46b1-b340-bde45ad0c1f9.18974").into();

        String tokenStr = (String) MmkvUtils.getSaveParams(MmkvUtils.TOKEN_KEY, "");
        EasyHttp.get(lifecycleOwner).api(commApi.setParams("CN", "Bearer " + tokenStr))
                .request(new OnHttpListener<HttpData<GetLoginInfoApi.UserInfoData>>() {

                    @Override
                    public void onSucceed(HttpData<GetLoginInfoApi.UserInfoData> result) {
                        Timber.e("----result="+new Gson().toJson(result));
                        if(loginView != null){
                            if(result.isNo200Code()){
                              loginView.not200CodeMsg(result.getMessage());
                            }
                            if(result.isRequestSucceed()){
                                loginView.onSuccessData(result.getData(),tag);
                            }

                        }

                    }

                    @Override
                    public void onFail(Exception e) {
                        loginView.onFailedData(e.getMessage());
                    }
                });
    }




    //初始化默认的用户信息
    public void initDefaultUseInfo(LifecycleOwner lifecycleOwner,JumpApi jumpApi,String jsonStr,LoginView loginView,int code){
        RequestBody requestBody = RequestBody.create(JSON,jsonStr);

        EasyHttp.post(lifecycleOwner)
                .api(jumpApi.setDefaultApi())
                .body(requestBody)
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if(jsonObject.getBoolean("success") && jsonObject.getString("code").equals("200")){
                                loginView.onSuccessData("",code);
                            }else{
                                loginView.not200CodeMsg(jsonObject.getString("msg"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        loginView.onFailedData(e.getMessage());
                    }
                });
    }




    //设置用户信息
    public void setUserInfoData(LifecycleOwner lifecycleOwner, GetLoginInfoApi getLoginInfoApi, String jsonStr,LoginView loginView, int code){

        Timber.e("----提交参数="+jsonStr);

        RequestBody requestBody = RequestBody.create(JSON,jsonStr);

        EasyHttp.put(lifecycleOwner)
                .api(getLoginInfoApi.putUserInfo())
                .body(requestBody)
                .request(new OnHttpListener<HttpData<String>>() {

                    @Override
                    public void onSucceed(HttpData<String> result) {
                        if(loginView != null){
                            if(result.isRequestSucceed()){
                                loginView.onSuccessData(result.getData(), code);
                            }

                            if(result.isNo200Code()){
                                loginView.not200CodeMsg(result.getMessage());

                            }

                        }
                    }

                    @Override
            public void onFail(Exception e) {
                loginView.onFailedData(e.getMessage());
            }
        });
    }







    public void getDefaultUserData(LifecycleOwner lifecycleOwner,InitUserInfoApi initUserInfoApi,LoginView loginView,int tagCode){
        String tokenStr = (String) MmkvUtils.getSaveParams(MmkvUtils.TOKEN_KEY, "");
        EasyHttp.get(lifecycleOwner).api(initUserInfoApi.getDefaultUser("Bearer " + tokenStr,"CN"))
                .request(new OnHttpListener<HttpData<InitUserInfoApi.UserInfoData>>() {

                    @Override
                    public void onSucceed(HttpData<InitUserInfoApi.UserInfoData> result) {
                        if(loginView != null){
                            if(result.isRequestSucceed()){
                                loginView.onSuccessData(result.getData(),tagCode);
                            }

                            if(result.isNo200Code()){
                                loginView.not200CodeMsg(result.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                });
    }


    //用户中心，获取用户信息
    public void getUserCenterInfo(LifecycleOwner lifecycleOwner,UserCenterApi userCenterApi,LoginView loginView,int code){
        String tokenStr = (String) MmkvUtils.getSaveParams(MmkvUtils.TOKEN_KEY, "");
        EasyHttp.get(lifecycleOwner).api(userCenterApi.setHeadParams("CN","Bearer " + tokenStr)).request(new OnHttpListener<HttpData<JSONObject>>() {
            @Override
            public void onSucceed(HttpData<JSONObject> result) {

            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }



    //获取oss Token
    public void getOssToken(LifecycleOwner lifecycleOwner,GetOssApi ossApi,LoginView loginView,int code){
        String tokenStr = (String) MmkvUtils.getSaveParams(MmkvUtils.TOKEN_KEY, "");
        EasyHttp.get(lifecycleOwner).api(ossApi.getOssToke("CN","Bearer " + tokenStr)).request(new OnHttpListener<HttpData<GetOssApi.OssTokenBean>>() {


            @Override
            public void onSucceed(HttpData<GetOssApi.OssTokenBean> result) {
                if(loginView != null){
                    if(result.isRequestSucceed()){
                        loginView.onSuccessData(result.getData(),code);
                    }

                    if(result.isNo200Code()){
                        loginView.not200CodeMsg(result.getMessage());
                    }
                }

            }

            @Override
            public void onFail(Exception e) {
                if(loginView != null)
                    loginView.onFailedData(e.getMessage());
            }
        });
    }


    //获取设备固件版本信息
    public void getDeviceOtaVersion(LifecycleOwner lifecycleOwner, DeviceVersionApi deviceVersionApi,LoginView loginView,int code){

        RequestServer requestServer = new RequestServer(BodyType.FORM);
        EasyConfig.getInstance().setServer(requestServer).into();
        EasyHttp.get(lifecycleOwner).api(deviceVersionApi.getDeviceVersionApi()).request(new OnHttpListener<HttpData<DeviceVersionApi.DeviceVersionInfo>>() {


            @Override
            public void onSucceed(HttpData<DeviceVersionApi.DeviceVersionInfo> result) {
                if(loginView != null){
                    if(result.isRequestSucceed()){
                        loginView.onSuccessData(result.getData(),code);
                    }

                    if(result.isNo200Code()){
                        loginView.not200CodeMsg(result.getMessage());
                    }
                }
            }

            @Override
            public void onFail(Exception e) {
                if(loginView != null)
                    loginView.onFailedData(e.getMessage());
            }
        });
    }


    //绑定设备或解绑设备
    public void bindAndUnBindDevice(LifecycleOwner lifecycleOwner, DeviceApi deviceApi,String str,LoginView loginView,int code){
        RequestServer requestServer = new RequestServer(BodyType.JSON);
        EasyConfig.getInstance().setServer(requestServer).into();

        RequestBody requestBody = RequestBody.create(JSON,str);

        EasyHttp.post(lifecycleOwner)
                .body(requestBody)
                .api(deviceApi).request(new OnHttpListener<String>() {

            @Override
            public void onSucceed(String result) {
                try {
                  if(result == null)
                      return;
                  HttpData httpData = new Gson().fromJson(result,HttpData.class);
                    if(loginView != null){
                        if(httpData.isRequestSucceed()){
                            loginView.onSuccessData("绑定成功",code);
                        }

                        if(httpData.isNo200Code()){
                            loginView.not200CodeMsg(httpData.getMessage());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(Exception e) {
                if(loginView != null)
                    loginView.onFailedData(e.getMessage());
            }
        });
    }


    //获取绑定的设备列表
    public void getUserBindList(LifecycleOwner lifecycleOwner,DeviceApi deviceApi,int code,LoginView loginView){
        EasyHttp.get(lifecycleOwner).api(deviceApi).request(new OnHttpListener<HttpData<List<DeviceApi.UserBindDevice>>>() {
            @Override
            public void onSucceed(HttpData<List<DeviceApi.UserBindDevice>> result) {
                if(loginView != null){
                    if(result.isRequestSucceed()){
                        loginView.onSuccessData(result.getData(),code);
                    }

                    if(result.isNo200Code()){
                        loginView.not200CodeMsg(result.getMessage());
                    }
                }

            }

            @Override
            public void onFail(Exception e) {
                loginView.onFailedData(e.getMessage());
            }
        });
    }



    //首页获取累计的数据
    public void getHomeViewTotalData(LifecycleOwner lifecycleOwner,UserHomeApi userHomeApi,int code,LoginView loginView){
        EasyHttp.get(lifecycleOwner).api(userHomeApi.getApi()).request(new OnHttpListener<HttpData<UserHomeApi.HomeTotalBean>>() {


            @Override
            public void onSucceed(HttpData<UserHomeApi.HomeTotalBean> result) {
                if(loginView != null){
                    if(result.isSuccess()){
                        loginView.onSuccessData(result.getData(),code);

                    }
                    if(result.isNo200Code()){
                        loginView.not200CodeMsg(result.getMessage());
                    }
                }
            }



            @Override
            public void onFail(Exception e) {
                loginView.onFailedData(e.getMessage());
            }
        });
    }

}

package com.bonlala.ebike.http.net;

/**
 * Created by Admin
 * Date 2022/3/29
 */
public class ServerConstance {

    //获取验证码
    public static final String GET_VERiFY_CODE = "app/login/sendViaCode";

    //登录
    public static final String LOGIN_BY_TYPE = "app/login/viaLogin";

    //更新用户信息url
    public static final String UPDATE_USER_INFO = "app/user/updateInfo";
    //获取用户信息
    public static final String GET_USER_INFO = "app/user/info";


    //绑定设备
    public static final String BIND_DEVICE= "app/device/bind";
    //解绑设备
    public static final String UNBIND_DEVICE = "app/device/unbind";
    //查询已绑定设备列表
    public static final String GET_BIND_DEVICE_LIST = "app/device/list";


}

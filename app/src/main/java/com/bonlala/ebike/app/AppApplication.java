package com.bonlala.ebike.app;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.IBinder;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.bonlala.blelibrary.BleApplication;
import com.bonlala.blelibrary.BleOperateManager;
import com.bonlala.ebike.R;
import com.bonlala.ebike.http.GlideApp;
import com.bonlala.ebike.http.RequestHandler;
import com.bonlala.ebike.http.RequestServer;
import com.bonlala.ebike.http.api.device.DeviceApi;
import com.bonlala.ebike.manager.ActivityManager;
import com.bonlala.ebike.other.AppConfig;
import com.bonlala.ebike.other.DebugLoggerTree;
import com.bonlala.ebike.other.SmartBallPulseFooter;
import com.bonlala.ebike.other.TitleBarStyle;
import com.bonlala.ebike.other.ToastLogInterceptor;
import com.bonlala.ebike.other.ToastStyle;
import com.bonlala.ebike.ui.amap.BNInitHelper;
import com.bonlala.ebike.ui.amap.LocationService;
import com.bonlala.ebike.ui.ble.ConnectStatusService;
import com.bonlala.ebike.utils.BikeUtils;
import com.bonlala.ebike.utils.MmkvUtils;
import com.hjq.bar.TitleBar;
import com.hjq.gson.factory.GsonFactory;
import com.hjq.http.EasyConfig;
import com.hjq.http.config.IRequestInterceptor;
import com.hjq.http.model.BodyType;
import com.hjq.http.model.HttpHeaders;
import com.hjq.http.model.HttpParams;
import com.hjq.http.request.HttpRequest;
import com.hjq.toast.ToastUtils;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;
import java.util.HashMap;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 应用入口
 */
public final class AppApplication extends BleApplication {


    private static AppApplication appApplication;

    //用户绑定的设备信息
    public static HashMap<String, DeviceApi.UserBindDevice> userBindDeviceHashMap = new HashMap<>();
    //已经连接过的设备mac
    public static String connDeviceMac;


    //用户判断日期选择的背景是否为白色背景，在DateDialog初始化之前设置值,
    private boolean isDateSelectWhite = false;

    //是否连接成功
    private boolean isConnectStatus = false;

    //是否是本地环境
    private boolean isLocalService = false;

    //连接状态的service
    private static ConnectStatusService connectStatusService;

    private static BleOperateManager bleOperateManager;

    private static LocationService locationService;

    public static AppApplication getInstance(){
        return appApplication;

    }

    //@Log("启动耗时")
    @Override
    public void onCreate() {
        super.onCreate();
        appApplication = this;

        initSdk(this);
    }


    public static LocationService getLocationService(){
        if(locationService == null)
            locationService = new LocationService(appApplication);

        return locationService;
    }


    public static BleOperateManager getBleOperateManager(){
        if(bleOperateManager == null)
            bleOperateManager = BleOperateManager.getInstance();
        return bleOperateManager;
    }


    public  boolean isConnectStatus() {
        return isConnectStatus;
    }

    public  void setConnectStatus(boolean connectStatus) {
        isConnectStatus = connectStatus;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 清理所有图片内存缓存
        GlideApp.get(this).onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // 根据手机内存剩余情况清理图片内存缓存
        GlideApp.get(this).onTrimMemory(level);
    }


    public static void setHttpToken(){
        String tokenStr = (String) MmkvUtils.getSaveParams(MmkvUtils.TOKEN_KEY,"");
        if(!BikeUtils.isEmpty(tokenStr) ){
            EasyConfig.getInstance().addHeader("appToken","Bearer "+tokenStr).into();
        }
    }


    /**
     * 初始化一些第三方框架
     */
    public static void initSdk(Application application) {

        BNInitHelper bnInitHelper = new BNInitHelper(application);
        bnInitHelper.init();

//        SDKInitializer.setAgreePrivacy(application, true);
//        //百度地图初始化
//        SDKInitializer.setApiKey("pvQUG8uflebXXsFzA83c0LIuIpxX1nzp");
//        SDKInitializer.setHttpsEnable(true);
//
//        SDKInitializer.setCoordType(CoordType.BD09LL);
//        SDKInitializer.initialize(application);

        // 设置标题栏初始化器
        TitleBar.setDefaultStyle(new TitleBarStyle());

        // 设置全局的 Header 构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((cx, layout) ->
                new MaterialHeader(application).setColorSchemeColors(ContextCompat.getColor(application, R.color.common_accent_color)));
        // 设置全局的 Footer 构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((cx, layout) -> new SmartBallPulseFooter(application));
        // 设置全局初始化器
        SmartRefreshLayout.setDefaultRefreshInitializer((cx, layout) -> {
            // 刷新头部是否跟随内容偏移
            layout.setEnableHeaderTranslationContent(true)
                    // 刷新尾部是否跟随内容偏移
                    .setEnableFooterTranslationContent(true)
                    // 加载更多是否跟随内容偏移
                    .setEnableFooterFollowWhenNoMoreData(true)
                    // 内容不满一页时是否可以上拉加载更多
                    .setEnableLoadMoreWhenContentNotFull(false)
                    // 仿苹果越界效果开关
                    .setEnableOverScrollDrag(false);
        });

        // 初始化吐司
        ToastUtils.init(application, new ToastStyle());
        // 设置调试模式
        ToastUtils.setDebugMode(AppConfig.isDebug());
        // 设置 Toast 拦截器
        ToastUtils.setInterceptor(new ToastLogInterceptor());

//        // 本地异常捕捉
//        CrashHandler.register(application);
//
//        // 友盟统计、登录、分享 SDK
//        UmengClient.init(application, AppConfig.isLogEnable());

        // Bugly 异常捕捉
        CrashReport.initCrashReport(application, AppConfig.getBuglyId(), AppConfig.isDebug());

        // Activity 栈管理初始化
        ActivityManager.getInstance().init(application);

        // MMKV 初始化
        MMKV.initialize(application);
        MmkvUtils.initMkv();

        // 网络请求框架初始化
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        EasyConfig.with(okHttpClient)
                // 是否打印日志
                .setLogEnabled(AppConfig.isLogEnable())
                // 设置服务器配置
                .setServer(new RequestServer(BodyType.JSON))
                // 设置请求处理策略
                .setHandler(new RequestHandler(application))
                // 设置请求重试次数
                .setRetryCount(1)
                .setInterceptor(new IRequestInterceptor() {
                    @Override
                    public void interceptArguments(@NonNull HttpRequest<?> httpRequest,
                                                   @NonNull HttpParams params,
                                                   @NonNull HttpHeaders headers) {
                        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
                    }
                })
                .into();

        setHttpToken();

        // 设置 Json 解析容错监听
        GsonFactory.setJsonCallback((typeToken, fieldName, jsonToken) -> {
            // 上报到 Bugly 错误列表
            CrashReport.postCatchedException(new IllegalArgumentException(
                    "类型解析异常：" + typeToken + "#" + fieldName + "，后台返回的类型为：" + jsonToken));
        });

        // 初始化日志打印
        if (AppConfig.isLogEnable()) {
            Timber.plant(new DebugLoggerTree());
        }

        // 注册网络状态变化监听
        ConnectivityManager connectivityManager = ContextCompat.getSystemService(application, ConnectivityManager.class);
        if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onLost(@NonNull Network network) {
                    Activity topActivity = ActivityManager.getInstance().getTopActivity();
                    if (!(topActivity instanceof LifecycleOwner)) {
                        return;
                    }

                    LifecycleOwner lifecycleOwner = ((LifecycleOwner) topActivity);
                    if (lifecycleOwner.getLifecycle().getCurrentState() != Lifecycle.State.RESUMED) {
                        return;
                    }

                    ToastUtils.show(R.string.common_network_error);
                }
            });
        }



        bindConnService();
    }

    private static void bindConnService(){
        Intent intent = new Intent(appApplication, ConnectStatusService.class);
        appApplication.bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }


    public ConnectStatusService getConnectStatusService(){
        if(connectStatusService == null){
            bindConnService();
        }

        return connectStatusService;

    }

    public boolean isDateSelectWhite() {
        return isDateSelectWhite;
    }

    public void setDateSelectWhite(boolean dateSelectWhite) {
        isDateSelectWhite = dateSelectWhite;
    }

    private static final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                connectStatusService = ((ConnectStatusService.CBikeServiceBinder) iBinder).getService();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            connectStatusService = null;
        }
    };

    public boolean isLocalService() {
        return isLocalService;
    }

    public void setLocalService(boolean localService) {
        isLocalService = localService;
    }
}
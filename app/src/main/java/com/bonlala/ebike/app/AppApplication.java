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
 *    author : Android ?????????
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : ????????????
 */
public final class AppApplication extends BleApplication {


    private static AppApplication appApplication;

    //???????????????????????????
    public static HashMap<String, DeviceApi.UserBindDevice> userBindDeviceHashMap = new HashMap<>();
    //????????????????????????mac
    public static String connDeviceMac;


    //????????????????????????????????????????????????????????????DateDialog????????????????????????,
    private boolean isDateSelectWhite = false;

    //??????????????????
    private boolean isConnectStatus = false;

    //?????????????????????
    private boolean isLocalService = false;

    //???????????????service
    private static ConnectStatusService connectStatusService;

    private static BleOperateManager bleOperateManager;

    private static LocationService locationService;

    public static AppApplication getInstance(){
        return appApplication;

    }

    //@Log("????????????")
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
        // ??????????????????????????????
        GlideApp.get(this).onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // ??????????????????????????????????????????????????????
        GlideApp.get(this).onTrimMemory(level);
    }


    public static void setHttpToken(){
        String tokenStr = (String) MmkvUtils.getSaveParams(MmkvUtils.TOKEN_KEY,"");
        if(!BikeUtils.isEmpty(tokenStr) ){
            EasyConfig.getInstance().addHeader("appToken","Bearer "+tokenStr).into();
        }
    }


    /**
     * ??????????????????????????????
     */
    public static void initSdk(Application application) {

        BNInitHelper bnInitHelper = new BNInitHelper(application);
        bnInitHelper.init();

//        SDKInitializer.setAgreePrivacy(application, true);
//        //?????????????????????
//        SDKInitializer.setApiKey("pvQUG8uflebXXsFzA83c0LIuIpxX1nzp");
//        SDKInitializer.setHttpsEnable(true);
//
//        SDKInitializer.setCoordType(CoordType.BD09LL);
//        SDKInitializer.initialize(application);

        // ???????????????????????????
        TitleBar.setDefaultStyle(new TitleBarStyle());

        // ??????????????? Header ?????????
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((cx, layout) ->
                new MaterialHeader(application).setColorSchemeColors(ContextCompat.getColor(application, R.color.common_accent_color)));
        // ??????????????? Footer ?????????
        SmartRefreshLayout.setDefaultRefreshFooterCreator((cx, layout) -> new SmartBallPulseFooter(application));
        // ????????????????????????
        SmartRefreshLayout.setDefaultRefreshInitializer((cx, layout) -> {
            // ????????????????????????????????????
            layout.setEnableHeaderTranslationContent(true)
                    // ????????????????????????????????????
                    .setEnableFooterTranslationContent(true)
                    // ????????????????????????????????????
                    .setEnableFooterFollowWhenNoMoreData(true)
                    // ???????????????????????????????????????????????????
                    .setEnableLoadMoreWhenContentNotFull(false)
                    // ???????????????????????????
                    .setEnableOverScrollDrag(false);
        });

        // ???????????????
        ToastUtils.init(application, new ToastStyle());
        // ??????????????????
        ToastUtils.setDebugMode(AppConfig.isDebug());
        // ?????? Toast ?????????
        ToastUtils.setInterceptor(new ToastLogInterceptor());

//        // ??????????????????
//        CrashHandler.register(application);
//
//        // ?????????????????????????????? SDK
//        UmengClient.init(application, AppConfig.isLogEnable());

        // Bugly ????????????
        CrashReport.initCrashReport(application, AppConfig.getBuglyId(), AppConfig.isDebug());

        // Activity ??????????????????
        ActivityManager.getInstance().init(application);

        // MMKV ?????????
        MMKV.initialize(application);
        MmkvUtils.initMkv();

        // ???????????????????????????
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        EasyConfig.with(okHttpClient)
                // ??????????????????
                .setLogEnabled(AppConfig.isLogEnable())
                // ?????????????????????
                .setServer(new RequestServer(BodyType.JSON))
                // ????????????????????????
                .setHandler(new RequestHandler(application))
                // ????????????????????????
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

        // ?????? Json ??????????????????
        GsonFactory.setJsonCallback((typeToken, fieldName, jsonToken) -> {
            // ????????? Bugly ????????????
            CrashReport.postCatchedException(new IllegalArgumentException(
                    "?????????????????????" + typeToken + "#" + fieldName + "??????????????????????????????" + jsonToken));
        });

        // ?????????????????????
        if (AppConfig.isLogEnable()) {
            Timber.plant(new DebugLoggerTree());
        }

        // ??????????????????????????????
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
package com.bonlala.ebike.ui.amap;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.baidu.navisdk.adapter.struct.BNTTsInitConfig;
import com.baidu.navisdk.adapter.struct.BNaviInitConfig;

import java.io.File;

public class BNInitHelper {
    public static final String TAG = "EBike";
    public static final String APP_FOLDER_NAME = TAG;

    private final Context mContext;

    public BNInitHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    public void init() {
        initSdk();
        initNavi();
    }

    public void initSdk() {
        SDKInitializer.setAgreePrivacy(mContext, true);
        SDKInitializer.initialize(mContext);
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    public void initNavi() {
        // 针对单次有效的地图设置项 - DemoNaviSettingActivity


        if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
            return;
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().enableOutLog(true);

        BNaviInitConfig bNaviInitConfig = new BNaviInitConfig.Builder()
                .sdcardRootPath(mContext.getExternalFilesDir(null).getPath())
                .appFolderName(APP_FOLDER_NAME)
                .naviInitListener(new IBaiduNaviManager.INaviInitListener() {
                                      @Override
                                      public void onAuthResult(int status, String msg) {
                                          String result;
                                          if (0 == status) {
                                              result = "key校验成功!";
                                          } else {
                                              result = "key校验失败, " + msg;
                                          }
                                          Log.e(TAG, result);
                                      }

                                      @Override
                                      public void initStart() {
                                          Log.e(TAG, "initStart");
                                      }

                                      @Override
                                      public void initSuccess() {
                                          Log.e(TAG, "initSuccess");
                                          String cuid = BaiduNaviManagerFactory.getBaiduNaviManager().getCUID();
                                          Log.e(TAG, "cuid = " + cuid);
                                          // 初始化tts
                                          initTTS();
                                          mContext.sendBroadcast(new Intent("com.navi.ready"));
                                      }

                                      @Override
                                      public void initFailed(int i) {
                                          Log.e(TAG, "initFailed-" + i);
                                      }
                                  }
                ).build();
        BaiduNaviManagerFactory.getBaiduNaviManager().init(mContext,bNaviInitConfig);
    }

    private void initTTS() {
        // 使用内置TTS
        BNTTsInitConfig config = new BNTTsInitConfig.Builder()
                .context(mContext)
                .sdcardRootPath(getSdcardDir())
                .appFolderName(APP_FOLDER_NAME)
//                .appId(BNDemoUtils.getTTSAppID())
//                .appKey(BNDemoUtils.getTTSAppKey())
//                .secretKey(BNDemoUtils.getTTSsecretKey())
                .authSn("8092f102-684cde5d-01-0050-006d-0091-01")
                .build();
        BaiduNaviManagerFactory.getTTSManager().initTTS(config);
    }

    private String getSdcardDir() {
        if (Build.VERSION.SDK_INT >= 29) {
            // 如果外部储存可用 ,获得外部存储路径
            File file = mContext.getExternalFilesDir(null);
            if (file != null && file.exists()) {
                return file.getPath();
            } else {
                return mContext.getFilesDir().getPath();
            }
        } else {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

}
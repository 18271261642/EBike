package com.bonlala.ebike.ui.amap;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRouteGuideManager;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBNaviListener;
import com.baidu.navisdk.adapter.IBNaviViewListener;
import com.baidu.navisdk.adapter.struct.BNGuideConfig;
import com.baidu.navisdk.adapter.struct.BNHighwayInfo;
import com.baidu.navisdk.adapter.struct.BNRoadCondition;
import com.baidu.navisdk.adapter.struct.BNavLineItem;
import com.baidu.navisdk.adapter.struct.BNaviInfo;
import com.baidu.navisdk.adapter.struct.BNaviLocation;
import com.baidu.navisdk.adapter.struct.BNaviResultInfo;
import com.baidu.navisdk.adapter.struct.RoadEventItem;
import com.bonlala.ebike.R;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import timber.log.Timber;

/**
 * 摩托导航
 * Created by Admin
 * Date 2022/4/16
 */
public class BdMotoNaviActivity extends FragmentActivity {

    private static final String TAG = "BdMotoNaviActivity";

    private FrameLayout bmMapViewLayout;

    private IBNRouteGuideManager mRouteGuideManager;
    private GuidePop guidePop;

    private int curRemainDistance;
    private int overSpeedDistance = -1;

    View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRouteGuideManager = BaiduNaviManagerFactory.getRouteGuideManager();

        Bundle params = new Bundle();
        params.putInt(BNaviCommonParams.RoutePlanKey.VEHICLE_TYPE, IBNRoutePlanManager.Vehicle.MOTOR);


        BNGuideConfig config = new BNGuideConfig.Builder()
                .params(params)
                .build();
        View parentView = LayoutInflater.from(this).inflate(R.layout.activity_bd_navi_layout, null, true);
        bmMapViewLayout = parentView.findViewById(R.id.bmMapViewLayout);
        view = mRouteGuideManager.onCreate(this, config);
        if (view == null)
            return;
        bmMapViewLayout.addView(view);

        setContentView(parentView);

        guidePop = new GuidePop(this);
        initTTSListener();
        routeGuideEvent();
    }

    private void initTTSListener() {
        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Timber.e("ttsCallback.onPlayStart");
                        ControlBoardWindow.getInstance().showControl("ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayEnd");
                        ControlBoardWindow.getInstance().showControl("ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayError");
                        ControlBoardWindow.getInstance().showControl("ttsCallback.onPlayError");
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.e("BNSDKDemo", "ttsHandler.msg.what=" + msg.what);
                        ControlBoardWindow.getInstance()
                                .showControl("ttsHandler.msg.what=" + msg.what);
                    }
                }
        );
    }

    private void unInitTTSListener() {
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(null);
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(null);

    }


    // 导航过程事件监听
    private void routeGuideEvent() {




        BaiduNaviManagerFactory.getRouteGuideManager().setNaviListener(new IBNaviListener() {

            @Override
            public void onRoadNameUpdate(String name) { // 弹窗展示
                Timber.e("-----当前路名更新=%s", name);
                ControlBoardWindow.getInstance().showControl("当前路名更新——" + name);
                guidePop.setdata(name);
            }

            @Override
            public void onRemainInfoUpdate(int remainDistance, int remainTime) { // 弹窗展示
                Timber.e("-----\"距离目的地的剩余=%s", remainDistance+"m "+remainTime+"s");
                ControlBoardWindow.getInstance()
                        .showControl("距离目的地的剩余——" + remainDistance + "m " + remainTime + "s");
                guidePop.setdata(remainDistance, remainTime);
                curRemainDistance = remainDistance;
            }

            @Override
            public void onViaListRemainInfoUpdate(Message msg) {

            }

            @Override
            public void onGuideInfoUpdate(BNaviInfo naviInfo) { // 弹窗展示
                Timber.e("-----诱导信息=%s", naviInfo.toString());
                ControlBoardWindow.getInstance().showControl("诱导信息——" + naviInfo.toString());
                guidePop.setdata(naviInfo);
            }

            @Override
            public void onHighWayInfoUpdate(Action action, BNHighwayInfo info) {
                ControlBoardWindow.getInstance()
                        .showControl("高速信息——" + action + " " + info.toString());
            }

            @Override
            public void onFastExitWayInfoUpdate(Action action, String name, int dist, String id) {
                Timber.e("-----快速路出口信息="+action + " " + name + " 出口还有" + dist + "米");
                ControlBoardWindow.getInstance()
                        .showControl("快速路出口信息——" + action + " " + name + " 出口还有" + dist + "米");
            }

            @Override
            public void onEnlargeMapUpdate(Action action, View enlargeMap, String remainDistance,
                                           int progress, String roadName, Bitmap turnIcon) {
                ControlBoardWindow.getInstance().showControl("放大图回调信息");
            }

            @Override
            public void onDayNightChanged(DayNightMode style) {
                ControlBoardWindow.getInstance().showControl("日夜更替信息回调" + style);
            }

            @Override
            public void onRoadConditionInfoUpdate(double progress, List<BNRoadCondition> items) {
                Timber.e("-----路况信息更新 进度：=%s", progress );
                ControlBoardWindow.getInstance().showControl("路况信息更新 进度：" + progress + " 路况：。。。");
            }

            @Override
            public void onMainSideBridgeUpdate(int type) {
                Timber.e("-----主辅路、高架桥信息更新=%s", type );
                ControlBoardWindow.getInstance().showControl("主辅路、高架桥信息更新:" + type + " 意义不明？？—gb");
            }

            @Override
            public void onLaneInfoUpdate(Action action, List<BNavLineItem> laneItems) {
                if (laneItems != null && laneItems.size() > 0) {
                    Timber.e("-----车道线信息更新=%s", action + laneItems.get(0).toString() );
                    ControlBoardWindow.getInstance().showControl("车道线信息更新:" +
                            action + laneItems.get(0).toString() + " ...");
                }
            }

            @Override
            public void onSpeedUpdate(int speed, int overSpeed) {

//                ControlBoardWindow.getInstance()
//                        .showControl("当前车速：" + speed + "限速：" + overSpeed + " 是否超速：" + (speed > overSpeed) );
//                if (overSpeed == -1) {
//                    return;
//                }
//                if (speed > overSpeed && ( (overSpeedDistance == -1 || overSpeedDistance - curRemainDistance > 1000) ) ) {
//                    // 1km内超速回调一次，偏航重置
//                    ControlBoardWindow.getInstance()
//                            .showControl("超速回调！！！");
//                    Toast.makeText(DemoGuideActivity.this, "超速回调！！！",
//                            Toast.LENGTH_SHORT).show();
//                    overSpeedDistance = curRemainDistance;
//                }
            }

            @Override
            public void onOverSpeed(int speed, int speedLimit) {
                Toast.makeText(BdMotoNaviActivity.this, "超速回调！！！",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onArriveDestination() {

                BNaviResultInfo info =
                        BaiduNaviManagerFactory.getRouteGuideManager().getNaviResultInfo();

                Timber.e("----导航结算数据=%s",info.toString());
                Toast.makeText(BdMotoNaviActivity.this, "导航结算数据: " + info.toString(),
                        Toast.LENGTH_SHORT).show();
                ControlBoardWindow.getInstance().showControl("抵达目的地：" + info.toString());
            }

            @Override
            public void onArrivedWayPoint(int index) {
                Timber.e("----到达途径点=%s",index);
                ControlBoardWindow.getInstance().showControl("到达途径点——" + index);
            }

            @Override
            public void onLocationChange(BNaviLocation naviLocation) {
                Timber.e("---GPS位置有更新时的回调");
                ControlBoardWindow.getInstance().showControl("GPS位置有更新时的回调:");
            }

            @Override
            public void onMapStateChange(MapStateMode mapStateMode) {
                ControlBoardWindow.getInstance()
                        .showControlOnlyone("底图操作态和导航态的回调:", "onMapStateChange");
            }

            @Override
            public void onStartYawing(String flag) {
                overSpeedDistance = -1;
                // Toast.makeText(DemoGuideActivity.this, flag + "", Toast.LENGTH_SHORT).show();
                ControlBoardWindow.getInstance().showControl("开始偏航的回调");
                List<RoadEventItem> roadEvents = BaiduNaviManagerFactory.getRouteGuideManager()
                        .getForwardRoadEvent();

                if (roadEvents != null && roadEvents.size() > 0) {
                    for (int i = 0; i < roadEvents.size(); i++) {
                        RoadEventItem roadEventItem = roadEvents.get(i);
                        Timber.e( "roadEvent eventType==" + roadEventItem.roadEventType +
                                ", eventTypeStr=" + roadEventItem.roadEventTypeStr +
                                ", longitude=" + roadEventItem.longitude + ", latitude=" + roadEventItem.latitude);
                    }
                }
            }

            @Override
            public void onYawingSuccess() {
                ControlBoardWindow.getInstance().showControl("偏航成功的回调");
            }

            @Override
            public void onYawingArriveViaPoint(int index) {

            }

            @Override
            public void onNotificationShow(String msg) {
                ControlBoardWindow.getInstance().showControl("导航中通知型消息的回调" + msg);
            }

            @Override
            public void onHeavyTraffic() {
                Log.e(TAG, "onHeavyTraffic");
                ControlBoardWindow.getInstance().showControl("导航中前方一公里出现严重拥堵的回调");
            }

            @Override
            public void onNaviGuideEnd() {
                BdMotoNaviActivity.this.finish();
            }

            @Override
            public void onPreferChanged(int preferType) {

            }
        });

        BaiduNaviManagerFactory.getRouteGuideManager().setNaviViewListener(
                new IBNaviViewListener() {
                    @Override
                    public void onMainInfoPanCLick() {
                        ControlBoardWindow.getInstance().showControl("诱导面板的点击");
                        guidePop.showAtLocation(view,
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }

                    @Override
                    public void onNaviTurnClick() {
                        ControlBoardWindow.getInstance().showControl("界面左上角转向操作的点击");
                    }

                    @Override
                    public void onFullViewButtonClick(boolean show) {
                        ControlBoardWindow.getInstance().showControl("全览按钮的点击");
                    }

                    @Override
                    public void onFullViewWindowClick(boolean show) {
                        ControlBoardWindow.getInstance().showControl("全览小窗口的点击");
                    }

                    @Override
                    public void onNaviBackClick() {
                        Log.e(TAG, "onNaviBackClick");
                        ControlBoardWindow.getInstance().showControl("导航页面左下角退出按钮点击");
                    }

                    @Override
                    public void onBottomBarClick(Action action) {
                        ControlBoardWindow.getInstance().showControl("底部中间部分点击");
                    }

                    @Override
                    public void onNaviSettingClick() {
                        Log.e(TAG, "onNaviSettingClick");
                        ControlBoardWindow.getInstance().showControl("底部右边更多设置按钮点击");
                    }

                    @Override
                    public void onRefreshBtnClick() {
                        ControlBoardWindow.getInstance().showControl("刷新按钮");
                    }

                    @Override
                    public void onZoomLevelChange(int level) {
                        ControlBoardWindow.getInstance().showControl("地图缩放等级:" + level);
                    }

                    @Override
                    public void onMapClicked(double x, double y) {
                        ControlBoardWindow.getInstance().showControl("地图点击的回调(国测局GCJ02坐标):x="
                                + x + " y=" + y, "onMapClicked");
                    }

                    @Override
                    public void onMapMoved() {
                        Log.e(TAG, "onMapMoved");
                        ControlBoardWindow.getInstance().showControlOnlyone("移动地图的回调", "onMapMoved");
                    }

                    @Override
                    public void onFloatViewClicked() {
                        ControlBoardWindow.getInstance().showControl("后台诱导悬浮窗的点击");
                        try {
                            Intent intent = new Intent();
                            intent.setPackage(getPackageName());
                            intent.setClass(BdMotoNaviActivity.this,
                                    Class.forName(BdMotoNaviActivity.class.getName()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            startActivity(intent);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



    @Override
    protected void onStart() {
        super.onStart();
        mRouteGuideManager.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mRouteGuideManager.onResume();
    }

    protected void onPause() {
        super.onPause();
        mRouteGuideManager.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRouteGuideManager.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouteGuideManager.onDestroy(false);
        unInitTTSListener();
        mRouteGuideManager = null;
//        BaiduNaviManagerFactory.getRouteResultManager().onDestroy();
    }
}

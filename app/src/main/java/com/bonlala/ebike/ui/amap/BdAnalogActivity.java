package com.bonlala.ebike.ui.amap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRouteGuideManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBNaviListener;
import com.baidu.navisdk.adapter.struct.BNGuideConfig;
import com.baidu.navisdk.adapter.struct.BNHighwayInfo;
import com.baidu.navisdk.adapter.struct.BNRoadCondition;
import com.baidu.navisdk.adapter.struct.BNavLineItem;
import com.baidu.navisdk.adapter.struct.BNaviInfo;
import com.baidu.navisdk.adapter.struct.BNaviLocation;
import com.baidu.navisdk.adapter.struct.BNaviResultInfo;
import com.baidu.navisdk.adapter.struct.RoadEventItem;
import com.baidu.navisdk.comapi.routeguide.RouteGuideParams;
import com.baidu.navisdk.ui.routeguide.model.z;
import com.bonlala.blelibrary.BleConstant;
import com.bonlala.blelibrary.BleOperateManager;
import com.bonlala.blelibrary.DeviceAutoDataBean;
import com.bonlala.blelibrary.MapNaviBean;
import com.bonlala.blelibrary.Utils;
import com.bonlala.blelibrary.listener.WriteBackDataListener;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.ui.view.CusMapCircleView;
import com.bonlala.ebike.utils.BikeUtils;
import com.github.promeg.pinyinhelper.Pinyin;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import timber.log.Timber;

/**
 * 模拟导航
 * Created by Admin
 * Date 2022/4/16
 */
public class BdAnalogActivity extends FragmentActivity {

    private CusMapCircleView cusMapView;

    private IBNRouteGuideManager mRouteGuideManager;

    private IBNaviListener.DayNightMode mMode = IBNaviListener.DayNightMode.DAY;

    private FrameLayout bmMapViewLayout;

    private MapNaviBean mapNaviBean = new MapNaviBean();

    private ImageView naviGpsStatusImg;

    private BleOperateManager bleOperateManager;


    //转向
    private ImageView naviStatusImgView;
    //下个路名
    private TextView nextRouteTv;
    //剩余距离
    private TextView endDistanceTv;
    private TextView routeDirectTv;
    private TextView naviUnitTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bd_navi_layout);

        findViews();
        bleOperateManager = AppApplication.getBleOperateManager();

        naviGpsStatusImg = findViewById(R.id.naviGpsStatusImg);
        cusMapView = findViewById(R.id.naviIngCusMapView);
        bmMapViewLayout = findViewById(R.id.bmMapViewLayout);
        // 模拟导航ui自定义,隐藏导航自带的退出、速度、开始/暂停按钮。
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager()
                .setAnalogQuitButtonVisible(false);
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager()
                .setAnalogSpeedButtonVisible(false);
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager()
                .setAnalogSwitchButtonVisible(false);

        Bundle params = new Bundle();
        params.putBoolean(BNaviCommonParams.ProGuideKey.IS_REALNAVI, false);
//        params.putBoolean(BNaviCommonParams.ProGuideKey.IS_SUPPORT_FULL_SCREEN,
//                supportFullScreen());
        mRouteGuideManager = BaiduNaviManagerFactory.getRouteGuideManager();
        BNGuideConfig config = new BNGuideConfig.Builder()
                .params(params)
                .build();



        View view = mRouteGuideManager.onCreate(this, config);

        if (view != null && view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViews();
        }
        bmMapViewLayout.addView(view);

        initTTSListener();
        initGuidEvent();
    }


    private void findViews(){
        naviStatusImgView = findViewById(R.id.naviStatusImgView);
        nextRouteTv = findViewById(R.id.nextRouteTv);
        endDistanceTv = findViewById(R.id.endDistanceTv);
        routeDirectTv = findViewById(R.id.routeDirectTv);
        naviUnitTv = findViewById(R.id.naviUnitTv);
    }


    private int getTurnPosition(int descIndex){

        if(descIndex == 5){ //掉头
            naviStatusImgView.setImageResource(R.drawable.ic_navi_u_turn);
            routeDirectTv.setText("掉头");
            return 1;
        }

        if(descIndex == 6){ //左后转
            naviStatusImgView.setImageResource(R.drawable.ic_navi_turn_left);
            routeDirectTv.setText("左后转");
            return  2;
        }

        if(descIndex == 7){ //左转
            naviStatusImgView.setImageResource(R.drawable.ic_navi_left);
            routeDirectTv.setText("左转");
            return  3;
        }

        if(descIndex == 8){ //左前方
            naviStatusImgView.setImageResource(R.drawable.ic_navi_left_before);
            routeDirectTv.setText("左前方");
            return  4;
        }

        if(descIndex == 1){     //直行
            naviStatusImgView.setImageResource(R.drawable.ic_navi_straight);
            routeDirectTv.setText("直行");
            return  5;
        }
        if(descIndex == 2){ //右前方
            naviStatusImgView.setImageResource(R.drawable.ic_navi_right_before);
            routeDirectTv.setText("右前方");
            return  6;
        }
        if(descIndex == 3){     //右转
            naviStatusImgView.setImageResource(R.drawable.ic_navi_right);
            routeDirectTv.setText("右转");
            return  7;
        }
        if(descIndex == 4){ //右后方
            naviStatusImgView.setImageResource(R.drawable.ic_navi_turn_right);
            routeDirectTv.setText("右后方");
            return  8;
        }

        if(descIndex == 24){    //终点
            naviStatusImgView.setImageResource(R.drawable.ic_navi_end);
            routeDirectTv.setText("到达终点");
            return 9;
        }
        naviStatusImgView.setImageResource(R.drawable.ic_navi_straight);
        routeDirectTv.setText("直行");
        return 5;

    }


    private void initTTSListener() {
        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayError");
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.e("BNSDKDemo", "ttsHandler.msg.what=" + msg.what);
                    }
                }
        );

        BaiduNaviManagerFactory.getRouteGuideManager().setNaviListener(new IBNaviListener() {
            @Override
            public void onNaviGuideEnd() {
                finish();
            }
        });
    }

    private void uninitTTSListener() {
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(null);
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(null);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mRouteGuideManager.onStart();

        registerIntentFilter();
    }

    private void registerIntentFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleConstant.BLE_BATTERY_ACTION);
        intentFilter.addAction(BleConstant.DEVICE_AUTO_DATA_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStop() {
        super.onStop();
        bleOperateManager.intoAndOutNavi(false);
        mRouteGuideManager.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouteGuideManager.onDestroy(false);
        uninitTTSListener();
        mRouteGuideManager = null;
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        mRouteGuideManager.onBackPressed(false, true);
    }


    private void initGuidEvent(){

        //进入导航
        bleOperateManager.intoAndOutNavi(true);

        BaiduNaviManagerFactory.getRouteGuideManager().setNaviListener(new IBNaviListener() {
            @Override
            public void onRoadNameUpdate(String name) { // 弹窗展示
                Timber.e("-----当前路名更新=%s", name);
                ControlBoardWindow.getInstance().showControl("当前路名更新——" + name);

                //当前道路
                if(BikeUtils.isEmpty(name))
                    return;
                String pingyinStr = Pinyin.toPinyin(name," ");
                Timber.e("-----转换为拼音="+pingyinStr);

            }

            @Override
            public void onRemainInfoUpdate(int remainDistance, int remainTime) { // 弹窗展示
                Timber.e("-----\"距离目的地的剩余=%s", remainDistance+"m "+remainTime+"s");
                ControlBoardWindow.getInstance()
                        .showControl("距离目的地的剩余——" + remainDistance + "m " + remainTime + "s");

                mapNaviBean.setEndDistance(remainDistance);

                mapNaviBean.setEndTimeHour(remainTime / 3600);
                mapNaviBean.setEndTimeMinute((remainTime/60) % 60);
                writeRouteMsg();

            }

            @Override
            public void onViaListRemainInfoUpdate(Message msg) {

            }

            @Override
            public void onGuideInfoUpdate(BNaviInfo naviInfo) { // 弹窗展示
                naviGpsStatusImg.setImageBitmap(naviInfo.getTurnIcon());


                Timber.e("-----诱导信息=%s", naviInfo.toString()+"\n"+naviInfo.getTurnIconName());
                ControlBoardWindow.getInstance().showControl("诱导信息——" + naviInfo.toString());

                //下个路口名称
                String nextRouteName = naviInfo.getRoadName();
                //下个路口距离
                int nextRouteDistance = naviInfo.getDistance();
                mapNaviBean.setRouteDistance(nextRouteDistance);
                //转向
                naviInfo.getTurnIconName();
                //路口方向
                int turnIndex = z.H().h().getInt("TurnKind",0);
                int routeDirection = getTurnPosition(turnIndex);
                mapNaviBean.setRouteDirect(routeDirection);

                writeRouteMsg();

                String nextS = BaiduNaviManagerFactory.getRouteGuideManager().getNextGuideText();



                String var2 = z.H().a(z.H().h().getInt("remain_dist", 0));
                String var1 = z.H().o();
                int v = z.H().h().getInt("remain_dist", 0);

                int turnV = z.H().h().getInt("TurnKind");


                Timber.e("----nextS="+nextS+"\n"+var2+"\n"+var1+"\n"+v+"\n"+turnV+"\n"+turnIndex);
                String nextRoutePinyin = Pinyin.toPinyin(nextRouteName," ");
                writeNextRoute(nextRoutePinyin);

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
                    Timber.e("-----车道线信息更新=%s", action + laneItems.get(0).getResName() );
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
                Toast.makeText(BdAnalogActivity.this, "超速回调！！！",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onArriveDestination() {

                BNaviResultInfo info =
                        BaiduNaviManagerFactory.getRouteGuideManager().getNaviResultInfo();

                Timber.e("----导航结算数据=%s",info.toString());
                Toast.makeText(BdAnalogActivity.this, "导航结算数据: " + info.toString(),
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
           //     overSpeedDistance = -1;
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
                ControlBoardWindow.getInstance().showControl("导航中前方一公里出现严重拥堵的回调");
            }

            @Override
            public void onNaviGuideEnd() {
                bleOperateManager.intoAndOutNavi(false);
                BdAnalogActivity.this.finish();
            }

            @Override
            public void onPreferChanged(int preferType) {

            }
        });
    }


    //下一条路
    private void writeNextRoute(String nextRoutStr){
        nextRouteTv.setText(nextRoutStr);
        bleOperateManager.seNextRouteName(nextRoutStr,writeBackDataListener);
    }

    private final DecimalFormat decimalFormat = new DecimalFormat("#.#");

    //路口方向等
    private void writeRouteMsg(){
        if(mapNaviBean == null)
            return;

        Timber.e("------导航信息="+mapNaviBean.toString());

        bleOperateManager.setDirectAndDistance(mapNaviBean.getRouteDirect(),mapNaviBean.getRouteDistance(),mapNaviBean.getEndDistance(),mapNaviBean.getEndTimeHour(),mapNaviBean.getEndTimeMinute(),writeBackDataListener);
        String disStr = "";
        int dis = mapNaviBean.getRouteDistance();
        if(dis == 0){
            endDistanceTv.setText("");
            naviUnitTv.setText("");
            return;
        }

        if(dis >999){
            disStr = decimalFormat.format(Float.valueOf(dis) / 1000);
            naviUnitTv.setText("千米");
        }else{
            disStr = dis+"";
            naviUnitTv.setText("米");
        }

        endDistanceTv.setText(disStr);

    }

    private final WriteBackDataListener writeBackDataListener = new WriteBackDataListener() {
        @Override
        public void backWriteData(byte[] data) {

        }
    };


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == null)
                return;
            if(action.equals(BleConstant.BLE_BATTERY_ACTION)){
                String bleBattery = intent.getStringExtra(BleConstant.BROADCAST_PARAMS_KEY);
                if(BikeUtils.isEmpty(bleBattery))
                    return;
                cusMapView.setBatteryValue(bleBattery);
            }

            if(action.equals(BleConstant.DEVICE_AUTO_DATA_ACTION)){
                DeviceAutoDataBean deviceAutoDataBean = (DeviceAutoDataBean) intent.getSerializableExtra(BleConstant.BROADCAST_PARAMS_KEY);
                if(deviceAutoDataBean == null)
                    return;
                int unit = deviceAutoDataBean.getUnit();
                //平均速度
                int aveSpeed = deviceAutoDataBean.getAvgSpeed();
                float resultAveSpeed = aveSpeed == 0 ? 0.0f : Float.valueOf(aveSpeed) / 10;
                if(unit == 0){  //公制
                    cusMapView.setAvgSpeedValue(resultAveSpeed+"");
                }else{
                    cusMapView.setAvgSpeedValue(Utils.kmToMile(Double.valueOf(resultAveSpeed))+"");
                }

                //当前速度
                float currentSpeed = deviceAutoDataBean.getCurrentSpeed() == 0 ? 0.0f : (Float.valueOf(deviceAutoDataBean.getCurrentSpeed()) / 10);
                cusMapView.setCircleMaxAndCurrValue(100f,unit == 0 ?currentSpeed : Float.valueOf((float) Utils.kmToMile(Double.valueOf(currentSpeed))));

                //运动时间
                cusMapView.setSportTime(deviceAutoDataBean.getHhMmSsStr());
                //运动距离
                float rideDistance = deviceAutoDataBean.getRideDistance() == 0 ? 0.0f : Float.valueOf(deviceAutoDataBean.getRideDistance() / 10);

                if(unit == 0){
                    cusMapView.setRideDistance(rideDistance+"");
                }else{
                    cusMapView.setRideDistance(Utils.kmToMile(rideDistance)+"");
                }
                //档位
                cusMapView.setGear(deviceAutoDataBean.getGear());
            }

        }
    };

}

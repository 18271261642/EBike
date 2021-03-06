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
 * ????????????
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


    //??????
    private ImageView naviStatusImgView;
    //????????????
    private TextView nextRouteTv;
    //????????????
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
        // ????????????ui?????????,?????????????????????????????????????????????/???????????????
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

        if(descIndex == 5){ //??????
            naviStatusImgView.setImageResource(R.drawable.ic_navi_u_turn);
            routeDirectTv.setText("??????");
            return 1;
        }

        if(descIndex == 6){ //?????????
            naviStatusImgView.setImageResource(R.drawable.ic_navi_turn_left);
            routeDirectTv.setText("?????????");
            return  2;
        }

        if(descIndex == 7){ //??????
            naviStatusImgView.setImageResource(R.drawable.ic_navi_left);
            routeDirectTv.setText("??????");
            return  3;
        }

        if(descIndex == 8){ //?????????
            naviStatusImgView.setImageResource(R.drawable.ic_navi_left_before);
            routeDirectTv.setText("?????????");
            return  4;
        }

        if(descIndex == 1){     //??????
            naviStatusImgView.setImageResource(R.drawable.ic_navi_straight);
            routeDirectTv.setText("??????");
            return  5;
        }
        if(descIndex == 2){ //?????????
            naviStatusImgView.setImageResource(R.drawable.ic_navi_right_before);
            routeDirectTv.setText("?????????");
            return  6;
        }
        if(descIndex == 3){     //??????
            naviStatusImgView.setImageResource(R.drawable.ic_navi_right);
            routeDirectTv.setText("??????");
            return  7;
        }
        if(descIndex == 4){ //?????????
            naviStatusImgView.setImageResource(R.drawable.ic_navi_turn_right);
            routeDirectTv.setText("?????????");
            return  8;
        }

        if(descIndex == 24){    //??????
            naviStatusImgView.setImageResource(R.drawable.ic_navi_end);
            routeDirectTv.setText("????????????");
            return 9;
        }
        naviStatusImgView.setImageResource(R.drawable.ic_navi_straight);
        routeDirectTv.setText("??????");
        return 5;

    }


    private void initTTSListener() {
        // ??????????????????tts????????????
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

        // ????????????tts ??????????????????
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

        //????????????
        bleOperateManager.intoAndOutNavi(true);

        BaiduNaviManagerFactory.getRouteGuideManager().setNaviListener(new IBNaviListener() {
            @Override
            public void onRoadNameUpdate(String name) { // ????????????
                Timber.e("-----??????????????????=%s", name);
                ControlBoardWindow.getInstance().showControl("????????????????????????" + name);

                //????????????
                if(BikeUtils.isEmpty(name))
                    return;
                String pingyinStr = Pinyin.toPinyin(name," ");
                Timber.e("-----???????????????="+pingyinStr);

            }

            @Override
            public void onRemainInfoUpdate(int remainDistance, int remainTime) { // ????????????
                Timber.e("-----\"????????????????????????=%s", remainDistance+"m "+remainTime+"s");
                ControlBoardWindow.getInstance()
                        .showControl("??????????????????????????????" + remainDistance + "m " + remainTime + "s");

                mapNaviBean.setEndDistance(remainDistance);

                mapNaviBean.setEndTimeHour(remainTime / 3600);
                mapNaviBean.setEndTimeMinute((remainTime/60) % 60);
                writeRouteMsg();

            }

            @Override
            public void onViaListRemainInfoUpdate(Message msg) {

            }

            @Override
            public void onGuideInfoUpdate(BNaviInfo naviInfo) { // ????????????
                naviGpsStatusImg.setImageBitmap(naviInfo.getTurnIcon());


                Timber.e("-----????????????=%s", naviInfo.toString()+"\n"+naviInfo.getTurnIconName());
                ControlBoardWindow.getInstance().showControl("??????????????????" + naviInfo.toString());

                //??????????????????
                String nextRouteName = naviInfo.getRoadName();
                //??????????????????
                int nextRouteDistance = naviInfo.getDistance();
                mapNaviBean.setRouteDistance(nextRouteDistance);
                //??????
                naviInfo.getTurnIconName();
                //????????????
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
                        .showControl("??????????????????" + action + " " + info.toString());
            }

            @Override
            public void onFastExitWayInfoUpdate(Action action, String name, int dist, String id) {
                Timber.e("-----?????????????????????="+action + " " + name + " ????????????" + dist + "???");
                ControlBoardWindow.getInstance()
                        .showControl("???????????????????????????" + action + " " + name + " ????????????" + dist + "???");


            }

            @Override
            public void onEnlargeMapUpdate(Action action, View enlargeMap, String remainDistance,
                                           int progress, String roadName, Bitmap turnIcon) {
                ControlBoardWindow.getInstance().showControl("?????????????????????");
            }

            @Override
            public void onDayNightChanged(DayNightMode style) {
                ControlBoardWindow.getInstance().showControl("????????????????????????" + style);
            }

            @Override
            public void onRoadConditionInfoUpdate(double progress, List<BNRoadCondition> items) {
                Timber.e("-----?????????????????? ?????????=%s", progress );
                ControlBoardWindow.getInstance().showControl("?????????????????? ?????????" + progress + " ??????????????????");
            }

            @Override
            public void onMainSideBridgeUpdate(int type) {
                Timber.e("-----?????????????????????????????????=%s", type );
                ControlBoardWindow.getInstance().showControl("?????????????????????????????????:" + type + " ?????????????????????gb");
            }

            @Override
            public void onLaneInfoUpdate(Action action, List<BNavLineItem> laneItems) {
                if (laneItems != null && laneItems.size() > 0) {
                    Timber.e("-----?????????????????????=%s", action + laneItems.get(0).getResName() );
                    ControlBoardWindow.getInstance().showControl("?????????????????????:" +
                            action + laneItems.get(0).toString() + " ...");
                }
            }

            @Override
            public void onSpeedUpdate(int speed, int overSpeed) {

//                ControlBoardWindow.getInstance()
//                        .showControl("???????????????" + speed + "?????????" + overSpeed + " ???????????????" + (speed > overSpeed) );
//                if (overSpeed == -1) {
//                    return;
//                }
//                if (speed > overSpeed && ( (overSpeedDistance == -1 || overSpeedDistance - curRemainDistance > 1000) ) ) {
//                    // 1km????????????????????????????????????
//                    ControlBoardWindow.getInstance()
//                            .showControl("?????????????????????");
//                    Toast.makeText(DemoGuideActivity.this, "?????????????????????",
//                            Toast.LENGTH_SHORT).show();
//                    overSpeedDistance = curRemainDistance;
//                }
            }

            @Override
            public void onOverSpeed(int speed, int speedLimit) {
                Toast.makeText(BdAnalogActivity.this, "?????????????????????",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onArriveDestination() {

                BNaviResultInfo info =
                        BaiduNaviManagerFactory.getRouteGuideManager().getNaviResultInfo();

                Timber.e("----??????????????????=%s",info.toString());
                Toast.makeText(BdAnalogActivity.this, "??????????????????: " + info.toString(),
                        Toast.LENGTH_SHORT).show();
                ControlBoardWindow.getInstance().showControl("??????????????????" + info.toString());
            }

            @Override
            public void onArrivedWayPoint(int index) {
                Timber.e("----???????????????=%s",index);
                ControlBoardWindow.getInstance().showControl("?????????????????????" + index);
            }

            @Override
            public void onLocationChange(BNaviLocation naviLocation) {
                Timber.e("---GPS???????????????????????????");
                ControlBoardWindow.getInstance().showControl("GPS???????????????????????????:");
            }


            @Override
            public void onMapStateChange(MapStateMode mapStateMode) {
                ControlBoardWindow.getInstance()
                        .showControlOnlyone("????????????????????????????????????:", "onMapStateChange");
            }

            @Override
            public void onStartYawing(String flag) {
           //     overSpeedDistance = -1;
                // Toast.makeText(DemoGuideActivity.this, flag + "", Toast.LENGTH_SHORT).show();
                ControlBoardWindow.getInstance().showControl("?????????????????????");
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
                ControlBoardWindow.getInstance().showControl("?????????????????????");
            }

            @Override
            public void onYawingArriveViaPoint(int index) {

            }

            @Override
            public void onNotificationShow(String msg) {
                ControlBoardWindow.getInstance().showControl("?????????????????????????????????" + msg);
            }

            @Override
            public void onHeavyTraffic() {
                ControlBoardWindow.getInstance().showControl("???????????????????????????????????????????????????");
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


    //????????????
    private void writeNextRoute(String nextRoutStr){
        nextRouteTv.setText(nextRoutStr);
        bleOperateManager.seNextRouteName(nextRoutStr,writeBackDataListener);
    }

    private final DecimalFormat decimalFormat = new DecimalFormat("#.#");

    //???????????????
    private void writeRouteMsg(){
        if(mapNaviBean == null)
            return;

        Timber.e("------????????????="+mapNaviBean.toString());

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
            naviUnitTv.setText("??????");
        }else{
            disStr = dis+"";
            naviUnitTv.setText("???");
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
                //????????????
                int aveSpeed = deviceAutoDataBean.getAvgSpeed();
                float resultAveSpeed = aveSpeed == 0 ? 0.0f : Float.valueOf(aveSpeed) / 10;
                if(unit == 0){  //??????
                    cusMapView.setAvgSpeedValue(resultAveSpeed+"");
                }else{
                    cusMapView.setAvgSpeedValue(Utils.kmToMile(Double.valueOf(resultAveSpeed))+"");
                }

                //????????????
                float currentSpeed = deviceAutoDataBean.getCurrentSpeed() == 0 ? 0.0f : (Float.valueOf(deviceAutoDataBean.getCurrentSpeed()) / 10);
                cusMapView.setCircleMaxAndCurrValue(100f,unit == 0 ?currentSpeed : Float.valueOf((float) Utils.kmToMile(Double.valueOf(currentSpeed))));

                //????????????
                cusMapView.setSportTime(deviceAutoDataBean.getHhMmSsStr());
                //????????????
                float rideDistance = deviceAutoDataBean.getRideDistance() == 0 ? 0.0f : Float.valueOf(deviceAutoDataBean.getRideDistance() / 10);

                if(unit == 0){
                    cusMapView.setRideDistance(rideDistance+"");
                }else{
                    cusMapView.setRideDistance(Utils.kmToMile(rideDistance)+"");
                }
                //??????
                cusMapView.setGear(deviceAutoDataBean.getGear());
            }

        }
    };

}

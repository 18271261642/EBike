package com.bonlala.ebike.ui.amap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.ParallelRoadListener;
import com.amap.api.navi.enums.AMapNaviParallelRoadStatus;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.model.RouteOverlayOptions;
import com.amap.api.navi.view.RouteOverLay;
import com.bonlala.ebike.R;
import com.bonlala.ebike.ui.amap.utils.NaviUtil;
import com.bonlala.ebike.ui.amap.utils.TTSController;
import com.bonlala.ebike.ui.amap.view.CarOverlay;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class BaseCustomActivity extends Activity implements AMapNaviListener, AMapNaviViewListener, ParallelRoadListener {

    protected TextureMapView mAMapNaviView;
    protected AMapNavi mAMapNavi;
    protected TTSController mTtsManager;
    protected NaviLatLng mEndLatlng = new NaviLatLng(40.084894,116.603039);
    protected NaviLatLng mStartLatlng = new NaviLatLng(39.825934,116.342972);
    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
    protected List<NaviLatLng> mWayPointList;

    NaviLatLng p1 = new NaviLatLng(39.993266, 116.473193);//????????????
    NaviLatLng p2 = new NaviLatLng(39.917337, 116.397056);//???????????????
    NaviLatLng p3 = new NaviLatLng(39.904556, 116.427231);//?????????
    NaviLatLng p4 = new NaviLatLng(39.773801, 116.368984);//???????????????(???5???)
    NaviLatLng p5 = new NaviLatLng(40.041986, 116.414496);//?????????(???5???)


    /**
     * ????????????
     */
    private static final int MESSAGE_CAR_LOCK = 0;
    /**
     * ???????????????
     */
    private static final int MESSAGE_CAR_UNLOCK = 1;

    private Handler handler = new UiHandler(this);

    class UiHandler extends Handler {

        private final WeakReference context;
        public UiHandler(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (context.get() == null) {
                    return;
                }

                switch (msg.what) {
                    case MESSAGE_CAR_LOCK:
                        if (carOverlay != null) {
                            carOverlay.setLock(true);
                        }
                        break;
                    case MESSAGE_CAR_UNLOCK:
                        if (carOverlay != null) {
                            carOverlay.setLock(false);
                        }
                        break;
                    default:
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ???????????????
     */
    CarOverlay carOverlay;
    /**
     * ??????Overlay
     */
    RouteOverLay routeOverLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //?????????????????????
        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();

        //
        try {
            mAMapNavi = AMapNavi.getInstance(getApplicationContext());
            mAMapNavi.addAMapNaviListener(this);
            mAMapNavi.addAMapNaviListener(mTtsManager);
            mAMapNavi.addParallelRoadListener(this);
            mAMapNavi.setUseInnerVoice(true);

            //?????????????????????????????????
            mAMapNavi.setEmulatorNaviSpeed(75);
            sList.add(p3);
            eList.add(p2);
        } catch (AMapException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAMapNaviView.getMap().setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {

                handler.sendEmptyMessage(MESSAGE_CAR_UNLOCK);
                handler.removeMessages(MESSAGE_CAR_LOCK);
                handler.sendEmptyMessageDelayed(MESSAGE_CAR_LOCK, 3000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();

//        ????????????????????????????????????????????????????????????????????????????????????
        mTtsManager.stopSpeaking();
//
//        ????????????????????????????????????stop???????????????????????????????????????????????????????????????????????????????????????????????????
//        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0 ?????????naviview destroy?????????????????????AMapNavi.stopNavi();???????????????
        if (mAMapNavi!=null){
            mAMapNavi.stopNavi();
            mAMapNavi.destroy();
        }
        mTtsManager.destroy();



        if (carOverlay != null) {
            carOverlay.destroy();
        }

        if (routeOverLay != null) {
            routeOverLay.removeFromMap();
            routeOverLay.destroy();
        }

        if (mAMapNaviView != null) {
            mAMapNaviView.onDestroy();
        }

    }

    @Override
    public void onInitNaviFailure() {
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitNaviSuccess() {
        // ???????????????
        int strategy = 0;
        try {
            //????????????????????????????????????true??????????????????????????????????????????
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);

    }

    @Override
    public void onStartNavi(int type) {
        //??????????????????
    }

    @Override
    public void onTrafficStatusUpdate() {
        //
    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
        //??????????????????
    }

    @Override
    public void onGetNavigationText(int type, String text) {
        //?????????????????????????????????
    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {
        //??????????????????
    }

    @Override
    public void onArriveDestination() {
        //???????????????
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {

    }

    @Override
    public void onReCalculateRouteForYaw() {
        //?????????????????????????????????
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        //?????????????????????????????????
    }

    @Override
    public void onArrivedWayPoint(int wayID) {
        //???????????????
    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
        //GPS??????????????????
    }

    @Override
    public void onNaviSetting() {
        //??????????????????????????????
    }

    @Override
    public void onNaviMapMode(int isLock) {
        //?????????????????????????????????
    }

    @Override
    public void onNaviCancel() {
        finish();
    }


    @Override
    public void onNaviTurnClick() {
        //??????view???????????????
    }

    @Override
    public void onNextRoadClick() {
        //???????????????View????????????
    }


    @Override
    public void onScanViewButtonClick() {
        //????????????????????????
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] amapServiceAreaInfos) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        //???????????????????????????????????????NaviInfo???????????????
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        //?????????
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        //??????????????????
    }

    @Override
    public void hideCross() {
        //??????????????????
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
        //??????????????????

    }

    @Override
    public void hideLaneInfo() {
        //??????????????????
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        //???????????????????????????
    }

    @Override
    public void notifyParallelRoad(AMapNaviParallelRoadStatus aMapNaviParallelRoadStatus) {
        if (aMapNaviParallelRoadStatus.getmElevatedRoadStatusFlag() == 1) {
            Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "??????????????????");
        } else if (aMapNaviParallelRoadStatus.getmElevatedRoadStatusFlag() == 2) {
            Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "??????????????????");
        }

        if (aMapNaviParallelRoadStatus.getmParallelRoadStatusFlag() == 1) {
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "???????????????");
        } else if (aMapNaviParallelRoadStatus.getmParallelRoadStatusFlag() == 2) {
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "???????????????");
        }
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        //????????????????????????
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        //?????????????????????????????????
    }


    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        //?????????????????????????????????
    }

    @Override
    public void onPlayRing(int i) {

    }


    @Override
    public void onLockMap(boolean isLock) {
        //????????????????????????????????????
    }

    @Override
    public void onNaviViewLoaded() {
        Log.d("wlx", "????????????????????????");
        Log.d("wlx", "???????????????AMapNaviView.getMap().setOnMapLoadedListener();???overwrite??????SDK??????????????????");
    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }


    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        AMapNaviPath naviPath = mAMapNavi.getNaviPath();
        if (naviPath != null) {

            if (routeOverLay == null) {
                /**
                 * ?????????????????????
                 */
                routeOverLay = new RouteOverLay(mAMapNaviView.getMap(), naviPath, this);
                BitmapDescriptor smoothTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_green);
                BitmapDescriptor unknownTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_no);
                BitmapDescriptor slowTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_slow);
                BitmapDescriptor jamTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_bad);
                BitmapDescriptor veryJamTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_grayred);

                RouteOverlayOptions routeOverlayOptions = new RouteOverlayOptions();
                routeOverlayOptions.setSmoothTraffic(smoothTraffic.getBitmap());
                routeOverlayOptions.setUnknownTraffic(unknownTraffic.getBitmap());
                routeOverlayOptions.setSlowTraffic(slowTraffic.getBitmap());
                routeOverlayOptions.setJamTraffic(jamTraffic.getBitmap());
                routeOverlayOptions.setVeryJamTraffic(veryJamTraffic.getBitmap());

                routeOverLay.setRouteOverlayOptions(routeOverlayOptions);
            }
            if (routeOverLay != null) {
                routeOverLay.setAMapNaviPath(naviPath);
                routeOverLay.addToMap();
            }

            float bearing = NaviUtil.getRotate(mStartLatlng, naviPath.getCoordList().get(1));
            if (mStartLatlng != null) {
                carOverlay.reset();
                /**
                 * ??????????????????
                 */
                carOverlay.draw(mAMapNaviView.getMap(), new LatLng(mStartLatlng.getLatitude(), mStartLatlng.getLongitude()), bearing);
                if (naviPath.getEndPoint() != null) {
                    LatLng latlng = new LatLng(naviPath.getEndPoint().getLatitude(), naviPath.getEndPoint().getLongitude());
                    carOverlay.setEndPoi(latlng);
                }
            }
        }

        /**
         * ??????????????????
          */
        mAMapNavi.startNavi(NaviType.EMULATOR);

    }

    /**
     * ??????????????????
     */
    private int roadIndex;
    public void drawArrow(NaviInfo naviInfo) {
        try {
            if (roadIndex != naviInfo.getCurStep()) {
                List<NaviLatLng> arrow = routeOverLay.getArrowPoints(naviInfo.getCurStep());
                if (routeOverLay != null && arrow != null && arrow.size() > 0) {
                    routeOverLay.drawArrow(arrow);
                    roadIndex = naviInfo.getCurStep();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult result) {
        //??????????????????
        Log.e("dm", "--------------------------------------------");
        Log.i("dm", "??????????????????????????????=" + result.getErrorCode() + ",Error Message= " + result.getErrorDescription());
        Log.i("dm", "???????????????????????????http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        Log.e("dm", "--------------------------------------------");
        Toast.makeText(this, "errorInfo???" + result.getErrorDetail() + ", Message???" + result.getErrorDescription(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onGpsSignalWeak(boolean b) {

    }
}

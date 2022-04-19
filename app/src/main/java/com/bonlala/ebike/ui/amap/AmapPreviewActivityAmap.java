package com.bonlala.ebike.ui.amap;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bonlala.blelibrary.BleConstant;
import com.bonlala.blelibrary.CusLocalBean;
import com.bonlala.blelibrary.DeviceAutoDataBean;
import com.bonlala.blelibrary.OnCusLocationListener;
import com.bonlala.blelibrary.Utils;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.ui.view.CusMapCircleView;
import com.bonlala.ebike.utils.BikeUtils;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.hjq.shape.layout.ShapeConstraintLayout;
import com.hjq.shape.layout.ShapeFrameLayout;
import java.util.List;
import androidx.annotation.Nullable;
import timber.log.Timber;


/**
 * Created by Admin
 * Date 2022/4/7
 */
public class AmapPreviewActivityAmap extends AppActivity {

    //返回
    private ShapeFrameLayout mapPreviewBackLayout;

    //地图
    private MapView previewMapView;
    private BaiduMap baiduMap;

    //定位服务
    private LocationService locationService;

    //GPS图片
    private ImageView previewMapGpsStatusImg;

    //搜索框
    private ShapeConstraintLayout previewMapSearchLayout;
    private CusMapCircleView cusMapView;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_amap_preview_layout;
    }

    @Override
    protected void initView() {
        LocationClient.setAgreePrivacy(true);

        initViews();


        previewMapView = findViewById(R.id.previewMapView);

        baiduMap = previewMapView.getMap();

        //不显示缩放按钮
        previewMapView.showZoomControls(false);
        //开启地图的定位图层
        baiduMap.setMyLocationEnabled(true);
    }


    private void registerIntentFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleConstant.BLE_BATTERY_ACTION);
        intentFilter.addAction(BleConstant.DEVICE_AUTO_DATA_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 0x00){
           // mNaviHelper.startBikeNavi(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时必须调用mMapView. onResume ()
        previewMapView.onResume();
       // mNaviHelper.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时必须调用mMapView. onPause ()
        previewMapView.onPause();
//        locationService.unregisterListener(onCusLocationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        previewMapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        unregisterReceiver(broadcastReceiver);
        locationService.unregisterListener(bdAbstractLocationListener);
    }



    @Override
    protected void initData() {
        registerIntentFilter();

    }

    @Override
    protected boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    private void initViews(){

        previewMapGpsStatusImg = findViewById(R.id.previewMapGpsStatusImg);
        mapPreviewBackLayout = findViewById(R.id.mapPreviewBackLayout);
        previewMapSearchLayout = findViewById(R.id.previewMapSearchLayout);
        cusMapView = findViewById(R.id.cusMapView);
        previewMapSearchLayout = findViewById(R.id.previewMapSearchLayout);

        setOnClickListener(previewMapSearchLayout,mapPreviewBackLayout);


        requestPermission();


        locationService  = AppApplication.getLocationService();

        locationService.registerListener(bdAbstractLocationListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
        locationService.requestLocation();


    }


    private void requestPermission(){
        XXPermissions.with(this).permission(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION).request(new OnPermissionCallback() {
            @Override
            public void onGranted(List<String> permissions, boolean all) {

            }
        });
    }



    @Override
    public void onClick(View view) {
        if(view == previewMapSearchLayout){
           startActivity(RoutePreviewActivity.class);
        }
        if(view == mapPreviewBackLayout){
           finish();
        }

    }



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

                //const double KmToMi = 1.6093439975538;          // 1英里 = 1.6公里
                //const double MToFt = 3.28083989501;        // 1米 = 3.28英尺
                //公英制 0公制；1英制
                int unit = deviceAutoDataBean.getUnit();

                //平均速度
                int aveSpeed = deviceAutoDataBean.getAvgSpeed();
                float resultAveSpeed = aveSpeed == 0 ? 0.0f : (Float.valueOf(aveSpeed) / 10);

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

    private final BDAbstractLocationListener bdAbstractLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || previewMapView == null) {
                return;
            }
            int gpsRssi = bdLocation.getGpsCheckStatus();
            Timber.e("----GPS信号="+gpsRssi);

            previewMapGpsStatusImg.setImageResource(showGpsStatus(gpsRssi));

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            baiduMap.setMyLocationData(locData);

            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    };



    private int showGpsStatus(int value){
        if(value == 0)
            return R.mipmap.ic_preview_no_gps;
        if(value == 1)
            return R.mipmap.ic_preview_gps1;
        if(value == 2)
            return R.mipmap.ic_preview_gps2;
        return R.mipmap.ic_preview_gps;

    }
}

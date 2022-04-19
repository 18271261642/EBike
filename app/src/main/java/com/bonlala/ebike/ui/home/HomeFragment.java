package com.bonlala.ebike.ui.home;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bonlala.base.BaseFragment;
import com.bonlala.blelibrary.BleConstant;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.http.GlideApp;
import com.bonlala.ebike.http.api.device.DeviceApi;
import com.bonlala.ebike.http.api.user.LoginPresent;
import com.bonlala.ebike.http.api.user.LoginView;
import com.bonlala.ebike.http.api.user.UserHomeApi;
import com.bonlala.ebike.http.api.weather.OnWeatherBackListener;
import com.bonlala.ebike.http.api.weather.WeatherApi;
import com.bonlala.ebike.ui.HomeActivity;
import com.bonlala.ebike.ui.activity.HistoryDataActivity;
import com.bonlala.ebike.ui.amap.AmapPreviewActivityAmap;
import com.bonlala.ebike.ui.ble.BatteryListener;
import com.bonlala.ebike.ui.ble.DeviceSetActivity;
import com.bonlala.ebike.ui.ble.OnHomeBleConnstatusListener;
import com.bonlala.ebike.ui.ble.SearchDeviceActivity;
import com.bonlala.ebike.ui.ble.ShowLogActivity;
import com.bonlala.ebike.utils.BikeUtils;
import com.bonlala.ebike.utils.LogcatHelper;
import com.bonlala.ebike.utils.MmkvUtils;
import com.bonlala.widget.view.CircleProgress;
import com.google.gson.Gson;
import com.hjq.shape.layout.ShapeConstraintLayout;
import com.hjq.shape.layout.ShapeFrameLayout;
import com.hjq.shape.view.ShapeTextView;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import timber.log.Timber;


public class HomeFragment extends BaseFragment<HomeActivity> implements OnWeatherBackListener ,
        BatteryListener, LoginView  {

    private static final String TAG = "HomeFragment";

    private LoginPresent loginPresent;

    private ConstraintLayout homeDeviceTypeLayout;
    private ConstraintLayout homeEmptyLayout;
    private ImageView homeEmptyImgView;

    //电量展示
    private CircleProgress homeBatteryView;

    //天气状态图片
    private ImageView homeWeatherStatusImg;
    //天气温度
    private TextView homeWeatherTempTv;
    //描述
    private TextView homeWeatherDescTv;

    //连接新设备按钮
    private ShapeTextView homeConnNewDeviceTv;


    //左侧连接状态按钮
    private ShapeTextView homeDeviceConnTv;
    //底部连接tv
    private TextView homeLeftConnTxtTv;
    private ShapeFrameLayout homeBleStatusLayout;


    //导航图片
    private ImageView homeDeviceNavigationImgView;
    //进入设置页面
    private ImageView homeDeviceSetImgView;
    //名称
    private TextView homeDeviceNameTv;
    //mac
    private TextView homeDeviceMacTv;


    //总次数
    private TextView homeTotalNumTv;
    //中时间
    private TextView homeTotalTimeTv;
    //总距离
    private TextView homeTotalDistanceTv;
    //总碳量
    private TextView homeTotalCo2Tv;
    //总卡路里
    private TextView homeTotalCalTv;

    //历史记录
    private ShapeConstraintLayout homeHistoryLayout;

    //连接设备的图片
    private ImageView homeDeviceImgView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {

        findViews();

    }

    @Override
    protected void initData() {
        Timber.e("----initData-----");

        loginPresent = new LoginPresent();
        loginPresent.attachView(this);

        String weatherUrl = MmkvUtils.getWeatherImgUrl();
        if(!BikeUtils.isEmpty(weatherUrl)){
            GlideApp.with(this).load(weatherUrl).into(homeWeatherStatusImg);
        }


        getAttachActivity().getWeatherData(this);
        getAttachActivity().setBatteryListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleConstant.HOME_CONN_STATUS_ACTION);
        intentFilter.addAction(BleConstant.BLE_BATTERY_ACTION);
        intentFilter.addAction(BleConstant.DIS_CONNECTED_ACTION);
        intentFilter.addAction(BleConstant.DEVICE_CONNECTNG_ACTION);
        getAttachActivity().registerReceiver(broadcastReceiver,intentFilter);
    }


    @Override
    public void onResume() {
        super.onResume();
        Timber.e("----onResume-----");


        loginPresent.getPresentHomeTotal(this,new UserHomeApi().getTotalData(),0x00);
        loginPresent.getPresentUserBindDevice(this,new DeviceApi().findBindList(),0x01);
    }




    //连接状态处理
    private void showConnStatus(){
        boolean isOpenBle = BikeUtils.isBleEnable(getAttachActivity());
        boolean isConn = AppApplication.getInstance().isConnectStatus();
        homeBleStatusLayout.getShapeDrawableBuilder().setSolidColor(isOpenBle ? Color.parseColor("#0A84FF") : Color.parseColor("#C7C7CC")).intoBackground();

        //蓝牙未打开
        if(!isOpenBle){ //#F2F2F7
            homeLeftConnTxtTv.setText("OFF");
            homeLeftConnTxtTv.setTextColor(Color.parseColor("#C7C7CC"));
            //#ffc7c7cc
            homeDeviceConnTv.setText("未连接");
            homeDeviceConnTv.setTextColor(Color.parseColor("#ffc7c7cc"));
            return;
        }


       // homeDeviceConnTv.setText( isConn ? "已连接" : "连接");
        homeDeviceConnTv.getShapeDrawableBuilder().setSolidColor(isConn ? Color.parseColor("#F2F2F2") : Color.parseColor("#30D158")).intoBackground();

        if(isConn){
            homeLeftConnTxtTv.setText("ON");
            homeLeftConnTxtTv.setTextColor(Color.parseColor("#C7C7CC"));
            homeDeviceConnTv.setText("已连接");
            homeDeviceConnTv.setTextColor(Color.parseColor("#ff30d158"));
            return;
        }
        homeDeviceConnTv.setText( "连接");
        AppApplication.getInstance().getConnectStatusService().autoConn(AppApplication.connDeviceMac);

    }


    @Override
    public void backCurrWeather(WeatherApi.WeatherBean weatherBean) {
        if(weatherBean == null)
            return;
        //天气
        MmkvUtils.saveWeatherImgUrl(weatherBean.getWeatherImgUrl());
        GlideApp.with(this).load(weatherBean.getWeatherImgUrl()).into(homeWeatherStatusImg);
        homeWeatherTempTv.setText(weatherBean.getTemp()+"℃");
        homeWeatherDescTv.setText(weatherBean.getWeatherTypeName());
    }


    private void findViews(){
        homeBleStatusLayout = findViewById(R.id.homeBleStatusLayout);
        homeLeftConnTxtTv = findViewById(R.id.homeLeftConnTxtTv);
        homeEmptyImgView = findViewById(R.id.homeEmptyImgView);
        homeDeviceImgView = findViewById(R.id.homeDeviceImgView);
        homeDeviceTypeLayout = findViewById(R.id.homeDeviceTypeLayout);
        homeEmptyLayout = findViewById(R.id.homeEmptyLayout);

        homeTotalNumTv = findViewById(R.id.homeTotalNumTv);
        homeTotalTimeTv = findViewById(R.id.homeTotalTimeTv);
        homeTotalDistanceTv = findViewById(R.id.homeTotalDistanceTv);
        homeTotalCo2Tv = findViewById(R.id.homeTotalCo2Tv);
        homeTotalCalTv = findViewById(R.id.homeTotalCalTv);


        homeConnNewDeviceTv = findViewById(R.id.homeConnNewDeviceTv);
        homeBatteryView = findViewById(R.id.homeBatteryView);
        homeDeviceNameTv = findViewById(R.id.homeDeviceNameTv);
        homeDeviceMacTv = findViewById(R.id.homeDeviceMacTv);
        homeDeviceSetImgView = findViewById(R.id.homeDeviceSetImgView);
        homeWeatherStatusImg = findViewById(R.id.homeWeatherStatusImg);
        homeWeatherTempTv = findViewById(R.id.homeWeatherTempTv);
        homeWeatherDescTv = findViewById(R.id.homeWeatherDescTv);
        homeDeviceConnTv = findViewById(R.id.homeDeviceConnTv);
        homeDeviceNavigationImgView = findViewById(R.id.homeDeviceNavigationImgView);

        homeHistoryLayout = findViewById(R.id.homeHistoryLayout);

        setOnClickListener(homeDeviceConnTv,homeDeviceNavigationImgView,
                homeDeviceSetImgView,homeConnNewDeviceTv,homeHistoryLayout);

        homeDeviceSetImgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(ShowLogActivity.class);
                return true;
            }
        });

        homeDeviceNavigationImgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String path = Environment.getExternalStorageDirectory().getPath()+"/aLog/log-"+ BikeUtils.getCurrDate()+".txt";
                Timber.e("----日志="+path);
                LogcatHelper.shareFile(getAttachActivity(),new File(path));
                return true;
            }
        });

        homeBatteryView.setMaxValue(100f);

        homeConnNewDeviceTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(AmapPreviewActivityAmap.class);
                return false;
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
           getAttachActivity().unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        //历史记录
        if(view == homeHistoryLayout){
            startActivity(HistoryDataActivity.class);
        }

        if(view == homeConnNewDeviceTv){
          startActivity(SearchDeviceActivity.class);
        }

        if(view == homeDeviceConnTv){   //连接状态
            boolean isOpenBle = BikeUtils.isBleEnable(getAttachActivity());
            boolean isConn = AppApplication.getInstance().isConnectStatus();
            if(!isOpenBle){
                BikeUtils.openBletooth(getAttachActivity());
                return;
            }

            if(isConn){
                return;
            }

            if(AppApplication.connDeviceMac != null){
                AppApplication.getInstance().getConnectStatusService().autoConn(AppApplication.connDeviceMac);
                return;
            }
            startActivity(SearchDeviceActivity.class);
        }

        if(view == homeDeviceNavigationImgView){    //导航
            startActivity(AmapPreviewActivityAmap.class);

//            startActivity(RoutePreviewActivity.class);
        }

        if(view == homeDeviceSetImgView){
            boolean isConn = AppApplication.getInstance().isConnectStatus();
//            if(!isConn){
//                ToastUtils.show("设备未连接!");
//                return;
//            }
            startActivity(DeviceSetActivity.class);
        }
    }


    //电量回调
    @Override
    public void backBatteryData(String battery) {
        try {
            homeBatteryView.setValue(Float.parseFloat(battery));
        }catch (Exception e){
            e.printStackTrace();
        }

    }




    @Override
    public void not200CodeMsg(String msg) {

    }

    @Override
    public void onSuccessData(Object data, int tagCode) {
        if(tagCode == 0x00){
            UserHomeApi.HomeTotalBean homeTotalBean = (UserHomeApi.HomeTotalBean) data;
            showTotalData(homeTotalBean);
        }

        if(tagCode == 0x01){
            List<DeviceApi.UserBindDevice> bindDeviceList = (List<DeviceApi.UserBindDevice>) data;

            if(bindDeviceList == null || bindDeviceList.isEmpty()){
                homeDeviceTypeLayout.setVisibility(View.GONE);
                homeEmptyLayout.setVisibility(View.VISIBLE);
                GlideApp.with(this).load(R.drawable.ic_home_empty_img).into(homeEmptyImgView);
            }else{
                homeDeviceTypeLayout.setVisibility(View.VISIBLE);
                homeEmptyLayout.setVisibility(View.GONE);
                showBindDevice(bindDeviceList.get(0));

              //  showConnStatus();
            }

        }
    }

    @Override
    public void onFailedData(String error) {

    }
    private void showBindDevice(DeviceApi.UserBindDevice device){
        try {
            AppApplication.connDeviceMac = BikeUtils.formatNameToMac(device.getDeviceName());
            MmkvUtils.saveConnDeviceName(device.getDeviceName());
            MmkvUtils.saveConnDeviceMac(AppApplication.connDeviceMac);
            if(device.getDeviceTypeImgUrl() != null)
               GlideApp.with(this).load(device.getDeviceTypeImgUrl()).into(homeDeviceImgView);
            homeDeviceMacTv.setText(AppApplication.connDeviceMac);
            showConnStatus();
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private void showTotalData(UserHomeApi.HomeTotalBean homeTotalBean){
        try {
            homeTotalNumTv.setText(homeTotalBean.getExerciseCount()+"");
            homeTotalTimeTv.setText(BikeUtils.formatSecond2(homeTotalBean.getTotalTime()));
            homeTotalDistanceTv.setText(homeTotalBean.getTripDist() == 0 ? "0" : ( decimalFormat.format( homeTotalBean.getTripDist()/1000)));
            homeTotalCo2Tv.setText(homeTotalBean.getReduceCarbon() == 0 ?"0" : decimalFormat.format(homeTotalBean.getReduceCarbon() / 1000));
            homeTotalCalTv .setText(homeTotalBean.getCalorie() == 0 ? "0" : decimalFormat.format(homeTotalBean.getCalorie() / 1000));
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == null)
                return;

            if(action.equals(BleConstant.HOME_CONN_STATUS_ACTION)){
                showConnStatus();
            }

            //连接中
            if(action.equals(BleConstant.DEVICE_CONNECTNG_ACTION)){
              //  homeDeviceConnTv.setText( "连接中..");
            }
        }
    };
}
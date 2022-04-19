package com.bonlala.ebike.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.bonlala.blelibrary.BleConstant;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.http.api.device.DeviceApi;
import com.bonlala.ebike.http.api.user.LoginPresent;
import com.bonlala.ebike.http.api.user.LoginView;
import com.bonlala.ebike.http.api.weather.OnWeatherBackListener;
import com.bonlala.ebike.http.api.weather.WeatherApi;
import com.bonlala.ebike.ui.ble.BatteryListener;
import com.bonlala.ebike.ui.ble.OnHomeBleConnstatusListener;
import com.bonlala.ebike.utils.BikeUtils;
import com.bonlala.ebike.utils.MmkvUtils;
import com.goodix.ble.libcomx.task.TaskOutput;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.GenericSignatureFormatError;
import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import timber.log.Timber;

/**
 * Created by Admin
 * Date 2022/3/25
 */
public class HomeActivity extends AppActivity implements LoginView {


    private LoginPresent loginPresent;

    private BatteryListener batteryListener;

    private OnHomeBleConnstatusListener onHomeBleConnstatusListener;

    public void setOnHomeBleConnstatusListener(OnHomeBleConnstatusListener onHomeBleConnstatusListener) {
        this.onHomeBleConnstatusListener = onHomeBleConnstatusListener;
    }

    public void setBatteryListener(BatteryListener batteryListener) {
        this.batteryListener = batteryListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_layout;
    }

    @Override
    protected void initView() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void initData() {
        loginPresent = new LoginPresent();
        loginPresent.attachView(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleConstant.BLE_BATTERY_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);

        getUserBindDevice();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    //获取天气
    public void getWeatherData(OnWeatherBackListener onWeatherBackListener){
        long saveTime = MmkvUtils.getWeatherLocalTime();
        if(saveTime != 0 && System.currentTimeMillis()/1000 - saveTime >=3600)
            return;

            //113.854447,22.78397
            EasyHttp.get( this).api(new WeatherApi("深圳市宝安区",22.78397d,113.854447d)).request(new OnHttpListener<String>() {

                @Override
                public void onSucceed(String result) {
                    Timber.e("----获取天气="+result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if(!jsonObject.getString("code").equals("200"))
                            return;
                        String data = jsonObject.getString("data");
                        MmkvUtils.saveLocalTime(System.currentTimeMillis()/1000);
                        WeatherApi.WeatherBean weatherBean = new Gson().fromJson(data,WeatherApi.WeatherBean.class);
                        if(onWeatherBackListener != null)
                            onWeatherBackListener.backCurrWeather(weatherBean);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFail(Exception e) {

                }
            });
    }



    public void getUserBindDevice(){
        loginPresent.getPresentUserBindDevice(this,new DeviceApi().findBindList(),0x00);
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
                if(batteryListener != null)
                    batteryListener.backBatteryData(bleBattery);
            }

        }
    };






    @Override
    public void not200CodeMsg(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void onSuccessData(Object data, int tagCode) {
        List<DeviceApi.UserBindDevice> dtList = (List<DeviceApi.UserBindDevice>) data;
        Timber.e("_-----dt="+new Gson().toJson(dtList));
        if(dtList != null && !dtList.isEmpty()){
            DeviceApi.UserBindDevice userBindDevice = dtList.get(0);
            AppApplication.userBindDeviceHashMap.put(userBindDevice.getMac(),userBindDevice);
            AppApplication.connDeviceMac = userBindDevice.getMac();
        }

    }

    @Override
    public void onFailedData(String error) {
        ToastUtils.show(error);
    }
}

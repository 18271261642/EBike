package com.bonlala.ebike.ui.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonlala.blelibrary.listener.BleConnStatusListener;
import com.bonlala.blelibrary.listener.ConnStatusListener;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.http.GlideApp;
import com.bonlala.ebike.http.api.device.DeviceApi;
import com.bonlala.ebike.http.api.user.LoginPresent;
import com.bonlala.ebike.http.api.user.LoginView;
import com.bonlala.ebike.ui.bean.SearchDeviceBean;
import com.bonlala.ebike.utils.BikeUtils;
import com.bonlala.ebike.utils.MmkvUtils;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 搜索码表页面
 * Created by Admin
 * Date 2022/4/9
 */
public class SearchDeviceActivity extends AppActivity implements LoginView {

    private static final String TAG = "SearchDeviceActivity";

    private RecyclerView searchDeviceRecyclerView;
    private SearchDeviceAdapter searchDeviceAdapter;
    private List<SearchDeviceBean> deviceBeanList;
    private List<String> repeatList;

    private ImageView searchDeviceGifImgView;


    //状态
    private TextView searchDeviceStatusTv;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;

    private LoginPresent loginPresent;


    private final Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x00){
                handler.removeMessages(0x00);
                SearchDeviceBean sbs = (SearchDeviceBean) msg.obj;

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_device_layout;
    }

    @Override
    protected void initView() {
        searchDeviceGifImgView = findViewById(R.id.searchDeviceGifImgView);
        searchDeviceStatusTv = findViewById(R.id.searchDeviceStatusTv);
        searchDeviceRecyclerView = findViewById(R.id.searchDeviceRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchDeviceRecyclerView.setLayoutManager(linearLayoutManager);
        deviceBeanList = new ArrayList<>();
        searchDeviceAdapter = new SearchDeviceAdapter(this,deviceBeanList);
        searchDeviceRecyclerView.setAdapter(searchDeviceAdapter);
        repeatList = new ArrayList<>();

        searchDeviceAdapter.setOnItemClickListener(new SearchDeviceAdapter.OnSearchItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SearchDeviceBean sb = deviceBeanList.get(position);
                if(sb == null)
                    return;
                AppApplication.getBleOperateManager().stopScanDevice();
                showDialog("连接中...");
                connDevice(sb);
                HashMap<String,Object> map = new HashMap<>();
                map.put("deviceName",sb.getBluetoothDevice().getName());
                map.put("deviceType","C078");
                map.put("mac",sb.getBluetoothDevice().getAddress());


                loginPresent.bindAndUnBindDevice(SearchDeviceActivity.this,new DeviceApi().bindDevice(sb.getBluetoothDevice().getName(),"C078",sb.getBluetoothDevice().getAddress()),new Gson().toJson(map),0x00);

            }
        });

        searchDeviceStatusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanDevice();
            }
        });


        GlideApp.with(this).load(R.drawable.ic_search_device).into(searchDeviceGifImgView);

    }


    @Override
    protected void onStop() {
        super.onStop();
        AppApplication.getBleOperateManager().stopScanDevice();
    }

    private void connDevice(SearchDeviceBean searchD){
        MmkvUtils.saveConnDeviceMac(searchD.getBluetoothDevice().getAddress());
        MmkvUtils.saveConnDeviceName(searchD.getBluetoothDevice().getName());
        AppApplication.getBleOperateManager().setBleConnStatusListener(new BleConnStatusListener() {
            @Override
            public void onConnectStatusChanged(String mac, int status) {

            }
        });
        AppApplication.getBleOperateManager().connYakDevice(searchD.getBluetoothDevice(), new ConnStatusListener() {
            @Override
            public void connStatus(int status) {

            }

            @Override
            public void setNoticeStatus(int code) {

                MmkvUtils.saveConnDeviceMac(searchD.getBluetoothDevice().getAddress());
                MmkvUtils.saveConnDeviceName(searchD.getBluetoothDevice().getName());
                AppApplication.getInstance().setConnectStatus(true);

            }
        });
    }


    @Override
    protected void initData() {

        loginPresent = new LoginPresent();
        loginPresent.attachView(this);


        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null)
            return;
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null)
            return;
        if (!bluetoothAdapter.isEnabled())
            openBletooth();

        getPermission();

        startScanDevice();
    }
    private void openBletooth() {
        try {
            // 请求打开 Bluetooth
            Intent requestBluetoothOn = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
            requestBluetoothOn
                    .setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            // 设置 Bluetooth 设备可见时间
            requestBluetoothOn.putExtra(
                    BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                    30 * 1000);
            // 请求开启 Bluetooth
            startActivityForResult(requestBluetoothOn,
                    1001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //搜索
    private void startScanDevice(){
        deviceBeanList.clear();
        repeatList.clear();
        AppApplication.getBleOperateManager().scanYakBleDevice(new SearchResponse() {
            @Override
            public void onSearchStarted() {
                searchDeviceStatusTv.setText("开始搜索.");
            }

            @Override
            public void onDeviceFounded(SearchResult searchResult) {
                String nameStr = searchResult.getName();
                if(BikeUtils.isEmpty(nameStr) || nameStr.equals("NULL"))
                    return;
                searchDeviceStatusTv.setText("搜索中...");
                Log.e(TAG,"----nameStr="+nameStr);
                if(!nameStr.toLowerCase(Locale.ROOT).contains("c07"))
                    return;
                if(repeatList.contains(nameStr))
                    return;
                if(repeatList.size()>30)
                    return;

                repeatList.add(nameStr);

                SearchDeviceBean sb = new SearchDeviceBean(searchResult.device,searchResult.rssi);
                deviceBeanList.add(sb);
                Collections.sort(deviceBeanList, new Comparator<SearchDeviceBean>() {
                    @Override
                    public int compare(SearchDeviceBean searchDeviceBean, SearchDeviceBean t1) {
                        return String.valueOf(Math.abs(searchDeviceBean.getRssi())).compareTo(String.valueOf(Math.abs(t1.getRssi())));
                    }
                });
                searchDeviceAdapter.notifyDataSetChanged();

            }

            @Override
            public void onSearchStopped() {
                searchDeviceStatusTv.setText("搜索完成");
            }

            @Override
            public void onSearchCanceled() {
                searchDeviceStatusTv.setText("搜索完成");
            }
        },15 * 1000,1);
    }


    private void getPermission(){
        XXPermissions.with(this).permission(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION).request(new OnPermissionCallback() {
            @Override
            public void onGranted(List<String> permissions, boolean all) {

            }
        });

    }




    @Override
    public void not200CodeMsg(String msg) {
        hideDialog();
        ToastUtils.show(msg);
        AppApplication.getBleOperateManager().disConnYakDevice();
        MmkvUtils.saveConnDeviceName("");
        MmkvUtils.saveConnDeviceMac("");
    }

    @Override
    public void onSuccessData(Object data, int tagCode) {
        if(tagCode == 0x00){
            AppApplication.connDeviceMac = MmkvUtils.getConnDeviceMac();
            finish();
        }
    }

    @Override
    public void onFailedData(String error) {
        hideDialog();
        AppApplication.getBleOperateManager().disConnYakDevice();
        MmkvUtils.saveConnDeviceName("");
        MmkvUtils.saveConnDeviceMac("");
    }
}

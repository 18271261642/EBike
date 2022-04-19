package com.bonlala.ebike.ui.ble;

import android.view.View;
import android.widget.TextView;

import com.bonlala.base.BaseAdapter;
import com.bonlala.blelibrary.BleConstant;
import com.bonlala.blelibrary.BleOperateManager;
import com.bonlala.blelibrary.listener.WriteBackDataListener;
import com.bonlala.blelibrary.listener.WriteCommBackListener;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.widget.layout.WrapRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 表盘页面
 * Created by Admin
 * Date 2022/4/11
 */
public class DialManagerActivity extends AppActivity {

    private WrapRecyclerView dialRecyclerView;
    private DialAdapter dialAdapter;
    private List<DialBean> beanList;

    private BleOperateManager bleOperateManager;

    private TextView currDialTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dial_manager_layout;
    }

    @Override
    protected void initView() {
        findViews();

    }

    @Override
    protected void initData() {
        bleOperateManager = AppApplication.getBleOperateManager();


        DialBean d1 = new DialBean(String.valueOf(R.drawable.ic_device_dial_1),"",true);
        DialBean d2 = new DialBean(String.valueOf(R.drawable.ic_device_dial_2),"",false);
        DialBean d3 = new DialBean(String.valueOf(R.drawable.ic_device_dial_3),"",false);
        beanList.add(d1);
        beanList.add(d2);
        beanList.add(d3);

        dialAdapter.setData(beanList);

        bleOperateManager.getDeviceCurrentDial(new WriteCommBackListener() {
            @Override
            public void backCommData(int... value) {

            }

            @Override
            public void backCommStrDate(String... value) {
                currDialTv.setText("当前表盘返回="+value[0]);
            }
        });
    }

    private void findViews(){
        currDialTv = findViewById(R.id.currDialTv);
        dialRecyclerView = findViewById(R.id.dialRecyclerView);
        beanList = new ArrayList<>();
        dialAdapter = new DialAdapter(this);
        dialAdapter.setOnItemClickListener(onItemClickListener);
        dialRecyclerView.setAdapter(dialAdapter);

    }

    private BaseAdapter.OnItemClickListener onItemClickListener = new BaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
            for(int i = 0;i<beanList.size();i++){
                DialBean db = beanList.get(i);
                db.setCurrentDial(i == position);
            }
            dialAdapter.setData(beanList);

            setDialPosition(position);
        }
    };



    private void setDialPosition(int position){
        bleOperateManager.writeBleCommonByte(BleConstant.setDeviceDial(position + 1), new WriteBackDataListener() {
            @Override
            public void backWriteData(byte[] data) {

            }
        });
    }
}

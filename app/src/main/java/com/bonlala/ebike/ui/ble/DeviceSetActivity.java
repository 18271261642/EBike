package com.bonlala.ebike.ui.ble;



import android.view.View;
import android.widget.TextView;

import com.bonlala.base.BaseDialog;
import com.bonlala.blelibrary.BleConstant;
import com.bonlala.blelibrary.BleOperateManager;
import com.bonlala.blelibrary.SetBean;
import com.bonlala.blelibrary.Utils;
import com.bonlala.blelibrary.listener.WriteBackDataListener;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.dialog.DoubleDialogVIew;
import com.bonlala.ebike.dialog.InputDialog;
import com.bonlala.ebike.dialog.SexDialogView;
import com.bonlala.ebike.dialog.SinalDialogVIew;
import com.bonlala.ebike.http.api.device.DeviceApi;
import com.bonlala.ebike.http.api.user.LoginPresent;
import com.bonlala.ebike.http.api.user.LoginView;
import com.bonlala.ebike.ui.view.MenuItemVIew;
import com.bonlala.ebike.utils.MmkvUtils;
import com.bonlala.ebike.utils.SetConstant;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


/**
 * 码表设置页面
 * Created by Admin
 * Date 2022/4/10
 */
public class DeviceSetActivity extends AppActivity implements LoginView {


    private LoginPresent loginPresent;

    private BleOperateManager bleOperateManager;

    private MenuItemVIew menuSetUnitItem,menuSetBackLevelItem,menuSetAutoPowerItem,
            menuBikeWheelItem,menuLimitSpeedItem,menuHelpGearItem,menuSetUuidItem,
            menuSetNameItem,menuSetOtaItem,menuSetDialItem;

    private final SetConstant setConstant = new SetConstant();


    private final SetBean setBean = new SetBean();

    private TextView commSetTv;

    //单项选中
    private SinalDialogVIew sinalDialogVIew;
    //双项选中
    private DoubleDialogVIew doubleDialogVIew;

    @Override
    protected int getLayoutId() {
        return R.layout.activitiy_device_set_layout;
    }

    @Override
    protected void initView() {

        commSetTv = findViewById(R.id.commSetTv);
        findViews();

        showDefaultValue();
    }

    @Override
    protected void initData() {
        loginPresent = new LoginPresent();
        loginPresent.attachView(this);


        if(bleOperateManager == null)
            bleOperateManager = AppApplication.getBleOperateManager();


        //读取码表设置
        bleOperateManager.writeBleCommonByte(BleConstant.getCommSetData(), new WriteBackDataListener() {
            @Override
            public void backWriteData(byte[] data) {
                if(data.length<8)
                    return;
                if((data[0] & 0xff) == 170 && data[1] == 31)
                analysisCommSet(data);
            }
        });
    }

    //aa 1f 02000000000200081a04
    private void analysisCommSet(byte[] dt){
        try {
            //总里程 4个byte
            int countDistance = Utils.getIntFromBytes(dt[5],dt[4],dt[3],dt[2]);
            //单位 0 km
            int unit = dt[6] & 0xff;
            //背光等级
            int backLevel = dt[7] &0xff;
            //自动关机
            int autoPower = dt[8] & 0xff;
            //轮径
            int wheelRadius = dt[9] & 0xff;
            //限速档位
            int limitSpeed = dt[10] & 0xff;

            String str = "----解析="+countDistance+"\n"+unit+"\n"+backLevel+"\n"+autoPower+"\n"+wheelRadius+"\n"+limitSpeed;

            Timber.e(str);

            setBean.setUnit(unit);
            setBean.setBackLightLevel(backLevel+"");
            //自动关机
            ArrayList<String> autoPowerList = setConstant.getAutoPowerOffSource();
            String autoStr = null;
            for(int i = 0;i<autoPowerList.size();i++){
                if(i == autoPower){
                    autoStr = autoPowerList.get(i);
                }
            }
            //轮径
            String wheelStr = null;
            ArrayList<String> wheelRadiusLt = setConstant.getWheelSource();
            for(int k = 0;k<wheelRadiusLt.size();k++){
                if(k == wheelRadius){
                    wheelStr = wheelRadiusLt.get(k);
                }
            }
            //自动关机
            setBean.setAutoPowerOff(autoStr);
            setBean.setWheelRadius(wheelStr);
            setBean.setLimitSpeed(limitSpeed);

            commSetTv.setText(str);
            showDefaultValue();
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    private void showDefaultValue(){
        //名称
        menuSetNameItem.setRightTxt(MmkvUtils.getConnDeviceName());


        //单位
        menuSetUnitItem.setRightTxt(setBean.getUnit() == 0 ? "公制" : "英制");
        //背光等级
        menuSetBackLevelItem.setRightTxt(setBean.getBackLightLevel());
        //自动关机时间
        menuSetAutoPowerItem.setRightTxt(setBean.getAutoPowerOff());
        //轮径设置
        menuBikeWheelItem.setRightTxt(setBean.getWheelRadius()+"");
        //阻力档位设置
        menuHelpGearItem.setRightTxt(setBean.getGearLevel());
        //限速设置
        menuLimitSpeedItem.setRightTxt(setBean.getLimitSpeed()+"");

        //公英制
        menuSetUnitItem.setRightTxt(setBean.getUnit() == 0 ? "公制" : "英制");
    }


    private void findViews(){
        menuSetDialItem = findViewById(R.id.menuSetDialItem);
        menuSetOtaItem = findViewById(R.id.menuSetOtaItem);
        menuSetUnitItem = findViewById(R.id.menuSetUnitItem);
        menuSetBackLevelItem = findViewById(R.id.menuSetBackLevelItem);
        menuSetAutoPowerItem = findViewById(R.id.menuSetAutoPowerItem);
        menuBikeWheelItem = findViewById(R.id.menuBikeWheelItem);
        menuLimitSpeedItem = findViewById(R.id.menuLimitSpeedItem);
        menuHelpGearItem = findViewById(R.id.menuHelpGearItem);

        menuSetNameItem = findViewById(R.id.menuSetNameItem);
        menuSetUuidItem = findViewById(R.id.menuSetUuidItem);

        setOnClickListener(menuSetUnitItem,menuSetBackLevelItem,menuSetAutoPowerItem,
                menuBikeWheelItem,menuLimitSpeedItem,menuHelpGearItem
                ,menuSetUuidItem,menuSetDialItem,
                menuSetNameItem,menuSetOtaItem);
    }


    //解绑
    @Override
    public void onRightClick(View view) {
        unBindDevice();
    }


    @Override
    public void onClick(View view) {
        if(view == menuSetDialItem){    //表盘设置
            startActivity(DialManagerActivity.class);
        }
        if(view == menuSetOtaItem){ //OTA
            startActivity(DeviceDfuActivity.class);
        }

        if(view == menuSetUnitItem){    //单位
            setItemSelect(setConstant.getUnitSource(),"设置单位",0,setBean.getUnit() == 0 ? "公制" : "英制");
        }

        if(view == menuSetBackLevelItem){   //背光
            setItemSelect(setConstant.getBackLightInterval(),"设置背光等级",1,setBean.getBackLightLevel());
        }

        if(view == menuSetAutoPowerItem){   //自动关机
            setItemSelect(setConstant.getAutoPowerOffSource(),"设置自动关机时间",2,setBean.getAutoPowerOff());
        }

        if(view == menuBikeWheelItem){  //轮径
            setItemSelect(setConstant.getWheelSource(),"设置轮径",3,setBean.getWheelRadius());
        }

        if(view == menuLimitSpeedItem){ //限速
            setItemSelect(setConstant.getLimitSpeedSource(),"设置限速",4,setBean.getLimitSpeed()+"");
        }

        if(view == menuHelpGearItem){   //助力档位
            setItemSelect(setConstant.getGearSource(),"助力档位设置",5,setBean.getGearLevel());
        }


        if(view == menuSetNameItem){    //设备名称
           // showInputDialog();
        }

        if(view == menuSetUuidItem){    //uuid

        }
    }


    private void showInputDialog(){
        new InputDialog.Builder(this)
                .setHint("输入名称")
                .setTitle("设置名称")
                .setCancelable(false)
                .setWhiteBg(true)
                .setListener(new InputDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        menuSetNameItem.setRightTxt(content);
                    }
                }).create().show();
    }





    private void setItemSelect(ArrayList<String> dataSource, String txt, int type, String defaultValue){
        int defaultPosition = 0;
        for(int d = 0;d<dataSource.size();d++){
            if(dataSource.get(d).equals(defaultValue)){
                defaultPosition = d;
            }
        }
        if(sinalDialogVIew == null)
            sinalDialogVIew = new SinalDialogVIew(getActivity(),R.style.edit_AlertDialog_style);
        sinalDialogVIew.show();
        sinalDialogVIew.setCancelable(false);
        sinalDialogVIew.setTitleTxt(txt);
        sinalDialogVIew.setUnitShow(type == 4,"km/h");
        sinalDialogVIew.setContentScrollData(dataSource);
        sinalDialogVIew.setDefaultSelect(defaultPosition);
        sinalDialogVIew.setOnSelectListener(new SinalDialogVIew.OnSelectListener() {
            @Override
            public void backSelectData(String data) {
                sinalDialogVIew.dismiss();
                setOperateData(dataSource,data,type);
            }
        });
    }


    private void setOperateData(List<String> lt , String data, int type){
        int position = 0;
        for(int i = 0;i<lt.size();i++){
            if( lt.get(i).equals(data)){
                position = i;
            }
        }

//        //性别
//        if(type == -2){
//            setBean.setSex(position);
//            sexTv.setText(data);
//        }
//        if(type == -3){     //身高
//            setBean.setHeight(Integer.valueOf(StringUtils.substringBefore(data,"cm")));
//            heightTv.setText(data);
//        }
//        if(type == -4){ //体重
//            setBean.setWeight(Integer.valueOf(StringUtils.substringBefore(data,"kg")));
//            weightTv.setText(data);
//        }


        if(type == 0){      //公英制
            setBean.setUnit(position);
            menuSetUnitItem.setRightTxt(data);
            writeCommData(BleConstant.setUnit(data.equals("公制")));
        }
        if(type == 1){  //背光等级
            setBean.setBackLightLevel(data);
            menuSetBackLevelItem.setRightTxt(data);
            writeCommData(BleConstant.setBackLight(Integer.parseInt(data)-1));
        }

        if(type == 2){  //自动关机时间
            setBean.setAutoPowerOff(data);
            menuSetAutoPowerItem.setRightTxt(data);
            writeCommData(BleConstant.setLightOff(position));
        }

        if(type == 3){  //设置轮径
            setBean.setWheelRadius(data);
            menuBikeWheelItem.setRightTxt(data);
            writeCommData(BleConstant.setBikeWheel(position));
        }
        if(type == 4){  //限速设置
            setBean.setLimitSpeed(Integer.parseInt(data));
            menuLimitSpeedItem.setRightTxt(data+"");
            writeCommData(BleConstant.setLimitSpeed(Integer.parseInt(data)));
        }

        if(type == 5){  //助力档位设置
            setBean.setGearLevel(data);
            menuHelpGearItem.setRightTxt(data);
            writeCommData(BleConstant.setGear(position));
        }


//        //设置大灯
//        if(type == 9){  //设置大灯
//          //  setBean.setLightStatus(position);
//            menuSetLightItem.setRightTxt(data);
//            writeCommData(BleConstant.setLightStatus(position ==1));
//        }
    }

    private void writeCommData(byte[] bytes){
        if(!AppApplication.getInstance().isConnectStatus()){
            ToastUtils.show("设备未连接!");
            return;
        }
        showDialog();
        bleOperateManager.writeBleCommonByte(bytes, new WriteBackDataListener() {
            @Override
            public void backWriteData(byte[] data) {
              hideDialog();
            }
        });
    }


    private void unBindDevice(){


     new SexDialogView.Builder(this)
             .setIsWhiteBg(true)
             .setTitle("提醒")
             .setLeftAndRightTxt("取消","确定")
             .setContentTxt("是否断开连接?")
             .setOnSexSelectListener(new SexDialogView.OnSexSelectListener() {
                 @Override
                 public void selectSex(int sex) {
                    if(sex == 1){   //是
                        showDialog();
                        if(AppApplication.connDeviceMac != null){
                            Map<String,Object> map = new HashMap<>();
                            map.put("deviceName",MmkvUtils.getConnDeviceName());
                            map.put("deviceType","C078");
                            map.put("mac",AppApplication.connDeviceMac);
                            loginPresent.bindAndUnBindDevice(DeviceSetActivity.this,new DeviceApi().unBindDevice(),new Gson().toJson(map),0x00);
                        }
                    }
                 }
             }).create().show();
    }




    @Override
    public void not200CodeMsg(String msg) {
        hideDialog();
        ToastUtils.show(msg);
    }

    @Override
    public void onSuccessData(Object data, int tagCode) {
        hideDialog();
        if(tagCode == 0x00){
            AppApplication.getBleOperateManager().disConnYakDevice();
            MmkvUtils.saveConnDeviceName("");
            MmkvUtils.saveConnDeviceMac("");
            finish();
        }

    }

    @Override
    public void onFailedData(String error) {
        hideDialog();
        ToastUtils.show(error);
    }
}

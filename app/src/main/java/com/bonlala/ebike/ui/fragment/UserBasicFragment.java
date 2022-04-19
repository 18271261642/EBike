package com.bonlala.ebike.ui.fragment;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bonlala.base.BaseActivity;
import com.bonlala.base.BaseDialog;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.TitleBarFragment;
import com.bonlala.ebike.dialog.DateDialog;
import com.bonlala.ebike.dialog.HeightSelectDialog;
import com.bonlala.ebike.dialog.SexDialogView;
import com.bonlala.ebike.http.api.user.InitUserInfoApi;
import com.bonlala.ebike.http.api.user.JumpApi;
import com.bonlala.ebike.http.api.user.OnGetUserInfoListener;
import com.bonlala.ebike.ui.activity.SelectCountryActivity;
import com.bonlala.ebike.ui.activity.SetUserInfoActivity;
import com.bonlala.ebike.utils.BikeUtils;
import com.bonlala.widget.view.ClearEditText;
import com.google.gson.Gson;

import java.util.Objects;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 设置基础的信息，身高体重等
 * Created by Admin
 * Date 2022/3/26
 */
public class UserBasicFragment extends TitleBarFragment<SetUserInfoActivity> implements OnGetUserInfoListener {


    private InitUserInfoApi.UserInfoData uDataInfo;


    private TextView userBasicMiTv,userBasicKmTv;

    //下一步
    private ImageView userBasicNextImg;
    //性别
    private ConstraintLayout userBasicSexLayout;
    //生日
    private ConstraintLayout userBasicBirthdayLayout;
    private TextView userBasicBirthdayTv;
    //身高
    private ConstraintLayout userBasicHeightLayout;
    private TextView userBasicHeightTv;
    //体重
    private ConstraintLayout userBasicWeightLayout;
    private TextView userBasicWeightTv;

    //昵称
    private ClearEditText userBasicUserNameTv;
    //性别
    private TextView userBasicSexTv;

    //国家选择
    private ConstraintLayout userBasicCountryLayout;
    private TextView userBasicCountryTv;

    public static UserBasicFragment getInstance(){
        return new UserBasicFragment();
    }



    //身高，体重选择
    private HeightSelectDialog.Builder heightSelectDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_basic_layout;
    }

    @Override
    protected void initView() {

        userBasicCountryTv = findViewById(R.id.userBasicCountryTv);
        userBasicNextImg = findViewById(R.id.userBasicNextImg);
        userBasicSexLayout = findViewById(R.id.userBasicSexLayout);
        userBasicBirthdayLayout = findViewById(R.id.userBasicBirthdayLayout);
        userBasicHeightLayout = findViewById(R.id.userBasicHeightLayout);
        userBasicWeightLayout = findViewById(R.id.userBasicWeightLayout);
        userBasicWeightTv = findViewById(R.id.userBasicWeightTv);
        userBasicHeightTv = findViewById(R.id.userBasicHeightTv);
        userBasicBirthdayTv = findViewById(R.id.userBasicBirthdayTv);
        userBasicUserNameTv = findViewById(R.id.userBasicUserNameTv);
        userBasicSexTv = findViewById(R.id.userBasicSexTv);
        userBasicCountryLayout = findViewById(R.id.userBasicCountryLayout);
        userBasicMiTv = findViewById(R.id.userBasicMiTv);
        userBasicKmTv  = findViewById(R.id.userBasicKmTv);



        setOnClickListener(userBasicNextImg,userBasicSexLayout,userBasicBirthdayLayout,
                userBasicHeightLayout,userBasicWeightLayout,userBasicCountryLayout,userBasicKmTv,userBasicMiTv);
    }

    @Override
    protected void initData() {
        getAttachActivity().setGetUserInfoListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            uDataInfo = getAttachActivity().getUserInfoData();
            if(uDataInfo == null)
                return;
            userBasicUserNameTv.setText(uDataInfo.getNickname());
            userBasicSexTv.setText(uDataInfo.getGender() == 0 ? "男" : "女");
            userBasicBirthdayTv.setText(BikeUtils.getFormatDate(uDataInfo.getBirthday() * 1000,"yyyy-MM-dd"));
            userBasicWeightTv.setText(uDataInfo.getWeight()+" kg");
            userBasicHeightTv.setText(uDataInfo.getHeight()+" cm");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();

    }


    private void selectUnit(boolean isKm){
        userBasicKmTv.setBackground(getResources().getDrawable(isKm ? R.drawable.login_phone_checked : R.drawable.login_normal));
        userBasicMiTv.setBackground(getResources().getDrawable(isKm ? R.drawable.login_normal : R.drawable.login_phone_checked));
    }


    @Override
    public void onClick(View view) {

        if(view == userBasicMiTv){
            selectUnit(false);
        }

        if(view == userBasicKmTv){
            selectUnit(true);
        }



        if(view == userBasicNextImg){
            //用户名
            getAttachActivity().getUserInfoData().setNickname(userBasicUserNameTv.getText().toString());

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            UserPowerFragment userPowerFragment = UserPowerFragment.getInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("bg_status",0);
            userPowerFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.userFragmentLayout,userPowerFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

        if(view == userBasicCountryLayout){ //国家
            startActivityForResult(SelectCountryActivity.class, new BaseActivity.OnActivityCallback() {
                @Override
                public void onActivityResult(int resultCode, @Nullable Intent data) {
                    if(resultCode == 0x00){
                       if(data == null)
                           return;
                        int countryId = data.getIntExtra("select_country_id",0);
                        String countryStr = data.getStringExtra("select_country");
                        userBasicCountryTv.setText(countryStr);
                       getAttachActivity().getUserInfoData().setCountryId(countryId);
                        getAttachActivity().getDefaultUserInfo().setCountryId(countryId);
                    }
                }
            });

        }

        if(view == userBasicSexLayout){ //性别
            new SexDialogView.Builder(getAttachActivity()).setThemeStyle(R.style.edit_AlertDialog_style)
                    .setCancelable(false)
                    .setBgAlpha(false)
                    .setOnSexSelectListener(new SexDialogView.OnSexSelectListener() {
                        @Override
                        public void selectSex(int sex) {
                            userBasicSexTv.setText(sex == 0 ? "男" : "女");
                            getAttachActivity().getDefaultUserInfo().setGender(sex);
                            getAttachActivity().getUserInfoData().setGender(sex);
                        }
                    })
                    .create().show();

        }

        if(view == userBasicBirthdayLayout){
            new DateDialog.Builder(getAttachActivity())
                    .setBgColor(false)
                    .setThemeStyle(R.style.edit_AlertDialog_style).setListener(new DateDialog.OnListener() {
                @Override
                public void onSelected(BaseDialog dialog, int year, int month, int day) {
                    String birthStr = year+"-"+String.format("%02d",month)+"-"+day;
                    userBasicBirthdayTv.setText(birthStr);
                    getAttachActivity().getDefaultUserInfo().setBirthday(BikeUtils.transToDate(birthStr));
                    getAttachActivity().getUserInfoData().setBirthday((int) BikeUtils.transToDate(birthStr));

                }
            }).show();
        }

        //身高
        if(view == userBasicHeightLayout){
            selectHeightAndWeight(true);
        }

        //体重
        if(view == userBasicWeightLayout){
            selectHeightAndWeight(false);
        }
    }

    private void selectHeightAndWeight(boolean isHeight){
        if(heightSelectDialog == null)
            heightSelectDialog = new HeightSelectDialog.Builder(getAttachActivity());
        Objects.requireNonNull(heightSelectDialog.setCancelable(false)
                .setUnitShow(true, isHeight ? "cm" : "kg"))
                .setSelectItemColor(false)
                .setSignalSelectListener(new HeightSelectDialog.SignalSelectListener() {
                    @Override
                    public void onSignalSelect(String txt) {
                        if(isHeight){
                            getAttachActivity().getDefaultUserInfo().setHeight(Integer.parseInt(txt.trim()));
                            getAttachActivity().getUserInfoData().setHeight(Integer.parseInt(txt.trim()));
                            userBasicHeightTv.setText(txt);
                        }else{
                            userBasicWeightTv.setText(txt);
                            getAttachActivity().getDefaultUserInfo().setWeight(Integer.parseInt(txt.trim()));
                            getAttachActivity().getUserInfoData().setWeight(Integer.parseInt(txt.trim()));
                        }
                    }
                }).show();
    }

    @Override
    public void getUserInfoData(InitUserInfoApi.UserInfoData userInfoData) {
        try {
            if(userInfoData == null)
                return;
            uDataInfo = userInfoData;
            if(uDataInfo.getCadenceRegion() == null)
                return;
            userBasicUserNameTv.setText(userInfoData.getNickname());
            userBasicSexTv.setText(userInfoData.getGender() == null ?"男" : userInfoData.getGender() == 0 ? "男" : "女");
            userBasicBirthdayTv.setText(BikeUtils.getFormatDate(userInfoData.getBirthday() ,"yyyy-MM-dd"));
            userBasicWeightTv.setText(userInfoData.getWeight()+" kg");
            userBasicHeightTv.setText(userInfoData.getHeight()+" cm");

            Gson gson = new Gson();
            String cadenceRegionStr = gson.toJson(userInfoData.getCadenceRegion());
            String heartRateRegionStr = gson.toJson(userInfoData.getHeartRateRegion());
            String powerRegion = gson.toJson(userInfoData.getPowerRegion());


            JumpApi.DefaultUserInfo defaultUserInfo = getAttachActivity().getDefaultUserInfo();

            JumpApi.DefaultUserInfo.CadenceRegionBean cadenceRegionBean = gson.fromJson(cadenceRegionStr,JumpApi.DefaultUserInfo.CadenceRegionBean.class);
            JumpApi.DefaultUserInfo.HeartRateRegionBean heartRateRegionBean = gson.fromJson(heartRateRegionStr,JumpApi.DefaultUserInfo.HeartRateRegionBean.class);
            JumpApi.DefaultUserInfo.PowerRegionBean powerRegionBean = gson.fromJson(powerRegion,JumpApi.DefaultUserInfo.PowerRegionBean.class);
            defaultUserInfo.setCadenceRegion(cadenceRegionBean);
            defaultUserInfo.setHeartRateRegion(heartRateRegionBean);
            defaultUserInfo.setPowerRegion(powerRegionBean);
            getAttachActivity().setDefaultUserInfo(defaultUserInfo);


        }catch (Exception e){
            e.printStackTrace();
        }


    }


}

package com.bonlala.ebike.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonlala.base.BaseActivity;
import com.bonlala.base.BaseDialog;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.dialog.DateDialog;
import com.bonlala.ebike.dialog.HeightSelectDialog;
import com.bonlala.ebike.dialog.SexDialogView;
import com.bonlala.ebike.http.GlideApp;
import com.bonlala.ebike.http.api.user.CountryApi;
import com.bonlala.ebike.http.api.user.GetLoginInfoApi;
import com.bonlala.ebike.http.api.user.GetOssApi;
import com.bonlala.ebike.http.api.user.LoginPresent;
import com.bonlala.ebike.http.api.user.LoginView;
import com.bonlala.ebike.http.oss.AliyunManager;
import com.bonlala.ebike.utils.BikeUtils;
import com.bonlala.ebike.utils.GetJsonDataUtil;
import com.bonlala.ebike.utils.MmkvUtils;
import com.bonlala.widget.view.ClearEditText;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import timber.log.Timber;


/**
 * 个人信息页面
 * Created by Admin
 * Date 2022/3/31
 */
public class PersonalActivity extends AppActivity implements LoginView<GetLoginInfoApi.UserInfoData> {


    private LoginPresent loginPresent;

    private GetLoginInfoApi.UserInfoData userInfoData;

    //ossToken
    private GetOssApi.OssTokenBean ossTokenBean;
    private AliyunManager aliyunManager;


    private TitleBar toolbar;
    //头像的图片imgView
    private ImageView headImgView;
    //顶部背景图片
    private AppCompatImageView personalTopImgView;

    //身高，体重选择
    private HeightSelectDialog.Builder heightSelectDialog;

    //国家
    private TextView personalCountryTv;
    //昵称
    private ClearEditText personalNameTv;
    //性别
    private TextView personalSexTv;
    //生日
    private TextView personalBirthTv;
    //身高内容的tv
    private TextView personalHeightTv;
    //体重
    private TextView personalWeightTv;
    //简介内容
    private ClearEditText personalDescContentTv;
    //简介计数
    private TextView descNumberTv;

    //国家列表数据
    private  List<CountryApi.CountryItemBean> countryItemBeanList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_layout;
    }

    @Override
    protected void initView() {
        descNumberTv = findViewById(R.id.descNumberTv);
        personalTopImgView = findViewById(R.id.personalTopImgView);
        headImgView = findViewById(R.id.personalImgView);
        personalHeightTv = findViewById(R.id.personalHeightTv);
        personalWeightTv = findViewById(R.id.personalWeightTv);
        personalCountryTv = findViewById(R.id.personalCountryTv);
        personalSexTv = findViewById(R.id.personalSexTv);
        personalBirthTv = findViewById(R.id.personalBirthTv);
        personalNameTv = findViewById(R.id.personalNameTv);
        personalCountryTv = findViewById(R.id.personalCountryTv);
        personalDescContentTv = findViewById(R.id.personalDescContentTv);


        //国家
        findViewById(R.id.personalCountryLayout);
        //生日
        findViewById(R.id.personalBirthLayout);
        //修改背景
        findViewById(R.id.personalUpBgTv);
        //修改头像
        findViewById(R.id.personalHdLayout);
        //身高
        findViewById(R.id.personalHeightLayout);
        //体重
        findViewById(R.id.personalWeightLayout);
        //性别
        findViewById(R.id.personalSexLayout);

        setOnClickListener(R.id.personalBirthLayout,R.id.personalUpBgTv,R.id.personalSexLayout,
                R.id.personalHdLayout,R.id.personalHeightLayout,R.id.personalWeightLayout,
                R.id.personalCountryLayout);


        toolbar = findViewById(R.id.tv_home_title);
        toolbar.setLineVisible(false);
        // 给这个 ToolBar 设置顶部内边距，才能和 TitleBar 进行对齐
        ImmersionBar.setTitleBar(this, toolbar);

       this.getStatusBarConfig()
                .statusBarDarkFont(false)
                .init();

       XXPermissions.with(this).permission(Manifest.permission.CAMERA).request(onPermissionCallback);

       XXPermissions.with(this).permission(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE).request(onPermissionCallback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            XXPermissions.with(this).permission(Manifest.permission.MANAGE_EXTERNAL_STORAGE).request(onPermissionCallback);
        }

        personalDescContentTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if(str == null)
                    return;
                descNumberTv.setText(str.length()+"/200");
            }
        });

    }

    @Override
    protected void initData() {
        loginPresent = new LoginPresent();
        loginPresent.attachView(this);


        String countryStr = new GetJsonDataUtil().getJson(this,"country.json");
        if(countryStr != null){
           countryItemBeanList = new Gson().fromJson(countryStr,new TypeToken<List<CountryApi.CountryItemBean>>(){}.getType());

        }

        showDialog();
        loginPresent.getLoginInfoData(this,new GetLoginInfoApi(),0x00);

        loginPresent.getPresentAliOssToken(this,new GetOssApi(),0x01);
    }

    @Override
    protected boolean isStatusBarEnabled() {
        return super.isStatusBarEnabled();
    }

    @Override
    protected boolean isStatusBarDarkFont() {
        return true;
    }


    @Override
    public void onLeftClick(View view) {
       finish();
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.personalCountryLayout){ //国家
            selectCountry();
        }
        if (view.getId() == R.id.personalUpBgTv) {    //修改背景
            startToSelectImg(0);
        }

        if (view.getId() == R.id.personalHdLayout) {  //修改头像
            startToSelectImg(1);
        }

        if (view.getId() == R.id.personalBirthLayout) {   //生日
            AppApplication.getInstance().setDateSelectWhite(true);
            new DateDialog.Builder(this)
                    .setBgColor(true)
                    .setThemeStyle(R.style.edit_AlertDialog_style).setListener(new DateDialog.OnListener() {
                @Override
                public void onSelected(BaseDialog dialog, int year, int month, int day) {
                    String birthStr = year+"-"+String.format("%02d",month)+"-"+String.format("%02d",day);
                    personalBirthTv.setText(birthStr);
                    if(userInfoData == null)
                        return;
                    //2147483647
                    //1206973440
                    //1650038400000

                    Timber.e("----BikeUtils.transToDate(birthStr)="+BikeUtils.transToDate(birthStr));

                    userInfoData.setBirthday( BikeUtils.transToDate(birthStr));

                }
            }).show();
        }

        if (view.getId() == R.id.personalHeightLayout) {  //身高
            selectHeightAndWeight(true);
        }
        if (view.getId() == R.id.personalWeightLayout) {  //体重
            selectHeightAndWeight(false);
        }
        //性别
        if (view.getId() == R.id.personalSexLayout) {
            new SexDialogView.Builder(this).setThemeStyle(R.style.edit_AlertDialog_style)
                    .setCancelable(false)
                    .setBgAlpha(true)
                    .setOnSexSelectListener(new SexDialogView.OnSexSelectListener() {
                        @Override
                        public void selectSex(int sex) {
                            if(userInfoData == null)
                                return;
                            userInfoData.setGender(sex);
                            personalSexTv.setText(sex == 0 ? "男" : "女");
                        }
                    })
                    .create().show();

        }
    }


    //选择国家
    private void selectCountry(){
        startActivityForResult(SelectCountryActivity.class, new BaseActivity.OnActivityCallback() {
            @Override
            public void onActivityResult(int resultCode, @Nullable Intent data) {
                if(resultCode == 0x00){
                    if(data == null)
                        return;
                    int countryId = data.getIntExtra("select_country_id",0);
                    String countryStr = data.getStringExtra("select_country");
                    personalCountryTv.setText(countryStr);
                    userInfoData.setCountryId(countryId);
                }
            }
        });
    }



    //选择图片
    private void startToSelectImg(int code){

        XXPermissions.with(this).permission(Manifest.permission.READ_EXTERNAL_STORAGE).request(onPermissionCallback);


        ImageSelectActivity.start(this, new ImageSelectActivity.OnPhotoSelectListener() {
            @Override
            public void onSelected(List<String> data) {
                setSelectImg(data.get(0),code);
            }
        });
    }


    private void setSelectImg(String localUrl,int code){
        if(code == 0){ //背景
           // personalTopImgView.seti
            GlideApp.with(this).load(new File(localUrl)).into(personalTopImgView);

            if(aliyunManager == null)
                return;
            aliyunManager.upLoadFile(localUrl,0,"bg_img.png");
        }

        if(code == 1){  //头像
            // 显示圆角的 ImageView
            GlideApp.with(this)
                    .load(localUrl)
                    .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners((int) getResources().getDimension(R.dimen.dp_10))))
                    .into(headImgView);

            if(aliyunManager == null)
                return;
            aliyunManager.upLoadFile(localUrl,1,"head_img.png");
        }
    }



    //选择身高和体重
    private void selectHeightAndWeight(boolean isHeight){
        if(heightSelectDialog == null)
            heightSelectDialog = new HeightSelectDialog.Builder(this);
        Objects.requireNonNull(heightSelectDialog.setCancelable(false)
                .setUnitShow(true, isHeight ? "cm" : "kg"))
                .setSelectItemColor(true)
                .setSignalSelectListener(new HeightSelectDialog.SignalSelectListener() {
                    @Override
                    public void onSignalSelect(String txt) {
                        if(isHeight){
                            personalHeightTv.setText(txt+" cm");
                            userInfoData.setHeight(Integer.parseInt(txt));
                        }else{
                           personalWeightTv.setText(txt +" kg");
                           userInfoData.setWeight(Integer.parseInt(txt));
                        }
                    }
                }).show();
    }

    private final OnPermissionCallback onPermissionCallback = new OnPermissionCallback() {
        @Override
        public void onGranted(List<String> permissions, boolean all) {

        }
    };

    //保存
    @Override
    public void onRightClick(View view) {
        if(userInfoData == null)
            return;
        String nicNameStr = personalNameTv.getText().toString();
        String userDesc = personalDescContentTv.getText().toString();
        userInfoData.setIntroduce(userDesc);
        userInfoData.setNickname(nicNameStr);
        String jsonStr = new Gson().toJson(userInfoData);

        loginPresent.updatePresentUserInfo(this,new GetLoginInfoApi(),jsonStr,0x02);

    }

    @Override
    public void not200CodeMsg(String msg) {
        hideDialog();
        ToastUtils.show(msg);
    }

    @Override
    public void onSuccessData(Object data, int tagCode) {
        hideDialog();
        if(data == null)
            return;
        if(tagCode == 0x00){
            userInfoData = (GetLoginInfoApi.UserInfoData) data;
            MmkvUtils.saveUserInfoData(userInfoData);
            showUserData();
        }

        if(tagCode == 0x01){    //获取osstoken
            ossTokenBean = (GetOssApi.OssTokenBean) data;
            aliyunManager = new AliyunManager(ossTokenBean.getBucket(),ossTokenBean.getAccessId(),
                    ossTokenBean.getAccessKey(),ossTokenBean.getSecurityToken(),"head_img.png",callback);
        }

        if(tagCode == 0x02){
            ToastUtils.show("保存成功");
            MmkvUtils.saveUserInfoData(userInfoData);
        }
    }

    @Override
    public void onFailedData(String error) {
        hideDialog();
        ToastUtils.show(error);
    }


    //显示用户信息
    private void showUserData(){
        try {
            //头像
            String headImgUrl = userInfoData.getAvatar();
            //背景头像
            String bgUrl = userInfoData.getBackgroundUrl();

            if(!BikeUtils.isEmpty(headImgUrl)){
                // 显示圆角的 ImageView
                GlideApp.with(this)
                        .load(headImgUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners((int) getResources().getDimension(R.dimen.dp_10))))
                        .into(headImgView);
            }else {
                // 显示圆角的 ImageView
                GlideApp.with(this)
                        .load(R.drawable.ic_launcher_background)
                        .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners((int) getResources().getDimension(R.dimen.dp_10))))
                        .into(headImgView);
            }
           if(!BikeUtils.isEmpty(bgUrl)){
               GlideApp.with(this).load(bgUrl).placeholder(R.drawable.ic_user_top_bg).into(personalTopImgView);
           }else{
               GlideApp.with(this).load(R.drawable.ic_user_top_bg).into(personalTopImgView);
           }

            if(countryItemBeanList != null){
                Timber.e("------country="+countryItemBeanList.size());
                CountryApi.CountryItemBean countryItemBean = countryItemBeanList.get(userInfoData.getCountryId()-1832);
                if(countryItemBean != null){
                    personalCountryTv.setText(countryItemBean.getLabelName()+"");
                }
            } else {
                personalCountryTv.setText("安道尔");
            }
            personalNameTv.setText(userInfoData.getNickname());
            personalSexTv.setText(userInfoData.getGender() == 0 ? "男" : "女");

            Timber.e("-----生日="+BikeUtils.getFormatDate(userInfoData.getBirthday(),"yyyy-MM-dd")+"\n"+userInfoData.getBirthday());

            personalBirthTv.setText(BikeUtils.getFormatDate(userInfoData.getBirthday(),"yyyy-MM-dd"));
            personalHeightTv.setText(userInfoData.getHeight()+" cm");
            personalWeightTv.setText(userInfoData.getWeight()+" kg");
            personalDescContentTv.setText(userInfoData.getIntroduce());
            descNumberTv.setText(userInfoData.getIntroduce().length()+"/200");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private final AliyunManager.callback callback = new AliyunManager.callback() {
        @Override
        public void upLoadSuccess(int type, String imgPath) {
            Timber.e("------上传成功="+type+"--="+imgPath);
            if(type == 1){  //头像
                userInfoData.setAvatar(imgPath);
            }

            if(type == 0){  //背景
                userInfoData.setBackgroundUrl(imgPath);
            }

        }

        @Override
        public void upLoadFailed(String error) {
            Timber.e("------上传失败="+error);
        }

        @Override
        public void upLoadProgress(long currentSize, long totalSize) {
            Timber.e("------上传进度="+currentSize+"--="+totalSize);
        }
    };

}

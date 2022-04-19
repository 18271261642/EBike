package com.bonlala.ebike.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonlala.base.BaseDialog;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.dialog.PrivacyDialogView;
import com.bonlala.ebike.dialog.ShowPrivacyDialogView;
import com.bonlala.ebike.http.HttpData;
import com.bonlala.ebike.http.api.PrivacyBeanApi;
import com.bonlala.ebike.http.api.user.GetLoginInfoApi;
import com.bonlala.ebike.http.api.user.LoginApi;
import com.bonlala.ebike.http.api.user.LoginPresent;
import com.bonlala.ebike.http.api.user.LoginView;
import com.bonlala.ebike.ui.HomeActivity;
import com.bonlala.ebike.utils.BikeUtils;
import com.bonlala.ebike.utils.MmkvUtils;
import com.bonlala.widget.view.ClearEditText;
import com.bonlala.widget.view.CountdownView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;

import androidx.appcompat.app.AlertDialog;
import timber.log.Timber;


/**
 * 登录页面
 * Created by Admin
 * Date 2022/3/25
 */
public class LoginActivity extends AppActivity implements View.OnClickListener, LoginView {

    private static final String TAG = "LoginActivity";

    //手机号登录
    private TextView phoneTv,mailTv;
    //验证码
    private CountdownView mCountdownView;
    //手机号输入框
    private ClearEditText accountEdit;
    //验证码输入框
    private ClearEditText verticalCodeEdit;

    //隐私协议
    private TextView loginPrivacyTv;
    //微信登录
    private LinearLayout loginThirdWxLayout;

    private TextView loginLoginTv;

    private TextView loginTypeTv;

    private boolean isPhoneType = true;

    //CheckBox
    private CheckBox privacyCheckBox;


    private LoginPresent loginPresent;


    private ShowPrivacyDialogView privacyDialogView;
    private AlertDialog.Builder serviceDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String tokenStr = (String) MmkvUtils.getSaveParams(MmkvUtils.TOKEN_KEY,"");
        if(!BikeUtils.isEmpty(tokenStr)){
            startActivity(HomeActivity.class);
            finish();
        }
    }

    @Override
    protected int getLayoutId() {

        return R.layout.activity_login_layout;
    }

    @Override
    protected void initView() {
        privacyCheckBox = findViewById(R.id.privacyCheckBox);
        loginPrivacyTv = findViewById(R.id.loginPrivacyTv);
        loginLoginTv = findViewById(R.id.loginLoginTv);
        phoneTv = findViewById(R.id.loginPhoneTv);
        mailTv = findViewById(R.id.loginMailTv);
        mCountdownView = findViewById(R.id.loginGetPhoneCodeTv);
        accountEdit = findViewById(R.id.loginAccountEdit);
        verticalCodeEdit = findViewById(R.id.loginVerCodeEdit);
        loginTypeTv = findViewById(R.id.loginTypeTv);
        loginThirdWxLayout = findViewById(R.id.loginThirdWxLayout);


        setOnClickListener(phoneTv,mailTv,mCountdownView,loginLoginTv,loginThirdWxLayout,loginPrivacyTv);

        privacyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                privacyCheckBox.setChecked(b);
            }
        });

        loginPrivacyTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showService();
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        setAccountType(true);
        loginPresent = new LoginPresent();
        loginPresent.attachView(this);

        boolean isAgree = MmkvUtils.getPrivacy();
        if(!isAgree)
         showPrivacyView();
    }



    private void showPrivacyView(){
        if(privacyDialogView == null)
            privacyDialogView = new ShowPrivacyDialogView(this,R.style.edit_AlertDialog_style,LoginActivity.this);
        privacyDialogView.show();
        privacyDialogView.setOnPrivacyClickListener(new ShowPrivacyDialogView.OnPrivacyClickListener() {
            @Override
            public void onCancelClick() {
                MmkvUtils.setIsAgreePrivacy(false);
                privacyDialogView.dismiss();
                finish();
            }

            @Override
            public void onConfirmClick() {
                privacyDialogView.dismiss();
                MmkvUtils.setIsAgreePrivacy(true);
            }
        });
    }




    @Override
    public void onClick(View view) {
        if(view == loginPrivacyTv){
            startActivity(PrivacyActivity.class);
        }
        if(view == phoneTv){
           setAccountType(true);
        }
        if(view == mailTv){
            setAccountType(false);
        }

        if(view == mCountdownView){
            String accountStr = accountEdit.getText().toString();
            if(TextUtils.isEmpty(accountStr))
                return;
            if(isPhoneType){
                boolean isSucc = BikeUtils.isValidPhone(accountStr);
                if(!isSucc){
                    ToastUtils.show("请输入正确的手机号码!");
                    return;
                }
            }else{
                boolean isMail = BikeUtils.isValidEmail(accountStr);
                if(!isMail){
                    ToastUtils.show("请输入正确的邮箱!");
                    return;
                }
            }

            loginPresent.getLoginVerifyCode(this, new LoginApi().getVerifyCode(isPhoneType ? 0 : 1,accountStr),0);
        }


        //登录
        if(view == loginLoginTv){
            String accountStr = accountEdit.getText().toString();
            String verfifyCode = verticalCodeEdit.getText().toString();
            if(TextUtils.isEmpty(accountStr) || TextUtils.isEmpty(verfifyCode))
                return;
            if(!privacyCheckBox.isChecked()){
                ToastUtils.show("请同意隐私政策!");
                return;
            }


            if(isPhoneType){
                boolean isSucc = BikeUtils.isValidPhone(accountStr);
                if(!isSucc){
                    ToastUtils.show("请输入正确的手机号码!");
                    return;
                }
            }else{
                boolean isMail = BikeUtils.isValidEmail(accountStr);
                if(!isMail){
                    ToastUtils.show("请输入正确的邮箱!");
                    return;
                }
            }

            loginPresent.loginByAccountType(this,new LoginApi().setLoginType(isPhoneType ? 0 : 1,accountStr,Integer.parseInt(verfifyCode)),1);
        }

        if(view == loginThirdWxLayout){
            loginPresent.getLoginInfoData(this,new GetLoginInfoApi(),2);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setAccountType(boolean isPhone){
        accountEdit.setText("");
        isPhoneType = isPhone;
        loginTypeTv.setText(isPhone ? "中国+86" : "邮箱");
        accountEdit.setHint(isPhone ? "请输入手机号" : "请输入邮箱");

        accountEdit.setInputType(isPhoneType ? 3 : 0x00000031);

        phoneTv.setBackground(getResources().getDrawable(isPhone ? R.drawable.login_phone_checked : R.drawable.login_normal));
        mailTv.setBackground(getResources().getDrawable(isPhone ? R.drawable.login_normal : R.drawable.login_phone_checked));
    }

    private void showFirstPrivacy(){
        new BaseDialog.Builder<>(this)
                .setThemeStyle(R.style.edit_AlertDialog_style)
                .setContentView(R.layout.dialog_privacy_view)
                .setGravity(Gravity.CENTER)
                .setOnClickListener(R.id.privacyDialogSureTv, (dialog, view1) -> {
                    dialog.dismiss();
                })
                .create().show();

    }


    @Override
    public void onSuccessData(Object data, int tagCode) {
        if(data == null)
            return;
        Timber.e("-----data="+data.toString());
        if(tagCode == 0){
            ToastUtils.show(data.toString());
            mCountdownView.start();
        }

        if(tagCode == 1){
            LoginApi.LoginTokenBean loginTokenBean = (LoginApi.LoginTokenBean) data;
            Timber.e("------登录taoken="+loginTokenBean.toString());
            MmkvUtils.setSaveParams(MmkvUtils.TOKEN_KEY,loginTokenBean.getAppAuthToken());
            AppApplication.setHttpToken();
            if(loginTokenBean.getStatus() == 0){
                startActivity(SetUserInfoActivity.class);
            }else{
                startActivity(HomeActivity.class);
            }

            finish();
        }

    }

    @Override
    public void onFailedData(String error) {

    }

    @Override
    public void not200CodeMsg(String msg) {
        ToastUtils.show(msg);
    }


    private void showService(){
        serviceDialog = new AlertDialog.Builder(this)
                .setTitle("选择网络环境")
                .setItems(new CharSequence[]{"本地环境", "预发布环境"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppApplication.getInstance().setLocalService(i == 0);
                        dialogInterface.dismiss();
                    }
                });
        serviceDialog.create().show();
    }


}

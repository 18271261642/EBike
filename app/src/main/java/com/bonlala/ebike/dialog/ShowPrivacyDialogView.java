package com.bonlala.ebike.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import com.bonlala.ebike.R;
import com.bonlala.ebike.http.HttpData;
import com.bonlala.ebike.http.api.PrivacyBeanApi;
import com.bonlala.ebike.ui.activity.BrowserActivity;
import com.bonlala.ebike.ui.activity.PrivacyActivity;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.shape.view.ShapeButton;

import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.LifecycleOwner;


/**
 * 展示隐私政策和用户协议的dialog
 * Created by Admin
 * Date 2021/10/12
 */
public class ShowPrivacyDialogView extends AppCompatDialog implements View.OnClickListener {


    private TextView userAgreementTv;
    private TextView privacyTv;

    private ShapeButton privacyCancelBtn;
    private ShapeButton privacyConfirmBtn;

    private Activity activity;


    private OnPrivacyClickListener onPrivacyClickListener;

    public ShowPrivacyDialogView(Context context) {
        super(context);
    }

    public ShowPrivacyDialogView(Context context, int theme,Activity ac) {
        super(context, theme);
        this.activity = ac;
    }

    public void setOnPrivacyClickListener(OnPrivacyClickListener onPrivacyClickListener) {
        this.onPrivacyClickListener = onPrivacyClickListener;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.show_privacy_dialog_view,null);
        view.setMinimumWidth((int) (getWindow().getWindowManager()
                        .getDefaultDisplay().getWidth() * 0.8));
        setContentView(view);

        initViews();

    }

    private void initViews() {
        userAgreementTv = findViewById(R.id.userAgreementTv);
        privacyTv = findViewById(R.id.privacyTv);
        privacyCancelBtn = findViewById(R.id.privacyCancelBtn);
        privacyConfirmBtn = findViewById(R.id.privacyConfirmBtn);

        userAgreementTv.setOnClickListener(this);
        privacyTv.setOnClickListener(this);
        privacyCancelBtn.setOnClickListener(this);
        privacyConfirmBtn.setOnClickListener(this);


    }

    //隐私政策
    public void startPrivacyActivity() {
        getNetUrl(true);

    }

    //用户协议
    public void startAgreementActivity() {
        getNetUrl(false);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.userAgreementTv){   //用户协议
            startAgreementActivity();
        }
        if(view.getId() == R.id.privacyTv){     //隐私政策
            startPrivacyActivity();

        }

        if(view.getId() == R.id.privacyCancelBtn){
            if(onPrivacyClickListener != null)
                onPrivacyClickListener.onCancelClick();
        }

        if(view.getId() == R.id.privacyConfirmBtn){
            if(onPrivacyClickListener != null)
                onPrivacyClickListener.onConfirmClick();
        }
    }


    public interface OnPrivacyClickListener{
        void onCancelClick();
        void onConfirmClick();
    }


    private void getNetUrl(boolean isPrivacy){
        EasyHttp.get((LifecycleOwner) activity).api(new PrivacyBeanApi().getApi()).request(new OnHttpListener<HttpData<PrivacyBeanApi.DataBean>>() {


            @Override
            public void onSucceed(HttpData<PrivacyBeanApi.DataBean> result) {
                BrowserActivity.start(getContext(),isPrivacy ? result.getData().getPrivacyAgreement() : result.getData().getUserAgreement(),isPrivacy ? "隐私政策" : "用户协议");
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }
}

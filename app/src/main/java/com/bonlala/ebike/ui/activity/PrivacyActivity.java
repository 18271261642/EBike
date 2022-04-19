package com.bonlala.ebike.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.http.HttpData;
import com.bonlala.ebike.http.api.PrivacyBeanApi;
import com.bonlala.ebike.http.net.CommNetView;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.http.model.CacheMode;
import com.hjq.shape.view.ShapeTextView;

import timber.log.Timber;

/**
 * 隐私政策查看
 * Created by Admin
 * Date 2022/3/26
 */
public class PrivacyActivity extends AppActivity  {


    private WebView privacyWebView;
    //隐私政策
    private ShapeTextView privacyPrivacyTv;
    //用户协议
    private ShapeTextView privacyUserAgreementTv;





    @Override
    protected int getLayoutId() {
        return R.layout.activity_privacy_layout;
    }

    @Override
    protected void initView() {
        privacyWebView = findViewById(R.id.privacyWebView);
        privacyPrivacyTv = findViewById(R.id.privacyPrivacyTv);
        privacyUserAgreementTv = findViewById(R.id.privacyUserAgreementTv);

        setOnClickListener(privacyPrivacyTv,privacyUserAgreementTv);

        WebSettings webSettings = privacyWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptEnabled(true);


    }

    @Override
    protected void initData() {
        setPrivacyType(false);
    }


    @Override
    public void onClick(View view) {
        if(view == privacyPrivacyTv){
            setPrivacyType(true);
        }

        if(view == privacyUserAgreementTv){
            setPrivacyType(false);
        }
    }

    private void setPrivacyType(boolean isPrivacy){
        privacyPrivacyTv.getShapeDrawableBuilder().setSolidColor(isPrivacy ? Color.parseColor("#30D158") : Color.WHITE).intoBackground();
        privacyPrivacyTv.setTextColor(isPrivacy ? Color.WHITE : Color.parseColor("#1C1C1E"));

        privacyUserAgreementTv.getShapeDrawableBuilder().setSolidColor(!isPrivacy ? Color.parseColor("#30D158") : Color.WHITE).intoBackground();
        privacyUserAgreementTv.setTextColor(!isPrivacy ? Color.WHITE : Color.parseColor("#1C1C1E"));

        getNetUrl(isPrivacy);

    }


    private void getNetUrl(boolean isPrivacy){
        EasyHttp.get(this).api(new PrivacyBeanApi().getApi()).request(new OnHttpListener<HttpData<PrivacyBeanApi.DataBean>>() {


            @Override
            public void onSucceed(HttpData<PrivacyBeanApi.DataBean> result) {
                privacyWebView.loadUrl(isPrivacy ? result.getData().getPrivacyAgreement() : result.getData().getUserAgreement());
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

}

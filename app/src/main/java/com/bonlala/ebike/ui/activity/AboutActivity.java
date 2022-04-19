package com.bonlala.ebike.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.http.GlideApp;
import com.bonlala.ebike.manager.ActivityManager;
import com.bonlala.ebike.manager.CacheDataManager;
import com.bonlala.ebike.manager.ThreadPoolManager;
import com.bonlala.ebike.utils.MmkvUtils;
import com.hjq.toast.ToastUtils;

/**
 * 关于页面
 * Created by Admin
 * Date 2022/3/31
 */
public class AboutActivity extends AppActivity {


    //缓存大小
    private TextView aboutCacheTv;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_layout;
    }

    @Override
    protected void initView() {
        aboutCacheTv = findViewById(R.id.aboutCacheTv);

        findViewById(R.id.aboutVersionLayout);
        findViewById(R.id.aboutClearLayout);
        findViewById(R.id.aboutLoginOutTv);


        setOnClickListener(R.id.aboutVersionLayout,R.id.aboutClearLayout,R.id.aboutLoginOutTv);

    }

    @Override
    protected void initData() {
        aboutCacheTv.setText("缓存: "+CacheDataManager.getTotalCacheSize(getActivity())+"b");

    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.aboutClearLayout){
            // 清除内存缓存（必须在主线程）
            GlideApp.get(getActivity()).clearMemory();
            ThreadPoolManager.getInstance().execute(() -> {
                CacheDataManager.clearAllCache(this);
                // 清除本地缓存（必须在子线程）
                GlideApp.get(getActivity()).clearDiskCache();
                post(() -> {
                    // 重新获取应用缓存大小
                    aboutCacheTv.setText("缓存: "+CacheDataManager.getTotalCacheSize(getActivity()));
                });
            });
        }

        if(view.getId() == R.id.aboutVersionLayout){
            ToastUtils.show("已是最新版版本!");
        }

        if(view.getId() == R.id.aboutLoginOutTv){
            logout();
        }
    }


    private void logout(){

        MmkvUtils.setSaveParams(MmkvUtils.TOKEN_KEY,"");
        startActivity(LoginActivity.class);
        ActivityManager.getInstance().finishAllActivities();
        finish();
    }
}

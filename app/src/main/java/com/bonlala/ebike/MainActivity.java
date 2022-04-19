package com.bonlala.ebike;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.ui.HomeActivity;

public class MainActivity extends AppActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        String root = getFilesDir().getAbsolutePath() ;

        Log.e("TAG","---path="+root);

        startActivity(HomeActivity.class);
    }
}
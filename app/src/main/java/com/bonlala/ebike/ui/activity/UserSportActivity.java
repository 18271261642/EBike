package com.bonlala.ebike.ui.activity;

import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.ui.fragment.UserBasicFragment;
import com.bonlala.ebike.ui.fragment.UserSportFragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 运动信息
 * Created by Admin
 * Date 2022/3/31
 */
public class UserSportActivity extends AppActivity {


    private FragmentManager fragmentManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_sport_layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.userSportContentLayout, UserSportFragment.getInstance());
        fragmentTransaction.commit();
    }
}

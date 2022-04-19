package com.bonlala.ebike.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.bonlala.ebike.R;
import com.bonlala.ebike.app.TitleBarFragment;
import com.bonlala.ebike.ui.activity.UserSportActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Admin
 * Date 2022/3/31
 */
public class UserSportFragment extends TitleBarFragment<UserSportActivity> {

    //踏频fragment的tag
    private static final String TREAD_FRAGMENT_TAG = "tread_fragment_tag";
    //心率区间的tag
    private static final String HEART_FRAGMENT_TAG = "heart_fragment_tag";


    public static UserSportFragment getInstance(){
        return new UserSportFragment();
    }


    FragmentManager fragmentManager ;
    FragmentTransaction fragmentTransaction;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_sport_layout;
    }

    @Override
    protected void initView() {
        findViewById(R.id.userSportHeartLayout);
        findViewById(R.id.userSportPowerLayout);
        findViewById(R.id.userSportTreadLayout);



        setOnClickListener(R.id.userSportHeartLayout,R.id.userSportPowerLayout,R.id.userSportTreadLayout);


    }

    @Override
    protected void initData() {
        fragmentManager = getParentFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    @Override
    protected boolean isStatusBarDarkFont() {
        return super.isStatusBarDarkFont();
    }


    @Override
    public void onLeftClick(View view) {
        getAttachActivity().finish();
    }

    @Override
    public void onClick(View view) {

        Bundle bundle = new Bundle();
        bundle.putInt("bg_status",1);


        if(view.getId() == R.id.userSportHeartLayout){  //心率区间
            fragmentManager = getParentFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            UserHeartFragment userHeartFragment = UserHeartFragment.getInstance();
            userHeartFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.userSportContentLayout,userHeartFragment,HEART_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        if(view.getId() == R.id.userSportPowerLayout){
            fragmentManager = getParentFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            UserPowerFragment userPowerFragment = UserPowerFragment.getInstance();
            userPowerFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.userSportContentLayout,userPowerFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        if(view.getId() == R.id.userSportTreadLayout){      //踏频
            fragmentManager = getParentFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            UserTreadFragment userTreadFragment = UserTreadFragment.getInstance();
            userTreadFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.userSportContentLayout,userTreadFragment,TREAD_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}

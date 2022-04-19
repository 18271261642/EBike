package com.bonlala.ebike.ui.fragment;

import android.view.View;

import com.bonlala.ebike.R;
import com.bonlala.ebike.app.TitleBarFragment;
import com.bonlala.ebike.ui.HomeActivity;
import com.bonlala.ebike.ui.activity.SetUserInfoActivity;
import com.hjq.bar.OnTitleBarListener;

/**
 * Created by Admin
 * Date 2022/3/30
 */
public class UserBasicCompleteFragment extends TitleBarFragment<SetUserInfoActivity> {


    public static UserBasicCompleteFragment getInstance(){
        return new UserBasicCompleteFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_basic_over_layout;
    }

    @Override
    protected void initView() {

        findViewById(R.id.userBasicSetOverTv);


        setOnClickListener(R.id.userBasicSetOverTv);

    }

    @Override
    protected void initData() {

    }


    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.userBasicSetOverTv){
            startActivity(HomeActivity.class);
            getActivity().finish();
        }

    }


    @Override
    public void onLeftClick(View view) {
        getParentFragmentManager().popBackStack();
    }
}

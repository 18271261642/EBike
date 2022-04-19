package com.bonlala.ebike.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bonlala.ebike.R;
import com.bonlala.ebike.app.TitleBarFragment;
import com.bonlala.ebike.ui.activity.SetUserInfoActivity;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.shape.layout.ShapeConstraintLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 设置心率区间
 * Created by Admin
 * Date 2022/3/28
 */
public class UserHeartFragment extends TitleBarFragment<SetUserInfoActivity> {

    //踏频fragment的tag
    private static final String TREAD_FRAGMENT_TAG = "tread_fragment_tag";


    public static UserHeartFragment getInstance(){
        return new UserHeartFragment();
    }


    private LinearLayout userHeartParentLayout;
    private ShapeConstraintLayout userHeartMaxLayout, userHeart6Layout,userHeart5Layout,userHeart4Layout,
            userHeart3Layout,userHeart2Layout,userHeart1Layout;

    private ImageView userHeartNextImg;



    //标题栏背景及字体颜色
    int[] titleColorBg = new int[]{R.color.transparent,R.color.white};
    int[] titleLeftIcon = new int[]{R.drawable.icon_comm_white_back,R.drawable.ic_title_bar_black_back};
    //最大心率区间
    int[] itemMaxColor = new int[]{Color.parseColor("#1affffff"),Color.parseColor("#FF1B82")};



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_heart_layout;
    }

    @Override
    protected void initView() {
        userHeartNextImg = findViewById(R.id.userHeartNextImg);

        userHeartParentLayout = findViewById(R.id.userHeartParentLayout);
        userHeartMaxLayout = findViewById(R.id.userHeartMaxLayout);
        userHeart6Layout = findViewById(R.id.userHeart6Layout);
        userHeart5Layout = findViewById(R.id.userHeart5Layout);
        userHeart4Layout = findViewById(R.id.userHeart4Layout);
        userHeart3Layout = findViewById(R.id.userHeart3Layout);
        userHeart2Layout = findViewById(R.id.userHeart2Layout);
        userHeart1Layout = findViewById(R.id.userHeart1Layout);



       // userHeartParentLayout.getBackground().setAlpha(50);

        setOnClickListener(userHeartNextImg);


    }

    @Override
    protected void initData() {

        assert getArguments() != null;
        int statusCode = getArguments().getInt("bg_status");
        if(statusCode == 0){
            userHeartParentLayout.setBackground(getResources().getDrawable(R.drawable.login_bg_shap));
        }else{
            userHeartParentLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }

        if(getTitleBar() == null)
            return;
        getTitleBar().setTitleColor(statusCode == 0 ? Color.WHITE : Color.parseColor("#3A3A3C"));
        getTitleBar().setBackgroundColor(statusCode == 0 ? getResources().getColor(R.color.transparent) : getResources().getColor(R.color.white));
        getTitleBar().setLeftIcon(getResources().getDrawable(titleLeftIcon[statusCode]));

        userHeartMaxLayout.getShapeDrawableBuilder().setAngle(50).intoBackground();
        userHeart6Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
        userHeart5Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
        userHeart4Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
        userHeart3Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
        userHeart2Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
        userHeart1Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();

    }

    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }


    @Override
    public void onClick(View view) {
        if(view == userHeartNextImg){
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            UserTreadFragment userTreadFragment = UserTreadFragment.getInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("bg_status",0);
            userTreadFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.userFragmentLayout,userTreadFragment,TREAD_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onLeftClick(View view) {
        getParentFragmentManager().popBackStack();
    }
}

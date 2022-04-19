package com.bonlala.ebike.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bonlala.ebike.R;
import com.bonlala.ebike.app.TitleBarFragment;
import com.bonlala.ebike.ui.activity.SetUserInfoActivity;
import com.hjq.shape.layout.ShapeConstraintLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 设置踏频率
 * Created by Admin
 * Date 2022/3/28
 */
public class UserTreadFragment extends TitleBarFragment<SetUserInfoActivity> {


    public static UserTreadFragment getInstance(){
        return new UserTreadFragment();
    }

    private ImageView userTreadNextImg;
    private LinearLayout treadParentLayout;

    private ShapeConstraintLayout serTread4Layout,serTread3Layout,
            serTread2Layout,serTread1Layout;



    //标题栏背景及字体颜色
    int[] titleColorBg = new int[]{R.color.transparent,R.color.white};
    int[] titleLeftIcon = new int[]{R.drawable.icon_comm_white_back,R.drawable.ic_title_bar_black_back};

    //区域4背景
    int[] item4Color = new int[]{Color.parseColor("#80ff453a"),Color.parseColor("#FF453A")};
    //区域3背景
    int[] item3Color = new int[]{Color.parseColor("#80ff9f0a"),Color.parseColor("#FF9F0A")};
    //区域2背景
    int[] item2Color = new int[]{Color.parseColor("#80ffd60a"),Color.parseColor("#FFD60A")};
    //区域1背景
    int[] item1Color = new int[]{Color.parseColor("#80ac8e68"),Color.parseColor("#AC8E68")};


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_tread_layout;
    }

    @Override
    protected void initView() {
        findViewById(R.id.userTreadNextImg);

        treadParentLayout = findViewById(R.id.treadParentLayout);
        userTreadNextImg = findViewById(R.id.userTreadNextImg);
        serTread4Layout = findViewById(R.id.serTread4Layout);
        serTread3Layout = findViewById(R.id.serTread3Layout);
        serTread2Layout = findViewById(R.id.serTread2Layout);
        serTread1Layout = findViewById(R.id.serTread1Layout);



        setOnClickListener(userTreadNextImg,serTread4Layout,serTread4Layout,
                serTread4Layout,serTread4Layout);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if(bundle == null)
            return;
        int bgStatus = bundle.getInt("bg_status");
        if(bgStatus == 0){
            treadParentLayout.setBackground(getResources().getDrawable(R.drawable.login_bg_shap));
        }else{
            treadParentLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }


        //0是从个人信息设置中过来的，1是设置中来的
        if(getTitleBar() == null)
            return;
        getTitleBar().setBackgroundColor(getColor(titleColorBg[bgStatus]));
        getTitleBar().setTitleColor(bgStatus == 0 ? Color.WHITE : Color.parseColor("#3A3A3C"));
        getTitleBar().setLeftIcon(getResources().getDrawable(titleLeftIcon[bgStatus]));
        if(bgStatus == 0){
            serTread4Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
            serTread3Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
            serTread2Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
            serTread1Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onLeftClick(View view) {
        getParentFragmentManager().popBackStack();
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.userTreadNextImg){
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.userFragmentLayout,UserBasicCompleteFragment.getInstance());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}

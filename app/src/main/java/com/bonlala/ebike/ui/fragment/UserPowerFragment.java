package com.bonlala.ebike.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonlala.base.BaseFragment;
import com.bonlala.ebike.R;
import com.bonlala.ebike.action.TitleBarAction;
import com.bonlala.ebike.app.TitleBarFragment;
import com.bonlala.ebike.dialog.HeightSelectDialog;
import com.bonlala.ebike.dialog.SinalDialogVIew;
import com.bonlala.ebike.http.api.user.InitUserInfoApi;
import com.bonlala.ebike.ui.activity.SetUserInfoActivity;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.shape.layout.ShapeConstraintLayout;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 用户功率设置
 * Created by Admin
 * Date 2022/3/26
 */
public class UserPowerFragment extends TitleBarFragment<SetUserInfoActivity>  {

    private ImageView userPowerNextImg;


    //功率阈值
    private TextView userPowerMaxValue;

    private HeightSelectDialog.Builder heightSelectDialog;

    public static UserPowerFragment getInstance(){
        return new UserPowerFragment();
    }

    int[] titleLeftIcon = new int[]{R.drawable.icon_comm_white_back,R.drawable.ic_title_bar_black_back};
    private LinearLayout userPowerParentLayout;

    private ShapeConstraintLayout userPowerMaxLayout,userPower7Layout,userPower6Layout,
            userPower5Layout,userPower4Layout,userPower3Layout,userPower2Layout,userPower1Layout;




    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_power_layout;
    }

    @Override
    protected void initView() {

        userPowerMaxValue = findViewById(R.id.userPowerMaxValue);
        userPowerNextImg = findViewById(R.id.userPowerNextImg);

        userPowerParentLayout = findViewById(R.id.userPowerParentLayout);
        userPowerMaxLayout = findViewById(R.id.userPowerMaxLayout);
        userPower7Layout = findViewById(R.id.userPower7Layout);
        userPower6Layout = findViewById(R.id.userPower6Layout);
        userPower5Layout = findViewById(R.id.userPower5Layout);
        userPower4Layout = findViewById(R.id.userPower4Layout);
        userPower3Layout = findViewById(R.id.userPower3Layout);
        userPower2Layout = findViewById(R.id.userPower2Layout);
        userPower1Layout = findViewById(R.id.userPower1Layout);


        setOnClickListener(userPowerNextImg,userPowerMaxLayout);

    }

    @Override
    protected void initData() {
        assert getArguments() != null;
        int statusCode = getArguments().getInt("bg_status");
        if(statusCode == 0){
            userPowerParentLayout.setBackground(getResources().getDrawable(R.drawable.login_bg_shap));

            userPowerMaxLayout.getBackground().setAlpha(50);
            userPower7Layout.getBackground().setAlpha(50);
            userPower6Layout.getBackground().setAlpha(50);
            userPower5Layout.getBackground().setAlpha(50);
            userPower4Layout.getBackground().setAlpha(50);
            userPower3Layout.getBackground().setAlpha(50);
            userPower2Layout.getBackground().setAlpha(50);
            userPower1Layout.getBackground().setAlpha(50);


//            userPowerMaxLayout.getShapeDrawableBuilder().setSolidColor(Color.parseColor("#FF1B82")).setAngle(50).intoBackground();
//            userPower7Layout.getShapeDrawableBuilder().setSolidColor(Color.parseColor("#FF453A")).setAngle(50).intoBackground();
//            userPower6Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
//            userPower5Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
//            userPower4Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
//            userPower3Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
//            userPower2Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();
//            userPower1Layout.getShapeDrawableBuilder().setAngle(50).intoBackground();

        }else{
            userPowerParentLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }

        if(getTitleBar() == null)
            return;
        getTitleBar().setTitleColor(statusCode == 0 ? Color.WHITE : Color.parseColor("#3A3A3C"));
        getTitleBar().setBackgroundColor(statusCode == 0 ? getResources().getColor(R.color.transparent) : getResources().getColor(R.color.white));
        getTitleBar().setLeftIcon(getResources().getDrawable(titleLeftIcon[statusCode]));


        if(statusCode == 0){
            InitUserInfoApi.UserInfoData userInfoData = getAttachActivity().getUserInfoData();
            if(userInfoData == null)
                return;



        }

    }

    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }


    @Override
    public void onClick(View view) {
        if(view == userPowerNextImg){
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            UserHeartFragment heartFragment = UserHeartFragment.getInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("bg_status",0);
            heartFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.userFragmentLayout,heartFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        //功率阈值
        if(view == userPowerMaxLayout){
            showSingleView();
        }




    }


    @Override
    public void onLeftClick(View view) {
        getParentFragmentManager().popBackStack();
    }

    private void showSingleView(){
        List<String> list = new ArrayList<>();
        for(int i = 100;i<=500;i++){
            list.add(String.valueOf(i));
        }
       heightSelectDialog = new HeightSelectDialog.Builder(getAttachActivity());
        Objects.requireNonNull(heightSelectDialog.setUnitShow(true, "w"))
                .setSelectItemColor(false)
                .setSourceData(list)
                .setSignalSelectListener(new HeightSelectDialog.SignalSelectListener() {
                    @Override
                    public void onSignalSelect(String txt) {
                        userPowerMaxValue.setText(txt+"");
                    }
                }).create().show();

    }

}

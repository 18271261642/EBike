package com.bonlala.ebike.ui.dashboard;



import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.TitleBarFragment;
import com.bonlala.ebike.http.GlideApp;
import com.bonlala.ebike.http.api.user.GetLoginInfoApi;
import com.bonlala.ebike.ui.HomeActivity;
import com.bonlala.ebike.ui.activity.AboutActivity;
import com.bonlala.ebike.ui.activity.PersonalActivity;
import com.bonlala.ebike.ui.activity.PrivacyActivity;
import com.bonlala.ebike.ui.activity.UserSportActivity;
import com.bonlala.ebike.ui.ble.ShowLogActivity;
import com.bonlala.ebike.utils.MmkvUtils;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.gson.Gson;

import timber.log.Timber;

/**
 * 底部菜单，我的页面
 */
public class DashboardFragment extends TitleBarFragment<HomeActivity> {


    private LinearLayout mineTopLayout;

    //圆角头像
    private ImageView headImgView;
    //昵称
    private TextView userNicTv;
    //个人简介
    private TextView userDescTv;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dashboard;
    }

    @Override
    protected void initView() {
        mineTopLayout = findViewById(R.id.mineTopLayout);
        headImgView = findViewById(R.id.userMineHdImg);
        userNicTv = findViewById(R.id.userNicTv);
        userDescTv = findViewById(R.id.userDescTv);



        findViewById(R.id.mineAboutLayout);
        findViewById(R.id.mineAboutPrivacyLayout);
        findViewById(R.id.minePersonalLayout);
        findViewById(R.id.minePersonalHdLayout);
        findViewById(R.id.mineSportLayout);





        setOnClickListener(R.id.mineAboutLayout,R.id.mineAboutPrivacyLayout,
                R.id.minePersonalLayout,R.id.minePersonalHdLayout,R.id.mineSportLayout);

        // 显示圆角的 ImageView
        GlideApp.with(this)
                .load(R.drawable.ic_launcher_background)
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners((int) getResources().getDimension(R.dimen.dp_10))))
                .into(headImgView);
    }



    private void showUserInfoData(){
        GetLoginInfoApi.UserInfoData userInfoData = MmkvUtils.getUserInfoData();
        if(userInfoData == null)
            return;
        Timber.e("------用户信息="+new Gson().toJson(userInfoData));
        // 显示圆角的 ImageView
        GlideApp.with(this)
                .load(userInfoData.getAvatar())
                .placeholder(R.drawable.ic_launcher_background)
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners((int) getResources().getDimension(R.dimen.dp_10))))
                .into(headImgView);

        //昵称
        userNicTv.setText(userInfoData.getNickname()+"");
        //简介
        userDescTv.setText(userInfoData.getIntroduce()+"");
    }




    @Override
    public void onResume() {
        super.onResume();
        showUserInfoData();
    }

    @Override
    protected void initData() {

    }




    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.mineAboutLayout){
            startActivity(AboutActivity.class);
        }

        if(view.getId() == R.id.mineAboutPrivacyLayout){
            startActivity(PrivacyActivity.class);
        }

        if(view.getId() == R.id.minePersonalLayout || view.getId() == R.id.minePersonalHdLayout){
            startActivity(PersonalActivity.class);
        }

        if(view.getId() == R.id.mineSportLayout){
            startActivity(UserSportActivity.class);
        }
    }
}